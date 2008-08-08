
package org.vfny.geoserver.services;

import org.vfny.geoserver.control.IPreferenceStore;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.servlets.AbstractService;


/**
 * Abstract class that other services extend.
 *
 * @author Jesse
 */
public abstract class AbstractPreferenceService extends AbstractService {
    private IPreferenceStore controller;

    public AbstractPreferenceService(String arg0, String arg1, Service arg2) {
        super(arg0, arg1, arg2);
    }

    public IPreferenceStore getController() {
        return controller;
    }

    public void setController(IPreferenceStore controller) {
        this.controller = controller;
    }
}
