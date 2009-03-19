package org.geoserver.wcs.web.publish;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.web.publish.LayerConfigurationPanel;

/**
 * A configuration panel for CoverageInfo properties that related to WCS publication
 * @author Andrea Aime - OpenGeo
 *
 */
@SuppressWarnings("serial")
public class WCSLayerConfig extends LayerConfigurationPanel {

    private List<String> selectedRequestSRSs;
    private List<String> selectedResponseSRSs;
    private List<String> selectedFormats;
    private List<String> selectedInterpolationMethods;
    private String newRequestSRS;
    private String newResponseSRS;
    private String newInterpolationMethod;
    private String newFormat;

    public WCSLayerConfig(String id, IModel model){
        super(id, model);

        final CoverageInfo coverage = (CoverageInfo) getLayerInfo().getResource();
        add(new ListMultipleChoice("requestSRS", 
                    new PropertyModel(this, "selectedRequestSRSs"), 
                    coverage.getRequestSRS())
        );

        add(new TextField("newRequestSRS", new PropertyModel(this, "newRequestSRS")));

        add(new Button("deleteSelectedRequestSRSs"){
            public void onSubmit(){
                coverage.getRequestSRS().removeAll(selectedRequestSRSs);
                selectedRequestSRSs.clear();
            }
        });

        add(new Button("addNewRequestSRS"){
            public void onSubmit(){
                coverage.getRequestSRS().add(newRequestSRS);
                newRequestSRS = "";
            }
        });

        add (new ListMultipleChoice("responseSRS", 
                    new PropertyModel(this, "selectedResponseSRSs"),
                    coverage.getResponseSRS())
        );

        add(new TextField("newResponseSRS", new PropertyModel(this, "newResponseSRS")));

        add(new Button("deleteSelectedResponseSRSs"){
            public void onSubmit(){
                coverage.getResponseSRS().removeAll(selectedResponseSRSs);
                selectedResponseSRSs.clear();
            }
        });

        add(new Button("addNewResponseSRS"){
            public void onSubmit(){
                coverage.getResponseSRS().add(newResponseSRS);
                newResponseSRS = "";
            }
        });

        add(new TextField("defaultInterpolationMethod", new PropertyModel(coverage, "defaultInterpolationMethod")));
 
        add(new ListMultipleChoice("interpolationMethods", 
                    new PropertyModel(this, "selectedInterpolationMethods"), 
                    coverage.getInterpolationMethods()
                    )
        );

        add(new TextField("newInterpolationMethod", new PropertyModel(this, "newInterpolationMethod")));

        add(new Button("deleteInterpolationMethods"){
            public void onSubmit(){
                coverage.getInterpolationMethods().removeAll(selectedInterpolationMethods);
                selectedInterpolationMethods.clear();
            }
        });

        add(new Button("addNewInterpolationMethod"){
            public void onSubmit(){
                coverage.getInterpolationMethods().add(newInterpolationMethod);
                newInterpolationMethod = "";
            }
        });

        add(new TextField("nativeFormat", new PropertyModel(coverage, "nativeFormat")));

        add(new ListMultipleChoice("supportedFormats",
                new PropertyModel(this, "selectedFormats"),
                coverage.getSupportedFormats()
                )
        );

        add(new TextField("newFormat", new PropertyModel(this, "newFormat")));

        add(new Button("deleteSelectedFormats"){
            public void onSubmit(){
                coverage.getSupportedFormats().removeAll(selectedFormats);
                selectedFormats.clear();
            }
        });

        add(new Button("addNewFormat"){
            public void onSubmit(){
                coverage.getSupportedFormats().add(newFormat);
                newFormat = "";
            }
        });
   }
}
