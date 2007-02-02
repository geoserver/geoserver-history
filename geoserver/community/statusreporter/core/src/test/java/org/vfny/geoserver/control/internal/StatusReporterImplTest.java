package org.vfny.geoserver.control.internal;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.vfny.geoserver.control.IStatusReporter;

import junit.framework.TestCase;

public class StatusReporterImplTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetStatusReports() {
		FullApplicationContext context = new FullApplicationContext(true, true);
		StatusReporterImpl reporter = new StatusReporterImpl();
		reporter.setApplicationContext(context);
		
		List reports = reporter.getStatusReports();
		
		assertNotNull(reports);
		assertTrue(reports.size() >= 4);
	}

	public void testReset() {
		InternalApplicationContext resettableContext = new InternalApplicationContext();
		StatusReporterImpl reporter = new StatusReporterImpl();
		reporter.setApplicationContext(resettableContext);
		
		reporter.reset();
		
		assertTrue(resettableContext.refreshCalled);
	}
	
	private class InternalApplicationContext extends AbstractApplicationContext {

		public boolean refreshCalled = false;

		@Override
		public void refresh() throws BeansException, IllegalStateException {
			this.refreshCalled = true;
		}

		@Override
		public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void refreshBeanFactory() throws BeansException, IllegalStateException {
			// TODO Auto-generated method stub
			
		}
		
	}

}
