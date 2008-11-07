package org.geoserver.wps;

import java.util.Map;

import net.opengis.ows11.CodeType;
import net.opengis.wps.InputType;
import net.opengis.wps.WpsFactory;

import org.geoserver.wps.xml.ComplexDataTypeBinding;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSConfiguration;

/**
 * Utility methods for Web Processing Service module.
 * 
 * @author Lucas Reed, Refractions Research
 * @author Justin Deoliveira, OpenGEO
 *
 */
public class WPSUtils {

    /**
     * factory instance for wps model objects
     */
    static WpsFactory f = WpsFactory.eINSTANCE;
    
    /**
     * Looks up a process factory by its identifier, returning null if no 
     * such factory can be found.
     * 
     */
    public static ProcessFactory findProcessFactory(CodeType name) {
        for (ProcessFactory pf : Processors.getProcessFactories()) {
            if (pf.getName().equals(name.getValue())) {
                return pf;
            }
        }

        return null;
    }
}
