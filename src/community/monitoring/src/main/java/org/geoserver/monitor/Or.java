package org.geoserver.monitor;

import java.util.List;

public class Or extends CompositeFilter {
    public Or(Filter... filters) {
        super(filters);
    }

    public Or(List<Filter> filters) {
        super(filters);
    }
}
