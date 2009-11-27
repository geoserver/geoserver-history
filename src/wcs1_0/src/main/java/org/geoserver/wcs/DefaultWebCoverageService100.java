/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wcs;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import net.opengis.gml.GridType;
import net.opengis.wcs10.AxisSubsetType;
import net.opengis.wcs10.DescribeCoverageType;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.GetCapabilitiesType;
import net.opengis.wcs10.GetCoverageType;
import net.opengis.wcs10.InterpolationMethodType;
import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.TypedLiteralType;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.data.util.CoverageUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.wcs.response.Wcs10CapsTransformer;
import org.geoserver.wcs.response.Wcs10DescribeCoverageTransformer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
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

/**
 * The Default WCS 1.0.0 Service implementation
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public class DefaultWebCoverageService100 implements WebCoverageService100 {
    Logger LOGGER = Logging.getLogger(DefaultWebCoverageService100.class);

    private final static Hints HINTS = new Hints(new HashMap());
    static {
        HINTS.add(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
    }

    private WCSInfo wcs;

    private Catalog catalog;

    private GeoServer geoServer;

    /**
     * 
     * @param geoServer
     */
    public DefaultWebCoverageService100(GeoServer geoServer) {
        this.wcs = geoServer.getService(WCSInfo.class);
        this.geoServer = geoServer;
        this.catalog = geoServer.getCatalog();
    }

    /**
     * 
     */
    public WCSInfo getServiceInfo() {
        return wcs;
    }

    /**
     * 
     */
    public Wcs10CapsTransformer getCapabilities(GetCapabilitiesType request) {
        // do the version negotiation dance
        List<String> provided = new ArrayList<String>();
        provided.add("1.0.0");
        List<String> accepted = null;
        if (request.getVersion() != null) {
            accepted = new ArrayList<String>();
            accepted.add(request.getVersion());
        }
        String version = RequestUtils.getVersionPreOws(provided, accepted);

        if ("1.0.0".equals(version)) {
            Wcs10CapsTransformer capsTransformer = new Wcs10CapsTransformer(geoServer);
            capsTransformer.setEncoding(Charset.forName((wcs.getGeoServer().getGlobal()
                    .getCharset())));
            return capsTransformer;
        }

        throw new WcsException("Could not understand version:" + version);
    }

    /**
     * 
     */
    public Wcs10DescribeCoverageTransformer describeCoverage(DescribeCoverageType request) {
        final String version = request.getVersion();
        if ("1.0.0".equals(version)) {
            Wcs10DescribeCoverageTransformer describeTransformer = new Wcs10DescribeCoverageTransformer(
                    wcs, catalog);
            describeTransformer.setEncoding(Charset.forName((wcs.getGeoServer().getGlobal()
                    .getCharset())));
            return describeTransformer;
        }

        throw new WcsException("Could not understand version:" + version);
    }

    /**
     * 
     */
    public GridCoverage[] getCoverage(GetCoverageType request) {
        CoverageInfo meta = null;
        GridCoverage2D coverage = null;
        List<GridCoverage> coverageResults = new ArrayList<GridCoverage>();
        try {
            String coverageName = request.getSourceCoverage().indexOf("@") > 0 ? request
                    .getSourceCoverage().substring(0, request.getSourceCoverage().indexOf("@"))
                    : request.getSourceCoverage();
            String fieldName = request.getSourceCoverage().indexOf("@") > 0 ? request
                    .getSourceCoverage().substring(request.getSourceCoverage().indexOf("@") + 1)
                    : null;

            // stripping namespace
            coverageName = coverageName.contains(":") ? coverageName.substring(coverageName
                    .indexOf(":") + 1) : coverageName;

            meta = catalog.getCoverageByName(request.getSourceCoverage());

            // first let's run some sanity checks on the inputs
            checkDomainSubset(meta, request.getDomainSubset());
            checkRangeSubset(meta, request.getRangeSubset());
            checkInterpolationMethod(meta, request.getInterpolationMethod());
            checkOutput(meta, request.getOutput());

            // grab the format, the reader using the default params,
            final Format format = meta.getStore().getFormat();
            final AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) meta
                    .getGridCoverageReader(null, HINTS);
            final ParameterValueGroup params = reader.getFormat().getReadParameters();

            if (reader != null) {
                // handle spatial domain subset, if needed
                final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
                ReferencedEnvelope destinationEnvelope = null;

                Object requestedEnvelope = request.getDomainSubset().getSpatialSubset()
                        .getEnvelope().get(0);
                if (requestedEnvelope instanceof ReferencedEnvelope)
                    destinationEnvelope = (ReferencedEnvelope) requestedEnvelope;
                else if (requestedEnvelope instanceof GeneralEnvelope)
                    destinationEnvelope = new ReferencedEnvelope(
                            (GeneralEnvelope) requestedEnvelope);

                final CoordinateReferenceSystem nativeCRS = originalEnvelope
                        .getCoordinateReferenceSystem();
                final GeneralEnvelope destinationEnvelopeInSourceCRS;
                if (destinationEnvelope != null) {
                    // grab the native crs
                    // if no crs has beens specified, the native one is assumed
                    if (destinationEnvelope.getCoordinateReferenceSystem() == null) {
                        destinationEnvelope = new ReferencedEnvelope(destinationEnvelope, nativeCRS);
                        destinationEnvelopeInSourceCRS = new GeneralEnvelope(destinationEnvelope);
                        destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(nativeCRS);
                    } else {
                        // otherwise we need to transform
                        final CoordinateReferenceSystem bboxCRS = destinationEnvelope
                                .getCoordinateReferenceSystem();
                        final MathTransform bboxToNativeTx = CRS.findMathTransform(bboxCRS,
                                nativeCRS, true);
                        destinationEnvelopeInSourceCRS = CRS.transform(bboxToNativeTx,
                                destinationEnvelope);
                        destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(nativeCRS);
                    }
                } else {
                    destinationEnvelopeInSourceCRS = reader.getOriginalEnvelope();
                    destinationEnvelope = new ReferencedEnvelope(destinationEnvelopeInSourceCRS);
                }

                String gridCRS = null;
                if (request.getOutput().getCrs() != null)
                    gridCRS = request.getOutput().getCrs().getValue();

                // Compute the target crs, the crs that the final coverage will be served into
                final CoordinateReferenceSystem targetCRS;
                if (gridCRS == null) {
                    targetCRS = reader.getOriginalEnvelope().getCoordinateReferenceSystem();
                    gridCRS = CRS.lookupIdentifier(targetCRS, false);
                } else
                    targetCRS = CRS.decode(gridCRS);

                // grab the grid to world transformation
                /**
                 * Reading Coverage on Requested Envelope
                 */
                Rectangle destinationSize = null;
                MathTransform gridToCRS = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
                if (gridCRS != null
                        && request.getDomainSubset().getSpatialSubset().getGrid().size() > 0) {
                    GridType grid = (GridType) request.getDomainSubset().getSpatialSubset()
                            .getGrid().get(0);
                    int[] lowers = new int[] { (int) grid.getLimits().getMinX(),
                            (int) grid.getLimits().getMinY() };
                    int[] highers = new int[] { (int) grid.getLimits().getMaxX(),
                            (int) grid.getLimits().getMaxY() };

                    // if no offsets has been specified we try to default on the native ones
                    if (!(gridToCRS instanceof AffineTransform2D)
                            && !(gridToCRS instanceof IdentityTransform))
                        throw new WcsException(
                                "Internal error, the coverage we're playing with does not have an affine transform...");

                    if (gridToCRS instanceof IdentityTransform) {
                        if (grid.getDimension().intValue() == 2)
                            highers = new int[] { 1, 1 };
                    }

                    destinationSize = new Rectangle(lowers[0], lowers[1], highers[0], highers[1]);
                }

                // now we have enough info to read the coverage, grab the parameters and add the
                // grid geometry info
                final Map parameters = CoverageUtils.getParametersKVP(reader.getFormat()
                        .getReadParameters());
                final GeneralEnvelope intersected = new GeneralEnvelope(
                        destinationEnvelopeInSourceCRS);
                intersected.intersect(originalEnvelope);

                // final GridGeometry2D destinationGridGeometry = new GridGeometry2D(new
                // GeneralGridRange(destinationSize), destinationEnvelopeInSourceCRS);
                final GridGeometry2D destinationGridGeometry = new GridGeometry2D(
                        PixelInCell.CELL_CENTER, gridToCRS, intersected, null);

                parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),
                        destinationGridGeometry);

                coverage = (GridCoverage2D) reader.read(CoverageUtils.getParameters(reader
                        .getFormat().getReadParameters(), parameters, true));
                if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
                    throw new IOException("The requested coverage could not be found.");
                }

                /**
                 * Band Select (works on just one field)
                 */
                GridCoverage2D bandSelectedCoverage = coverage;
                // ImageIOUtilities.visualize(coverage.getRenderedImage());
                String interpolationType = null;
                if (request.getRangeSubset() != null) {
                    if (request.getRangeSubset().getAxisSubset().size() > 1) {
                        throw new WcsException("Multi field coverages are not supported yet");
                    }

                    interpolationType = request.getInterpolationMethod().getLiteral();

                    // extract the band indexes
                    List axisSubset = request.getRangeSubset().getAxisSubset();
                    if (axisSubset.size() > 0) {
                        AxisSubsetType axis = (AxisSubsetType) axisSubset.get(0);

                        try {
                            String axisName = axis.getName();

                            int[] bands = null;
                            if (axisName.equals("Band") && axis.getSingleValue().size() > 0) {
                                bands = new int[axis.getSingleValue().size()];
                                for (int s = 0; s < axis.getSingleValue().size(); s++) {
                                    bands[s] = Integer.parseInt(((TypedLiteralType) axis
                                            .getSingleValue().get(s)).getValue()) - 1;
                                }
                            } else if (axis.getInterval().size() > 0) {
                                IntervalType interval = (IntervalType) axis.getInterval().get(0);
                                int min = Integer.parseInt(interval.getMin().getValue());
                                int max = Integer.parseInt(interval.getMax().getValue());
                                int res = (interval.getRes() != null ? Integer.parseInt(interval
                                        .getRes().getValue()) : 1);

                                bands = new int[(int) (Math.floor(max - min) / res + 1)];
                                for (int b = 0; b < bands.length; b++)
                                    bands[b] = (min + b * res) - 1;
                            }

                            // finally execute the band select
                            bandSelectedCoverage = (GridCoverage2D) WCSUtils.bandSelect(coverage,
                                    bands);
                        } catch (Exception e) {
                            // Warning: Axis not found!!!
                            throw new WcsException("Band Select Operation: "
                                    + e.getLocalizedMessage());
                        }
                    }
                }

                /**
                 * Checking for supported Interpolation Methods
                 */
                Interpolation interpolation = Interpolation
                        .getInstance(Interpolation.INTERP_NEAREST);
                if (interpolationType != null) {
                    if (interpolationType.equalsIgnoreCase("bilinear")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
                    } else if (interpolationType.equalsIgnoreCase("bicubic")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
                    } else if (interpolationType.equalsIgnoreCase("nearest neighbor")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
                    }
                }

                /**
                 * Crop
                 */
                final GridCoverage2D croppedGridCoverage = WCSUtils.crop(bandSelectedCoverage,
                        (GeneralEnvelope) coverage.getEnvelope(), nativeCRS,
                        destinationEnvelopeInSourceCRS, Boolean.TRUE);

                /**
                 * Scale
                 */
                final GridCoverage2D scaledCoverage = WCSUtils.scale(croppedGridCoverage,
                        destinationGridGeometry);

                /**
                 * Re-project
                 */
                final GridCoverage2D reprojectedCoverage = WCSUtils.reproject(scaledCoverage,
                        nativeCRS, targetCRS, interpolation);

                coverageResults.add(reprojectedCoverage);
            }

            return coverageResults.toArray(new GridCoverage2D[] {});
        } catch (Exception e) {
            if (e instanceof WcsException)
                throw (WcsException) e;
            else
                throw new WcsException(e);
        }

    }

    private static void checkDomainSubset(CoverageInfo meta, DomainSubsetType domainSubset)
            throws Exception {
        ReferencedEnvelope bbox = null;
        Object requestedEnvelope = domainSubset.getSpatialSubset().getEnvelope().get(0);
        if (requestedEnvelope instanceof ReferencedEnvelope) {
            bbox = (ReferencedEnvelope) requestedEnvelope;
        } else if (requestedEnvelope instanceof GeneralEnvelope) {
            bbox = new ReferencedEnvelope((GeneralEnvelope) requestedEnvelope);
        }

        CoordinateReferenceSystem bboxCRs = bbox.getCoordinateReferenceSystem();
        // TODO: FIX THIS!!!
        Envelope gridEnvelope = /* meta.getCoverage().getEnvelope() */null;
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
        double[] lower = bbox.getLowerCorner().getCoordinate();
        double[] upper = bbox.getUpperCorner().getCoordinate();
        for (int i = 0; i < lower.length; i++) {
            if (lower[i] > upper[i]) {
                final CoordinateSystemAxis axis = bboxCRs.getCoordinateSystem().getAxis(i);
                // see if the coordinates can be fixed
                if (bboxCRs instanceof GeographicCRS && axis.getDirection() == AxisDirection.EAST) {

                    if (gridEnvelopeBboxCRS != null) {
                        // try to guess which one needs to be fixed
                        final double envMax = gridEnvelopeBboxCRS.getMaximum(i);
                        if (envMax >= lower[i])
                            upper[i] = upper[i] + (axis.getMaximumValue() - axis.getMinimumValue());
                        else
                            lower[i] = lower[i] - (axis.getMaximumValue() - axis.getMinimumValue());

                    } else {
                        // just fix the upper and hope...
                        upper[i] = upper[i] + (axis.getMaximumValue() - axis.getMinimumValue());
                    }
                }

                // if even after the fix we're in the wrong situation, complain
                if (lower[i] > upper[i]) {
                    throw new WcsException(
                            "illegal bbox, min of dimension " + (i + 1) + ": " + lower[i] + " is "
                                    + "greater than max of same dimension: " + upper[i],
                            WcsExceptionCode.InvalidParameterValue, "BoundingBox");
                }
            }

        }
    }

    private static void checkInterpolationMethod(CoverageInfo info,
            InterpolationMethodType interpolationMethod) {
        // check interpolation method
        String interpolation = interpolationMethod.getLiteral();
        if (interpolation != null) {
            boolean interpolationSupported = false;

            if (interpolation.startsWith("nearest")) {
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
    }

    /**
     * Checks that the elements of the Output part of the request do make sense by comparing them to
     * the coverage metadata
     * 
     * @param info
     * @param rangeSubset
     */
    private static void checkOutput(CoverageInfo meta, OutputType output) {
        if (output == null)
            return;

        // check output format
        String format = output.getFormat().getValue();
        String declaredFormat = getDeclaredFormat(meta.getSupportedFormats(), format);
        if (declaredFormat == null)
            throw new WcsException("format " + format + " is not supported for this coverage",
                    InvalidParameterValue, "format");

        // check requested CRS
        // if (output.getCrs() != null) {
        // String requestedCRS = output.getCrs().getValue();
        // if (getRequestResponseCRS(meta.getRequestCRSs(), requestedCRS) == null &&
        // getRequestResponseCRS(meta.getResponseCRSs(), requestedCRS) == null)
        // throw new WcsException("CRS " + requestedCRS + " is not supported for this coverage",
        // InvalidParameterValue, "CRS");
        // } else {
        // // The requested CRS was not specified ... what to do ???
        // }
    }

    /**
     * Extracts only the final part of an EPSG code allowing for a specification independent
     * comparison (that is, it removes the EPSG:, urn:xxx:, http://... prefixes)
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
     * Checks if the supported format string list contains the specified format, doing a case
     * insensitive search. If found the declared output format name is returned, otherwise null is
     * returned.
     * 
     * @param supportedFormats
     * @param format
     * @return
     */
    private static String getDeclaredFormat(List supportedFormats, String format) {
        // supported formats may be setup using old style formats, first scan
        // the configured list
        for (Iterator it = supportedFormats.iterator(); it.hasNext();) {
            String sf = (String) it.next();
            if (sf.equalsIgnoreCase(format.trim())) {
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
     * Checks if the request/response CRS list contains the requested output CRS, doing a case
     * insensitive search. If found the requested output CRS ID is returned, otherwise null is
     * returned.
     * 
     * @param requestResoinseCRSs
     * @param crs
     * @return
     */
    private String getRequestResponseCRS(List requestResoinseCRSs, String crs) {
        // supported formats may be setup using old style formats, first scan
        // the configured list
        for (Iterator it = requestResoinseCRSs.iterator(); it.hasNext();) {
            String reqResCRS = (String) it.next();
            if (reqResCRS.equalsIgnoreCase(crs)) {
                return reqResCRS;
            }
        }
        return null;
    }

    /**
     * Checks that the elements of the RangeSubset part of the request do make sense by comparing
     * them to the coverage metadata
     * 
     * @param info
     * @param rangeSubset
     */
    private static void checkRangeSubset(CoverageInfo info, RangeSubsetType rangeSubset) {
        // quick escape if no range subset has been specified (it's legal)
        if (rangeSubset == null)
            return;

        // check axis
        if (rangeSubset.getAxisSubset().size() > 1) {
            throw new WcsException("Multi axis coverages are not supported yet",
                    InvalidParameterValue, "RangeSubset");
        } else if (rangeSubset.getAxisSubset().size() == 0)
            return;

        AxisSubsetType axisSubset = (AxisSubsetType) rangeSubset.getAxisSubset().get(0);

        // prepare a support structure to quickly get the band index of a key
        // (and remember we replaced spaces with underscores in the keys to
        // avoid issues with the kvp parsing of indentifiers that include spaces)

        // check indexes
        int[] bands = null;
        if (axisSubset.getSingleValue().size() > 0) {
            bands = new int[1];
            bands[0] = Integer.parseInt(((TypedLiteralType) axisSubset.getSingleValue().get(0))
                    .getValue());
        } else if (axisSubset.getInterval().size() > 0) {
            IntervalType interval = (IntervalType) axisSubset.getInterval().get(0);
            int min = Integer.parseInt(interval.getMin().getValue());
            int max = Integer.parseInt(interval.getMax().getValue());
            int res = (interval.getRes() != null ? Integer.parseInt(interval.getRes().getValue())
                    : 1);

            bands = new int[(max - min) / res];
            for (int b = 0; b < bands.length; b++)
                bands[b] = min + (b * res);
        }

        if (bands == null)
            throw new WcsException("Invalid values for axis " + axisSubset.getName(),
                    InvalidParameterValue, "AxisSubset");
    }

}