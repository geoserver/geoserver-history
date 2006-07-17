/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.wfs.feature.FeatureTypeInfo;
import org.geotools.catalog.GeoResource;
import org.geotools.feature.FeatureType;
import org.geotools.gml.producer.FeatureTypeTransformer;

/**
 * Handles a DescribeFeatureType request and creates a DescribeFeatureType
 * response GML string.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DescribeResponse.java,v 1.22 2004/04/16 07:06:10 jive Exp $
 *
 * @task TODO: implement the response streaming in writeTo instead of the
 *       current String generation
 */
public class DescribeFeatureType {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    // Initialize some generic GML information
    // ABSTRACT OUTSIDE CLASS, IF POSSIBLE
    private static final String SCHEMA_URI = "\"http://www.w3.org/2001/XMLSchema\"";
    private static final String XS_NAMESPACE = "\n  xmlns:xs=" + SCHEMA_URI;
    private static final String GML_URL = "\"http://www.opengis.net/gml\"";
    private static final String GML_NAMESPACE = "\n  xmlns:gml=" + GML_URL;
    private static final String ELEMENT_FORM_DEFAULT = "\n  elementFormDefault=\"qualified\"";
    private static final String ATTR_FORM_DEFAULT = "\n  attributeFormDefault=\"unqualified\" version=\"1.0\">";
    private static final String TARGETNS_PREFIX = "\n  targetNamespace=\"";
    private static final String TARGETNS_SUFFIX = "\" ";

    /** Fixed return footer information */
    private static final String FOOTER = "\n</xs:schema>";
    
    /** Main XML class for interpretation and response. */
    private String xmlResponse = new String();

    /**
     * Catalog reference
     */
    private GeoServerCatalog catalog;
    /**
     * WFS service bean
     */
    private WFS wfs;
    /**
     * The output format
     */
    private String outputFormat;
    
    /**
     * String requested types.
     */
    private List requestedTypes;
    
    public DescribeFeatureType( GeoServerCatalog catalog, WFS wfs ) {
		this.catalog = catalog;
		this.wfs = wfs;
	}
    
    public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
    
    public void setRequestedTypes(List requestedTypes) {
		this.requestedTypes = requestedTypes;
	}
    
    public String describeFeatureType() throws WFSException {
    	 	
    		if (!outputFormat.equalsIgnoreCase("XMLSCHEMA")) {
	        String msg ="output format: " + outputFormat + " not "
            + "supported by geoserver"; 
    			throw new WFSException( msg, null );
	     }

	     // generates response, using general function
	     try {
			xmlResponse = generateTypes();
	     } 
	     catch (IOException e) {
	    	 	throw new WFSException( e , null );
		 }

	     if ( !wfs.isVerbose() ) {
	         //strip out the formatting.  This is pretty much the only way we
	         //can do this, as the user files are going to have newline
	         //characters and whatnot, unless we can get rid of formatting
	         //when we read the file, which could be worth looking into if
	         //this slows things down.
	     	xmlResponse = xmlResponse.replaceAll(">\n[ \\t\\n]*", ">");
	        xmlResponse = xmlResponse.replaceAll("\n[ \\t\\n]*", " ");
	     }	
	     
	     return xmlResponse;
    }
    

    /**
     * Writes the describe response to the output stream.
     *
     * @param out Where to write to.
     *
     * @throws WFSException For any io exceptions.  Needs to be a buffer or
     *         file strategy, if on SPEED it's already too late at this point
     *         and the client is going to get some odd errors.
     */
    public void writeTo(OutputStream out) throws WFSException {
        try {
            byte[] content = xmlResponse.getBytes();
            out.write(content);
        } 
        catch (IOException ex) {
            throw new WFSException( ex, null );
        }
    }

