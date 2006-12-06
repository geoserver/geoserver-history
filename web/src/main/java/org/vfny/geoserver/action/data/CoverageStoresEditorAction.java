/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.CoverageStoresEditorForm;
import org.vfny.geoserver.global.UserContainer;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class CoverageStoresEditorAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			UserContainer user, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		CoverageStoresEditorForm dataFormatsForm = (CoverageStoresEditorForm) form;
		String dataFormatID = dataFormatsForm.getDataFormatId();
		String namespace = dataFormatsForm.getNamespaceId();
		String type = dataFormatsForm.getType();
		String url = dataFormatsForm.getUrl();
		String description = dataFormatsForm.getDescription();

		DataConfig dataConfig = (DataConfig) getDataConfig();
		CoverageStoreConfig config = null;

		config = (CoverageStoreConfig) dataConfig.getDataFormat(dataFormatID);

		if (config == null) {
			// we are creating a new one.
			dataConfig.addDataFormat(getUserContainer(request)
					.getDataFormatConfig());
			config = (CoverageStoreConfig) dataConfig
					.getDataFormat(dataFormatID);
		}

		// /////////////////////////////////////////////////////////////////
		//
		// If we got here everything was fine then we can save the
		// configuration.
		//
		// ///////////////////////////////////////////////////////////////
		boolean enabled = dataFormatsForm.isEnabled();
		if (dataFormatsForm.isEnabledChecked() == false) {
			enabled = false;
		}
		config.setEnabled(enabled);
		config.setNameSpaceId(namespace);
		config.setType(type);
		config.setUrl(url);
		config.setAbstract(description);
		dataConfig.addDataFormat(config);
		getUserContainer(request).setDataFormatConfig(null);
		getApplicationState().notifyConfigChanged();
		return mapping.findForward("config.data.format");

	}

	/** Used to debug connection parameters */
	public void dump(String msg, Map params) {
		if (msg != null) {
			System.out.print(msg + " ");
		}
		System.out.print(": { ");
		for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			System.out.print(entry.getKey());
			System.out.print("=");
			dump(entry.getValue());
			if (i.hasNext()) {
				System.out.print(", ");
			}
		}
		System.out.println("}");
	}

	public void dump(Object obj) {
		if (obj == null) {
			System.out.print("null");
		} else if (obj instanceof String) {
			System.out.print("\"");
			System.out.print(obj);
			System.out.print("\"");
		} else {
			System.out.print(obj);
		}

	}

	public void dump(String msg, Object array[]) {
		if (msg != null) {
			System.out.print(msg + " ");
		}
		System.out.print(": ");
		if (array == null) {
			System.out.print("null");
			return;
		}
		System.out.print("(");
		for (int i = 0; i < array.length; i++) {
			dump(array[i]);
			if (i < array.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println(")");
	}
}
