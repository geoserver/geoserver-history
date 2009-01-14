package org.geoserver.wfs.response;

import com.thoughtworks.xstream.XStream;

/**
 * Represents the ogr2ogr output format configuration as a whole.
 * Only used for XStream driven de-serialization 
 * @author Andrea Aime - OpenGeo

 */
class OgrConfiguration {
    public static final OgrConfiguration DEFAULT;
    static {
        DEFAULT = new OgrConfiguration();
        // assume it's in the classpath
        DEFAULT.ogr2ogrLocation = "ogr2ogr";
        // add some default formats
        DEFAULT.formats = new OgrFormat[] {
                new OgrFormat("MapInfo file", "OGR-TAB", ".tab"),
                new OgrFormat("MapInfo file", "OGR-MIF", ".mif",  "-dsco", "FORMAT=MIF"),
                new OgrFormat("CSV", "OGR-CSV", ".csv"),
                new OgrFormat("KML", "OGR-KML", ".kml"),
        };
    }
    
    String ogr2ogrLocation;
    OgrFormat[] formats;
    
    public static void main(String[] args) {
        // generates the default configuration xml and prints it to the output
        XStream xstream = Ogr2OgrConfigurator.buildXStream();
        System.out.println(xstream.toXML(OgrConfiguration.DEFAULT));
    }
}
