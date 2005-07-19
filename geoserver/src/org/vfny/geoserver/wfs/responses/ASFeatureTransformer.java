/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.wfs.responses;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollectionIteration;
import org.geotools.feature.FeatureType;
import org.geotools.gml.producer.GeometryTransformer;
import org.geotools.xml.transform.TransformerBase;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * ASFeatureTransformer provides a mechanism for converting Feature objects into
 * GML Application Schemas, as directed by the xpath attriutes in schema.xml
 * accompanying a Gesoerver feature type info.xml
 *
 * Need to get hold of xpath attriubutes - currently these are in the Geoserver
 * FeatureTypeInfo/AttributeTypeInfo not Geotools AttributeType - they probably
 * should be - if this approach works...
 *  
 * This class should probably go back to org.geotools.gml.producer. See the doumentation
 * accompanying FeatureTransformer
 * 
 * @author 
 * @version $Id$
 *
 */

public class ASFeatureTransformer extends TransformerBase {

    /** The logger for the filter module. */
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wfs");
	
    private static Set gmlAtts;
    private String collectionPrefix = "wfs";
    private String collectionNamespace = "http://www.opengis.net/wfs";
    private NamespaceSupport nsLookup = new NamespaceSupport();
    private FeatureTypeNamespaces featureTypeNamespaces = new FeatureTypeNamespaces(nsLookup);
    private SchemaLocationSupport schemaLocation = new SchemaLocationSupport();
    private int maxFeatures = -1;
    private boolean prefixGml = false;
    private String srsName;
    private String lockId;
    private int numDecimals = 4;

    /*
     * Associates an XMLElementStructure with a feature name, if the
     * feature has one; XMLElementStructure groups database attriubutes into
     * break groups for outputting nested XML.
     */
    private HashMap StructureMap = new HashMap(10);

    public void setCollectionNamespace(String nsURI) {
        collectionNamespace = nsURI;
    }

    public String getCollectionNamespace() {
        return collectionNamespace;
    }

    public void setCollectionPrefix(String prefix) {
        this.collectionPrefix = prefix;
    }

    public String getCollectionPrefix() {
        return collectionPrefix;
    }

    public void setNumDecimals(int numDecimals) {
        this.numDecimals = numDecimals;
    }

    public NamespaceSupport getFeatureNamespaces() {
        return nsLookup;
    }

    public FeatureTypeNamespaces getFeatureTypeNamespaces() {
        return featureTypeNamespaces;
    }

    public void addSchemaLocation(String nsURI, String uri) {
        schemaLocation.setLocation(nsURI, uri);
    }

    /**
     * Used to set the srsName attribute of the Geometries to be turned to xml.
     * For now we can only have all with the same srsName.
     *
     * @param srsName DOCUMENT ME!
     *
     * @task REVISIT: once we have better srs support in our feature model this
     *       should be rethought, as it's a rather blunt approach.
     */
    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    /**
     * Used to set a lockId attribute after a getFeatureWithLock.
     *
     * @param lockId DOCUMENT ME!
     *
     * @task REVISIT: Ian, this is probably the most wfs specific addition. If
     *       you'd like I can subclass and add it there.  It has to be added
     *       as an attribute to FeatureCollection, to report a
     *       GetFeatureWithLock
     */
    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    /**
     * If Gml Prefixing is enabled then attributes with names that could be
     * prefixed with gml, such as description, pointProperty, and name, will
     * be.  So if an attribute called name is encountered, instead of
     * prepending the default prefix (say gt2:name), it will turn out as
     * gml:name.  Right now this is fairly hacky, as the gml:name,
     * gml:description, ect., should be in the first attributes by default.
     * The actualy geometry encodings will always be prefixed with the proper
     * gml, like gml:coordinates. This only applies to attributes, that could
     * also be part of the features normal schema (for example a pointProperty
     * could be declared in the gt2  namespace, instead of a gml:pointProperty
     * it would be a gt2:pointProperty.
     *
     * @param prefixGml DOCUMENT ME!
     *
     * @task REVISIT: only prefix name, description, and boundedBy if they
     *       occur in their proper places.  Right now names always get gml
     *       prefixed if the gmlPrefixing is on, which is less than ideal.
     * @task REVISIT: The other approach is to allow for generic mapping, users
     *       would set which attributes they wanted to have different
     *       prefixes.
     */
    public void setGmlPrefixing(boolean prefixGml) {
        this.prefixGml = prefixGml;

        if (prefixGml && (gmlAtts == null)) {
            gmlAtts = new HashSet();
            gmlAtts.add("pointProperty");
            gmlAtts.add("geometryProperty");
            gmlAtts.add("polygonProperty");
            gmlAtts.add("lineStringProperty");
            gmlAtts.add("multiPointProperty");
            gmlAtts.add("multiLineStringProperty");
            gmlAtts.add("multiPolygonProperty");
            gmlAtts.add("description");
            gmlAtts.add("name");

            //boundedBy is done in handleAttribute to make use of the writeBounds
            //code.
        }
    }
    
