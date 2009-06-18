package org.geoserver.web.importer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.importer.LayerSummary;
import org.geoserver.web.wicket.GeoServerDataProvider;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;

@SuppressWarnings("serial")
public class ImportSummaryProvider extends GeoServerDataProvider<LayerSummary> {
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImportSummaryProvider.class);
    
    static final Property<LayerSummary> LAYER = new BeanProperty<LayerSummary>("name", "layerName");
    static final Property<LayerSummary> SUCCESS = new AbstractProperty<LayerSummary>("success") {

        public Object getPropertyValue(LayerSummary item) {
            return item.getStatus().successful();
        }
        
    };
    
    static final Property<LayerSummary> DETAILS = new AbstractProperty<LayerSummary>("details") {
        public Object getPropertyValue(LayerSummary item) {
            return new ResourceModel("ImportSummaryPage." + item.getStatus());
        }
    };
    
    static final Property<LayerSummary> SRS = new BeanProperty<LayerSummary>("SRS", "layer.resource.SRS");
    static final Property<LayerSummary> TYPE = new AbstractProperty<LayerSummary>("type") {

        public Object getPropertyValue(LayerSummary item) {
            try {
                if(item.getLayer() == null)
                    return null;
                FeatureType type = ((FeatureTypeInfo) item.getLayer().getResource()).getFeatureType();
                GeometryDescriptor gd = type.getGeometryDescriptor();
                if(gd == null)
                    return null;
                else
                    return gd.getType().getBinding().getSimpleName();
            } catch(IOException e) {
                LOGGER.log(Level.WARNING, "Problems occurred computing the geometry type");
                return null;
            }
        }
    };
    
    static final Property<LayerSummary> COMMANDS= new PropertyPlaceholder<LayerSummary>("commands");
    
    private List<LayerSummary> layers;

    public ImportSummaryProvider(List<LayerSummary> layers) {
        this.layers = layers;
    }

    @Override
    protected List<LayerSummary> getItems() {
        return layers;
    }

    @Override
    protected List getProperties() {
        return Arrays.asList(LAYER, TYPE, SUCCESS, DETAILS, SRS, COMMANDS);
    }

    public IModel model(Object object) {
        return new Model((LayerSummary) object);
    }

}

