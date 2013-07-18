package com.abreqadhabra.nflight.service.core.server.factory;

import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;
import com.abreqadhabra.nflight.common.util.PropertyFile;
import com.abreqadhabra.nflight.service.core.Profile;
import com.abreqadhabra.nflight.service.core.ProfileImpl;
import com.abreqadhabra.nflight.service.core.server.NFServer;
import com.abreqadhabra.nflight.service.core.server.NFService;

abstract class AbstractRMIServer extends NFServer {
	public AbstractRMIServer(ProfileImpl profile) throws Exception {
		super(profile);
	}
}

class UnicastRMIServerImpl extends AbstractRMIServer {

	public UnicastRMIServerImpl(ProfileImpl profile) throws Exception {
		super(profile);
	}

	@Override
	public void init() throws Exception {

	}

	@Override
	public void startup() throws Exception {

	}

	@Override
	public void shutdown() throws Exception {

	}

	@Override
	public boolean status() throws Exception {

		return false;
	}
}

class ActivatableRMIServerImpl extends AbstractRMIServer {

	public ActivatableRMIServerImpl(ProfileImpl profile) throws Exception {
		super(profile);
	}

	@Override
	public void init() throws Exception {

	}

	@Override
	public void startup() throws Exception {

	}

	@Override
	public void shutdown() throws Exception {

	}

	@Override
	public boolean status() throws Exception {

		return false;
	}
}

abstract class AbstractRMIService implements NFService {

}

class UnicastRMIServiceImpl extends AbstractRMIService {

	public UnicastRMIServiceImpl(ProfileImpl profile) {

	}

	@Override
	public boolean isRunning() throws RemoteException {

		return false;
	}

	@Override
	public String sayHello() throws RemoteException {

		return null;
	}
}

class ActivatableRMIServiceImpl extends AbstractRMIService {

	public ActivatableRMIServiceImpl(ProfileImpl profile) {

	}

	@Override
	public boolean isRunning() throws RemoteException {

		return false;
	}

	@Override
	public String sayHello() throws RemoteException {

		return null;
	}

}

abstract class RMIServerAbstractFactory {
	abstract AbstractRMIServer createServer(ProfileImpl profile)
			throws Exception;

	abstract AbstractRMIService createService(ProfileImpl profile);
}

class UnicastRMIServerConcreteFactory extends RMIServerAbstractFactory {

	AbstractRMIServer createServer(ProfileImpl profile) throws Exception {
		return new UnicastRMIServerImpl(profile);
	}

	AbstractRMIService createService(ProfileImpl profile) {
		return new UnicastRMIServiceImpl(profile);
	}
}

class ActivatableRMIServerConcreteFactory extends RMIServerAbstractFactory {
	AbstractRMIServer createServer(ProfileImpl profile) throws Exception {
		return new ActivatableRMIServerImpl(profile);
	}

	AbstractRMIService createService(ProfileImpl profile) {
		return new ActivatableRMIServiceImpl(profile);
	}
}

// Factory creator - an indirect way of instantiating the factories
class RMIServerFactoryMaker {
	private static RMIServerAbstractFactory abstractFactory = null;

	static RMIServerAbstractFactory getFactory(String choice) {
		if (choice.equals("unicast")) {
			abstractFactory = new UnicastRMIServerConcreteFactory();
		} else if (choice.equals("activatable")) {
			abstractFactory = new ActivatableRMIServerConcreteFactory();
		}
		return abstractFactory;
	}
}

// Client
public class RMIServerAbstractFactoryTest {
	private static final Class<RMIServerAbstractFactoryTest> THIS_CLAZZ = RMIServerAbstractFactoryTest.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);

	public static void main(String args[]) throws Exception {

		// Settings specified in the default property file
		Properties props = PropertyFile.readPropertyFilePath(
				THIS_CLAZZ.getName(), Profile.FILE_BOOT_PROPERTIES);

		ProfileImpl profile = new ProfileImpl(props);

		RMIServerAbstractFactory unicastAbstractFactory = RMIServerFactoryMaker
				.getFactory("unicast");
		AbstractRMIServer unicastServer = unicastAbstractFactory
				.createServer(profile);
		AbstractRMIService unicastService = unicastAbstractFactory
				.createService(profile);

		RMIServerAbstractFactory activatableAbstractFactory = RMIServerFactoryMaker
				.getFactory("activatable");
		AbstractRMIServer activatableServer = activatableAbstractFactory
				.createServer(profile);
		AbstractRMIService activatableService = activatableAbstractFactory
				.createService(profile);

		// more function calls on product
	}
}