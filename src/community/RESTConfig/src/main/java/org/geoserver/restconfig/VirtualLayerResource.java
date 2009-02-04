package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.FreemarkerFormat;
import org.geoserver.rest.format.MapJSONFormat;
import org.geoserver.rest.format.MapXMLFormat;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;

public class VirtualLayerResource extends MapResource {
    Data myData;

    DataConfig myDataConfig;

    public VirtualLayerResource(Data d, DataConfig dc, Context context, Request request, Response response) {
        super(context,request,response);
        myData = d;
        myDataConfig = dc;
    }

    public void setData(Data d) {
        myData = d;
    }

    public void setDataConfig(DataConfig dc) {
        myDataConfig = dc;
    }

    public Data getData() {
        return myData;
    }

    public DataConfig getDataConfig() {
        return myDataConfig;
    }

    @Override
    protected Map<String, DataFormat> createSupportedFormats(Request request,
            Response response) {
        HashMap m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/vlayer.ftl",
                getClass(), MediaType.TEXT_HTML));
        m.put("xml", new MapXMLFormat());
        m.put("json", new MapJSONFormat());
        m.put(null, m.get("xml"));

        return m;
    }

    public Map getMap() {
        Map folders = RESTUtils.getVirtualFolderMap(getDataConfig());
        String folderName = (String) getRequest().getAttributes().get("folder");
        String layerName = (String) getRequest().getAttributes().get("layer");

        if (folders.containsKey(folderName)
                && folders.get(folderName) instanceof Map) {
            Map folder = (Map) folders.get(folderName);
            if (folder.containsKey(layerName)) {
                Object layer = folder.get(layerName);
                if (layer instanceof DataStoreConfig) {
                    return DataStoreResource.getMap((DataStoreConfig) layer);
                } else if (layer instanceof CoverageStoreConfig) {
                    return CoverageStoreResource
                            .getMap((CoverageStoreConfig) layer);
                }
            }
        }

        return null;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public synchronized void handleDelete() {
        Map folders = RESTUtils.getVirtualFolderMap(getDataConfig());
        String folderName = (String) getRequest().getAttributes().get("folder");
        String layerName = (String) getRequest().getAttributes().get("layer");

        if (folders.containsKey(folderName)
                && folders.get(folderName) instanceof Map) {
            Map folder = (Map) folders.get(folderName);
            if (folder.containsKey(layerName)) {
                Object layer = folder.get(layerName);
                if (layer instanceof DataStoreConfig) {
                    // not implemented yet
                } else if (layer instanceof CoverageStoreConfig) {
                    final CoverageStoreConfig coverageStoreConfig = (CoverageStoreConfig) layer;
                    final String coverageStoreConfigId = coverageStoreConfig
                            .getId();
                    final Iterator it = myDataConfig.getCoverages().values()
                            .iterator();
                    final List<CoverageConfig> coverageConfigs = new ArrayList<CoverageConfig>();
                    while (it.hasNext()) {
                        final CoverageConfig coverageConfig = (CoverageConfig) it
                                .next();
                        final String coverageConfigId = coverageConfig
                                .getFormatId();
                        if (coverageConfigId
                                .equalsIgnoreCase(coverageStoreConfigId))
                            coverageConfigs.add(coverageConfig);

                    }
                    if (!coverageConfigs.isEmpty()) {
                        boolean error = false;
                        for (CoverageConfig coverageConfig : coverageConfigs) {
                            if (myDataConfig
                                    .removeCoverage(coverageStoreConfigId + ":"
                                            + coverageConfig.getName()) == null)
                                error = true;
                        }
                        if (!error) {
                            if (myDataConfig
                                    .removeDataFormat(coverageStoreConfig
                                            .getId()) != null)
                                try {
                                    RESTUtils.saveConfiguration(
                                            getDataConfig(), getData());
                                } catch (ConfigurationException e) {
                                    getResponse().setEntity(
                                            new StringRepresentation(
                                                    "Failure while saving configuration: "
                                                            + e,
                                                    MediaType.TEXT_PLAIN));
                                    getResponse().setStatus(
                                            Status.SERVER_ERROR_INTERNAL);
                                    return;
                                }
                            else
                                error = true;
                        }
                        if (true)
                            try {
                                RESTUtils.reloadConfiguration();
                            } catch (Exception e) {
                                getResponse().setEntity(
                                        new StringRepresentation(
                                                "Failure while reloading configuration: "
                                                        + e,
                                                MediaType.TEXT_PLAIN));
                                getResponse().setStatus(
                                        Status.SERVER_ERROR_INTERNAL);
                                return;
                            }

                    }
                }
            }
        }
    }

}
