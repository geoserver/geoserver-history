/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.xml;

import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

import javax.xml.transform.TransformerException;

import org.geotools.filter.*;
import com.vividsolutions.jts.geom.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.geotools.gml.producer.*;
/**
 * ArgHelper purpose.
 * <p>
 * Description of ArgHelper ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * ArgHelper x = new ArgHelper(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
 */
class ArgHelper {
	
	/**
	 * Mapping purpose.
	 * <p>
	 * Used to mask attribute specific fucntions from the user.
	 * </p>
	 * 
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected interface Mapping{
		
		/**
		 * getType purpose.
		 * <p>
		 * Returns a constant type name.
		 * </p>
		 * @return String a constant type name.
		 */
		public abstract String getType();
		
		/**
		 * getInstance purpose.
		 * <p>
		 * Creates an instance of the appropriate type for this Mapping. 
		 * This is where type-dependant magic occurs
		 * </p>
		 * @param value The Element to interpret.
		 * @return The particular argument type expected.
		 */
		public abstract Object getInstance(Element value) throws ValidationException;
		
		/**
		 * isClassInstance purpose.
		 * <p>
		 * Tests to see if this class is of the expected type.
		 * </p>
		 * @param obj The object to test.
		 * @return true when they are compatible
		 */
		public abstract boolean isClassInstance(Object obj);
		
		/**
		 * encode purpose.
		 * <p>
		 * Creates an XML String from the obj provided, if the 
		 * object is of the expected type for this mapping.
		 * </p>
		 * @param obj The object to try and encode.
		 * @return An XML String if the type is correct, ClassCastException otherwise.
		 */
		public abstract String encode(Object obj) throws ValidationException;
		
