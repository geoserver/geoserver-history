/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.data.util.CoverageUtils;
import org.geoserver.platform.ServiceException;
import org.geoserver.wcs.WCSInfo;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.util.WCSUtils;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;
import org.vfny.geoserver.wcs.requests.CoverageRequest;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class CoverageResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses");

    /**
     *
     */
    CoverageResponseDelegate delegate;

    /**
     * This is the request provided to the execute( Request ) method.
     *
     * <p>
     * We save it so we can access the handle provided by the user for error
     * reporting during the writeTo( OutputStream ) opperation.
     * </p>
     *
     * <p>
     * This value will be <code>null</code> until execute is called.
     * </p>
     *
     * @uml.property name="request"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private CoverageRequest request;

    /**
     * Empty constructor
     */
    public CoverageResponse() {
        request = null;
    }

    /**
     * Returns any extra headers that this service might want to set in the HTTP response object.
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer geoServer) {
        return delegate.getContentType();
    }

    public String getContentEncoding() {
        return delegate.getContentEncoding();
    }

    public String getContentDisposition() {
        return delegate.getContentDisposition();
    }

    /**
     * Jody here with one pass replacement for writeTo.
     *
     * <p>
     * This code is a discussion point, when everyone has had there input we
     * will try and set things up properly.
     * </p>
     *
     * <p>
     * I am providing a mirror of the existing desing: - execute gathers the
     * resultList - sets up the header
     * </p>
     *
     * @param out
     *            DOCUMENT ME!
     *
     * @throws WcsException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalStateException
     *             DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if ((request == null) || (delegate == null)) {
            throw new IllegalStateException("execute has not been called prior to writeTo");
        }

        delegate.encode(out);
    }

    /**
     * Executes CoverageRequest.
     *
     * <p>
     * Willing to execute a CoverageRequest.
     * </p>
     *
     * @param req
     *            DOCUMENT ME!
     *
     * @throws WcsException
     *             DOCUMENT ME!
     */
    public void execute(Request req) throws WcsException {
        execute((CoverageRequest) req);
    }

    public void execute(CoverageRequest request) throws WcsException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(new StringBuffer("execute CoverageRequest response. Called request is: ").append(
                    request).toString());
        }

        this.request = request;

        final String outputFormat = request.getOutputFormat();

        delegate = CoverageResponseDelegateFactory.encoderFor(outputFormat);
        if(delegate == null)
            throw new WcsException("Output format: " + outputFormat + " not supported by geoserver " +
            		"for this Coverage", WcsExceptionCode.InvalidParameterValue, "format");

        final WCSInfo wcs = request.getWCS();
        final Catalog catalog = wcs.getGeoServer().getCatalog();
        CoverageInfo meta = null;
        GridCoverage coverage = null;

        try {
            meta = catalog.getCoverageByName(request.getCoverage());

            if (!meta.getSupportedFormats().contains(outputFormat.toUpperCase())) {
                WcsException newEx = new WcsException(new StringBuffer("output format: ").append(
                            outputFormat).append(" not ")
                                                                                         .append("supported by geoserver for this Coverage")
                                                                                         .toString());
                throw newEx;
            }

            final Format format = meta.getStore().getFormat();
            final AbstractGridCoverage2DReader reader = 
            	(AbstractGridCoverage2DReader) catalog.getResourcePool().getGridCoverageReader(meta.getStore(), WCSUtils.getReaderHints(wcs));

            // /////////////////////////////////////////////////////////
            //
            // Setting coverage reading params.
            //
            // /////////////////////////////////////////////////////////
            final ParameterValueGroup params = reader.getFormat().getReadParameters();

            final GridCoverage2D finalCoverage = getFinalCoverage(request, meta, reader,
                    CoverageUtils.getParametersKVP(params));
            delegate.prepare(outputFormat, finalCoverage);
        } catch (IOException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());

            throw newEx;
        } catch (NoSuchElementException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        } catch (IllegalArgumentException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        } catch (SecurityException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        } catch (WcsException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        } catch (FactoryException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        } catch (IndexOutOfBoundsException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        } catch (TransformException e) {
            final WcsException newEx = new WcsException(e, "problem with CoverageResults",
                    request.getHandle());
            throw newEx;
        }
    }

    /**
     * Release locks if we are into that sort of thing.
     *
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort(ServiceInfo gs) {
        if (request == null) {
            return; // request was not attempted
        }
    }

    /**
     * GetCroppedCoverage
     *
     * @param request CoverageRequest
     * @param meta CoverageInfo
     * @param parameters
     * @param coverage GridCoverage
     * @return GridCoverage2D
     * @throws WcsException
     * @throws IOException
     * @throws IndexOutOfBoundsException
     * @throws FactoryException
     * @throws TransformException
     */
    private static GridCoverage2D getFinalCoverage(CoverageRequest request, CoverageInfo meta,
        AbstractGridCoverage2DReader coverageReader /*GridCoverage coverage*/, Map parameters)
        throws WcsException, IOException, IndexOutOfBoundsException, FactoryException,
            TransformException {
        // This is the final Response CRS
        final String responseCRS = request.getResponseCRS();

        // - first check if the responseCRS is present on the Coverage
        // ResponseCRSs list
        if (!meta.getResponseSRS().contains(responseCRS)) {
            throw new WcsException("This Coverage does not support the requested Response-CRS.");
        }

        // - then create the Coordinate Reference System
        final CoordinateReferenceSystem targetCRS = CRS.decode(responseCRS);

        // This is the CRS of the requested Envelope
        final String requestCRS = request.getCRS();

        // - first check if the requestCRS is present on the Coverage
        // RequestCRSs list
        if (!meta.getResponseSRS().contains(requestCRS)) {
            throw new WcsException("This Coverage does not support the requested CRS.");
        }

        // - then create the Coordinate Reference System
        final CoordinateReferenceSystem sourceCRS = CRS.decode(requestCRS);

        // This is the CRS of the Coverage Envelope
        final CoordinateReferenceSystem cvCRS = ((GeneralEnvelope) coverageReader
            .getOriginalEnvelope()).getCoordinateReferenceSystem();
        final MathTransform GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform = CRS
            .findMathTransform(cvCRS, sourceCRS, true);
        final MathTransform GCCRSTodeviceCRSTransform = CRS.findMathTransform(cvCRS, targetCRS, true);
        final MathTransform deviceCRSToGCCRSTransform = GCCRSTodeviceCRSTransformdeviceCRSToGCCRSTransform
            .inverse();

        com.vividsolutions.jts.geom.Envelope envelope = request.getEnvelope();
        GeneralEnvelope destinationEnvelope;
        final boolean lonFirst = sourceCRS.getCoordinateSystem().getAxis(0).getDirection().absolute()
                                          .equals(AxisDirection.EAST);

        // the envelope we are provided with is lon,lat always
        if (!lonFirst) {
            destinationEnvelope = new GeneralEnvelope(new double[] {
                        envelope.getMinY(), envelope.getMinX()
                    }, new double[] { envelope.getMaxY(), envelope.getMaxX() });
        } else {
            destinationEnvelope = new GeneralEnvelope(new double[] {
                        envelope.getMinX(), envelope.getMinY()
                    }, new double[] { envelope.getMaxX(), envelope.getMaxY() });
        }

        destinationEnvelope.setCoordinateReferenceSystem(sourceCRS);

        // this is the destination envelope in the coverage crs
        final GeneralEnvelope destinationEnvelopeInSourceCRS = (!deviceCRSToGCCRSTransform
            .isIdentity()) ? CRS.transform(deviceCRSToGCCRSTransform, destinationEnvelope)
                           : new GeneralEnvelope(destinationEnvelope);
        destinationEnvelopeInSourceCRS.setCoordinateReferenceSystem(cvCRS);

        /**
         * Reading Coverage on Requested Envelope
         */
        Rectangle destinationSize = null;

        if ((request.getGridLow() != null) && (request.getGridHigh() != null)) {
            final int[] lowers = new int[] {
                    request.getGridLow()[0].intValue(), request.getGridLow()[1].intValue()
                };
            final int[] highers = new int[] {
                    request.getGridHigh()[0].intValue(), request.getGridHigh()[1].intValue()
                };

            destinationSize = new Rectangle(lowers[0], lowers[1], highers[0], highers[1]);
        } else {
            /*destinationSize = coverageReader.getOriginalGridRange().toRectangle();*/
            throw new WcsException("Neither Grid Size nor Grid Resolution have been specified.");
        }

        /**
         * Checking for supported Interpolation Methods
         */
        Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        final String interpolationType = request.getInterpolation();

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
        
        // check input size before reading
        GridGeometry2D requestedGridGeometry = new GridGeometry2D(new GeneralGridEnvelope(destinationSize), destinationEnvelopeInSourceCRS);
        WCSUtils.checkInputLimits(request.getWCS(), meta, coverageReader, requestedGridGeometry);

        // /////////////////////////////////////////////////////////
        //
        // Reading the coverage
        //
        // /////////////////////////////////////////////////////////
        
        parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),
            requestedGridGeometry);

        final GridCoverage2D coverage = coverageReader.read(CoverageUtils.getParameters(
                    coverageReader.getFormat().getReadParameters(), parameters, true));

        if ((coverage == null) || !(coverage instanceof GridCoverage2D)) {
            throw new IOException("The requested coverage could not be found.");
        }
        
        // double check after reading
        WCSUtils.checkInputLimits(request.getWCS(), coverage);

        /**
         * Band Select
         */
        Coverage bandSelectedCoverage = null;

        bandSelectedCoverage = WCSUtils.bandSelect(request.getParameters(), coverage);

        /**
         * Crop
         */
        final GridCoverage2D croppedGridCoverage = WCSUtils.crop(bandSelectedCoverage,
                (GeneralEnvelope) coverage.getEnvelope(), cvCRS, destinationEnvelopeInSourceCRS,
                Boolean.TRUE);

        /**
         * Scale/Resampling (if necessary)
         */
        GridCoverage2D subCoverage = croppedGridCoverage;
        final GeneralGridEnvelope newGridrange = new GeneralGridEnvelope(destinationSize);

        /*if (!newGridrange.equals(croppedGridCoverage.getGridGeometry()
                        .getGridRange())) {*/
        subCoverage = WCSUtils.scale(croppedGridCoverage, newGridrange, croppedGridCoverage, cvCRS,
                destinationEnvelopeInSourceCRS);
        //}
        
        // before extracting the output make sure it's not too big
        WCSUtils.checkOutputLimits(request.getWCS(), requestedGridGeometry.getGridRange2D(), subCoverage.getRenderedImage().getSampleModel());

        /**
         * Reproject
         */
        subCoverage = WCSUtils.reproject(subCoverage, sourceCRS, targetCRS, interpolation);

        return subCoverage;
    }
}
