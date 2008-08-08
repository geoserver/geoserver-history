package org.geoserver.wcs.web.data;

import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.web.data.ResourceConfigurationPanel;

@SuppressWarnings("serial")
public class WCSResourceConfigurationPanel extends ResourceConfigurationPanel {

    private List<String> selectedRequestSRSs;
    private List<String> selectedResponseSRSs;
    private List<String> selectedFormats;
    private List<String> selectedInterpolationMethods;
    private String newRequestSRS;
    private String newResponseSRS;
    private String newInterpolationMethod;
    private String newFormat;

    public WCSResourceConfigurationPanel(String id, IModel model){
        super(id, model);

        add(new ListMultipleChoice("requestSRS", 
                    new PropertyModel(this, "selectedRequestSRSs"), 
                    ((CoverageInfo)getResourceInfo()).getRequestSRS()
                    )
        );

        add(new TextField("newRequestSRS", new PropertyModel(this, "newRequestSRS")));

        add(new Button("deleteSelectedRequestSRSs"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getRequestSRS().removeAll(selectedRequestSRSs);
                selectedRequestSRSs.clear();
            }
        });

        add(new Button("addNewRequestSRS"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getRequestSRS().add(newRequestSRS);
                newRequestSRS = "";
            }
        });

        add (new ListMultipleChoice("responseSRS", 
                    new PropertyModel(this, "selectedResponseSRSs"),
                    ((CoverageInfo)getResourceInfo()).getResponseSRS()
                    )
        );

        add(new TextField("newResponseSRS", new PropertyModel(this, "newResponseSRS")));

        add(new Button("deleteSelectedResponseSRSs"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getResponseSRS().removeAll(selectedResponseSRSs);
                selectedResponseSRSs.clear();
            }
        });

        add(new Button("addNewResponseSRS"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getResponseSRS().add(newResponseSRS);
                newResponseSRS = "";
            }
        });

        add(new TextField("defaultInterpolationMethod"));
 
        add(new ListMultipleChoice("interpolationMethods", 
                    new PropertyModel(this, "selectedInterpolationMethods"), 
                    ((CoverageInfo)getResourceInfo()).getInterpolationMethods()
                    )
        );

        add(new TextField("newInterpolationMethod", new PropertyModel(this, "newInterpolationMethod")));

        add(new Button("deleteInterpolationMethods"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getInterpolationMethods().removeAll(selectedInterpolationMethods);
                selectedInterpolationMethods.clear();
            }
        });

        add(new Button("addNewInterpolationMethod"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getInterpolationMethods().add(newInterpolationMethod);
                newInterpolationMethod = "";
            }
        });

        add(new TextField("nativeFormat"));

        add(new ListMultipleChoice("supportedFormats",
                new PropertyModel(this, "selectedFormats"),
                ((CoverageInfo)getResourceInfo()).getSupportedFormats()
                )
        );

        add(new TextField("newFormat", new PropertyModel(this, "newFormat")));

        add(new Button("deleteSelectedFormats"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getSupportedFormats().removeAll(selectedFormats);
                selectedFormats.clear();
            }
        });

        add(new Button("addNewFormat"){
            public void onSubmit(){
                ((CoverageInfo)getResourceInfo()).getSupportedFormats().add(newFormat);
                newFormat = "";
            }
        });
   }
}
