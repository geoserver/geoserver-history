package org.vfny.geoserver.global;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.data.coverage.grid.stream.StreamGridCoverageExchange;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageExchange;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.DefaultCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class MapLayerInfo extends GlobalLayerSupertype {
	public static int TYPE_VECTOR = 0;
	public static int TYPE_RASTER = 1;
	
	private FeatureTypeInfo feature;
	private CoverageInfo coverage;
	private int type;
	
    private String name;

    private String label;

    private String description;

    private String dirName;
    
    public MapLayerInfo() {
    	name = "";
    	label = "";
    	description = "";
    	dirName = "";
    	
    	coverage = null;
    	feature = null;
    	type = -1;
    }
    
    public MapLayerInfo(CoverageInfoDTO dto, Data data)
        throws ConfigurationException {

        name = dto.getName();
        label = dto.getLabel();
        description = dto.getDescription();
        dirName = dto.getDirName();
        
        coverage = new CoverageInfo(dto, data);
        feature = null;
        type = TYPE_RASTER;
    }

    public MapLayerInfo(FeatureTypeInfoDTO dto, Data data)
	    throws ConfigurationException {
	
	    name = dto.getName();
	    label = dto.getTitle();
	    description = dto.getAbstract();
	    dirName = dto.getDirName();
	    
	    feature = new FeatureTypeInfo(dto, data);
	    coverage = null;
	    type = TYPE_VECTOR;
    }

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * getBoundingBox purpose.
     * 
     * <p>
     * The feature source bounds.
     * </p>
     *
     * @return Envelope the feature source bounds.
     *
     * @throws IOException when an error occurs
     */
    public Envelope getBoundingBox() throws IOException {
    	if( this.type == TYPE_VECTOR ) {
            return feature.getBoundingBox();
    	} else {
    		return coverage.getEnvelope();
    	}
    }

	public CoverageInfo getCoverage() {
		return coverage;
	}
	public void setCoverage(CoverageInfo coverage) {
        this.name = coverage.getName();
        this.label = coverage.getLabel();
        this.description = coverage.getDescription();
        this.dirName = coverage.getDirName();
        
		this.coverage = coverage;
		this.feature = null;
		this.type = TYPE_RASTER;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public FeatureTypeInfo getFeature() {
		return feature;
	}
	public void setFeature(FeatureTypeInfo feature) {
	    this.name = feature.getName();
	    this.label = feature.getTitle();
	    this.description = feature.getAbstract();
	    this.dirName = feature.getDirName();
	    
		this.feature = feature;
		this.coverage = null;
	    this.type = TYPE_VECTOR;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
	private URL getResource(String path, String baseDir) throws MalformedURLException{
		URL url = null;
		if (path.startsWith("file:data/")) {
			path = path.substring(5); // remove 'file:' prefix
			
			File file = new File(baseDir, path);
			url = file.toURL();
		}
		
		return url;
	}

	private Feature wrapGcInFeature(GridCoverage gridCoverage)
	throws IllegalAttributeException, SchemaException {
		// create surrounding polygon
		PrecisionModel pm = new PrecisionModel();
		CoordinateSequenceFactory csf = DefaultCoordinateSequenceFactory.instance();
		GeometryFactory gf = new GeometryFactory(pm, 0);
		Coordinate[] coord = new Coordinate[5];
		Rectangle2D rect = ((GridCoverage2D) gridCoverage).getEnvelope2D().getBounds2D();
		coord[0] = new Coordinate(rect.getMinX(), rect.getMinY());
		coord[1] = new Coordinate(rect.getMaxX(), rect.getMinY());
		coord[2] = new Coordinate(rect.getMaxX(), rect.getMaxY());
		coord[3] = new Coordinate(rect.getMinX(), rect.getMaxY());
		coord[4] = new Coordinate(rect.getMinX(), rect.getMinY());
		
		LinearRing ring = new LinearRing(csf.create(coord), gf);
		Polygon bounds = new Polygon(ring, null, gf);
		
		// create the feature type
		AttributeType geom = AttributeTypeFactory.newAttributeType("geom", Polygon.class);
		AttributeType grid = AttributeTypeFactory.newAttributeType("grid", GridCoverage2D.class);
		
		FeatureType schema = null;
		AttributeType[] attTypes = {geom, grid};
		
		schema = FeatureTypeFactory.newFeatureType(attTypes, this.name);
		
		// create the feature
		Feature feature = schema.create(new Object[] {bounds, gridCoverage});
		
		return feature;
	}
	
	
	private GridCoverage getGridCoverage(HttpServletRequest request, CoverageInfo meta) throws IOException {
		GridCoverage2D coverage = null;
		
		try {
			String formatID = meta.getFormatId();
			DataConfig dataConfig = (DataConfig) request
						.getSession()
						.getServletContext()
						.getAttribute(DataConfig.CONFIG_KEY);
			DataFormatConfig dfConfig = dataConfig.getDataFormat(formatID);

			String realPath = request.getRealPath("/");
			URL url = getResource(dfConfig.getUrl(), realPath);
			GridCoverageExchange gce = new StreamGridCoverageExchange();
			GridCoverageReader reader = gce.getReader(url);
			Format format = reader.getFormat();
			ParameterValueGroup params = format.getReadParameters();
			
			if( params != null ) {
				List list=params.values();
				Iterator it=list.iterator();
				while(it.hasNext())
				{
					ParameterValue param=((ParameterValue)it.next());
					ParameterDescriptor descr=(ParameterDescriptor)param.getDescriptor();
					
					Object value = null;
					String key = descr.getName().toString();
					
					Class[] clArray = {String.class};
					Object[] inArray = {dfConfig.getParameters().get(key)};
					value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
					
					params.parameter(key).setValue(value);
				}
			}
			
			coverage = (GridCoverage2D) reader.read(
					params != null ?
					(GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[params.values().size()])
					: null
					);
		} catch (InvalidParameterValueException e) {
			throw new IOException(e.getMessage());
		} catch (ParameterNotFoundException e) {
			throw new IOException(e.getMessage());
		} catch (MalformedURLException e) {
			throw new IOException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IOException(e.getMessage());
		} catch (SecurityException e) {
			throw new IOException(e.getMessage());
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		} catch (InstantiationException e) {
			throw new IOException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IOException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IOException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IOException(e.getMessage());
		}
		
		return coverage;
	}
	
	public FeatureCollection getCoverageToFeatures(HttpServletRequest request)
	throws DataSourceException {
		FeatureCollection collection = FeatureCollections.newCollection();
		// last step, wrap, add the the feature collection and return
		try {
			GridCoverage2D gridCoverage = (GridCoverage2D) getGridCoverage(request, this.coverage);
			collection.add(wrapGcInFeature(gridCoverage));
		} catch (Exception e) {
			throw new DataSourceException("IO error", e);
		}
		
		return collection;
	}
	
}