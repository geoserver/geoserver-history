package org.geoserver.catalog.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geotools.resources.Utilities;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Hibernate interceptor which forwards hibernate events to the catalog.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class HibernateCatalogInterceptor extends EmptyInterceptor
	implements ApplicationContextAware {

	/**
	 * application context
	 */
	ApplicationContext applicationContext;
	/**
	 * catalog
	 */
	HibernateCatalog catalog;
	
	public HibernateCatalogInterceptor() {
		
	}
	
	public HibernateCatalogInterceptor( HibernateCatalog catalog ) {
		this.catalog = catalog;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void setCatalog(HibernateCatalog catalog) {
		this.catalog = catalog;
	}
	
	protected HibernateCatalog catalog() {
		if ( catalog == null ) {
			synchronized ( this ) {
				if ( catalog == null ) {
					catalog = (HibernateCatalog) applicationContext.getBean( "catalog" );
				}
			}
		}
		
		return catalog;
	}
	public void afterTransactionCompletion(Transaction tx) {
		if ( !tx.wasRolledBack() ) {
			catalog().fireEvents( tx );
		}
		else {
			catalog().rollbackEvents( tx );
		}
	} 
	
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if ( id == null ) {
			//new object
			catalog().added( entity );
		}
		return false;
	}
	
	
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		catalog().removed( entity );
	}
	
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		List propertyNamesChanged = new ArrayList();
		List oldValues = new ArrayList();
		List newValues = new ArrayList();
		
		for ( int i = 0; i < propertyNames.length; i++) {
			Object oldValue = previousState[i];
			Object newValue = currentState[i];
			
			if ( !Utilities.equals( oldValue, newValue ) ) {
				propertyNamesChanged.add( propertyNames[i] );
				oldValues.add( oldValue );
				newValues.add( newValue );
			}
		}
		catalog().modified(entity, propertyNamesChanged, oldValues, newValues );
		return false;
	}
	
//	
}
