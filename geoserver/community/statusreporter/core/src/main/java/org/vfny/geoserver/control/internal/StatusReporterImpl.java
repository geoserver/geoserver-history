package org.vfny.geoserver.control.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.vfny.geoserver.control.IStatusChecker;
import org.vfny.geoserver.control.IStatusReporter;

public class StatusReporterImpl implements IStatusReporter, ApplicationContextAware {

	private ApplicationContext applicationContext;

	public List getStatusReports() {
		List reports = new ArrayList();
		
		Map checkerBeans = (Map) applicationContext.getBeansOfType(IStatusChecker.class);
		
		Iterator iter = checkerBeans.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			
			IStatusChecker checker = (IStatusChecker) entry.getValue();
			
			reports.add(checker.checkStatus());
		}
		
		return reports;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void reset() {
		if (applicationContext instanceof AbstractApplicationContext) {
			((AbstractApplicationContext) applicationContext).refresh();
		}
	}
}
