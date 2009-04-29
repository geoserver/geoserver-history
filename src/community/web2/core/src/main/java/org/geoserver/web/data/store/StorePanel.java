package org.geoserver.web.data.store;

import static org.geoserver.web.data.store.StoreProvider.ENABLED;
import static org.geoserver.web.data.store.StoreProvider.NAME;
import static org.geoserver.web.data.store.StoreProvider.REMOVE;
import static org.geoserver.web.data.store.StoreProvider.TYPE;
import static org.geoserver.web.data.store.StoreProvider.WORKSPACE;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.CatalogIconFactory;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.data.workspace.WorkspaceEditPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

public class StorePanel extends GeoServerTablePanel<StoreInfo> {

    private static final long serialVersionUID = 5957961031378924960L;

    private ModalWindow popupWindow;

    public StorePanel(String id) {
        this(id, new StoreProvider());
    }

    public StorePanel(String id, StoreProvider provider) {
        super(id, provider);

        // the popup window for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);
    }

    private Catalog getCatalog() {
        return GeoServerApplication.get().getCatalog();
    }

    @Override
    protected Component getComponentForProperty(String id, IModel itemModel,
            Property<StoreInfo> property) {

        final CatalogIconFactory icons = CatalogIconFactory.get();

        if (property == TYPE) {
            final StoreInfo storeInfo = (StoreInfo) itemModel.getObject();

            ResourceReference storeIcon = icons.getStoreIcon(storeInfo);

            Fragment f = new Fragment(id, "iconFragment", StorePanel.this);
            f.add(new Image("storeIcon", storeIcon));

            return f;
        } else if (property == WORKSPACE) {
            return workspaceLink(id, itemModel);
        } else if (property == NAME) {
            return storeNameLink(id, itemModel);
        } else if (property == ENABLED) {
            return new Label(id, ENABLED.getModel(itemModel));
        } else if (property == REMOVE) {
            return removeLink(id, itemModel);
        }
        throw new IllegalArgumentException("Don't know a property named " + property.getName());
    }

    private Component storeNameLink(String id, final IModel itemModel) {
        return new SimpleAjaxLink(id, NAME.getModel(itemModel)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String storeName = getLink().getModelObjectAsString();
                StoreInfo store = getCatalog().getStoreByName(storeName, StoreInfo.class);
                if (store == null) {
                    popupWindow.setContent(new Label(popupWindow.getContentId(),
                            "Cannot find the store " + store
                                    + " anymore, I guess it has been removed. "
                                    + "Will refresh the store list."));
                    popupWindow.show(target);
                    target.addComponent(StorePanel.this);
                } else if (store instanceof DataStoreInfo) {
                    setResponsePage(new DataAccessEditPage(store.getId()));
                } else {
                    setResponsePage(new CoverageStoreEditPage(store.getId()));
                }
            }
        };
    }

    private Component workspaceLink(String id, IModel itemModel) {
        return new SimpleAjaxLink(id, WORKSPACE.getModel(itemModel)) {
            public void onClick(AjaxRequestTarget target) {
                WorkspaceInfo info = getCatalog().getWorkspaceByName(getModelObjectAsString());
                if (info != null)
                    setResponsePage(new WorkspaceEditPage(info));
            }
        };
    }

    protected Component removeLink(String id, final IModel itemModel) {
        StoreInfo info = (StoreInfo) itemModel.getObject();

        ResourceModel resRemove = new ResourceModel("removeStore", "Remove");

        ParamResourceModel confirmRemove = new ParamResourceModel("confirmRemoveStoreX", this, info
                .getName());

        SimpleAjaxLink linkPanel = new ConfirmationAjaxLink(id, null, resRemove, confirmRemove) {
            public void onClick(AjaxRequestTarget target) {
                getCatalog().remove((StoreInfo) itemModel.getObject());
                target.addComponent(StorePanel.this);
            }
        };
        return linkPanel;
    }
}
