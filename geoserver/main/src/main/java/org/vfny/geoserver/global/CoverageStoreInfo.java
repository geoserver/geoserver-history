/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterNotFoundException;
import org.vfny.geoserver.global.dto.CoverageStoreInfoDTO;
import org.vfny.geoserver.util.CoverageStoreUtils;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is the configuration iformation for one coverage Format.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class CoverageStoreInfo extends GlobalLayerSupertype {
    /** for logging */
    private static final Logger LOGGER = Logger.getLogger(CoverageStoreInfo.class.toString());

    /**
     * CoverageStoreInfo we are representing
     */
    private Format format = null;

    /**
     * ref to the parent class's collection
     */
    private Data data;

    /**
     *
     */
    private String id;
    private SoftReference reader = null;
    private SoftReference hintReader = null;

    /**
     *
     */
    private String nameSpaceId;

    /**
     *
     */
    private String type;

    /**
     *
     */
    private String url;
    private boolean enabled;

    /**
     *
     */
    private String title;
    private String _abstract;

    /**
     * Storage for metadata
     */
    private Map meta;

    /**
     * CoverageStoreInfo constructor.
     *
     * <p>
     * Stores the specified data for later use.
     * </p>
     *
     * @param config
     *            CoverageStoreInfoDTO the current configuration to use.
     * @param data
     *            Data a ref to use later to look up related informtion
     */
    public CoverageStoreInfo(CoverageStoreInfoDTO config, Data data) {
        this.data = data;
        meta = new HashMap(10);
        enabled = config.isEnabled();
        id = config.getId();
        nameSpaceId = config.getNameSpaceId();
        type = config.getType();
        url = config.getUrl();
        title = config.getTitle();
        _abstract = config.getAbstract();
        format = lookupFormat();
    }

    private Format lookupFormat() {
        final Format[] formats = CoverageStoreUtils.formats;

        for (int i = 0; i < formats.length; i++) {
            final Format tempFormat = CoverageStoreUtils.formats[i];

            if (tempFormat.getName().equals(type)) {
                return tempFormat;
            }
        }

        return null;
    }

    /**
     * toDTO purpose.
     *
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with extreme
     * caution.
     * </p>
     *
     * @return CoverageStoreInfoDTO the generated object
     */
    Object toDTO() {
        CoverageStoreInfoDTO dto = new CoverageStoreInfoDTO();
        dto.setAbstract(_abstract);
        dto.setEnabled(enabled);
        dto.setId(id);
        dto.setNameSpaceId(nameSpaceId);
        dto.setType(type);
        dto.setUrl(url);
        dto.setTitle(title);

        return dto;
    }

    /**
     * getId purpose.
     *
     * <p>
     * Returns the format's id.
     * </p>
     *
     * @return String the id.
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME !
     *
     * @return Format
     *
     * @throws IllegalStateException
     *             if this CoverageStoreInfo is disabled by configuration
     * @throws NoSuchElementException
     *             if no CoverageStoreInfo is found
     */
    public Format getFormat() throws IllegalStateException, NoSuchElementException {
        if (!isEnabled()) {
            throw new IllegalStateException("this format is not enabled, check your configuration");
        }

        if (format == null) {
            LOGGER.warning("failed to establish connection with " + toString());
            throw new NoSuchElementException("No format found capable of managing " + toString());
        }

        return format;
    }

    /**
     * getTitle purpose.
     *
     * <p>
     * Returns the dataStore's title.
     * </p>
     *
     * @return String the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * getAbstract purpose.
     *
     * <p>
     * Returns the dataStore's abstract.
     * </p>
     *
     * @return String the abstract.
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * isEnabled purpose.
     *
     * <p>
     * Returns true when the data store is enabled.
     * </p>
     *
     * @return true when the data store is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Implement toString.
     *
     * @return String
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new StringBuffer("FormatConfig[type=").append(getType()).append(", enabled=")
                                                     .append(isEnabled()).append(", abstract=")
                                                     .append(getAbstract()).append("]").toString();
    }

    /**
     * Implement containsMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
     */
    public boolean containsMetaData(String key) {
        return meta.containsKey(key);
    }

    /**
     * Implement putMetaData.
     *
     * @param key
     * @param value
     *
     * @see org.geotools.data.MetaData#putMetaData(java.lang.String,
     *      java.lang.Object)
     */
    public void putMetaData(String key, Object value) {
        meta.put(key, value);
    }

    /**
     * Implement getMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
     */
    public Object getMetaData(String key) {
        return meta.get(key);
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * getNameSpace purpose.
     *
     * @return NameSpaceInfo the namespace for this format.
     */
    public NameSpaceInfo getNameSpace() {
        return (NameSpaceInfo) data.getNameSpace(getNamesSpacePrefix());
    }

    /**
     * Access namespace id
     *
     * @return DOCUMENT ME!
     */
    public String getNamesSpacePrefix() {
        return nameSpaceId;
    }

    public synchronized GridCoverageReader getReader() {
        // /////////////////////////////////////////////////////////
        //
        // Trying to leverage cached GridCoverageReader
        //
        // /////////////////////////////////////////////////////////
        if ((reader != null) && (reader.get() != null)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Reusing cached GridCoverageReader");
            }

            return (GridCoverageReader) reader.get();
        }

        try {
            // /////////////////////////////////////////////////////////
            //
            // Getting coverage reader using the format and the real path.
            //
            // /////////////////////////////////////////////////////////
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Creating a new GridCoverageReader");
            }

            final File obj = GeoserverDataDirectory.findDataFile(getUrl());

            // XXX CACHING READERS HERE
            reader = new SoftReference(((AbstractGridFormat) getFormat()).getReader(obj));

            return (GridCoverageReader) reader.get();
        } catch (InvalidParameterValueException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (ParameterNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (SecurityException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return null;
    }

    public synchronized GridCoverageReader createReader(Hints hints) {
        // /////////////////////////////////////////////////////////
        //
        // Trying to leverage cached GridCoverageReader
        //
        // /////////////////////////////////////////////////////////
        if ((hintReader != null) && (hintReader.get() != null)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Reusing cached GridCoverageReader");
            }

            return (GridCoverageReader) hintReader.get();
        } else if ((hints == null) && ((reader != null) && (reader.get() != null))) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Reusing cached GridCoverageReader");
            }

            return (GridCoverageReader) reader.get();
        }

        try {
            // /////////////////////////////////////////////////////////
            //
            // Getting coverage reader using the format and the real path.
            //
            // /////////////////////////////////////////////////////////
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Creating a new GridCoverageReader");
            }

            final File obj = GeoserverDataDirectory.findDataFile(getUrl());

            // XXX CACHING READERS HERE
            hintReader = new SoftReference(((AbstractGridFormat) getFormat()).getReader(obj, hints));

            return (GridCoverageReader) hintReader.get();
        } catch (InvalidParameterValueException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (ParameterNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (SecurityException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return null;
    }
}
