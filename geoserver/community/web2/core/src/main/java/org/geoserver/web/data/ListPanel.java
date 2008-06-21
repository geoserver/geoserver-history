package org.geoserver.web.data;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.list.ListView;

public class ListPanel extends Panel{
    public ListPanel(String id, ListView list){
        super(id);
        add(list);
    }
}
