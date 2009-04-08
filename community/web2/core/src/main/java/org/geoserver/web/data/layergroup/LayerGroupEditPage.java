package org.geoserver.web.data.layergroup;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.style.StyleDetachableModel;
import org.geoserver.web.data.table.LayerDetachableModel;
import org.geoserver.web.wicket.EnvelopePanel;
import org.geoserver.web.wicket.GeoServerDataProvider;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.BeanProperty;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class LayerGroupEditPage extends GeoServerSecuredPage {

    IModel lgModel;
    ModalWindow popupWindow;
    GeoServerTablePanel layerTable;
    
    public LayerGroupEditPage( LayerGroupInfo layerGroup ) {
        lgModel = new LayerGroupDetachableModel( layerGroup );
        
        add( popupWindow = new ModalWindow( "popup" ) );
       
        Form form = new Form( "form", new CompoundPropertyModel( lgModel ) ) {
            protected void onSubmit() {
                Catalog catalog = getCatalog();
                catalog.save( (LayerGroupInfo) lgModel.getObject() );
            }
        };
        add(form);
        form.add(new TextField("name"));
        
        //bounding box
        form.add(new EnvelopePanel( "bounds" ).setReadOnly(true));
        form.add(new SubmitLink( "generateBounds") {
            @Override
            public void onSubmit() {
                LayerGroupInfo lg = (LayerGroupInfo) lgModel.getObject();
                try {
                    new CatalogBuilder( getCatalog() ).calculateLayerGroupBounds( lg );
                    getCatalog().save( lg );
                } 
                catch (Exception e) {
                    throw new WicketRuntimeException( e );
                }
            }
        });
        
        //layers
        LayerGroupEntryProvider provider = new LayerGroupEntryProvider(layerGroup);
        form.add( layerTable = new GeoServerTablePanel<LayerGroupEntry>("layers",provider) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<LayerGroupEntry> property) {
                if ( property == LayerGroupEntryProvider.LAYER ) {
                    return layerLink( id, itemModel );
                }
                if ( property == LayerGroupEntryProvider.STYLE ) {
                    return styleLink( id, itemModel );
                }
                if ( property == LayerGroupEntryProvider.REMOVE ) {
                    return removeLink( id, itemModel );
                }
                return null;
            }
        }.setFilterable( false ));
        layerTable.setOutputMarkupId( true );
      
        form.add( new AjaxLink( "add" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 375 );
                popupWindow.setInitialWidth( 525 );
                popupWindow.setContent( new LayerListPanel(popupWindow.getContentId()) {
                    @Override
                    protected void handleLayer(LayerInfo layer, AjaxRequestTarget target) {
                        popupWindow.close( target );
                        
                        LayerGroupInfo lg = (LayerGroupInfo) lgModel.getObject();
                        lg.getLayers().add( layer );
                        lg.getStyles().add( layer.getDefaultStyle() );
                        
                        getCatalog().save( lg );
                        target.addComponent( layerTable );
                    }
                });
                
                popupWindow.show(target);
                
            }
        });
        
        
        form.add(new SubmitLink("save"){
            @Override
            public void onSubmit() {
                LayerGroupInfo lg = (LayerGroupInfo) lgModel.getObject();
                getCatalog().save( lg );
            }
        });
        form.add(new BookmarkablePageLink("cancel", LayerGroupPage.class));
    }
    
    Component layerLink(String id, IModel itemModel) {
        LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
        return new Label( id, entry.layer.getName() );
    }
    
    Component styleLink(String id, final IModel itemModel) {
        LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
        return new SimpleAjaxLink( id, new Model( entry.style.getName() )) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 375 );
                popupWindow.setInitialWidth( 525 );
                popupWindow.setContent( new StyleListPanel( popupWindow.getContentId() ) {
                    @Override
                    protected void handleStyle(StyleInfo style, AjaxRequestTarget target) {
                        popupWindow.close( target );
                        
                        LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
                        
                        //update the style
                        LayerGroupInfo lg = (LayerGroupInfo) lgModel.getObject();
                        
                        lg.getStyles().set( entry.index, style );
                        getCatalog().save( lg );
                        
                        //redraw
                        target.addComponent( layerTable );
                    }
                });
                popupWindow.show(target);
            }

        };
    }
    
    Component removeLink(String id, IModel itemModel) {
        return new AjaxLink( id ) {

            @Override
            public void onClick(AjaxRequestTarget target) {
            }
            
        };
    }
    
    abstract static class StyleListPanel extends GeoServerTablePanel<StyleInfo> {

        static Property<StyleInfo> NAME = 
            new BeanProperty<StyleInfo>("name", "name");
        
        public StyleListPanel(String id) {
            super(id, new GeoServerDataProvider<StyleInfo>() {
                @Override
                protected List<StyleInfo> getItems() {
                    return getCatalog().getStyles();
                }

                @Override
                protected List<Property<StyleInfo>> getProperties() {
                    return Arrays.asList( NAME );
                }

                public IModel model(Object object) {
                    return new StyleDetachableModel( (StyleInfo) object );
                }
            });
        }

        @Override
        protected Component getComponentForProperty(String id, IModel itemModel,
                Property<StyleInfo> property) {
            final StyleInfo style = (StyleInfo) itemModel.getObject();
            if ( property == NAME ) {
                return new SimpleAjaxLink( id, NAME.getModel( itemModel ) ) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        handleStyle(style, target);
                    }
                };
            }
            
            return null;
        }
        
        protected abstract void handleStyle( StyleInfo style, AjaxRequestTarget target );

    }

    abstract static class LayerListPanel extends GeoServerTablePanel<LayerInfo> {
        static Property<LayerInfo> NAME = 
            new BeanProperty<LayerInfo>("name", "name");
        
        static Property<LayerInfo> STORE = 
            new BeanProperty<LayerInfo>("store", "resource.store.name");
        
        static Property<LayerInfo> WORKSPACE = 
            new BeanProperty<LayerInfo>("workspace", "resource.store.workspace.name");
        
        LayerListPanel( String id ) {
            super( id, new GeoServerDataProvider<LayerInfo>() {

                @Override
                protected List<LayerInfo> getItems() {
                    return getCatalog().getLayers();
                }

                @Override
                protected List<Property<LayerInfo>> getProperties() {
                    return Arrays.asList( NAME, STORE, WORKSPACE );
                }

                public IModel model(Object object) {
                    return new LayerDetachableModel((LayerInfo)object);
                }

            });
        }
        
        @Override
        protected Component getComponentForProperty(String id, final IModel itemModel,
                Property<LayerInfo> property) {
            IModel model = property.getModel( itemModel );
            if ( NAME == property ) {
                return new SimpleAjaxLink( id, model ) {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        LayerInfo layer = (LayerInfo) itemModel.getObject();
                        handleLayer( layer, target );
                    }
                };
            }
            else {
                return new Label( id, model );
            }
        }
        
        protected void handleLayer( LayerInfo layer, AjaxRequestTarget target ) {
        }
    }
}
