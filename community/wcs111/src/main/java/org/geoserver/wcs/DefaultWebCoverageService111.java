package org.geoserver.wcs;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import net.opengis.ows.v1_1_0.BoundingBoxType;
import net.opengis.wcs.v1_1_1.AxisSubsetType;
import net.opengis.wcs.v1_1_1.DescribeCoverageType;
import net.opengis.wcs.v1_1_1.FieldSubsetType;
import net.opengis.wcs.v1_1_1.GetCapabilitiesType;
import net.opengis.wcs.v1_1_1.GetCoverageType;
import net.opengis.wcs.v1_1_1.GridCrsType;
import net.opengis.wcs.v1_1_1.OutputType;
import net.opengis.wcs.v1_1_1.RangeSubsetType;

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
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.vfny.geoserver.global.CoverageDimension;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

public class DefaultWebCoverageService111 implements WebCoverageService111 {
    Logger LOGGER = Logging.getLogger(DefaultWebCoverageService.class);

    private final static Hints HINTS = new Hints(new HashMap());
    static {
        HINTS.add(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        HINTS.add(new Hints(Hints.OVERVIEW_POLICY, Hints.VALUE_OVERVIEW_POLICY_IGNORE));
    }

    private WCS wcs;

    private Data catalog;

    public DefaultWebCoverageService111(WCS wcs, Data catalog) {
        this.wcs = wcs;
        this.catalog = catalog;
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
        String version = RequestUtils.getVersionPreOws(provided, accepted);

        // TODO: add support for 1.0.0 in here

        if ("1.1.0".equals(version) || "1.1.1".equals(version)) {
            return new WCSCapsTransformer(wcs, catalog);
        }

        throw new WcsException("Could not understand version:" + version);
    }

    public DescribeCoverageTransformer describeCoverage(DescribeCoverageType request) {
        final String version = request.getVersion();
        if ("1.1.0".equals(version) || "1.1.1".equals(version)) {
            return new DescribeCoverageTransformer(wcs, catalog);
        }

        throw new WcsException("Could not understand version:" + version);
    }

    public GridCoverage[] getCoverage(GetCoverageType request) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(new StringBuffer("execute CoverageRequest response. Called request is: ")
                    .append(request).toString());
        }

        CoverageInfo meta = null;
        GridCoverage2D coverage = null;
        try {
            meta = catalog.getCoverageInfo(request.getIdentifier().getValue());

            // first let's run some sanity checks on the inputs
            checkRangeSubset(meta, request.getRangeSubset());
            checkOutput(meta, request.getOutput());

            // grab the format, the reader using the default params,
            final Format format = meta.getFormatInfo().getFormat();
            final AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) meta
                    .createReader(HINTS);
            final ParameterValueGroup params = reader.getFormat().getReadParameters();

