package org.openplans.geoserver.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.openplans.geoserver.binding.KvpBinding;
import org.openplans.geoserver.binding.KvpRequestReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class RequestController implements Controller, ApplicationContextAware  {

	static final Logger logger = Logger.getLogger(RequestController.class);
	
	ApplicationContext context;
	
	public ModelAndView handleRequest(
		HttpServletRequest request, HttpServletResponse response
	) throws Exception {
	
		
		//parse kvp set
		Map kvp = KvpRequestReader.parseKvpSet(request.getQueryString());
		
		// parse into objects
		Collection bindings = 
			context.getParent().getBeansOfType(KvpBinding.class).values();
		Map parsedKvp = new HashMap();
		
		for (Iterator itr = kvp.entrySet().iterator(); itr.hasNext();) {
			Map.Entry entry = (Entry) itr.next();
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
			
			Object obj = null;
			for (Iterator bitr = bindings.iterator(); bitr.hasNext();) {
				KvpBinding binding = (KvpBinding) bitr.next();
				try {
					if (binding.canBind(key,value)) {
						obj = binding.bind(key,value);
						if (obj != null) {
							break;
						}
					}
				}
				catch(Throwable t) {
					//log and continue
					logger.warn("KvpBinding failure",t);
				}
			}
			
			if (obj != null) {
				parsedKvp.put(key,obj);
			}
			else parsedKvp.put(key,value);
		}
		
		return null;
	}
	
	public void setApplicationContext(ApplicationContext context) 
		throws BeansException {
		
		this.context = context;
	}
}
