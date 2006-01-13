/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.FilterType;
import org.geotools.filter.GeometryFilter;
import org.geotools.map.MapLayer;
import org.vfny.geoserver.wms.WMSMapContext;

import com.vividsolutions.jts.geom.Envelope;


/**
 * 
 */
public class EncodeKML {
    /** Standard Logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map.kml");
    
    /** the XML and KML header */
    private static final String KML_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\t"
            +"<kml xmlns=\"http://earth.google.com/kml/2.0\">\n";
    
    /** the KML closing element */
    private static final String KML_FOOTER = "</kml>\n";

	/**
	 * Map context document - layers, styles aoi etc.
	 * 
	 * @uml.property name="mapContext"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private WMSMapContext mapContext;

	/**
	 * Actualy writes the KML out
	 * 
	 * @uml.property name="writer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private KMLWriter writer;

    
    /** Flag to be monotored by writer loops */
    private boolean abortProcess;
    
    /**
     * Creates a new EncodeKML object.
     *
     * @param mapContext A full description of the map to be encoded.
     */
    public EncodeKML(WMSMapContext mapContext) {
        this.mapContext = mapContext;
    }
    
    /**
     * Sets the abort flag.  Active encoding may be halted, but this is not garanteed.
     */
    public void abort() {
        abortProcess = true;
    }
    
    /**
     * Perform the actual encoding.  May return early if abort it called.
     *
     * @param out Ouput stream to send the data to.
     *
     * @throws IOException Thrown if anything goes wrong whilst writing
     */
    public void encode(final OutputStream out) throws IOException {
        Envelope env = this.mapContext.getAreaOfInterest();
        this.writer = new KMLWriter(out, mapContext);
        //once KML supports bbox queries against WMS this can be used to 
        //decimate the geometries based on zoom level.
        //writer.setMinCoordDistance(env.getWidth() / 1000);
        
        abortProcess = false;
        
        long t = System.currentTimeMillis();
        try {
            writeHeader();
            writeLayers();
            writeFooter();
            
            this.writer.flush();
            t = System.currentTimeMillis() - t;
            LOGGER.fine("KML generated, it took" + t + " ms");
        } catch (IOException ioe) {
            if (abortProcess) {
                LOGGER.fine("KML encoding aborted");
                return;
            } else {
                throw ioe;
            }
        } catch (AbortedException ex) {
            return;
        }
    }
    
    /**
     * writes out standard KML header
     *
     * @throws IOException
     */
    private void writeHeader() throws IOException {
        writer.write(KML_HEADER);
    }
   
    /**
     * writes out standard KML footer
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeFooter() throws IOException {
        writer.write(KML_FOOTER);
    }
    
    
    /**
     * Processes each of the layers within the current mapContext in turn.
     * 
     * @throws IOException
     * @throws AbortedException
     *
     * @task TODO: Wrap each layer in a 'Folder' tag
     */
    private void writeLayers() throws IOException, AbortedException {
        MapLayer[] layers = mapContext.getLayers();
        int nLayers = layers.length;
        int defMaxDecimals = writer.getMaximunFractionDigits();
        
        FilterFactory fFac = new FilterFactoryImpl();
        for (int i = 0; i < nLayers; i++) {
            MapLayer layer = layers[i];
            FeatureReader featureReader = null;
            FeatureSource fSource = layer.getFeatureSource();
            FeatureType schema = fSource.getSchema();
            try {
                Expression bboxExpression = fFac.createBBoxExpression(mapContext
                        .getAreaOfInterest());
                GeometryFilter bboxFilter = fFac.createGeometryFilter(FilterType.GEOMETRY_INTERSECTS);
                bboxFilter.addLeftGeometry(bboxExpression);
                bboxFilter.addRightGeometry(fFac.createAttributeExpression(
                        schema, schema.getDefaultGeometry().getName()));
                
                Query bboxQuery = new DefaultQuery(schema.getTypeName(),
                        bboxFilter);
                
                featureReader = fSource.getFeatures(bboxQuery).reader();
                FeatureCollection fc = fSource.getFeatures(bboxQuery);
                writer.writeFeatures(fc, layer.getStyle());
                LOGGER.fine("finished writing");
            } catch (IOException ex) {
                LOGGER.info("process failed: " + ex.getMessage());
                throw ex;
            } catch (AbortedException ae) {
                LOGGER.info("process aborted: " + ae.getMessage());
                throw ae;
            } catch (Throwable t) {
                LOGGER.warning("UNCAUGHT exception: " + t.getMessage());
                
                IOException ioe = new IOException("UNCAUGHT exception: "
                        + t.getMessage());
                ioe.setStackTrace(t.getStackTrace());
                throw ioe;
            } finally {
                if (featureReader != null) {
                    try{
                        featureReader.close();
                    }catch(IOException ioe){
                        //featureReader was probably closed already.
                    }
                }
            }
        }
    }
}
