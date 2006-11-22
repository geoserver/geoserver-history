package org.geoserver.ows;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.xml.AbstractComplexBinding;

public abstract class ComplexEMFBinding extends AbstractComplexBinding {

	public Object getProperty(Object object, QName name) throws Exception {
		EObject eObject = (EObject) object;
		if ( EMFUtils.has( eObject, name.getLocalPart() ) ) {
			return EMFUtils.get( eObject, name.getLocalPart() );
		}
		
		return null;
	}
}
