package org.openplans.geoserver.wms.bindings;

import java.util.List;
import java.util.Map;

import org.openplans.geoserver.binding.KvpBinding;
import org.openplans.geoserver.binding.KvpRequestReader;

import com.vividsolutions.jts.geom.Envelope;

public class BBoxBinding extends KvpBinding {

	public BBoxBinding(List keys, String key) {
		super(keys, key);
	}

	public Object bind(Map kvp) {
		Envelope bbox = new Envelope();
		
		String bboxParam = (String) kvp.get("bbox");
        Object[] bboxValues = KvpRequestReader
        	.readFlat(bboxParam, KvpRequestReader.INNER_DELIMETER).toArray();

        if (bboxValues.length != 4) {
        	String msg = bboxParam + " is not a valid pair of coordinates"; 
            throw new IllegalArgumentException(msg);
        }

        try {
            double minx = Double.parseDouble(bboxValues[0].toString());
            double miny = Double.parseDouble(bboxValues[1].toString());
            double maxx = Double.parseDouble(bboxValues[2].toString());
            double maxy = Double.parseDouble(bboxValues[3].toString());
            bbox = new Envelope(minx, maxx, miny, maxy);

            if (minx > maxx) {
            	String msg = "illegal bbox, minX: " + minx + " is greater " 
            		+ "than maxX: " + maxx;
                throw new IllegalArgumentException(msg);
            }

            if (miny > maxy) {
            	String msg = "illegal bbox, minY: " + miny + " is greater "
            		+ "than maxY: " + maxy;
                throw new IllegalArgumentException(msg);
            }
        } 
        catch (NumberFormatException ex) {
        	String msg = "Illegal value for BBOX parameter: " + bboxParam; 
        	new IllegalArgumentException(msg).initCause(ex);
        }

        return bbox;
	}

}
