package com.abreqadhabra.nflight.application.service.network.rmi.helper;

import java.nio.file.Path;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.activation.Activatable;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;
import java.rmi.activation.ActivationID;
import java.rmi.activation.ActivationSystem;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.concurrent.thread.ThreadHelper;
import com.abreqadhabra.nflight.application.common.launcher.runtime.process.ProcessExecutor;
import com.abreqadhabra.nflight.application.service.network.rmi.conf.RMIServantConfig;
import com.abreqadhabra.nflight.application.service.network.rmi.exception.RMIServantException;
import com.abreqadhabra.nflight.common.Env;
import com.abreqadhabra.nflight.common.exception.NFlightException;
import com.abreqadhabra.nflight.common.exception.NFlightRemoteException;
import com.abreqadhabra.nflight.common.exception.UnexpectedRemoteException;
import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.IOStream;

public class ActivatableRMIServantHelper extends RMIServantHelper {
	private static Class<ActivatableRMIServantHelper> THIS_CLAZZ = ActivatableRMIServantHelper.class;
	private static String CLAZZ_NAME = THIS_CLAZZ.getName();
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static Remote install(String className, String codebase,
			String policyFile, MarshalledObject<Object> data)
			throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		try {
			Properties props = new Properties();
			props.put("java.security.policy", policyFile);

			// Create a new activation group descriptor
			ActivationGroupDesc groupDesc = new ActivationGroupDesc(props, null);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupDesc : " + groupDesc);

			// Once the ActivationGroupDesc has been created,
			// register it with the activation system to
			// obtain its ID
			ActivationGroupID groupID = ActivationGroup.getSystem()
					.registerGroup(groupDesc);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"Activation group descriptor registered.");

			// Now we need to explicitly create the group
			ActivationGroup.createGroup(groupID, groupDesc, 0);

			// Register the rmi object with rmid Auto restart
			ActivationDesc desc = new ActivationDesc(groupID, className,
					codebase, data, true);

			LOGGER.logp(Level.FINER, THIS_CLAZZ.getName(), METHOD_NAME,
					"groupID:" + groupID + "\nclassName : " + className
							+ "\ncodebase : " + codebase + "\ndata : " + data);

			// return a stub.
			return Activatable.register(desc);

		} catch (ActivationException | RemoteException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}

	public static void checkActivationSystem() throws NFlightRemoteException {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		try {
			// 액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵
			ActivationSystem activationSystem = ActivationGroup.getSystem();
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"액티베이션 시스템이 기동되어 있을 경우 RMID를 기동을 스킵 " + activationSystem);
		} catch (ActivationException ae) {
			try {
				// Port already in use: 1098 Address already in use: JVM_Bind
				// 액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작
				startActivationSystem();
				// 콜러블로 기동결과에 대한 값이 있을때까지 대기
				Thread.sleep(Config
						.getInt(RMIServantConfig.KEY_INT_RMI_ACTIVATABLE_RMID_DELAY_SECONDS));
			} catch (InterruptedException e) {
				throw new RMIServantException(e);
			}
			LOGGER.logp(Level.CONFIG, THIS_CLAZZ.getName(), METHOD_NAME,
					"액티베이션 시스템이 기동되어 있지 않을 경우 RMID 시작 ");
		}
	}

	public static void startActivationSystem() throws NFlightRemoteException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		// // 생성방식 정리 필요 Singleton
		// Configure configure = new ConfigureImpl(THIS_CLAZZ,
		// RMIServantConfiguration.FILE_SERVICE_PROPERTIES);

		Path codebasePath = IOStream.getCodebasePath(CLAZZ_NAME);
		Path path = codebasePath
				.resolve(RMIServantConfig.PATH_RMI_ACTIVATABLE_RMID_POLICY);

		String command = Config
				.get(RMIServantConfig.KEY_STR_RMI_ACTIVATABLE_RMID_START)
				+ " -J-D"
				+ Env.PROPERTIES_SYSTEM.JAVA_SECURITY_POLICY.toString()
				+ "="
				+ path.toString() + " -log rmid.log";
		// 메이븐 clean이 가능하도록 Common 경로에 로그 생성
		// + RMIServantConfiguration.CODE_BASE_PATH.resolve("rmid.log");

		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "command is "
				+ command);
		new Thread(getActivationRunnable(command)).start();
	}

	public static void stopActivationSystem() throws NFlightException,
			NFlightRemoteException {
		final Thread CURRENT_THREAD = Thread.currentThread();
		final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
				.getMethodName();

		String command = Config
				.get(RMIServantConfig.KEY_STR_RMI_ACTIVATABLE_RMID_STOP);
		LOGGER.logp(Level.FINER, CLAZZ_NAME, METHOD_NAME, "command is "
				+ command);
		new Thread(getActivationRunnable(command)).start();

		// try {
		// ActivationGroup.getSystem().shutdown();
		// } catch (RemoteException | ActivationException e) {
		// throw new RMIServantException(e);
		// }

	}

	static Runnable getActivationRunnable(String command) {
		return new Runnable() {
			private String command;
			private ProcessExecutor executor;
			public Runnable init(String command) {
				this.executor = new ProcessExecutor();
				this.command = command;
				return (this);
			}
			@Override
			public void run() {
				final Thread CURRENT_THREAD = Thread.currentThread();
				final String METHOD_NAME = CURRENT_THREAD.getStackTrace()[1]
						.getMethodName();
				LOGGER.logp(
						Level.FINER,
						CLAZZ_NAME,
						METHOD_NAME,
						"current thread is "
								+ ThreadHelper.getThreadName(CURRENT_THREAD));
				try {
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopping...");
					this.executor.execute(this.command);
					LOGGER.logp(Level.SEVERE, CLAZZ_NAME, METHOD_NAME,
							"Stopped");
				} catch (Exception e) {
					StackTraceElement[] current = e.getStackTrace();
					if (e instanceof NFlightException) {
						NFlightException ne = (NFlightException) e;
						LOGGER.logp(Level.SEVERE, current[0].getClassName(),
								current[0].getMethodName(), "\n"
										+ NFlightException.getStackTrace(ne));
						ThreadHelper.interrupt(CLAZZ_NAME, Thread.currentThread());
					} else {
						e.printStackTrace();
						ThreadHelper.shutdown();
					}
				}
			}
		}.init(command);
	}

	// The ActivatableShutdown interface implementation
	public static boolean uninstall(ActivationID activationID)
			throws NFlightRemoteException {
		return deactivate(true, activationID);
	}

	static boolean deactivate(boolean forever, ActivationID activationID)
			throws NFlightRemoteException {
		try {
			if (forever) {
				Activatable.unregister(activationID);
				return true;
			} else {
				return Activatable.inactive(activationID);
			}
		} catch (RemoteException | ActivationException e) {
			throw new RMIServantException(e);
		} catch (Exception e) {
			throw new UnexpectedRemoteException(e);
		}
	}
}
