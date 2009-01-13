package org.geoserver.wfs.response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;

public class Ogr2OgrTestUtil {
    private static Logger LOGGER = Logging.getLogger(Ogr2OgrTestUtil.class);

    private static Boolean IS_OGR_AVAILABLE;
    private static String OGR2OGR;

    public static boolean isOgrAvailable() {

        // check this just once
        if (IS_OGR_AVAILABLE == null) {
            try {
                File props = new File("./src/test/resources/ogr2ogr.properties");
                Properties p = new Properties();
                p.load(new FileInputStream(props));
                
                OGR2OGR = p.getProperty("ogr2ogr");
                // assume it's in the path if the property file hasn't been configured
                if(OGR2OGR == null)
                    OGR2OGR = "ogr2ogr";
                
                // run the process so that whatever output is generates goes to /dev/null
                // but at the same time having it block trying to write to its stdout/stderr
                ProcessBuilder pb = new ProcessBuilder(OGR2OGR, "--version");
                pb.redirectErrorStream();
                Process proc = pb.start();
                InputStreamReader isr = new InputStreamReader(proc.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while (br.readLine() != null);
                
                int exitStatus = proc.waitFor();
                IS_OGR_AVAILABLE = exitStatus == 0;
            } catch (Exception e) {
                IS_OGR_AVAILABLE = false;
                e.printStackTrace();
                LOGGER.log(Level.SEVERE,
                        "Disabling ogr2ogr output format tests, as ogr2ogr lookup failed", e);
            }
        }

        return IS_OGR_AVAILABLE;
    }
    
    public static String getOgr2Ogr() {
        if(isOgrAvailable())
            return OGR2OGR;
        else
            return null;
    }
    
    
}
