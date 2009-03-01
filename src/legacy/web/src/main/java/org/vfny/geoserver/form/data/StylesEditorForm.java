/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.RequestsLegacy;
import org.xml.sax.SAXException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StylesEditorForm extends ActionForm {
    private String styleID;
    private String filename; // kept, but not used
    private FormFile sldFile = null;
    private boolean _default;
    private boolean defaultChecked = false;
    private boolean defaultInitial;
    private String[] validationReport = null; // the SLD file with validation errors for it (saxexceptions)
    private String sldContents;
    private String action;

    /**
     *  okay this is a bit weird because of how struts html:checkbox works.
     *   1. if the "thing" is check, then the set method will be called (with "true")
     *   2. if the "thing" is not checked, then nothing happens!
     *
     *  So, struts says to always set the thing to "false" in the reset method.
     *    That way, if there's no event (ie. its unset), its state is false
     *    If there is an event, it'll be set to true.
     *
     *  Unforunately, this doesnt work well when you want a default value (the above give you a default value of false).
     *  To set the default to "true", then we need two variables.
     *   The main one, which you set to the default value.
     *   A secondary one that tells you if the user actually checked it.
     *
     *   In this way, the default value will be sent to the user, but they can uncheck it!
     */
    private boolean fullyValidate;
    private boolean fullyValidateChecked;
    private String originalStyleId;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        validationReport = null;
        super.reset(arg0, request);

        DataConfig config = ConfigRequests.getDataConfig(request);

        UserContainer user = RequestsLegacy.getUserContainer(request);
        StyleConfig style = user.getStyle();
        fullyValidate = true; //default value
        fullyValidateChecked = false; //required by html:checkbox

        if (style == null) {
            // Should not happen (unless they bookmark)
            styleID = "";
            originalStyleId = "";
            _default = config.getStyles().isEmpty();
            filename = "";
            sldFile = null;
        } else {
            styleID = style.getId();
            originalStyleId = style.getId();
            _default = style.isDefault();

            if (style.getFilename() != null) {
                filename = style.getFilename().getName();
            }

            sldFile = null;
        }

        sldContents = readSldFileContents(filename);

        defaultChecked = false;
        defaultInitial = _default;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((styleID == null) || styleID.equals("")) {
            errors.add("styleID", new ActionError("error.styleID.required", styleID));

            return errors;
        }

        if (!Pattern.matches("^[-\\w.:]*$", styleID)) {
            errors.add("styleID", new ActionError("error.styleID.invalid", styleID));

            return errors;
        }

        Boolean maxSize = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);

        if ((maxSize != null) && (maxSize.booleanValue())) {
            String size = null;
            ControllerConfig cc = mapping.getModuleConfig().getControllerConfig();

            if (cc == null) {
                size = Long.toString(CommonsMultipartRequestHandler.DEFAULT_SIZE_MAX);
            } else {
                size = cc.getMaxFileSize(); // struts-config : <controller maxFileSize="nK" />
            }

            errors.add("styleID", new ActionError("error.file.maxLengthExceeded", size));

            return errors;
        }

        //        if (this.getSldFile().getFileSize() == 0) { // filename not filed or file does not exist
        //            errors.add("styleID", new ActionError("error.file.required"));
        //
        //            return errors;
        //        }
        filename = this.getSldFile().getFileName();

        //Requests.getApplicationState(request);
        return errors;
    }

    /**
     * Access _default property.
     *
     * @return Returns the _default.
     */
    public boolean isDefault() {
        return _default;
    }

    /**
     * Set _default to _default.
     *
     * @param _default The _default to set.
     */
    public void setDefault(boolean _default) {
        defaultChecked = true;
        this._default = _default;
    }

    /**
     * Access filename property.
     *
     * @return Returns the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set filename to filename.
     *
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Access formfile property.
     *
     * @return Returns the formfile.
     */
    public FormFile getSldFile() {
        return this.sldFile;
    }

    /**
     * Set formfile to sldFile.
     *
     * @param filename The formfile to set.
     */
    public void setSldFile(FormFile filename) {
        this.sldFile = filename;
    }

    public boolean getFullyValidateChecked() {
        return fullyValidateChecked;
    }

    public void setFullyValidateChecked(boolean fullyValidateChecked) {
        this.fullyValidateChecked = fullyValidateChecked;
    }

    /**
     * Access fullyValidate property.
     *
     *  true -> validate against the xsd schema
     *
     * @return Returns the fullyValidate.
     */
    public boolean getFullyValidate() {
        return fullyValidate;
    }

    /**
     * Set fullyValidate to fullyValidate.
     *
     * @param fullyValidate The fullyValidate to set.
     */
    public void setFullyValidate(boolean fullyValidate) {
        fullyValidateChecked = true;
        this.fullyValidate = fullyValidate;
    }

    /**
     * Access styleID property.
     *
     * @return Returns the styleID.
     */
    public String getStyleID() {
        return styleID;
    }

    /**
     * Set styleID to styleID.
     *
     * @param styleID The styleID to set.
     */
    public void setStyleID(String styleID) {
        this.styleID = styleID;
    }

    /**
     * Does the magic with _default & defaultChecked.
     * <p>
     * Because of the way that STRUTS works, if the user does not check the default box,
     * or unchecks it, setDefault() is never called, thus we must monitor setDefault()
     * to see if it doesn't get called. This must be accessible, as ActionForms need to
     * know about it -- there is no way we can tell whether we are about to be passed to
     * an ActionForm or not.
     * </p>
     * @return true if default shoudl be selected
     */
    public boolean isDefaultValue() {
        if (defaultChecked) {
            return _default;
        }

        return defaultInitial;
    }

    public String getValidationReportIndexed(int i) {
        return validationReport[i];
    }

    public String[] getValidationReport() {
        return validationReport;
    }

    public void setValidationReport(String[] exs) {
        validationReport = exs;
    }

    public String readSldFileContents(String sldFileName) {
        if (sldFileName == null) {
            return "";
        }

        BufferedReader br = null;

        try {
            File styleDir = new File(GeoserverDataDirectory.getGeoserverDataDirectory(), "styles");
            File styleFile = new File(styleDir, sldFileName);
            br = new BufferedReader(new FileReader(styleFile));

            StringBuffer sb = new StringBuffer();
            String s;

            while ((s = br.readLine()) != null)
                sb.append(s.replaceAll("\t", "  ")).append("\n");

            br.close();

            return sb.toString();
        } catch (Throwable t) {
            // could not read the file contents
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }

            return "-";
        }
    }

    public String getSldContents() {
        return sldContents;
    }

    public void setSldContents(String sldContents) {
        this.sldContents = sldContents;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOriginalStyleId() {
        return originalStyleId;
    }
}
