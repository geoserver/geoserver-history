/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.form.data.DataDataStoresEditorForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataDataStoresEditorAction extends ConfigAction {

	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

		DataDataStoresEditorForm dataStoresForm = (DataDataStoresEditorForm) form;
		
		String dataStoreID = dataStoresForm.getDataStoreId();
		String namespace = dataStoresForm.getNamespaceId();
		String description = dataStoresForm.getDescription();

        DataConfig dataConfig = (DataConfig) getDataConfig();
        DataStoreConfig config = null;
        
        config = (DataStoreConfig) dataConfig.getDataStore(dataStoreID);


		// After extracting params into a map
		Map aMap = new HashMap();
        
        aMap.putAll(dataStoresForm.getParams());
        		
		// Test to see if they work. if not, send em back!
        try {
            //Promote the connectionParameters to their actual classes
            DataStoreFactorySpi factory = config.getFactory();
            /* 
            System.out.println( "before:"+aMap );
            System.out.println( "canProcess"+factory.canProcess( aMap ));
                        
            aMap = DataStoreUtils.toConnectionParams( factory,aMap);
            System.out.println( "after:"+aMap );
            System.out.println( "canProcess"+factory.canProcess( aMap ));            
            */
     /*       if( !factory.canProcess( aMap )){
                // We could not use these params!
                //
                ActionErrors errors = new ActionErrors();                
                errors.add( ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.cannotProcessConnectionParams")) ;
                saveErrors(request, errors);
                return mapping.findForward("dataConfigDataStores");    
            }
       */     
            DataStore victim = factory.createDataStore( aMap );
            System.out.println( "victim:"+victim);            
            if( victim == null ){
                // We *really* could not use these params!
                //
                ActionErrors errors = new ActionErrors();
                errors.add( ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.invalidConnectionParams")) ;
                saveErrors(request, errors);
                return mapping.findForward("dataConfigDataStores");                            
            }
        } catch (Throwable throwable) {
            ActionErrors errors = new ActionErrors();
            errors.add( ActionErrors.GLOBAL_ERROR,
                new ActionError("error.exception", throwable.getMessage())) ;
                
            saveErrors(request, errors);
            
            return mapping.findForward("dataConfigDataStores");
        }
						
		boolean enabled = dataStoresForm.isEnabled();
		if (dataStoresForm.isEnabledChecked() == false) {
			enabled = false;
		}

		config.setEnabled(enabled);
		config.setNameSpaceId(namespace);
		config.setAbstract(description);
		
		config.setConnectionParams(aMap);
		dataConfig.addDataStore(config);
        
        request.getSession().removeAttribute("selectedDataStoreId");			

		return mapping.findForward("dataConfigDataStores");
	}
		
}
