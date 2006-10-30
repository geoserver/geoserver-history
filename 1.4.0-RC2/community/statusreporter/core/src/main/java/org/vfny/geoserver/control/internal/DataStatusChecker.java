package org.vfny.geoserver.control.internal;

import org.geoserver.data.DefaultGeoServerCatalog;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.control.IStatusChecker;
import org.vfny.geoserver.control.IStatusReport;
import org.vfny.geoserver.global.WMS;

public class DataStatusChecker implements IStatusChecker, ApplicationContextAware {

	private static final String NAME = "Data";
	private ApplicationContext applicationContext;

	public IStatusReport checkStatus() {
		int status = IStatusReport.OKAY;
		Exception message = null;
		
		DefaultGeoServerCatalog catalog = null;
		try {
			catalog = (DefaultGeoServerCatalog) applicationContext.getBean("catalog");
		} catch (Exception e) {
			status = IStatusReport.ERROR;
			message = e;
			return new DefaultStatusReport(NAME, status, message);
		}
		
		if (catalog == null) {
			status = IStatusReport.ERROR;
			message = new Exception("Data failed to load. Please check server logs");
		} 
		
		//TODO: maybe look at catalog's status?
		
		return new DefaultStatusReport(NAME, status, message);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
