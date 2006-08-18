/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.coverage.grid.AbstractGridFormat;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.DataCoveragesNewForm;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.CoverageStoreInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.CoverageUtils;

/**
 * DataCoveragesNewAction purpose.
 * 
 * <p>
 * Description of DataCoveragesNewAction ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * 
 * <ul>
 * <li> Coverage: description </li>
 * </ul>
 * 
 * <p>
 * Example Use:
 * </p>
 * 
 * <pre><code>
 *     DataCoveragesNewAction x = new DataCoveragesNewAction(...);
 * </code></pre>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author cholmesny
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id: DataCoveragesNewAction.java,v 1.15 2004/09/17 16:34:47
 *          cholmesny Exp $
 */
public class DataCoveragesNewAction extends ConfigAction {
	public final static String NEW_COVERAGE_KEY = "newCoverage";

	public ActionForward execute(ActionMapping mapping,
			ActionForm incomingForm, UserContainer user,
			HttpServletRequest request, HttpServletResponse response)
			throws ConfigurationException {

		final DataCoveragesNewForm form = (DataCoveragesNewForm) incomingForm;
		final String formatID = form.getSelectedNewCoverage();
		final Data catalog = getData();
		CoverageStoreInfo cvStoreInfo = catalog.getFormatInfo(formatID);
		if(cvStoreInfo == null) {
			cvStoreInfo = new CoverageStoreInfo(getDataConfig().getDataFormat(formatID).toDTO(), catalog);
		}
		GridCoverage gc = null;

		final Format format = cvStoreInfo.getFormat(); 
		GridCoverageReader reader = cvStoreInfo.getReader();
		if (reader == null)
			try {
				reader = ((AbstractGridFormat) format).getReader(CoverageUtils.getResourceAsFile(cvStoreInfo.getUrl(), catalog.getBaseDir()));
			} catch (MalformedURLException ex) {
				throw new ConfigurationException(ex);
			}

		final ParameterValueGroup params = format.getReadParameters();

        // After extracting params into a map
        List parameters = new ArrayList(); // values used for connection

		// Convert Params into the kind of Map we actually need
        //
        ParameterValue param;
        String key;
        Object value;
        final String readGeometryKey = AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString();
		if (params != null) {
			final List list = params.values();
			final Iterator it = list.iterator();
			while (it.hasNext()) {
				param = ((ParameterValue) it.next());
				final ParameterDescriptor descr = (ParameterDescriptor) param
						.getDescriptor();

				key = descr.getName().toString();
				// //
                //
                // Skipping decimation parameters
                //
                // //
                if (key.equalsIgnoreCase(readGeometryKey)) {
                    continue;
                }
				value = CoverageUtils.getCvParamValue(key, param, cvStoreInfo.getParameters());

				if (value != null) {
                    //params.parameter(key).setValue(value);
                    parameters.add(new DefaultParameterDescriptor(key, value.getClass(), null, value).createValue());
                }
			}
		}
		try {
			// trying to read the created coverage in order to check the entered
			// parameters
			gc = reader.read((GeneralParameterValue[]) parameters.toArray(new GeneralParameterValue[parameters.size()]));
                    /*params != null ? (GeneralParameterValue[]) params
					.values().toArray(
							new GeneralParameterValue[params.values().size()])
					: null);*/

			if (gc == null || !(gc instanceof GridCoverage2D))
				throw new IOException(
						"The requested coverage could not be found.");
		} catch (InvalidParameterValueException e) {
			throw new ConfigurationException(e);
		} catch (ParameterNotFoundException e) {
			throw new ConfigurationException(e);
		} catch (MalformedURLException e) {
			throw new ConfigurationException(e);
		} catch (IllegalArgumentException e) {
			throw new ConfigurationException(e);
		} catch (SecurityException e) {
			throw new ConfigurationException(e);
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}

		final GridCoverage2D finalCoverage = (GridCoverage2D) gc;
		CoverageConfig coverageConfig = new CoverageConfig(formatID, format, finalCoverage, request);

		request.setAttribute(NEW_COVERAGE_KEY, "true");
		request.getSession().setAttribute(DataConfig.SELECTED_COVERAGE, coverageConfig);

		user.setCoverageConfig(coverageConfig);
		return mapping.findForward("config.data.coverage.editor");
	}
}
