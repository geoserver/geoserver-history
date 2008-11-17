package org.geoserver.wps;

import net.opengis.ows11.CodeType;
import net.opengis.wps10.Wps10Factory;

import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;

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
    static Wps10Factory f = Wps10Factory.eINSTANCE;
    
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
