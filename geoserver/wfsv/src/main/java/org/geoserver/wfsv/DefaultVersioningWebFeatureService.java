/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfsv.GetDiffType;
import net.opengis.wfsv.GetLogType;
import org.geoserver.wfs.DefaultWebFeatureService;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.data.postgis.FeatureDiffReader;
import org.vfny.geoserver.global.Data;


/**
 * Default implementation of the versioned feature service
 *
 * @author aaime
 */
public class DefaultVersioningWebFeatureService extends DefaultWebFeatureService
    implements VersionedWebFeatureService {
    public DefaultVersioningWebFeatureService(WFS wfs, Data catalog) {
        super(wfs, catalog);
    }

    public TransactionResponseType transaction(TransactionType request)
        throws WFSException {
        VersioningTransaction transaction = new VersioningTransaction(wfs,
                catalog, context);
        transaction.setFilterFactory(filterFactory);

        return transaction.transaction(request);
    }

    public FeatureCollectionType getLog(GetLogType request) {
        GetLog log = new GetLog(wfs, catalog);

        return log.run(request);
    }

    public FeatureDiffReader[] getDiff(GetDiffType request) {
        GetDiff diff = new GetDiff(wfs, catalog);

        return diff.run(request);
    }
}
