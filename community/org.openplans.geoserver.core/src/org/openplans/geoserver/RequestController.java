package org.openplans.geoserver;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
		
		//look up all kvp bindings
		Collection bindings = context.getBeansOfType(KvpBinding.class).values();
		
		//parsed kvp objects
		Map parsedKvp = new HashMap();
		
		//for each binding, locate they kvp it declares
		for (Iterator itr = bindings.iterator(); itr.hasNext();) {
			KvpBinding binding = (KvpBinding)itr.next();
			List keys = binding.getKeys();
			
			//create a map of just the keys that the binding declares
			HashMap m = new HashMap();
			for (Iterator kitr = keys.iterator(); kitr.hasNext();) {
				String key = (String)kitr.next();
				String value = (String) kvp.get(key.toUpperCase());
				if (value != null) {
					m.put(key,value);
				}
			}
			
			if (m.keySet().size() == keys.size()) {
				//all keys were found, perform the binding
				try {
					Object object = binding.bind(m);	
					if (object != null) {
						parsedKvp.put(binding.getKey(),object);
					}
				}
				catch(Throwable t) {
					logger.warn("Key value pair binding problem", t);
				}
			}
		}
		
		//figure out what the service and operation being performed
		String service = (String) kvp.get("service".toUpperCase());
		if (service == null) {
			logger.error("Could not determine service from request.");
			return null;
		}
		
		//look the string to an actual object
		Object serviceImpl = null;
		Map mappings = context.getBeansOfType(ServiceMapping.class);
		for (Iterator itr = mappings.values().iterator(); itr.hasNext();) {
			ServiceMapping mapping = (ServiceMapping)itr.next();
			String serviceBeanId = (String) mapping.getMappings().get(service);
			if (serviceBeanId != null) {
				serviceImpl = context.getBean(serviceBeanId);
				if (serviceImpl != null) 
					break;
			}
		}
		if (serviceImpl == null) {
			logger.error("Could not find implementation for service "+service);
			return null;
		}
		
		//figure out what operation is being called
		String operation = (String) kvp.get("request".toUpperCase());
		if (operation == null) {
			logger.error("Could not determine operation from request.");
			return null;
		}
		
		//find the method which maps to the request
		Method method = serviceImpl.getClass().getMethod(operation,null);
		if (method == null) {
			logger.error("Could not find implementation for opereation "+operation);
			return null;
		}
		
		//inject the service with parsed parameters
		for (Iterator itr = parsedKvp.entrySet().iterator(); itr.hasNext();) {
			Map.Entry entry = (Entry) itr.next();
			String key = (String) entry.getKey();
			Object val = entry.getValue();
			
			//convert to camel case
			key = Character.toUpperCase(key.charAt(0)) + key.substring(1);
			Method setter = serviceImpl.getClass()
				.getMethod("set" + key, new Class[]{val.getClass()});
			if (setter != null) {
				try {
					setter.invoke(serviceImpl,new Object[]{val});
				}
				catch(Throwable t) {
					String msg = "Error occured in " + serviceImpl.getClass() + 
						"#" + setter.getName();
					logger.error(msg,t);
				}	
			}
			else {
				logger.warn("No setter found for " + key);
			}
		}
		
		//invoke the operation
		try {
			method.invoke(serviceImpl,null);	
		}
		catch(Throwable t) {
			String msg = "Error occured perforing operation" + operation + 
				"on service" + service;
			logger.error(msg,t);
		}
		
		return null;
	}
	
	public void setApplicationContext(ApplicationContext context) 
		throws BeansException {
		
		this.context = context;
	}
}
