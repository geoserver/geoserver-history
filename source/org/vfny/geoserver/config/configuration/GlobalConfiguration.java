/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.2</a>, using an
 * XML Schema.
 * $Id: GlobalConfiguration.java,v 1.1 2002/03/28 18:56:34 robhranac Exp $
 */

package org.vfny.geoserver.config.configuration;

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
 * Comment describing your root element
 * @version $Revision: 1.1 $ $Date: 2002/03/28 18:56:34 $
**/
public class GlobalConfiguration implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _title;

    private java.lang.String _abstract;

    private java.lang.String _keywords;

    private java.lang.String _onlineResource;

    private java.lang.String _fees;

    private java.lang.String _accessConstraints;

    private java.lang.String _uRL;

    private java.lang.String _maintainer;


      //----------------/
     //- Constructors -/
    //----------------/

    public GlobalConfiguration() {
        super();
    } //-- org.vfny.geoserver.config.configuration.GlobalConfiguration()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getAbstract()
    {
        return this._abstract;
    } //-- java.lang.String getAbstract() 

    /**
    **/
    public java.lang.String getAccessConstraints()
    {
        return this._accessConstraints;
    } //-- java.lang.String getAccessConstraints() 

    /**
    **/
    public java.lang.String getFees()
    {
        return this._fees;
    } //-- java.lang.String getFees() 

    /**
    **/
    public java.lang.String getKeywords()
    {
        return this._keywords;
    } //-- java.lang.String getKeywords() 

    /**
    **/
    public java.lang.String getMaintainer()
    {
        return this._maintainer;
    } //-- java.lang.String getMaintainer() 

    /**
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getOnlineResource()
    {
        return this._onlineResource;
    } //-- java.lang.String getOnlineResource() 

    /**
    **/
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
    **/
    public java.lang.String getURL()
    {
        return this._uRL;
    } //-- java.lang.String getURL() 

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
     * @param _abstract
    **/
    public void setAbstract(java.lang.String _abstract)
    {
        this._abstract = _abstract;
    } //-- void setAbstract(java.lang.String) 

    /**
     * 
     * @param accessConstraints
    **/
    public void setAccessConstraints(java.lang.String accessConstraints)
    {
        this._accessConstraints = accessConstraints;
    } //-- void setAccessConstraints(java.lang.String) 

    /**
     * 
     * @param fees
    **/
    public void setFees(java.lang.String fees)
    {
        this._fees = fees;
    } //-- void setFees(java.lang.String) 

    /**
     * 
     * @param keywords
    **/
    public void setKeywords(java.lang.String keywords)
    {
        this._keywords = keywords;
    } //-- void setKeywords(java.lang.String) 

    /**
     * 
     * @param maintainer
    **/
    public void setMaintainer(java.lang.String maintainer)
    {
        this._maintainer = maintainer;
    } //-- void setMaintainer(java.lang.String) 

    /**
     * 
     * @param name
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param onlineResource
    **/
    public void setOnlineResource(java.lang.String onlineResource)
    {
        this._onlineResource = onlineResource;
    } //-- void setOnlineResource(java.lang.String) 

    /**
     * 
     * @param title
    **/
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * 
     * @param uRL
    **/
    public void setURL(java.lang.String uRL)
    {
        this._uRL = uRL;
    } //-- void setURL(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.vfny.geoserver.config.configuration.GlobalConfiguration unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.vfny.geoserver.config.configuration.GlobalConfiguration) Unmarshaller.unmarshal(org.vfny.geoserver.config.configuration.GlobalConfiguration.class, reader);
    } //-- org.vfny.geoserver.config.configuration.GlobalConfiguration unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
