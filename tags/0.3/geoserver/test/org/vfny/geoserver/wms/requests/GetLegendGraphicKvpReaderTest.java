package org.vfny.geoserver.wms.requests;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.styling.Style;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.testdata.MockUtils;

import com.mockrunner.mock.web.MockHttpServletRequest;

public class GetLegendGraphicKvpReaderTest extends TestCase {

	/**
	 * request reader to test against, initialized by default with all
	 * parameters from <code>requiredParameters</code> and
	 * <code>optionalParameters</code>
	 */
	GetLegendGraphicKvpReader requestReader;

	/** test values for required parameters */
	Map requiredParameters;

	/** test values for optional parameters */
	Map optionalParameters;
	
	/** both required and optional parameters joint up */
	Map allParameters;

	MockHttpServletRequest httpRequest;

	/**
	 * Remainder:
	 * <ul>
	 * <li>VERSION/Required
	 * <li>REQUEST/Required
	 * <li>LAYER/Required
	 * <li>FORMAT/Required
	 * <li>STYLE/Optional
	 * <li>FEATURETYPE/Optional
	 * <li>RULE/Optional
	 * <li>SCALE/Optional
	 * <li>SLD/Optional
	 * <li>SLD_BODY/Optional
	 * <li>WIDTH/Optional
	 * <li>HEIGHT/Optional
	 * <li>EXCEPTIONS/Optional
	 * </ul>
	 */
	protected void setUp() throws Exception {
		super.setUp();
		requiredParameters = new HashMap();
		requiredParameters.put("VERSION", "1.0.0");
		requiredParameters.put("REQUEST", "GetLegendGraphic");
		requiredParameters.put("LAYER", "cite:Ponds");
		requiredParameters.put("FORMAT", "image/png");

		optionalParameters = new HashMap();
		optionalParameters.put("STYLE", "Ponds");
		optionalParameters.put("FEATURETYPE", "fake_not_used");
		// optionalParameters.put("RULE", "testRule");
		optionalParameters.put("SCALE", "1000");
		optionalParameters.put("WIDTH", "120");
		optionalParameters.put("HEIGHT", "90");
		// ??optionalParameters.put("EXCEPTIONS", "");

		allParameters = new HashMap(requiredParameters);
		allParameters.putAll(optionalParameters);

		this.requestReader = new GetLegendGraphicKvpReader(allParameters);
		this.httpRequest = MockUtils.newHttpRequest(allParameters, true);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testVersion() throws ServiceException {
		requiredParameters.put("VERSION", "WRONG");
		this.requestReader = new GetLegendGraphicKvpReader(requiredParameters);
		try {
			requestReader.getRequest(httpRequest);
			fail("Expected ServiceException due to wrong VERSION parameter");
		} catch (ServiceException e) {
			// OK
		}
		requiredParameters.put("VERSION", "1.0.0");
		GetLegendGraphicRequest parsedRequest;
		parsedRequest = (GetLegendGraphicRequest) requestReader
				.getRequest(httpRequest);
	}

	/**
	 * This test ensures that when a SLD parameter has been passed that refers
	 * to a SLD document with multiple styles, the required one is choosed based
	 * on the LAYER parameter.
	 * <p>
	 * This is the case where a remote SLD document is used in "library" mode.
	 * </p>
	 */
	public void testRemoteSLDMultipleStyles() throws ServiceException {
		final URL remoteSldUrl = getClass().getResource("test-data/MultipleStyles.sld");
		this.allParameters.put("SLD", remoteSldUrl.toExternalForm());

		this.allParameters.put("LAYER", "cite:Ponds");
		this.allParameters.put("STYLE", "Ponds");
		requestReader = new GetLegendGraphicKvpReader(this.allParameters);
		GetLegendGraphicRequest request = (GetLegendGraphicRequest)requestReader.getRequest(httpRequest);
		
		//the style names Ponds is declared in third position on the sld doc
		Style selectedStyle = request.getStyle();
		assertNotNull(selectedStyle);
		assertEquals("Ponds", selectedStyle.getName());

	
		this.allParameters.put("LAYER", "cite:Lakes");
		this.allParameters.put("STYLE", "Lakes");
		requestReader = new GetLegendGraphicKvpReader(this.allParameters);
		request = (GetLegendGraphicRequest)requestReader.getRequest(httpRequest);
		
		//the style names Ponds is declared in third position on the sld doc
		selectedStyle = request.getStyle();
		assertNotNull(selectedStyle);
		assertEquals("Lakes", selectedStyle.getName());
	}

}
