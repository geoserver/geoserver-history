/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.csv;

import java.io.File;
import java.util.List;

import org.geoserver.platform.ServiceException;
import org.geotools.data.jdbc.JDBCDataStore;

/**
 * Given a CSV file takes the necessary actions to create/override the layers,
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class LayerGenerator {
    public LayerGenerator(JDBCDataStore targetDataStore, DDLDelegate ddlDelegate) {
        // ...
    }

    public List<LayerResult> loadCsvFile(File csvFile) throws ServiceException {
        return null;
    }
}
