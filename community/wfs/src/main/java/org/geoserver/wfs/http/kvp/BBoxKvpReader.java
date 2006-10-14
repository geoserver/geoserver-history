package org.geoserver.wfs.http.kvp;

import java.util.Iterator;
import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

import com.vividsolutions.jts.geom.Envelope;

public class BBoxKvpReader extends KvpParser {

	public BBoxKvpReader() {
		super( "bbox", Envelope.class );
	}
	
	public Object parse( String value ) throws Exception {
		 
         List unparsed = KvpUtils.readFlat( value, KvpUtils.INNER_DELIMETER );
         
         // check to make sure that the bounding box has 4 coordinates
         if (unparsed.size() != 4) {
             throw new IllegalArgumentException("Requested bounding box contains wrong"
                 + "number of coordinates (should have " + "4): " + unparsed.size());
         } 

        	 //if it does, store them in an array of doubles
        	 int j = 0;

        	 Iterator i = unparsed.iterator();
        	 double[] bbox = new double[4];
         while ( i.hasNext() ) {
             try {
            	 	bbox[j] = Double.parseDouble((String) i.next());
                j++;
             } 
             catch (NumberFormatException e) {
                 throw new IllegalArgumentException("Bounding box coordinate " + j
                     + " is not parsable:" + unparsed.get(j));
             }
         }
         
         return new Envelope( bbox[0], bbox[1], bbox[2], bbox[3] );
	}

}
