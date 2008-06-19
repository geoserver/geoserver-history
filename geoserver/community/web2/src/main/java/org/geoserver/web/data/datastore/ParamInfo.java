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
        this.title = param.title == null? null : param.title.toString();
        this.password = param.isPassword();
        this.binding = param.type;
        this.required = param.required;
        this.value = param.sample;
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