package org.geoserver.web.util;

import java.util.Map;

import org.apache.wicket.model.IModel;

/**
 * A model which backs onto an underlying map.
 * <p>
 * The semantics of this model are similar to {@link #PropertyModel} except for
 * that expressions map to keys of a map rather than java bean property names.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class MapModel implements IModel {

    Map map;
    String expression;
    
    public MapModel( Map map, String expression ) {
        this.map = map;
        this.expression = expression;
    }
    
    public Object getObject() {
        return map.get( expression );
    }

    public void setObject(Object object) {
        map.put( expression, object );
    }

    public void detach() {
    }

}
