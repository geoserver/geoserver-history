/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.coverage.grid.GridFormatFactorySpi;
import org.geotools.referencing.FactoryFinder;
import org.geotools.referencing.crs.GeographicCRS;
import org.opengis.coverage.grid.Format;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.action.data.DataFormatUtils;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;


/**
 * Represents the information required for editing a DataStore.
 * 
 * <p>
 * The parameters required by a DataStore are dynamically generated from the
 * DataStoreFactorySPI. Most use of DataStoreFactorySPI has been hidden behind
 * the DataStoreUtil class.
 * </p>
 *
 * @author Richard Gould, Refractions Research
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class DataFormatsEditorForm extends ActionForm {
    /** Help text for Params if available */
    private ArrayList paramHelp;

    /**
     * Used to identify the DataStore being edited. Maybe we should grab this
     * from session?
     */
    private String dataFormatId;

    /** Enabled status of DataStore */
    private boolean enabled;

    /* NamespaceID used for DataStore content */
    //private String namespaceId;
    private String type;
    private String url;

    /* Description of DataStore (abstract?) */
    private String description;

    // These are not stored in a single map so we can access them
    // easily from JSP page
    //

    /** String representation of connection parameter keys */
    private List paramKeys;

    /** String representation of connection parameter values */
    private List paramValues;

    //
    // More hacky attempts to transfer information into the JSP smoothly
    //

//    /** Available NamespaceIds */
//    private SortedSet namespaces;

    /**
     * Because of the way that STRUTS works, if the user does not check the
     * enabled box, or unchecks it, setEnabled() is never called, thus we must
     * monitor setEnabled() to see if it doesn't get called. This must be
     * accessible, as ActionForms need to know about it -- there is no way we
     * can tell whether we are about to be passed to an ActionForm or not.
     * Probably a better way to do this, but I can't think of one. -rgould
     */
    private boolean enabledChecked = false;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        enabledChecked = false;

        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

//        namespaces = new TreeSet(config.getNameSpaces().keySet());

        DataFormatConfig dfConfig = Requests.getUserContainer(request).getDataFormatConfig();

        if (dfConfig == null) {
            // something is horribly wrong no DataStoreID selected!
            // The JSP needs to not include us if there is no
            // selected DataStore
            //
            throw new RuntimeException(
                "selectedDataFormatId required in Session");
        }

        dataFormatId = dfConfig.getId();
        description = dfConfig.getAbstract();
        enabled = dfConfig.isEnabled();
        url = dfConfig.getUrl();
//        namespaceId = dfConfig.getNameSpaceId();
//        if (namespaceId.equals("")) {
//        	namespaceId = config.getDefaultNameSpace().getPrefix();
//        }

        //Retrieve connection params
        Format factory = dfConfig.getFactory();
        type = (dfConfig.getType() != null && dfConfig.getType().length() > 0 ? dfConfig.getType() : factory.getName());
        ParameterValueGroup params = factory.getReadParameters();

        if( params != null && params.values().size() > 0 ) {
            paramKeys = new ArrayList(params.values().size());
            paramValues = new ArrayList(params.values().size());
            paramHelp = new ArrayList(params.values().size());

            List list=params.values();
            Iterator it=list.iterator();
            ParameterDescriptor descr=null;
            ParameterValue val=null;
            while(it.hasNext())
            {
            	val = (ParameterValue)it.next();
            	if( val != null ) {
                	descr = (ParameterDescriptor)val.getDescriptor();
                	String key = descr.getName().toString();

                    if ("namespace".equals(key)) {
                        // skip namespace as it is *magic* and
                        // appears to be an entry used in all datastores?
                        //
                        continue;
                    }

                    Object value = dfConfig.getParameters().get(key);
                    String text;

                    if (value == null) {
                        text = null;
                    } else if (value instanceof String) {
                        text = (String) value;
                    } else {
                        text = value.toString();
                    }

                    paramKeys.add(key);
                    paramValues.add((text != null) ? text : "");
                    paramHelp.add(key);
            	}
            }
        }
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        // Selected DataStoreConfig is in session
        //
        UserContainer user = Requests.getUserContainer( request );
        DataFormatConfig dfConfig = user.getDataFormatConfig();
        //
        // dsConfig is the only way to get a factory
        Format factory = dfConfig.getFactory();
         ParameterValueGroup info = factory.getReadParameters();

        Map connectionParams = new HashMap();

        // Convert Params into the kind of Map we actually need
        //
        if( paramKeys != null) {
            for (int i = 0; i < paramKeys.size(); i++) {
                String key = (String) getParamKey(i);

                ParameterValue param = DataFormatUtils.find(info, key);

                if (param == null) {
                    errors.add("paramValue[" + i + "]",
                        new ActionError("error.dataFormatEditor.param.missing", key,
                            factory.getDescription()));

                    continue;
                }

                Object value = null;

                try {
    				if( key.equalsIgnoreCase("crs") ) {
						if( getParamValue(i) != null && ((String) getParamValue(i)).length() > 0 ) {
							CRSFactory crsFactory = FactoryFinder.getCRSFactory();
							CoordinateReferenceSystem crs = crsFactory.createFromWKT((String) getParamValue(i));
							value = crs;
						} else {
							CoordinateReferenceSystem crs = GeographicCRS.WGS84;
							value = crs;
						}
    				} else if( key.equalsIgnoreCase("envelope") ) {
    					
    				} else {
                    	Class[] clArray = {getParamValue(i).getClass()};
                    	Object[] inArray = {getParamValue(i)};
                    	value = param.getValue().getClass().getConstructor(clArray).newInstance(inArray);
    				}
                } catch (Exception e) {
                	value = null;
//                    errors.add("paramValue[" + i + "]",
//                            new ActionError("error.dataFormatEditor.param.parse", key,
//                            		getParamValue(i).getClass(), e));
                }

//                if ((value == null) && param.required) {
//                    errors.add("paramValue[" + i + "]",
//                        new ActionError("error.dataStoreEditor.param.required", key));
    //
//                    continue;
//                }

                if (value != null) {
                    connectionParams.put(key, value);
                }
            }        	
        }

        // put magic namespace into the mix
        //
        //connectionParams.put("namespace", getNamespaceId());

        dump("form", connectionParams );
        // Factory will provide even more stringent checking
        //
