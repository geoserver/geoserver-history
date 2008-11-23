package org.geoserver.wfsv.response.v1_0_0;

import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.xml.v1_0_0.WFSConfiguration;
import org.geoserver.wfsv.response.v1_1_0.AbstractTransactionOutputFormat;
import org.geotools.xml.Encoder;
import org.opengis.filter.FilterFactory;
import org.vfny.geoserver.global.Data;

public class TransactionOutputFormat extends AbstractTransactionOutputFormat {

    public TransactionOutputFormat(WFS wfs, Data catalog, WFSConfiguration configuration,
            FilterFactory filterFactory) {
        super(wfs, catalog, configuration, filterFactory,
                org.geoserver.wfs.xml.v1_0_0.WFS.TRANSACTION,
                "text/xml; subtype=wfs-transaction/1.0.0");

    }

    protected void encodeTypeSchemaLocation(Encoder encoder, String proxifiedBaseUrl,
            String namespaceURI, StringBuffer typeNames) {
        encoder.setSchemaLocation(namespaceURI, ResponseUtils.appendQueryString(proxifiedBaseUrl
                + "wfs", "service=WFS&version=1.0.0&request=DescribeFeatureType&typeName="
                + typeNames.toString()));
    }

    protected void encodeWfsSchemaLocation(Encoder encoder, String proxifiedBaseUrl) {
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, ResponseUtils
                .appendPath(proxifiedBaseUrl, "schemas/wfs/1.0.0/WFS-transaction.xsd"));
    }

}
