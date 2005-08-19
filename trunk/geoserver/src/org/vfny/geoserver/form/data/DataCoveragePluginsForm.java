/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.opengis.coverage.grid.Format;
import org.vfny.geoserver.action.data.DataFormatUtils;


/**
 * Used to accept information from user for a New DataStore Action.
 * 
 * <p>
 * This form contains a convience property getDataStoreDescrptions() which is
 * simply to make writing the JSP easier.
 * </p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataDataStoresNewForm.java,v 1.8 2004/03/15 08:16:11 jive Exp $
 */
public class DataCoveragePluginsForm extends ActionForm {
    private static final Pattern idPattern = Pattern.compile("^\\a$");

	/**
	 * 
	 * @uml.property name="formats"
	 * @uml.associationEnd elementType="org.opengis.coverage.grid.Format" multiplicity=
	 * "(0 -1)"
	 */
	private List formats;

	/**
	 * 
	 * @uml.property name="formatDescriptions"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List formatDescriptions;

	/**
	 * 
	 * @uml.property name="formatIDs"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List formatIDs;

    
    /**
     * Default state of New form
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        formats = DataFormatUtils.listDataFormats();
        formatDescriptions = new ArrayList();
        formatIDs = new ArrayList();
        
        for(Iterator i = formats.iterator(); i.hasNext();) {
        	Format fTmp = (Format) i.next();
        	formatDescriptions.add(fTmp.getDescription());
        	formatIDs.add(fTmp.getName());
        }
    }


    /**
     * Check NewForm for correct use
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

	/**
	 * 
	 * @uml.property name="formatDescriptions"
	 */
	/*
	 * Allows the JSP page to easily access the list of dataFormat Descriptions
	 */
	public List getFormatDescriptions() {
		return formatDescriptions;
	}

	/**
	 * 
	 * @uml.property name="formatIDs"
	 */
	public List getFormatIDs() {
		return formatIDs;
	}

	/**
	 * 
	 * @uml.property name="formats"
	 */
	public List getFormats() {
		return formats;
	}

}
