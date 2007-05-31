package org.geoserver.wfs;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.vfny.geoserver.global.FeatureTypeInfo;

/**
 * Processes standard update elements
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class UpdateElementHandler implements TransactionElementHandler {

    /**
     * logger
     */
    static Logger LOGGER = Logger.getLogger("org.geoserver.wfs");

    private WFS wfs;

    public UpdateElementHandler(WFS wfs) {
        this.wfs = wfs;
    }

    public void checkValidity(EObject element, Map typeInfos) throws WFSTransactionException {
        // check inserts are enabled
        if ((wfs.getServiceLevel() & WFS.SERVICE_UPDATE) == 0) {
            throw new WFSException("Transaction Update support is not enabled");
        }

        // check that all required properties have a specified value
        UpdateElementType update = (UpdateElementType) element;

        try {
            FeatureTypeInfo meta = (FeatureTypeInfo) typeInfos.values().iterator().next();
            FeatureType featureType = meta.getFeatureType();

            for (Iterator prop = update.getProperty().iterator(); prop.hasNext();) {
                PropertyType property = (PropertyType) prop.next();

                if (property.getValue() == null) {
                    String propertyName = property.getName().getLocalPart();
                    AttributeType attributeType = featureType.getAttributeType(propertyName);

                    if ((attributeType != null) && (attributeType.getMinOccurs() > 0)) {
                        String msg = "Property '" + attributeType.getName()
                                + "' is mandatory but no value specified.";
                        throw new WFSException(msg, "MissingParameterValue");
                    }
                }
            }
        } catch (IOException e) {
            throw new WFSTransactionException("Could not locate feature type information for "
                    + update.getTypeName(), e, update.getHandle());
        }
    }

    public void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException {
        UpdateElementType update = (UpdateElementType) element;
        QName elementName = update.getTypeName();
        String handle = update.getHandle();
        long updated = response.getTransactionSummary().getTotalUpdated().longValue();

        FeatureStore store = (FeatureStore) featureStores.get(elementName);

        if (store == null) {
            throw new WFSException("Could not locate FeatureStore for '" + elementName + "'");
        }

        LOGGER.finer("Transaction Update:" + element);

        try {
            Filter filter = (Filter) update.getFilter();

            AttributeType[] types = new AttributeType[update.getProperty().size()];
            Object[] values = new Object[update.getProperty().size()];

            for (int j = 0; j < update.getProperty().size(); j++) {
                PropertyType property = (PropertyType) update.getProperty().get(j);
                types[j] = store.getSchema().getAttributeType(property.getName().getLocalPart());
                values[j] = property.getValue();
            }

            // Pass through data to collect fids and damaged
            // region
            // for validation
            //
            Set fids = new HashSet();
            LOGGER.finer("Preprocess to remember modification as a set of fids");

            FeatureCollection features = store.getFeatures(filter);
            Iterator preprocess = features.iterator();

            try {
                while (preprocess.hasNext()) {
                    Feature feature = (Feature) preprocess.next();
                    fids.add(feature.getID());

                    // envelope.expandToInclude(feature.getBounds());
                }
            } catch (NoSuchElementException e) {
                throw new WFSException("Could not aquire FeatureIDs", e);
            } finally {
                features.close(preprocess);
            }

            try {
                if (types.length == 1) {
                    store.modifyFeatures(types[0], values[0], filter);
                } else {
                    store.modifyFeatures(types, values, filter);
                }
            } finally {
                // make sure we unlock
                if ((request.getLockId() != null) && store instanceof FeatureLocking
                        && (request.getReleaseAction() == AllSomeType.SOME_LITERAL)) {
                    FeatureLocking locking = (FeatureLocking) store;
                    locking.unLockFeatures(filter);
                }
            }

            // Post process - check features for changed boundary and
            // pass them off to the ValidationProcessor
            if (!fids.isEmpty()) {
                LOGGER.finer("Post process update for boundary update and featureValidation");

                FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
                Set featureIds = new HashSet();

                for (Iterator f = fids.iterator(); f.hasNext();) {
                    featureIds.add(ff.featureId((String) f.next()));
                }

                Id modified = ff.id(featureIds);

                FeatureCollection changed = store.getFeatures(modified);

                // envelope.expandToInclude(changed.getBounds());

                // featureValidation(typeInfo.getDataStore().getId(),store.getSchema(),
                // changed);
            }

            // update the update counter
            updated += fids.size();
        } catch (IOException ioException) {
            // JD: changing from throwing service exception to
            // adding action that failed
            throw new WFSTransactionException(ioException, null, handle);
        }

        // update transaction summary
        response.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(updated));
    }

    public Class getElementClass() {
        return UpdateElementType.class;
    }

    public QName[] getTypeNames(EObject element) throws WFSTransactionException {
        return new QName[] { ((UpdateElementType) element).getTypeName() };
    }
}
