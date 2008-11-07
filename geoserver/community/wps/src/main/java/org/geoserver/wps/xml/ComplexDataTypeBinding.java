package org.geoserver.wps.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wps.ComplexDataType;
import net.opengis.wps.WpsFactory;

import org.geoserver.wps.ComplexDataEncoderDelegate;
import org.geotools.wps.WPS;
import org.geotools.xml.AbstractComplexEMFBinding;

public class ComplexDataTypeBinding extends AbstractComplexEMFBinding {

    public ComplexDataTypeBinding( WpsFactory factory ) {
        super( factory );
    }
    
    public QName getTarget() {
        return WPS.ComplexDataType;
    }
    
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    @Override
    public List getProperties(Object object) throws Exception {
        ComplexDataType complex = (ComplexDataType) object;
        if ( !complex.getData().isEmpty() && complex.getData().get( 0 ) instanceof ComplexDataEncoderDelegate ) {
            ComplexDataEncoderDelegate delegate = (ComplexDataEncoderDelegate) complex.getData().get( 0 );
            List properties = new ArrayList();
            properties.add( new Object[]{ 
                delegate.getProcessParameterIO().getElement(), delegate } );
            
            return properties;
        }
        
        return null;
    }
}