    /**
     * Internal method to generate the XML response object, using feature
     * types.
     *
     * @param wfsRequest The request object.
     *
     * @return The XMLSchema describing the features requested.
     *
     * @throws WFSException For any problems.
     */
    private final String generateTypes()
        throws WFSException, IOException {
        
        // Initialize return information and intermediate return objects
        StringBuffer tempResponse = new StringBuffer();

        //list of catalog handles
        List handles = catalog.resources( FeatureTypeInfo.class );
        if (requestedTypes != null || requestedTypes.size() > 0) {
        		O:for ( Iterator h = handles.iterator(); h.hasNext(); ) {
        			GeoResource handle = (GeoResource) h.next();
    				FeatureTypeInfo info = 
    					(FeatureTypeInfo) handle.resolve( FeatureTypeInfo.class, null );
    				
    				for ( Iterator t = requestedTypes.iterator(); t.hasNext(); ) {
    					String requestedType = (String) t.next();
    	    				if ( info.name().equals( requestedType ) 
    	    						|| info.getTypeName().equals( requestedType ) ) {
    	    					
    	    					//found, continue on and keep this handle in list
    	    					continue O;
    	    				}
    	    			}
    				
    				//not found, remove the handle from the list
    				h.remove();
        		}
        		    
        	}
        else {
        		//if there are no specific requested types then get all.
        	}
       
        //generate a list of info objects
        List infos = new ArrayList();
        for ( Iterator h = handles.iterator(); h.hasNext(); ) {
        		GeoResource handle = (GeoResource) h.next();
        		FeatureTypeInfo info = 
        			(FeatureTypeInfo) handle.resolve( FeatureTypeInfo.class, null );
        		infos.add( info );
        }
        
        tempResponse.append("<?xml version=\"1.0\" encoding=\""
            + wfs.getCharSet().displayName() + "\"?>"
            + "\n<xs:schema ");

        //allSameType will throw WFSException if there are types that are not found.
        if ( allSameType( infos ) ) {
            //all the requested have the same namespace prefix, so return their
            //schemas.
        		FeatureTypeInfo ftInfo =  (FeatureTypeInfo) infos.get( 0 );
        		String targetNs = 
        			catalog.getNamespaceSupport().getURI( ftInfo.getNamespacePrefix() );
        		
            //String targetNs = nsInfoType.getXmlns();
            tempResponse.append(TARGETNS_PREFIX + targetNs + TARGETNS_SUFFIX);

            //namespace
            tempResponse.append("\n  " + "xmlns:" + ftInfo.getNamespacePrefix()
                + "=\"" + targetNs + "\"");

            //xmlns:" + nsPrefix + "=\"" + targetNs
            //+ "\"");
            tempResponse.append(GML_NAMESPACE);
            tempResponse.append(XS_NAMESPACE);
            tempResponse.append(ELEMENT_FORM_DEFAULT + ATTR_FORM_DEFAULT);

            //request.getBaseUrl should actually be GeoServer.getSchemaBaseUrl()
            //but that method is broken right now.  See the note there.
            
            //JD: need a good way to publish resources under a web url, at the 
            // same time abstracting away the httpness of the service, for 
            // now replacing the schemas.opengis.net
            
//            tempResponse.append("\n\n<xs:import namespace=" + GML_URL
//                + " schemaLocation=\"" + request.getSchemaBaseUrl()
//                + "gml/2.1.2/feature.xsd\"/>\n\n");
            tempResponse.append("\n\n<xs:import namespace=" + GML_URL
                    + " schemaLocation=\"" + wfs.getSchemaBaseURL() 
                    + "gml/2.1.2/feature.xsd\"/>\n\n");
            tempResponse.append( generateSpecifiedTypes( infos ) );
        } else {
            //the featureTypes do not have all the same prefixes.
            tempResponse.append(XS_NAMESPACE);
            tempResponse.append(ELEMENT_FORM_DEFAULT + ATTR_FORM_DEFAULT);

            Set prefixes = new HashSet();
            Iterator i = infos.iterator();

            //iterate through the types, and make a set of their prefixes.
            while ( i.hasNext() ) {
            		FeatureTypeInfo ftInfo = (FeatureTypeInfo) i.next();
                prefixes.add( ftInfo.getNamespacePrefix() );
            }

            Iterator prefixIter = prefixes.iterator();

            while (prefixIter.hasNext()) {
                //iterate through prefixes, and add the types that have that prefix.
                String prefix = prefixIter.next().toString();
                tempResponse.append( getNSImport( prefix, infos ) );
            }
        }

        tempResponse.append(FOOTER);

        return tempResponse.toString();
    }

