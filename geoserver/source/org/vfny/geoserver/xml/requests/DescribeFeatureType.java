package org.vfny.geoserver.xml.requests;

import java.io.*;
import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.apache.xerces.parsers.SAXParser;

class DescribeFeatureType implements ContentHandler {

		private Locator locator;
		private String currentTag = new String();
		private List featureTypes = Collections.synchronizedList(new ArrayList());

		public List getFeatureTypes() {
				return this.featureTypes;
		}

		public void setDocumentLocator (Locator locator) {
		}

		public void startDocument()
				throws SAXException {
		}

		public void endDocument()
				throws SAXException {
		}

		public void processingInstruction(String target, String data)
				throws SAXException {
		}

		public void startPrefixMapping(String prefix, String uri) {
		}

		public void endPrefixMapping(String prefix) {
		}

		public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
				throws SAXException {
				
				currentTag = localName;
		}

		public void endElement(String namespaceURI, String localName, String rawName)
				throws SAXException {
				currentTag = "";
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {

				String s = new String(ch, start, length);
				if( currentTag.equals("TypeName") ) {
						featureTypes.add(s);
				}
		}

		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		public void skippedEntity(String name)
				throws SAXException {
		}

}
