/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.list.ListView;

@SuppressWarnings("serial")
public class ListPanel extends Panel{
    public ListPanel(String id, ListView list){
        super(id);
        add(list);
    }
}