    /**
     * Creates a import namespace element, for cases when requests contain
     * multiple namespaces, as you can not have more than one target
     * namespace.  See wfs spec. 8.3.1.  All the typeNames that have the
     * correct prefix are added to the import statement.
     *
     * @param prefix the namespace prefix, which must be mapped in the main
     *        ConfigInfo, for this import statement.
     * @param typeNames a list of all requested typeNames, only those that
     *        match the prefix will be a part of this import statement.
     * @param r DOCUMENT ME!
     *
     * @return The namespace element.
     */
    private StringBuffer getNSImport(String prefix, List infos) {
        LOGGER.finer("prefix is " + prefix);

        StringBuffer retBuffer = new StringBuffer("\n  <xs:import namespace=\"");
        String namespace = catalog.getNamespaceSupport().getURI( prefix );
        retBuffer.append(namespace + "\"");
        retBuffer.append("\n        schemaLocation=\"" + wfs.getOnlineResource().toString()
            + "request=DescribeFeatureType&typeName=");

        Iterator i = infos.iterator();

        //boolean first = true;
        while (i.hasNext()) {
        		FeatureTypeInfo info = (FeatureTypeInfo) i.next(); 
            String typeName = info.name();

            if ( typeName.startsWith( prefix + ":" ) ) {
            		retBuffer.append( typeName + ",");
            }
           
            //JD: some of this logic should be fixed by poplulating the 
            // info objects properly, double check
//            if (typeName.startsWith(prefix)
//                    || ((typeName.indexOf(':') == -1)
//                    && prefix.equals(r.getWFS().getData().getDefaultNameSpace()
//                                          .getPrefix()))) {
//                retBuffer.append(typeName + ",");
//            }
        }

        retBuffer.deleteCharAt(retBuffer.length() - 1);
        retBuffer.append("\"/>");

        return retBuffer;
    }

    
    /**
     * Internal method to print just the requested types.  They should all be
     * in the same namespace, that handling should be done before.  This will
     * not do any namespace handling, just prints up either what's in the
     * schema file, or if it's not there then generates the types from their
     * FeatureTypes.  Also appends the global element so that the types can
     * substitute as features.
     *
     * @param requestedTypes The requested table names.
     * @param gs DOCUMENT ME!
     *
     * @return A string of the types printed.
     *
     * @throws WFSException DOCUMENT ME!
     *
     * @task REVISIT: We need a way to make sure the extension bases are
     *       correct. should likely add a field to the info.xml in the
     *       featureTypes folder, that optionally references an extension base
     *       (should it be same namespace? we could also probably just do an
     *       import on the extension base).  This function then would see if
     *       the typeInfo has an extension base, and would add or import the
     *       file appropriately, and put the correct substitution group in
     *       this function.
     */
    private String generateSpecifiedTypes( List infos )
        throws WFSException {
        //TypeRepository repository = TypeRepository.getInstance();
        String tempResponse = new String();
        
        String generatedType = new String();
        Set validTypes = new HashSet();

        // Loop through requested tables to add element types
        for (int i = 0; i < infos.size(); i++) {
        		FeatureTypeInfo ftInfo = (FeatureTypeInfo) infos.get( i );
			
        		if (!validTypes.contains( ftInfo )) {

                File schemaFile = ftInfo.getSchemaFile();
               try {
                   //Hack here, schemaFile should not be null, but it is
                   //when a fType is first created, since we only add the 
                   //schemaFile param to dto on a load.  This should be
                   //fixed, maybe even have the schema file persist, or at
                   //the very least be present right after creation.
                   if (schemaFile != null &&
                        schemaFile.exists() && schemaFile.canRead()) {
                       generatedType = writeFile(schemaFile);
                   } else {
                       FeatureType ft2 = ftInfo.getFeatureType();
                       String gType2 = generateFromSchema(ft2);

                        if ((gType2 != null) && (gType2 != "")) {
                            generatedType = gType2;
                        }
                   }
                } catch (IOException e) {
                    generatedType = "";
                }

                if (!generatedType.equals("")) {
                    tempResponse = tempResponse + generatedType;
                    validTypes.add(ftInfo);
                }
            }
        }

        // Loop through requested tables again to add elements
        // NOT VERY EFFICIENT - PERHAPS THE MYSQL ABSTRACTION CAN FIX THIS;
        //  STORE IN HASH?
        for (Iterator i = validTypes.iterator(); i.hasNext();) {
            // Print element representation of table
            tempResponse = tempResponse
                + printElement((FeatureTypeInfo) i.next());
        }

        tempResponse = tempResponse + "\n\n";

        return tempResponse;
    }

