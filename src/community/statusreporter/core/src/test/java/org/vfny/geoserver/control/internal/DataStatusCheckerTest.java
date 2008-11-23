package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IStatusReport;

public class DataStatusCheckerTest extends StatusCheckerTestCase {

	public DataStatusCheckerTest() {
		super(DataStatusChecker.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCheckStatus() {

		IStatusReport good = goodChecker.checkStatus();
		assertEquals(good.getModuleName().toLowerCase(), "data");
		assertEquals(good.getStatus(), IStatusReport.OKAY);
		
		IStatusReport bad = badChecker.checkStatus();
		assertEquals(bad.getModuleName().toLowerCase(), "data");
		assertEquals(bad.getStatus(), IStatusReport.ERROR);
		assertNotNull(bad.getMessage());
		
	}

}
