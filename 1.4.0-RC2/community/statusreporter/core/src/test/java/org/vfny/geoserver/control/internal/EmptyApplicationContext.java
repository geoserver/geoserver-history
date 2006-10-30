package org.vfny.geoserver.control.internal;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.Resource;

public class EmptyApplicationContext implements ApplicationContext {

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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
