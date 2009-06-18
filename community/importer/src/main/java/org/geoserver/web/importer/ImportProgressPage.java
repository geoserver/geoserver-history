package org.geoserver.web.importer;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.time.Duration;
import org.geoserver.importer.FeatureTypeImporter;
import org.geoserver.importer.ImportSummary;
import org.geoserver.importer.ImporterThreadManager;
import org.geoserver.web.GeoServerSecuredPage;

public class ImportProgressPage extends GeoServerSecuredPage {
    String importerId;
    Label percentage;
    Label currentFile;
    WebMarkupContainer info;
    
    public ImportProgressPage(String importerKey) {
        this.importerId = importerKey;
        
        FeatureTypeImporter importer = getImporter();
        
        // construction
        add(new Label("project", importer.getProject()));
        add(info = new WebMarkupContainer("info"));
        info.setOutputMarkupId(true);
        info.add(percentage = new Label("percentage", "0"));
        info.add(currentFile = new Label("currentFile", ""));
        add(new AjaxLink("cancel") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                FeatureTypeImporter importer = getImporter();
                importer.cancel();
                
                setResponsePage(new ImportSummaryPage(importer.getSummary()));
            }
            
        });
        
        info.add(new AbstractAjaxTimerBehavior(Duration.milliseconds(500)) {

            @Override
            protected void onTimer(AjaxRequestTarget target) {
                FeatureTypeImporter importer = getImporter();
                
                ImportSummary summary = importer.getSummary();
                if(summary.isCompleted())
                    setResponsePage(new ImportSummaryPage(summary));
                
                long perc = Math.round(100.0 * summary.getProcessedLayers() / summary.getTotalLayers());
                percentage.setModelObject(perc);
                currentFile.setModelObject(summary.getCurrentLayer());
                
                target.addComponent(info);
            }

            
            
        });
    }
    
    FeatureTypeImporter getImporter() {
        ImporterThreadManager manager = (ImporterThreadManager) getGeoServerApplication().getBean("importerPool");
        FeatureTypeImporter importer = manager.getImporter(importerId);
        return importer;
    }

}
