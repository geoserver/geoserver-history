/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.9.2</a>, using an
 * XML Schema.
 * $Id: FeatureType.java,v 1.1 2002/08/22 15:34:22 robhranac Exp $
 */ package org.vfny.geoserver.config.featureType;   

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 *
 * @version $Revision: 1.1 $ $Date: 2002/08/22 15:34:22 $
 **/
public class FeatureType extends org.vfny.geoserver.config.featureType.FeatureTypeType
    implements java.io.Serializable
{
    //----------------/
    //- Constructors -/
    //----------------/
    public FeatureType() {
        super();
    } 

    //-- org.vfny.geoserver.config.featureType.FeatureType()       //-----------/
    //- Methods -/
    //-----------/
    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid()
    /**
     *
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
       
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer)
    /**
     *
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
       
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler)
    /**
     *
     * @param reader
    **/
    public static org.vfny.geoserver.config.featureType.FeatureType unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.vfny.geoserver.config.featureType.FeatureType) Unmarshaller.unmarshal(org.vfny.geoserver.config.featureType.FeatureType.class, reader);
    } //-- org.vfny.geoserver.config.featureType.FeatureType unmarshal(java.io.Reader)
    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()
}
