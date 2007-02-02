package org.vfny.geoserver.control.internal;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.control.IStatusChecker;
import org.vfny.geoserver.control.IStatusReport;
import org.vfny.geoserver.global.WFS;

public class WFSStatusChecker implements IStatusChecker, ApplicationContextAware {
	
	private static final String NAME = "WFS";

	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public IStatusReport checkStatus() {
		int status = IStatusReport.OKAY;
		Exception message = null;
		
		WFS wfs = null;
		try {
			wfs = (WFS) applicationContext.getBean("wfs");
		} catch (Exception e) {
			status = IStatusReport.ERROR;
			message = e;
			return new DefaultStatusReport(NAME, status, message);
		}
		
		if (wfs == null) {
			status = IStatusReport.ERROR;
			message = new Exception("WFS failed to load. Please check server logs");
		} else if (!wfs.isEnabled()) {
			status = IStatusReport.ERROR;
			message = new Exception("WFS service is currently disabled");
		}
		
		return new DefaultStatusReport(NAME, status, message);
	}
}
