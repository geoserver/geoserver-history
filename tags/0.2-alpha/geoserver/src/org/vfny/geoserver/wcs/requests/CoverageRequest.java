/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import java.util.logging.Logger;


import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageRequest extends WCSRequest {
	/** Standard logging instance for class */
	private static final Logger LOGGER = Logger.getLogger(
	"org.vfny.geoserver.requests");
	
	protected String coverage = null;
	
	protected String outputFormat = "GML";
	
	protected Envelope envelope = null;
	
	protected String interpolation = null;
	
	protected String handle = null;
	
	protected String coverageVersion = null;
	
	public CoverageRequest() {
		super();
		setRequest("GetCoverage");
	}
	
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
	
	public String getOutputFormat() {
		return this.outputFormat;
	}
	
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	public String getHandle() {
		return this.handle;
	}
	
	public void setCoverageVersion(String version) {
		this.version = version;
	}
	
	public String getCoverageVersion() {
		return this.version;
	}
	
	public String toString() {
		StringBuffer returnString = new StringBuffer("\nRequest");
		returnString.append(": " + handle);
		returnString.append("\n coverage:" + coverage);
		returnString.append("\n output format:" + outputFormat);
		returnString.append("\n version:" + version);
		returnString.append("\n envelope:" + envelope);
		returnString.append("\n interpolation:" + interpolation);
		
		return returnString.toString();
	}
	
	public boolean equals(Object obj) {
		super.equals(obj);
		
		if (!(obj instanceof CoverageRequest)) {
			return false;
		}
		
		CoverageRequest request = (CoverageRequest) obj;
		boolean isEqual = true;
		
		if ((this.coverage == null) && (request.getCoverage() == null)) {
			isEqual = isEqual && true;
		} else if ((this.coverage == null) || (request.getCoverage() == null)) {
			isEqual = false;
		} else if (request.getCoverage().equals(coverage)) {
			isEqual = isEqual && true;
		} else {
			isEqual = false;
		}
		
		LOGGER.finest("checking version equality: " + isEqual);
		
		if ((this.version == null) && (request.getVersion() == null)) {
			isEqual = isEqual && true;
		} else if ((this.version == null) || (request.getVersion() == null)) {
			isEqual = false;
		} else if (request.getVersion().equals(version)) {
			isEqual = isEqual && true;
		} else {
			isEqual = false;
		}
		
		LOGGER.finest("checking version equality: " + isEqual);
		
		if ((this.handle == null) && (request.getHandle() == null)) {
			isEqual = isEqual && true;
		} else if ((this.handle == null) || (request.getHandle() == null)) {
			isEqual = false;
		} else if (request.getHandle().equals(handle)) {
			isEqual = isEqual && true;
		} else {
			isEqual = false;
		}
		
		LOGGER.finest("checking handle equality: " + isEqual);
		
		if ((this.outputFormat == null) && (request.getOutputFormat() == null)) {
			isEqual = isEqual && true;
		} else if ((this.outputFormat == null)
				|| (request.getOutputFormat() == null)) {
			isEqual = false;
		} else if (request.getOutputFormat().equals(outputFormat)) {
			isEqual = isEqual && true;
		} else {
			isEqual = false;
		}
		
		LOGGER.finest("checking output format equality: " + isEqual);
		
		if ((this.envelope == null) && (request.getEnvelope() == null)) {
			isEqual = isEqual && true;
		} else if ((this.envelope == null)
				|| (request.getEnvelope() == null)) {
			isEqual = false;
		} else if (request.getEnvelope().equals(envelope)) {
			isEqual = isEqual && true;
		} else {
			isEqual = false;
		}
		
		LOGGER.finest("checking envelope equality: " + isEqual);
		
		if ((this.interpolation == null) && (request.getInterpolation() == null)) {
			isEqual = isEqual && true;
		} else if ((this.interpolation == null)
				|| (request.getInterpolation() == null)) {
			isEqual = false;
		} else if (request.getInterpolation().equals(interpolation)) {
			isEqual = isEqual && true;
		} else {
			isEqual = false;
		}
		
		LOGGER.finest("checking interpolation equality: " + isEqual);
		
		return isEqual;
	}
	
	public int hashCode() {
		int result = super.hashCode();
		result = (23 * result) + ((handle == null) ? 0 : handle.hashCode());
		result = (23 * result) + ((coverage == null) ? 0 : coverage.hashCode());
		
		return result;
	}
	/**
	 * @return Returns the envelope.
	 */
	public Envelope getEnvelope() {
		return envelope;
	}
	/**
	 * @param envelope The envelope to set.
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	
	public void setEnvelope(String envelope) {
		String[] coords = envelope.split(",");
		try {
			double arg0 = Double.parseDouble(coords[0]);
			double arg1 = Double.parseDouble(coords[1]);
			double arg2 = Double.parseDouble(coords[2]);
			double arg3 = Double.parseDouble(coords[3]);
			
			this.envelope = new Envelope(arg0, arg2, arg1, arg3);
		} catch(Exception e) {
			this.envelope = null;
		}
	}
	/**
	 * @return Returns the interpolation.
	 */
	public String getInterpolation() {
		return interpolation;
	}
	/**
	 * @param interpolation The interpolation to set.
	 */
	public void setInterpolation(String interpolation) {
		this.interpolation = interpolation;
	}
	/**
	 * @return Returns the coverage.
	 */
	public String getCoverage() {
		return coverage;
	}
	/**
	 * @param coverage The coverage to set.
	 */
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
}
