/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.response.v1_1_0;

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfsv.GetDiffType;
import org.apache.xml.serialize.OutputFormat;
import org.eclipse.emf.common.util.EList;
import org.geoserver.ows.Response;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.data.postgis.FeatureDiff;
import org.geotools.data.postgis.FeatureDiffReader;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;


/**
 * WFS output format for a GetDiff operation whose output format is a WFS 1.1
 * transaction
 *
 * @author Andrea Aime, TOPP
 *
 */
public class GetDiffTransactionOutputFormat extends Response {
    /**
     * WFS configuration
     */
    WFS wfs;
    Data catalog;

    /**
     * Xml configuration
     */
    WFSConfiguration configuration;

    /**
     * Filter factory used to build fid filters
     */
    FilterFactory filterFactory;

    public GetDiffTransactionOutputFormat(WFS wfs, Data catalog, WFSConfiguration configuration,
        FilterFactory filterFactory) {
        super(FeatureDiffReader[].class);

        this.wfs = wfs;
        this.configuration = configuration;
        this.catalog = catalog;
        this.filterFactory = filterFactory;
    }

    /**
     * @return "text/xml";
     */
    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "text/xml; subtype=wfs-transaction/1.1.0";
    }

    /**
     * Checks that the resultType is of type "hits".
     */
    public boolean canHandle(Operation operation) {
        GetDiffType request = (GetDiffType) OwsUtils.parameter(operation.getParameters(),
                GetDiffType.class);

        return (request != null)
        && request.getOutputFormat().equals("text/xml; subtype=wfs-transaction/1.1.0");
    }

    public void write(Object value, OutputStream output, Operation operation)
        throws IOException, ServiceException {
        final FeatureDiffReader[] diffReaders = (FeatureDiffReader[]) value;

        // create a new feature collcetion type with just the numbers
        final TransactionType transaction = WfsFactory.eINSTANCE.createTransactionType();

        for (int i = 0; i < diffReaders.length; i++) {
            final FeatureDiffReader diffReader = diffReaders[i];

            // create a single insert element, a single delete element, and as
            // many update elements as needed
            final SimpleFeatureType schema = diffReader.getSchema();
            final QName typeName = new QName(schema.getNamespace().getAuthority(),
                    schema.getTypeName());
            final Set deletedIds = new HashSet();
            final InsertElementType insert = WfsFactory.eINSTANCE.createInsertElementType();

            while (diffReader.hasNext()) {
                FeatureDiff diff = diffReader.next();

                switch (diff.getState()) {
                case FeatureDiff.INSERTED:
                    insert.getFeature().add(diff.getFeature());

                    break;

                case FeatureDiff.DELETED:
                    deletedIds.add(filterFactory.featureId(diff.getID()));

                    break;

                case FeatureDiff.UPDATED:

                    final UpdateElementType update = WfsFactory.eINSTANCE.createUpdateElementType();
                    final EList properties = update.getProperty();

                    SimpleFeature f = diff.getFeature();

                    for (Iterator it = diff.getChangedAttributes().iterator(); it.hasNext();) {
                        final PropertyType property = WfsFactory.eINSTANCE.createPropertyType();
                        String name = (String) it.next();
                        property.setName(new QName(name));
                        property.setValue(f.getAttribute(name));
                        properties.add(property);
                    }

                    FeatureId featureId = filterFactory.featureId(diff.getID());
                    final Filter filter = filterFactory.id(Collections.singleton(featureId));
                    update.setFilter(filter);
                    update.setTypeName(typeName);
                    transaction.getUpdate().add(update);

                    break;

                default:
                    throw new WFSException("Could not handle diff type " + diff.getState());
                }
            }

            // create insert and delete elements if needed
            if (insert.getFeature().size() > 0) {
                transaction.getInsert().add(insert);
            }

            if (deletedIds.size() > 0) {
                final DeleteElementType delete = WfsFactory.eINSTANCE.createDeleteElementType();
                delete.setFilter(filterFactory.id(deletedIds));
                delete.setTypeName(typeName);
                transaction.getDelete().add(delete);
            }
        }

        Encoder encoder = new Encoder(configuration, configuration.schema());
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
            ResponseUtils.appendPath(wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd"));

        OutputFormat format = new OutputFormat();
        format.setIndenting(true);
        encoder.setOutputFormat(format);

        // set up schema locations
        // round up the info objects for each feature collection
        HashMap /* <String,Set> */ ns2metas = new HashMap();

        for (int i = 0; i < diffReaders.length; i++) {
            final FeatureDiffReader diffReader = diffReaders[i];
            final SimpleFeatureType featureType = diffReader.getSchema();

            // load the metadata for the feature type
            String namespaceURI = featureType.getName().getNamespaceURI();
            FeatureTypeInfo meta = catalog.getFeatureTypeInfo(featureType.getName().getLocalPart(),
                    namespaceURI);

            // add it to the map
            Set metas = (Set) ns2metas.get(namespaceURI);

            if (metas == null) {
                metas = new HashSet();
                ns2metas.put(namespaceURI, metas);
            }

            metas.add(meta);
        }

        // declare application schema namespaces
        for (Iterator i = ns2metas.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            String namespaceURI = (String) entry.getKey();
            Set metas = (Set) entry.getValue();

            StringBuffer typeNames = new StringBuffer();

            for (Iterator m = metas.iterator(); m.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
                typeNames.append(meta.getName());

                if (m.hasNext()) {
                    typeNames.append(",");
                }
            }

            // set the schema location
            encoder.setSchemaLocation(namespaceURI,
                ResponseUtils.appendQueryString(wfs.getOnlineResource().toString(),
                    "service=WFS&version=1.1.0&request=DescribeFeatureType&typeName="
                    + typeNames.toString()));
        }

        try {
            System.out.println(transaction);
            encoder.encode(transaction, org.geoserver.wfs.xml.v1_1_0.WFS.TRANSACTION, output);
        } catch (SAXException e) {
            throw (IOException) new IOException("Encoding error ").initCause(e);
        } finally {
        	for (int i = 0; i < diffReaders.length; i++) {
				diffReaders[i].close();
			}
        }
    }
}
