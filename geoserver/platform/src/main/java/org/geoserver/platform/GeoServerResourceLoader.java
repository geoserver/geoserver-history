/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.platform;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;


/**
 * Manages resources in GeoServer.
 * <p>
 * The loader maintains a search path in which it will use to look up resources.
 * The {@link #baseDirectory} is a member of this path.
 * </p>
 * <p>
 * Files and directories created by the resource loader are made relative to
 * {@link #baseDirectory}.
 * </p>
 * <p>
 * <pre>
 *         <code>
 * File dataDirectory = ...
 * GeoServerResourceLoader loader = new GeoServerResourceLoader( dataDirectory );
 * loader.addSearchLocation( new File( "/WEB-INF/" ) );
 * loader.addSearchLocation( new File( "/data" ) );
 * ...
 * File catalog = loader.find( "catalog.xml" );
 *         </code>
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerResourceLoader extends DefaultResourceLoader {
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.global");

    /** "path" for resource lookups */
    Set searchLocations;

    /**
     * Base directory
     */
    File baseDirectory;

    /**
     * Creates a new resource loader with no base directory.
     * <p>
     * Such a constructed resource loader is not capable of creating resources
     * from relative paths.
     * </p>
     */
    public GeoServerResourceLoader() {
        searchLocations = new TreeSet();
    }

    /**
     * Creates a new resource loader.
     *
     * @param baseDirectory The directory in which
     */
    public GeoServerResourceLoader(File baseDirectory) {
        this();
        this.baseDirectory = baseDirectory;
        setSearchLocations(Collections.EMPTY_SET);
    }

    /**
     * Adds a location to the path used for resource lookups.
     *
     * @param A directory containing resources.
     */
    public void addSearchLocation(File searchLocation) {
        searchLocations.add(searchLocation);
    }

    /**
     * Sets the search locations used for resource lookups.
     *
     * @param searchLocations A set of {@link File}.
     */
    public void setSearchLocations(Set searchLocations) {
        this.searchLocations = new HashSet(searchLocations);

        //always add the base directory
        if (baseDirectory != null) {
            this.searchLocations.add(baseDirectory);
        }
    }

    /**
     * @return The base directory.
     */
    public File getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * Sets the base directory.
     *
     * @param baseDirectory
     */
    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;

        searchLocations.add(baseDirectory);
    }

    /**
     * Performs a resource lookup.
     *
     * @param location The name of the resource to lookup, can be absolute or
     * relative.
     *
     * @return The file handle representing the resource, or null if the
     * resource could not be found.
     *
     * @throws IOException In the event of an I/O error.
     */
    public File find(String location) throws IOException {
        //first to an existance check
        File file = new File(location);

        if (file.isAbsolute()) {
            return file;
        } else {
            //try a relative url
            for (Iterator f = searchLocations.iterator(); f.hasNext();) {
                File base = (File) f.next();
                file = new File(base, location);

                try {
                    if (file.exists()) {
                        return file;
                    }
                } catch (SecurityException e) {
                    LOGGER.warning("Failed attemp to check existance of " + file.getAbsolutePath());
                }
            }
        }

        Resource resource = getResource(location);

        if (resource.exists()) {
            return resource.getFile();
        }

        return null;
    }

    /**
     * Creates a new directory.
     * <p>
     * Relative paths are created relative to {@link #baseDirectory}.
     * If {@link #baseDirectory} is not set, an IOException is thrown.
     * </p>
     * <p>
     * If <code>location</code> already exists as a file, an IOException is thrown.
     * </p>
     * @param location Location of directory to create, either absolute or
     * relative.
     *
     * @return The file handle of the created directory.
     *
     * @throws IOException
     */
    public File createDirectory(String location) throws IOException {
        File file = find(location);

        if (file != null) {
            if (!file.isDirectory()) {
                String msg = location + " already exists and is not directory";
                throw new IOException(msg);
            }
        }

        file = new File(location);

        if (file.isAbsolute()) {
            file.mkdir();

            return file;
        }

        //no base directory set, cannot create a relative path
        if (baseDirectory == null) {
            String msg = "No base location set, could not create directory: " + location;
            throw new IOException(msg);
        }

        file = new File(baseDirectory, location);
        file.mkdir();

        return file;
    }

    /**
     * Creates a new file.
     * <p>
     * Relative paths are created relative to {@link #baseDirectory}.
     * </p>
     * If {@link #baseDirectory} is not set, an IOException is thrown.
     * </p>
     * <p>
     * If <code>location</code> already exists as a directory, an IOException is thrown.
     * </p>
     * @param location Location of file to create, either absolute or relative.
     *
     * @return The file handle of the created file.
     *
     * @throws IOException In the event of an I/O error.
     */
    public File createFile(String location) throws IOException {
        File file = find(location);

        if (file != null) {
            if (file.isDirectory()) {
                String msg = location + " already exists and is a directory";
                throw new IOException(msg);
            }

            return file;
        }

        file = new File(location);

        if (file.isAbsolute()) {
            file.createNewFile();

            return file;
        }

        //no base directory set, cannot create a relative path
        if (baseDirectory == null) {
            String msg = "No base location set, could not create file: " + location;
            throw new IOException(msg);
        }

        file = new File(baseDirectory, location);
        file.createNewFile();

        return file;
    }
}
