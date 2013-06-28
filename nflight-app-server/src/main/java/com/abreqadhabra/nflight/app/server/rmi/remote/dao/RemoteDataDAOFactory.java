package com.abreqadhabra.nflight.app.server.rmi.remote.dao;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.dao.AirlineDAO;
import com.abreqadhabra.nflight.dao.CommonDAO;
import com.abreqadhabra.nflight.dao.DAOFactory;

/**
 * Implements class DAO Factory for remote data. 
 * <p>
 * 
 * @author Dongsup Kim
 * @version 0.8 10-Jan-2007
 */
public class RemoteDataDAOFactory extends DAOFactory {
    
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    
    /**
     * Service name to register this service in the registry
     */
    public static final String SERVICENAME = "FlyByNightService";
    
    public final Object getDataDAO(String serviceURL) {
        Remote dao = null;
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }else{
                _logger.log(Level.INFO, "SecurityManager not found.");
            }
            _logger.log(Level.INFO, "Binding " + serviceURL + SERVICENAME);
            dao = Naming.lookup(serviceURL + SERVICENAME);
        } catch (MalformedURLException me) {
            _logger.log(Level.WARNING, me.getMessage());
        } catch (RemoteException re) {
            _logger.log(Level.WARNING, re.getMessage());
            re.printStackTrace();
        } catch (NotBoundException ne) {
            _logger.log(Level.WARNING, ne.getMessage());
        }
        return dao;
    }

	@Override
	public CommonDAO getCommonDAO() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AirlineDAO getAirlineDAO() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    
}
