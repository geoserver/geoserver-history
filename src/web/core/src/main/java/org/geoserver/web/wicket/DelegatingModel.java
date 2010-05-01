/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public class DelegatingModel implements IModel {
    Component myComponent; 

    public DelegatingModel(Component c){
        myComponent = c;
    }

    public Object getObject(){
        return myComponent.getModel().getObject();
    }

    public void setObject(Object o){
        myComponent.getModel().setObject(o);
    }

    public void detach(){
        myComponent.getModel().detach();
    }
}


