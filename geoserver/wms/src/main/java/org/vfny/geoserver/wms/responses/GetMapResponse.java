/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.platform.GeoServerExtensions;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridCoverageNDReader;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureTypeBuilder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.type.GeometricAttributeType;
import org.geotools.map.DefaultMapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.CRSUtilities;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.CoverageDimension;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.CoverageUtils;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import org.vfny.geoserver.wms.RasterMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.map.metatile.MetatileMapProducer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * A GetMapResponse object is responsible of generating a map based on a GetMap
 * request. The way the map is generated is independent of this class, wich will
 * use a delegate object based on the output format requested
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Simone Giannecchini - GeoSolutions SAS
 * @version $Id$
 */
public class GetMapResponse implements Response {
	/** DOCUMENT ME! */
	private static final Logger LOGGER = Logger.getLogger(GetMapResponse.class
			.getPackage().getName());

	/**
	 * The map producer that will be used for the production of a map in the
	 * requested format.
	 */
	private GetMapProducer delegate;

	/**
	 * The map context
	 */
	private WMSMapContext map;

	/**
	 * WMS module
	 */
	private WMS wms;

	/**
	 * custom response headers
	 */
	private HashMap responseHeaders;

	String headerContentDisposition;

	private ApplicationContext applicationContext;

	/**
	 * Creates a new GetMapResponse object.
	 * 
	 * @param applicationContext
	 */
	public GetMapResponse(WMS wms, ApplicationContext applicationContext) {
		this.wms = wms;
		this.applicationContext = applicationContext;
		responseHeaders = new HashMap(10);
	}

