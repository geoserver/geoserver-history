package org.geoserver.wps.gs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.Environment.Variable;

public class GeorectifyConfiguration {
	
	//TODO: Configure me through spring + property file
	
	final String DEFAULT_GDAL_TRANSLATE_PATH = "gdal_translate";
	
	final String DEFAULT_GDAL_WARP_PATH = "gdalwarp";
	
	private final static String GDAL_CACHE_MAX_KEY = "GDAL_CACHE_MAX";
	
	final int DEFAULT_GDAL_CACHE_MAX = 512;
	
	final int DEFAULT_WARPING_MEMORY = 40;
	
	final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	
	final String LOGGING_DIR = System.getProperty("java.io.tmpdir");
	
	final Long DEFAULT_EXECUTION_TIMEOUT = 60000l;
	
	private File tempFolder;
	
	private File loggingFolder;
	
	private long executionTimeout = DEFAULT_EXECUTION_TIMEOUT; 
	
	private List<Variable> envVariables;
	
	private int warpingMemory = DEFAULT_WARPING_MEMORY;
	
	private int gdalCacheMaxMemory = DEFAULT_GDAL_CACHE_MAX;
	
	private String warpingPath = DEFAULT_GDAL_WARP_PATH;
	
	private String translatePath = DEFAULT_GDAL_TRANSLATE_PATH;
	
	public GeorectifyConfiguration(){
		try {
			init();
		} catch (IOException e) {
			//TODO : FIXME
		}
	}

	private void init() throws IOException {
		//Init from property file
		tempFolder = initFolder(TEMP_DIR);
		loggingFolder = initFolder(LOGGING_DIR);
		initEnvVars();
	}
	
	private void initEnvVars() {
		envVariables = new ArrayList<Variable>();
		// as an instance: PATH, LD_LIBRARY_PATH and similar
		Variable var = new Variable();
		var.setKey(GDAL_CACHE_MAX_KEY);
		final String value = Integer.toString(gdalCacheMaxMemory);
		if (value != null) {
			var.setValue(value);
			envVariables.add(var);
		}
	}
	
	private File initFolder(final String folderPath) throws IOException {
        File tempFolder = new File(folderPath);
        if (!tempFolder.exists()){
        	try {
        		boolean createdFolder = tempFolder.mkdir();
	        	if (!createdFolder) {
	        	
	        	}
        	} catch (SecurityException se){
        		//Proceeding with default temp dir. We should need to log this
        		tempFolder = new File(System.getProperty("java.io.tmpdir"));
        	}
        }
        if (!tempFolder.exists() || !tempFolder.canWrite()){
        	throw new IOException("Unable to write on the specified folder: " + tempFolder.getAbsolutePath());
        }
        return tempFolder;
	}
	
	public File getTempFolder() {
		return tempFolder;
	}

	public void setTempFolder(File tempFolder) {
		this.tempFolder = tempFolder;
	}

	public File getLoggingFolder() {
		return loggingFolder;
	}

	public void setLoggingFolder(File loggingFolder) {
		this.loggingFolder = loggingFolder;
	}

	public long getExecutionTimeout() {
		return executionTimeout;
	}

	public void setExecutionTimeout(long executionTimeout) {
		this.executionTimeout = executionTimeout;
	}

	public List<Variable> getEnvVariables() {
		return envVariables;
	}

	public void setEnvVariables(List<Variable> envVariables) {
		this.envVariables = envVariables;
	}

	public int getWarpingMemory() {
		return warpingMemory;
	}

	public void setWarpingMemory(int warpingMemory) {
		this.warpingMemory = warpingMemory;
	}

	public int getGdalCacheMaxMemory() {
		return gdalCacheMaxMemory;
	}

	public void setGdalCacheMaxMemory(int gdalCacheMaxMemory) {
		this.gdalCacheMaxMemory = gdalCacheMaxMemory;
	}

	public String getWarpingPath() {
		return warpingPath;
	}

	public void setWarpingPath(String warpingPath) {
		this.warpingPath = warpingPath;
	}

	public String getTranslatePath() {
		return translatePath;
	}

	public void setTranslatePath(String translatePath) {
		this.translatePath = translatePath;
	}
	
}