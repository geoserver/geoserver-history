package org.geoserver.security.dao;


import org.geoserver.security.model.configuration.ConfigureChainOfResponsibility;

/**
 * DAO for loading the security properties files.
 * 
 * @author Francesco Izzi (geoSDI)
 */

public interface IDAOConfiguration {

	
	ConfigureChainOfResponsibility loadConfiguration() throws DAOException;
}
