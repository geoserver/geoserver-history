/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.xacml.role.DefaultRoleAssignmentAuthority;
import org.geoserver.xacml.role.RoleAssignmentAuthority;
import org.geotools.xacml.geoxacml.config.GeoXACML;
import org.geotools.xacml.geoxacml.finder.impl.GeoSelectorModule;
import org.geotools.xacml.transport.XACMLTransport;

import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.AttributeFinderModule;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.finder.impl.CurrentEnvModule;

/**
 * @author Christian Mueller
 * 
 *         GeoXAMCL Configuration for Geoserver environment
 * 
 */
public class GeoXACMLConfig {

    private static PDP pdp;

    private static Object pdpLock = new Object();

    private static XACMLTransportFactory transportFactory;

    private static Object transportFactoryLock = new Object();

    private static RoleAssignmentAuthority raa;

    private static Object raaLock = new Object();

    private static String repositoryBaseDir = null;
    
    static public void reset() {
        synchronized (pdpLock) {
            pdp=null;
        }
    }
    
    static public void setPolicyRepsoitoryBaseDir(String baseDir) {
        repositoryBaseDir=baseDir;
    }

    
    /**
     * @return a XAMCL PDP (Policy Declision Point) for the geoserver environment
     */
    static protected PDP getPDP() {

        synchronized (pdpLock) {
            if (pdp != null)
                return pdp;

            // initialize the geotools part
            GeoXACML.initialize();

            DataDirPolicyFinderModlule policyModule = new DataDirPolicyFinderModlule();
            if (repositoryBaseDir!=null)
                policyModule.setBaseDir(repositoryBaseDir);

            PolicyFinder policyFinder = new PolicyFinder();
            Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
            policyModules.add(policyModule);
            policyFinder.setModules(policyModules);

            // for current time, current date ....
            CurrentEnvModule envModule = new CurrentEnvModule();

            // xpath support
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

    /**
     * Use GeoserverExtensions to create a {@link RoleAssignmentAuthority} If nothing is configured,
     * use {@link DefaultRoleAssignmentAuthority}
     * 
     * @return a RoleAssignmentAuthorty
     */
    static public RoleAssignmentAuthority getRoleAssignmentAuthority() {
        synchronized (raaLock) {
            if (raa != null)
                return raa;
            raa = GeoServerExtensions.bean(RoleAssignmentAuthority.class);
            if (raa == null) {
                raa = new DefaultRoleAssignmentAuthority();
            }
            return raa;
        }

    }

    static public XACMLTransport getXACMLTransport() {
        return getXACMLTransportFacytory().getXACMLTransport();
    }

    static private XACMLTransportFactory getXACMLTransportFacytory() {
        if (transportFactory != null)
            return transportFactory;

        synchronized (transportFactoryLock) {
            if (transportFactory != null)
                return transportFactory;
            transportFactory = GeoServerExtensions.bean(XACMLTransportFactory.class);
            if (transportFactory == null)
                transportFactory = new DefaultXACMLTransportFactory(getPDP(), true);
            return transportFactory;
        }

    }
}
