package com.abreqadhabra.nflight.application.service.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.abreqadhabra.nflight.application.common.launcher.Config;
import com.abreqadhabra.nflight.application.common.launcher.LauncherHelper;
import com.abreqadhabra.nflight.application.service.ServiceFactory;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig;
import com.abreqadhabra.nflight.application.service.conf.ServiceConfig.ENUM_SERVICE_TYPE;
import com.abreqadhabra.nflight.application.service.thread.ServiceThreadFactory;

public class Command {

	public static void main(String[] args) throws Exception {

		// 시스템프로퍼티 등록
		Config.load(Command.class, ServiceConfig.PATH_SERVICE_PROPERTIES);

		LauncherHelper.setSecurityManager();// 추후 삭제

		Callable serviceRunnable = ServiceFactory
				.createtServiceTask(ENUM_SERVICE_TYPE.network_blocking);
	//Thread t = new ServiceThreadFactory()..newThread(serviceRunnable);

		new ArrayList<Object>();
//		Command cmd = new Command(t, "start", null);
		// cmd.execute();

		Invoker invoker = new Invoker();
	//	invoker.add(cmd);
		invoker.executeAll();
	}

	private Object receiver; // the "encapsulated" object
	private Method action; // the "pre-registered" request
	private Object[] args; // the "pre-registered" arg list

	public Command(Object obj, String methodName, Object[] arguments) {
		Class<?> clazz = obj.getClass(); // get the object's "Class"
		this.receiver = obj;
		if (arguments == null) {
			this.args = new Object[0];
		} else {
			this.args = arguments;
		}
		Class<?>[] argTypes = new Class<?>[this.args.length];
		for (int i = 0; i < this.args.length; i++) {
			// get the "Class" for each
			argTypes[i] = this.args[i].getClass(); // supplied argument
		}
		// get the "Method" data structure with the correct name and
		// signature
		try {
			this.action = clazz.getMethod(methodName, argTypes);
		} catch (NoSuchMethodException e) {
			System.out.println(e);
		}
	}

	public Object execute() {
		// in C++, you do something like --- return receiver->action( args
		// );
		try {
			return this.action.invoke(this.receiver, this.args);
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (InvocationTargetException e) {
			System.out.println(e);
		}
		return null;
	}
}