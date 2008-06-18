package org.geoserver.web.util;

import java.util.Map;

import org.apache.wicket.model.IModel;

/**
 * A model which backs onto an underlying map.
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class MapModel implements IModel {

    Map map;
    String key;
    
    public MapModel( Map map, String key ) {
        this.map = map;
        this.key = key;
    }
    
    public Object getObject() {
        return map.get( key );
    }

    public void setObject(Object object) {
        map.put( key, object );
    }

    public void detach() {
    }

}