//        if (!factory.canProcess( connectionParams )) {
//            errors.add("paramValue",
//                new ActionError("error.datastoreEditor.validation"));
//        }

        return errors;
    }
    /** Used to debug connection parameters */
    public void dump( String msg, Map params ){
    	if( msg != null ){
    		System.out.print( msg + " ");
    	}
    	System.out.print( " connection params { " );
    	for( Iterator i=params.entrySet().iterator(); i.hasNext();){
    		Map.Entry entry = (Map.Entry) i.next();
    		System.out.print( entry.getKey() );
    		System.out.print("=");
    		if( entry.getValue()==null){
    			System.out.print("null");
    		}
    		else if ( entry.getValue() instanceof String){
    			System.out.print("\"");
    			System.out.print( entry.getValue() );
    			System.out.print("\"");
    		}
    		else {
    			System.out.print( entry.getValue() );
    		}
    		if( i.hasNext() ){
    			System.out.print( ", " );
    		}
    	}
    	System.out.println( "}" );
    }
    
    public Map getParams() {
        Map map = new HashMap();

        if( paramKeys != null ) {
            for (int i = 0; i < paramKeys.size(); i++) {
                map.put(paramKeys.get(i), paramValues.get(i));
                
            }
        }

        return map;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public List getParamKeys() {
        return paramKeys;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return
     */
    public String getParamKey(int index) {
        return (String) paramKeys.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return
     */
    public String getParamValue(int index) {
        return (String) paramValues.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     * @param value DOCUMENT ME!
     */
    public void setParamValues(int index, String value) {
        paramValues.set(index, value);
    }

    /**
     * getDataStoreId purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getDataFormatId() {
        return dataFormatId;
    }

    /**
     * getDescription purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * isEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

//    /**
//     * getNamespaces purpose.
//     * 
//     * <p>
//     * Description ...
//     * </p>
//     *
//     * @return
//     */
//    public SortedSet getNamespaces() {
//        return namespaces;
//    }

    /**
     * getParamValues purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public List getParamValues() {
        return paramValues;
    }

    /**
     * setDescription purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * setEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        setEnabledChecked(true);
        enabled = b;
    }

    /**
     * setParamKeys purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param list
     */
    public void setParamKeys(List list) {
        paramKeys = list;
    }

    /**
     * setParamValues purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param list
     */
    public void setParamValues(List list) {
        paramValues = list;
    }

//    /**
//     * getNamespaceId purpose.
//     * 
//     * <p>
//     * Description ...
//     * </p>
//     *
//     * @return
//     */
//    public String getNamespaceId() {
//        return namespaceId;
//    }
//
//    /**
//     * setNamespaceId purpose.
//     * 
//     * <p>
//     * Description ...
//     * </p>
//     *
//     * @param string
//     */
//    public void setNamespaceId(String string) {
//        namespaceId = string;
//    }

    /**
     * enabledChecked property
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabledChecked() {
        return enabledChecked;
    }

    /**
     * enabledChecked property
     *
     * @param b DOCUMENT ME!
     */
    public void setEnabledChecked(boolean b) {
        enabledChecked = b;
    }

    /**
     * Index property paramHelp
     *
     * @return DOCUMENT ME!
     */
    public String[] getParamHelp() {
        return (String[]) paramHelp.toArray(new String[paramHelp.size()]);
    }

    /**
     * Index property paramHelp
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getParamHelp(int index) {
        return (String) paramHelp.get(index);
    }
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
