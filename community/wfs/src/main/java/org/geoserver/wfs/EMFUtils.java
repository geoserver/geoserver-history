package org.geoserver.wfs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Utility methods for working with emf model objects.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class EMFUtils {

	/**
	 * Determines if an eobject has a particular property.
	 * 
	 * @param eobject The eobject.
	 * @param property The property to check for.
	 * 
	 * @return <code>true</code> if the property exists, otherwise <code>false</code.
	 */
	public static boolean has( EObject eobject, String property ) {
		return feature( eobject, property ) != null;
	}
	
	/**
	 * Sets a property of an eobject.
	 * 
	 * @param eobject THe object.
	 * @param property The property to set.
	 * @param value The value of the property.
	 */
	public static void set( EObject eobject, String property, Object value ) {
		EStructuralFeature feature = feature( eobject, property );
		eobject.eSet( feature, value );
	}
	
	/**
	 * Gets the property of an eobject.
	 * 
	 * @param eobject The object.
	 * @param property The property to get.
	 * 
	 * @return The value of the property.
	 */
	public static Object get( EObject eobject, String property ) {
		EStructuralFeature feature = feature( eobject, property );
		return eobject.eGet( feature );
	}
	
	/**
	 * Method which looks up a structure feature of an eobject, first doing 
	 * an exact name match, then a case insensitive one.
	 * 
	 * @param eobject The eobject.
	 * @param property The property
	 * 
	 * @return The structure feature, or <code>null</code> if not found.
	 */
	private static EStructuralFeature feature( EObject eobject, String property ) {
		EStructuralFeature feature = eobject.eClass().getEStructuralFeature( property );
		if ( feature != null ) 
			return feature;
		
		//do a case insentive check
		for ( Iterator itr = eobject.eClass().getEStructuralFeatures().iterator(); itr.hasNext(); ) {
			feature = (EStructuralFeature) itr.next();
			if ( feature.getName().equalsIgnoreCase( property ) ) {
				return feature;
			}
		}
		
		return null;
	}
	
	/**
	 * Sets a particular property on each {@link EObject} in a list to a particular value.
	 * <p>
	 * The following must hold:
	 * <code>
	 * objects.size() == values.size()
	 * </code>
	 * </p>
	 * 
	 * @param objects A list of {@link EObject}.
	 * @param property The property to set on each eobject in <code>objects</code>
	 * @param values The value to set on each eobjct in <code>objects</code>
	 */
	public static void set( List objects, String property, List values ) {
		
		for ( int i = 0; i < objects.size(); i++ ) {
			EObject eobject = (EObject) objects.get( i );
			set( eobject, property, values.get( i ) );
		}
	
	}
	
	/**
	 * Sets a particular property on each {@link EObject} in a list to a particular value.
	 * <p>
	 * 
	 * @param objects A list of {@link EObject}.
	 * @param property The property to set on each eobject in <code>objects</code>
	 * @param value The value to set on each eobjct in <code>objects</code>
	 */
	public static void set(List objects, String property, Object value ) {
		
		for ( int i = 0; i < objects.size(); i++ ) {
			EObject eobject = (EObject) objects.get( i );
			set( eobject, property, value );
		}
		
	}
	
	/**
	 * Obtains the values of a particular property on each {@link EObject} in a list.
	 * 
	 * @param objects A list of {@link EObject}.
	 * @param property The property to obtain.
	 * 
	 * @return The list of values.
	 */
	public static List get( List objects, String property ) {
		
		List values = new ArrayList();
		for ( int i = 0; i < objects.size(); i++ ) {
			EObject eobject = (EObject) objects.get( i );
			EStructuralFeature feature = feature( eobject, property );
			
			values.add( eobject.eGet( feature ) );
		}
		
		return values;
		
	}
	
	/**
	 * Determines if a particular propety has been set on an eobject.
	 * 
	 * @param eobjects The eobject.
	 * @param property The property to check.
	 * 
	 * @return <code>true</code> if the property has been set, otherwise <code>false</code>
	 */
	public static boolean isSet( EObject eobject, String property ) {
	
		EStructuralFeature feature = feature( eobject, property );
		return eobject.eIsSet( feature );
	
	}
	
	/**
	 * Determines if a particular propety has been set on each {@link EObject} in a list.
	 * 
	 * @param objects A list of {@link EObject}
	 * @param property The property to check.
	 * 
	 * @return <code>true</code> if every element in the list has been set, otherwise <code>false</code>
	 */
	public static boolean isSet( List objects, String property ) {
	
		for ( int i = 0; i < objects.size(); i++ ) {
			EObject eobject = (EObject) objects.get( i );
			if ( !isSet( eobject, property ) )
				return false;
		}
		
		return true;
	}

	/**
	 * Determines if a particular propety is unset on each {@link EObject} in a list.
	 * 
	 * @param objects A list of {@link EObject}
	 * @param property The property to check.
	 * 
	 * @return <code>true</code> if every element in the list is unset, otherwise <code>false</code>
	 */
	public static boolean isUnset( List objects, String property ) {
		
		for ( int i = 0; i < objects.size(); i++ ) {
			EObject eobject = (EObject) objects.get( i );
			if ( isSet( eobject, property ) )
				return false;
		
		}
		
		return true;
	}
	
	/**
	 * Clones an eobject.
	 * 
	 * @param prototype The object to be cloned from.
	 * @param factory The factory used to create the clone.
	 * 
	 * @return THe cloned object, with all properties the same to the original.
	 */
	public static EObject clone( EObject prototype, EFactory factory ) {
		EObject clone = factory.create( prototype.eClass() );
		for ( Iterator i = clone.eClass().getEStructuralFeatures().iterator(); i.hasNext(); ) {
			EStructuralFeature feature = (EStructuralFeature) i.next();
			clone.eSet( feature, prototype.eGet( feature ) );
		}
		
		return clone;
	}
}
