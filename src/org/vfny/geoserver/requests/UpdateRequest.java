/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.filter.Filter;
import org.geotools.feature.FeatureType;
import org.geotools.feature.AttributeType;
import org.geotools.feature.SchemaException;
import org.vfny.geoserver.responses.WfsException;
import org.vfny.geoserver.responses.WfsTransactionException;

/**
 * This class represents an update request.  An update request consists
 * of one or more properties, which consist of a name and an attribute, and
 * a filter.  A property is represented by an inner class called Property,
 * which are added to the property list by calling add Property.  
 *  
 *
 * @version $VERSION$
 * @author Chris Holmes, TOPP
 */
public class UpdateRequest extends SubTransactionRequest {

    /** SubTransaction type.*/
    public static final short operationType = UPDATE;

       /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    /** The properties to be changed */
    private List properties;
    
    /** Specifies the features to change. */
    private Filter filter;
    
    /** Specifies the table of features */
    protected String typeName = null;
    

    /** Specifices the user-defined name for the entire get feature request */
    protected boolean releaseAll = true;

    /** Constructor */
    public UpdateRequest() {
	properties = new ArrayList();
    }

    /**
     * adds a property to this update object.  
     *
     * @param propertyName The name of the attribute that will be changed.
     * @param value The value to be changed.
     */
     public void addProperty(String propertyName, Object value) {
    	properties.add(new Property(propertyName, value));
    }

    /** Gets the name of the features to update. */
    public String getTypeName() { return typeName; }

    /** Sets the name of the features to update. */
    public void setTypeName(String typeName) { 
        this.typeName = typeName; }

    /** Gets whether all locked features should be released after
     this transaction, or only those that were affected. */ 
    public boolean getReleaseAll() { return releaseAll; }

    /** Sets  whether all locked features should be released after
     this transaction, or only those that were affected. */ 
    public void setReleaseAll(boolean releaseAll) { 
        this.releaseAll = releaseAll; }

    /** Sets the filter for this request. */
    public void setFilter(Filter filter)
        throws WfsException {
	this.filter = filter;
    }

    /** Gets the filter for this request */
    public Filter getFilter() {
	return filter;
    }

    /** Gets the list of property values. */
    public Object[] getValues(){
	int arrSize = properties.size();
	Object[] retArr = new Object[arrSize];
	for (int i = 0; i < arrSize; i++) {
	    retArr[i] = ((Property)properties.get(i)).getValue();
	}
	return retArr;
    }

    /** Gets the list of property names. */
    public String[] getPropertyNames() {
	int arrSize = properties.size();
	String[] retArr = new String[arrSize];
	for (int i = 0; i < arrSize; i++) {
	    retArr[i] = ((Property)properties.get(i)).getName();
	}
	return retArr;
    }
    
    /**
     * Gets the attribute types that correspond to the names of
     * properties in the passed in schema.
     *
     * @param schema Queried with property names to find the types.
     * @return an array of attribute types, in the same order as getValues.
     * @throws SchemaException if any of the names held by properties of
     * this request don't match the schema.
     */
    public AttributeType[] getTypes(FeatureType schema) 
	throws SchemaException {
	int arrSize = properties.size();
	AttributeType[] retArr = new AttributeType[arrSize];
	String curName;
	AttributeType curType;
	    for (int i = 0; i < arrSize; i++) {
		curName = ((Property)properties.get(i)).getName();
		curType = schema.getAttributeType(curName);
		if (curType == null) {
		    String message = "Could not find property named: " 
			+ curName + " in schema: " + schema.getTypeName();
		    throw new SchemaException(message);
		}
		retArr[i] = curType;
	    }
	return retArr;
    }
	
    /** Gets the subtransaction type. */
    public short getOpType() { return operationType; }

    /**
     * helper function for equals.  Checks for nulls, as
     * this class can hold nulls, and will be equal if
     * two of the fields are both null.
     *
     * @param mine The field of this object.
     * @param test the field of to test.
     *
     * @return true if mine equals test, including if they
     * are both null.
     */
    private boolean testField(Object mine, Object test) {
	if (mine != null) {
	    return mine.equals(test);
	} else {
	    return test == null;
	}
    }

    public boolean equals(Object obj) {
	if (obj != null && obj.getClass() == this.getClass()){
	    UpdateRequest testUpdate = (UpdateRequest)obj;
	    boolean isEqual = true;
	    isEqual = testField(this.filter, testUpdate.filter) && isEqual;
	    isEqual = testField(this.typeName, testUpdate.typeName) && isEqual;
	    isEqual = testField(this.handle, testUpdate.handle) && isEqual;
	    isEqual = (this.releaseAll == testUpdate.releaseAll) && isEqual;
	
	    if(this.properties.size() == testUpdate.properties.size()){
		    for(int i = 0; i < testUpdate.properties.size(); i++) {
			isEqual = isEqual && this.properties.contains
			    (testUpdate.properties.get(i));
		    }
	    } else {
		isEqual = false;
	    }
	    
	    return isEqual;
	} else {
	    return false;
	}
    }

     public String toString() {
	StringBuffer uRequest = new StringBuffer("TypeName: " + typeName);
	uRequest.append("\nhandle: " + handle);
	uRequest.append("\nReleaseAll: " + releaseAll);
	uRequest.append("\nfilter: " + filter + "\n");
	for (int i = 0; i < properties.size(); i++) {
	    uRequest.append(properties.get(i).toString() + "\n");
	}
	return uRequest.toString();
    }    

    /**
     * Private class representing a property of an update request.
     */
    private class Property {
	
	/** The name of the property, reflecting an attribute of a schema. */
	private String name;

	/** The new value that it should be changed to. */
	private Object value;

	/** constructor.*/
	public Property(String name, Object value) {
	    this.name = name;
	    this.value = value;
	}

	public void setValue(Object value) { this.value = value; }

	public Object getValue() { return value; }

	public void setName(String name) {this.name = name; }

	public String getName() { return name;}

	public boolean equals(Object obj) {
	    if (obj != null && 
		obj.getClass() == this.getClass()){
		Property prop = (Property)obj;
		return (testField(this.name, prop.getName()) &&
			testField(this.value, prop.getValue()));
		
	    } else {
		return false;
	    }
	}
	
	public String toString(){
	    return "Property - Name: " + name + ", Value: " + value;
	}

    }

}
