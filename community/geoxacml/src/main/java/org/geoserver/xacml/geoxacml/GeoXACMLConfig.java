/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.xacml.geoxacml.config.GeoXACML;
import org.geotools.xacml.geoxacml.finder.impl.GeoSelectorModule;

import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.AttributeFinderModule;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.finder.impl.CurrentEnvModule;

public class GeoXACMLConfig {

    private static PDP pdp;
    
    static synchronized public  PDP getPDP() {
        if (pdp!=null) return pdp;
        
        GeoXACML.initialize();
        
        DataDirPolicyFinderModlule policyModule = new DataDirPolicyFinderModlule();
        
                         
        PolicyFinder policyFinder = new PolicyFinder();
        Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
        policyModules.add(policyModule);
        policyFinder.setModules(policyModules);

        CurrentEnvModule envModule = new CurrentEnvModule();
        GeoSelectorModule selectorModule = new GeoSelectorModule();
        
        AttributeFinder attrFinder = new AttributeFinder();
        List<AttributeFinderModule> attrModules = new ArrayList<AttributeFinderModule>();
        attrModules.add(envModule);
        attrModules.add(selectorModule);
        attrFinder.setModules(attrModules);
 
        pdp = new PDP(new PDPConfig(attrFinder, policyFinder, null));
        return pdp;
}
    
    
    
}
