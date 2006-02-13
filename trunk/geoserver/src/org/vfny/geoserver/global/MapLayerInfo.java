/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;


import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.DefaultFeature;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureTypeBuilder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.type.GeometricAttributeType;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.util.CoverageUtils;
import org.vfny.geoserver.util.DataFormatUtils;

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
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class MapLayerInfo extends GlobalLayerSupertype {
	public static int TYPE_VECTOR = 0;
	public static int TYPE_RASTER = 1;

	/**
	 * 
	 * @uml.property name="feature"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private FeatureTypeInfo feature;

	/**
	 * 
	 * @uml.property name="coverage"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private CoverageInfo coverage;

	/**
	 * 
	 * @uml.property name="type" multiplicity="(0 1)"
	 */
	private int type;

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 * @uml.property name="label" multiplicity="(0 1)"
	 */
	private String label;

	/**
	 * 
	 * @uml.property name="description" multiplicity="(0 1)"
	 */
	private String description;

	/**
	 * 
	 * @uml.property name="dirName" multiplicity="(0 1)"
	 */
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
    		GeneralEnvelope bounds = null;
    		try {
    			bounds = DataFormatUtils.adjustEnvelope(coverage.getEnvelope().getCoordinateReferenceSystem(), coverage.getEnvelope());
    		} catch (MismatchedDimensionException e) {
    			throw new IOException("Problems getting Coverage BoundingBox: " + e.getMessage());
    		} catch (IndexOutOfBoundsException e) {
    			throw new IOException("Problems getting Coverage BoundingBox: " + e.getMessage());
    		} catch (NoSuchAuthorityCodeException e) {
    			throw new IOException("Problems getting Coverage BoundingBox: " + e.getMessage());
    		}
    		
    		return new Envelope(
    				bounds.getLowerCorner().getOrdinate(0),
    				bounds.getUpperCorner().getOrdinate(0),
    				bounds.getLowerCorner().getOrdinate(1),
    				bounds.getUpperCorner().getOrdinate(1)
			);
    	}
    }

	/**
	 * 
	 * @uml.property name="coverage"
	 */
	public CoverageInfo getCoverage() {
		return coverage;
	}

	/**
	 * 
	 * @uml.property name="coverage"
	 */
	public void setCoverage(CoverageInfo coverage) {
		this.name = coverage.getName();
		this.label = coverage.getLabel();
		this.description = coverage.getDescription();
		this.dirName = coverage.getDirName();

		this.coverage = coverage;
		this.feature = null;
		this.type = TYPE_RASTER;
	}

	/**
	 * 
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @uml.property name="dirName"
	 */
	public String getDirName() {
		return dirName;
	}

	/**
	 * 
	 * @uml.property name="dirName"
	 */
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	/**
	 * 
	 * @uml.property name="feature"
	 */
	public FeatureTypeInfo getFeature() {
		return feature;
	}

	/**
	 * 
	 * @uml.property name="feature"
	 */
	public void setFeature(FeatureTypeInfo feature) {
		this.name = feature.getName();
		this.label = feature.getTitle();
		this.description = feature.getAbstract();
		this.dirName = feature.getDirName();

		this.feature = feature;
		this.coverage = null;
		this.type = TYPE_VECTOR;
	}

	/**
	 * 
	 * @uml.property name="label"
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @uml.property name="label"
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @uml.property name="type"
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @uml.property name="type"
	 */
	public void setType(int type) {
		this.type = type;
	}

	
	
	private Feature wrapGcInFeature(GridCoverage gridCoverage)
	throws IllegalAttributeException, SchemaException {
		// create surrounding polygon
		PrecisionModel pm = new PrecisionModel();
		CoordinateSequenceFactory csf = DefaultCoordinateSequenceFactory.instance();
		GeometryFactory gf = new GeometryFactory(pm, 0);
		Coordinate[] coord = new Coordinate[5];
		Rectangle2D rect = ((GridCoverage2D) gridCoverage).getEnvelope2D().getBounds2D();
		CoordinateReferenceSystem sourceCrs = ((GridCoverage2D) gridCoverage).getEnvelope2D().getCoordinateReferenceSystem();
		coord[0] = new Coordinate(rect.getMinX(), rect.getMinY());
		coord[1] = new Coordinate(rect.getMaxX(), rect.getMinY());
		coord[2] = new Coordinate(rect.getMaxX(), rect.getMaxY());
		coord[3] = new Coordinate(rect.getMinX(), rect.getMaxY());
		coord[4] = new Coordinate(rect.getMinX(), rect.getMinY());
		
		LinearRing ring = new LinearRing(csf.create(coord), gf);
		Polygon bounds = new Polygon(ring, null, gf);
		
		// create the feature type
 		GeometricAttributeType geom = new GeometricAttributeType("geom", Polygon.class, true, 1, 1, null, sourceCrs, null);
		AttributeType grid = AttributeTypeFactory.newAttributeType("grid", GridCoverage.class);
		
		DefaultFeatureType schema = null;
		AttributeType[] attTypes = {geom, grid};
		
		schema = (DefaultFeatureType) FeatureTypeBuilder.newFeatureType(attTypes, this.name);
		
		// create the feature
		Feature feature = new CoverageFeature(schema, new Object[] {bounds, gridCoverage}, this.name);
		feature.setDefaultGeometry(bounds);
		
		return feature;
	}
	
	
	private GridCoverage getGridCoverage(HttpServletRequest request, CoverageInfo meta) throws IOException {
		GridCoverage coverage;
		
		try {
			String formatID = meta.getFormatId();
			DataConfig dataConfig = (DataConfig) request
						.getSession()
						.getServletContext()
						.getAttribute(DataConfig.CONFIG_KEY);
			DataFormatConfig dfConfig = dataConfig.getDataFormat(formatID);

			String realPath = request.getRealPath("/");
			URL url = CoverageUtils.getResource(dfConfig.getUrl(), realPath);

//			GridCoverageExchange gce = new StreamGridCoverageExchange();
//			GridCoverageReader reader = gce.getReader(url);
//			Format format = reader.getFormat();

			Format format = dfConfig.getFactory();
			GridCoverageReader reader = ((AbstractGridFormat) format).getReader(url);

			ParameterValueGroup params = format.getReadParameters();
			
			if( params != null ) {
				List list=params.values();
				Iterator it=list.iterator();
				while(it.hasNext())
				{
					ParameterValue param=((ParameterValue)it.next());
					ParameterDescriptor descr=(ParameterDescriptor)param.getDescriptor();
					
					String key = descr.getName().toString();
					Object value = CoverageUtils.getCvParamValue(key, param, dfConfig.getParameters());
					
					if( value != null )
						params.parameter(key).setValue(value);
				}
			}
			
			coverage = /*(GridCoverage2D) reader.read(
					params != null ?
					(GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[params.values().size()])
					: null
					);*/
				reader.read(
						params != null ?
								(GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[params.values().size()])
								: null
				);
			if (coverage == null || !(coverage instanceof GridCoverage2D))
				throw new IOException(
				"The requested coverage could not be found.");
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
		}
		
		return coverage;
	}
	
	public FeatureSource getCoverageToFeatures(HttpServletRequest request)
	throws DataSourceException {
		FeatureCollection collection = FeatureCollections.newCollection();
		// last step, wrap, add to the feature collection and return
		try {
			GridCoverage gridCoverage = getGridCoverage(request, this.coverage);
			collection.add(wrapGcInFeature(gridCoverage));
		} catch (Exception e) {
			throw new DataSourceException("IO error", e);
		}

		final FeatureSource source = DataUtilities.source(collection);
		
		return source;
	}

	public GridCoverage getCoverageToLayer(HttpServletRequest request)
	throws DataSourceException {
		GridCoverage gridCoverage = null;
		try {
			gridCoverage = getGridCoverage(request, this.coverage);
		} catch (Exception e) {
			throw new DataSourceException("IO error", e);
		}

		return gridCoverage;
	}
	
	public Style getDefaultStyle() {
		if( this.type == TYPE_VECTOR )
			return this.feature.getDefaultStyle();
		else if( this.type == TYPE_RASTER )
			return this.coverage.getDefaultStyle();
		
		return null;
	}
	
	
	/************************************
	 * 
	 * Extension of the DefaultFeature.
	 *  
	 */
	private class CoverageFeature extends DefaultFeature {
		public CoverageFeature(DefaultFeatureType fType, Object[] attributes, String featureId) throws IllegalAttributeException {
			super(
					fType,
					attributes, 
					featureId);
		}
	}
}