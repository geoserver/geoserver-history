package org.geoserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import net.opengis.wfs.FeatureCollectionType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;


/**
 * WFS output format for a GetFeature operation in which the outputFormat is "excel".
 *
 * @author Sebastian Benthall, OpenGeo, seb@opengeo.org
 */
public class ExcelOutputFormat extends WFSGetFeatureOutputFormat {

    public ExcelOutputFormat() {
        //this is the name of your output format, it is the string
        // that will be used when requesting the format in a 
        // GEtFeature request: 
        // ie ;.../geoserver/wfs?request=getfeature&outputFormat=myOutputFormat
        super("excel");
    }
    
    /**
     * @return "application/msexcel";
     */
    @Override
    public String getMimeType(Object value, Operation operation)
               throws ServiceException {
         // return the mime type of the format here, the parent 
         // class returns 'text/xml'
         return "application/msexcel";
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
    	
        //get the feature collection
        FeatureCollection fc = (FeatureCollection) featureCollection.getFeature().get(0);
    	
        //Create the workbook and sheet
        
    	HSSFWorkbook wb = new HSSFWorkbook();    	
    	HSSFSheet sheet = wb.createSheet("sheet");
    	
  
        //write out the header
    	HSSFRow header = sheet.createRow((short) 0);
    	
        SimpleFeatureType ft = (SimpleFeatureType) fc.getSchema();
        HSSFCell cell;
        
        for ( int i = 0; i < ft.getAttributeCount(); i++ ) {
            AttributeDescriptor ad = ft.getAttribute( i );
            
            cell = header.createCell((short) i);
            cell.setCellValue(ad.getName().toString());
        }
        
        //write out the features
        FeatureIterator i = fc.features();
        int r = 0; // row index
        try {
        	HSSFRow row;
            while( i.hasNext() ) {
            	r++; //start at 1, since header is at 0
                SimpleFeature f = (SimpleFeature) i.next();
                row = sheet.createRow((short) r);
                for ( int j = 0; j < f.getNumberOfAttributes(); j++ ) {
                    Object att = f.getValue( j );
                    if ( att != null ) {
                    	cell = row.createCell((short) j);
                    	cell.setCellValue( att.toString() );
                    }
                }    
            }
        }
        finally {
            fc.close( i );
        }
        
        //write to output
        wb.write(output);
    }
}
