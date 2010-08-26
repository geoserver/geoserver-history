package org.geoserver.monitor.ows.wfs;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.geotools.xml.EMFUtils;

public class TransactionHandler extends WFSRequestObjectHandler {

    public TransactionHandler() {
        super("net.opengis.wfs.TransactionType");
    }

    @Override
    public List<String> getLayers(Object request) {
        FeatureMap elements = (FeatureMap) EMFUtils.get((EObject)request, "group");
        
        List<String> layers = new ArrayList();
        ListIterator i = elements.valueListIterator();
        while(i.hasNext()) {
            Object e = i.next();
            if (EMFUtils.has((EObject)e, "typeName")) {
                Object typeName = EMFUtils.get((EObject)e, "typeName");
                if (typeName != null) {
                    layers.add(toString(typeName));
                }
            }
        }
        
        return layers;
    }

}