            // handle spatial domain subset, if needed
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
            final BoundingBoxType bbox = request.getDomainSubset().getBoundingBox();
            final CoordinateReferenceSystem nativeCRS = originalEnvelope
                    .getCoordinateReferenceSystem();
            final GeneralEnvelope destinationEnvelopeInSourceCRS;
            final GeneralEnvelope destinationEnvelope;
            if (bbox != null) {
                // first off, parse the envelope corners
                double[] lowerCorner = new double[bbox.getLowerCorner().size()];
                double[] upperCorner = new double[bbox.getUpperCorner().size()];
                for (int i = 0; i < lowerCorner.length; i++) {
                    lowerCorner[i] = (Double) bbox.getLowerCorner().get(i);
                    upperCorner[i] = (Double) bbox.getUpperCorner().get(i);
                }
                destinationEnvelope = new GeneralEnvelope(lowerCorner, upperCorner);
                // grab the native crs
                // if no crs has beens specified, the native one is assumed
                if (bbox.getCrs() == null) {
                    destinationEnvelope.setCoordinateReferenceSystem(nativeCRS);
                    destinationEnvelopeInSourceCRS = destinationEnvelope;
                } else {
                    // otherwise we need to transform
                    final CoordinateReferenceSystem bboxCRS = CRS.decode(bbox.getCrs());
                    destinationEnvelope.setCoordinateReferenceSystem(bboxCRS);
                    final MathTransform bboxToNativeTx = CRS.findMathTransform(bboxCRS, nativeCRS,
                            true);
                    destinationEnvelopeInSourceCRS = CRS.transform(bboxToNativeTx,
                            destinationEnvelope);
                    destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(nativeCRS);
                }
            } else {
                destinationEnvelopeInSourceCRS = reader.getOriginalEnvelope();
                destinationEnvelope = destinationEnvelopeInSourceCRS;
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
                double[] origin = (double[]) gridCRS.getGridOrigin();
                double[] offsets = (double[]) gridCRS.getGridOffsets();

                // from the specification if grid origin is omitted and the crs
                // is 2d the default it's 0,0
                if (origin == null) {
                    origin = new double[] { 0, 0 };
                }

                // if no offsets has been specified we try to default on the
                // native ones
                if (offsets == null) {
                    if (!(gridToCRS instanceof AffineTransform2D)
                            && !(gridToCRS instanceof IdentityTransform))
                        throw new WcsException(
                                "Internal error, the coverage we're playing with does not have an affine transform...");

                    if (gridToCRS instanceof IdentityTransform) {
                        if (gridCRS.getGridType().equals(GridType.GT2dSimpleGrid))
                            offsets = new double[] { 1, 1 };
                        else
                            offsets = new double[] { 1, 0, 0, 1 };
                    } else {
                        AffineTransform2D affine = (AffineTransform2D) gridToCRS;
                        if (gridCRS.getGridType().equals(GridType.GT2dSimpleGrid))
                            offsets = new double[] { affine.getScaleX(), affine.getScaleY() };
                        else
                            offsets = new double[] { affine.getScaleX(), affine.getShearX(),
                                    affine.getShearY(), affine.getScaleY() };
                    }
                }

                // building the actual transform for the resulting grid geometry
                AffineTransform tx;
                if (gridCRS.getGridType().equals(GridType.GT2dSimpleGrid)) {
                    tx = new AffineTransform(offsets[0], 0, 0, offsets[1], origin[0], origin[1]);
                } else {
                    tx = new AffineTransform(offsets[0], offsets[2], offsets[1], offsets[3],
                            origin[0], origin[1]);
                }
                gridToCRS = new AffineTransform2D(tx);
            }

