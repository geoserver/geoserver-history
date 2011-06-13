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
	
	private final static String DEFAULT_GDAL_WARPING_PARAMETERS = "-co TILED=yes -wm 64 -multi -dstalpha";
	
	private final static String DEFAULT_GDAL_TRANSLATE_PARAMETERS = "";//-expand rgb";
	
//	private final static String DEFAULT_GDAL_DATA = "d:\\work\\programs\\FWTools2.4.7\\data";
	
	private final static String GDAL_CACHE_MAX_KEY = "GDAL_CACHE_MAX";
	
	private final static String GDAL_DATA_KEY = "GDAL_DATA";
	
	final int DEFAULT_GDAL_CACHE_MAX = 512;
	
	final int DEFAULT_WARPING_MEMORY = 64;
	
	final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	
	final String LOGGING_DIR = System.getProperty("java.io.tmpdir");
	
	final Long DEFAULT_EXECUTION_TIMEOUT = 180000l;
	
	private File tempFolder;
	
	private File loggingFolder;
	
	private long executionTimeout = DEFAULT_EXECUTION_TIMEOUT; 
	
	private List<Variable> envVariables;
	
	private int warpingMemory = DEFAULT_WARPING_MEMORY;
	
	private int gdalCacheMaxMemory = DEFAULT_GDAL_CACHE_MAX;

	//Add here any parameter used by gdalwarp
	private String gdalWarpingParameters = DEFAULT_GDAL_WARPING_PARAMETERS;

	//Add here any parameter used by gdal_translate when setting gcps 
	private String gdalTranslateParameters = DEFAULT_GDAL_TRANSLATE_PARAMETERS; 
	
	private String warpingPath = DEFAULT_GDAL_WARP_PATH;
	
	private String translatePath = DEFAULT_GDAL_TRANSLATE_PATH;
	
//	private String gdalDataPath = DEFAULT_GDAL_DATA;
	
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
		String value = Integer.toString(gdalCacheMaxMemory);
		if (value != null) {
			var.setValue(value);
			envVariables.add(var);
		}
//		var = new Variable();
//		var.setKey(GDAL_DATA_KEY);
//		value = gdalDataPath;
//		if (value != null) {
//			var.setValue(value);
//			envVariables.add(var);
//		}
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

	public String getGdalWarpingParameters() {
		return gdalWarpingParameters;
	}

	public String getGdalTranslateParameters() {
		return gdalTranslateParameters;
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