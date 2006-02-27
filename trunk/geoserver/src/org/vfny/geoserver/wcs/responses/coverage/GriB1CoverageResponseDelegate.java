/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses.coverage;

import java.io.IOException;
import java.io.OutputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.grib1.GRIB1Writer;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.wcs.responses.CoverageResponseDelegate;

/**
 * @author giannecchini
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GriB1CoverageResponseDelegate implements CoverageResponseDelegate {

	private String outputFormat;

	/**
	 * 
	 * @uml.property name="sourceCoverage"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private GridCoverage2D sourceCoverage;

	/**
	 * 
	 */
	public GriB1CoverageResponseDelegate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#canProduce(java.lang.String)
	 */
	public boolean canProduce(String outputFormat) {
		if(outputFormat.equalsIgnoreCase("GriB1"))
//			//checking if we can produce a GriB1 from this coverage 
//			//since we might not have enough metadata
//			//depending on the coverage source.
//			//As an instance if we were using an Ascii Grid coverage we would not be able (FOR THE MOMENT)
//			//to produce a GriB1 because of lacking of metadata.
//			if(this.sourceCoverage!=null)
//			{	
//				//getting all the names we need to get
//				final String []names=new GRIB1Writer("fake!!").getMetadataNames();
//				final int length=names.length;
//				//check to have all the metadata we need
//				for(int i=0;i<length;i++)
//					if(this.sourceCoverage.getMetadataValue(names[i])==null)
//						return false;
//				//we have all of them
//				return true;
//			}
			
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#prepare(java.lang.String, org.geotools.coverage.grid.GridCoverage2D)
	 */
	public void prepare(String outputFormat, GridCoverage2D coverage)
			throws IOException {
		this.outputFormat = outputFormat;
		this.sourceCoverage = coverage;

	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#getContentType(org.vfny.geoserver.global.GeoServer)
	 */
	public String getContentType(GeoServer gs) {
		// TODO Auto-generated method stub
		return "application/octet-stream";
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#getContentEncoding()
	 */
	public String getContentEncoding() {
		return "grb";
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContentDisposition() {
		return "attachment;filename="+this.sourceCoverage.getName()+".grb";
	}

	/* (non-Javadoc)
	 * @see org.vfny.geoserver.wcs.responses.CoverageResponseDelegate#encode(java.io.OutputStream)
	 * TODO
	 */
	public void encode(OutputStream output) throws ServiceException,
			IOException {
		if (sourceCoverage == null) {
			throw new IllegalStateException(
					"It seems prepare() has not been called"
					+ " or has not succeed");
		}
		//encode and write to the output
		GridCoverageWriter writer = new GRIB1Writer(output);
		if(writer!=null){
            //requested metadata names
            String[] metadataNames = writer.getMetadataNames();

            if(metadataNames!=null)
            {
            	//setting requested  metadata for the writer
            	for (int j = 0; j < metadataNames.length; j++) 
            		writer.setMetadataValue(metadataNames[j],          	
            			sourceCoverage.getMetadataValue(metadataNames[j]));
            	//write
            	writer.write(this.sourceCoverage,null);
            	output.flush();
        		//freeing everything
        		writer.dispose();
        		writer=null;
        		this.sourceCoverage.dispose();
        		this.sourceCoverage=null;            	
            	return;
            }
 		}
     	throw new ServiceException("Could not instantiate a grib writer. Reason might be missing metadata! (This is a known issue!)");
	}

}
