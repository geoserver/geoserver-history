/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.File;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.form.data.StylesEditorForm;
import org.vfny.geoserver.global.UserContainer;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StylesEditorAction extends ConfigAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        StylesEditorForm stylesForm = (StylesEditorForm) form;
        FormFile file= stylesForm.getSldFile() ;
        final String filename = file.getFileName();
        final String styleID = stylesForm.getStyleID();
        
        StyleConfig style = user.getStyle();
        if( style == null ){
            // Must of bookmarked? Redirect so they can select            
            return mapping.findForward("config.data.style");            
        }
        File rootDir= new File(getServlet().getServletContext().getRealPath("/"));
        File styleDir= new File(rootDir, "data/styles");
        // send content of FormFile to /styles :
        // there nothing to keep the styles in memory for XMLConfigWriter.store() 
        InputStreamReader isr= new InputStreamReader(file.getInputStream()) ;
        FileWriter fw= new FileWriter( new File(styleDir,filename) ) ;
        char[] tampon= new char[1024] ;
        int charsRead ;
        while ((charsRead= isr.read(tampon,0,1024))!=-1) {
          fw.write(tampon,0,charsRead) ;
        }
        fw.flush() ;
        fw.close() ;
        isr.close() ;
        style.setFilename( new File(styleDir,filename) );
        style.setId(styleID);

        // Do configuration parameters here
        DataConfig config = (DataConfig) getDataConfig();        
        config.addStyle( style.getId(), style );
        getApplicationState().notifyConfigChanged();
        return mapping.findForward("config.data.style");
    }
}
