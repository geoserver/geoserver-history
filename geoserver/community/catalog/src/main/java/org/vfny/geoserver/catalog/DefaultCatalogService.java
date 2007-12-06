/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.catalog.requests.AddCoverageRequest;
import org.vfny.geoserver.catalog.requests.AddFeatureTypeRequest;
import org.vfny.geoserver.catalog.requests.DeleteFeatureTypeRequest;
import org.vfny.geoserver.catalog.requests.UpdateRequest;
import org.vfny.geoserver.catalog.responses.AddCoverageResponse;
import org.vfny.geoserver.catalog.responses.AddFeatureTypeResponse;
import org.vfny.geoserver.catalog.responses.DeleteFeatureTypeResponse;
import org.vfny.geoserver.catalog.responses.UpdateResponse;
import org.vfny.geoserver.catalog.servlets.AddCoverage;
import org.vfny.geoserver.catalog.servlets.AddFeatureType;
import org.vfny.geoserver.catalog.servlets.DeleteFeatureType;
import org.vfny.geoserver.catalog.servlets.Update;


public class DefaultCatalogService implements CatalogService,
    ApplicationContextAware {
    /**
     * Application context
     */
    ApplicationContext context;

    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        this.context = context;
    }

    public AddCoverageResponse addCoverage(AddCoverageRequest request) {
        AddCoverage addCoverage = (AddCoverage) context.getBean(
                "catalogAddCoverage");

        return (AddCoverageResponse) addCoverage.getResponse();
    }

    public AddFeatureTypeResponse addFeatureType(AddFeatureTypeRequest request) {
        AddFeatureType addFeatureType = (AddFeatureType) context.getBean(
                "catalogAddFeatureType");

        return (AddFeatureTypeResponse) addFeatureType.getResponse();
    }

    public DeleteFeatureTypeResponse deleteFeatureType(
        DeleteFeatureTypeRequest request) {
        DeleteFeatureType deleteFeatureType = (DeleteFeatureType) context
            .getBean("catalogDeleteFeatureType");

        return (DeleteFeatureTypeResponse) deleteFeatureType.getResponse();
    }

    public UpdateResponse updateCatalog(UpdateRequest request) {
        Update updateCatalog = (Update) context.getBean("catalogUpdateCatalog");

        return (UpdateResponse) updateCatalog.getResponse();
    }
}
