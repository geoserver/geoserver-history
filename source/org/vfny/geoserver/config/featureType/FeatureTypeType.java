/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.2</a>, using an
 * XML Schema.
 * $Id: FeatureTypeType.java,v 1.1 2002/03/01 21:36:22 robhranac Exp $
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
 * @version $Revision: 1.1 $ $Date: 2002/03/01 21:36:22 $
**/
public abstract class FeatureTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _title;

    private java.lang.String _abstract;

    private java.lang.String _keywords;

    private java.lang.String _sRS;

    private java.lang.String _operations;

    private LatLonBoundingBox _latLonBoundingBox;

    private MetadataURL _metadataURL;

    private java.lang.String _host;

    private java.lang.String _port;

    private java.lang.String _databaseName;

    private java.lang.String _user;

    private java.lang.String _password;


      //----------------/
     //- Constructors -/
    //----------------/

    public FeatureTypeType() {
        super();
    } //-- org.vfny.geoserver.config.featureType.FeatureTypeType()


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
    public java.lang.String getDatabaseName()
    {
        return this._databaseName;
    } //-- java.lang.String getDatabaseName() 

    /**
    **/
    public java.lang.String getHost()
    {
        return this._host;
    } //-- java.lang.String getHost() 

    /**
    **/
    public java.lang.String getKeywords()
    {
        return this._keywords;
    } //-- java.lang.String getKeywords() 

    /**
    **/
    public LatLonBoundingBox getLatLonBoundingBox()
    {
        return this._latLonBoundingBox;
    } //-- LatLonBoundingBox getLatLonBoundingBox() 

    /**
    **/
    public MetadataURL getMetadataURL()
    {
        return this._metadataURL;
    } //-- MetadataURL getMetadataURL() 

    /**
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getOperations()
    {
        return this._operations;
    } //-- java.lang.String getOperations() 

    /**
    **/
    public java.lang.String getPassword()
    {
        return this._password;
    } //-- java.lang.String getPassword() 

    /**
    **/
    public java.lang.String getPort()
    {
        return this._port;
    } //-- java.lang.String getPort() 

    /**
    **/
    public java.lang.String getSRS()
    {
        return this._sRS;
    } //-- java.lang.String getSRS() 

    /**
    **/
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
    **/
    public java.lang.String getUser()
    {
        return this._user;
    } //-- java.lang.String getUser() 

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
     * @param _abstract
    **/
    public void setAbstract(java.lang.String _abstract)
    {
        this._abstract = _abstract;
    } //-- void setAbstract(java.lang.String) 

    /**
     * 
     * @param databaseName
    **/
    public void setDatabaseName(java.lang.String databaseName)
    {
        this._databaseName = databaseName;
    } //-- void setDatabaseName(java.lang.String) 

    /**
     * 
     * @param host
    **/
    public void setHost(java.lang.String host)
    {
        this._host = host;
    } //-- void setHost(java.lang.String) 

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
     * @param latLonBoundingBox
    **/
    public void setLatLonBoundingBox(LatLonBoundingBox latLonBoundingBox)
    {
        this._latLonBoundingBox = latLonBoundingBox;
    } //-- void setLatLonBoundingBox(LatLonBoundingBox) 

    /**
     * 
     * @param metadataURL
    **/
    public void setMetadataURL(MetadataURL metadataURL)
    {
        this._metadataURL = metadataURL;
    } //-- void setMetadataURL(MetadataURL) 

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
     * @param operations
    **/
    public void setOperations(java.lang.String operations)
    {
        this._operations = operations;
    } //-- void setOperations(java.lang.String) 

    /**
     * 
     * @param password
    **/
    public void setPassword(java.lang.String password)
    {
        this._password = password;
    } //-- void setPassword(java.lang.String) 

    /**
     * 
     * @param port
    **/
    public void setPort(java.lang.String port)
    {
        this._port = port;
    } //-- void setPort(java.lang.String) 

    /**
     * 
     * @param sRS
    **/
    public void setSRS(java.lang.String sRS)
    {
        this._sRS = sRS;
    } //-- void setSRS(java.lang.String) 

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
     * @param user
    **/
    public void setUser(java.lang.String user)
    {
        this._user = user;
    } //-- void setUser(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
