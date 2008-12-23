package org.geoserver.security.dao;

/**
 * DAO for loading the security properties files.
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class DAOException extends Exception {

	public DAOException() {
		super();
	}

	public DAOException(String s) {
		super(s);
	}

	public DAOException(Exception e) {
		super(e);
	}

}