    /**
     * Transforms a FeatureTypeInfo into gml, with no headers.
     *
     * @param schema the schema to transform.
     *
     * @return DOCUMENT ME!
     *
     * @throws WFSException DOCUMENT ME!
     *
     * @task REVISIT: when this class changes to writing directly to out this
     *       can just take a writer and write directly to it.
     */
    private String generateFromSchema(FeatureType schema)
        throws WFSException {
        try {
            StringWriter writer = new StringWriter();
            FeatureTypeTransformer t = new FeatureTypeTransformer();
            t.setIndentation(4);
            t.setOmitXMLDeclaration(true);
            t.transform(schema, writer);

            return writer.getBuffer().toString();
        } 
        catch (TransformerException te) {
            LOGGER.warning(te.toString());
            throw new WFSException("problem transforming type", te, null);
        }
    }

    /**
     * Internal method to print XML element information for table.
     *
     * @param type The table name.
     *
     * @return The element part of the response.
     */
    private static String printElement(FeatureTypeInfo type) {
    		return "\n  <xs:element name='" + type.getTypeName() + "' type='"
    			+ type.getNamespacePrefix()+":"+type.schemaName() + 
    			"' substitutionGroup='gml:_Feature'/>";
    }

    /**
     * Adds a feature type object to the final output buffer
     *
     * @param inputFileName The name of the feature type.
     *
     * @return The string representation of the file containing the schema.
     *
     * @throws WFSException For io problems reading the file.
     */
    public String writeFile(File inputFile) throws WFSException {
        LOGGER.finest("writing file " + inputFile);

        String finalOutput = new String();

        try {
	    // File inputFile = new File(inputFileName);
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] fileBuffer = new byte[inputStream.available()];
            int bytesRead;

            while ((bytesRead = inputStream.read(fileBuffer)) != -1) {
                String tempOutput = new String(fileBuffer);
                finalOutput = finalOutput + tempOutput;
            }
        } catch (IOException e) {
            //REVISIT: should things fail if there are featureTypes that
            //don't have schemas in the right place?  Because as it is now
            //a describe all will choke if there is one ft with no schema.xml
            throw new WFSException("problem writing featureType information "
                + " from " + inputFile);
        }

        return finalOutput;
    }

    /**
     * Checks that the collection of featureTypeNames all have the same prefix.
     * Used to determine if their schemas are all in the same namespace or if
     * imports need to be done.
     *
     * @param  infos list of feature type info objects..
     *
     * @return true if all the types in the collection have the same prefix.
     *
     * @throws WFSException if any of the names do not exist in this
     *         repository.
     */
    public boolean allSameType( List infos )
        throws WFSException {
        Iterator i = infos.iterator();
        boolean sameType = true;

        if ( !i.hasNext() ) {
            return false;
        }

        FeatureTypeInfo first = (FeatureTypeInfo)  i.next(); 
		
		while ( i.hasNext() ) {
			FeatureTypeInfo ftInfo = (FeatureTypeInfo) i.next();
			if ( !first.getNamespacePrefix().equals( ftInfo.getNamespacePrefix() ) ) {
				return false;
			}
		}
		

        return sameType;
    }
    

}
