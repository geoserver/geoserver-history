/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;


/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
public class StyleResource extends Resource {
    private DataConfig myDC;
    private Data myData;
    protected static Logger LOG = org.geotools.util.logging.Logging.getLogger("org.geoserver.community");

    public StyleResource(){
        super();
    }

    public StyleResource(Context context, Request request, Response response,
        DataConfig myDataConfig) {
        super(context, request, response);
        myDC = myDataConfig;
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public void setData(Data d){
        myData = d;
    } 

    public Data getData(){
        return myData;
    }
    
    public void handleGet() {
        Request req = getRequest();

        String styleName = (String) req.getAttributes().get("style");
        StyleConfig sc = (StyleConfig) myDC.getStyles().get(styleName);

        if (sc != null) {
            getResponse()
                .setEntity(new FileRepresentation(sc.getFilename(), MediaType.APPLICATION_XML, 10));
        } else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse()
                .setEntity(new StringRepresentation(
                    "Error - Couldn't find the requested resource", MediaType.TEXT_PLAIN));
        }
    }

    public boolean allowPut(){
        return true;
    }

    public void handlePut() {
        // TODO: validate SLD
        Request req = getRequest();
        String styleName = (String) req.getAttributes().get("style");
        try{
            File styleDir = GeoserverDataDirectory.findCreateConfigDir("styles");
            File tempSLDFile = new File(styleDir, styleName + ".sld.temp");
            File newSLDFile = new File(styleDir, styleName + ".sld");


            LOG.fine("Writing temporary SLD file to: " + newSLDFile);

            try{
                BufferedReader reader = 
                    new BufferedReader(
                            new InputStreamReader(
                                req.getEntity().getStream()
                                )
                            ); 
                BufferedWriter fw = new BufferedWriter(new FileWriter(tempSLDFile));

                String line;
                while ((line = reader.readLine()) != null){
                    fw.write(line);
                    fw.newLine();
                }

                fw.flush();
                fw.close();

                tempSLDFile.renameTo(newSLDFile);
            } catch (IOException ioe){
                LOG.severe("Problem writing temp file while PUTting a new style");
                // TODO: This should have an HTTP error code
                return;
            }

            try{

                StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
                SLDParser styleReader = new SLDParser(factory, newSLDFile.toURL());
                Style[] readStyles = null;
                Style newStyle;

                readStyles = styleReader.readXML();

                if (readStyles.length == 0){
                    // TODO: This should have an HTTP error code
                    LOG.severe("XML was valid, but no styles were found.  Please ensure that the submitted SLD validates against the SLD schema.");
                    return;
                }

                newStyle = readStyles[0];
                LOG.fine("SLD is " + newStyle);
            } catch (Exception e){
                LOG.severe("Error while trying to parse SLD file; bailing out");
                return;
            }

            StyleConfig style = null;

            if (myDC.getStyles().containsKey(styleName)){
                style = myDC.getStyle(styleName);
                myDC.removeStyle(styleName);
            } else {
                style = new StyleConfig();
            }
            style.setFilename(newSLDFile);

            style.setId(styleName);
            myDC.addStyle(style.getId(), style);

            saveConfiguration();
        } catch (ConfigurationException ce){
            LOG.severe("Couldn't find config directory!!" + ce);
            // TODO: These should have an HTTP error code.
        } 

        getResponse().setEntity(new StringRepresentation("AOK, style " + styleName + " created.", MediaType.TEXT_PLAIN));
    }

    public boolean allowDelete(){
        return true;
    }

    public void handleDelete(){
        String styleName = (String)getRequest().getAttributes().get("style");
        
        if (styleName != null && getDataConfig().getStyles().containsKey(styleName)){
            try{
                getDataConfig().removeStyle(styleName);
                saveConfiguration();
                getResponse().setEntity(
                        new StringRepresentation("Deleting style " + styleName,
                            MediaType.TEXT_PLAIN)
                        );
            } catch (Exception e){
                LOG.severe("Problem while deleting style " + styleName);
            }
        } else {
            LOG.severe("Delete Style failed because style " + styleName + " could not be found");
            // TODO: This should have an HTTP status code
        }
    }

    private void saveConfiguration() throws ConfigurationException{
        getData().load(getDataConfig().toDTO());
        XMLConfigWriter.store((DataDTO)getData().toDTO(),
            GeoserverDataDirectory.getGeoserverDataDirectory()
            );
    }
}
