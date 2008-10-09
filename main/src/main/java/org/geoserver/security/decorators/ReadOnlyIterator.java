/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.util.Iterator;

import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.Response;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;

public class ReadOnlyIterator implements Iterator {
    Iterator wrapped;

    WrapperPolicy policy;

    public ReadOnlyIterator(Iterator wrapped, WrapperPolicy policy) {
        this.wrapped = wrapped;
        this.policy = policy;
    }

    public boolean hasNext() {
        return wrapped.hasNext();
    }

    public Object next() {
        return wrapped.next();
    }

    public void remove() {
        throw unsupportedOperation();
    }

    /**
     * Notifies the caller the requested operation is not supported, using a
     * plain {@link UnsupportedOperationException} in case we have to conceal
     * the fact the data is actually writable, using an Acegi security exception
     * otherwise to force an authentication from the user
     */
    RuntimeException unsupportedOperation() {
        if (policy.response == Response.CHALLENGE) {
            return SecureCatalogImpl.unauthorizedAccess();
        } else
            return new UnsupportedOperationException("This iterator is read only");
    }

}