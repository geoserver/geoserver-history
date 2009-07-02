/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.xacml.rbac;

import java.net.URI;
import java.util.Set;

import com.sun.xacml.PolicyMetaData;
import com.sun.xacml.VersionConstraints;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.finder.PolicyFinderResult;

/**
 * 
 * Abbreviations: PPS Permission Policy Set RPS Role Policy Set
 * 
 * @author christian
 * 
 */
public class RBACPolicyFinder extends PolicyFinder {

    private PolicyFinder ppsFinder = new PolicyFinder();;

    @Override
    public void init() {
        super.init();
        ppsFinder.init();
    }

    /*
     * @see com.sun.xacml.finder.PolicyFinder#getModules() throws nsupportedOperationException
     */
    @Override
    public Set<PolicyFinderModule> getModules() {
        throw new UnsupportedOperationException("Not available for RBAC");
    }

    /*
     * @see com.sun.xacml.finder.PolicyFinder#setModules(java.util.Set) throws
     * UnsupportedOperationException
     */
    @Override
    public void setModules(Set<PolicyFinderModule> modules) {
        throw new UnsupportedOperationException("Not available for RBAC");

    }

    public Set<PolicyFinderModule> getPPSModules() {
        return ppsFinder.getModules();
    }

    public void setPPSModules(Set<PolicyFinderModule> modules) {
        PolicyFinderModule module = checkPPSModules(modules);
        if (module != null)
            throw new IllegalArgumentException("Permission Policy Set finder module :"
                    + module.getIdentifier() + "does not support find by reference");

        ppsFinder.setModules(modules);
    }

    /**
     * 
     * @return the RPS Modules Uses the functionallity of the base class
     */
    public Set<PolicyFinderModule> getRPSModules() {
        return super.getModules();
    }

    /**
     * @param modules
     *            , the RPS finder modules Uses the functionallity of the base class
     */
    public void setRPSModules(Set<PolicyFinderModule> modules) {
        super.setModules(modules);
    }

    /**
     * Checks if all PPS Module Finders support finding by reference
     * 
     * @param modules
     *            , Set of PPS finder modules
     * @return null if all finder modules support find by reference, otherwise the first module
     *         found not supporting find by reference
     */
    private PolicyFinderModule checkPPSModules(Set<PolicyFinderModule> modules) {
        for (PolicyFinderModule module : modules) {
            if (module.isIdReferenceSupported() == false) {
                return module;
            }
        }
        return null;
    }

    @Override
    public PolicyFinderResult findPolicy(URI idReference, int type, VersionConstraints constraints,
            PolicyMetaData parentMetaData) throws IllegalArgumentException {
        // check for PPS Policies
        PolicyFinderResult result = ppsFinder.findPolicy(idReference, type, constraints,
                parentMetaData);

        if (result.indeterminate())
            return result; // error condition
        if (result.notApplicable() == false) { // we have found exact one policy
            return result;
        }
        // no PPS policy found, delegate to super class
        return super.findPolicy(idReference, type, constraints, parentMetaData);
    }

    public PolicyFinder getPpsFinder() {
        return ppsFinder;
    }
}
