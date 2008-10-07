package org.geoserver.catalog.hibernate;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class HibernateTestSupport extends AbstractTransactionalDataSourceSpringContextTests  {

	public HibernateTestSupport() {
		setDefaultRollback( false );
	}
	
	protected String[] getConfigLocations() {
		return new String[]{"classpath:applicationContext-hibernateWeb.xml",
				"classpath:applicationContext-hibernateTest.xml"};
	}
}
