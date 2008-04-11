/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.ows.HttpServletRequestAware;
import org.geoserver.ows.KvpRequestReader;
import org.geoserver.ows.util.KvpUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureReader;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureType;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedStyle;
import org.geotools.styling.RemoteOWS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleAttributeExtractor;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.TemporaryFeatureTypeInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.SLDValidator;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapKvpReader;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.servlets.GetMap;

public class GetMapKvpRequestReader extends KvpRequestReader implements
		HttpServletRequestAware {
	/**
	 * get map
	 */
	GetMap getMap;

	/**
	 * current request
	 */
	HttpServletRequest httpRequest;

	/**
	 * style factory
	 */
	StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);

	/**
	 * filter factory
	 */
	FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

	/**
	 * Flag to control wether styles are mandatory
	 */
	boolean styleRequired;

	/**
	 * The WMS service, that we use to pick up base layer definitions
	 */
	WMS wms;
	
	/**
	 * The data catalog
	 */
	Data catalog;
	
	/**
	 * This flags allows the kvp reader to go beyond the SLD library mode specification
	 * and match the first style that can be applied to a given layer. This is for
	 * backwards compatibility
	 */
	boolean laxStyleMatchAllowed = true;

	public GetMapKvpRequestReader(GetMap getMap, WMS wms) {
		super(GetMapRequest.class);

		this.getMap = getMap;
		this.wms = wms;
		this.catalog = wms.getData();
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public void setStyleFactory(StyleFactory styleFactory) {
		this.styleFactory = styleFactory;
	}

	public void setFilterFactory(FilterFactory filterFactory) {
		this.filterFactory = filterFactory;
	}

	public boolean isStyleRequired() {
		return styleRequired;
	}

	public void setStyleRequired(boolean styleRequired) {
		this.styleRequired = styleRequired;
	}

	public Object createRequest() throws Exception {
		GetMapRequest request = new GetMapRequest(getMap);
		request.setHttpServletRequest(httpRequest);

		return request;
	}

	public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
		GetMapRequest getMap = (GetMapRequest) super.read(request, kvp, rawKvp);

		// do some additional checks

		// srs
		String epsgCode = getMap.getSRS();

		if (epsgCode != null) {
			try {
				// set the crs as well
				CoordinateReferenceSystem mapcrs = CRS.decode(epsgCode);
				getMap.setCrs(mapcrs);
			} catch (Exception e) {
				// couldnt make it - we send off a service exception with the
				// correct info
			    throw new WmsException("Error occurred decoding the espg code " + epsgCode, "InvalidSRS", e);
			}
		}
		
		// remote OWS
		String remoteOwsType = getMap.getRemoteOwsType();
		remoteOwsType = remoteOwsType != null ? remoteOwsType.toUpperCase() : null;
		if(remoteOwsType != null && !"WFS".equals(remoteOwsType)) {
		    throw new WmsException("Unsupported remote OWS type '" + remoteOwsType + "'");
		}

		// remote OWS url
	    URL remoteOwsUrl = getMap.getRemoteOwsURL();
	    if(remoteOwsUrl != null && remoteOwsType == null)
	        throw new WmsException("REMOTE_OWS_URL specified, but REMOTE_OWS_TYPE is missing");
		
		// layers
		String layerParam = (String) kvp.get("LAYERS");
		if(layerParam != null)
		    getMap.setLayers(parseLayers(KvpUtils.readFlat(layerParam), remoteOwsUrl, remoteOwsType)); 

		// raw styles parameter
        String stylesParam = (String) kvp.get("STYLES");
        List styleNameList = Collections.EMPTY_LIST;
        if(stylesParam != null)
            styleNameList = KvpUtils.readFlat(stylesParam);

		// styles
		// process SLD_BODY, SLD, then STYLES parameter
		if (getMap.getSldBody() != null) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("Getting layers and styles from SLD_BODY");
			}

			if (getMap.getValidateSchema().booleanValue()) {
				List errors = validateSld(new ByteArrayInputStream(getMap
						.getSldBody().getBytes()));

				if (errors.size() != 0) {
					throw new WmsException(SLDValidator.getErrorMessage(
							new ByteArrayInputStream(getMap.getSldBody()
									.getBytes()), errors));
				}
			}

			StyledLayerDescriptor sld = parseSld(new ByteArrayInputStream(
					getMap.getSldBody().getBytes()));
			processSld(getMap, sld, styleNameList);
		} else if (getMap.getSld() != null) {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("Getting layers and styles from reomte SLD");
			}

			URL sldUrl = getMap.getSld();

			if (getMap.getValidateSchema().booleanValue()) {
				InputStream input = Requests.getInputStream(sldUrl);
				List errors = null;

				try {
					errors = validateSld(input);
				} finally {
					input.close();
				}

				if ((errors != null) && (errors.size() != 0)) {
					input = Requests.getInputStream(sldUrl);

					try {
						throw new WmsException(SLDValidator.getErrorMessage(
								input, errors));
					} finally {
						input.close();
					}
				}
			}

			// JD: GEOS-420, Wrap the sldUrl in getINputStream method in order
			// to do compression
			InputStream input = Requests.getInputStream(sldUrl);

			try {
				StyledLayerDescriptor sld = parseSld(input);
				processSld(getMap, sld, styleNameList);
			} finally {
				input.close();
			}
		} else {
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("Getting layers and styles from LAYERS and STYLES");
			}
			
			// ok, parse the styles parameter in isolation
			if(styleNameList.size() > 0)
			    getMap.setStyles(parseStyles(styleNameList));

			// first, expand base layers and default styles
			if (getMap.getLayers() != null) {
				List oldLayers = new ArrayList(Arrays
						.asList(getMap.getLayers()));
				List oldStyles = getMap.getStyles() != null ? new ArrayList(
						getMap.getStyles()) : new ArrayList();
				List newLayers = new ArrayList();
				List newStyles = new ArrayList();

				for (int i = 0; i < oldLayers.size(); i++) {
					MapLayerInfo info = (MapLayerInfo) oldLayers.get(i);
					Style style = oldStyles.isEmpty() ? null
							: (Style) oldStyles.get(i);
					if (info.getType() == MapLayerInfo.TYPE_BASEMAP) {
						List subLayers = info.getSubLayers();
						newLayers.addAll(subLayers);
						List currStyles = info.getStyles();
						for (int j = 0; j < subLayers.size(); j++) {
							MapLayerInfo currLayer = (MapLayerInfo) subLayers
									.get(j);
							Style currStyle = currStyles.isEmpty() ? null
									: (Style) currStyles.get(j);
							if (currStyle != null)
								newStyles.add(currStyle);
							else
								newStyles.add(currLayer.getDefaultStyle());
						}
					} else {
						newLayers.add(info);
						if (style != null)
							newStyles.add(style);
						else
							newStyles.add(info.getDefaultStyle());
					}
				}
				getMap.setLayers(newLayers);
				getMap.setStyles(newStyles);
			}

			// then proceed with standard processing
			MapLayerInfo[] layers = getMap.getLayers();
			if ((layers != null) && (layers.length > 0)) {
				List styles = getMap.getStyles();

				if (layers.length != styles.size()) {
					String msg = layers.length	+ " layers requested, but found "
							+ styles.size()	+ " styles specified. ";
					throw new WmsException(msg, getClass().getName());
				}

				for (int i = 0; i < getMap.getStyles().size(); i++) {
					Style currStyle = (Style) getMap.getStyles().get(i);
					if(currStyle == null)
					    throw new WmsException("Could not find a style for layer " 
					            + getMap.getLayers()[i].getName() 
					            + ", either none was specified or no default style is available for it",
					            "NoDefaultStyle");
					checkStyle(currStyle, layers[i]);
					if (LOGGER.isLoggable(Level.FINE)) {
						LOGGER.fine(new StringBuffer("establishing ")
								.append(currStyle.getName()).append(
										" style for ").append(
										layers[i].getName()).toString());
					}
				}
			}
		}

		// filters
		// in case of a mixed request, get with sld in post body, layers
		// are not parsed, so we can't parse filters neither...
		if ((getMap.getLayers() != null) && (getMap.getLayers().length > 0)) {
			List filters = (getMap.getFilter() != null) ? getMap.getFilter()
					: Collections.EMPTY_LIST;
			List cqlFilters = (getMap.getCQLFilter() != null) ? getMap
					.getCQLFilter() : Collections.EMPTY_LIST;
			List featureId = (getMap.getFeatureId() != null) ? getMap
					.getFeatureId() : Collections.EMPTY_LIST;

			if (!featureId.isEmpty()) {
				if (!filters.isEmpty()) {
					throw new WmsException("GetMap KVP request contained "
							+ "conflicting filters.  Filter: " + filters
							+ ", fid: " + featureId);
				}

				Set ids = new HashSet();
				for (Iterator i = featureId.iterator(); i.hasNext();) {
					ids.add(filterFactory.featureId((String) i.next()));
				}
				filters = Collections.singletonList(filterFactory.id(ids));
			}

			if (!cqlFilters.isEmpty()) {
				if (!filters.isEmpty()) {
					throw new WmsException("GetMap KVP request contained "
							+ "conflicting filters.  Filter: " + filters
							+ ", fid: " + featureId + ", cql: " + cqlFilters);
				}

				filters = cqlFilters;
			}

			int numLayers = getMap.getLayers().length;

			if (!filters.isEmpty() && (numLayers != filters.size())) {
				// as in wfs getFeatures, perform lenient parsing, if just one
				// filter, it gets
				// applied to all layers
				if (filters.size() == 1) {
					Filter f = (Filter) filters.get(0);
					filters = new ArrayList(numLayers);

					for (int i = 0; i < numLayers; i++) {
						filters.add(f);
					}
				} else {
					String msg = numLayers
							+ " layers requested, but found "
							+ filters.size()
							+ " filters specified. "
							+ "When you specify the FILTER parameter, you must provide just one, \n"
							+ " that will be applied to all layers, or exactly one for each requested layer";
					throw new WmsException(msg, getClass().getName());
				}
			}
			
			getMap.setFilter(filters);
		}

		// set the raw params used to create the request
		getMap.setRawKvp(rawKvp);

		return getMap;
	}

	/**
	 * validates an sld document.
	 * 
	 */
	private List validateSld(InputStream input) {
		// user requested to validate the schema.
		SLDValidator validator = new SLDValidator();

		return validator.validateSLD(input, httpRequest.getSession()
				.getServletContext());
	}

	/**
	 * Parses an sld document.
	 */
	private StyledLayerDescriptor parseSld(InputStream input) {
		SLDParser parser = new SLDParser(styleFactory, input);

		return parser.parseSLD();
	}

	
	public void processSld(final GetMapRequest request,
			final StyledLayerDescriptor sld, final List styleNames) throws WmsException {
	    if(request.getLayers() == null || request.getLayers().length == 0) {
	        processStandaloneSld(request, sld);
	    } else {
	        processLibrarySld(request, sld, styleNames);
	    }
	}
	
	
	/**
     * Looks in <code>sld</code> for the layers and styles to use in the map
     * composition and sets them to the <code>request</code>
     * 
     * <p>
     * This method processes SLD in library mode
     * Library mode engages when "SLD" or "SLD_BODY" are used in conjuction with
     * LAYERS and STYLES. From the spec: <br>
     * <cite> When an SLD is used as a style
     * library, the STYLES CGI parameter is interpreted in the usual way in the
     * GetMap request, except that the handling of the style names is organized
     * so that the styles defined in the SLD take precedence over the named
     * styles stored within the map server. The user-defined SLD styles can be
     * given names and they can be marked as being the default style for a
     * layer. To be more specific, if a style named “CenterLine” is referenced
     * for a layer and a style with that name is defined for the corresponding
     * layer in the SLD, then the SLD style definition is used. Otherwise, the
     * standard named-style mechanism built into the map server is used. If the
     * use of a default style is specified and a style is marked as being the
     * default for the corresponding layer in the SLD, then the default style
     * from the SLD is used; otherwise, the standard default style in the map
     * server is used. </cite>
     * 
     * @param request
     *            the GetMap request to which to set the layers and styles
     * @param sld
     *            a SLD document to take layers and styles from, following the
     *            "literal" or "library" rule.
     * 
     * @throws WmsException
     *             if anything goes wrong
     * @throws RuntimeException
     *             DOCUMENT ME!
     */
    private void processLibrarySld(final GetMapRequest request,
            final StyledLayerDescriptor sld, final List styleNames) {
        final StyledLayer[] styledLayers = sld.getStyledLayers();
        final int slCount = styledLayers.length;

        if (slCount == 0) {
            throw new WmsException("SLD document contains no layers");
        }

        final List layers = new ArrayList();
        final List styles = new ArrayList();
        MapLayerInfo[] libraryModeLayers = request.getLayers();
        MapLayerInfo currLayer = null;
        Style currStyle = null;
        String styleName = null;

        for (int i = 0; i < libraryModeLayers.length; i++) {
            currLayer = libraryModeLayers[i];
            if(styleNames != null && styleNames.size() > 0)
                styleName = (String) styleNames.get(i);

            // base map layers do not participate in library mode
            if (currLayer.getType() == MapLayerInfo.TYPE_BASEMAP) {
                List subLayers = currLayer.getSubLayers();
                layers.addAll(subLayers);
                List currStyles = currLayer.getStyles();
                for (int j = 0; j < subLayers.size(); j++) {
                    MapLayerInfo l = (MapLayerInfo) subLayers.get(j);
                    Style s = currStyles.isEmpty() ? null
                            : (Style) currStyles.get(j);
                    if (s != null)
                        styles.add(currStyle);
                    else
                        styles.add(currLayer.getDefaultStyle());
                }
            } else {
                layers.add(currLayer);
                styles.add(findStyleOf(request, currLayer, styleName, styledLayers));
            }
        }
        
        request.setLayers((MapLayerInfo[]) layers
                .toArray(new MapLayerInfo[layers.size()]));
        request.setStyles(styles);
    }

    /**
     * This one processes an SLD in non library mode, that is, it assumes it's the
     * definition of the map
     * @param request
     * @param sld
     */
    public static void processStandaloneSld(final GetMapRequest request,
            final StyledLayerDescriptor sld) {
        final StyledLayer[] styledLayers = sld.getStyledLayers();
        final int slCount = styledLayers.length;

        if (slCount == 0) {
            throw new WmsException("SLD document contains no layers");
        }

        final List layers = new ArrayList();
        final List styles = new ArrayList();
        MapLayerInfo currLayer = null;
        Style currStyle = null;
        
        StyledLayer sl = null;
        String layerName;
        UserLayer ul;

        for (int i = 0; i < slCount; i++) {
            sl = styledLayers[i];
            layerName = sl.getName();

            if (null == layerName) {
                throw new WmsException(
                        "A UserLayer without layer name was passed");
            }
            
            if(sl instanceof UserLayer && ((((UserLayer) sl)).getRemoteOWS() != null)) {
                // this beast can define multiple feature sources and multiple styles, we'll
                // have to mix and match them (ugh)
                ul = ((UserLayer) sl);
                try {
                    addRemoteLayersFromUserLayer(request, ul, layers, styles);
                } catch(IOException e) {
                    throw new WmsException("Error accessing remote layers", "RemoteAccessFailed", e);
                }
            } else {
                // simpler case, one layer, eventually multiple styles
                currLayer = new MapLayerInfo();
    
                // handle the InLineFeature stuff
                // TODO: add support for remote WFS here
                if ((sl instanceof UserLayer)
                        && ((((UserLayer) sl)).getInlineFeatureDatastore() != null)) {
                    // SPECIAL CASE - we make the temporary version
                    ul = ((UserLayer) sl);
    
                    try {
                        initializeInlineFeatureLayer(request, ul, currLayer);
                    } catch (Exception e) {
                        throw new WmsException(e);
                    }
                } else {
                    try {
                        currLayer.setFeature(GetMapKvpReader.findFeatureLayer(
                                request, layerName));
                    } catch (WmsException e) {
                        currLayer.setCoverage(GetMapKvpReader
                                .findCoverageLayer(request, layerName));
                    }
                }
    
                if (currLayer.getType() == MapLayerInfo.TYPE_VECTOR) {
                    // currStyle = findStyleOf(request, currLayer,
                    // styledLayers); // DJB: this looks like a bug, we should
                    // get the style from styledLayers[i]
    
                    // the correct thing to do its grab the style from
                    // styledLayers[i]
                    // inside the styledLayers[i] will either be :
                    // a) nothing - in which case grab the layer's default style
                    // b) a set of:
                    // i) NameStyle -- grab it from the pre-loaded styles
                    // ii)UserStyle -- grab it from the sld the user uploaded
                    //
                    // NOTE: we're going to get a set of layer->style pairs for
                    // (b).
                    addStyles(request, currLayer, styledLayers[i], layers,
                            styles);
                } else if (currLayer.getType() == MapLayerInfo.TYPE_RASTER) {
                    try {
                        addStyles(request, currLayer, styledLayers[i], layers,
                                styles);
                    } catch (WmsException wm) {
                        // hmm, well, the style they specified in the wms
                        // request
                        // wasn't found. Let's try the default raster style
                        // named 'raster'
                        currStyle = findStyle(request, "raster");
                        if (currStyle == null) {
                            // nope, no default raster style either. Give up.
                            throw new WmsException(
                                    wm.getMessage()
                                            + "  Also tried to use "
                                            + "the generic raster style 'raster', but it wasn't available.");
                        }
                        layers.add(currLayer);
                        styles.add(currStyle);
                    }
                }
            }
        }
        
        request.setLayers((MapLayerInfo[]) layers
                .toArray(new MapLayerInfo[layers.size()]));
        request.setStyles(styles);
    }

	private static void addRemoteLayersFromUserLayer(GetMapRequest request, UserLayer ul, List layers, List styles) 
	    throws WmsException, IOException {
	    RemoteOWS service = ul.getRemoteOWS();
	    if(!service.getService().equalsIgnoreCase("WFS"))
	        throw new WmsException("GeoServer only supports WFS as remoteOWS service");
	    if(service.getOnlineResource() == null)
            throw new WmsException("OnlineResource for remote WFS not specified in SLD");
	    final FeatureTypeConstraint[] featureConstraints = ul.getLayerFeatureConstraints();
        if(featureConstraints == null || featureConstraints.length == 0)
	        throw new WmsException("No FeatureTypeConstraint specified, no layer can be loaded for this UserStyle");
	    
	    DataStore remoteWFS = null;
	    List remoteTypeNames = null; 
	    try {
	        URL url = new URL(service.getOnlineResource());
	        remoteWFS = connectRemoteWFS(url);
            remoteTypeNames = new ArrayList(Arrays.asList(remoteWFS.getTypeNames()));
            Collections.sort(remoteTypeNames);
	    } catch(MalformedURLException e) {
	        throw new WmsException("Invalid online resource url: '" + service.getOnlineResource() + "'");
	    } 
	    
	    Style[] layerStyles = ul.getUserStyles();
	    if(request.getFilter() == null)
	        request.setFilter(new ArrayList());
	    for (int i = 0; i < featureConstraints.length; i++) {
	        // make sure the layer is there
	        String name = featureConstraints[i].getFeatureTypeName();
            if(Collections.binarySearch(remoteTypeNames, name) < 0) {
               throw new WmsException("Could not find layer feature type '" 
                       + name + "' on remote WFS '" + service.getOnlineResource()); 
            }
            
            // grab the filter
            Filter filter = featureConstraints[i].getFilter();
            if(filter == null)
                filter = Filter.INCLUDE;
            
            // connect the layer
            FeatureSource fs = remoteWFS.getFeatureSource(name);
            
            // this is messy, why the spec allows for multiple constraints and multiple
            // styles is beyond me... we'll style each remote layer with all possible
            // styles, feauture type style matching will do the rest during rendering
            for (int j = 0; j < layerStyles.length; j++) {
                Style style = layerStyles[i];
                MapLayerInfo info = new MapLayerInfo(fs);
                layers.add(info);
                styles.add(style);
                // treat it like an externally provided filter... ugly I know, but
                // the sane thing (adding a filter as a MapLayerInfo field) would
                // break havoc in GetFeatureInfo
                request.getFilter().add(filter);
            }
        }
    }

    /**
	 * the correct thing to do its grab the style from styledLayers[i] inside
	 * the styledLayers[i] will either be : a) nothing - in which case grab the
	 * layer's default style b) a set of: i) NameStyle -- grab it from the
	 * pre-loaded styles ii)UserStyle -- grab it from the sld the user uploaded
	 * 
	 * NOTE: we're going to get a set of layer->style pairs for (b). these are
	 * added to layers,styles
	 * 
	 * NOTE: we also handle some featuretypeconstraints
	 * 
	 * @param request
	 * @param currLayer
	 * @param layer
	 * @param layers
	 * @param styles
	 */
	public static void addStyles(GetMapRequest request, MapLayerInfo currLayer,
			StyledLayer layer, List layers, List styles) throws WmsException {
		if (currLayer == null) {
			return; // protection
		}

		Style[] layerStyles = null;
		FeatureTypeConstraint[] ftcs = null;

		if (layer instanceof NamedLayer) {
			ftcs = ((NamedLayer) layer).getLayerFeatureConstraints();
			layerStyles = ((NamedLayer) layer).getStyles();
		} else if (layer instanceof UserLayer) {
			ftcs = ((UserLayer) layer).getLayerFeatureConstraints();
			layerStyles = ((UserLayer) layer).getUserStyles();
		}

		// DJB: TODO: this needs to do the whole thing, not just names
		if (ftcs != null) {
			FeatureTypeConstraint ftc;
			final int length = ftcs.length;

			for (int t = 0; t < length; t++) {
				ftc = ftcs[t];

				if (ftc.getFeatureTypeName() != null) {
					String ftc_name = ftc.getFeatureTypeName();

					// taken from lite renderer
					boolean matches;

					try {
						matches = currLayer.getFeature().getFeatureType()
								.isDescendedFrom(null, ftc_name)
								|| currLayer.getFeature().getFeatureType()
										.getTypeName().equalsIgnoreCase(
												ftc_name);
					} catch (Exception e) {
						matches = false; // bad news
					}

					if (!matches) {
						continue; // this layer is fitered out
					}
				}
			}
		}

		// handle no styles -- use default
		if ((layerStyles == null) || (layerStyles.length == 0)) {
			layers.add(currLayer);
			styles.add(currLayer.getDefaultStyle());

			return;
		}

		final int length = layerStyles.length;
		Style s;

		for (int t = 0; t < length; t++) {
			if (layerStyles[t] instanceof NamedStyle) {
				layers.add(currLayer);
				s = findStyle(request, ((NamedStyle) layerStyles[t]).getName());

				if (s == null) {
					throw new WmsException("couldnt find style named '"
							+ ((NamedStyle) layerStyles[t]).getName() + "'");
				}

				styles.add(s);
			} else {
				layers.add(currLayer);
				styles.add(layerStyles[t]);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param request
	 * @param currStyleName
	 * 
	 * @return the configured style named <code>currStyleName</code> or
	 *         <code>null</code> if such a style does not exists on this
	 *         server.
	 */
	public static Style findStyle(GetMapRequest request, String currStyleName) {
		Style currStyle;
		Map configuredStyles = request.getWMS().getData().getStyles();

		currStyle = (Style) configuredStyles.get(currStyleName);

		return currStyle;
	}

	/**
	 * Finds the style for <code>layer</code> in <code>styledLayers</code>
	 * or the layer's default style if <code>styledLayers</code> has no a
	 * UserLayer or a NamedLayer with the same name than <code>layer</code>
	 * <p>
	 * This method is used to parse the style of a layer for SLD and SLD_BODY
	 * parameters, both in library and literal mode. Thus, once the declared
	 * style for the given layer is found, it is checked for validity of
	 * appliance for that layer (i.e., whether the featuretype contains the
	 * attributes needed for executing the style filters).
	 * </p>
	 * 
	 * @param request
	 *            used to find out an internally configured style when
	 *            referenced by name by a NamedLayer
	 * 
	 * @param layer
	 *            one of the layers that was requested through the
	 *            LAYERS parameter or through and SLD document when the request
	 *            is in literal mode.
	 * @param styledLayers
	 *            a set of StyledLayers from where to find the SLD layer with
	 *            the same name as <code>layer</code> and extract the style to
	 *            apply.
	 * 
	 * @return the Style applicable to <code>layer</code> extracted from
	 *         <code>styledLayers</code>.
	 * 
	 * @throws RuntimeException
	 *             if one of the StyledLayers is neither a UserLayer nor a
	 *             NamedLayer. This shuoldn't happen, since the only allowed
	 *             subinterfaces of StyledLayer are NamedLayer and UserLayer.
	 * @throws WmsException
	 */
	private Style findStyleOf(GetMapRequest request,
			MapLayerInfo layer, String styleName, StyledLayer[] styledLayers)
			throws WmsException {
		Style style = null;
		String layerName = layer.getName();
		StyledLayer sl;

		for (int i = 0; i < styledLayers.length; i++) {
			sl = styledLayers[i];

			if (layerName.equals(sl.getName())) {
				if (sl instanceof UserLayer) {
					Style[] styles = ((UserLayer) sl).getUserStyles();

					// if the style name has not been specified, look it up
					// the default style, otherwise lookup the one requested
					for(int j = 0; style == null && styles != null && j < styles.length; j++) {
					    if(styleName == null || styleName.equals("") && styles[j].isDefault())
					        style = styles[j];
					    else if(styleName != null && styleName.equals(styles[j].getName()))
					        style = styles[j];
					}
				} else if (sl instanceof NamedLayer) {
					Style[] styles = ((NamedLayer) sl).getStyles();

					// if the style name has not been specified, look it up
                    // the default style, otherwise lookup the one requested
                    for(int j = 0; style == null && styles != null && j < styles.length; j++) {
                        if((styleName == null || styleName.equals("")) && styles[j].isDefault())
                            style = styles[j];
                        else if(styleName != null && styleName.equals(styles[j].getName()))
                            style = styles[j];
                    }

					if (style instanceof NamedStyle) {
						style = findStyle(request, style.getName());
					}
				} else {
					throw new RuntimeException("Unknown layer type: " + sl);
				}

				break;
			}
		}
		
		// fallback on the old GeoServer behaviour, if the style is not found find
		// the first style that matches the type name
		// TODO: would be nice to have a switch to turn this off since it's out of the spec
		if(style == null && laxStyleMatchAllowed) {
			for (int i = 0; i < styledLayers.length; i++) {
				sl = styledLayers[i];

				if (layerName.equals(sl.getName())) {
					if (sl instanceof UserLayer) {
						Style[] styles = ((UserLayer) sl).getUserStyles();

						if ((null != styles) && (0 < styles.length)) {
							style = styles[0];
						}
					} else if (sl instanceof NamedLayer) {
						Style[] styles = ((NamedLayer) sl).getStyles();

						if ((null != styles) && (0 < styles.length)) {
							style = styles[0];
						}

						if (style instanceof NamedStyle) {
							style = findStyle(request, style.getName());
						}
					} else {
						throw new RuntimeException("Unknown layer type: " + sl);
					}

					break;
				}
			}
		}

		// still not found? Fall back on the server default ones
		if (style == null) {
		    if(styleName == null || "".equals(styleName)) {
		        style = layer.getDefaultStyle();
		        if(style == null)
		            throw new WmsException("Could not find a default style for " + layer.getName());
		    } else {
		        style = catalog.getStyle(styleName);
                if (style == null) {
                   String msg = "No such style: " + styleName;
                   throw new WmsException(msg, "StyleNotDefined");
                }
            }
		}

		checkStyle(style, layer);

		return style;
	}

	/**
	 * Checks to make sure that the style passed in can process the FeatureType.
	 * 
	 * @param style
	 *            The style to check
	 * @param layer
	 *            The source requested.
	 * 
	 * @throws WmsException
	 *             DOCUMENT ME!
	 */
	private static void checkStyle(Style style, MapLayerInfo layer)
			throws WmsException {
	    if(layer.getType() == layer.TYPE_BASEMAP || layer.getType() == layer.TYPE_RASTER)
	        return;
	    
	    // extract attributes used in the style
		StyleAttributeExtractor sae = new StyleAttributeExtractor();
		sae.visit(style);
		String[] styleAttributes = sae.getAttributeNames();

		// see if we can collect any attribute out of the provided layer
		Set attributes = new HashSet();
		if(layer.getType() == MapLayerInfo.TYPE_VECTOR || layer.getType() == MapLayerInfo.TYPE_REMOTE_VECTOR) {
            try {
                final FeatureType type;
                if(layer.getType() == MapLayerInfo.TYPE_VECTOR)
                    type = layer.getFeature().getFeatureType();
                else
                    type = layer.getRemoteFeatureSource().getSchema();
                for(int i = 0; i < type.getAttributeCount(); i++) {
                    attributes.add(type.getAttributeType(i).getLocalName());
                }
            } catch (IOException ioe) {
                throw new RuntimeException(
                        "Error getting FeatureType, this should never happen!");
            }
		} 
		
		// check all attributes required by the style are available
		String attName;
		final int length = styleAttributes.length;
		for (int i = 0; i < length; i++) {
			attName = styleAttributes[i];

			if (!attributes.contains(attName)) {
				throw new WmsException(
						"The requested Style can not be used with this layer.  The style specifies " +
						"an attribute of " + attName + " and the layer is: " + layer.getName());
			}
		}
	}

	/**
	 * Method to initialize a user layer which contains inline features.
	 * 
	 * @param httpRequest
	 *            The request
	 * @param mapLayer
	 *            The map layer.
	 * 
	 * @throws Exception
	 */

	// JD: the reason this method is static is to share logic among the xml
	// and kvp reader, ugh...
	public static void initializeInlineFeatureLayer(
			GetMapRequest getMapRequest, UserLayer ul, MapLayerInfo currLayer)
			throws Exception {
		// SPECIAL CASE - we make the temporary version
		currLayer.setFeature(new TemporaryFeatureTypeInfo(ul
				.getInlineFeatureDatastore()));

		// what if they didn't put an "srsName" on their geometry in their
		// inlinefeature?
		// I guess we should assume they mean their geometry to exist in the
		// output SRS of the
		// request they're making.
		if (ul.getInlineFeatureType().getDefaultGeometry()
				.getCoordinateSystem() == null) {
			LOGGER
					.warning("No CRS set on inline features default geometry.  Assuming the requestor has their inlinefeatures in the boundingbox CRS.");

			FeatureType currFt = ul.getInlineFeatureType();
			Query q = new DefaultQuery(currFt.getTypeName(), Filter.INCLUDE);
			FeatureReader ilReader = ul.getInlineFeatureDatastore()
					.getFeatureReader(q, Transaction.AUTO_COMMIT);
			CoordinateReferenceSystem crs = (getMapRequest.getCrs() == null) ? DefaultGeographicCRS.WGS84
					: getMapRequest.getCrs();
			MemoryDataStore reTypedDS = new MemoryDataStore(
					new ForceCoordinateSystemFeatureReader(ilReader, crs));
			currLayer.setFeature(new TemporaryFeatureTypeInfo(reTypedDS));
		}
	}
	
	 protected MapLayerInfo[] parseLayers(List values, URL remoteOwsUrl, String remoteOwsType) throws Exception {
	        List layers = new ArrayList();
	        List layerNames = values;
	        String layerName = null;

	        // Grab remote OWS data store if needed
	        DataStore remoteWFS = null;
	        List remoteTypeNames = null; 
	        if("WFS".equals(remoteOwsType) && remoteOwsUrl != null) {
	             remoteWFS = connectRemoteWFS(remoteOwsUrl);
	             remoteTypeNames = new ArrayList(Arrays.asList(remoteWFS.getTypeNames()));
	             Collections.sort(remoteTypeNames);
	        }

	        ////
	        // Layer lookup requires to:
	        // * Look into the remote OWS first
	        // * Look among the local layers
	        //   * expand local grouped layers (flatten them)
	        ////
	        for (Iterator it = layerNames.iterator(); it.hasNext();) {
	            layerName = (String) it.next();
	            
	            // search into the remote WFS if there is any
	            if(remoteTypeNames != null && Collections.binarySearch(remoteTypeNames, layerName) >= 0) {
	                FeatureSource remoteSource = remoteWFS.getFeatureSource(layerName);
	                if(remoteSource != null)
	                    layers.add(new MapLayerInfo(remoteSource));
	                    continue;
	            }

	            Integer layerType = catalog.getLayerType(layerName);
	            if (layerType != null) {
	                layers.add(buildMapLayerInfo(layerName));
	            } else {
	                if(wms.getBaseMapLayers().containsKey(layerName)) {
	                    layers.add(buildMapLayerInfo(layerName));
	                } else {
	                    ////
	                    // Search for grouped layers (attention: heavy process)
	                    ////
	                    boolean found = false;
	                    String catalogLayerName = null;
	    
	                    for (Iterator c_keys = catalog.getLayerNames().iterator(); c_keys.hasNext();) {
	                        catalogLayerName = (String) c_keys.next();
	    
	                        try {
	                            FeatureTypeInfo ftype = findFeatureLayer(catalogLayerName);
	                            String wmsPath = ftype.getWmsPath();
	    
	                            if ((wmsPath != null) && wmsPath.matches(".*/" + layerName)) {
	                                layers.add(buildMapLayerInfo(catalogLayerName));
	                                found = true;
	                            }
	                        } catch (Exception e_1) {
	                            try {
	                                CoverageInfo cv = findCoverageLayer(catalogLayerName);
	                                String wmsPath = cv.getWmsPath();
	    
	                                if ((wmsPath != null) && wmsPath.matches(".*/" + layerName)) {
	                                    layers.add(buildMapLayerInfo(catalogLayerName));
	                                    found = true;
	                                }
	                            } catch (Exception e_2) {
	                            }
	                        }
	                    }
	                    if(!found)
	                        throw new WmsException("Could not find layer "  + layerName,"LayerNotDefined");
	                }
	                
	            } 
	        }

	        if (layers.size() == 0) {
	            throw new WmsException("No LAYERS has been requested", getClass().getName());
	        }
	        return (MapLayerInfo[]) layers.toArray(new MapLayerInfo[layers.size()]);
	    }

        private static DataStore connectRemoteWFS(URL remoteOwsUrl) throws WmsException {
            try {
                WFSDataStoreFactory factory = new WFSDataStoreFactory();
                Map params = new HashMap(factory.getImplementationHints());
                params.put(WFSDataStoreFactory.URL.key, remoteOwsUrl + "request=GetCapabilities&service=WFS");
                params.put(WFSDataStoreFactory.TRY_GZIP.key, Boolean.TRUE);
                return factory.createDataStore(params);
            } catch(Exception e) {
                throw new WmsException("Could not connect to remote OWS", "RemoteOWSFailure",e);
            }
        }
	    
	    private MapLayerInfo buildMapLayerInfo(String layerName) throws Exception {
	        MapLayerInfo li = new MapLayerInfo();

	        FeatureTypeInfo ftype = findFeatureLayer(layerName);
	        if (ftype != null) {
	            li.setFeature(ftype);
	        } else {
	            CoverageInfo cv = findCoverageLayer(layerName);
	            if (cv != null) {
	                li.setCoverage(cv);
	            } else {
	                if (wms.getBaseMapLayers().containsKey(layerName)) {
	                    String styleCsl = (String) wms.getBaseMapStyles().get(layerName);
	                    String layerCsl = (String) wms.getBaseMapLayers().get(layerName);
	                    MapLayerInfo[] layerArray = (MapLayerInfo[]) parseLayers(KvpUtils.readFlat(layerCsl), null, null);
	                    List styleList = (List) parseStyles(KvpUtils.readFlat(styleCsl));
	                    li.setBase(layerName, new ArrayList(Arrays.asList(layerArray)), styleList);
	                } else {
	                    throw new WmsException("Layer " + layerName + " could not be found");
	                }
	            }
	        }
	        return li;
	    }

	    FeatureTypeInfo findFeatureLayer(String layerName)
	        throws WmsException {
	        FeatureTypeInfo ftype = null;
	        Integer layerType = catalog.getLayerType(layerName);

	        if (Data.TYPE_VECTOR != layerType) {
	            return null;
	        } else {
	            ftype = catalog.getFeatureTypeInfo(layerName);
	        }

	        return ftype;
	    }

	    CoverageInfo findCoverageLayer(String layerName) throws WmsException {
	        CoverageInfo cv = null;
	        Integer layerType = catalog.getLayerType(layerName);

	        if (Data.TYPE_RASTER != layerType) {
	            return null;
	        } else {
	            cv = catalog.getCoverageInfo(layerName);
	        }

	        return cv;
	    }
	    
	    protected List parseStyles(List styleNames) throws Exception {
	        List styles = new ArrayList();
	        for (Iterator it = styleNames.iterator(); it.hasNext();) {
                String styleid = (String) it.next();
                
                if ("".equals(styleid)) {
                    //return null, this should flag request reader to use default for 
                    // the associated layer
                    styles.add(null);
                } else {
                    final Style style = catalog.getStyle(styleid);
                    if (style == null) {
                        String msg = "No such style: " + styleid;
                        throw new WmsException(msg, "StyleNotDefined");
                    }
                    styles.add(style);
                }
            }
	        return styles;
	    }

	    /**
		 * This flags allows the kvp reader to go beyond the SLD library mode specification
		 * and match the first style that can be applied to a given layer. This is for
		 * backwards compatibility
		 */
		public boolean isLaxStyleMatchAllowed() {
			return laxStyleMatchAllowed;
		}

		public void setLaxStyleMatchAllowed(boolean laxStyleMatchAllowed) {
			this.laxStyleMatchAllowed = laxStyleMatchAllowed;
		}
}
