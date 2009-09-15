/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import static org.geoserver.security.DataAccessRule.ANY;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Allows one to manage the rules used by the service layer security subsystem
 * TODO: consider splitting the persistence of properties into two strategies,
 * and in memory one, and a file system one (this class is so marginal that
 * I did not do so right away, in memory access is mostly handy for testing)
 */
public class ServiceAccessRuleDAO {

    static final Logger LOGGER = Logging.getLogger(ServiceAccessRuleDAO.class);

    Catalog rawCatalog;
    
    TreeSet<ServiceAccessRule> rules;

    /**
     * Used to check the file for modifications
     */
    PropertyFileWatcher watcher;

    /**
     * Stores the time of the last rule list loading
     */
    long lastModified;
    
    /**
     * The security dir
     */
    File securityDir;
    
    /**
     * Returns the instanced contained in the Spring context for the UI to use
     * @return
     */
    public static ServiceAccessRuleDAO get() {
       return GeoServerExtensions.bean(ServiceAccessRuleDAO.class); 
    }
    
    public ServiceAccessRuleDAO(Catalog rawCatalog) throws ConfigurationException {
        this.rawCatalog = rawCatalog;
        this.securityDir = GeoserverDataDirectory.findCreateConfigDir("security");
    }

    /**
     * Builds a new dao
     * 
     * @param rawCatalog
     */
    public ServiceAccessRuleDAO() throws ConfigurationException {
        this.securityDir = GeoserverDataDirectory.findCreateConfigDir("security");
    }
    
    /**
     * Builds a new dao with a custom security dir. Used mostly for testing purposes
     * 
     * @param rawCatalog
     */
    ServiceAccessRuleDAO(Catalog rawCatalog, File securityDir) throws ConfigurationException {
        this.securityDir = securityDir;
    }

    /**
     * Returns the list of rules contained in the property file. The returned rules are
     * sorted against the {@link ServiceAccessRule} natural order
     * 
     * @return
     */
    public List<ServiceAccessRule> getRules() {
        checkPropertyFile(false);
        return new ArrayList<ServiceAccessRule>(rules);
    }

    /**
     * Adds/overwrites a rule in the rule set
     * 
     * @param rule
     * @return true if the set did not contain the rule already, false otherwise
     */
    public boolean addRule(ServiceAccessRule rule) {
        lastModified = System.currentTimeMillis();
        return rules.add(rule);
    }
    
    /**
     * Forces a reload of the rules
     */
    public void reload() {
        checkPropertyFile(true);
    }
    
    /**
     * Cleans up the contents of the rule set
     */
    public void clear() {
        rules.clear();
        lastModified = System.currentTimeMillis();
    }

    /**
     * Removes the rule from rule set
     * @param rule
     * @return
     */
    public boolean removeRule(ServiceAccessRule rule) {
        lastModified = System.currentTimeMillis();
        return rules.remove(rule);
    }

    /**
     * Writes the rules back to file system
     * @throws IOException
     */
    public void storeRules() throws IOException {
        FileOutputStream os = null;
        try {
            // turn back the users into a users map
            Properties p = toProperties();

            // write out to the data dir
            File propFile = new File(securityDir, "service.properties");
            os = new FileOutputStream(propFile);
            p.store(os, null);
        } catch (Exception e) {
            if (e instanceof IOException)
                throw (IOException) e;
            else
                throw (IOException) new IOException(
                        "Could not write updated data access rules to file system").initCause(e);
        } finally {
            if (os != null)
                os.close();
        }
    }

    /**
     * Checks the property file is up to date, eventually rebuilds the tree
     */
    void checkPropertyFile(boolean force) {
        try {
            if (rules == null) {
                // no security folder, let's work against an empty properties then
                if (securityDir == null || !securityDir.exists()) {
                    this.rules = new TreeSet<ServiceAccessRule>();
                } else {
                    // no security config, let's work against an empty properties then
                    File layers = new File(securityDir, "service.properties");
                    if (!layers.exists()) {
                        this.rules = new TreeSet<ServiceAccessRule>();
                    } else {
                        // ok, something is there, let's load it
                        watcher = new PropertyFileWatcher(layers);
                        loadRules(watcher.getProperties());
                    }
                }
                lastModified = System.currentTimeMillis();
            } else if (watcher != null && (watcher.isStale() || force)) {
                loadRules(watcher.getProperties());
                lastModified = System.currentTimeMillis();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to reload data access rules from layers.properties, keeping old rules",
                    e);
        }
    }

    /**
     * Parses the rules contained in the property file
     * 
     * @param props
     * @return
     */
    void loadRules(Properties props) {
        TreeSet<ServiceAccessRule> result = new TreeSet<ServiceAccessRule>();
        for (Map.Entry entry : props.entrySet()) {
            String ruleKey = (String) entry.getKey();
            String ruleValue = (String) entry.getValue();

            ServiceAccessRule rule = parseServiceAccessRule(ruleKey, ruleValue);
            if (rule != null) {
                if (result.contains(rule))
                    LOGGER.warning("Rule " + ruleKey + "." + ruleValue
                            + " overwrites another rule on the same path");
                result.add(rule);
            }
        }
        
        // make sure to add the "all access alloed" rule if the set if empty
        if(result.size() == 0) {
            result.add(new ServiceAccessRule(new ServiceAccessRule()));
        }
        
        rules = result;
    }

    /**
     * Parses a single layer.properties line into a {@link DataAccessRule}, returns false if the
     * rule is not valid
     * 
     * @return
     */
    ServiceAccessRule parseServiceAccessRule(String ruleKey, String ruleValue) {
        final String rule = ruleKey + "=" + ruleValue;

        // parse
        String[] elements = parseElements(ruleKey);
        if(elements.length != 2) {
            LOGGER.warning("Invalid rule " + rule + ", the expected format is service.method=role1,role2,...");
            return null;
        }
        String service = elements[0];
        String method = elements[1];
        Set<String> roles = parseRoles(ruleValue);

        // check ANY usage sanity
        if (ANY.equals(service)) {
            if (!ANY.equals(method)) {
                LOGGER.warning("Invalid rule " + rule + ", when namespace "
                        + "is * then also layer must be *. Skipping rule " + rule);
                return null;
            }
        }

        // build the rule
        return new ServiceAccessRule(service, method, roles);
    }
    
    /**
     * Turns the rules list into a property bag
     * @return
     */
    Properties toProperties() {
        Properties props = new Properties();
        for (ServiceAccessRule rule : rules) {
            props.put(rule.getKey(), rule.getValue());
        }
        return props;
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
