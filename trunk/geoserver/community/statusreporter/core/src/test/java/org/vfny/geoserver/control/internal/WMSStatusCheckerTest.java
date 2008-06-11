package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IStatusReport;

public class WMSStatusCheckerTest extends StatusCheckerTestCase {

	public WMSStatusCheckerTest() {
		super(WMSStatusChecker.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCheckStatus() throws Exception {
		IStatusReport good = goodChecker.checkStatus();
		assertEquals(good.getModuleName().toLowerCase(), "wms");
		assertEquals(good.getStatus(), IStatusReport.OKAY);
		
		disableWMS();
		
		IStatusReport disabled = goodChecker.checkStatus();
		assertEquals(disabled.getStatus(), IStatusReport.ERROR);
		assertNotNull(disabled.getMessage());
		
		IStatusReport bad = badChecker.checkStatus();
		assertEquals(bad.getModuleName().toLowerCase(), "wms");
		assertEquals(bad.getStatus(), IStatusReport.ERROR);
		assertNotNull(bad.getMessage());
	}

}
