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
import org.restlet.data.Status;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
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
public class UpdateLayerResource extends MapResource {
	private DataConfig dc;
	private Data dt;
	private String layerName;

	public UpdateLayerResource(DataConfig dc, Data dt) {
		this.dc = dc;
		this.dt = dt;
	}

	protected void putMap(Object details) throws RestletException {
		Map m = (Map) details;
		// TODO: Don't blindly assume map contains valid config info
		Object foundLayer = findLayer(getRequest().getAttributes());

		if (foundLayer != null) {
			if (foundLayer instanceof FeatureTypeInfo) {
				FeatureTypeInfo featureTypeInfo = (FeatureTypeInfo) foundLayer;
				String qualifiedName = featureTypeInfo.getDataStoreInfo().getId() + ":" + featureTypeInfo.getTypeName();
				
				FeatureTypeConfig ftConfig = dc.getFeatureTypeConfig(qualifiedName);
				
				if (m.containsKey("Style"))
					ftConfig.addStyle((String) m.get("Style"));

				if (m.containsKey("DefaultStyle"))
					ftConfig.setDefaultStyle((String) m.get("DefaultStyle"));
				
				if (m.containsKey("WMSPath"))
					ftConfig.setWmsPath((String) m.get("WMSPath"));
				
				//dc.removeFeatureType(qualifiedName);
				//dc.addFeatureType(qualifiedName, myFTC); // TODO: This isn't needed, is it?
		
				dt.load(dc.toDTO());
		
				try {
					saveConfiguration();
				} catch (ConfigurationException e) {
					throw new RestletException("Error while saving Catalog: " + e.getLocalizedMessage(), Status.SERVER_ERROR_INTERNAL);
				}
			}
			
			if (foundLayer instanceof CoverageInfo) {
				CoverageInfo coverageInfo = (CoverageInfo) foundLayer;
				String coverageName = coverageInfo.getName(); 
				       coverageName = coverageName.indexOf(":") > 0 ? coverageName.substring(coverageName.indexOf(":") + 1, coverageName.length()) : coverageName;
				String qualifiedName = coverageInfo.getFormatInfo().getId() + ":" + coverageName;
				
				CoverageConfig cvConfig = dc.getCoverageConfig(qualifiedName);
				
				if (m.containsKey("Style"))
					cvConfig.addStyle((String) m.get("Style"));

				if (m.containsKey("DefaultStyle"))
					cvConfig.setDefaultStyle((String) m.get("DefaultStyle"));
				
				if (m.containsKey("WMSPath"))
					cvConfig.setWmsPath((String) m.get("WMSPath"));
				
				dt.load(dc.toDTO());
				
				try {
					saveConfiguration();
				} catch (ConfigurationException e) {
					throw new RestletException("Error while saving Catalog: " + e.getLocalizedMessage(), Status.SERVER_ERROR_INTERNAL);
				}
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
