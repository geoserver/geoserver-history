package org.geoserver.wcs;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.wcs11.AxisSubsetType;
import net.opengis.wcs11.DescribeCoverageType;
import net.opengis.wcs11.DomainSubsetType;
import net.opengis.wcs11.FieldSubsetType;
import net.opengis.wcs11.GetCapabilitiesType;
import net.opengis.wcs11.GetCoverageType;
import net.opengis.wcs11.GridCrsType;
import net.opengis.wcs11.OutputType;
import net.opengis.wcs11.RangeSubsetType;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageDimensionInfo;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.data.util.CoverageUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.wcs.kvp.GridCS;
import org.geoserver.wcs.kvp.GridType;
import org.geoserver.wcs.response.DescribeCoverageTransformer;
import org.geoserver.wcs.response.WCSCapsTransformer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegateFactory;

public class DefaultWebCoverageService111 implements WebCoverageService111 {
    Logger LOGGER = Logging.getLogger(DefaultWebCoverageService111.class);

    private final static Hints HINTS = new Hints();
    static {
        HINTS.add(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        HINTS.add(new Hints(Hints.OVERVIEW_POLICY, OverviewPolicy.IGNORE));
    }

    private WCSInfo wcs;

    private Catalog catalog;

	private GeoServer geoServer;

    public DefaultWebCoverageService111(GeoServer geoServer) {
        this.wcs = geoServer.getService(WCSInfo.class);
        this.geoServer = geoServer;
        this.catalog = geoServer.getCatalog();
    }
    
    public WCSInfo getServiceInfo() {
        return wcs;
    }

    public WCSCapsTransformer getCapabilities(GetCapabilitiesType request) {
        // do the version negotiation dance
        List<String> provided = new ArrayList<String>();
        // provided.add("1.0.0");
        provided.add("1.1.0");
        provided.add("1.1.1");
        List<String> accepted = null;
        if (request.getAcceptVersions() != null)
            accepted = request.getAcceptVersions().getVersion();
        String version = RequestUtils.getVersionOws11(provided, accepted);

        // TODO: add support for 1.0.0 in here

        if ("1.1.0".equals(version) || "1.1.1".equals(version)) {
            WCSCapsTransformer capsTransformer = new WCSCapsTransformer(geoServer);
            capsTransformer.setEncoding(Charset.forName((wcs.getGeoServer().getGlobal().getCharset())));
            return capsTransformer;
        }

        throw new WcsException("Could not understand version:" + version);
    }

    public DescribeCoverageTransformer describeCoverage(DescribeCoverageType request) {
        final String version = request.getVersion();
        if ("1.1.0".equals(version) || "1.1.1".equals(version)) {
            DescribeCoverageTransformer describeTransformer = new DescribeCoverageTransformer(wcs, catalog);
            describeTransformer.setEncoding(Charset.forName(wcs.getGeoServer().getGlobal().getCharset()));
            return describeTransformer;
        }

        throw new WcsException("Could not understand version:" + version);
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
	public GridCoverage[] getCoverage(GetCoverageType request) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(new StringBuffer("execute CoverageRequest response. Called request is: ")
                    .append(request).toString());
        }

        CoverageInfo meta = null;
        GridCoverage2D coverage = null;
        try {
            meta = catalog.getCoverageByName(request.getIdentifier().getValue());

            // first let's run some sanity checks on the inputs
            checkDomainSubset(meta, request.getDomainSubset());
            checkRangeSubset(meta, request.getRangeSubset());
            checkOutput(meta, request.getOutput());

            // grab the format, the reader using the default params
            final AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) meta.getGridCoverageReader(null, HINTS);

            // handle spatial domain subset, if needed
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
            final BoundingBoxType bbox = request.getDomainSubset().getBoundingBox();
            final CoordinateReferenceSystem nativeCRS = originalEnvelope.getCoordinateReferenceSystem();
            final GeneralEnvelope requestedEnvelopeInSourceCRS;
            final GeneralEnvelope requestedEnvelope;
            MathTransform bboxToNativeTx=null;
            if (bbox != null) {
                // first off, parse the envelope corners
                double[] lowerCorner = new double[bbox.getLowerCorner().size()];
                double[] upperCorner = new double[bbox.getUpperCorner().size()];
                for (int i = 0; i < lowerCorner.length; i++) {
                    lowerCorner[i] = (Double) bbox.getLowerCorner().get(i);
                    upperCorner[i] = (Double) bbox.getUpperCorner().get(i);
                }
                requestedEnvelope = new GeneralEnvelope(lowerCorner, upperCorner);
                // grab the native crs
                // if no crs has beens specified, the native one is assumed
                if (bbox.getCrs() == null) {
                    requestedEnvelope.setCoordinateReferenceSystem(nativeCRS);
                    requestedEnvelopeInSourceCRS = requestedEnvelope;
                } else {
                    // otherwise we need to transform
                    final CoordinateReferenceSystem bboxCRS = CRS.decode(bbox.getCrs());
                    requestedEnvelope.setCoordinateReferenceSystem(bboxCRS);
                    bboxToNativeTx = CRS.findMathTransform(bboxCRS, nativeCRS,true);
                    if(!bboxToNativeTx.isIdentity()){
	                    requestedEnvelopeInSourceCRS = CRS.transform(bboxToNativeTx,requestedEnvelope);
	                    requestedEnvelopeInSourceCRS.setCoordinateReferenceSystem(nativeCRS);
                    }
                    else
                    	requestedEnvelopeInSourceCRS= new GeneralEnvelope(requestedEnvelope);
                }
            } else {
                requestedEnvelopeInSourceCRS = reader.getOriginalEnvelope();
                requestedEnvelope = requestedEnvelopeInSourceCRS;
            }

            final GridCrsType gridCRS = request.getOutput().getGridCRS();
            // TODO: handle time domain subset...

            // Compute the target crs, the crs that the final coverage will be
            // served into
            final CoordinateReferenceSystem targetCRS;
            if (gridCRS == null)
                targetCRS = reader.getOriginalEnvelope().getCoordinateReferenceSystem();
            else
                targetCRS = CRS.decode(gridCRS.getGridBaseCRS());

            // grab the grid to world transformation
            MathTransform gridToCRS = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
            if (gridCRS != null) {
                Double[] origin = (Double[]) gridCRS.getGridOrigin();
                Double[] offsets = (Double[]) gridCRS.getGridOffsets();

                // from the specification if grid origin is omitted and the crs
                // is 2d the default it's 0,0
                if (origin == null) {
                    origin = new Double[] { 0.0, 0.0 };
                }

                // if no offsets has been specified we try to default on the
                // native ones
                if (offsets == null) {
                    if (!(gridToCRS instanceof AffineTransform2D)&& !(gridToCRS instanceof IdentityTransform))
                        throw new WcsException(
                                "Internal error, the coverage we're playing with does not have an affine transform...");

                    if (gridToCRS instanceof IdentityTransform) {
                        if (gridCRS.getGridType().equals(GridType.GT2dSimpleGrid))
                            offsets = new Double[] { 1.0, 1.0 };
                        else
                            offsets = new Double[] { 1.0, 0.0, 0.0, 1.0 };
                    } else {
                        AffineTransform2D affine = (AffineTransform2D) gridToCRS;
                        if (gridCRS.getGridType().equals(GridType.GT2dSimpleGrid))
                            offsets = new Double[] { affine.getScaleX(), affine.getScaleY() };
                        else
                            offsets = new Double[] { affine.getScaleX(), affine.getShearX(),affine.getShearY(), affine.getScaleY() };
                    }
                }

                // building the actual transform for the resulting grid geometry
                AffineTransform tx;
                if (gridCRS.getGridType().equals(GridType.GT2dSimpleGrid.getXmlConstant())) {
                    tx = new AffineTransform(offsets[0], 0, 0, offsets[1], origin[0], origin[1]);
                } else {
                    tx = new AffineTransform(offsets[0], offsets[2], offsets[1], offsets[3],
                            origin[0], origin[1]);
                }
                gridToCRS = new AffineTransform2D(tx);
            }

            // now we have enough info to read the coverage, grab the parameters
            // and add the grid geometry info
            final Map parameters = CoverageUtils.getParametersKVP(reader.getFormat().getReadParameters());
            final GeneralEnvelope intersectionEnvelopeInSourceCRS = new GeneralEnvelope(requestedEnvelopeInSourceCRS);
            intersectionEnvelopeInSourceCRS.intersect(originalEnvelope);
            final GeneralEnvelope intersectionEnvelope= 
            	bboxToNativeTx.isIdentity()?
            			new GeneralEnvelope(intersectionEnvelopeInSourceCRS):
            			CRS.transform(bboxToNativeTx.inverse(), intersectionEnvelopeInSourceCRS);
            intersectionEnvelope.setCoordinateReferenceSystem(targetCRS);
            
            
            final GridGeometry2D requestedGridGeometry =new GridGeometry2D(PixelInCell.CELL_CENTER, gridToCRS, intersectionEnvelopeInSourceCRS, null);
            parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),requestedGridGeometry);
            coverage = (GridCoverage2D) reader.read(CoverageUtils.getParameters(reader.getFormat().getReadParameters(), parameters, true));
            if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
                throw new IOException("The requested coverage could not be found.");
            }

            /**
             * Band Select (works on just one field)
             */
            GridCoverage2D bandSelectedCoverage = coverage;
            String interpolationType = null;
            if (request.getRangeSubset() != null) {
                if (request.getRangeSubset().getFieldSubset().size() > 1) {
                    throw new WcsException("Multi field coverages are not supported yet");
                }

                FieldSubsetType field = (FieldSubsetType) request.getRangeSubset().getFieldSubset().get(0);
                interpolationType = field.getInterpolationType();

                // handle axis subset
                if (field.getAxisSubset().size() > 1) {
                    throw new WcsException("Multi axis coverages are not supported yet");
                }
                if (field.getAxisSubset().size() == 1) {
                    // prepare a support structure to quickly get the band index
                    // of a
                    // key
                    List<CoverageDimensionInfo> dimensions = meta.getDimensions();
                    Map<String, Integer> dimensionMap = new HashMap<String, Integer>();
                    for (int i = 0; i < dimensions.size(); i++) {
                        String keyName = dimensions.get(i).getName().replace(' ', '_');
                        dimensionMap.put(keyName, i);
                    }

                    // extract the band indexes
                    AxisSubsetType axisSubset = (AxisSubsetType) field.getAxisSubset().get(0);
                    List keys = axisSubset.getKey();
                    int[] bands = new int[keys.size()];
                    for (int j = 0; j < bands.length; j++) {
                        final String key = (String) keys.get(j);
                        Integer index = dimensionMap.get(key);
                        if (index == null)
                            throw new WcsException("Unknown field/axis/key combination "
                                    + field.getIdentifier().getValue() + "/"
                                    + axisSubset.getIdentifier() + "/" + key);
                        bands[j] = index;
                    }

                    // finally execute the band select
                    try {
                        bandSelectedCoverage = (GridCoverage2D) WCSUtils
                                .bandSelect(coverage, bands);
                    } catch (WcsException e) {
                        throw new WcsException(e.getLocalizedMessage());
                    }
                }
            }

            /**
             * Checking for supported Interpolation Methods
             */
            Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
            if (interpolationType != null) {
                if (interpolationType.equalsIgnoreCase("bilinear")) {
                    interpolation = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
                } else if (interpolationType.equalsIgnoreCase("bicubic")) {
                    interpolation = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
                } else if (interpolationType.equalsIgnoreCase("nearest")) {
                    interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
                }
            }

            /**
             * Reproject
             */
            // adjust the grid geometry to use the final bbox and crs
            final GridGeometry2D destinationGridGeometry =new GridGeometry2D(PixelInCell.CELL_CENTER, gridToCRS, intersectionEnvelope, null);
            final GridCoverage2D reprojectedCoverage = WCSUtils.resample(
            		bandSelectedCoverage,
                    nativeCRS, targetCRS, destinationGridGeometry,interpolation);

            return new GridCoverage[] { reprojectedCoverage };
        } catch (Throwable e) {
            if (e instanceof WcsException)
                throw (WcsException) e;
            else
                throw new WcsException(e);
        }

    }

    private void checkDomainSubset(CoverageInfo meta, DomainSubsetType domainSubset)
            throws Exception {
        BoundingBoxType bbox = domainSubset.getBoundingBox();
        
        // workaround for http://jira.codehaus.org/browse/GEOT-1710
        if("urn:ogc:def:crs:OGC:1.3:CRS84".equals(bbox.getCrs()))
            bbox.setCrs("EPSG:4326");
        
        CoordinateReferenceSystem bboxCRs = CRS.decode(bbox.getCrs());
        Envelope gridEnvelope = meta.getGridCoverage(null, HINTS).getEnvelope();
        GeneralEnvelope gridEnvelopeBboxCRS = null;
        if (bboxCRs instanceof GeographicCRS) {
            try {
                CoordinateOperationFactory cof = CRS.getCoordinateOperationFactory(true);

                final CoordinateOperation operation = cof.createOperation(gridEnvelope
                        .getCoordinateReferenceSystem(), bboxCRs);
                gridEnvelopeBboxCRS = CRS.transform(operation, gridEnvelope);
            } catch (Exception e) {
                // this may happen, there is nothing we can do about it, we just
                // use the back transformed envelope to be more lenient about
                // which coordinate coorections to make on the longitude axis
                // should the antimeridian style be used
            }
        }

        // check the coordinates, but make sure the case 175,-175 is handled
        // as valid for the longitude axis in a geographic coordinate system
        // see section 7.6.2 of the WCS 1.1.1 spec)
        List<Double> lower = bbox.getLowerCorner();
        List<Double> upper = bbox.getUpperCorner();
        for (int i = 0; i < lower.size(); i++) {
            if (lower.get(i) > upper.get(i)) {
                final CoordinateSystemAxis axis = bboxCRs.getCoordinateSystem().getAxis(i);
                // see if the coordinates can be fixed
                if (bboxCRs instanceof GeographicCRS && axis.getDirection() == AxisDirection.EAST) {

                    if (gridEnvelopeBboxCRS != null) {
                        // try to guess which one needs to be fixed
                        final double envMax = gridEnvelopeBboxCRS.getMaximum(i);
                        if (envMax >= lower.get(i))
                            upper.set(i, upper.get(i) + (axis.getMaximumValue() - axis.getMinimumValue()));
                        else
                            lower.set(i, lower.get(i) - (axis.getMaximumValue() - axis.getMinimumValue()));
                            
                    } else {
                        // just fix the upper and hope...
                        upper.set(i, upper.get(i) + (axis.getMaximumValue() - axis.getMinimumValue()));
                    }
                }

                // if even after the fix we're in the wrong situation, complain
                if (lower.get(i) > upper.get(i)) {
                    throw new WcsException("illegal bbox, min of dimension " + (i + 1) + ": "
                            + lower.get(i) + " is " + "greater than max of same dimension: "
                            + upper.get(i), WcsExceptionCode.InvalidParameterValue, "BoundingBox");
                }
            }

        }
    }

    /**
     * Checks that the elements of the Output part of the request do make sense
     * by comparing them to the coverage metadata
     * 
     * @param info
     * @param rangeSubset
     */
    private void checkOutput(CoverageInfo meta, OutputType output) {
        if (output == null)
            return;

        String format = output.getFormat();
        String declaredFormat = getDeclaredFormat(meta.getSupportedFormats(), format);
        if (declaredFormat == null)
            throw new WcsException("format " + format + " is not supported for this coverage",
                    InvalidParameterValue, "format");

        final GridCrsType gridCRS = output.getGridCRS();
        if (gridCRS != null) {
            // check grid base crs is valid, and eventually default it out
            String gridBaseCrs = gridCRS.getGridBaseCRS();
            if (gridBaseCrs != null) {
                // make sure the requested is among the supported ones, by
                // making a
                // code level
                // comparison (to avoid assuming epsg:xxxx and
                // http://www.opengis.net/gml/srs/epsg.xml#xxx are different
                // ones.
                // We'll also consider the urn one comparable, allowing eventual
                // axis flip on the
                // geographic crs
                String actualCRS = null;
                final String gridBaseCrsCode = extractCode(gridBaseCrs);
                for (Iterator it = meta.getResponseSRS().iterator(); it.hasNext();) {
                    final String responseCRS = (String) it.next();
                    final String code = extractCode(responseCRS);
                    if (code.equalsIgnoreCase(gridBaseCrsCode)) {
                        actualCRS = responseCRS;
                    }
                }
                if (actualCRS == null)
                    throw new WcsException("CRS " + gridBaseCrs
                            + " is not among the supported ones for coverage " + meta.getName(),
                            WcsExceptionCode.InvalidParameterValue, "GridBaseCrs");
                gridCRS.setGridBaseCRS(gridBaseCrs);
            } else {
                String code = GML2EncodingUtils.epsgCode(meta.getCRS());
                gridCRS.setGridBaseCRS("urn:x-ogc:def:crs:EPSG:" + code);
            }

            // check grid type makes sense and apply default otherwise
            String gridTypeValue = gridCRS.getGridType();
            GridType type = GridType.GT2dGridIn2dCrs;
            if (gridTypeValue != null) {
                type = null;
                for (GridType gt : GridType.values()) {
                    if (gt.getXmlConstant().equalsIgnoreCase(gridTypeValue))
                        type = gt;
                }
                if (type == null)
                    throw new WcsException("Unknown grid type " + gridTypeValue,
                            InvalidParameterValue, "GridType");
                else if (type == GridType.GT2dGridIn3dCrs)
                    throw new WcsException("Unsupported grid type " + gridTypeValue,
                            InvalidParameterValue, "GridType");
            }
            gridCRS.setGridType(type.getXmlConstant());

            // check gridcs and apply only value we know about
            String gridCS = gridCRS.getGridCS();
            if (gridCS != null) {
                if (!gridCS.equalsIgnoreCase(GridCS.GCSGrid2dSquare.getXmlConstant()))
                    throw new WcsException("Unsupported grid cs " + gridCS, InvalidParameterValue,
                            "GridCS");
            }
            gridCRS.setGridCS(GridCS.GCSGrid2dSquare.getXmlConstant());

            // check the grid origin and set defaults
            CoordinateReferenceSystem crs = null;
            try {
                crs = CRS.decode(gridCRS.getGridBaseCRS());
            } catch (Exception e) {
                throw new WcsException("Could not understand crs " + gridCRS.getGridBaseCRS(),
                        WcsExceptionCode.InvalidParameterValue, "GridBaseCRS");
            }
            if (!gridCRS.isSetGridOrigin() || gridCRS.getGridOrigin() == null) {
                // if not set, we have a default of "0 0" as a string, since I
                // cannot
                // find a way to make it default to new Double[] {0 0} I'll fix
                // it here
                Double[] origin = new Double[type.getOriginArrayLength()];
                Arrays.fill(origin, 0.0);
                gridCRS.setGridOrigin(origin);
            } else {
                Double[] gridOrigin = (Double[]) gridCRS.getGridOrigin();
                // make sure the origin dimension matches the output crs
                // dimension
                if (gridOrigin.length != type.getOriginArrayLength())
                    throw new WcsException("Grid origin size (" + gridOrigin.length
                            + ") inconsistent with grid type " + type.getXmlConstant()
                            + " that requires (" + type.getOriginArrayLength() + ")",
                            WcsExceptionCode.InvalidParameterValue, "GridOrigin");
                gridCRS.setGridOrigin(gridOrigin);
            }

            // perform same checks on the offsets
            Double[] gridOffsets = (Double[]) gridCRS.getGridOffsets();
            if (gridOffsets != null) {
                // make sure the origin dimension matches the grid type
                if (type.getOffsetArrayLength() != gridOffsets.length)
                    throw new WcsException("Invalid offsets lenght, grid type "
                            + type.getXmlConstant() + " requires " + type.getOffsetArrayLength(),
                            InvalidParameterValue, "GridOffsets");
            } else {
                gridCRS.setGridOffsets(null);
            }
        }
    }

    /**
     * Extracts only the final part of an EPSG code allowing for a specification
     * independent comparison (that is, it removes the EPSG:, urn:xxx:,
     * http://... prefixes)
     * 
     * @param srsName
     * @return
     */
    private String extractCode(String srsName) {
        if (srsName.startsWith("http://www.opengis.net/gml/srs/epsg.xml#"))
            return srsName.substring(40);
        else if (srsName.startsWith("urn:"))
            return srsName.substring(srsName.lastIndexOf(':') + 1);
        else if (srsName.startsWith("EPSG:"))
            return srsName.substring(5);
        else
            return srsName;
    }

    /**
     * Checks if the supported format string list contains the specified format,
     * doing a case insensitive search. If found the declared output format name
     * is returned, otherwise null is returned.
     * 
     * @param supportedFormats
     * @param format
     * @return
     */
    private String getDeclaredFormat(List supportedFormats, String format) {
        // supported formats may be setup using old style formats, first scan
        // the
        // configured list
        for (Iterator it = supportedFormats.iterator(); it.hasNext();) {
            String sf = (String) it.next();
            if (sf.equalsIgnoreCase(format)) {
                return sf;
            } else {
                CoverageResponseDelegate delegate = CoverageResponseDelegateFactory.encoderFor(sf);
                if (delegate != null && delegate.canProduce(format))
                    return sf;
            }
        }
        return null;
    }

    /**
     * Checks that the elements of the RangeSubset part of the request do make
     * sense by comparing them to the coverage metadata
     * 
     * @param info
     * @param rangeSubset
     */
    private void checkRangeSubset(CoverageInfo info, RangeSubsetType rangeSubset) {
        // quick escape if no range subset has been specified (it's legal)
        if (rangeSubset == null)
            return;

        if (rangeSubset.getFieldSubset().size() > 1) {
            throw new WcsException("Multi field coverages are not supported yet",
                    InvalidParameterValue, "RangeSubset");
        }

        // check field identifier
        FieldSubsetType field = (FieldSubsetType) rangeSubset.getFieldSubset().get(0);
        final String fieldId = field.getIdentifier().getValue();
        if (!fieldId.equalsIgnoreCase("contents"))
            throw new WcsException("Unknown field " + fieldId, InvalidParameterValue, "RangeSubset");

        // check interpolation
        String interpolation = field.getInterpolationType();
        if (interpolation != null) {
            boolean interpolationSupported = false;

            if (interpolation.equalsIgnoreCase("nearest")) {
                interpolation = "nearest neighbor";
            }
            for (Iterator it = info.getInterpolationMethods().iterator(); it.hasNext();) {
                String method = (String) it.next();
                if (interpolation.equalsIgnoreCase(method)) {
                    interpolationSupported = true;
                }
            }

            if (!interpolationSupported)
                throw new WcsException(
                        "The requested Interpolation method is not supported by this Coverage.",
                        InvalidParameterValue, "RangeSubset");
        }

        // check axis
        if (field.getAxisSubset().size() > 1) {
            throw new WcsException("Multi axis coverages are not supported yet",
                    InvalidParameterValue, "RangeSubset");
        } else if (field.getAxisSubset().size() == 0)
            return;

        AxisSubsetType axisSubset = (AxisSubsetType) field.getAxisSubset().get(0);
        final String axisId = axisSubset.getIdentifier();
        if (!axisId.equalsIgnoreCase("Bands"))
            throw new WcsException("Unknown axis " + axisId + " in field " + fieldId,
                    InvalidParameterValue, "RangeSubset");

        // prepare a support structure to quickly get the band index of a key
        // (and remember we replaced spaces with underscores in the keys to
        // avoid issues
        // with the kvp parsing of indentifiers that include spaces)
        List<CoverageDimensionInfo> dimensions = info.getDimensions();
        Set<String> dimensionMap = new HashSet<String>();
        for (int i = 0; i < dimensions.size(); i++) {
            String keyName = dimensions.get(i).getName().replace(' ', '_');
            dimensionMap.add(keyName);
        }

        // check keys
        List keys = axisSubset.getKey();
        int[] bands = new int[keys.size()];
        for (int j = 0; j < bands.length; j++) {
            final String key = (String) keys.get(j);
            String parsedKey = null;
            for (String dimensionName : dimensionMap) {
                if (dimensionName.equalsIgnoreCase(key)) {
                    parsedKey = dimensionName;
                    break;
                }
            }
            if (parsedKey == null)
                throw new WcsException("Unknown field/axis/key combination " + fieldId + "/"
                        + axisSubset.getIdentifier() + "/" + key, InvalidParameterValue,
                        "RangeSubset");
            else
                keys.set(j, parsedKey);
        }
    }
}
