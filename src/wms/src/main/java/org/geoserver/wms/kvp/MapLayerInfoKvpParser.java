package org.geoserver.wms.kvp;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.LayerInfo;
import org.geoserver.ows.FlatKvpParser;
import org.geoserver.ows.KvpParser;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;

/**
 * KVP parser to parse a comma separated list of layer names into a list of {@link MapLayerInfo}
 * 
 * @author Gabriel Roldan
 */
public class MapLayerInfoKvpParser extends KvpParser {

    private FlatKvpParser rawNamesParser;

    private final WMS wms;

    public MapLayerInfoKvpParser(final String key, final WMS wms) {
        super(key, MapLayerInfo.class);
        this.wms = wms;
        rawNamesParser = new FlatKvpParser(key, String.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MapLayerInfo> parse(final String paramValue) throws Exception {

        final List<String> layerNames = (List<String>) rawNamesParser.parse(paramValue);

        List<MapLayerInfo> layers = new ArrayList<MapLayerInfo>(layerNames.size());

        MapLayerInfo layer = null;

        for (String layerName : layerNames) {

            LayerInfo layerInfo = wms.getLayerByName(layerName);
            if (layerInfo == null) {
                throw new ServiceException(layerName + ": no such layer on this server",
                        "LayerNotDefined", getClass().getSimpleName());
            }
            layer = new MapLayerInfo(layerInfo);
            layers.add(layer);
        }

        return layers;
    }

}