		/**
		 * getElementName purpose.
		 * <p>
		 * This is the name of the element represented.
		 * </p>
		 * @return
		 */
		public abstract String getElementName();
	}
	
	private static final Mapping[] argumentTypeMappings = {new FilterMapping(),
			new GeometryMapping(), new EnvelopeMapping(), new ShortMapping(),
			new IntegerMapping(), new LongMapping(), new FloatMapping(),
			new DoubleMapping(), new DateMapping(),	new URIMapping(),
			new BooleanMapping(), new StringMapping()};
	

	/**
	 * getArgumentInstance purpose.
	 * <p>
	 * Returns an instance for the specified argument type from the Element provided.
	 * </p>
	 * @param typeName String the argument type name.
	 * @param value Element the element to create the Argument from.
	 * @return The Specified argument in Object form.
	 */
	public static Object getArgumentInstance(String typeName, Element value) throws ValidationException{
		if(typeName == null)
			throw new NullPointerException("A Typename must be specified.");
		for(int i=0;i<argumentTypeMappings.length;i++)
			if(typeName.equals(argumentTypeMappings[i].getElementName()))
				return argumentTypeMappings[i].getInstance(value);
		return null;
	}
	
	/**
	 * getArgumentType purpose.
	 * <p>
	 * Finds the appropriate argument type if one exists.
	 * </p>
	 * @param o The Object to search for it's type.
	 * @return The Object type or "" if not found.
	 */
	public static String getArgumentType(Object o){
		if(o == null)
			throw new NullPointerException("An argument instance must be specified.");
		for(int i=0;i<argumentTypeMappings.length;i++)
			if(argumentTypeMappings[i].isClassInstance(o))
				return argumentTypeMappings[i].getType();
		return "";
	}
	
	/**
	 * getArgumentEncoding purpose.
	 * <p>
	 * Creates an XML encodeing of the Object if it is a known argument type.
	 * </p>
	 * @param o Object the object to attempt to encode.
	 * @return an XML string if it is a known type, "" otherwise.
	 */
	public static String getArgumentEncoding(Object o) throws ValidationException{
		if(o == null)
			throw new NullPointerException("An argument instance must be specified.");
		for(int i=0;i<argumentTypeMappings.length;i++)
			if(argumentTypeMappings[i].isClassInstance(o))
				return argumentTypeMappings[i].encode(o);
		return "";
		
	}
	
	/**
	 * FilterMapping purpose.
	 * <p>
	 * Represents the specifics for the Filter Argument type
	 * </p>
	 * 
	 * @see Mapping
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class FilterMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "ogc:FilterType";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into an instance of type Filter
		 * @return Filter the filter instance if posible, null otherwise.
		 */
		public Object getInstance(Element value){
			// value must be the node for "ogc:Filter"
			if (value != null && ((value = ReaderUtils.getFirstChildElement(value)) != null)) {
					return FilterDOMParser.parseFilter(value);
			}
			return null;
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Filter
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Filter);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a filter. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Filter
		 */
		public String encode(Object obj) throws ValidationException{
			Filter f = null;
			if(obj == null)
				throw new NullPointerException("Cannot encode a null Filter.");
			if(!(obj instanceof Filter))
				throw new ClassCastException("Cannot cast "+obj.getClass().toString()+" to a Filter.");
			f = (Filter)obj;
			
			StringWriter sw = new StringWriter();
			org.geotools.filter.XMLEncoder xe = new org.geotools.filter.XMLEncoder(sw);
			try{
			xe.encode(f);
			}catch(IOException e){throw new ValidationException(e);}
			return "<filter>\n"+sw.toString()+"</filter>\n";
		}
	}
	
	/**
	 * GeometryMapping purpose.
	 * <p>
	 * Represents the workings for a Geometry Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class GeometryMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "gml:AbstractGeometryType";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Geometry.
		 * @return Geometry an instance of Geometry if one can be created, null otherwise.
		 */
		public Object getInstance(Element value){
			return (Geometry)ExpressionDOMParser.parseGML(value);
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Geometry
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Geometry);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a geometry. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type geometry
		 */
		public String encode(Object obj) throws ValidationException{
			StringWriter sw = new StringWriter();
			GeometryTransformer transformer = new GeometryTransformer();
			try{
			transformer.transform(obj,sw);;
			}catch(TransformerException e){throw new ValidationException(e);}
			return "<geometry>\n"+sw.toString()+"</geometry>\n";
		}
	}

	
	/**
	 * EnvelopeMapping purpose.
	 * <p>
	 * Represents the workings for a Envelope Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class EnvelopeMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "ogc:BBOXType";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Envelope.
		 * @return Geometry an instance of Envelope if one can be created, null otherwise.
		 */
		public Object getInstance(Element bboxElem) throws ValidationException{
			if(bboxElem == null)
				throw new NullPointerException("The bounding Box element passed in was null");
			try{
			boolean dynamic = ReaderUtils.getBooleanAttribute(bboxElem, "dynamic", false);
			if (!dynamic) {
				double minx = ReaderUtils.getDoubleAttribute(bboxElem, "minx", true);
				double miny = ReaderUtils.getDoubleAttribute(bboxElem, "miny", true);
				double maxx = ReaderUtils.getDoubleAttribute(bboxElem, "maxx", true);
				double maxy = ReaderUtils.getDoubleAttribute(bboxElem, "maxy", true);

				return new Envelope(minx, maxx, miny, maxy);
			}
			}catch(SAXException e){throw new ValidationException(e);}
			return null;
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Envelope
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Envelope);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Envelope. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Envelope
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The bounding Box obj passed in was null");
			if(!(obj instanceof Envelope))
				throw new ClassCastException("Object of type Envelope was expected.");

			String s = "";
			Envelope e = (Envelope)obj;
			s += "<bbox ";
			if(!e.isNull()){
				s += "dynamic = \"false\" ";
				s += "minx = \""+e.getMinX()+"\" ";
				s += "miny = \""+e.getMinY()+"\" ";
				s += "maxx = \""+e.getMaxX()+"\" ";
				s += "maxy = \""+e.getMaxY()+"\" ";
			}else{
				s += "dynamic = \"true\" ";
			}
			s += " />\n";
			return s;
		}
	}
	
	/**
	 * ShortMapping purpose.
	 * <p>
	 * Represents the workings for a Short Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class ShortMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:short";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Short.
		 * @return Geometry an instance of Short if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The short element passed in was null");
			return new Short(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Short
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Short);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a short. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type short
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The short obj passed in was null");
			if(!(obj instanceof Short))
				throw new ClassCastException("Object of type Short was expected.");

			return "<short>"+((Short)obj).toString()+"</short>\n";
		}
	}

	
	/**
	 * IntegerMapping purpose.
	 * <p>
	 * Represents the workings for a Integer Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class IntegerMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:integer";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Integer.
		 * @return Geometry an instance of Integer if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The integer passed in was null");
			return new Integer(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Integer
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Integer);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Integer. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Integer
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The integer obj passed in was null");
			if(!(obj instanceof Integer))
				throw new ClassCastException("Object of type Integer was expected.");

			return "<integer>"+((Integer)obj).toString()+"</integer>\n";
		}
	}

	/**
	 * LongMapping purpose.
	 * <p>
	 * Represents the workings for a Long Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class LongMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:long";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Long.
		 * @return Geometry an instance of Long if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The long passed in was null");
			return new Long(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Long);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Integer. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Integer
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The long obj passed in was null");
			if(!(obj instanceof Long))
				throw new ClassCastException("Object of type Long was expected.");

			return "<long>"+((Long)obj).toString()+"</long>\n";
		}
	}

	/**
	 * FloatMapping purpose.
	 * <p>
	 * Represents the workings for a Float Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class FloatMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:float";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Float.
		 * @return Geometry an instance of Float if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The float passed in was null");
			return new Float(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Float);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Float. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Float
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The float obj passed in was null");
			if(!(obj instanceof Float))
				throw new ClassCastException("Object of type Long was expected.");

			return "<float>"+((Float)obj).toString()+"</float>\n";
		}
	}

	/**
	 * DoubleMapping purpose.
	 * <p>
	 * Represents the workings for a Double Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class DoubleMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:double";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Double.
		 * @return Geometry an instance of Double if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The double passed in was null");
			return new Double(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Double);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Double. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Double
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The double obj passed in was null");
			if(!(obj instanceof Double))
				throw new ClassCastException("Object of type Long was expected.");

			return "<double>"+((Double)obj).toString()+"</double>\n";
		}
	}

	/**
	 * DateMapping purpose.
	 * <p>
	 * Represents the workings for a Date Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class DateMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:dateTime";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Date.
		 * @return Geometry an instance of Date if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem) throws ValidationException{
			if(elem == null)
				throw new NullPointerException("The dateTime passed in was null");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ssZ");
			try{
			return sdf.parse(ReaderUtils.getElementText(elem));
			}catch(ParseException e){throw new ValidationException(e);}
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Date);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Date. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Date
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The dateTime obj passed in was null");
			if(!(obj instanceof Date))
				throw new ClassCastException("Object of type Long was expected.");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ssZ");
			return "<dateTime>"+sdf.format((Date)obj)+"</dateTime>\n";
		}
	}

	/**
	 * URIMapping purpose.
	 * <p>
	 * Represents the workings for a URI Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class URIMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:anyUri";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a URI.
		 * @return Geometry an instance of URI if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem) throws ValidationException{
			if(elem == null)
				throw new NullPointerException("The anyUri passed in was null");
			try{
			return new URI(ReaderUtils.getElementText(elem));
			}catch(URISyntaxException e){throw new ValidationException(e);}
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof URI);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a URI. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type URI
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The anyUri obj passed in was null");
			if(!(obj instanceof URI))
				throw new ClassCastException("Object of type Long was expected.");

			return "<anyUri>"+((URI)obj).toString()+"</anyUri>\n";
		}
	}

	/**
	 * BooleanMapping purpose.
	 * <p>
	 * Represents the workings for a Boolean Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class BooleanMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:boolean";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a Boolean.
		 * @return Geometry an instance of Boolean if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The boolean passed in was null");
			return new Boolean(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof Boolean);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a Boolean. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type Boolean
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The boolean obj passed in was null");
			if(!(obj instanceof Boolean))
				throw new ClassCastException("Object of type Long was expected.");

			return "<boolean>"+((Boolean)obj).toString()+"</boolean>\n";
		}
	}

	/**
	 * StringMapping purpose.
	 * <p>
	 * Represents the workings for a String Mapping
	 * </p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @author $Author: dmzwiers $ (last modification)
	 * @version $Id: ArgHelper.java,v 1.2 2004/01/20 00:27:11 dmzwiers Exp $
	 */
	protected static class StringMapping implements Mapping{
		
		/**
		 * 
		 * Implementation of getType.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
		 * 
		 * @return the type name
		 */
		public String getType(){
			return "xs:string";
		}
		
		/**
		 * 
		 * Implementation of getInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
		 * 
		 * @param value Element the element to parse into a String.
		 * @return Geometry an instance of String if one can be created, null otherwise.
		 */
		public Object getInstance(Element elem){
			if(elem == null)
				throw new NullPointerException("The string passed in was null");
			return new String(ReaderUtils.getElementText(elem));
		}
		
		/**
		 * 
		 * Implementation of isClassInstance.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
		 * 
		 * @param c The Object to test
		 * @return true when both of type Long
		 */
		public boolean isClassInstance(Object c){
			return (c!=null && c instanceof String);
		}
		
		/**
		 * Implementation of encode.
		 * 
		 * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
		 * 
		 * @param obj An object to encode as a String. 
		 * @return String the XML encoding
		 * @throws ClassCastException when obj is not of type String
		 */
		public String encode(Object obj){
			if(obj == null)
				throw new NullPointerException("The string obj passed in was null");
			if(!(obj instanceof String))
				throw new ClassCastException("Object of type Long was expected.");

			return "<string>"+((String)obj).toString()+"</string>\n";
		}
	}
}
