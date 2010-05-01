/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.response;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.apache.commons.io.FileUtils;
import org.geoserver.data.util.IOUtils;
import org.geoserver.feature.RetypingFeatureCollection;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 *
 * This class returns a shapefile encoded results of the users's query.
 *
 * Based on ShapeFeatureResponseDelegate.java from geoserver 1.5.x
 *
 * @author originally authored by Chris Holmes, The Open Planning Project, cholmes@openplans.org
 * @author ported to gs 1.6.x by Saul Farber, MassGIS, saul.farber@state.ma.us
 *
 */
public class ShapeZipOutputFormat extends WFSGetFeatureOutputFormat implements ApplicationContextAware {
    private static final Logger LOGGER = Logging.getLogger(ShapeZipOutputFormat.class);
    private String outputFileName;
    private ApplicationContext applicationContext;
    
    /**
     * Tuple used when fanning out a collection with generic geometry types to multiple outputs 
     * @author Administrator
     *
     */
    private static class StoreWriter {
        DataStore dstore;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
    }

    public ShapeZipOutputFormat() {
        super("SHAPE-ZIP");
    }

    /**
     * @see WFSGetFeatureOutputFormat#getMimeType(Object, Operation)
     */
    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "application/zip";
    }
    
    public String getCapabilitiesElementName() {
        return "SHAPE-ZIP";
    }

    /**
     * We abuse this method to pre-discover the query typenames so we know what to set in the
     * content-disposition header.
     */
    protected boolean canHandleInternal(Operation operation) {
        GetFeatureType request = (GetFeatureType) OwsUtils.parameter(operation.getParameters(),
                GetFeatureType.class);
        outputFileName = ((QName) ((QueryType) request.getQuery().get(0)).getTypeName().get(0))
            .getLocalPart();

        return true;
    }

    public String[][] getHeaders(Object value, Operation operation)
        throws ServiceException {
        return (String[][]) new String[][] {
            { "Content-Disposition", "attachment; filename=" + outputFileName + ".zip" }
        };
    }

    /**
     * @see WFSGetFeatureOutputFormat#write(Object, OutputStream, Operation)
     */
    @SuppressWarnings("unchecked")
    protected void write(FeatureCollectionType featureCollection, OutputStream output,
        Operation getFeature) throws IOException, ServiceException {
        //We might get multiple featurecollections in our response (multiple queries?) so we need to
        //write out multiple shapefile sets, one for each query response.
        File tempDir = IOUtils.createTempDirectory("wfsshptemp");
        
        // target charset
        Charset charset = getShapefileCharset(getFeature);
        try {
            Iterator<SimpleFeatureCollection> outputFeatureCollections;
            outputFeatureCollections = featureCollection.getFeature().iterator();
            SimpleFeatureCollection curCollection;

            // if an emtpy result out of feature type with unknown geometry is created, the
            // zip file will be empty and the zip output stream will break
            boolean shapefileCreated = false;
            while (outputFeatureCollections.hasNext()) {
                curCollection = outputFeatureCollections.next();
                
                if(curCollection.getSchema().getGeometryDescriptor() == null) {
                    throw new WFSException("Cannot write geometryless shapefiles, yet " 
                            + curCollection.getSchema() + " has no geometry field");
                } 
                Class geomType = curCollection.getSchema().getGeometryDescriptor().getType().getBinding();
                if(GeometryCollection.class.equals(geomType) || Geometry.class.equals(geomType)) {
                    // in this case we fan out the output to multiple shapefiles
                    shapefileCreated |= writeCollectionToShapefiles(curCollection, tempDir, charset);
                } else {
                    // simple case, only one and supported type
                    writeCollectionToShapefile(curCollection, tempDir, charset);
                    shapefileCreated = true;
                }

            }
            
            // take care of the case the output is completely empty
            if(!shapefileCreated) {
                SimpleFeatureCollection fc;
                fc = (SimpleFeatureCollection) featureCollection.getFeature().iterator().next();
                fc = remapCollectionSchema(fc, Point.class);
                writeCollectionToShapefile(fc, tempDir, getShapefileCharset(getFeature));
                createEmptyZipWarning(tempDir);
            }
            
            // zip all the files produced
            final FilenameFilter filter = new FilenameFilter() {
            
                public boolean accept(File dir, String name) {
                    return name.endsWith(".shp") || name.endsWith(".shx") || name.endsWith(".dbf")
                           || name.endsWith(".prj") || name.endsWith(".cst");
                }
            };
            ZipOutputStream zipOut = new ZipOutputStream(output);
            IOUtils.zipDirectory(tempDir, zipOut, filter);
            zipOut.finish();

            // This is an error, because this closes the output stream too... it's
            // not the right place to do so
            // zipOut.close();
        } finally {
            // make sure we remove the temp directory and its contents completely now
            try {
                FileUtils.deleteDirectory(tempDir);
            } catch(IOException e) {
                LOGGER.warning("Could not delete temp directory: " + tempDir.getAbsolutePath() + " due to: " + e.getMessage());
            }
        }
    }

    private void createEmptyZipWarning(File tempDir) throws IOException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(tempDir, "README.TXT"));
            pw.print("The query result is empty, and the geometric type of the features is unknwon:" +
            		"an empty point shapefile has been created to fill the zip file");
        } finally {
            pw.close();
        }
    }   

    /**
     * Write one featurecollection to an appropriately named shapefile.
     * @param c the featurecollection to write
     * @param tempDir the temp directory into which it should be written
     */
    private void writeCollectionToShapefile(SimpleFeatureCollection c, File tempDir, Charset charset) {
        c = remapCollectionSchema(c, null);
        
        SimpleFeatureType schema = c.getSchema();
        if(schema.getTypeName().contains(".")) {
        	// having dots in the name prevents various programs to recognize the file as a shapefile
        	SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        	tb.init(c.getSchema());
        	tb.setName(c.getSchema().getTypeName().replace('.', '_'));
        	SimpleFeatureType renamed = tb.buildFeatureType();
        	c = new RetypingFeatureCollection(c, renamed);
        }

        SimpleFeatureStore fstore = null;
        ShapefileDataStore dstore = null;
        try {
            // create attribute name mappings, to be compatible 
            // with shapefile constraints:
            //  - geometry field is always named the_geom
            //  - field names have a max length of 10
            Map<String,String> attributeMappings=createAttributeMappings(c.getSchema());
            // wraps the original collection in a remapping wrapper
            SimpleFeatureCollection remapped = new RemappingFeatureCollection(c,attributeMappings);
            SimpleFeatureType remappedSchema=(SimpleFeatureType)remapped.getSchema();
            dstore = buildStore(tempDir, charset,  remappedSchema); 
            fstore = (SimpleFeatureStore) dstore.getFeatureSource();
            // we need retyping too, because the shapefile datastore
            // could have sorted fields in a different order
            SimpleFeatureCollection retyped = new RetypingFeatureCollection(remapped, fstore.getSchema());
            fstore.addFeatures(retyped);
          
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING,
                "Error while writing featuretype '" + schema.getTypeName() + "' to shapefile.", ioe);
            throw new ServiceException(ioe);
        } finally {
            if(dstore != null) {
                dstore.dispose();
            }
        }
    }
    
    /**
     * Takes a feature collection with a generic schema and remaps it to one whose schema
     * respects the limitations of the shapefile format
     * @param fc
     * @param targetGeometry
     * @return
     */
    SimpleFeatureCollection remapCollectionSchema(SimpleFeatureCollection fc, Class targetGeometry) {
        SimpleFeatureType schema = fc.getSchema();
        
        // having dots in the name prevents various programs to recognize the file as a shapefile
        if(schema.getTypeName().contains(".")) {
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            if(targetGeometry == null) {
                tb.init(schema);
            } else {
                // force generic geometric attributes to the desired geometry type
                for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
                    if(!(ad instanceof GeometryDescriptor)) {
                        tb.add(ad);
                    } else {
                        Class geomType = ad.getType().getBinding();
                        if(geomType.equals(Geometry.class) || geomType.equals(GeometryCollection.class)) {
                            tb.add(ad.getName().getLocalPart(), targetGeometry, 
                                    ((GeometryDescriptor) ad).getCoordinateReferenceSystem());
                        } else {
                            tb.add(ad);
                        }
                    }
                } 
            }
            tb.setName(fc.getSchema().getTypeName().replace('.', '_'));
            SimpleFeatureType renamed = tb.buildFeatureType();
            fc = new RetypingFeatureCollection(fc, renamed);
        }
        
        // create attribute name mappings, to be compatible 
        // with shapefile constraints:
        //  - geometry field is always named the_geom
        //  - field names have a max length of 10
        Map<String,String> attributeMappings = createAttributeMappings(schema);
        return new RemappingFeatureCollection(fc,attributeMappings);
    }
    
    /**
     * Maps schema attributes to shapefile-compatible attributes.
     * 
     * @param schema
     * @return
     */
    private Map<String, String> createAttributeMappings(SimpleFeatureType schema) {
        Map<String, String> result = new HashMap<String,String>();
        
        // track the field names used and reserve "the_geom" for the geometry
        Set<String> usedFieldNames = new HashSet<String>(); 
        usedFieldNames.add("the_geom");
        
        // scan and remap
        for(AttributeDescriptor attDesc : schema.getAttributeDescriptors()) {
            if(attDesc instanceof GeometryDescriptor) {
                result.put(attDesc.getLocalName(), "the_geom");
            } else {
                String name = attDesc.getLocalName();
                result.put(attDesc.getLocalName(), getShapeCompatibleName(usedFieldNames, name));
            }
        }
        return result;
    }
    
    /**
     * If necessary remaps the name so that it's less than 10 chars long and 
     * @param usedFieldNames
     * @param name
     * @return
     */
    String getShapeCompatibleName(Set<String> usedFieldNames, String name) {
        // 10 chars limit
        if(name.length() > 10)
            name = name.substring(0,10);
        
        // don't use an already assigned name, create a new unique name (it might
        // conflict even if we did not cut it to 10 chars due to remaps of previous long attributes)
        int counter=0;
        while(usedFieldNames.contains(name)) {
            String postfix=(counter++)+"";
            name = name.substring(0, name.length() - postfix.length()) + postfix;
        }
        usedFieldNames.add(name);
        
        return name;
    }
       
    /**
     * Write one featurecollection to a group of appropriately named shapefiles, one per geometry
     * type. This method assume the features will have a Geometry type and the actual type of each
     * feature will be discovered during the scan. Each feature will be routed to a shapefile that
     * contains only a specific geometry type chosen among point, multipoint, polygons and lines.
     * @param c the featurecollection to write
     * @param tempDir the temp directory into which it should be written
     * @return true if a shapefile has been created, false otherwise
     */
    private boolean writeCollectionToShapefiles(SimpleFeatureCollection c, File tempDir, Charset charset) {
        c = remapCollectionSchema(c, null);
        SimpleFeatureType schema = c.getSchema();
        
        boolean shapefileCreated = false;
        
        Map<Class, StoreWriter> writers = new HashMap<Class, StoreWriter>();
        SimpleFeatureIterator it;
        try {
            it = c.features(); 
            while(it.hasNext()) {
                SimpleFeature f = it.next();
                
                if(f.getDefaultGeometry() == null) {
                    LOGGER.warning("Skipping " + f.getID() + " as its geometry is null");
                    continue;
                }
                
                FeatureWriter<SimpleFeatureType, SimpleFeature> writer = getFeatureWriter(f, writers, tempDir, charset);
                SimpleFeature fw = writer.next();
                
                // we cannot trust attribute order, shapefile changes the location and name of the geometry
                for (AttributeDescriptor d : fw.getFeatureType().getAttributeDescriptors()) {
                    fw.setAttribute(d.getLocalName(), f.getAttribute(d.getLocalName()));
                }
                fw.setDefaultGeometry(f.getDefaultGeometry());
                writer.write();
                shapefileCreated = true;
            }
            
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING,
                "Error while writing featuretype '" + schema.getTypeName() + "' to shapefile.", ioe);
            throw new ServiceException(ioe);
        } finally {
            // close all writers, dispose all datastores, even if an exception occurs
            // during closeup (shapefile datastore will have to copy the shapefiles, that migh
            // fail in many ways)
            IOException stored = null;
            for (StoreWriter sw : writers.values()) {
                try {
                    sw.writer.close();
                    sw.dstore.dispose();
                } catch(IOException e) {
                    stored = e;
                }
            }
            // if an exception occurred make the world aware of it
            if(stored != null)
                throw new ServiceException(stored);
        }
        
        return shapefileCreated;
    }
    
    /**
     * Returns the feature writer for a specific geometry type, creates a new datastore
     * and a new writer if there are none so far
     */
    private FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(SimpleFeature f, 
            Map<Class, StoreWriter> writers, File tempDir, Charset charset) throws IOException {
        // get the target class
        Class<?> target;
        Geometry g = (Geometry) f.getDefaultGeometry();
        String suffix = null;
        
        if(g instanceof Point) {
            target = Point.class;
            suffix = "Point";
        } else if(g instanceof MultiPoint) {
            target = MultiPoint.class;
            suffix = "MPoint";
        } else if(g instanceof MultiPolygon || g instanceof Polygon) {
            target = MultiPolygon.class;
            suffix = "Polygon";
        } else if(g instanceof LineString || g instanceof MultiLineString) {
            target = MultiLineString.class;
            suffix = "Line";
        } else {
            throw new RuntimeException("This should never happen, " +
            		"there's a bug in the SHAPE-ZIP output format. I got a geometry of type " + g.getClass());
        }
        
        // see if we already have a cached writer
        StoreWriter storeWriter = writers.get(target);
        if(storeWriter == null) {
            // retype the schema
            SimpleFeatureType original = f.getFeatureType();
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor d : original.getAttributeDescriptors()) {
                if(Geometry.class.isAssignableFrom(d.getType().getBinding())) {
                    GeometryDescriptor gd = (GeometryDescriptor) d;
                    builder.add(gd.getLocalName(), target, gd.getCoordinateReferenceSystem());
                    builder.setDefaultGeometry(gd.getLocalName());
                } else {
                    builder.add(d);
                }
            }
            builder.setName(original.getTypeName().replace('.', '_') + suffix);
            builder.setNamespaceURI(original.getName().getURI());
            SimpleFeatureType retyped = builder.buildFeatureType();
            
            // create the datastore for the current geom type
            DataStore dstore = buildStore(tempDir, charset, retyped);
            
            // cache it
            storeWriter = new StoreWriter();
            storeWriter.dstore = dstore;
            storeWriter.writer = dstore.getFeatureWriter(retyped.getTypeName(), Transaction.AUTO_COMMIT);
            writers.put(target, storeWriter);
        }
        return storeWriter.writer;
    }
    
    


    /**
     * Looks up the charset parameter, either in the GetFeature request or as a global parameter
     * @param getFeature
     * @return the found charset, or the platform's default one if none was specified
     */
    private Charset getShapefileCharset(Operation getFeature) {
        Charset result = null;
        
        GetFeatureType gft = (GetFeatureType) getFeature.getParameters()[0];
        if(gft.getFormatOptions() != null && gft.getFormatOptions().get("CHARSET") != null) {
           result = (Charset) gft.getFormatOptions().get("CHARSET");
        } else {
            final String charsetName = GeoServerExtensions.getProperty("GS-SHAPEFILE-CHARSET", applicationContext);
            if(charsetName != null)
                result = Charset.forName(charsetName);
        }
        
        // if not specified let's use the shapefile default one
        return result != null ? result : Charset.forName("ISO-8859-1");
    }

    /**
     * Creates a shapefile data store for the specified schema 
     * @param tempDir
     * @param charset
     * @param schema
     * @return
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private ShapefileDataStore buildStore(File tempDir,
            Charset charset, SimpleFeatureType schema) throws MalformedURLException,
            FileNotFoundException, IOException {
        File file = new File(tempDir, schema.getTypeName() + ".shp");
        ShapefileDataStore sfds = new ShapefileDataStore(file.toURL());
        
        // handle shapefile encoding
        // and dump the charset into a .cst file, for debugging and control purposes
        // (.cst is not a standard extension)
        sfds.setStringCharset(charset);
        File charsetFile = new File(tempDir, schema.getTypeName()+ ".cst");
        PrintWriter pw = null;
        try {
            pw  = new PrintWriter(charsetFile);
            pw.write(charset.name());
        } finally {
            if(pw != null) pw.close();
        }

        try {
            sfds.createSchema(schema);
        } catch (NullPointerException e) {
            LOGGER.warning(
                "Error in shapefile schema. It is possible you don't have a geometry set in the output. \n"
                + "Please specify a <wfs:PropertyName>geom_column_name</wfs:PropertyName> in the request");
            throw new ServiceException(
                "Error in shapefile schema. It is possible you don't have a geometry set in the output.");
        }
        
        try {
            if(schema.getCoordinateReferenceSystem() != null)
                sfds.forceSchemaCRS(schema.getCoordinateReferenceSystem());
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Could not properly create the .prj file", e);
        }

        return sfds;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    
}