            // now we have enough info to read the coverage, grab the parameters
            // and add the grid geometry info
            final Map parameters = CoverageUtils.getParametersKVP(reader.getFormat()
                    .getReadParameters());
            final GeneralEnvelope intersected = new GeneralEnvelope(destinationEnvelopeInSourceCRS);
            intersected.intersect(originalEnvelope);
            final GridGeometry2D destinationGridGeometry = new GridGeometry2D(gridToCRS,
                    intersected);
            parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),
                    destinationGridGeometry);
            coverage = (GridCoverage2D) reader.read(CoverageUtils.getParameters(reader.getFormat()
                    .getReadParameters(), parameters, true));
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

                FieldSubsetType field = (FieldSubsetType) request.getRangeSubset().getFieldSubset()
                        .get(0);
                if (field.getAxisSubset().size() > 1) {
                    throw new WcsException("Multi axis coverages are not supported yet");
                }
                interpolationType = field.getInterpolationType();

                // prepare a support structure to quikly get the band index of a
                // key
                CoverageDimension[] dimensions = meta.getDimensions();
                Map<String, Integer> dimensionMap = new HashMap<String, Integer>();
                for (int i = 0; i < dimensions.length; i++) {
                    String keyName = dimensions[i].getName().replace(' ', '_');
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
                    bandSelectedCoverage = (GridCoverage2D) WCSUtils.bandSelect(coverage, bands);
                } catch (WcsException e) {
                    throw new WcsException(e.getLocalizedMessage());
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
             * Reproject
             */
            final GridCoverage2D reprojectedCoverage = WCSUtils.reproject(scaledCoverage,
                    nativeCRS, targetCRS, interpolation);

            return new GridCoverage[] { reprojectedCoverage };
        } catch (Exception e) {
            if (e instanceof WcsException)
                throw (WcsException) e;
            else
                throw new WcsException(e);
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
        String format = output.getFormat();
        String declaredFormat = getDeclaredFormat(meta.getSupportedFormats(), format);
        if (declaredFormat == null)
            throw new WcsException("format " + format + " is not supported for this coverage",
                    InvalidParameterValue, "format");

        // check grid base crs is valid, and eventually default it out
        String gridBaseCrs = output.getGridCRS().getGridBaseCRS();
        if (gridBaseCrs != null) {
            // make sure the requested is among the supported ones, by making a
            // code level
            // comparison (to avoid assuming epsg:xxxx and
            // http://www.opengis.net/gml/srs/epsg.xml#xxx are different ones.
            // We'll also consider the urn one comparable, allowing eventual
            // axis flip on the
            // geographic crs
            String actualCRS = null;
            final String gridBaseCrsCode = extractCode(gridBaseCrs);
            for (Iterator it = meta.getResponseCRSs().iterator(); it.hasNext();) {
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
            output.getGridCRS().setGridBaseCRS(gridBaseCrs);
        } else {
            String code = GML2EncodingUtils.epsgCode(meta.getCrs());
            output.getGridCRS().setGridBaseCRS("urn:x-ogc:def:crs:EPSG:" + code);
        }

        // check grid type makes sense and apply default otherwise
        String gridTypeValue = output.getGridCRS().getGridType();
        GridType type = GridType.GT2dGridIn2dCrs;
        if (gridTypeValue != null) {
            type = null;
            for (GridType gt : GridType.values()) {
                if (gt.getXmlConstant().equalsIgnoreCase(gridTypeValue))
                    type = gt;
            }
            if (type == null)
                throw new WcsException("Unknown grid type " + gridTypeValue, InvalidParameterValue,
                        "GridType");
            else if (type == GridType.GT2dGridIn3dCrs)
                throw new WcsException("Unsupported grid type " + gridTypeValue,
                        InvalidParameterValue, "GridType");
        }
        output.getGridCRS().setGridType(type.getXmlConstant());

        // check gridcs and apply only value we know about
        String gridCS = output.getGridCRS().getGridCS();
        if (gridCS != null) {
            if (!gridCS.equalsIgnoreCase(GridCS.GCSGrid2dSquare.getXmlConstant()))
                throw new WcsException("Unsupported grid cs " + gridCS, InvalidParameterValue,
                        "GridCS");
        }
        output.getGridCRS().setGridCS(GridCS.GCSGrid2dSquare.getXmlConstant());

        // check the grid origin and set defaults
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(output.getGridCRS().getGridBaseCRS());
        } catch (Exception e) {
            throw new WcsException("Could not understand crs "
                    + output.getGridCRS().getGridBaseCRS(), WcsExceptionCode.InvalidParameterValue,
                    "GridBaseCRS");
        }
        Double[] gridOrigin = (Double[]) output.getGridCRS().getGridOrigin();
        if (gridOrigin != null) {
            // make sure the origin dimension matches the output crs dimension
            if (gridOrigin.length != type.getOriginArrayLength())
                throw new WcsException("Grid origin size (" + gridOrigin.length
                        + ") inconsistent with grid type " + type.getXmlConstant()
                        + " that requires (" + type.getOriginArrayLength() + ")",
                        WcsExceptionCode.InvalidParameterValue, "GridOrigin");
            output.getGridCRS().setGridOrigin(gridOrigin);
        } else {
            output.getGridCRS().setGridOrigin(null);
        }

        // perform same checks on the offsets
        Double[] gridOffsets = (Double[]) output.getGridCRS().getGridOffsets();
        if (gridOffsets != null) {
            // make sure the origin dimension matches the grid type
            if (type.getOffsetArrayLength() != gridOffsets.length)
                throw new WcsException("Invalid offsets lenght, grid type " + type.getXmlConstant()
                        + " requires " + type.getOffsetArrayLength(), InvalidParameterValue,
                        "GridOffsets");
        } else {
            output.getGridCRS().setGridOffsets(null);
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
        for (Iterator it = supportedFormats.iterator(); it.hasNext();) {
            String sf = (String) it.next();
            if (sf.equalsIgnoreCase(format))
                return sf;
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
        if (!fieldId.equalsIgnoreCase(info.getLabel()))
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
        CoverageDimension[] dimensions = info.getDimensions();
        Set<String> dimensionMap = new HashSet<String>();
        for (int i = 0; i < dimensions.length; i++) {
            String keyName = dimensions[i].getName().replace(' ', '_');
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
