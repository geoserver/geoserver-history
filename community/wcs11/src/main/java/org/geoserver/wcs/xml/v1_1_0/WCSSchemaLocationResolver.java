package org.geoserver.wcs.xml.v1_1_0;


import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;

/**
 * 
 * @generated
 */
public class WCSSchemaLocationResolver implements XSDSchemaLocationResolver {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 *	@generated modifiable
	 */
	public String resolveSchemaLocation(XSDSchema xsdSchema, String namespaceURI,  String schemaLocationURI) {
		if (schemaLocationURI == null)
			return null;
			
		//if no namespace given, assume default for the current schema
		if ((namespaceURI == null || "".equals(namespaceURI)) && xsdSchema != null) {
			namespaceURI = xsdSchema.getTargetNamespace();
		}
			
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsAll.xsd")) {
				return getClass().getResource("wcsAll.xsd").toString();
			}
		}
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsGetCoverage.xsd")) {
				return getClass().getResource("wcsGetCoverage.xsd").toString();
			}
		}
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsDescribeCoverage.xsd")) {
				return getClass().getResource("wcsDescribeCoverage.xsd").toString();
			}
		}
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsGetCapabilities.xsd")) {
				return getClass().getResource("wcsGetCapabilities.xsd").toString();
			}
		}
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsCommon.xsd")) {
				return getClass().getResource("wcsCommon.xsd").toString();
			}
		}
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsGridCRS.xsd")) {
				return getClass().getResource("wcsGridCRS.xsd").toString();
			}
		}
		if ("http://www.opengis.net/wcs/1.1".equals(namespaceURI)) {
			if (schemaLocationURI.endsWith("wcsContents.xsd")) {
				return getClass().getResource("wcsContents.xsd").toString();
			}
		}
		
		return null;
	}

}