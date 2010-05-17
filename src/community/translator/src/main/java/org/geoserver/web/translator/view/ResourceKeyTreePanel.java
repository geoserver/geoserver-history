/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.ITreeState;
import org.apache.wicket.markup.html.tree.ITreeStateListener;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.translator.model.TranslateBean;

/**
 * A panel presenting the list of user interface string resource keys.
 * <p>
 * The contents of the tree view for this panel may be customized in order to show the resource keys
 * as a tree or as a flat view, collapse or expand all nodes, or show only the resource keys that
 * are not yet translated for the ongoing locale translation.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class ResourceKeyTreePanel extends Panel {
    private static final long serialVersionUID = 4308370741356525991L;

    private final BaseTree keyTree;

    private Set<String> keys;

    public ResourceKeyTreePanel(final String id, final IModel translateBeanModel) {
        super(id);
        setModel(translateBeanModel);
        setOutputMarkupId(true);

        keyTree = new LinkTree("keyTreePanel", new Model()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Component newNodeComponent(String id, IModel nodeModel) {

                Component nodeComponent = super.newNodeComponent(id, nodeModel);

                return nodeComponent;
            }
        };
        keyTree.setOutputMarkupId(true);
        keyTree.setRootLess(true);
        keyTree.getTreeState().addTreeStateListener(new SelectionKeyListener());

        refresh();

        add(keyTree);
    }

    /**
     * Parses the keywords and sets them as the or'ed filtered criteria, forces update of the
     * components that need to as a result of the different filtering
     */
    private Set<String> updateFilter(final String flatKeywords, final Collection<String> keys,
            final TranslateBean translationState) {

        final List<Pattern> patterns;
        {
            final String[] keywords = flatKeywords.split("\\s+");
            patterns = new ArrayList<Pattern>(keywords.length);
            String regex;
            for (String keyword : keywords) {
                regex = ".*" + escape(keyword) + ".*";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE
                        | Pattern.UNICODE_CASE);
                patterns.add(pattern);
            }
        }
        Set<String> matches = new HashSet<String>();
        final Locale baseLanguage = translationState.getBaseLocale();
        final Locale targetLanguage = translationState.getTargetLanguage();

        for (String resourceKey : keys) {
            for (Pattern pattern : patterns) {
                if (pattern.matcher(resourceKey).matches()) {
                    matches.add(resourceKey);
                    break;
                }
                if (matchesContent(resourceKey, pattern, baseLanguage, translationState)) {
                    matches.add(resourceKey);
                    break;
                }
                if (matchesContent(resourceKey, pattern, targetLanguage, translationState)) {
                    matches.add(resourceKey);
                    break;
                }
            }
        }

        return matches;
    }

    private boolean matchesContent(final String resourceKey, final Pattern pattern,
            final Locale locale, final TranslateBean translationState) {
        String resource = translationState.getResource(locale, resourceKey);
        if (resource == null) {
            return false;
        }
        return pattern.matcher(resource).matches();
    }

    /**
     * Escape any character that's special for the regex api
     * 
     * @param keyword
     * @return
     */
    private String escape(String keyword) {
        final String escapeSeq = "\\";
        final int len = keyword.length();
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < len; i++) {
            c = keyword.charAt(i);
            if (isSpecial(c)) {
                sb.append(escapeSeq);
            }
            sb.append(keyword.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Convenience method to determine if a character is special to the regex system.
     * 
     * @param chr
     *            the character to test
     * 
     * @return is the character a special character.
     */
    private boolean isSpecial(final char chr) {
        return ((chr == '.') || (chr == '?') || (chr == '*') || (chr == '^') || (chr == '$')
                || (chr == '+') || (chr == '[') || (chr == ']') || (chr == '(') || (chr == ')')
                || (chr == '|') || (chr == '\\') || (chr == '&'));
    }

    /**
     * Updates the state of the tree.
     * <p>
     * The following variables are considered to rebuild the tree:
     * <ul>
     * <li> {@link #flatView}
     * <li> {@link TranslateBean#isShowMissingOnly()}
     * </ul>
     * </p>
     */
    @SuppressWarnings("unchecked")
    public void refresh() {
        final IModel translateBeanModel = getModel();
        final Boolean missingOnly = (Boolean) new PropertyModel(translateBeanModel,
                "showMissingOnly").getObject();

        PropertyModel keysModel;
        if (missingOnly) {
            keysModel = new PropertyModel(translateBeanModel, "missingKeys");
        } else {
            keysModel = new PropertyModel(translateBeanModel, "resourceKeys");
        }

        Collection<String> keys = (Collection<String>) keysModel.getObject();

        final String flatKeywords = (String) new PropertyModel(translateBeanModel, "filter")
                .getObject();
        if (flatKeywords != null && flatKeywords.trim().length() > 0) {
            final TranslateBean translationState = (TranslateBean) translateBeanModel.getObject();
            this.keys = updateFilter(flatKeywords, keys, translationState);
        } else {
            this.keys = new TreeSet<String>(keys);
        }

        IModel flatViewModel = new PropertyModel(translateBeanModel, "flatView");
        boolean flatView = ((Boolean) flatViewModel.getObject()).booleanValue();
        if (flatView) {
            setFlatView();
        } else {
            setTreeView();
        }
    }

    public BaseTree getTree() {
        return keyTree;
    }

    public void expandAll(final AjaxRequestTarget target) {
        getTree().getTreeState().expandAll();
        if (target == null) {
            getTree().updateTree();
        } else {
            getTree().updateTree(target);
        }
    }

    public void collapseAll(final AjaxRequestTarget target) {
        getTree().getTreeState().collapseAll();
        if (target == null) {
            getTree().updateTree();
        } else {
            getTree().updateTree(target);
        }
    }

    private void setTreeView() {
        final TreeModel treeModel = createTreeModel();
        keyTree.setModelObject(treeModel);
    }

    private void setFlatView() {
        final TreeModel flatModel = creatFlatModel();
        keyTree.setModelObject(flatModel);
    }

    private TreeModel creatFlatModel() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new NodeBean("__ROOT", null));

        List<String> keySet = new ArrayList<String>(keys);
        Collections.sort(keySet, String.CASE_INSENSITIVE_ORDER);
        for (String key : keySet) {
            NodeBean node = new NodeBean(key, key);
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(node);
            rootNode.add(child);
        }

        TreeModel treeModel = new DefaultTreeModel(rootNode);
        return treeModel;
    }

    private TreeModel createTreeModel() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("__ROOT");

        List<String> keySet = new ArrayList<String>(keys);
        Collections.sort(keySet, String.CASE_INSENSITIVE_ORDER);
        HashMap<String, DefaultMutableTreeNode> hashMap = new HashMap<String, DefaultMutableTreeNode>();
        for (String key : keySet) {
            addNodes(rootNode, key, hashMap);
        }

        TreeModel treeModel = new DefaultTreeModel(rootNode);
        return treeModel;
    }

    private void addNodes(final DefaultMutableTreeNode parent, final String key,
            HashMap<String, DefaultMutableTreeNode> hashMap) {

        final int idx = key.indexOf('.');
        String name;
        if (idx == -1) {
            name = key;
        } else {
            name = key.substring(0, idx);
        }

        String path = getPath(parent, name);

        NodeBean node = new NodeBean(name, path);

        DefaultMutableTreeNode child = hashMap.get(path);
        if (child == null) {
            child = new DefaultMutableTreeNode(node);
            parent.add(child);
            hashMap.put(path, child);
        }

        if (idx > -1) {
            addNodes(child, key.substring(idx + 1), hashMap);
        }
    }

    private String getPath(DefaultMutableTreeNode node, String child) {
        String parentPath = getPath(node);
        StringBuilder sb = new StringBuilder(parentPath);
        if (sb.length() > 0) {
            sb.append('.');
        }
        sb.append(child);
        return sb.toString();
    }

    private String getPath(final DefaultMutableTreeNode node) {
        Object[] userObjectPath = node.getUserObjectPath();
        StringBuilder sb = new StringBuilder();
        // Ignore root
        for (int i = 1; i < userObjectPath.length; i++) {
            Object mb = userObjectPath[i];
            sb.append(mb.toString());
            if (i < userObjectPath.length - 1) {
                sb.append('.');
            }
        }

        return sb.toString();
    }

    private final class SelectionKeyListener implements ITreeStateListener, Serializable {
        private static final long serialVersionUID = -5354495649059977267L;

        private TreeNode previouslySelected;

        public void nodeUnselected(final TreeNode node) {
            // nothing to do
        }

        public void nodeSelected(final TreeNode node) {
            final ITreeState treeState = getTree().getTreeState();
            if (!node.isLeaf()) {
                if (node.equals(previouslySelected) && treeState.isNodeExpanded(node)) {
                    treeState.collapseNode(node);
                } else {
                    treeState.expandNode(node);
                }
            }
            previouslySelected = node;
        }

        public void nodeExpanded(final TreeNode node) {
            // nothing to do
        }

        public void nodeCollapsed(final TreeNode node) {
            // nothing to do
        }

        public void allNodesExpanded() {
            // nothing to do
        }

        public void allNodesCollapsed() {
            // nothing to do
        }
    }

    public static class NodeBean {

        private String name;

        private String resourceKey;

        /**
         * 
         * @param name
         *            the node name as it is to be shown in the tree
         * @param resourceKey
         *            the resource key, or {@code null} if the node is not for an actual resource
         *            key but an intermediate node in the tree
         */
        public NodeBean(final String name, final String resourceKey) {
            this.name = name;
            this.resourceKey = resourceKey;
        }

        public String getName() {
            return name;
        }

        public String getResourceKey() {
            return resourceKey;
        }

        /**
         * @return the name as to be shown in the UI
         */
        @Override
        public String toString() {
            return getName();
        }
    }

    @SuppressWarnings("unchecked")
    public String getSelectedNodeKey() {
        ITreeState treeState = getTree().getTreeState();
        Collection<DefaultMutableTreeNode> selectedNodes = treeState.getSelectedNodes();
        String selectedKey = null;
        if (selectedNodes.size() > 0) {
            DefaultMutableTreeNode selectedNode = selectedNodes.iterator().next();
            ResourceKeyTreePanel.NodeBean userObject = (NodeBean) selectedNode.getUserObject();
            selectedKey = userObject.getResourceKey();
        }
        return selectedKey;
    }

}
