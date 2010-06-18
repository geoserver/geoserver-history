package org.geoserver.wps.sextante;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import es.unex.sextante.dataObjects.IVectorLayer;

public class ShpLayerFactory extends DatastoreVectorLayerFactory{

	@Override
	protected IVectorLayer createLayer(DataStore dataStore, String sName,
			Object crs) throws IOException {
		GTShpLayer layer = GTShpLayer.createLayer(dataStore, sName,
				(CoordinateReferenceSystem) crs);
		layer.setName(sName);
		return layer;
	}

	public DataStore createDatastore(String m_sFilename, SimpleFeatureType m_FeatureType) throws IOException {
		File file = new File(m_sFilename);
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put(ShapefileDataStoreFactory.URLP.key, file.toURI()
				.toURL());
		params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key,
				false);

		FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
		ShapefileDataStore dataStore = (ShapefileDataStore) factory.createNewDataStore(params);
		dataStore.createSchema(m_FeatureType);
		return dataStore;
	}

}