	/**
	 * Returns any extra headers that this service might want to set in the HTTP
	 * response object.
	 */
	public HashMap getResponseHeaders() {
		return responseHeaders;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param req
	 *            DOCUMENT ME!
	 * 
	 * @throws ServiceException
	 *             DOCUMENT ME!
	 * @throws WmsException
	 *             DOCUMENT ME!
	 */
	public void execute(Request req) throws ServiceException {
		GetMapRequest request = (GetMapRequest) req;

		final String outputFormat = request.getFormat();

		this.delegate = getDelegate(outputFormat, wms);
		// JD:make instance variable in order to release resources later
		// final WMSMapContext map = new WMSMapContext();
		map = new WMSMapContext(request);
		this.delegate.setMapContext(map);

		// enable on the fly meta tiling if request looks like a tiled one
		if (MetatileMapProducer.isRequestTiled(request, delegate)) {
			if (LOGGER.isLoggable(Level.FINER)) {
				LOGGER
						.finer("Tiled request detected, activating on the fly meta tiler");
			}

			this.delegate = new MetatileMapProducer(request,
					(RasterMapProducer) delegate);
			this.delegate.setMapContext(map);
		}

		final MapLayerInfo[] layers = request.getLayers();
		final Style[] styles = (Style[]) request.getStyles().toArray(
				new Style[] {});
		final Filter[] filters = ((request.getFilter() != null) ? (Filter[]) request
				.getFilter().toArray(new Filter[] {})
				: null);



		// DJB: the WMS spec says that the request must not be 0 area
		// if it is, throw a service exception!
		final Envelope env = request.getBbox();
		if (env == null) {
		    throw new WmsException("GetMap requests must include a BBOX parameter.");
		}
		if (env.isNull() || (env.getWidth() <= 0) || (env.getHeight() <= 0)) {
			throw new WmsException(new StringBuffer(
					"The request bounding box has zero area: ").append(env)
					.toString());
		}

		// DJB DONE: replace by setAreaOfInterest(Envelope,
		// CoordinateReferenceSystem)
		// with the user supplied SRS parameter

		// if there's a crs in the request, use that. If not, assume its 4326
		final CoordinateReferenceSystem mapcrs = request.getCrs();

		// DJB: added this to be nicer about the "NONE" srs.
		if (mapcrs != null) {
			map.setAreaOfInterest(env, mapcrs);
		} else {
			map.setAreaOfInterest(env, DefaultGeographicCRS.WGS84);
		}

		map.setMapWidth(request.getWidth());
		map.setMapHeight(request.getHeight());
		map.setBgColor(request.getBgColor());
		map.setTransparent(request.isTransparent());
		map.setBuffer(request.getBuffer());
		map.setPaletteInverter(request.getPalette());

		// //
		//
		// Check to see if we really have something to display. Sometimes width
		// or height or both are non positivie or the requested area is null.
		//
		// ///
		if ((request.getWidth() <= 0) || (request.getHeight() <= 0)
				|| (map.getAreaOfInterest().getLength(0) <= 0)
				|| (map.getAreaOfInterest().getLength(1) <= 0)) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER
						.fine("We are not going to render anything because either the are is null ar the dimensions are not positive.");
			}

			return;
		}

		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine("setting up map");
		}

		try { // mapcontext can leak memory -- we make sure we done (see
			// finally block)


			// track the external caching strategy for any map layers
			boolean cachingPossible = request.getHttpServletRequest()
					.getMethod().equals("GET");
			String featureVersion = request.getFeatureVersion();
			int maxAge = Integer.MAX_VALUE;

			final int length = layers.length;

			for (int i = 0; i < length; i++) {
				final Style style = styles[i];
				Filter optionalFilter;
				try {
					optionalFilter = filters[i];
				} catch (Exception e) {
					optionalFilter = null;
				}

				final DefaultMapLayer layer;
				if(layers[i].getType() == MapLayerInfo.TYPE_REMOTE_VECTOR) {
				    cachingPossible = false;
				    
				    final FeatureSource source = layers[i].getRemoteFeatureSource();
                    layer = new DefaultMapLayer(source, style);
                    layer.setTitle(layers[i].getName());
                    
                    final DefaultQuery definitionQuery;
                    if (optionalFilter != null) {
                        definitionQuery = new DefaultQuery(source.getSchema()
                                .getTypeName(), optionalFilter);
                        definitionQuery.setVersion(featureVersion);

                        layer.setQuery(definitionQuery);
                    } else if (featureVersion != null) {
                        definitionQuery = new DefaultQuery(source.getSchema()
                                .getTypeName());
                        definitionQuery.setVersion(featureVersion);

                        layer.setQuery(definitionQuery);
                    }

                    map.addLayer(layer);
				} else if (layers[i].getType() == MapLayerInfo.TYPE_VECTOR) {
					if (cachingPossible) {
						if (layers[i].getFeature().isCachingEnabled()) {
							int nma = Integer.parseInt(layers[i].getFeature()
									.getCacheMaxAge());

							// suppose the map contains multiple cachable
							// layers...we can only cache the combined map for
							// the
							// time specified by the shortest-cached layer.
							if (nma < maxAge) {
								maxAge = nma;
							}
						} else {
							// if one layer isn't cachable, then we can't cache
							// any of them. Disable caching.
							cachingPossible = false;
						}
					}

					final FeatureSource source;
					// /////////////////////////////////////////////////////////
					//
					// Adding a feature layer
					//
					// /////////////////////////////////////////////////////////
					try {
						source = layers[i].getFeature().getFeatureSource(true);

						// NOTE for the feature. Here there was some code that
						// sounded like:
						// * get the bounding box from feature source
						// * eventually reproject it to the actual CRS used for
						// map
						// * if no intersection, don't bother adding the feature
						// source to the map
						// This is not an optimization, on the contrary,
						// computing the bbox may be
						// very expensive depending on the data size. Using
						// sigma.openplans.org data
						// and a tiled client like OpenLayers, it dragged the
						// server to his knees
						// and the client simply timed out
					} catch (IOException exp) {
						if (LOGGER.isLoggable(Level.SEVERE)) {
							LOGGER.log(Level.SEVERE, new StringBuffer(
									"Getting feature source: ").append(
									exp.getMessage()).toString(), exp);
						}

						throw new WmsException(null, new StringBuffer(
								"Internal error : ").append(exp.getMessage())
								.toString());
					}

					layer = new DefaultMapLayer(source, style);
					layer.setTitle(layers[i].getName());

					final Filter definitionFilter = layers[i].getFeature()
							.getDefinitionQuery();
					final DefaultQuery definitionQuery;
					if (definitionFilter != null) {
						definitionQuery = new DefaultQuery(source.getSchema()
								.getTypeName(), definitionFilter);
						definitionQuery.setVersion(featureVersion);

						layer.setQuery(definitionQuery);
					} else if (optionalFilter != null) {
						definitionQuery = new DefaultQuery(source.getSchema()
								.getTypeName(), optionalFilter);
						definitionQuery.setVersion(featureVersion);

						layer.setQuery(definitionQuery);
					} else if (featureVersion != null) {
						definitionQuery = new DefaultQuery(source.getSchema()
								.getTypeName());
						definitionQuery.setVersion(featureVersion);

						layer.setQuery(definitionQuery);
					}

					map.addLayer(layer);
				} else if (layers[i].getType() == MapLayerInfo.TYPE_RASTER) {
					// /////////////////////////////////////////////////////////
					//
					// Adding a coverage layer
					//
					// /////////////////////////////////////////////////////////
					GridCoverageReader reader = layers[i].getCoverage().getReader();
					if (reader != null) {
						// /////////////////////////////////////////////////////////
						//
						// Setting coverage reading params.
						//
						// /////////////////////////////////////////////////////////

                        /*
                         * Test if the parameter "TIME" is present in the WMS
                         * request, and by the way in the reading parameters. If
                         * it is the case, one can adds it to the request. If an
                         * exception is thrown, we have nothing to do.
                         */
                        try {
                        	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            ParameterValue time = reader.getFormat().getReadParameters().parameter(
                                    "Times");
                            if (time != null && request.getTime() != null) {
                            	List times = request.getTime();
                            	Object[] timePositions = new Object[times.size()];
                            	for (int t=0; t<times.size(); t++)
                            		//timePositions[t] = sdf.format(times.get(t));
                            		timePositions[t] = times.get(t);
                                time.setValue(timePositions);
                                layers[i]
			                              .getCoverage()
			                              .getParameters().put("Times", timePositions);
                            } else {
                                layers[i]
			                              .getCoverage()
			                              .getParameters().put("Times", null);
                            }
                        } catch (ParameterNotFoundException p) {
                        }

                        // //
                        //
                        // ???
                        //
                        // //
                        try {
                            ParameterValue dimRange = reader.getFormat().getReadParameters()
                                    .parameter("DIM_RANGE");
                            if (dimRange != null && request.getDimRange() != null) {
                                dimRange.setValue(request.getDimRange());
                            }
                        } catch (ParameterNotFoundException p) {
                        }

                        // //
                        //
                        // Parsing Bands and putting values into the Coverage read parameters
                        //
                        // //
                        try {
                            ParameterValue bands = reader.getFormat().getReadParameters()
                                    .parameter("Bands");
                            
                            if (bands != null) {
                                final CoverageDimension[] dimensions = layers[i].getCoverage().getDimensions();
                                final List<String> requestedBands = new ArrayList<String>();
                                for (CoverageDimension coverageDimension : dimensions) {
                                	final String bandName = coverageDimension.getName();
									if (request.getRawKvp().containsKey(bandName)) {
										requestedBands.add(bandName);
									}
								}
                                layers[i]
			                              .getCoverage()
			                              .getParameters().put("Bands", requestedBands.toArray(new String[1]));
                            }
                        } catch (ParameterNotFoundException p) {
                        }

                        // //
                        //
                        // Parsing Elevations and putting values into the Coverage read parameters
                        //
                        // //
                        try {
                            ParameterValue elevation = reader.getFormat().getReadParameters()
                                    .parameter("Elevations");
                            if (elevation != null && request.getElevation() != null) {
                            	String[] elevations = request.getElevation().split(",");
                            	int numZ = 0;
                            	for (String zeta : elevations) {
									elevations[numZ++] = zeta.trim();
								}
                                elevation.setValue(elevations);
                                layers[i]
			                              .getCoverage()
			                              .getParameters().put("Elevations", elevations);
                            } else {
                            	layers[i]
			                              .getCoverage()
			                              .getParameters().put("Elevations", null);
                            }
                        } catch (ParameterNotFoundException p) {
                        }
                        
                        // //
                        //
                        // Finally building up WMS layer by wrapping the Coverage Reader into a FeatureCollection
                        // along with all the Coverage read parameters collected until now.
                        //
                        // //
						try {
							final ParameterValueGroup params = (ParameterValueGroup) reader.getFormat().getReadParameters().clone();
							
							if (reader instanceof AbstractGridCoverage2DReader) {
								layer = new DefaultMapLayer(
										wrapGridCoverageReader((AbstractGridCoverage2DReader)reader, CoverageUtils
												.getParameters(params, layers[i]
												                              .getCoverage()
												                              .getParameters())), style);

								layer.setTitle(layers[i].getName());
								layer.setQuery(Query.ALL);
								map.addLayer(layer);
							} else if (reader instanceof AbstractGridCoverageNDReader) {
								final String coverageName = layers[i].getCoverage().getRealName();
								
		                        // //
		                        //
		                        // Real coverage name has to be present into the ND-Reader read params also
		                        //
		                        // //
								layers[i]
			                              .getCoverage()
			                              .getParameters().put("Coverage", coverageName);
								
								layer = new DefaultMapLayer(
										wrapGridCoverageReader((AbstractGridCoverageNDReader)reader,
												layers[i].getCoverage().getRealName(), CoverageUtils
												.getParameters(params, layers[i]
												                              .getCoverage()
												                              .getParameters())), style);

								layer.setTitle(layers[i].getName());
								layer.setQuery(Query.ALL);
								map.addLayer(layer);
							}

						} catch (IllegalArgumentException e) {
							if (LOGGER.isLoggable(Level.SEVERE)) {
								LOGGER.log(Level.SEVERE, new StringBuffer(
										"Wrapping GC in feature source: ")
										.append(e.getLocalizedMessage())
										.toString(), e);
							}

							throw new WmsException(
									null,
									new StringBuffer(
											"Internal error : unable to get reader for this coverage layer ")
											.append(layers[i].toString())
											.toString());
						}
					} else {
						throw new WmsException(
								null,
								new StringBuffer(
										"Internal error : unable to get reader for this coverage layer ")
										.append(layers[i].toString())
										.toString());
					}
				}
			}

			// /////////////////////////////////////////////////////////
			//
			// Producing the map in the requested format.
			//
			// /////////////////////////////////////////////////////////
			this.delegate.produceMap();

			if (cachingPossible) {
				responseHeaders.put("Cache-Control", "max-age=" + maxAge
						+ ", must-revalidate");
			}

			final String contentDisposition = this.delegate
					.getContentDisposition();
			if (contentDisposition != null) {
				this.headerContentDisposition = contentDisposition;
			}
		} catch (ClassCastException e) {
			if (LOGGER.isLoggable(Level.WARNING)) {
				LOGGER.log(Level.SEVERE, new StringBuffer(
						"Getting feature source: ").append(e.getMessage())
						.toString(), e);
			}

			throw new WmsException(e, new StringBuffer("Internal error : ")
					.append(e.getMessage()).toString(), "");
		} catch (TransformException e) {
			throw new WmsException(e, new StringBuffer("Internal error : ")
					.append(e.getMessage()).toString(), "");
		} catch (FactoryConfigurationError e) {
			throw new WmsException(e, new StringBuffer("Internal error : ")
					.append(e.getMessage()).toString(), "");
		} catch (SchemaException e) {
			throw new WmsException(e, new StringBuffer("Internal error : ")
					.append(e.getMessage()).toString(), "");
		} catch (IllegalAttributeException e) {
			throw new WmsException(e, new StringBuffer("Internal error : ")
					.append(e.getMessage()).toString(), "");
		} finally {
			// clean
			try {
				// map.clearLayerList();
			} catch (Exception e) // we dont want to propogate a new error
			{
				if (LOGGER.isLoggable(Level.SEVERE)) {
					LOGGER.log(Level.SEVERE, new StringBuffer(
							"Getting feature source: ").append(e.getMessage())
							.toString(), e);
				}
			}
		}
	}

	/**
	 * asks the internal GetMapDelegate for the MIME type of the map that it
	 * will generate or is ready to, and returns it
	 * 
	 * @param gs
	 *            DOCUMENT ME!
	 * 
	 * @return the MIME type of the map generated or ready to generate
	 * 
	 * @throws IllegalStateException
	 *             if a GetMapDelegate is not setted yet
	 */
	public String getContentType(GeoServer gs) throws IllegalStateException {
		if (this.delegate == null) {
			throw new IllegalStateException("No request has been processed");
		}

		return this.delegate.getContentType();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getContentEncoding() {
		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.finer("returning content encoding null");
		}

		return null;
	}

	/**
	 * if a GetMapDelegate is set, calls it's abort method. Elsewere do nothing.
	 * 
	 * @param gs
	 *            DOCUMENT ME!
	 */
	public void abort(Service gs) {
		if (this.delegate != null) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("asking delegate for aborting the process");
			}

			this.delegate.abort();
		}
	}

	/**
	 * delegates the writing and encoding of the results of the request to the
	 * <code>GetMapDelegate</code> wich is actually processing it, and has
	 * been obtained when <code>execute(Request)</code> was called
	 * 
	 * @param out
	 *            the output to where the map must be written
	 * 
	 * @throws ServiceException
	 *             if the delegate throws a ServiceException inside its
	 *             <code>writeTo(OuptutStream)</code>, mostly due to
	 * @throws IOException
	 *             if the delegate throws an IOException inside its
	 *             <code>writeTo(OuptutStream)</code>, mostly due to
	 * @throws IllegalStateException
	 *             if this method is called before <code>execute(Request)</code>
	 *             has succeed
	 */
	public void writeTo(OutputStream out) throws ServiceException, IOException {
		try { // mapcontext can leak memory -- we make sure we done (see
			// finally block)

			if (this.delegate == null) {
				throw new IllegalStateException(
						"No GetMapDelegate is setted, make sure you have called execute and it has succeed");
			}

			if (LOGGER.isLoggable(Level.FINER)) {
				LOGGER.finer(new StringBuffer("asking delegate for write to ")
						.append(out).toString());
			}

			this.delegate.writeTo(out);
		} finally {
			try {
				map.clearLayerList();
			} catch (Exception e) // we dont want to propogate a new error
			{
				if (LOGGER.isLoggable(Level.SEVERE)) {
					LOGGER.log(Level.SEVERE, new StringBuffer(
							"Getting feature source: ").append(e.getMessage())
							.toString(), e);
				}
			}
		}
	}

	/**
	 * Creates a GetMapDelegate specialized in generating the requested map
	 * format
	 * 
	 * @param outputFormat
	 *            a request parameter object wich holds the processed request
	 *            objects, such as layers, bbox, outpu format, etc.
	 * 
	 * @return A specialization of <code>GetMapDelegate</code> wich can
	 *         produce the requested output map format
	 * 
	 * @throws WmsException
	 *             if no specialization is configured for the output format
	 *             specified in <code>request</code> or if it can't be
	 *             instantiated
	 */
	private GetMapProducer getDelegate(String outputFormat, WMS wms)
			throws WmsException {
		final Collection producers = GeoServerExtensions.extensions(GetMapProducerFactorySpi.class);	

		for (Iterator iter = producers.iterator(); iter.hasNext();) {
			final GetMapProducerFactorySpi factory = (GetMapProducerFactorySpi) iter.next();

			if (factory.canProduce(outputFormat)) {
				return factory.createMapProducer(outputFormat, wms);
			}
		}

		WmsException e = new WmsException(
				"There is no support for creating maps in " + outputFormat
						+ " format");
		e.setCode("InvalidFormat");
		throw e;
	}

	/**
	 * Convenient mehtod to inspect the available
	 * <code>GetMapProducerFactorySpi</code> and return the set of all the map
	 * formats' MIME types that the producers can handle
	 * 
	 * @return a Set&lt;String&gt; with the supported mime types.
	 */
	public Set getMapFormats() {
		Set wmsGetMapFormats = loadImageFormats(applicationContext);

		return wmsGetMapFormats;
	}

	/**
	 * Convenience method for processing the GetMapProducerFactorySpi extension
	 * point and returning the set of available image formats.
	 * 
	 * @param applicationContext
	 *            The application context.
	 * 
	 */
	public static Set loadImageFormats(ApplicationContext applicationContext) {
		final Collection producers = GeoServerExtensions.extensions(GetMapProducerFactorySpi.class);
		final Set formats = new HashSet();

		for (Iterator iter = producers.iterator(); iter.hasNext();) {
			final GetMapProducerFactorySpi producer = (GetMapProducerFactorySpi) iter
					.next();
			formats.addAll(producer.getSupportedFormats());
		}

		return Collections.unmodifiableSet(formats);
	}

	public String getContentDisposition() {
		return headerContentDisposition;
	}
	
	// ////////////////////////////////////////////////////////////////////////
	//
	// TEMPORARY TO BE MOVED ON gt2-FeatureUtilities
	//
	// ////////////////////////////////////////////////////////////////////////
	/**
     * Wraps a grid coverage into a Feature. Code lifted from ArcGridDataSource
     * (temporary).
     *
     * @param  reader the grid coverage reader.
     * @return a feature with the grid coverage envelope as the geometry and the
     *         grid coverage itself in the "grid" attribute.
     */
    public static FeatureCollection wrapGridCoverageReader(final AbstractGridCoverage2DReader gridCoverageReader,
			GeneralParameterValue[] params) throws TransformException,
			FactoryConfigurationError, SchemaException,
			IllegalAttributeException {

		// create surrounding polygon
		final PrecisionModel pm = new PrecisionModel();
		final GeometryFactory gf = new GeometryFactory(pm, 0);
		final Rectangle2D rect = gridCoverageReader.getOriginalEnvelope()
				.toRectangle2D();
		final CoordinateReferenceSystem sourceCrs = CRSUtilities
				.getCRS2D(gridCoverageReader.getCrs());

		final Coordinate[] coord = new Coordinate[5];
		coord[0] = new Coordinate(rect.getMinX(), rect.getMinY());
		coord[1] = new Coordinate(rect.getMaxX(), rect.getMinY());
		coord[2] = new Coordinate(rect.getMaxX(), rect.getMaxY());
		coord[3] = new Coordinate(rect.getMinX(), rect.getMaxY());
		coord[4] = new Coordinate(rect.getMinX(), rect.getMinY());

		// }
		final LinearRing ring = gf.createLinearRing(coord);
		final Polygon bounds = new Polygon(ring, null, gf);

		// create the feature type
		final GeometricAttributeType geom = new GeometricAttributeType("geom",
				Polygon.class, true, 1, 1, null, sourceCrs, null);
		final AttributeType grid = AttributeTypeFactory.newAttributeType(
				"grid", AbstractGridCoverage2DReader.class);
		final AttributeType paramsAttr = AttributeTypeFactory.newAttributeType(
				"params", GeneralParameterValue[].class);

		final AttributeType[] attTypes = { geom, grid, paramsAttr };
		// Fix the schema name
		final String typeName = "GridCoverage";
		final DefaultFeatureType schema = (DefaultFeatureType) FeatureTypeBuilder
				.newFeatureType(attTypes, typeName);

		// create the feature
		Feature feature = schema.create(new Object[] { bounds,
				gridCoverageReader,params });

		final FeatureCollection collection = FeatureCollections.newCollection();
		collection.add(feature);

		return collection;
	}

    public static FeatureCollection wrapGridCoverageReader(final AbstractGridCoverageNDReader gridCoverageReader,
    		String coverageName, GeneralParameterValue[] params) throws TransformException,
			FactoryConfigurationError, SchemaException,
			IllegalAttributeException {

		// create surrounding polygon
		final PrecisionModel pm = new PrecisionModel();
		final GeometryFactory gf = new GeometryFactory(pm, 0);
		final Rectangle2D rect = gridCoverageReader.getOriginalEnvelope(coverageName)
				.toRectangle2D();
		final CoordinateReferenceSystem sourceCrs = CRSUtilities
				.getCRS2D(gridCoverageReader.getCrs(coverageName));

		final Coordinate[] coord = new Coordinate[5];
		coord[0] = new Coordinate(rect.getMinX(), rect.getMinY());
		coord[1] = new Coordinate(rect.getMaxX(), rect.getMinY());
		coord[2] = new Coordinate(rect.getMaxX(), rect.getMaxY());
		coord[3] = new Coordinate(rect.getMinX(), rect.getMaxY());
		coord[4] = new Coordinate(rect.getMinX(), rect.getMinY());

		// }
		final LinearRing ring = gf.createLinearRing(coord);
		final Polygon bounds = new Polygon(ring, null, gf);

		// create the feature type
		final GeometricAttributeType geom = new GeometricAttributeType("geom",
				Polygon.class, true, 1, 1, null, sourceCrs, null);
		final AttributeType grid = AttributeTypeFactory.newAttributeType(
				"grid", AbstractGridCoverageNDReader.class);
		final AttributeType paramsAttr = AttributeTypeFactory.newAttributeType(
				"params", GeneralParameterValue[].class);

		final AttributeType[] attTypes = { geom, grid, paramsAttr };
		// Fix the schema name
		final String typeName = "GridCoverage";
		final DefaultFeatureType schema = (DefaultFeatureType) FeatureTypeBuilder
				.newFeatureType(attTypes, typeName);

		// create the feature
		Feature feature = schema.create(new Object[] { bounds,
				gridCoverageReader,params });

		final FeatureCollection collection = FeatureCollections.newCollection();
		collection.add(feature);

		return collection;
	}

}
