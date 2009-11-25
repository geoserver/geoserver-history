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
import org.geoserver.security.DataAccessManager.CatalogMode;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Allows one to manage the rules used by the per layer security subsystem
 * TODO: consider splitting the persistence of properties into two strategies,
 * and in memory one, and a file system one (this class is so marginal that
 * I did not do so right away, in memory access is mostly handy for testing)
 */
public class DataAccessRuleDAO {

    static final Logger LOGGER = Logging.getLogger(DataAccessRuleDAO.class);

    Catalog rawCatalog;

    TreeSet<DataAccessRule> rules;

    /**
     * Default to the highest security mode
     */
    CatalogMode catalogMode = CatalogMode.HIDE;

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
    public static DataAccessRuleDAO get() {
       return GeoServerExtensions.bean(DataAccessRuleDAO.class); 
    }

    /**
     * Builds a new dao
     * 
     * @param rawCatalog
     */
    public DataAccessRuleDAO(Catalog rawCatalog) throws ConfigurationException {
        this.rawCatalog = rawCatalog;
        this.securityDir = GeoserverDataDirectory.findCreateConfigDir("security");
    }
    
    /**
     * Builds a new dao with a custom security dir. Used mostly for testing purposes
     * 
     * @param rawCatalog
     */
    DataAccessRuleDAO(Catalog rawCatalog, File securityDir) throws ConfigurationException {
        this.rawCatalog = rawCatalog;
        this.securityDir = securityDir;
    }

    /**
     * Returns the list of rules contained in the property file
     * 
     * @return
     */
    public List<DataAccessRule> getRules() {
        checkPropertyFile(false);
        return new ArrayList<DataAccessRule>(rules);
    }

    /**
     * Adds/overwrites a rule in the rule set
     * 
     * @param rule
     * @return true if the set did not contain the rule already, false otherwise
     */
    public boolean addRule(DataAccessRule rule) {
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
    public boolean removeRule(DataAccessRule rule) {
        lastModified = System.currentTimeMillis();
        return rules.remove(rule);
    }

    /**
     * The way the catalog should react to unauthorized access
     * 
     * @return
     */
    public CatalogMode getMode() {
        checkPropertyFile(false);
        return catalogMode;
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
            File propFile = new File(securityDir, "layers.properties");
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
                    this.rules = new TreeSet<DataAccessRule>();
                } else {
                    // no security config, let's work against an empty properties then
                    File layers = new File(securityDir, "layers.properties");
                    if (!layers.exists()) {
                        this.rules = new TreeSet<DataAccessRule>();
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
        TreeSet<DataAccessRule> result = new TreeSet<DataAccessRule>();
        catalogMode = CatalogMode.HIDE;
        for (Map.Entry entry : props.entrySet()) {
            String ruleKey = (String) entry.getKey();
            String ruleValue = (String) entry.getValue();

            // check for the mode
            if ("mode".equalsIgnoreCase(ruleKey)) {
                try {
                    catalogMode = CatalogMode.valueOf(ruleValue.toUpperCase());
                } catch (Exception e) {
                    LOGGER.warning("Invalid security mode " + ruleValue + " acceptable values are "
                            + Arrays.asList(CatalogMode.values()));
                }
            } else {
                DataAccessRule rule = parseDataAccessRule(ruleKey, ruleValue);
                if (rule != null) {
                    if (result.contains(rule))
                        LOGGER.warning("Rule " + ruleKey + "." + ruleValue
                                + " overwrites another rule on the same path");
                    result.add(rule);
                }
            }
        }
        
        // make sure the two basic rules if the set is empty
        if(result.size() == 0) {
            result.add(new DataAccessRule(DataAccessRule.READ_ALL));
            result.add(new DataAccessRule(DataAccessRule.WRITE_ALL));
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
        if(elements.length != 3) {
            LOGGER.warning("Invalid rule " + rule + ", the expected format is workspace.layer.mode=role1,role2,...");
            return null;
        }
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
     * Turns the rules list into a property bag
     * @return
     */
    Properties toProperties() {
        Properties props = new Properties();
        props.put("mode", catalogMode.toString());
        for (DataAccessRule rule : rules) {
        	String key = rule.getWorkspace().replaceAll("\\.", "\\\\.") + "." 
        	             + rule.getLayer().replaceAll("\\.", "\\\\.") + "." 
        	             + rule.getAccessMode().getAlias();
        	props.put(key, rule.getValue());
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
    static String[] parseElements(String path) {
        String[] rawParse = path.trim().split("\\s*\\.\\s*");
        List<String> result = new ArrayList<String>();
        String prefix = null;
        for (String raw : rawParse) {
            if(prefix != null)
                raw = prefix + "."  + raw;
            // just assume the escape is invalid char besides \. and check it once only
            if (raw.endsWith("\\")) {
                prefix = raw.substring(0, raw.length() - 1);
            } else {
                result.add(raw);
                prefix = null;
            }
        }
        
        return (String[]) result.toArray(new String[result.size()]);
    }

	public void setCatalogMode(CatalogMode catalogMode) {
		this.catalogMode = catalogMode;
	}
	
	public static CatalogMode getByAlias(String alias){
		for(CatalogMode mode: CatalogMode.values()){
			if(mode.name().equals(alias)){
				return mode;
			}
		}
		return null;
	}

}
