package org.vfny.geoserver.control.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.geoserver.data.DefaultGeoServerCatalog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.Resource;
import org.vfny.geoserver.control.IValidator;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.WFS;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

public class FullApplicationContext implements ApplicationContext {

	public WFSDTO wfsDTO = new WFSDTO();
	public WMSDTO wmsDTO = new WMSDTO();
	
	private Map beans;
	
	public FullApplicationContext( boolean wfsEnabled, boolean wmsEnabled ) {
		super();
		
		beans = new HashMap();
		
		beans.put("catalog", new DefaultGeoServerCatalog());
		beans.put("controller", new PreferenceStoreImpl(new BlankStore()));
		
		wfsDTO.setService(new ServiceDTO());
		wfsDTO.getService().setEnabled(wfsEnabled );
		beans.put("wfs", new WFS(wfsDTO));
		
		wmsDTO.setService(new ServiceDTO());
		wmsDTO.getService().setEnabled(wmsEnabled);
		beans.put("wms", new WMS(wmsDTO));
	}
	
	private class BlankStore implements IPreferenceStoreStrategy {

		public String get(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public String[] keys() {
			// TODO Auto-generated method stub
			return null;
		}

		public void put(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		public void setValidator(IValidator arg0) {
			// TODO Auto-generated method stub
			
		}

		public void unset(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApplicationContext getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getStartupDate() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void publishEvent(ApplicationEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean containsBeanDefinition(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getBeanDefinitionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String[] getBeanDefinitionNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getBeanDefinitionNames(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getBeanNamesForType(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getBeanNamesForType(Class arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getBeansOfType(Class arg0) throws BeansException {
		HashMap checkers = new HashMap();
		
		checkers.put("wfs", setContext(new WFSStatusChecker()));
		checkers.put("wms", setContext(new WMSStatusChecker()));
		checkers.put("data", setContext(new DataStatusChecker()));
		checkers.put("preference", setContext(new PreferenceStatusChecker()));
		
		return checkers;
	}
	
	public ApplicationContextAware setContext(ApplicationContextAware aware) {
		aware.setApplicationContext(this);
		return aware;
	}

	public Map getBeansOfType(Class arg0, boolean arg1, boolean arg2)
			throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsBean(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getAliases(String arg0)
			throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getBean(String arg0) throws BeansException {
		return beans.get(arg0);
	}

	public Object getBean(String arg0, Class arg1) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getType(String arg0) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSingleton(String arg0)
			throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	public BeanFactory getParentBeanFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage(MessageSourceResolvable arg0, Locale arg1)
			throws NoSuchMessageException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage(String arg0, Object[] arg1, Locale arg2)
			throws NoSuchMessageException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage(String arg0, Object[] arg1, String arg2,
			Locale arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	public Resource[] getResources(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public Resource getResource(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
