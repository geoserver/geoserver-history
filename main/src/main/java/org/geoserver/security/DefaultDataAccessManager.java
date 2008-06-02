package org.geoserver.security;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.relation.RoleStatus;

import org.acegisecurity.Authentication;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Default implementation of {@link DataAccessManager}, loads simple access
 * rules from a Properties objects. The format of each property is:<br>
 * <code>workspace.layer.mode=[role]*</code><br>
 * where:
 * <ul>
 * <li> workspace: either a workspace name or a * to indicate any workspace (in
 * this case, the layer must also be *) </li>
 * <li> layer: either a layer name (feature type, coverage, layer group) or * to
 * indicate any layer </li>
 * <li> mode: the access mode, at the time or writing, either &quot;r&quot;
 * (read) or &quot;w&quot; (write) </li>
 * <li> role: a user role</li>
 * </ul>
 * For more details on how the security rules are applied, see the &lt;a
 * href=&quot;http://geoserver.org/display/GEOS/GSIP+19+-+Per+layer+security&quot;/&gt;per
 * layer security proposal&lt;/a&gt; on the &lt;a
 * href=&quot;www.geoserver.org&quot;&gt;GeoServer&lt;/a&gt; web site.
 * 
 * &#064;author Andrea Aime - TOPP
 * 
 */
public class DefaultDataAccessManager extends DataAccessManager {
    static final Logger LOGGER = Logging.getLogger(DataAccessManager.class);

    SecureTreeNode root;

    Catalog catalog;

    DefaultDataAccessManager(Catalog catalog, Properties layers) {
        this.catalog = catalog;
        this.root = buildAuthorizationTree(layers);
    }

    DefaultDataAccessManager(Catalog catalog) throws Exception {
        this(catalog, getLayerProperties());
    }

    private static Properties getLayerProperties() throws Exception {
        File security = GeoserverDataDirectory.findConfigDir(GeoserverDataDirectory
                .getGeoserverDataDirectory(), "security");

        // no security config, let's work against an empty one
        if (security == null)
            return new Properties();

        // no security config, let's work against an empty one
        File layers = new File(security, "layers.properties");
        if (!layers.exists())
            return new Properties();

        // ok, something is there, let's load it
        Properties props = new Properties();
        props.load(new FileInputStream(layers));
        return props;
    }

    @Override
    public boolean canAccess(Authentication user, WorkspaceInfo workspace, AccessMode mode) {
        SecureTreeNode node = root.getDeepestNode(new String[] { workspace.getName() });
        return node.canAccess(user, mode);
    }

    @Override
    public boolean canAccess(Authentication user, LayerInfo layer, AccessMode mode) {
        if (layer.getResource() == null) {
            LOGGER.log(Level.FINE, "Layer " + layer + " has no attached resource, "
                    + "assuming it's possible to access it");
            // it's a layer whose resource we don't know about
            return true;
        } else {
            return canAccess(user, layer.getResource(), mode);
        }

    }

    @Override
    public boolean canAccess(Authentication user, LayerGroupInfo group, AccessMode mode) {
        for (LayerInfo layer : group.getLayers()) {
            if (!canAccess(user, layer, mode))
                return false;
        }
        return true;
    }

    @Override
    public boolean canAccess(Authentication user, ResourceInfo resource, AccessMode mode) {
        String workspace;
        try {
            workspace = resource.getStore().getWorkspace().getName();
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Errors occurred trying to gather workspace of resource "
                    + resource.getName());
            // it's a layer whose resource we don't know about
            return true;
        }

        SecureTreeNode node = root.getDeepestNode(new String[] { workspace, resource.getName() });
        return node.canAccess(user, mode);
    }

    SecureTreeNode buildAuthorizationTree(Properties props) {
        SecureTreeNode root = new SecureTreeNode();

        for (Map.Entry entry : props.entrySet()) {
            final String ruleKey = (String) entry.getKey();
            final String ruleValue = (String) entry.getValue();
            final String rule = ruleKey + "=" + ruleValue;

            // parse
            String[] elements = parseElements(ruleKey);
            final String workspace = elements[0];
            final String layerName = elements[1];
            final String modeAlias = elements[2];
            Set<String> roles = parseRoles(ruleValue);

            // perform basic checks on the elements
            if (elements.length != 3)
                LOGGER.warning("Invalid rule '" + rule
                        + "', the standard form is [namespace].[layer].[mode]=[role]+ "
                        + "Rule has been ignored");

            if (!"*".equals(workspace) && catalog.getWorkspace(workspace) == null)
                LOGGER.warning("Namespace/Workspace " + workspace + " is unknown in rule " + rule);

            if (!"*".equals(layerName) && catalog.getLayer(layerName) == null)
                LOGGER.warning("Layer " + workspace + " is unknown in rule + " + rule);

            // check the access mode
            AccessMode mode = AccessMode.getByAlias(modeAlias);
            if (mode == null) {
                LOGGER.warning("Unknown access mode " + modeAlias + " in " + entry.getKey()
                        + ", skipping rule " + rule);
                continue;
            }

            // look for the node where the rules will have to be set
            SecureTreeNode node;

            // check for the * ws definition
            if ("*".equals(workspace)) {
                if (!"*".equals(layerName)) {
                    LOGGER.warning("Invalid rule " + entry.getKey() + " when namespace "
                            + "is * then also layer must be *. Skipping rule " + rule);
                    continue;
                }

                node = root;
            } else {
                // get or create the workspace
                SecureTreeNode ws = root.getChild(workspace);
                if (ws == null) {
                    ws = root.addChild(workspace);
                }

                // if layer is "*" the rule applies to the ws, otherwise
                // get/create the layer
                if ("*".equals(layerName)) {
                    node = ws;
                } else {
                    SecureTreeNode layer = ws.getChild(layerName);
                    if (layer == null)
                        layer = ws.addChild(layerName);
                    node = layer;
                }

            }

            // actually set the rule
            if (node.getAuthorizedRoles(mode) != null && node.getAuthorizedRoles(mode).size() > 0) {
                LOGGER.warning("Rule " + rule
                        + " is overriding another rule targetting the same resource");
            }
            node.setAuthorizedRoles(mode, roles);
        }

        return root;
    }

    Set<String> parseRoles(String roleCsv) {
        // regexp: treat extra spaces as separators, ignore extra commas
        // "a,,b, ,, c" --> ["a","b","c"]
        String[] rolesArray = roleCsv.split("[\\s,]+");
        Set<String> roles = new HashSet<String>(rolesArray.length);
        roles.addAll(Arrays.asList(rolesArray));
        
        // if any of the roles is * we just remove all of the others
        for (String role : roles) {
            if("*".equals(role))
                return Collections.singleton("*");
        }

        return roles;
    }

    private String[] parseElements(String path) {
        // regexp: ignore extra spaces
        return path.split("\\s*\\.\\s*");
    }

}
