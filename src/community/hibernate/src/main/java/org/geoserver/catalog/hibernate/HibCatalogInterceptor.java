package org.geoserver.catalog.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.event.impl.CatalogPostModifyEventImpl;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Hibernate interceptor which forwards hibernate events to the catalog.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class HibCatalogInterceptor extends EmptyInterceptor implements ApplicationContextAware {

    private final static Logger LOGGER = Logging.getLogger(HibCatalogInterceptor.class);

    /**
     * 
     */
    private static final long serialVersionUID = -9046884809650001060L;

    /**
     * application context
     */
    static ApplicationContext applicationContext;

    /**
     * catalog
     */
    static HibCatalogImpl catalog;

    public HibCatalogInterceptor(HibCatalogImpl catalog) {
        // LOGGER.warning("----------------**> creating Interceptor w/ catalog" );
        this.catalog = catalog;
    }

    public HibCatalogInterceptor() {
        // LOGGER.warning("----------------**> creating empty Interceptor" );
    }

    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        // LOGGER.warning("----------------**> setting AC in  Interceptor" );
        applicationContext = ac;
    }

    public void setCatalog(HibCatalogImpl ctlg) {
        catalog = ctlg;
    }

    protected HibCatalogImpl catalog() {
        if (catalog == null) {
            synchronized (this) {
                if (catalog == null) {
                    catalog = (HibCatalogImpl) applicationContext.getBean("catalogTarget");
                }
            }
        }

        return catalog;
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        // TODO Auto-generated method stub
        super.afterTransactionBegin(tx);
        // LOGGER.info("afterTransactionBegin");
    }

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        // TODO Auto-generated method stub
        super.beforeTransactionCompletion(tx);
        // LOGGER.info("beforeTransactionCompletion");
    }

    public void afterTransactionCompletion(Transaction tx) {
        if (!tx.wasRolledBack()) {
            // LOGGER.warning("TRANSACTION COMMITTED");
            // catalog().event(new CatalogPostModifyEventImpl());
            // fireEvents(tx);
        } else {
            LOGGER.warning("TRANSACTION ROLLED BACK");
            // catalog().rollbackEvents(tx);
        }
    }

    public boolean onSave(CatalogInfo entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) {
        if (id == null) {
            // new object
            catalog().fireAdded(entity);
        }
        return false;
    }

    public void onDelete(CatalogInfo entity, Serializable id, Object[] state,
            String[] propertyNames, Type[] types) {
        // catalog().fireRemoved(entity);
    }

    public boolean onFlushDirty(CatalogInfo entity, Serializable id, Object[] currentState,
            Object[] previousState, String[] propertyNames, Type[] types) {
        List<String> propertyNamesChanged = new ArrayList<String>();
        List<Object> oldValues = new ArrayList<Object>();
        List<Object> newValues = new ArrayList<Object>();

        if (previousState != null) {
            for (int i = 0; i < propertyNames.length; i++) {
                Object oldValue = previousState[i];
                Object newValue = currentState[i];

                if (!Utilities.equals(oldValue, newValue)) {
                    propertyNamesChanged.add(propertyNames[i]);
                    oldValues.add(oldValue);
                    newValues.add(newValue);
                }
            }
        } else {
            for (int i = 0; i < propertyNames.length; i++) {
                propertyNamesChanged.add(propertyNames[i]);
            }

        }
        catalog().fireModified(entity, propertyNamesChanged, oldValues, newValues);
        return false;
    }
    //	
}
