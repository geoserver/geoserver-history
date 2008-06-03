/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.util;

import java.util.IdentityHashMap;

import org.geoserver.catalog.Wrapper;

/**
 * Utility to unwrap a catalog object wrapper
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class WrapperUtils {

    /**
     * Unwraps the wrapper until it finds the latest Wrapper around an original
     * implementation (or bails out after {@link #MAX_UNWRAPS} to avoid infinite loops)
     * 
     * @param <T>
     * @param wrapper
     * @return
     */
    public static <T> T deepUnwrap(Wrapper<T> wrapper) {
        Wrapper<T> current = wrapper;
        
        // store the wrappers we find during the unwrap path to avoid recursion
        IdentityHashMap found = new IdentityHashMap();
        for(;;) {
            if(current == null)
                return null;
            
            if(found.containsKey(current))
                throw new RuntimeException("Wrapper " + wrapper + " has a wrapping loop, the same element" + current
                        + " was found for the second time in the walk");
            else
                found.put(current, current);
            
            // unwrap and check if we are at the end
            T unwrapped = current.unwrap();
            if (unwrapped instanceof Wrapper) {
                current = (Wrapper<T>) unwrapped;
            } else {
                return unwrapped;
            }
        }
        
    }
}
