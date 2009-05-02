/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.geoserver.web.wicket.SRSProvider.SRS;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;

/**
 * A panel which contains a list of all coordinate reference systems available to GeoServer.
 * <p>
 * Using this compontent in a page would look like:
 * 
 * <pre>
 * public class MyPage {
 * 
 *     public MyPage() {
 *     ...
 *     add( new SRSListPanel( &quot;srsList&quot; ) );
 *     ...
 *   }
 * }
 * </pre>
 * 
 * And the markup:
 * 
 * <pre>
 * ...
 *  &lt;body&gt;
 *    &lt;div wicket:id=&quot;srsList&gt;&lt;/div&gt;
 *  &lt;/body&gt;
 *  ...
 * </pre>
 * 
 * </p>
 * <p>
 * Client could should override the method {@link #createLinkForCode(String, IModel)} to provide
 * some action when the user clicks on a SRS code in the list.
 * </p>
 * 
 * @author Andrea Aime, OpenGeo
 * @author Justin Deoliveira, OpenGeo
 * @authos Gabriel Roldan, OpenGeo
 * 
 */
@SuppressWarnings("serial")
public class SRSListPanel extends Panel {
    /**
     * max number of rows to show in the table
     */
    private static final int MAX_ROWS = 25;

    /**
     * logger
     */
    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.demo");


    /**
     * Creates the new SRS list panel.
     */
    public SRSListPanel(String id) {
        this(id, MAX_ROWS);
    }

    /**
     * Creates the new SRS list panel specifying the number of rows.
     */
    public SRSListPanel(String id, int nrows) {
        super(id);

        final GeoServerTablePanel<SRS> table = new GeoServerTablePanel<SRS>("table",
                new SRSProvider()) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<SRS> property) {

                SRS srs = (SRS) itemModel.getObject();

                if (SRSProvider.CODE.equals(property)) {

                    Component linkForCode = createLinkForCode(id, itemModel);

                    return linkForCode;

                } else if (SRSProvider.DESCRIPTION.equals(property)) {
                    String description = srs.getDescription();
                    return new Label(id, description.trim());

                } else {
                    throw new IllegalArgumentException("Unknown property: " + property);
                }
            }

        };

        add(table);
    }

    /**
     * Creates a link for an epsgCode.
     * <p>
     * Subclasses should override to perform an action when an epsg code has been selected. This
     * default implementation returns a link that does nothing.
     * </p>
     * 
     * @param linkId
     *            The id of the link component to be created.
     * @param itemModel
     *            The epsg code (integer).
     * 
     */
    protected Component createLinkForCode(String linkId, IModel itemModel) {
        return new AjaxLink(linkId) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                // do nothing
            }
        };
    }

    List<String> filterCodes(List<String> codes, String[] filters) {
        // if filtering is required, filter against the code and the description
        if (filters != null) {
            List<String> result = new ArrayList<String>();
            for (String code : codes) {
                code = code.toUpperCase();

                // grab the description
                String description = null;
                try {
                    description = CRS.getAuthorityFactory(true).getDescriptionText("EPSG:" + code)
                            .toString(getLocale()).toUpperCase();
                } catch (Exception e) {
                    // no problem
                }

                // check if we have all the keywords matching
                boolean fullMatch = true;
                for (String filter : filters) {
                    filter = filter.toUpperCase();
                    if (!code.contains(filter)
                            && !(description != null && description.contains(filter))) {
                        fullMatch = false;
                        break;
                    }
                }
                if (fullMatch)
                    result.add(code);
            }
            return result;
        } else {
            return codes;
        }
    }
}