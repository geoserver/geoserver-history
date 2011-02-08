package org.geoserver.monitor;

import java.util.List;

public class And extends CompositeFilter {

    public And(Filter... filters) {
        super(filters);
    }

    public And(List<Filter> filters) {
        super(filters);
    }

}
