/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.2</a>, using an
 * XML Schema.
 * $Id: MetadataURLType.java,v 1.1 2002/08/22 15:34:26 robhranac Exp $
 */

package org.vfny.geoserver.config.featureType;

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
 * @version $Revision: 1.1 $ $Date: 2002/08/22 15:34:26 $
**/
public abstract class MetadataURLType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _url;

    private java.lang.String _type;

    private java.lang.String _format;


      //----------------/
     //- Constructors -/
    //----------------/

    public MetadataURLType() {
        super();
    } //-- org.vfny.geoserver.config.featureType.MetadataURLType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getFormat()
    {
        return this._format;
    } //-- java.lang.String getFormat() 

    /**
    **/
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
    **/
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

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
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * @param format
    **/
    public void setFormat(java.lang.String format)
    {
        this._format = format;
    } //-- void setFormat(java.lang.String) 

    /**
     * 
     * @param type
    **/
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * 
     * @param url
    **/
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
