/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.util.Iterator;

import org.geoserver.catalog.impl.AbstractDecorator;
import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.Response;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;

public class ReadOnlyIterator extends AbstractDecorator<Iterator> implements Iterator {
    WrapperPolicy policy;

    public ReadOnlyIterator(Iterator wrapped, WrapperPolicy policy) {
        super(wrapped);
        this.policy = policy;
    }

    public boolean hasNext() {
        return delegate.hasNext();
    }

    public Object next() {
        return delegate.next();
    }

    public void remove() {
        throw unsupportedOperation();
    }

    /**
     * Notifies the caller the requested operation is not supported, using a
     * plain {@link UnsupportedOperationException} in case we have to conceal
     * the fact the data is actually writable, using an Spring security exception
     * otherwise to force an authentication from the user
     */
    RuntimeException unsupportedOperation() {
        if (policy.response == Response.CHALLENGE) {
            return SecureCatalogImpl.unauthorizedAccess();
        } else
            return new UnsupportedOperationException("This iterator is read only");
    }

}