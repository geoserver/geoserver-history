package org.vfny.geoserver.control.internal;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.control.IStatusChecker;

public class StatusCheckerTestCase extends TestCase {

	IStatusChecker goodChecker;
	IStatusChecker badChecker;
	
	Class checker;
	FullApplicationContext fullContext;
	EmptyApplicationContext emptyContext;
	private boolean wfsEnabled = true;
	private boolean wmsEnabled = true;

	public StatusCheckerTestCase() {
		
	}
	
	/**
	 * @param checker
	 */
	public StatusCheckerTestCase(Class checker) {
		super();
		this.checker = checker;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		if (checker == null) {
			return;
		}
		
		fullContext = new FullApplicationContext(wfsEnabled, wmsEnabled );
		
		goodChecker = (IStatusChecker) checker.newInstance();
		((ApplicationContextAware) goodChecker).setApplicationContext(fullContext);
		
		emptyContext = new EmptyApplicationContext();
		
		badChecker = (IStatusChecker) checker.newInstance();
		((ApplicationContextAware) badChecker).setApplicationContext(emptyContext);
	}
	
	protected void disableWFS() throws Exception {
		this.wfsEnabled  = false;
		tearDown();
		setUp();
	}
	
	protected void disableWMS() throws Exception {
		this.wmsEnabled = false;
		tearDown();
		setUp();
	}
	
	protected void tearDown() throws Exception {
		goodChecker = null;
		badChecker = null;
	}
	
	public void testNothing() throws Exception {
		//Because this class extends TestCase, junit thinks it's an actual test
		//so it expects to see at least one test method.
	}
}
