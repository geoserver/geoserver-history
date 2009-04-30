/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * An abstract filterable, sortable, pageable table with associated
 * filtering form and paging navigator.<p>
 * The construction of the page is driven by the properties returned
 * by a {@link GeoServerDataProvider}, subclasses only need to build a component
 * for each property by implementing the {@link #getComponentForProperty(String, IModel, Property)}
 * method
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class GeoServerTablePanel<T> extends Panel {
    
    
    private static final int DEFAULT_ITEMS_PER_PAGE = 25;

    // filter form components
    TextField filter;

    // table components
    DataView dataView;

    WebMarkupContainer listContainer;
    
    Pager navigatorTop;
    Pager navigatorBottom;

    GeoServerDataProvider<T> dataProvider;
    
    Form filterForm;

    public GeoServerTablePanel(String id, final GeoServerDataProvider<T> dataProvider) {
        super(id);
        this.dataProvider = dataProvider;

        // layer container used for ajax-y udpates of the table
        listContainer = new WebMarkupContainer("listContainer");

        // build the filter form
        filterForm = new Form("filterForm");
        add(filterForm);
        filterForm.add(filter = new TextField("filter", new Model()));
         filterForm.add(new AjaxSubmitButton("submit") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                updateFilter(target, filter.getModelObjectAsString());
            }
            
        });

        // setup the table
        listContainer.setOutputMarkupId(true);
        add(listContainer);
        dataView = new DataView("items", dataProvider) {

            @Override
            protected void populateItem(Item item) {
                final IModel itemModel = item.getModel();
                
                // odd/even style
                item.add(new SimpleAttributeModifier("class",
                        item.getIndex() % 2 == 0 ? "even" : "odd"));

                // create one component per viewable property
                item.add(new ListView("itemProperties", dataProvider
                        .getVisibleProperties()) {

                    @Override
                    protected void populateItem(ListItem item) {
                        Property<T> property = (Property<T>) item
                                .getModelObject();
                       
                        Component component = getComponentForProperty(
                                "component", itemModel, property);
                        
                        // add some checks for the id, the error message
                        // that wicket returns in case of mismatch is not
                        // that helpful
                        if (!"component".equals(component.getId()))
                            throw new IllegalArgumentException(
                                    "getComponentForProperty asked "
                                            + "to build a component "
                                            + "with id = 'component' "
                                            + "for property '"
                                            + property.getName()
                                            + "', but got '"
                                            + component.getId() + "' instead");
                        item.add(component);
                    }

                });
            }
            
            

        };
        listContainer.add(dataView);

        // add the sorting links
        listContainer.add(new ListView("sortableLinks", dataProvider
                .getVisibleProperties()) {

            @Override
            protected void populateItem(ListItem item) {
                Property<T> property = (Property<T>) item.getModelObject();
                
                // Simple i18n
                String pageName = this.getPage().getClass().getSimpleName();
                ResourceModel resMod = new ResourceModel(pageName+".th."+property.getName(), property.getName());
                
                if (property.getComparator() != null) {
                    Fragment f = new Fragment("header", "sortableHeader", item);
                    AjaxLink link = sortLink(dataProvider, item);
                    link.add(new Label("label", resMod));
                    f.add(link);
                    item.add(f);
                } else {
                    item.add(new Label("header", resMod));
                }
            }

        });

        // add the paging navigator and set the items per page
        dataView.setItemsPerPage(DEFAULT_ITEMS_PER_PAGE);
        filterForm.add(navigatorTop = new Pager("navigatorTop"));
        navigatorTop.setOutputMarkupId(true);
        add(navigatorBottom = new Pager("navigatorBottom"));
        navigatorBottom.setOutputMarkupId(true);
    }
    
    public void setItemsPerPage(int items) {
        dataView.setItemsPerPage(items);
    }
    
    public GeoServerTablePanel<T> setFilterable( boolean filterable ) {
        filterForm.setVisible( filterable );
        return this;
    }
    
    AjaxLink sortLink(final GeoServerDataProvider<T> dataProvider,
            ListItem item) {
        return new AjaxLink("link", item.getModel()) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                SortParam currSort = dataProvider.getSort();
                Property<T> property = (Property<T>) getModelObject();
                if (currSort == null
                        || !property.getName().equals(
                                currSort.getProperty())) {
                    dataProvider.setSort(new SortParam(property
                            .getName(), true));
                } else {
                    dataProvider.setSort(new SortParam(property
                            .getName(), !currSort.isAscending()));
                }
                target.addComponent(listContainer);
            }

        };
    }


