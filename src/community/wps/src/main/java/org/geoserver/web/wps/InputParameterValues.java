/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.wps.ppio.ComplexPPIO;
import org.geoserver.wps.ppio.ProcessParameterIO;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.opengis.feature.type.Name;

/**
 * Contains the set of values for a single parameter. For most input parameters
 * it will be just one value actually
 * 
 * @author Andrea Aime - OpenGeo
 */
@SuppressWarnings("serial")
class InputParameterValues implements Serializable {
	public enum ParameterType {
		LITERAL, TEXT, VECTOR_LAYER, RASTER_LAYER;
	};

	Name processName;
	String paramName;
	List<ParameterValue> values = new ArrayList<ParameterValue>();

	public InputParameterValues(Name processName, String paramName) {
		this.processName = processName;
		this.paramName = paramName;
		Parameter<?> p = getParameter();
		for (int i = 0; i < p.minOccurs; i++) {
			values.add(new ParameterValue(guessBestType(), getDefaultMime(),
					null));
		}
	}

	private ParameterType guessBestType() {
		if (!isComplex())
			return ParameterType.LITERAL;
		if (FeatureCollection.class.isAssignableFrom(getParameter().type)) {
			return ParameterType.VECTOR_LAYER;
		} else if (GridCoverage2D.class.isAssignableFrom(getParameter().type)) {
			return ParameterType.RASTER_LAYER;
		} else {
			return ParameterType.TEXT;
		}
	}

	String getDefaultMime() {
		if (!isComplex()) {
			return null;
		} else {
			return ((ComplexPPIO) getProcessParameterIO().get(0)).getMimeType();
		}
	}

	public List<String> getSupportedMime() {
		List<String> results = new ArrayList<String>();
		for (ProcessParameterIO ppio : getProcessParameterIO()) {
			ComplexPPIO cp = (ComplexPPIO) ppio;
			results.add(cp.getMimeType());
		}
		return results;
	}

	public boolean isComplex() {
		List<ProcessParameterIO> ppios = getProcessParameterIO();
		return ppios.size() > 0 && ppios.get(0) instanceof ComplexPPIO;
	}

	List<ProcessParameterIO> getProcessParameterIO() {
		return ProcessParameterIO.findAll(getParameter(), null);
	}

	ProcessFactory getProcessFactory() {
		return Processors.createProcessFactory(processName);
	}

	Parameter<?> getParameter() {
		return getProcessFactory().getParameterInfo(processName).get(paramName);
	}

	/**
	 * A single value, along with the chosen editor and its mime type
	 */
	static class ParameterValue implements Serializable {
		ParameterType type;
		String mime;
		Serializable value;

		public ParameterValue(ParameterType type, String mime,
				Serializable value) {
			this.type = type;
			this.mime = mime;
			this.value = value;
		}

	}
}