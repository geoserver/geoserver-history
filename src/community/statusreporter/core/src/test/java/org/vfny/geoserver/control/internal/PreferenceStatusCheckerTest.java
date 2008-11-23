package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IStatusReport;

public class PreferenceStatusCheckerTest extends StatusCheckerTestCase {

	public PreferenceStatusCheckerTest() {
		super(PreferenceStatusChecker.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCheckStatus() {
		IStatusReport good = goodChecker.checkStatus();
		assertEquals(good.getModuleName().toLowerCase(), "preferences");
		assertEquals(good.getStatus(), IStatusReport.OKAY);
		
		IStatusReport bad = badChecker.checkStatus();
		assertEquals(bad.getModuleName().toLowerCase(), "preferences");
		assertEquals(bad.getStatus(), IStatusReport.ERROR);
		assertNotNull(bad.getMessage());
	}

}
