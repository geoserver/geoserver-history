/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.util.Iterator;

import org.geoserver.platform.ExtensionPriority;
import org.geoserver.security.SecureCatalogImpl.Response;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;

/**
 * The default read only wrapper factory, used as a fallback when no other, more
 * specific factory can be used.
 * <p>
 * <b>Implementation note</b>: this factory uses actual decorator objects to
 * perform the secure wrapping. <br>
 * Proxies and invocation handlers could be used instead, the catch is that they
 * would be likely to fail in an event of a refactoring or the wrapped
 * interfaces. <br>
 * Given that it's security we're talking about, a type safe approach that gives
 * a compile error right away has been preferred.
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class DefaultSecureDataFactory implements SecuredObjectFactory {

    public boolean canSecure(Class clazz) {
        return DataAccess.class.isAssignableFrom(clazz) || DataStore.class.isAssignableFrom(clazz)
                || FeatureSource.class.isAssignableFrom(clazz)
                || FeatureStore.class.isAssignableFrom(clazz)
                || FeatureLocking.class.isAssignableFrom(clazz)
                || FeatureCollection.class.isAssignableFrom(clazz)
                || Iterator.class.isAssignableFrom(clazz)
                || FeatureIterator.class.isAssignableFrom(clazz);
    }

    public Object secure(Object object, WrapperPolicy policy) {
        // null check
        if (object == null)
            return null;

        // wrapping check
        Class clazz = object.getClass();
        if (!canSecure(clazz))
            throw new IllegalArgumentException("Don't know how to wrap objects of class "
                    + object.getClass());

        // scan classes from the most specific to the most general (inheritance
        // wise). Start with data stores and data access, which do provide
        // metadata
        if (DataStore.class.isAssignableFrom(clazz)) {
            return new ReadOnlyDataStore((DataStore) object, policy);
        } else if (DataAccess.class.isAssignableFrom(clazz)) {
            return new ReadOnlyDataAccess((DataAccess) object, policy);
        }

        
        // for FeatureSource and family, we only return writable wrappers if the
        // challenge mode is set to true, otherwise we're hide mode and we
        // should just return a read only wrapper, a FeatureSource
        if (SimpleFeatureSource.class.isAssignableFrom(clazz)) {
            // if the policy is not challenge, since this is a secured object,
            // it must be read only (we don't wrap native objects if not
            // required in order to add a security restriction)
            if (policy.response != Response.CHALLENGE) {
                return new ReadOnlySimpleFeatureSource((SimpleFeatureSource) object, policy);
            } else if (SimpleFeatureLocking.class.isAssignableFrom(clazz)) {
                return new ReadOnlySimpleFeatureLocking((SimpleFeatureLocking) object, policy);
            } else if (SimpleFeatureStore.class.isAssignableFrom(clazz)) {
                return new ReadOnlySimpleFeatureStore((SimpleFeatureStore) object, policy);
            } else if (SimpleFeatureSource.class.isAssignableFrom(clazz)) {
                return new ReadOnlySimpleFeatureSource((SimpleFeatureSource) object, policy);
            }
        } else if (FeatureSource.class.isAssignableFrom(clazz)) {
        
            // if the policy is not challenge, since this is a secured object,
            // it must be read only (we don't wrap native objects if not
            // required in order to add a security restriction)
            if (policy.response != Response.CHALLENGE) {
                return new ReadOnlyFeatureSource((FeatureSource) object, policy);
            } else if (FeatureLocking.class.isAssignableFrom(clazz)) {
                return new ReadOnlyFeatureLocking((FeatureLocking) object, policy);
            } else if (FeatureStore.class.isAssignableFrom(clazz)) {
                return new ReadOnlyFeatureStore((FeatureStore) object, policy);
            } else if (FeatureSource.class.isAssignableFrom(clazz)) {
                return new ReadOnlyFeatureSource((FeatureSource) object, policy);
            }
        }

        // deal with feature collection and family
        if (SimpleFeatureCollection.class.isAssignableFrom(clazz)) {
            return new ReadOnlySimpleFeatureCollection((SimpleFeatureCollection) object, policy);
        } else if (FeatureCollection.class.isAssignableFrom(clazz)) {
            return new ReadOnlyFeatureCollection((FeatureCollection) object, policy);
        } else if (Iterator.class.isAssignableFrom(clazz)) {
            return new ReadOnlyIterator((Iterator) object, policy);
        } else if (SimpleFeatureIterator.class.isAssignableFrom(clazz)) {
            return new ReadOnlySimpleFeatureIterator((SimpleFeatureIterator) object); 
        } else if (FeatureIterator.class.isAssignableFrom(clazz)) {
            return new ReadOnlyFeatureIterator((FeatureIterator) object); 
        }

        // all attempts have been made, we don't know how to handle this object
        throw new IllegalArgumentException("Don't know how to wrap objects of class "
                + object.getClass());
    }

    /**
     * Returns {@link ExtensionPriority#LOWEST} since the wrappers generated by
     * this factory
     */
    public int getPriority() {
        return ExtensionPriority.LOWEST;
    }

}
