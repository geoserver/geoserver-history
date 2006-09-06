package org.geoserver.xml.ows.v1_0_0;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;

public class OWSSchemaLocationResolver implements XSDSchemaLocationResolver {

	public String resolveSchemaLocation(XSDSchema xsdSchema, String namespaceURI,  String schemaLocationURI) {
		if (schemaLocationURI == null)
			return null;
		
		//if no namespace given, assume default for the current schema
		if ((namespaceURI == null || "".equals(namespaceURI)) && xsdSchema != null) {
			namespaceURI = xsdSchema.getTargetNamespace();
		}
			 
		
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("ows19115subset.xsd")) {
				return getClass().getResource("ows19115subset.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsAll.xsd")) {
				return getClass().getResource("owsAll.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsCommon.xsd")) {
				return getClass().getResource("owsCommon.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsDataIdentification.xsd")) {
				return getClass().getResource("owsDataIdentification.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsExceptionReport.xsd")) {
				return getClass().getResource("owsExceptionReport.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsGetCapabilities.xsd")) {
				return getClass().getResource("owsGetCapabilities.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsOperationsMetadata.xsd")) {
				return getClass().getResource("owsOperationsMetadata.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsServiceIdentification.xsd")) {
				return getClass().getResource("owsServiceIdentification.xsd").toString();
			}
		}
		if ("http://www.opengis.net/ows".equals(namespaceURI) && schemaLocationURI != null) {
			if (schemaLocationURI.endsWith("owsServiceProvider.xsd")) {
				return getClass().getResource("owsServiceProvider.xsd").toString();
			}
		}
		return null;
	}

}
