/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.util.DataStoreUtils;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class MapLayerInfo extends GlobalLayerSupertype {
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
			try {
				return feature.getBoundingBox();
			} catch (IllegalArgumentException e) {
		        FeatureSource realSource = feature.getFeatureSource();

		        return DataStoreUtils.getBoundingBoxEnvelope(realSource);
			}
		} else {

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
		// handle InlineFeatureStuff
		try {
			this.name = feature.getName();
			this.label = feature.getTitle();
			this.description = feature.getAbstract();
			this.dirName = feature.getDirName();
		} catch (IllegalArgumentException e) {
			this.name = "";
			this.label = "";
			this.description = "";
			this.dirName = "";
		}
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

//	/**
//	 * 
//	 * @param request
//	 * @param meta
//	 * @param envelope
//	 * @param dim
//	 * @return
//	 * @throws IOException
//	 */
//	public GridCoverageReader getReader() throws IOException {
//
//		try {
//
//			// /////////////////////////////////////////////////////////
//			//
//			// Getting coverage config
//			//
//			// /////////////////////////////////////////////////////////
//			final String formatID = coverage.getFormatId();
//			final CoverageStoreInfo gcInfo = coverage.getData().getFormatInfo(
//					formatID);
//
//			// /////////////////////////////////////////////////////////
//			//
//			// Getting coverage reader using the format and the real path.
//			//
//			// /////////////////////////////////////////////////////////
////			final File obj = CoverageUtils.getResourceAsFile(gcInfo.getUrl(),
////					gcInfo.getBaseDir());
////
////			// XXX CACHING READERS HERE
////			return ((AbstractGridFormat) gcInfo.getFormat()).getReader(obj);
//			return gcInfo.getReader();
//
//		} catch (InvalidParameterValueException e) {
//			final IOException ex = new IOException(e.getLocalizedMessage());
//			ex.initCause(e);
//
//			throw ex;
//		} catch (ParameterNotFoundException e) {
//			final IOException ex = new IOException(e.getLocalizedMessage());
//			ex.initCause(e);
//
//			throw ex;
//			// } catch (MalformedURLException e) {
//			// final IOException ex = new IOException(e.getLocalizedMessage());
//			// ex.initCause(e);
//			//
//			//			throw ex;
//		} catch (IllegalArgumentException e) {
//			final IOException ex = new IOException(e.getLocalizedMessage());
//			ex.initCause(e);
//
//			throw ex;
//		} catch (SecurityException e) {
//			final IOException ex = new IOException(e.getLocalizedMessage());
//			ex.initCause(e);
//
//			throw ex;
//		}
//
//	}

	// /**
	// *
	// * @param request
	// * @param meta
	// * @param envelope
	// * @return
	// * @throws IOException
	// */
	// private GridCoverageReader getReader(HttpServletRequest request,
	// CoverageInfo meta) throws IOException {
	// try {
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // Getting coverage config
	// //
	// // /////////////////////////////////////////////////////////
	// final String formatID = meta.getFormatId();
	// final DataConfig dataConfig = (DataConfig) request.getSession()
	// .getServletContext().getAttribute(DataConfig.CONFIG_KEY);
	// final CoverageStoreConfig dfConfig = dataConfig
	// .getDataFormat(formatID);
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // Getting coverage reader using the format and the real path.
	// //
	// // /////////////////////////////////////////////////////////
	// final String realPath = request.getRealPath("/");
	// final URL url = CoverageUtils.getResource(dfConfig.getUrl(),
	// realPath);
	// // XXX CACHING
	// final Format format = dfConfig.getFactory();
	// return ((AbstractGridFormat) format).getReader(url);
	//
	// } catch (InvalidParameterValueException e) {
	// final IOException ex = new IOException(e.getLocalizedMessage());
	// ex.initCause(e);
	//
	// throw ex;
	// } catch (ParameterNotFoundException e) {
	// final IOException ex = new IOException(e.getLocalizedMessage());
	// ex.initCause(e);
	//
	// throw ex;
	// } catch (MalformedURLException e) {
	// final IOException ex = new IOException(e.getLocalizedMessage());
	// ex.initCause(e);
	//
	// throw ex;
	// } catch (IllegalArgumentException e) {
	// final IOException ex = new IOException(e.getLocalizedMessage());
	// ex.initCause(e);
	//
	// throw ex;
	// } catch (SecurityException e) {
	// final IOException ex = new IOException(e.getLocalizedMessage());
	// ex.initCause(e);
	//
	// throw ex;
	// }
	//
	// }

	// /**
	// * Getting a gridcoverage using the information provided in this layer
	// info
	// * object with the specified dimensions.
	// *
	// * @param request
	// * @param envelope
	// * @param dim
	// * @return
	// * @throws DataSourceException
	// */
	// public GridCoverage getCoverage(HttpServletRequest request,
	// GeneralEnvelope envelope, Rectangle dim) throws DataSourceException {
	//
	// GridCoverage gc = null;
	// try {
	// if (envelope == null)
	// envelope = this.coverage.getEnvelope();
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // Do we need to proceed?
	// // I need to check the requested envelope in order to see if the
	// // coverage we ask intersect it otherwise it is pointless to load it
	// // since its reader might return null;
	// // /////////////////////////////////////////////////////////
	// final CoordinateReferenceSystem sourceCRS = envelope
	// .getCoordinateReferenceSystem();
	// final CoordinateReferenceSystem destCRS = coverage.getCrs();
	// if (!CRSUtilities.equalsIgnoreMetadata(sourceCRS, destCRS)) {
	// // get a math transform
	// final MathTransform transform = StreamingRenderer
	// .getMathTransform(sourceCRS, destCRS);
	//
	// // transform the envelope
	// if (!transform.isIdentity())
	// envelope = CRSUtilities.transform(transform, envelope);
	//
	// }
	//
	// // just do the intersection since
	// envelope.intersect(coverage.getEnvelope());
	// if (envelope.isEmpty())
	// return null;
	// envelope.setCoordinateReferenceSystem(destCRS);
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // get a reader
	// //
	// // /////////////////////////////////////////////////////////
	// final GridCoverageReader reader = coverage.getReader();
	// if (reader == null)
	// return null;
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // Setting coverage reading params.
	// //
	// // /////////////////////////////////////////////////////////
	// List parameters = new ArrayList();
	// final ParameterValueGroup params = reader.getFormat()
	// .getReadParameters();
	// final String readGeometryKey = AbstractGridFormat.READ_GRIDGEOMETRY2D
	// .getName().toString();
	// if (params != null) {
	// List list = params.values();
	// Iterator it = list.iterator();
	// ParameterValue param;
	// ParameterDescriptor descr;
	// String key;
	// Object value;
	// while (it.hasNext()) {
	// param = ((ParameterValue) it.next());
	// descr = (ParameterDescriptor) param.getDescriptor();
	//
	// key = descr.getName().toString();
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // request param for better management of coverage
	// //
	// // /////////////////////////////////////////////////////////
	// if (key.equalsIgnoreCase(readGeometryKey)
	// && envelope != null) {
	// /* params.parameter(key).setValue(envelope); */
	// continue;
	// } else {
	// // /////////////////////////////////////////////////////////
	// //
	// // format specific params
	// //
	// // /////////////////////////////////////////////////////////
	// value = CoverageUtils.getCvParamValue(key, param,
	// coverage.getParameters());
	//
	// if (value != null)
	// /* params.parameter(key).setValue(value); */
	// parameters.add(new DefaultParameterDescriptor(key,
	// value.getClass(), null, value));
	// }
	// }
	// }
	//
	// // /////////////////////////////////////////////////////////
	// //
	// // Reading the coverage
	// //
	// // /////////////////////////////////////////////////////////
	// gc = reader
	// .read(!parameters.isEmpty() ? (GeneralParameterValue[]) parameters
	// .toArray(new GeneralParameterValue[parameters
	// .size()])
	// : null);
	//
	// if (gc == null || !(gc instanceof GridCoverage2D))
	// throw new IOException(
	// "The requested coverage could not be found.");
	// } catch (InvalidParameterValueException e) {
	// final DataSourceException ex = new DataSourceException(
	// "IO error when getting a grid coverage", e);
	//
	// throw ex;
	//
	// } catch (ParameterNotFoundException e) {
	// final DataSourceException ex = new DataSourceException(
	// "IO error when getting a grid coverage", e);
	//
	// throw ex;
	//
	// } catch (MalformedURLException e) {
	// final DataSourceException ex = new DataSourceException(
	// "IO error when getting a grid coverage", e);
	//
	// throw ex;
	//
	// } catch (IllegalArgumentException e) {
	// final DataSourceException ex = new DataSourceException(
	// "IO error when getting a grid coverage", e);
	//
	// throw ex;
	//
	// } catch (SecurityException e) {
	// final DataSourceException ex = new DataSourceException(
	// "IO error when getting a grid coverage", e);
	//
	// throw ex;
	//
	// } catch (IOException e) {
	// final DataSourceException ex = new DataSourceException(
	// "IO error when getting a grid coverage", e);
	//
	// throw ex;
	//
	// } catch (TransformException e) {
	// final DataSourceException ex = new DataSourceException(
	//					"IO error when getting a grid coverage", e);
	//
	//			throw ex;
	//
	//		}
	//		return gc;
	//
	//	}

	public Style getDefaultStyle() {
		if (this.type == TYPE_VECTOR)
			return this.feature.getDefaultStyle();
		else if (this.type == TYPE_RASTER)
			return this.coverage.getDefaultStyle();

		return null;
	}

}