//    private AjaxButton filterResetButton() {
//        return new AjaxButton("resetFilter") {
//
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form form) {
//                updateFilter(target, "");
//            }
//
//        };
//    }
//
//    private AjaxButton filterSubmitButton() {
//        return new AjaxButton("applyFilter") {
//
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form form) {
//                updateFilter(target, filter.getModelObjectAsString().trim());
//            }
//
//        };
//    }
    
    private void updateFilter(AjaxRequestTarget target, String flatKeywords) {
        if ("".equals(flatKeywords)) {
            dataProvider.setKeywords(null);
            filter.setModelObject("");
            dataView.setCurrentPage(0);
        } else {
            String[] keywords = flatKeywords.split("\\s+");
            dataProvider.setKeywords(keywords);
            dataView.setCurrentPage(0);
            ParamResourceModel paramResMod = new ParamResourceModel(
                    "matchedXOutOfY", this, 
                    Integer.valueOf(dataProvider.size()), 
                    Integer.valueOf(dataProvider.fullSize()));
        }
        navigatorTop.updateMatched();
        navigatorBottom.updateMatched();

        target.addComponent(listContainer);
        target.addComponent(navigatorTop);
        target.addComponent(navigatorBottom);
    }
    
    /**
     * Turns filtering abilities on/off.
     */
    public void setFilterVisible(boolean filterVisible) {
        filterForm.setVisible(filterVisible);
    }
    
    /**
     * Returns the component that will represent a property of a table item. Usually it should be a
     * label, or a link, but you can return pretty much everything.
     * 
     * @param itemModel
     * @param property
     * @return
     */
    protected abstract Component getComponentForProperty(String id, IModel itemModel,
            Property<T> property);

    class Pager extends Panel {

        GeoServerPagingNavigator navigator;
        Label matched;

        Pager(String id) {
            super(id);

            add(navigator = new GeoServerPagingNavigator("navigator", dataView) {
                @Override
                protected void onAjaxEvent(AjaxRequestTarget target) {
                    super.onAjaxEvent(target);
                    navigatorTop.updateMatched();
                    navigatorBottom.updateMatched();
                    target.addComponent(navigatorTop);
                    target.addComponent(navigatorBottom);
                }
            });
            ParamResourceModel paramResMod = new ParamResourceModel("showingAllRecords", this,
                        start(), end(), dataProvider.fullSize());
            add(matched = new Label("filterMatch", paramResMod));
        }

        void updateMatched() {
            if (dataProvider.getKeywords() == null) {
                ParamResourceModel paramResMod = new ParamResourceModel("showingAllRecords", this,
                        start(), end(), dataProvider.fullSize());
                matched.setModel(paramResMod);
            } else {
                ParamResourceModel paramResMod = new ParamResourceModel("matchedXOutOfY", this,
                        start(), end(), dataProvider.size(), dataProvider.fullSize());
                matched.setModel(paramResMod);
            }
        }
        
        int start() {
            if(dataView.getDataProvider().size() > 0)
                return dataView.getItemsPerPage() * dataView.getCurrentPage() + 1;
            else
                return 0;
        }
        
        int end() {
            int count = dataView.getPageCount();
            int page = dataView.getCurrentPage();
            if(page < (count - 1))
                return dataView.getItemsPerPage() * (page + 1);
            else
                return dataView.getDataProvider().size();
                
        }
    }
}
