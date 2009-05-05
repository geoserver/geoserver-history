/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ResourceReference;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.web.data.resource.DataStorePanelInfo;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataAccessFactory;
import org.opengis.coverage.grid.Format;

/**
 * Utility class used to lookup icons for various catalog objects
 */
@SuppressWarnings("serial")
public class CatalogIconFactory implements Serializable {

    public static final ResourceReference RASTER_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/geosilk/raster.png");

    public static final ResourceReference VECTOR_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/geosilk/vector.png");

    public static final ResourceReference UNKNOWN_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/silk/error.png");

    public static final ResourceReference GROUP_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/silk/layers.png");

    public static final ResourceReference DISABLED_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/silk/error.png");

    public static final ResourceReference ENABLED_ICON = new ResourceReference(
            GeoServerBasePage.class, "img/icons/silk/tick.png");

    static final CatalogIconFactory INSTANCE = new CatalogIconFactory();

    public static final CatalogIconFactory get() {
        return INSTANCE;
    }

    private CatalogIconFactory() {
        // private constructor, this is a singleton
    }

    /**
     * Returns the appropriate icon for the specified layer
     * 
     * @param info
     * @return
     */
    public ResourceReference getLayerIcon(LayerInfo info) {
        ResourceReference icon = UNKNOWN_ICON;
        if (info.getType() == Type.VECTOR)
            icon = VECTOR_ICON;
        else if (info.getType() == Type.RASTER)
            icon = RASTER_ICON;
        return icon;
    }

    /**
     * Returns the appropriate icon for the specified store.
     * 
     * @param storeInfo
     * @return
     * @see #getStoreIcon(Class)
     */
    public ResourceReference getStoreIcon(final StoreInfo storeInfo) {
        Class<?> factoryClass;

        Catalog catalog = storeInfo.getCatalog();
        final ResourcePool resourcePool = catalog.getResourcePool();

        if (storeInfo instanceof DataStoreInfo) {
            DataAccessFactory dataStoreFactory;
            try {
                dataStoreFactory = resourcePool.getDataStoreFactory((DataStoreInfo) storeInfo);
            } catch (IOException e) {
                throw new RuntimeException("Can't aquire the datastore factory for store "
                        + storeInfo.getName(), e);
            }
            factoryClass = dataStoreFactory.getClass();
        } else if (storeInfo instanceof CoverageStoreInfo) {
            AbstractGridFormat format = resourcePool
                    .getGridCoverageFormat((CoverageStoreInfo) storeInfo);
            factoryClass = format.getClass();
        } else {
            throw new IllegalStateException(storeInfo.getClass().getName());
        }
        return getStoreIcon(factoryClass);
    }

    /**
     * Returns the appropriate icon given a data access or coverage factory class.
     * <p>
     * The lookup is performed by first searching for a registered {@link DataStorePanelInfo} for
     * the given store factory class, if not found, the icon for the {@link DataStorePanelInfo}
     * registered with the id {@code defaultVector} or {@code defaultRaster}, as appropriate will be
     * used.
     * </p>
     * 
     * @param factoryClass
     *            either a {@link DataAccessFactory} or a {@link Format} class
     * @return
     */
    public ResourceReference getStoreIcon(Class<?> factoryClass) {
        // look for the associated panel info if there is one
        final List<DataStorePanelInfo> infos;
        infos = GeoServerApplication.get().getBeansOfType(DataStorePanelInfo.class);

        for (DataStorePanelInfo panelInfo : infos) {
            if (factoryClass.equals(panelInfo.getFactoryClass())) {
                return new ResourceReference(panelInfo.getIconBase(), panelInfo.getIcon());
            }
        }

        if (DataAccessFactory.class.isAssignableFrom(factoryClass)) {
            // search for the declared default vector store icon
            for (DataStorePanelInfo panelInfo : infos) {
                if ("defaultVector".equals(panelInfo.getId())) {
                    return new ResourceReference(panelInfo.getIconBase(), panelInfo.getIcon());
                }
            }

            // fall back on generic vector icon otherwise
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/database_vector.png");

        } else if (Format.class.isAssignableFrom(factoryClass)) {
            // search for the declared default coverage store icon
            for (DataStorePanelInfo panelInfo : infos) {
                if ("defaultRaster".equals(panelInfo.getId())) {
                    return new ResourceReference(panelInfo.getIconBase(), panelInfo.getIcon());
                }
            }

            // fall back on generic raster icon otherwise
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/page_white_raster.png");
        }
        throw new IllegalArgumentException("Unrecognized store format class: " + factoryClass);
    }
    

    /**
     * Returns a reference to a general purpose icon to indicate an enabled/properly configured
     * resource
     */
    public ResourceReference getEnabledIcon() {
        return ENABLED_ICON;
    }

    /**
     * Returns a reference to a general purpose icon to indicate a
     * disabled/missconfigured/unreachable resource
     */
    public ResourceReference getDisabledIcon() {
        return DISABLED_ICON;
    }
}
