/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataSourceException;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.feature.DefaultFeature;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
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
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.util.CoverageUtils;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
	 */
	Object toDTO() {
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
	 * @throws IOException
	 *             when an error occurs
	 */
	public Envelope getBoundingBox() throws IOException {
		if (this.type == TYPE_VECTOR) {
			return feature.getBoundingBox();
		} else {
			// GeneralEnvelope bounds = null;
			// try {
			// bounds =
			// CoverageStoreUtils.adjustEnvelopeLongitudeFirst(coverage
			// .getEnvelope().getCoordinateReferenceSystem(), coverage
			// .getEnvelope());
			// } catch (MismatchedDimensionException e) {
			// final IOException ex = new IOException(new StringBuffer(
			// "Problems getting Coverage BoundingBox: ").append(
			// e.getLocalizedMessage()).toString());
			// ex.initCause(e);
			// throw ex;
			// } catch (IndexOutOfBoundsException e) {
			// final IOException ex = new IOException(new StringBuffer(
			// "Problems getting Coverage BoundingBox: ").append(
			// e.getLocalizedMessage()).toString());
			// ex.initCause(e);
			// throw ex;
			// } catch (NoSuchAuthorityCodeException e) {
			// final IOException ex = new IOException(new StringBuffer(
			// "Problems getting Coverage BoundingBox: ").append(
			// e.getLocalizedMessage()).toString());
			// ex.initCause(e);
			// throw ex;
			// }

			// using referenced envelope (experiment)
			return new ReferencedEnvelope(coverage.getEnvelope(), coverage
					.getCrs());
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

	/**
	 * Getting a grid coverage as requested.
	 * 
	 * @param request
	 * @param meta
	 * @param envelope
	 * @param dim
	 * @return
	 * @throws IOException
	 */
	private final GridCoverage getGridCoverage(HttpServletRequest request,
			CoverageInfo meta, GeneralEnvelope envelope, Rectangle dim)
			throws IOException {
		GridCoverage coverage;

		try {
			// /////////////////////////////////////////////////////////
			//
			// Getting coverage config
			//
			// /////////////////////////////////////////////////////////
			final String formatID = meta.getFormatId();
			final DataConfig dataConfig = (DataConfig) request.getSession()
					.getServletContext().getAttribute(DataConfig.CONFIG_KEY);
			final CoverageStoreConfig dfConfig = dataConfig
					.getDataFormat(formatID);

			// /////////////////////////////////////////////////////////
			//
			// Getting coverage reader using the format and the real path.
			//
			// /////////////////////////////////////////////////////////
			final String realPath = request.getRealPath("/");
			final URL url = CoverageUtils.getResource(dfConfig.getUrl(),
					realPath);
			final Format format = dfConfig.getFactory();
			final GridCoverageReader reader = ((AbstractGridFormat) format)
					.getReader(url);

			// /////////////////////////////////////////////////////////
			//
			// Setting coverage reading params.
			//
			// /////////////////////////////////////////////////////////
			final ParameterValueGroup params = format.getReadParameters();
			final String readEnvelopeKey = AbstractGridFormat.READ_ENVELOPE
					.getName().toString();
			final String requestedDimKey = AbstractGridFormat.READ_DIMENSIONS2D
					.getName().toString();
			if (params != null) {
				List list = params.values();
				Iterator it = list.iterator();
				ParameterValue param;
				ParameterDescriptor descr;
				String key;
				Object value;
				while (it.hasNext()) {
					param = ((ParameterValue) it.next());
					descr = (ParameterDescriptor) param.getDescriptor();

					key = descr.getName().toString();

					// /////////////////////////////////////////////////////////
					//
					// request param for better management of coverage
					//
					// /////////////////////////////////////////////////////////
					if (key.equalsIgnoreCase(readEnvelopeKey)
							&& envelope != null) {
						params.parameter(key).setValue(envelope);

					} else if (key.equalsIgnoreCase(requestedDimKey)
							&& dim != null) {
						params.parameter(key).setValue(dim);

					} else {
						// /////////////////////////////////////////////////////////
						//
						// format specific params
						//
						// /////////////////////////////////////////////////////////
						value = CoverageUtils.getCvParamValue(key, param, meta
								.getParameters());

						if (value != null)
							params.parameter(key).setValue(value);
					}
				}
			}

			// /////////////////////////////////////////////////////////
			//
			// Reading the coverage
			//
			// /////////////////////////////////////////////////////////
			coverage = reader
					.read(params != null ? (GeneralParameterValue[]) params
							.values().toArray(
									new GeneralParameterValue[params.values()
											.size()]) : null);
			if (coverage == null || !(coverage instanceof GridCoverage2D))
				throw new IOException(
						"The requested coverage could not be found.");
		} catch (InvalidParameterValueException e) {
			final IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);

			throw ex;
		} catch (ParameterNotFoundException e) {
			final IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);

			throw ex;
		} catch (MalformedURLException e) {
			final IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);

			throw ex;
		} catch (IllegalArgumentException e) {
			final IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);

			throw ex;
		} catch (SecurityException e) {
			final IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);

			throw ex;
		} catch (IOException e) {
			final IOException ex = new IOException(e.getLocalizedMessage());
			ex.initCause(e);

			throw ex;
		}

		return coverage;
	}

	public final GridCoverage getCoverage(HttpServletRequest request,
			GeneralEnvelope envelope, Rectangle dim) throws DataSourceException {
		try {

			return getGridCoverage(request, this.coverage, envelope, dim);

		} catch (IOException e) {
			final DataSourceException ex = new DataSourceException(
					"IO error when getting a grid coverage", e);

			throw ex;

		}

	}

	public Style getDefaultStyle() {
		if (this.type == TYPE_VECTOR)
			return this.feature.getDefaultStyle();
		else if (this.type == TYPE_RASTER)
			return this.coverage.getDefaultStyle();

		return null;
	}

}