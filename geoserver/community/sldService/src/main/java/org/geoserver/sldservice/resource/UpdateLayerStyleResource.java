package org.geoserver.sldservice.resource;

/* utility class that updates SLD property of a WMS layer. 
 */

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geoserver.rest.RestletException;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

/**
 * @author AlFa
 * 
 */
public class UpdateLayerStyleResource extends MapResource {
	private DataConfig dc;
	private Data dt;
	private String layerName;

	public UpdateLayerStyleResource(DataConfig dc, Data dt) {
		this.dc = dc;
		this.dt = dt;
	}

	protected void putMap(Map m) throws Exception {
		// TODO: Don't blindly assume map contains valid config info
		Object foundLayer = findLayer(getRequest().getAttributes());

		if (foundLayer != null) {
			if (foundLayer instanceof FeatureTypeInfo) {
				FeatureTypeInfo featureTypeInfo = (FeatureTypeInfo) foundLayer;
				String qualifiedName = featureTypeInfo.getDataStoreInfo().getId() + ":" + featureTypeInfo.getTypeName();
				dc.getFeatureTypeConfig(qualifiedName).setDefaultStyle((String) m.get("Style"));
				//dc.removeFeatureType(qualifiedName);
				//dc.addFeatureType(qualifiedName, myFTC); // TODO: This isn't needed, is it?
		
				dt.load(dc.toDTO());
		
				saveConfiguration();
			}
			
			if (foundLayer instanceof CoverageInfo) {
				CoverageInfo coverageInfo = (CoverageInfo) foundLayer;
			}
		}

	}

	private void saveConfiguration() throws ConfigurationException {
		dt.load(dc.toDTO());
		XMLConfigWriter.store((DataDTO) dt.toDTO(), GeoserverDataDirectory.getGeoserverDataDirectory());
	}
    
	private Object findLayer(Map attributes) {
		/* Looks in attribute map if there is the featureType param */
		if (attributes.containsKey("layerName")) {
			this.layerName = (String) attributes.get("layerName");

			/* First try to find as a FeatureType */
			try {
				return this.dt.getFeatureTypeInfo(this.layerName);
			} catch (NoSuchElementException e) {
				/* Try to find as coverage */
				try {
					return this.dt.getCoverageInfo(this.layerName);
				} catch (NoSuchElementException e1) {
					/* resurce not found return null */
					return null;
				}
			}
			/*attribute not found*/
		} else
			return null;
	}
	
	public boolean allowGet() {
		return false;
	}

	public boolean allowPut() {
		return true;
	}

	public boolean allowDelete() {
		return false;
	}

	public void handleDelete() {
	}

	@Override
	public Object getMap() throws RestletException {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getSupportedFormats() {
		Map m = new HashMap();

		m.put("json", new JSONFormat());
		m.put("xml", new AutoXMLFormat("LayerConfig"));
		m.put(null, m.get("xml"));

		return m;
	}
}
