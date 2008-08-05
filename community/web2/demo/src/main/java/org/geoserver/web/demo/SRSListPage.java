/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.geoserver.web.GeoServerBasePage;
import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.crs.GeoserverCustomWKTFactory;

public class SRSListPage extends GeoServerBasePage {

    
    private static final int MAX_ROWS = 20;

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.demo");

    private static CRSAuthorityFactory customFactory = ReferencingFactoryFinder
            .getCRSAuthorityFactory("EPSG", new Hints(Hints.CRS_AUTHORITY_FACTORY,
                    GeoserverCustomWKTFactory.class));

    public SRSListPage() {
        List<String> codeList = new ArrayList<String>(buildCodeList());
        Collections.sort(codeList, new CodeComparator());
        PageableListView srsList = new PageableListView("srslist", codeList, MAX_ROWS) {

            @Override
            protected void populateItem(ListItem item) {
                // odd/even style
                item.add(new SimpleAttributeModifier("class", item.getIndex() % 2 == 0 ? "even"
                        : "odd"));

                // grab the code 
                String code = (String) item.getModelObject();
                final BookmarkablePageLink link = new BookmarkablePageLink("codeLink", SRSDescriptionPage.class, new PageParameters("code=EPSG:" + code) );
                link.add(new Label("code", code));
                item.add(link);
                
                // grab a description
                String description = "-";
                try {
                    description = CRS.getAuthorityFactory(true).getDescriptionText("EPSG:" + code).toString(getLocale());
                } catch(Exception e) {
                    LOGGER.log(Level.FINE, "Could not get the description for srs code EPSG:{0}", code);
                }
                item.add(new Label("description", description));
            }
        };
        srsList.setOutputMarkupId(true);
        WebMarkupContainer container  = new WebMarkupContainer("listContainer");
        container.setOutputMarkupId(true);
        container.add(srsList);
        add(container);
        AjaxPagingNavigator navigator = new AjaxPagingNavigator("navigator", srsList);
        add(navigator);
    }

    List<String> buildCodeList() {
        Set<String> codes = CRS.getSupportedCodes("EPSG");

        try {
            codes.addAll(customFactory.getAuthorityCodes(CoordinateReferenceSystem.class));
        } catch (FactoryException e) {
            LOGGER.log(Level.WARNING, "Error occurred while trying to gather custom CRS codes", e);
        }

        // make a set with each code
        Set<String> idSet = new HashSet<String>();
        for (String code : codes) {
            String id = code.substring(code.indexOf(':') + 1); // just the non
                                                               // prefix part
            idSet.add(id);
        }
        List<String> ids = new ArrayList<String>(idSet);
        Collections.sort(ids); // sort to get them in order
        return ids;
    }
    
    public class CodeComparator implements Comparator<String> {

        public int compare(String s1, String s2) {
            Integer c1 = null, c2 = null;
            try {
                c1 = Integer.parseInt(s1);
            } catch(NumberFormatException e) {
                //
            }
            try {
                c2 = Integer.parseInt(s2);
            } catch(NumberFormatException e) {
                //
            }
            if(c1 == null) {
                if(c2 == null)
                    return s1.compareTo(s2);
                else
                    return -1;
            } else {
                if(c2 == null)
                    return 1;
                else
                    return c1 - c2;
            }
        }

    }


}
