/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;

import org.geoserver.security.DataAccessManager;
import org.geoserver.security.SecureCatalogImplTest;
import org.geoserver.xacml.security.XACMLDataAccessManager;

public class XACMLSecureCatalogImplTest extends SecureCatalogImplTest {

    @Override
    public void testComplex() throws Exception {
        //TODO
    }

    @Override
    public void testLockedChallenge() throws Exception {
      //TODO
    }

    @Override
    public void testPublicRead() throws Exception {
      //TODO
    }


    @Override
    protected DataAccessManager buildManager(String propertyFile) throws Exception {
        
        
        if ("wideOpen.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/wideOpen/");
        }        
        GeoXACMLConfig.reset();
        return new XACMLDataAccessManager();
        
    }

    @Override
    public void testLockedDown() throws Exception {
        //TODO
    }

    @Override
    public void testLockedMixed() throws Exception {
        //TODO
    }

}
