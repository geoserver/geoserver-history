package org.vfny.geoserver.control.internal;

import org.geoserver.data.DefaultGeoServerCatalog;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.control.IStatusChecker;
import org.vfny.geoserver.control.IStatusReport;

public class PreferenceStatusChecker implements IStatusChecker,
		ApplicationContextAware {

	private static final String NAME = "Preferences";
	private ApplicationContext applicationContext;

	public IStatusReport checkStatus() {
		int status = IStatusReport.OKAY;
		Exception message = null;
		
		PreferenceStoreImpl pref = null;
		try {
			pref = (PreferenceStoreImpl) applicationContext.getBean("controller");
		} catch (Exception e) {
			status = IStatusReport.ERROR;
			message = e;
			return new DefaultStatusReport(NAME, status, message);
		}
		
		if (pref == null) {
			status = IStatusReport.ERROR;
			message = new Exception("Preferences failed to load. Please check server logs");
		} 
		
		
		return new DefaultStatusReport(NAME, status, message);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
