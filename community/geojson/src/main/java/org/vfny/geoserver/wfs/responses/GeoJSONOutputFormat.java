package org.vfny.geoserver.wfs.responses;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import net.opengis.wfs.FeatureCollectionType;
import net.sf.json.JSONException;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class GeoJSONOutputFormat extends WFSGetFeatureOutputFormat {

    public static final String FORMAT = "json";
    
    public GeoJSONOutputFormat() {
        super(FORMAT);
        
    }
    
    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        return "applications/json"; 
    }
    
    protected String getContentDisposition(
            FeatureCollectionType featureCollection) {
        
        StringBuffer sb = new StringBuffer();
        for ( Iterator f = featureCollection.getFeature().iterator(); f.hasNext(); ) {
            FeatureCollection fc = (FeatureCollection) f.next();
            sb.append(fc.getSchema().getTypeName() + "_");
        }
        sb.setLength(sb.length()-1);
        return "inline; filename=" + sb.toString() + ".txt";
        
    }

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
        List resultsList = featureCollection.getFeature();

        //FeatureResults[] featureResults = (FeatureResults[]) resultsList
        //    .toArray(new FeatureResults[resultsList.size()]);
        LOGGER.info("about to encode JSON");

        try {
            jsonWriter.object().key("type").value("FeatureCollection");
            //TODO: Add CRS stuff.
            jsonWriter.key("members");
            jsonWriter.array();

            for (int i = 0; i < resultsList.size(); i++) {
                FeatureCollection collection = (FeatureCollection) resultsList.get(i);

                FeatureIterator iterator = collection.features();

                try {
                    FeatureType fType;
                    AttributeType[] types;

                    while (iterator.hasNext()) {
                        Feature feature = iterator.next();
                        jsonWriter.object();
                        jsonWriter.key("type").value("Feature");
                        jsonWriter.key("id");
                        jsonWriter.value(feature.getID());

                        fType = feature.getFeatureType();
                        types = fType.getAttributeTypes();

                        AttributeType defaultGeomType = fType.getDefaultGeometry();
                        jsonWriter.key("geometry");

                        if (feature.getDefaultGeometry() != null) {
                            jsonWriter.writeGeom(feature.getDefaultGeometry());
                        } else {
                            jsonWriter.value("null");
                        }

                        jsonWriter.key("geometry_name").value(defaultGeomType.getName());
                        jsonWriter.key("properties");
                        jsonWriter.object();

                        for (int j = 0; j < types.length; j++) {
                            Object value = feature.getAttribute(j);

                            if (value != null) {
                                if (value instanceof Geometry) {
                                    //This is an area of the spec where they decided to 'let
                                    //convention evolve', that is how to handle multiple 
                                    //geometries.  My take is to print the geometry here if
                                    //it's not the default.  If it's the default that you already
                                    //printed above, so you don't need it here.
                                    if (types[j].equals(defaultGeomType)) {
                                        //Do nothing, we wrote it above
                                        //jsonWriter.value("geometry_name");
                                    } else {
                                        jsonWriter.key(types[j].getName());
                                        jsonWriter.writeGeom((Geometry) value);
                                    }
                                } else {
                                    jsonWriter.key(types[j].getName());
                                    jsonWriter.value(value);
                                }
                            } else {
                                jsonWriter.key(types[j].getName());
                                jsonWriter.value("null");
                            }
                        }

                        jsonWriter.endObject(); //end the properties
                        jsonWriter.endObject(); //end the feature
                    }
                } //catch an exception here?
                finally {
                    collection.close(iterator);
                }

                jsonWriter.endArray();
                jsonWriter.endObject();

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
