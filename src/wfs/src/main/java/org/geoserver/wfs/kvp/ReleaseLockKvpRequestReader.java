/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.kvp;

import org.geoserver.ows.KvpRequestReader;
import java.util.Map;


public class ReleaseLockKvpRequestReader extends KvpRequestReader {
    public ReleaseLockKvpRequestReader() {
        super(String.class);
    }

    public Object createRequest() throws Exception {
        return new String();
    }

    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        return kvp.get("lockId");
    }
}
