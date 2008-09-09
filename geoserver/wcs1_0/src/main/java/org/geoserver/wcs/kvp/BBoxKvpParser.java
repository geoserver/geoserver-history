/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import java.util.List;

import org.geoserver.ows.util.KvpUtils;
import org.geoserver.platform.ServiceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class BBoxKvpParser extends WcsKvpParser {
    public BBoxKvpParser() {
        super("bbox", GeneralEnvelope.class);
    }

    public Object parse(String value) throws Exception {
        List unparsed = KvpUtils.readFlat(value, KvpUtils.INNER_DELIMETER);

        // check to make sure that the bounding box has 4 coordinates
        if (unparsed.size() < 4) {
            throw new IllegalArgumentException("Requested bounding box contains wrong"
                + "number of coordinates (should have " + "4): " + unparsed.size());
        }

        //if it does, store them in an array of doubles
        double[] bbox = new double[unparsed.size()];

        for (int i = 0; i < unparsed.size(); i++) {
            try {
                bbox[i] = Double.parseDouble((String) unparsed.get(i));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Bounding box coordinate " + i
                    + " is not parsable:" + unparsed.get(i));
            }
        }
        
        //ensure the values are sane
        double minx = bbox[0];
        double miny = bbox[1];
        double maxx = bbox[2];
        double maxy = bbox[3];
        
        double minz = 0.0;
        double maxz = 0.0;
        
        if (minx > maxx) {
            throw new ServiceException("illegal bbox, minX: " + minx + " is "
                + "greater than maxX: " + maxx);
        }

        if (miny > maxy) {
            throw new ServiceException("illegal bbox, minY: " + miny + " is "
                + "greater than maxY: " + maxy);
        }

        //check for crs
//        CoordinateReferenceSystem crs = null;

//        if (unparsed.size() > 4) {
//            crs = CRS.decode((String) unparsed.get(4));
//        } else {
//            //TODO: use the default crs of the system
//        }

//        return new ReferencedEnvelope(minx,maxx,miny,maxy,crs);
        if (unparsed.size() > 4) {
            minz = bbox[4];
            maxz = unparsed.size() == 5 ? minz : bbox[5];
            return new GeneralEnvelope(new double[] {minx, miny, minz}, new double[] {maxx, maxy, maxz});
        } else
            return new GeneralEnvelope(new double[] {minx, miny}, new double[] {maxx, maxy});
    }
}
