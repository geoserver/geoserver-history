package org.geoserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * WFS output format for a GetFeature operation in which the outputFormat is "csv".
 *
 * @author Justin Deoliveira, OpenGeo, jdeolive@opengeo.org
 * @author Sebastian Benthall, OpenGeo, seb@opengeo.org
 */
public class CSVOutputFormat extends WFSGetFeatureOutputFormat {

    public CSVOutputFormat() {
        //this is the name of your output format, it is the string
        // that will be used when requesting the format in a 
        // GEtFeature request: 
        // ie ;.../geoserver/wfs?request=getfeature&outputFormat=myOutputFormat
        super("csv");
    }
    
    /**
     * @return "text/csv";
     */
    @Override
    public String getMimeType(Object value, Operation operation)
               throws ServiceException {
         // return the mime type of the format here, the parent 
         // class returns 'text/xml'
         return "text/csv";
    }
    
    @Override
    protected boolean canHandleInternal(Operation operation) {
        //any additional checks that need to be performed to 
        // determine when the output format should be "engaged" 
        // should go here
        return super.canHandleInternal(operation);
    }
    
    /**
     * @see WFSGetFeatureOutputFormat#write(Object, OutputStream, Operation)
     */
    @Override
    protected void write(FeatureCollectionType featureCollection,
            OutputStream output, Operation getFeature) throws IOException,
            ServiceException {
    	   //write out content here
        
        //create a writer
        BufferedWriter w = new BufferedWriter( new OutputStreamWriter( output ) );
                   
        //get the feature collection
        FeatureCollection fc = (FeatureCollection) featureCollection.getFeature().get(0);
           
        //write out the header
        SimpleFeatureType ft = (SimpleFeatureType) fc.getSchema();
        for ( int i = 0; i < ft.getAttributeCount(); i++ ) {
            AttributeDescriptor ad = ft.getDescriptor( i );
            w.write( ad.getLocalName() );
               
            if ( i < ft.getAttributeCount()-1 ) {
               w.write( "," );
            }
        }
        w.write( "\n" );
           
        //write out the features
        FeatureIterator i = fc.features();
        try {
            while( i.hasNext() ) {
                SimpleFeature f = (SimpleFeature) i.next();
                for ( int j = 0; j < f.getAttributeCount(); j++ ) {
                    Object att = f.getAttribute( j );
                    if ( att != null ) {
                        w.write( att.toString() );
                    }
                    if ( j < f.getAttributeCount()-1 ) {
                        w.write(",");    
                    }
                }    
                w.write( "\n" );
            }
        }
        finally {
            fc.close( i );
        }
           
        w.flush();
    }

}
