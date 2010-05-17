/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geoserver.gwc;

import java.util.Iterator;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.wfs.TransactionEvent;
import org.geoserver.wfs.TransactionListener;
import org.geoserver.wfs.WFSException;
import org.geotools.util.logging.Logging;

public class GWCTransactionListener implements TransactionListener {
    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWCTransactionListener");
    
    final private Catalog cat;
    
    final private GWCCleanser cleanser;
    
    public GWCTransactionListener(Catalog cat, GWCCleanser cleanser) {
        this.cat = cat;
        this.cleanser = cleanser;
    }
    
 // TODO limit to subset
    public void dataStoreChange(TransactionEvent event) throws WFSException {
        String prefix = null;
        String layerName = null;
        
        try {
            prefix = cat.getNamespaceByURI(event.getLayerName().getNamespaceURI()).getPrefix();
            layerName = prefix +":"+ event.getLayerName().getLocalPart();
        } catch(NullPointerException npe) {
            log.fine("Null pointer while trying to determine feature prefix. Cache not truncated.");
            return;
        }
        
        // The layer itself
        cleanser.deleteLayer(layerName);
        
        // Now we check for layer groups that are affected
        Iterator<LayerGroupInfo> lgiter = cat.getLayerGroups().iterator();
        while(lgiter.hasNext()) {
            boolean truncate = false;
            LayerGroupInfo lgi = lgiter.next();
            //System.out.println(lgi.getName());
            
            // First we check for referenced to affected layers
            Iterator<LayerInfo> liter = lgi.getLayers().iterator();
            while(! truncate && liter.hasNext()) {
                LayerInfo li = liter.next();
                //System.out.println("   " + li.getResource().getPrefixedName());
                if(li.getResource().getPrefixedName().equals(layerName)) {
                    truncate = true;
                }
            }
            
            if(truncate) {
                cleanser.deleteLayer(lgi.getName());
            }
            // Next layer group
        }        
    }
}
