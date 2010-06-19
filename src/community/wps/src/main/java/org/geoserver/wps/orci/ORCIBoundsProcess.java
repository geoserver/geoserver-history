/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.orci;

import static org.geoserver.wps.orci.ORCIProcessFactory.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.text.Text;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

/**
 * Simple process with a {@link ReferencedEnvelope} as the output
 * @author Andrea Aime
 */
public class ORCIBoundsProcess implements org.geotools.process.Process, IORCIProcess {
    
    public InternationalString getDescription() {
        return Text.text("Computes the overlall bounds of the input features");
    }

    public Map<String, Parameter<?>> getParameterInfo() {
        Map<String, Parameter<?>> paramInfo = new HashMap<String, Parameter<?>>();
        paramInfo.put("features", new Parameter("features", FeatureCollection.class, Text.text("Features"), Text.text("The FeatureCollection whose bounds will be computed"), MandatoryParameter));
        return paramInfo;

    }

    public Map<String, Parameter<?>> getResultInfo(Map<String, Object> inputs) {
        Map<String, Parameter<?>> outputInfo = new HashMap<String, Parameter<?>>();
        outputInfo.put("bounds", new Parameter("bounds", ReferencedEnvelope.class, Text.text("Bounds"), Text.text("The feature collection bounds")));
        return outputInfo;
    }

    public InternationalString getTitle() {
        return Text.text("bounds");
    }

    public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor)
            throws ProcessException {
        FeatureCollection fc = (FeatureCollection) input.get("features");
        return Collections.singletonMap("bounds", (Object) fc.getBounds());
    }

}
