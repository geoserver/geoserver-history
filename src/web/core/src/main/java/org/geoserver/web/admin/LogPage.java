/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.logging.Level;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.NumberValidator;
import org.geoserver.config.GeoServerDataDirectory;
import org.geoserver.web.GeoServerSecuredPage;

/**
 * Shows the log file contents
 * @author Andrea Aime - OpenGeo
 */
public class LogPage extends GeoServerSecuredPage {
    static final String LINES = "lines";
    int lines = 1000;

    public LogPage(PageParameters params) {
        Form form = new Form("form");
        add(form);
        
        try {
            if(params.getKey(LINES) != null) {
                if(params.getInt(LINES) > 0) {
                   lines = params.getInt(LINES); 
                }
            }
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Error parsing the lines parameter: ", params.getKey(LINES));
        }
        
        form.add(new SubmitLink("refresh") {
            @Override
            public void onSubmit() {
                setResponsePage(LogPage.class, new PageParameters(LINES + "=" + String.valueOf(lines)));
            }
        });
        
        TextField lines = new TextField("lines", new PropertyModel(this, "lines"));
        lines.add(NumberValidator.POSITIVE);
        form.add(lines);
        
        TextArea logs = new TextArea("logs", new GSLogsModel());
        logs.setOutputMarkupId(true);
        logs.setMarkupId("logs");
        add(logs);
    }
    
    
    public class GSLogsModel extends LoadableDetachableModel {

        @Override
        protected Object load() {
            BufferedReader br = null;
            try {
                // locate the geoserver.log file
                GeoServerDataDirectory dd = getGeoServerApplication().getBeanOfType(GeoServerDataDirectory.class);
                File logDirectory = new File(dd.root(), "logs");
                if(!logDirectory.exists()) {
                    return "Could not find the logs directory inside the data dir: " + logDirectory.getAbsolutePath();
                }
                File logFile = new File(logDirectory, "geoserver.log");
                if(!logFile.exists()) {
                    return "Could not find the GeoServer log file: " + logFile.getAbsolutePath();
                }
                
                // load the logs line by line, keep only the last 1000 lines
                LinkedList<String> lineList = new LinkedList<String>();
                
                br = new BufferedReader(new FileReader(logFile));
                String line;
                while((line = br.readLine()) != null) {
                    lineList.addLast(line);
                    if(lineList.size() > LogPage.this.lines) {
                        lineList.removeFirst();
                    }
                }
                
                StringBuilder result = new StringBuilder();
                for (String logLine : lineList) {
                    result.append(logLine).append("\n");
                }
                return result;
            } catch(Exception e) {
                error(e);
                return e.getMessage();
            } finally {
                if(br != null) {
                    try {
                        br.close();
                    } catch(Exception e) {
                        // ignore
                    }
                }
            }
        }

    }
}