    /**
     * Used to associate a feature type (from GeoServer feature type info)
     *
     * @param results Geoserver set of FeatureResults for a WFS getFeature
     *
     */    
    public void setXMLelementStructure(GetFeatureResults results) {
        
        FeatureResults features;
        FeatureTypeInfo meta = null;

        int resCount = results.getResultsetsCount();

        LOGGER.fine("setXMLelementStructures for " + resCount + " feature type(s)");
        
        for (int resIndex = 0; resIndex < resCount; resIndex++) {

        	features = results.getFeatures(resIndex);
            meta = results.getTypeInfo(resIndex);

            // expect to find an XMLelementsStructure from a Geoserver schema.xml
            // for this feature type
            
            if (meta.getXMLelementStructure() == null) {
            	LOGGER.severe("Expected to find an XMLelementsStructure from a Geoserver "
            			+ " schema.xml for feature type " + meta.getGT2FeatureTypeName());
            	// todo: throw an exception
            } else {
	            StructureMap.put(meta.getGT2FeatureTypeName(), meta.getXMLelementStructure());
	            // LOGGER.fine("added element structure for feature type " + meta.getName());
            }
        }
    }

    public org.geotools.xml.transform.Translator createTranslator(
        ContentHandler handler) {
    	FeatureTranslator t = new FeatureTranslator(handler, collectionPrefix,
                collectionNamespace, featureTypeNamespaces, schemaLocation);
        java.util.Enumeration prefixes = nsLookup.getPrefixes();

        //setGmlPrefixing(true);
        t.setNumDecimals(numDecimals);
        t.setGmlPrefixing(prefixGml);
        t.setSrsName(srsName);
        t.setLockId(lockId);

        while (prefixes.hasMoreElements()) {
            String prefix = prefixes.nextElement().toString();
            String uri = nsLookup.getURI(prefix);
            t.getNamespaceSupport().declarePrefix(prefix, uri);
        }

        return t;
    }

    public static class FeatureTypeNamespaces {
        Map lookup = new HashMap();
        NamespaceSupport nsSupport;
        String defaultPrefix = null;

        public FeatureTypeNamespaces(NamespaceSupport nsSupport) {
            this.nsSupport = nsSupport;
        }

        public void declareDefaultNamespace(String prefix, String nsURI) {
            defaultPrefix = prefix;
            nsSupport.declarePrefix(prefix, nsURI);
        }

        public void declareNamespace(FeatureType type, String prefix,
            String nsURI) {
            lookup.put(type, prefix);
            nsSupport.declarePrefix(prefix, nsURI);
        }

        public String findPrefix(FeatureType type) {
            String pre = (String) lookup.get(type);

            if (pre == null) {
                pre = defaultPrefix;
            }

            return pre;
        }
    }

