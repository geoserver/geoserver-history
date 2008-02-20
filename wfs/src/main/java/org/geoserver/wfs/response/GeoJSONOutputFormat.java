package org.geoserver.wfs.response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import net.opengis.wfs.FeatureCollectionType;
import net.sf.json.JSONException;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

public class GeoJSONOutputFormat extends WFSGetFeatureOutputFormat {
    private final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(this.getClass().toString());
    
    /**
     * WFS configuration
     */
    private WFS wfs;
    
    public static final String FORMAT = "json";
    
    public GeoJSONOutputFormat(WFS wfs) {
        super(FORMAT);
        this.wfs = wfs;
    }
    
    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        return "application/json"; 
    }
    
    public String getCapabilitiesElementName() {
        return "GEOJSON";
    }
    
    @SuppressWarnings("unchecked")
    protected String getContentDisposition(
            FeatureCollectionType featureCollection) {
        
        StringBuffer sb = new StringBuffer();
        for ( Iterator f = featureCollection.getFeature().iterator(); f.hasNext(); ) {
            FeatureCollection<SimpleFeatureType, SimpleFeature> fc;
            fc = (FeatureCollection<SimpleFeatureType, SimpleFeature>) f.next();
            sb.append(fc.getSchema().getTypeName() + "_");
        }
        sb.setLength(sb.length()-1);
        return "inline; filename=" + sb.toString() + ".txt";
        
    }

    @SuppressWarnings("unchecked")
    protected void write(FeatureCollectionType featureCollection,
            OutputStream output, Operation getFeature) throws IOException,
            ServiceException {

        //TODO: investigate setting proper charsets in this
        //it's part of the constructor, just need to hook it up.
        Writer outWriter = new BufferedWriter(new OutputStreamWriter(output));

        GeoJSONBuilder jsonWriter = new GeoJSONBuilder(outWriter);

        // execute should of set all the header information
        // including the lockID
        //
        // execute should also fail if all of the locks could not be aquired
        List<FeatureCollection<SimpleFeatureType, SimpleFeature>> resultsList;
        resultsList = featureCollection.getFeature();

        // FeatureResults[] featureResults = (FeatureResults[]) resultsList
        //    .toArray(new FeatureResults[resultsList.size()]);
        LOGGER.info("about to encode JSON");

        // Generate bounds for every feature?
        boolean featureBounding = wfs.isFeatureBounding();
        boolean hasGeom = false;
        
        try {
            jsonWriter.object().key("type").value("FeatureCollection");
            jsonWriter.key("features");
            jsonWriter.array();
            
            CoordinateReferenceSystem crs = null;            
            for (int i = 0; i < resultsList.size(); i++) {
                FeatureCollection<SimpleFeatureType, SimpleFeature> collection = resultsList.get(i);
                FeatureIterator <SimpleFeature> iterator = collection.features();

                try {
                    SimpleFeatureType fType;
                    List types;

                    while (iterator.hasNext()) {
                        SimpleFeature feature = iterator.next();
                        jsonWriter.object();
                        jsonWriter.key("type").value("SimpleFeature");
                        jsonWriter.key("id").value(feature.getID());
                        
                        fType = feature.getFeatureType();
                        types = fType.getAttributes();
                        
                        AttributeDescriptor defaultGeomType = fType.getDefaultGeometry();

                        if(crs == null && defaultGeomType != null)
                        	crs = fType.getDefaultGeometry().getCRS();
                        
                        jsonWriter.key("geometry");
                        Geometry aGeom = (Geometry) feature.getDefaultGeometry();
                        
                        if (aGeom == null) {
                        	// In case the default geometry is not set, we will just use the first geometry we find
                        	for (int j = 0; j < types.size() && aGeom == null; j++) {
                                Object value = feature.getAttribute(j);
                                if (value != null && value instanceof Geometry) {
                                	aGeom = (Geometry) value;
                                }
                        	}    
                        }
                        // Write the geometry, whether it is a null or not
                        if(aGeom != null) {
                        	jsonWriter.writeGeom(aGeom);
                        	hasGeom = true;
                        } else {
                        	jsonWriter.value(null);
                        }
                        if(defaultGeomType != null)
                        	jsonWriter.key("geometry_name").value(defaultGeomType.getLocalName());

                        jsonWriter.key("properties");
                        jsonWriter.object();

                        for (int j = 0; j < types.size(); j++) {
                            Object value = feature.getAttribute(j);
                            AttributeDescriptor type = (AttributeDescriptor) types.get(j);
                            if (value != null) {
                                if (value instanceof Geometry) {
                                    //This is an area of the spec where they decided to 'let
                                    //convention evolve', that is how to handle multiple 
                                    //geometries.  My take is to print the geometry here if
                                    //it's not the default.  If it's the default that you already
                                    //printed above, so you don't need it here. 
                                    if (type.equals(defaultGeomType)) {
                                        //Do nothing, we wrote it above
                                        //jsonWriter.value("geometry_name");
                                    } else {
                                        jsonWriter.key(type.getLocalName());
                                        jsonWriter.writeGeom((Geometry) value);
                                    }
                                } else {
                                    jsonWriter.key(type.getLocalName());
                                    jsonWriter.value(value);
                                }
                                
                            } else {
                                jsonWriter.key(type.getLocalName());
                                jsonWriter.value(null);
                            }
                        }
                        // Bounding box for feature in properties
                        ReferencedEnvelope refenv = new ReferencedEnvelope(feature.getBounds());
                        if(featureBounding && ! refenv.isEmpty())
                        	jsonWriter.writeBoundingBox(refenv);
                        
                        jsonWriter.endObject(); //end the properties
                        jsonWriter.endObject(); //end the feature
                    }
                } //catch an exception here?
                finally {
                    collection.close(iterator);
                }

                jsonWriter.endArray(); //end features
                
                // Coordinate Referense System, currently only if the namespace is EPSG
                if(crs != null) {
                  	NamedIdentifier namedIdent = (NamedIdentifier) crs.getIdentifiers().iterator().next();
                	String csStr = namedIdent.getCodeSpace().toUpperCase();
                	
                	if(csStr.equals("EPSG")) {
                		jsonWriter.key("crs");
                		jsonWriter.object();
                		jsonWriter.key("type").value(csStr);
                			jsonWriter.key("properties");
                			jsonWriter.object();
                				jsonWriter.key("code");
                				jsonWriter.value(namedIdent.getCode());
                			jsonWriter.endObject(); // end properties
                		jsonWriter.endObject(); // end crs
                	}
                }

                // Bounding box for featurecollection
                if(hasGeom)
                	jsonWriter.writeBoundingBox(collection.getBounds());
                
                jsonWriter.endObject(); // end featurecollection
                outWriter.flush();
            }
        } catch (JSONException jsonException) {
            ServiceException serviceException = 
                new ServiceException("Error: " + jsonException.getMessage());
            serviceException.initCause(jsonException);
            throw serviceException;
        }
        
    }

}
