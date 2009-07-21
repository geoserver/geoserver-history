package org.geoserver.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import com.thoughtworks.xstream.XStream;

/**
 * This class holds the the configuration for the Proxy server module during runtime. It is also
 * serialized as XML to persistently store settings.
 * 
 * @author Alan Gerber <agerber@openplans.org>
 */
public class ProxyConfig implements java.io.Serializable{
    /**
     * Eclipse wants this here.  I reckon I should listen to it.
     */
    private static final long serialVersionUID = 1;

    /*
     * Sets the mode of the proxy server: -HOSTNAMEORMIMETYPE means a request must match have
     * matches on the hostname AND MIMEType whitelists -HOSTNAMEANDMIMETYPE means a request must
     * match have matches on the hostname OR MIMEType whitelists -HOSTNAME means a request must
     * match have a match on the hostname whitelist alone -MIMETYPE means a request must match have
     * a match on the MIMEType whitelist alone
     */
    public enum Mode {
        HOSTNAMEORMIMETYPE ("Hostname OR MIMEType"),
        HOSTNAMEANDMIMETYPE ("Hostname AND MIMEType"),
        HOSTNAME ("Hostname only"),
        MIMETYPE ("MIMEType only");
        
        public final String modeName;
        Mode(String modeName){
            this.modeName = modeName;
        }
        
        public static List<String> modeNames() {
            List<String> modeNames = new ArrayList<String>();
            for (Mode mode : Mode.values())
            {
                modeNames.add(mode.modeName);
            }
            return modeNames;
        }
    };

    public Mode mode;

    /* A list of regular expressions describing hostnames the proxy is permitted to forward to */
    public List<Pattern> hostnameWhitelist;

    /* A list of regular expressions describing MIMETypes the proxy is permitted to forward */
    public List<Pattern> mimetypeWhitelist;

    /*The name of the directory where configuration is stored*/
    private static final String confDirName = "proxy";
    /*The name of the file the configuration is stored in*/
    private static final String confFileName = "proxy.xml";
    
    /* Attempts to grab the proxy's config file from the data dir.
     * @return  the proxy's configuration's File
     * @throws  ConfigurationException if the config system is busted
     */
    public static File getConfigFile() throws ConfigurationException
    {
        File dir = GeoserverDataDirectory.findCreateConfigDir(ProxyConfig.confDirName);
        File proxyConfFile = new File(dir, ProxyConfig.confFileName);
        return proxyConfFile;
    }
    

    public static final ProxyConfig DEFAULT;
    static {
        DEFAULT = new ProxyConfig();
        DEFAULT.mode = Mode.HOSTNAME;
        DEFAULT.hostnameWhitelist = new ArrayList<Pattern>();
        DEFAULT.mimetypeWhitelist = new ArrayList<Pattern>();
    }

    private static final Logger LOG = org.geotools.util.logging.Logging
            .getLogger("org.geoserver.proxy");

    /* this is pretty unappealingly hackish */
    public static ProxyConfig loadConfFromDisk() {
        ProxyConfig retval;

        try {
            /*File put together here because it's unknown whether GSDD class is init'd in a 
             * static context*/
            File dir = GeoserverDataDirectory.findCreateConfigDir(confDirName);
            File proxyConfFile = new File(dir, confFileName);
            // TODO: threadsafe?
            InputStream proxyConfStream = new FileInputStream(proxyConfFile);
            // TODO: do i need a new one of these?
            XStream xs = new XStream();
            retval = (ProxyConfig) (xs.fromXML(proxyConfStream));
        } catch (Exception e) {
            /* TODO: make this reasonable */
            LOG.warning("Failed to open configuration for Proxy module. Using default. Exception:"
                    + e.toString());
            retval = DEFAULT;
        }
        return retval;
    }

    public static boolean writeConfigToDisk(ProxyConfig pc) {
        try {
            /*File put together here because it's unknown whether GSDD class is init'd in a 
             * static context*/
            File dir = GeoserverDataDirectory.findCreateConfigDir(confDirName);
            File proxyConfFile = new File(dir, confFileName);
            // TODO: do i need a new one of these? how expensive is building this vs. saving it?
            XStream xs = new XStream();
            String xml = xs.toXML(pc);
            FileWriter fw = new FileWriter(proxyConfFile, false); // false means overwrite old file
            fw.write(xml);
            fw.close();
            return true;
        } catch (Exception e) {
            LOG.warning("Failed to save configuration for Proxy module. Exception:"
                    + e.toString());
            return false;
        }
    }
    
    /*Output a textual representation of the list*/
    @Override
    public String toString(){
        StringBuilder stringForm = new StringBuilder(256);
        stringForm.append("Mode: " + this.mode.modeName + "\n");
        stringForm.append("Hostname regex whitelist: \n");
        for (Pattern pattern : this.hostnameWhitelist)
            stringForm.append(pattern + "\n");
        stringForm.append("MIMEType regex whitelist: \n");
        for (Pattern pattern : this.mimetypeWhitelist)
            stringForm.append(pattern + "\n");
        stringForm.append(this.mode.modeName + "\n");        
        return stringForm.toString();
    }
    
    /*Tester main method*/
    public static void main(String[] args)
    {
        System.out.println(DEFAULT);
        //lol don't do this the data directory ain't init'd
        //ProxyConfig testConf = loadConfFromDisk();
        System.out.println(testConf);
        writeConfigToDisk(testConf);
    }
}
