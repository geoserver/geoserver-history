package org.geoserver.wcs;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

import org.geoserver.data.util.CoverageUtils;
import org.geoserver.ows.util.CapabilitiesUtils;
import org.geoserver.wcs.kvp.GridType;
import org.geoserver.wcs.response.DescribeCoverageTransformer;
import org.geoserver.wcs.response.WCSCapsTransformer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
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
        String version = CapabilitiesUtils.getVersion(provided, accepted);

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
            
            // grab the format, the reader using the default params,
            final Format format = meta.getFormatInfo().getFormat();
            final AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) meta.createReader(HINTS);
            final ParameterValueGroup params = reader.getFormat().getReadParameters();
            
            // handle spatial domain subset, if needed
            final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
            final BoundingBoxType bbox = request.getDomainSubset().getBoundingBox();
            final CoordinateReferenceSystem nativeCRS = originalEnvelope.getCoordinateReferenceSystem();
            final GeneralEnvelope destinationEnvelopeInSourceCRS;
            final GeneralEnvelope destinationEnvelope;
            if(bbox != null) {
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
                if(bbox.getCrs() == null) {
                    destinationEnvelope.setCoordinateReferenceSystem(nativeCRS);
                    destinationEnvelopeInSourceCRS = destinationEnvelope;
                } else {
                    // otherwise we need to transform
                    final CoordinateReferenceSystem bboxCRS = CRS.decode(bbox.getCrs());
                    destinationEnvelope.setCoordinateReferenceSystem(bboxCRS);
                    final MathTransform bboxToNativeTx = CRS.findMathTransform(bboxCRS, nativeCRS, true);
                    destinationEnvelopeInSourceCRS = CRS.transform(bboxToNativeTx, destinationEnvelope);
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
            if(gridCRS == null)
                targetCRS = reader.getOriginalEnvelope().getCoordinateReferenceSystem();
            else
                targetCRS = CRS.decode(gridCRS.getGridBaseCRS());

            // grab the grid to world transformation
            MathTransform gridToCRS = reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
            if(gridCRS != null) {
                double[] origin = (double[]) gridCRS.getGridOrigin();
                double[] offsets = (double[]) gridCRS.getGridOffsets();

                // from the specification if grid origin is omitted and the crs is 2d the default it's 0,0
                if(origin == null) {
                    origin = new double[] {0, 0};
                }
                
                // if no offsets has been specified we try to default on the native ones
                if(offsets == null) {
                    if(!(gridToCRS instanceof AffineTransform2D) && !(gridToCRS instanceof IdentityTransform))
                        throw new WcsException("Internal error, the coverage we're playing with does not have an affine transform...");
                    
                    if(gridToCRS instanceof IdentityTransform) {
                        if(gridCRS.getGridType().equals(GridType.GT2dSimpleGrid))
                            offsets = new double[] {1, 1};
                        else
                            offsets = new double[] {1, 0, 0 ,1};
                    } else {
                        AffineTransform2D affine = (AffineTransform2D) gridToCRS;
                        if(gridCRS.getGridType().equals(GridType.GT2dSimpleGrid))
                            offsets = new double[] {affine.getScaleX(), affine.getScaleY()};
                        else
                            offsets = new double[] {affine.getScaleX(), affine.getShearX(), affine.getShearY(), affine.getScaleY()};
                    }
                }
                    
                // building the actual transform for the resulting grid geometry
                AffineTransform tx; 
                if(gridCRS.getGridType().equals(GridType.GT2dSimpleGrid)) {
                    tx = new AffineTransform(offsets[0], 0, 0, offsets[1], origin[0], origin[1]);
                } else {
                    tx = new AffineTransform(offsets[0], offsets[2], offsets[1], offsets[3], origin[0], origin[1]);
                }
                gridToCRS = new AffineTransform2D(tx);
            } 
            
            // now we have enough info to read the coverage, grab the parameters
            // and add the grid geometry info
            final Map parameters = CoverageUtils.getParametersKVP(reader.getFormat().getReadParameters());
            final GeneralEnvelope intersected = new GeneralEnvelope(destinationEnvelopeInSourceCRS);
            intersected.intersect(originalEnvelope);
            final GridGeometry2D destinationGridGeometry = new GridGeometry2D(gridToCRS, intersected);
            parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(), destinationGridGeometry);
            coverage = (GridCoverage2D) reader.read(CoverageUtils.getParameters(
                    reader.getFormat().getReadParameters(), parameters, true));
            if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
                throw new IOException("The requested coverage could not be found.");
            }
            
            /**
             * Band Select (works on just one field)
             */
            GridCoverage2D bandSelectedCoverage = coverage;
            String interpolationType = null;
            if(request.getRangeSubset() != null) {
                if(request.getRangeSubset().getFieldSubset().size() > 1) {
                    throw new WcsException("Multi field coverages are not supported yet");
                }
                    
                FieldSubsetType field = (FieldSubsetType) request.getRangeSubset().getFieldSubset().get(0);
                if(field.getAxisSubset().size() > 1) {
                    throw new WcsException("Multi axis coverages are not supported yet");
                }
                interpolationType = field.getInterpolationType();
                
                // prepare a support structure to quikly get the band index of a key
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
                    if(index == null)
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
                boolean interpolationSupported = false;
                Iterator internal = meta.getInterpolationMethods().iterator();

                while (internal.hasNext()) {
                    if (interpolationType.equalsIgnoreCase((String) internal.next())) {
                        interpolationSupported = true;
                    }
                }

                if (!interpolationSupported) {
                    throw new WcsException(
                            "The requested Interpolation method is not supported by this Coverage.");
                } else {
                    if (interpolationType.equalsIgnoreCase("bilinear")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
                    } else if (interpolationType.equalsIgnoreCase("bicubic")) {
                        interpolation = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
                    }
                }
            }

            /**
             * Crop
             */
            final GridCoverage2D croppedGridCoverage = WCSUtils.crop(bandSelectedCoverage,
                    (GeneralEnvelope) coverage.getEnvelope(), nativeCRS, destinationEnvelopeInSourceCRS,
                    Boolean.TRUE);

            /**
             * Scale
             */
            final GridCoverage2D scaledCoverage = WCSUtils.scale(croppedGridCoverage, destinationGridGeometry);

            /**
             * Reproject
             */
            final GridCoverage2D reprojectedCoverage = WCSUtils.reproject(scaledCoverage, 
                    nativeCRS, targetCRS, interpolation);
            
            return new GridCoverage[] {reprojectedCoverage};
        } catch (Exception e) {
            throw new WcsException(e);
        }
        
        
    }
}
