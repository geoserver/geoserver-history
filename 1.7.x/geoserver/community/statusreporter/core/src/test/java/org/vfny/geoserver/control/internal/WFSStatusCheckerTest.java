package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IStatusReport;

public class WFSStatusCheckerTest extends StatusCheckerTestCase {

	public WFSStatusCheckerTest() {
		super(WFSStatusChecker.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCheckStatus() throws Exception {
		IStatusReport good = goodChecker.checkStatus();
		assertEquals(good.getModuleName().toLowerCase(), "wfs");
		assertEquals(good.getStatus(), IStatusReport.OKAY);
		
		disableWFS();
		
		IStatusReport disabled = goodChecker.checkStatus();
		assertEquals(disabled.getStatus(), IStatusReport.ERROR);
		assertNotNull(disabled.getMessage());
		
		IStatusReport bad = badChecker.checkStatus();
		assertEquals(bad.getModuleName().toLowerCase(), "wfs");
		assertEquals(bad.getStatus(), IStatusReport.ERROR);
		assertNotNull(bad.getMessage());
	}

}
