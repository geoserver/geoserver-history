package org.geoserver.monitor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.logging.Logging;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class GeoIPPostProcessor implements Runnable {

    static Logger LOGGER = Logging.getLogger("org.geoserver.montior");
    
    /**
     * cached geoip lookup service
     */
    static LookupService geoIPLookup;
    
    //TODO: cache by IP address
    
    GeoServerResourceLoader loader;
    HttpServletRequest request;
    RequestData data;
    
    public GeoIPPostProcessor(GeoServerResourceLoader loader, HttpServletRequest request, 
        RequestData data) {
        
        this.loader = loader;
        this.request = request;
        this.data = data;
    }
    
    public void run() {
        if (data.getRemoteAddr() == null) {
            LOGGER.info("Request data did not contain ip address. Unable to perform GeoIP lookup.");
            return;
        }
        
        if (geoIPLookup == null) {
            synchronized (geoIPLookup) {
                if (geoIPLookup == null) {
                    geoIPLookup = lookupGeoIPDatabase();
                }
            }
        }
        
        if (geoIPLookup == null) {
            return;
        }
        
        Location loc = geoIPLookup.getLocation(data.getRemoteAddr());
        if (loc == null) {
            LOGGER.warning("Unable to obtain location for " + data.getRemoteAddr());
            return;
        }
        
        data.setRemoteCountry(loc.countryName);
        data.setRemoteCity(loc.city);
        data.setRemoteLat(loc.latitude);
        data.setRemoteLon(loc.longitude);
    }
    
    LookupService lookupGeoIPDatabase() {
        try {
            File f = loader.find("monitoring", "GeoLiteCity.dat");
            if (f == null) {
                String path = 
                    new File(loader.getBaseDirectory(), "monitoring/GeoLiteCity.dat").getAbsolutePath(); 
                LOGGER.warning("GeoIP database " + path  + " is not available. " +
                    "Please install the file to enable GeoIP lookups.");
                return null;
            }
            
            return new LookupService(f);
        } 
        catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error occured looking up GeoIP database", e);
            return null;
        }
    }

}
