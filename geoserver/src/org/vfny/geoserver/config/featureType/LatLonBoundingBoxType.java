/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.2</a>, using an
 * XML Schema.
 * $Id: LatLonBoundingBoxType.java,v 1.1 2002/08/22 15:34:24 robhranac Exp $
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
 * @version $Revision: 1.1 $ $Date: 2002/08/22 15:34:24 $
**/
public abstract class LatLonBoundingBoxType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _minx;

    private java.lang.String _miny;

    private java.lang.String _maxx;

    private java.lang.String _maxy;


      //----------------/
     //- Constructors -/
    //----------------/

    public LatLonBoundingBoxType() {
        super();
    } //-- org.vfny.geoserver.config.featureType.LatLonBoundingBoxType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getMaxx()
    {
        return this._maxx;
    } //-- java.lang.String getMaxx() 

    /**
    **/
    public java.lang.String getMaxy()
    {
        return this._maxy;
    } //-- java.lang.String getMaxy() 

    /**
    **/
    public java.lang.String getMinx()
    {
        return this._minx;
    } //-- java.lang.String getMinx() 

    /**
    **/
    public java.lang.String getMiny()
    {
        return this._miny;
    } //-- java.lang.String getMiny() 

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
     * @param maxx
    **/
    public void setMaxx(java.lang.String maxx)
    {
        this._maxx = maxx;
    } //-- void setMaxx(java.lang.String) 

    /**
     * 
     * @param maxy
    **/
    public void setMaxy(java.lang.String maxy)
    {
        this._maxy = maxy;
    } //-- void setMaxy(java.lang.String) 

    /**
     * 
     * @param minx
    **/
    public void setMinx(java.lang.String minx)
    {
        this._minx = minx;
    } //-- void setMinx(java.lang.String) 

    /**
     * 
     * @param miny
    **/
    public void setMiny(java.lang.String miny)
    {
        this._miny = miny;
    } //-- void setMiny(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
