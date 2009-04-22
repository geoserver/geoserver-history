/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import static org.geoserver.security.DataAccessRule.ANY;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.security.DataAccessManager.CatalogMode;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Allows one to manage the rules used by the per layer security subsystem
 */
public class DataAccessRuleDAO {

    static final Logger LOGGER = Logging.getLogger(DataAccessRuleDAO.class);

    Catalog rawCatalog;

    TreeSet<DataAccessRule> rules;

    /**
     * Default to the highest security mode
     */
    CatalogMode mode = CatalogMode.HIDE;

    /**
     * Used to check the file for modifications
     */
    PropertyFileWatcher watcher;

    /**
     * Stores the time of the last rule list loading
     */
    long lastModified;

    /**
     * Builds a new dao
     * 
     * @param rawCatalog
     */
    public DataAccessRuleDAO(Catalog rawCatalog) {
        this.rawCatalog = rawCatalog;
    }

    /**
     * Returns the list of rules contained in the property file
     * 
     * @return
     */
    public List<DataAccessRule> getRules() {
        checkPropertyFile();
        return new ArrayList<DataAccessRule>(rules);
    }

    /**
     * The way the catalog should react to unauthorized access
     * 
     * @return
     */
    public CatalogMode getMode() {
        checkPropertyFile();
        return mode;
    }

    /**
     * Checks the property file is up to date, eventually rebuilds the tree
     */
    void checkPropertyFile() {
        try {
            if (rules == null) {
                // delay building the authorization tree after the
                // catalog is fully built and available, otherwise we
                // end up ignoring the rules because the namespaces are not loaded
                File security = GeoserverDataDirectory.findConfigDir(GeoserverDataDirectory
                        .getGeoserverDataDirectory(), "security");

                // no security folder, let's work against an empty properties then
                if (security == null || !security.exists()) {
                    this.rules = new TreeSet<DataAccessRule>();
                } else {
                    // no security config, let's work against an empty properties then
                    File layers = new File(security, "layers.properties");
                    if (!layers.exists()) {
                        this.rules = new TreeSet<DataAccessRule>();
                    } else {
                        // ok, something is there, let's load it
                        watcher = new PropertyFileWatcher(layers);
                        loadRules(watcher.getProperties());
                    }
                }
            } else if (watcher != null && watcher.isStale()) {
                loadRules(watcher.getProperties());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to reload data access rules from layers.properties, keeping old rules",
                    e);
        }
        lastModified = System.currentTimeMillis();
    }

    /**
     * Parses the rules contained in the property file
     * 
     * @param props
     * @return
     */
    void loadRules(Properties props) {
        TreeSet<DataAccessRule> result = new TreeSet<DataAccessRule>();
        mode = CatalogMode.HIDE;
        for (Map.Entry entry : props.entrySet()) {
            String ruleKey = (String) entry.getKey();
            String ruleValue = (String) entry.getValue();

            // check for the mode
            if ("mode".equalsIgnoreCase(ruleKey)) {
                try {
                    mode = CatalogMode.valueOf(ruleValue.toUpperCase());
                } catch (Exception e) {
                    LOGGER.warning("Invalid security mode " + ruleValue + " acceptable values are "
                            + Arrays.asList(CatalogMode.values()));
                }
            } else {
                DataAccessRule rule = parseDataAccessRule(ruleKey, ruleValue);
                if (rule != null) {
                    if(result.contains(rule))
                        LOGGER.warning("Rule " + ruleKey + "." + ruleValue + " overwrites another rule on the same path");
                    result.add(rule);
                }
            }
        }
        rules = result;
    }

    /**
     * Parses a single layer.properties line into a {@link DataAccessRule}, returns false if the
     * rule is not valid
     * 
     * @return
     */
    DataAccessRule parseDataAccessRule(String ruleKey, String ruleValue) {
        final String rule = ruleKey + "=" + ruleValue;

        // parse
        String[] elements = parseElements(ruleKey);
        String workspace = elements[0];
        String layerName = elements[1];
        String modeAlias = elements[2];
        Set<String> roles = parseRoles(ruleValue);

        // perform basic checks on the elements
        if (elements.length != 3) {
            LOGGER.warning("Invalid rule '" + rule
                    + "', the standard form is [namespace].[layer].[mode]=[role]+ "
                    + "Rule has been ignored");
            return null;
        }

        // emit warnings for unknown workspaces, layers, but don't skip the rule,
        // people might be editing the catalog structure and will edit the access rule
        // file afterwards
        if (!ANY.equals(workspace) && rawCatalog.getWorkspaceByName(workspace) == null)
            LOGGER.warning("Namespace/Workspace " + workspace + " is unknown in rule " + rule);
        if (!ANY.equals(layerName) && rawCatalog.getLayerByName(layerName) == null)
            LOGGER.warning("Layer " + workspace + " is unknown in rule + " + rule);

        // check the access mode sanity
        AccessMode mode = AccessMode.getByAlias(modeAlias);
        if (mode == null) {
            LOGGER.warning("Unknown access mode " + modeAlias + " in " + ruleKey
                    + ", skipping rule " + rule);
            return null;
        }

        // check ANY usage sanity
        if (ANY.equals(workspace)) {
            if (!ANY.equals(layerName)) {
                LOGGER.warning("Invalid rule " + rule + ", when namespace "
                        + "is * then also layer must be *. Skipping rule " + rule);
                return null;
            }
        }

        // build the rule
        return new DataAccessRule(workspace, layerName, mode, roles);
    }

    /**
     * Returns the last modification date of the rules in this DAO (last time the rules were
     * reloaded from the property file)
     * 
     * @return
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Parses a comma separated list of roles into a set of strings, with special handling for the
     * {@link DataAccessRule#ANY} role
     * 
     * @param roleCsv
     * @return
     */
    Set<String> parseRoles(String roleCsv) {
        // regexp: treat extra spaces as separators, ignore extra commas
        // "a,,b, ,, c" --> ["a","b","c"]
        String[] rolesArray = roleCsv.split("[\\s,]+");
        Set<String> roles = new HashSet<String>(rolesArray.length);
        roles.addAll(Arrays.asList(rolesArray));

        // if any of the roles is * we just remove all of the others
        for (String role : roles) {
            if (ANY.equals(role))
                return Collections.singleton("*");
        }

        return roles;
    }

    /**
     * Parses workspace.layer.mode into an array of strings
     * 
     * @param path
     * @return
     */
    private String[] parseElements(String path) {
        // regexp: ignore extra spaces, split on dot
        return path.split("\\s*\\.\\s*");
    }

}
