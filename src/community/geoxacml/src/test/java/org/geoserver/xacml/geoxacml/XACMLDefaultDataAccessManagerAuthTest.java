/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import org.geoserver.security.DataAccessManager;
import org.geoserver.security.DefaultDataAccessManagerAuthTest;
import org.geoserver.xacml.security.XACMLDataAccessManager;

public class XACMLDefaultDataAccessManagerAuthTest extends DefaultDataAccessManagerAuthTest {

    @Override
    protected DataAccessManager buildManager(String propertyFile) throws Exception {

        if ("wideOpen.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/wideOpen/");
        }
        if ("publicRead.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/publicRead/");
        }
        if ("lockedDownHide.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/lockedDownHide/");
        }
        if ("lockedDownMixed.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/lockedDownMixed/");
        }
        if ("lockedDownChallenge.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/lockedDownChallenge/");
        }
        if ("lockedDown.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/lockedDown/");
        }
        if ("complex.properties".equals(propertyFile)) {
            GeoXACMLConfig.setPolicyRepsoitoryBaseDir("src/test/resources/complex/");
        }

        GeoXACMLConfig.reset();
        return new XACMLDataAccessManager();

    }

    @Override
    public void testUnknownMode() throws Exception {
        // already tested with "lockedDown"
    }

}
