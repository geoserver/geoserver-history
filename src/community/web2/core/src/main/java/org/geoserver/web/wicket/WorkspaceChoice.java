package org.geoserver.web.wicket;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.IConverter;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;

/**
 * A dropdown specialized in WorkspaceInfo selection
 * @author Andrea Aime - OpenGeo
 *
 */
@SuppressWarnings("serial")
public class WorkspaceChoice extends DropDownChoice {

    public WorkspaceChoice(String id, IModel model) {
        super(id, model, new WorkspacesModel());
    }
    
    @Override
    public IConverter getConverter(Class type) {
        return new IConverter() {
        
            public String convertToString(Object value, Locale locale) {
                return ((WorkspaceInfo) value).getName();
            }
        
            public Object convertToObject(String value, Locale locale) {
                Catalog catalog = GeoServerApplication.get().getCatalog();
                return catalog.getWorkspace(value);
            }
        };
    }
    
    static class WorkspacesModel extends LoadableDetachableModel {
        @Override
        protected Object load() {
            Catalog catalog = GeoServerApplication.get().getCatalog();
            List<WorkspaceInfo> workspaces = catalog.getWorkspaces();
            return workspaces;
        }
    }

}
