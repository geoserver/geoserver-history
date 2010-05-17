package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IStatusReport;

import junit.framework.TestCase;

public class DefaultStatusReportTest extends TestCase {

	public void testDefaultStatusReport() {
		String name = "name";
		int status = IStatusReport.OKAY;
		Exception message = new Exception();
		
		IStatusReport report = new DefaultStatusReport(name, status, message);
		
		assertEquals(name, report.getModuleName());
		assertEquals(status, report.getStatus());
		assertEquals(message, report.getMessage());
	}

}
