/*
 * Created on Feb 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.global.xml;

import java.util.HashSet;
import java.util.Set;

/**
 * XMLSchemaTranslator purpose.
 * <p>
 * Description of XMLSchemaTranslator ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: XMLSchemaTranslator.java,v 1.1 2004/02/06 19:10:50 dmzwiers Exp $
 */
public class XMLSchemaTranslator extends NameSpaceTranslator {

	private HashSet elements;
	
	/**
	 * XMLSchemaTranslator constructor.
	 * <p>
	 * Description
	 * </p>
	 * @param prefix
	 */
	public XMLSchemaTranslator(String prefix) {
		super(prefix);
		elements = new HashSet();
		elements.add(new BooleanElement(prefix));
		elements.add(new DecimalElement(prefix));
		elements.add(new IntegerElement(prefix));
		elements.add(new NegativeIntegerElement(prefix));
		elements.add(new NonNegativeIntegerElement(prefix));
		elements.add(new PositiveIntegerElement(prefix));
		elements.add(new LongElement(prefix));
		elements.add(new IntElement(prefix));
		elements.add(new ShortElement(prefix));
		elements.add(new ByteElement(prefix));
		elements.add(new UnsignedLongElement(prefix));
		elements.add(new UnsignedShortElement(prefix));
		elements.add(new UnsignedIntElement(prefix));
		elements.add(new UnsignedByteElement(prefix));
		elements.add(new FloatElement(prefix));
		elements.add(new DoubleElement(prefix));
		elements.add(new DateElement(prefix));
		elements.add(new DateTimeElement(prefix));
		elements.add(new DurationElement(prefix));
		elements.add(new GDayElement(prefix));
		elements.add(new GMonthElement(prefix));
		elements.add(new GMonthDayElement(prefix));
		elements.add(new GYearElement(prefix));
		elements.add(new GYearMonthElement(prefix));
		elements.add(new TimeElement(prefix));
		elements.add(new IDElement(prefix));
		elements.add(new IDREFElement(prefix));
		elements.add(new IDREFSElement(prefix));
		elements.add(new ENTITYElement(prefix));
		elements.add(new ENTITIESElement(prefix));
		elements.add(new NMTOKENElement(prefix));
		elements.add(new NMTOKENSElement(prefix));
		elements.add(new NOTATIONElement(prefix));
		elements.add(new StringElement(prefix));
		elements.add(new NormalizedStringElement(prefix));
		elements.add(new TokenElement(prefix));
		elements.add(new QNameElement(prefix));
		elements.add(new NameElement(prefix));
		elements.add(new NCNameElement(prefix));
	}

	/**
	 * Implementation of getElements.
	 * 
	 * @see org.vfny.geoserver.global.xml.NameSpaceTranslator#getElements()
	 * 
	 * @return
	 */
	public Set getElements() {
		return elements;
	}

	/**
	 * Implementation of getNameSpace.
	 * 
	 * @see org.vfny.geoserver.global.xml.NameSpaceTranslator#getNameSpace()
	 * 
	 * @return
	 */
	public String getNameSpace() {
		return "http://www.w3.org/2001/XMLSchema";
	}
}