    /**
     * Outputs gml without any fancy indents or newlines.
     */
    public class FeatureTranslator extends TranslatorSupport
        implements FeatureCollectionIteration.Handler {
        String fc = "FeatureCollection";
        GeometryTransformer.GeometryTranslator geometryTranslator;
        String memberString;
        String currentPrefix;
        FeatureTypeNamespaces types;
        boolean prefixGml = false;
        String srsName = null;
        String lockId = null;
        ContentHandler handler;
        private boolean running = true;

        /**
         * Constructor with handler.
         *
         * @param handler the handler to use.
         * @param prefix DOCUMENT ME!
         * @param ns DOCUMENT ME!
         * @param types DOCUMENT ME!
         * @param schemaLoc DOCUMENT ME!
         */
        public FeatureTranslator(ContentHandler handler, String prefix,
        		String ns, FeatureTypeNamespaces types,
				SchemaLocationSupport schemaLoc) {
        	
            super(handler, prefix, ns, schemaLoc);
            geometryTranslator = new GeometryTransformer.GeometryTranslator(handler);
            this.types = types;
            this.handler = handler;
            getNamespaceSupport().declarePrefix(geometryTranslator
                .getDefaultPrefix(), geometryTranslator.getDefaultNamespace());
            memberString = geometryTranslator.getDefaultPrefix()
                + ":featureMember";
        	
            // add all the global attribute prefixes from the map of XMLelementStructure
            // first coalesce into a singe set
            HashSet allAttPrefixes = new HashSet();
            Iterator i = StructureMap.values().iterator();
            while (i.hasNext()) {
            	XMLelementStructure structure = (XMLelementStructure)i.next();
            	structure.loadGlobalNamespaces(allAttPrefixes, nsSupport);
            }
        }

        void setGmlPrefixing(boolean prefixGml) {
            this.prefixGml = prefixGml;
        }

        void setSrsName(String srsName) {
            this.srsName = srsName;
        }

        void setNumDecimals(int numDecimals) {
            geometryTranslator = new GeometryTransformer.GeometryTranslator(handler,
                    numDecimals);
        }

        public void setLockId(String lockId) {
            this.lockId = lockId;
        }

        public void encode(Object o) throws IllegalArgumentException {
       	
            try {
                if (o instanceof FeatureCollection) {
                    FeatureCollection fc = (FeatureCollection) o;
                    FeatureCollectionIteration.iteration(this, fc);
                } else if (o instanceof FeatureReader) {
                    // THIS IS A HACK FOR QUICK USE
                    FeatureReader r = (FeatureReader) o;

                    startFeatureCollection();

                    handleFeatureReader(r);

                    endFeatureCollection();
                } else if (o instanceof FeatureResults) {
                    FeatureResults fr = (FeatureResults) o;
                    startFeatureCollection();
                    writeBounds(fr.getBounds());
                    handleFeatureReader(fr.reader());
                    endFeatureCollection();
                } else if (o instanceof FeatureResults[]) {
                    //Did FeatureResult[] so that we are sure they're all the same type.
                    //Could also consider collections here...  
                    FeatureResults[] results = (FeatureResults[]) o;
                    Envelope bounds = new Envelope();

                    for (int i = 0; i < results.length; i++) {
                        bounds.expandToInclude(results[i].getBounds());
                    }
                    
                    startFeatureCollection();
                    writeBounds(bounds);

                    for (int i = 0; i < results.length; i++) {
                        handleFeatureReader(results[i].reader());
                    }

                    endFeatureCollection();
                } else {
                    throw new IllegalArgumentException("Cannot encode " + o);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace(System.out);
                throw new RuntimeException("error reading FeatureResults", ioe);
            }
        }

        /*
         * MODIFIED FOR WRITING NESTED XML ELEMENTS
         */
        
        public void handleFeatureReader(FeatureReader r)
            throws IOException {
        	
        	// ASSUMES that feature type is constant within the reader AND
        	// that the no. of attributes is constant per feature - 
        	// eg a JDBC result set
        	// ALSO ASSUMES that a XMLelementStructure descibes a LIST
        	// NOT a tree - so can only handle one element type per
        	// nesting level - except at the leaf level 
        	
        	XMLelementStructure structure = (XMLelementStructure)
        		StructureMap.get(r.getFeatureType().getTypeName());  
        	
        	if (structure == null) {
        		throw new RuntimeException(
        			"Could not retrieve XMLelementStructure for feature type "
        			+ r.getFeatureType().getTypeName());
        	} 
        	
			Object[] previousValue = new Object[structure.getDBattributeCount()];
        	LOGGER.fine("dimensioned value state arrays for " 
        			+ structure.getDBattributeCount() + " elements");
        	
        	Feature f = null;
        	String typeName = "/" + r.getFeatureType().getTypeName(); 
        	
            try {
            	
            	String breakElement = "";

	            contentHandler.startElement("", "", memberString, NULL_ATTS);
            	
            	if (r.hasNext() && running) {
                	
            		f = r.next();
                	handleFeature(f);	// set default namepsace only
                	
                	if (structure.getDBattributeCount() != f.getNumberOfAttributes()) {
                		throw new RuntimeException(
                			"Database attribute count mismatch for feature type " 
                			+ r.getFeatureType().getTypeName());
                	}
            	} else {
                    contentHandler.endElement("", "", memberString);
                    return;
            	}
           	
                while (r.hasNext() && running) {

                	// write out elements from break element down the list
                	structure.writeElements(breakElement, f, this);

                	previousValue = rollover(f, previousValue);
                	
                	f = r.next();
                	
                	breakElement = structure.findBreak(f, previousValue, null);
                	
                	structure.closeTags(breakElement, this);
                	
                	if (breakElement.equalsIgnoreCase(typeName)) {
                        contentHandler.endElement("", "", memberString);
        	            contentHandler.startElement("", "", memberString, NULL_ATTS);
                	}
                }

            	structure.writeElements(breakElement, f, this);
                structure.closeTags("", this);
                
                contentHandler.endElement("", "", memberString);
                
            } catch (Exception ioe) {
                throw new RuntimeException("Error reading Features", ioe);
            } finally {
                if (r != null) {
                    LOGGER.finer("closing reader " + r);
                    r.close();
                }
            }
        }

        private Object[] rollover(Feature f, Object[] value) {
        	for (int i = 0; i < value.length; i++) {
        		value[i] = f.getAttribute(i);
        	}
        	return value;
        }
        
        public void startFeatureCollection() {
            try {
                
                String element = (getDefaultPrefix() == null) ? fc
                                                              : (getDefaultPrefix()
                    + ":" + fc);
                AttributesImpl atts = new AttributesImpl();

                // namespaces and schema loc atts are added elsewhere
                if (lockId != null) {
                    atts.addAttribute("", "lockId", "lockId", "", lockId);
                }

                contentHandler.startElement("", "", element, atts);

            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        public void endFeatureCollection() {
            end(fc);
        }

        /**
         * Prints up the gml for a featurecollection.
         *
         * @param collection DOCUMENT ME!
         */
        public void handleFeatureCollection(FeatureCollection collection) {
            startFeatureCollection();
            writeBounds(collection.getBounds());
        }

        /**
         * writes the <code>gml:boundedBy</code> element to output based on
         * <code>fc.getBounds()</code>
         *
         * @param bounds
         *
         * @throws RuntimeException if it is thorwn while writing the element
         *         or coordinates
         */
        public void writeBounds(Envelope bounds) {
            try {
                String boundedBy = geometryTranslator.getDefaultPrefix() + ":"
                    + "boundedBy";
                String box = geometryTranslator.getDefaultPrefix() + ":"
                    + "Box";
                contentHandler.startElement("", "", boundedBy, NULL_ATTS);
                geometryTranslator.encode(bounds, srsName);
                contentHandler.endElement("", "", boundedBy);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
        }

        /**
         * Sends sax for the ending of a feature collection.
         *
         * @param collection DOCUMENT ME!
         */
        public void endFeatureCollection(FeatureCollection collection) {
            endFeatureCollection();
        }

        /**
         * Sends sax for the ending of a feature.
         *
         * @param f DOCUMENT ME!
         *
         * @throws RuntimeException DOCUMENT ME!
         */
        public void endFeature(Feature f) {
            try {
                String name = f.getFeatureType().getTypeName();

                if (currentPrefix != null) {
                    name = currentPrefix + ":" + name;
                }

                contentHandler.endElement("", "", name);
                contentHandler.endElement("", "", memberString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * handles sax for an attribute.
         *
         * @param type DOCUMENT ME!
         * @param value DOCUMENT ME!
         *
         * @throws RuntimeException DOCUMENT ME!
         */
        public void handleAttribute(AttributeType type, Object value) {
            try {
                if (value != null) {
                    String name = type.getName();

                    //HACK: this should be user configurable, along with the 
                    //other gml substitutions I shall add.
                    if (name.equals("boundedBy")
                            && Geometry.class.isAssignableFrom(value.getClass())) {
                        writeBounds(((Geometry) value).getEnvelopeInternal());
                    } else {
                        String thisPrefix = currentPrefix;

                        if (prefixGml && gmlAtts.contains(name)) {
                            thisPrefix = "gml";
                        }

                        if (thisPrefix != null) {
                            name = thisPrefix + ":" + name;
                        }

                        contentHandler.startElement("", "", name, NULL_ATTS);

                        if (Geometry.class.isAssignableFrom(value.getClass())) {
                            geometryTranslator.encode((Geometry) value, srsName);
                        } else {
                            String text = value.toString();
                            contentHandler.characters(text.toCharArray(), 0,
                                text.length());
                        }

                        contentHandler.endElement("", "", name);
                    }
                }

                //REVISIT: xsi:nillable is the proper xml way to handle nulls,
                //but OGC people are fine with just leaving it out.       
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        
        /**
         * MODIFIED FROM handleAttribute(type,value) FOR WRITING NESTED XML ELEMENTS
         * 
         * 'Writes' an XML element. This is a callback method, driven by
         * XMLelementStructure 
         *
         * @param name the element name
         * @param value the element value
         * @param SAXatts a SAX attribute name/value list
         * @param closeTag if true do not close the element
         *
         * @throws RuntimeException DOCUMENT ME!
         */
        public void handleAttribute(
        		String name, Object value, AttributesImpl SAXatts,
				boolean closeTag) {        		

            try {
                if (value != null) {

                    //HACK: this should be user configurable, along with the 
                    //other gml substitutions I shall add.
                    if (name.equals("boundedBy")
                            && Geometry.class.isAssignableFrom(value.getClass())) {
                        writeBounds(((Geometry) value).getEnvelopeInternal());
                    } else {
                        String thisPrefix = currentPrefix;

                        if (prefixGml && gmlAtts.contains(name)) {
                            thisPrefix = "gml";
                        }

                        if (thisPrefix != null) {
                        	if (name.indexOf(':') == -1 )
                            	name = thisPrefix + ":" + name;
                        }

                        contentHandler.startElement("", "", name, SAXatts);

                        if (Geometry.class.isAssignableFrom(value.getClass())) {
                            geometryTranslator.encode((Geometry) value, srsName);
                        } else {
                            String text = value.toString();
                            contentHandler.characters(text.toCharArray(), 0,
                                text.length());
                        }

                        if (closeTag) contentHandler.endElement("", "", name);
                        	
                    }
                } else {
                	if (name.indexOf(':') == -1 ) name = currentPrefix + ":" + name; 
                    contentHandler.startElement("", "", name ,SAXatts);
                    if (closeTag) contentHandler.endElement("", "", name);
                }
            
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }        

        /**
         * close an element tag - called some timer after handleAttribute(
         *     	String name, Object value, AttributesImpl SAXatts,
         * 		boolean closeTag), where closeTag would have been false 
         * callabck method for XMLelementStructure
         *
         * @param name the element to close
         * 
         */        
        public void closeTag( String name) {
        	try {
        		if (name.indexOf(':') == -1 ) name = currentPrefix + ":" + name; 
        		contentHandler.endElement("", "", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        /**
         *
         * MODIFIED FOR WRITING NESTED XML ELEMENTS
         * 
         * Changed to only set set the default namepsace prefix
         *
         * @param f the feature type from which the default namespace prefix is determined
         *
         * If it can't find a prefix - have set it to "", previously threw a
         * RuntimeException. Not sure if this is a big deal or why? pb
         * 
         */
        public void handleFeature(Feature f) {
            try {
	            // contentHandler.startElement("", "", memberString, NULL_ATTS);
	
	            FeatureType type = f.getFeatureType();
	            String name = type.getTypeName();
	            currentPrefix = getNamespaceSupport().getPrefix(f.getFeatureType()
	                                                             .getNamespace().toString());
	
	            LOGGER.fine("currentPrefix = " + currentPrefix);
	            
	            if (currentPrefix == null) {
	                currentPrefix = types.findPrefix(f.getFeatureType());
	            }
	
	            if (currentPrefix == null) {
	            	// used to throw exception here
	                LOGGER.warning(
	                    "Could not locate namespace for FeatureType : "
	                    + type.getTypeName());
	                currentPrefix = "";
	            }
	
	            if (currentPrefix != null) {
	                name = currentPrefix + ":" + name;
	            }
/*	
	            AttributesImpl fidAtts = new org.xml.sax.helpers.AttributesImpl();
	            String fid = f.getID();
	
	            if (fid != null) {
	                fidAtts.addAttribute("", "fid", "fid", "fids", fid);
	            }
	
	            contentHandler.startElement("", "", name, fidAtts);
*/	            
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
