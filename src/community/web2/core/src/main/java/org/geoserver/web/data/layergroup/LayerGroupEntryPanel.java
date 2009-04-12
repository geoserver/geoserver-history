package org.geoserver.web.data.layergroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.data.layergroup.LayerGroupEditPage.LayerListPanel;
import org.geoserver.web.data.layergroup.LayerGroupEditPage.StyleListPanel;
import org.geoserver.web.wicket.GeoServerDataProvider;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ImageAjaxLink;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

public class LayerGroupEntryPanel extends Panel {

    ModalWindow popupWindow;
    LayerGroupEntryProvider entryProvider;
    GeoServerTablePanel<LayerGroupEntry> layerTable;
    List<LayerGroupEntry> items;
    
    public LayerGroupEntryPanel( String id, LayerGroupInfo layerGroup ) {
        super( id );
        
        items = new ArrayList();
        for ( int i = 0; i < layerGroup.getLayers().size(); i++ ) {
            LayerInfo layer = layerGroup.getLayers().get( i );
            StyleInfo style = layerGroup.getStyles().get( i );
            items.add( new LayerGroupEntry( layer, style ) );
        }
        
        add( popupWindow = new ModalWindow( "popup" ) );
        
        //layers
        entryProvider = new LayerGroupEntryProvider( items );
        add( layerTable = new GeoServerTablePanel<LayerGroupEntry>("layers",entryProvider) {

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
                if ( property == LayerGroupEntryProvider.POSITION ) {
                    return positionPanel( id, itemModel ); 
                }
                
                return null;
            }
        }.setFilterable( false ));
        layerTable.setOutputMarkupId( true );
        
        add( new AjaxLink( "add" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 375 );
                popupWindow.setInitialWidth( 525 );
                popupWindow.setContent( new LayerListPanel(popupWindow.getContentId()) {
                    @Override
                    protected void handleLayer(LayerInfo layer, AjaxRequestTarget target) {
                        popupWindow.close( target );
                        
                        entryProvider.getItems().add(
                            new LayerGroupEntry( layer, layer.getDefaultStyle() ) );
                        
                        //getCatalog().save( lg );
                        target.addComponent( layerTable );
                    }
                });
                
                popupWindow.show(target);
            }
        });
    }
    
    public List<LayerGroupEntry> getEntries() {
        return items;
    }
    
    Component layerLink(String id, IModel itemModel) {
        LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
        return new Label( id, entry.getLayer().getName() );
    }
    
    Component styleLink(String id, final IModel itemModel) {
        LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
        return new SimpleAjaxLink( id, new Model( entry.getStyle().getName() )) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 375 );
                popupWindow.setInitialWidth( 525 );
                popupWindow.setContent( new StyleListPanel( popupWindow.getContentId() ) {
                    @Override
                    protected void handleStyle(StyleInfo style, AjaxRequestTarget target) {
                        popupWindow.close( target );
                        
                        LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
                        entry.setStyle( style );
                        
                        //redraw
                        target.addComponent( layerTable );
                    }
                });
                popupWindow.show(target);
            }

        };
    }
    
    Component removeLink(String id, IModel itemModel) {
        final LayerGroupEntry entry = (LayerGroupEntry) itemModel.getObject();
        return new ImageAjaxLink( id, new ResourceReference( getClass(), "../../img/icons/silk/delete.png") ) {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                
                items.remove( entry );
                target.addComponent( layerTable );
            }
        };
    }
    
    Component positionPanel(String id, IModel itemModel) {
        return new PositionPanel( id, (LayerGroupEntry) itemModel.getObject() );
    }
  
    static class LayerGroupEntryProvider extends GeoServerDataProvider<LayerGroupEntry> {

        public static Property<LayerGroupEntry> LAYER = 
            new PropertyPlaceholder<LayerGroupEntry>( "layer" );

        public static Property<LayerGroupEntry> STYLE = 
            new PropertyPlaceholder<LayerGroupEntry>( "style" );
        
        public static Property<LayerGroupEntry> REMOVE = 
            new PropertyPlaceholder<LayerGroupEntry>( "remove" );
        
        public static Property<LayerGroupEntry> POSITION = 
            new PropertyPlaceholder<LayerGroupEntry>( "position" );

        static List PROPERTIES = Arrays.asList( LAYER, STYLE, REMOVE, POSITION );
        
        List<LayerGroupEntry> items;
        
        public LayerGroupEntryProvider( List<LayerGroupEntry> items ) {
            this.items = items;
        }
        
        @Override
        protected List<LayerGroupEntry> getItems() {
            return items; 
        }

        @Override
        protected List<Property<LayerGroupEntry>> getProperties() {
            return PROPERTIES;
        }

        public IModel model(Object object) {
            return new Model( (LayerGroupEntry) object );//return ((LayerGroupEntry)object).toDetachableModel();
        }
    }

    class PositionPanel extends Panel {
        
        LayerGroupEntry entry;
        public PositionPanel( String id, LayerGroupEntry entry ) {
            super( id );
            this.entry = entry;
            
            if ( items.indexOf( entry ) > 0 ) {
                add( new ImageAjaxLink( "up", new ResourceReference( getClass(), "../../img/icons/silk/arrow_up.png") ) {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        int index = items.indexOf( PositionPanel.this.entry );
                        items.remove( index );
                        items.add( index-1, PositionPanel.this.entry );
                        target.addComponent( layerTable );
                    }
                });
            }
            else {
                add( new ImageAjaxLink( "up", new ResourceReference( getClass(), "../../img/icons/blank.png") ) {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                    }
                });
            }
            
            if ( items.indexOf( entry ) < items.size() - 1 ) {
                add( new ImageAjaxLink( "down", new ResourceReference( getClass(), "../../img/icons/silk/arrow_down.png") ) {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        int index = items.indexOf( PositionPanel.this.entry );
                        items.remove( index );
                        items.add( index+1, PositionPanel.this.entry );
                        target.addComponent( layerTable );
                    }
                });
            }
            else {
                add( new ImageAjaxLink( "down", new ResourceReference( getClass(), "../../img/icons/blank.png") ) {
                    @Override
                    protected void onClick(AjaxRequestTarget target) {
                        
                    }
                });
            }
        }
    }
}
