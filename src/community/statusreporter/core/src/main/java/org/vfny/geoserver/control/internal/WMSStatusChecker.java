package org.vfny.geoserver.control.internal;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.control.IStatusChecker;
import org.vfny.geoserver.control.IStatusReport;
import org.vfny.geoserver.global.WFS;
import org.vfny.geoserver.global.WMS;

public class WMSStatusChecker implements IStatusChecker, ApplicationContextAware {

	private static final String NAME = "WMS";
	private ApplicationContext applicationContext;

	public IStatusReport checkStatus() {
		int status = IStatusReport.OKAY;
		Exception message = null;
		
		WMS wms = null;
		try {
			wms = (WMS) applicationContext.getBean("wms");
		} catch (Exception e) {
			status = IStatusReport.ERROR;
			message = e;
			return new DefaultStatusReport(NAME, status, message);
		}
		
		if (wms == null) {
			status = IStatusReport.ERROR;
			message = new Exception("WMS failed to load. Please check server logs");
		} else if (!wms.isEnabled()) {
			status = IStatusReport.ERROR;
			message = new Exception("WMS service is currently disabled");
		}
		
		return new DefaultStatusReport(NAME, status, message);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
