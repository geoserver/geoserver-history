/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.util.CoverageUtils;
import org.vfny.geoserver.wcs.WcsException;
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
	private static final Logger LOGGER = Logger
			.getLogger("org.vfny.geoserver.responses");

	/**
	 * 
	 * @uml.property name="delegate"
	 * @uml.associationEnd multiplicity="(0 1)"
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
	public String getContentType(GeoServer gs) {
		return delegate.getContentType(gs);
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
			throw new IllegalStateException(
					"execute has not been called prior to writeTo");
		}

		delegate.encode(out);
	}

	/**
	 * Executes FeatureRequest.
	 * 
	 * <p>
	 * Willing to execute a FetureRequest, or FeatureRequestWith Lock.
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
		if (LOGGER.isLoggable(Level.FINEST))
			LOGGER.finest(new StringBuffer(
					"execute CoverageRequest response. Called request is: ")
					.append(request).toString());
		this.request = request;
		final String outputFormat = request.getOutputFormat();

		try {
			delegate = CoverageResponseDelegateFactory.encoderFor(outputFormat);
		} catch (NoSuchElementException ex) {
			WcsException newEx = new WcsException(new StringBuffer("output format: ").append(
					outputFormat).append(" not ").append(
					"supported by geoserver").toString(), ex);
			newEx.initCause(ex);
			throw newEx;
		}

		final Data catalog = request.getWCS().getData();
		CoverageInfo meta = null;
		GridCoverage coverage = null;

		try {
			meta = catalog.getCoverageInfo(request.getCoverage());
			
			if (!meta.getSupportedFormats().contains(outputFormat.toUpperCase())) {
				WcsException newEx = new WcsException(new StringBuffer("output format: ").append(
						outputFormat).append(" not ").append(
						"supported by geoserver").toString());
				throw newEx;
			}
			
			final String formatID = meta.getFormatId();
			final DataConfig dataConfig = (DataConfig) request
					.getHttpServletRequest().getSession().getServletContext()
					.getAttribute(DataConfig.CONFIG_KEY);
			final CoverageStoreConfig dfConfig = dataConfig
					.getDataFormat(formatID);
			final String realPath = request.getHttpServletRequest()
					.getRealPath("/");
			final URL url = CoverageUtils.getResource(dfConfig.getUrl(),
					realPath);

			final Format format = dfConfig.getFactory();
			final GridCoverageReader reader = ((AbstractGridFormat) format)
					.getReader(url);

			final ParameterValueGroup params = format.getReadParameters();
			if (params != null) {
				List list = params.values();
				Iterator it = list.iterator();
				ParameterValue param;
				ParameterDescriptor descr ;

				String key ;
				Object value;
				while (it.hasNext()) {
					 param = ((ParameterValue) it.next());
					 descr = (ParameterDescriptor) param
							.getDescriptor();

					 key = descr.getName().toString();
					 value = CoverageUtils.getCvParamValue(key, param,
							meta.getParameters());

					if (value != null)
						params.parameter(key).setValue(value);
				}
			}

			coverage = reader
					.read(params != null ? (GeneralParameterValue[]) params
							.values().toArray(
									new GeneralParameterValue[params.values()
											.size()]) : null);
			if (coverage == null || !(coverage instanceof GridCoverage2D))
				throw new IOException(
						"The requested coverage could not be found.");

			final GridCoverage2D finalCoverage = CoverageUtils
					.getCroppedCoverage(request, meta, coverage);
			delegate.prepare(outputFormat, finalCoverage);
		} catch (IOException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			
			throw newEx;
		} catch (NoSuchElementException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		} catch (IllegalArgumentException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		} catch (SecurityException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		} catch (WcsException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		} catch (FactoryException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		} catch (IndexOutOfBoundsException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		} catch (TransformException e) {
			final WcsException newEx = new WcsException(e, "problem with CoverageResults", request
					.getHandle());
			throw newEx;
		}
	}

	/**
	 * Release locks if we are into that sort of thing.
	 * 
	 * @see org.vfny.geoserver.responses.Response#abort()
	 */
	public void abort(Service gs) {
		if (request == null) {
			return; // request was not attempted
		}

		Data catalog = gs.getData();
	}
}
