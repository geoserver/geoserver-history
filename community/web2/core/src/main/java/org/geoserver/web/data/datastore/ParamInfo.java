/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
/**
 * 
 */
package org.geoserver.web.data.datastore;

import java.io.Serializable;

import org.geotools.data.DataAccessFactory.Param;

/**
 * 
 * @author Gabriel Roldan
 */
public class ParamInfo implements Serializable {
    private final String name;

    private final String title;

    private boolean password;

    private Class binding;

    private boolean required;

    private Object value;

    public ParamInfo(Param param) {
        this.name = param.key;
        this.title = param.title == null ? null : param.title.toString();
        this.password = param.isPassword();
        if (Serializable.class.isAssignableFrom(param.type)) {
            this.binding = param.type;
            this.value = param.sample;
        } else {
            // handle the parameter as a string and let the DataStoreFactory
            // convert it to the appropriate type
            this.binding = String.class;
            this.value = param.sample == null? null : String.valueOf(param.sample);
        }
        this.required = param.required;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPassword() {
        return password;
    }

    public Class getBinding() {
        return binding;
    }

    public boolean isRequired() {
        return required;
    }
}