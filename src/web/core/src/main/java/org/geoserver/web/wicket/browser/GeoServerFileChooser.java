package org.geoserver.web.wicket.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.filechooser.FileSystemView;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.vfny.geoserver.global.GeoserverDataDirectory;

@SuppressWarnings("serial")
public class GeoServerFileChooser extends Panel {
    FileBreadcrumbs breadcrumbs;
    FileDataView fileTable;
    IModel file;
    
    public GeoServerFileChooser(String id, IModel file) {
        super(id, file);

        this.file = file;
        
        // build the roots
        ArrayList<File> roots = new ArrayList<File>(Arrays.asList(File.listRoots()));
        Collections.sort(roots);
        // TODO: find a better way to deal with the data dir
        File dataDirectory = GeoserverDataDirectory.getGeoserverDataDirectory();
        roots.add(0, dataDirectory);
        
        // find under which root the selection should be placed
        File selection = (File) file.getObject();
        File selectionRoot = null;
        if(selection != null && selection.exists()) {
            for (File root : roots) {
                if(isSubfile(root, selection.getAbsoluteFile()))
                    selectionRoot = root;
            }
            
            // if the file is not part of the known search paths, give up 
            // and switch back to the data directory
            if(selectionRoot == null) {
                selectionRoot = dataDirectory;
                file = new Model(selectionRoot);
            }
        } else {
            selectionRoot = dataDirectory;
            file = new Model(selectionRoot);
        }
        
        
        
        // the root chooser
        final DropDownChoice choice = new DropDownChoice("roots", new Model(selectionRoot), new Model(roots), new FileRenderer());
        choice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                File selection = (File) choice.getModelObject();
                breadcrumbs.setRootFile(selection);
                updateFileBrowser(selection, target);
            }
            
        });
        choice.setOutputMarkupId(true);
        add(choice);
        
        // the breadcrumbs
        breadcrumbs = new FileBreadcrumbs("breadcrumbs", new Model(selectionRoot), file) {

            @Override
            protected void pathItemClicked(File file, AjaxRequestTarget target) {
                updateFileBrowser(file, target);
            }
            
        };
        breadcrumbs.setOutputMarkupId(true);
        add(breadcrumbs);
        
        // the file tables
        fileTable = new FileDataView("fileTable", new FileProvider(file)) {

            @Override
            protected void linkNameClicked(File file, AjaxRequestTarget target) {
                updateFileBrowser(file, target);
            }
            
        };
        fileTable.setOutputMarkupId(true);
        add(fileTable);
    }
    
    void updateFileBrowser(File file, AjaxRequestTarget target) {
        if(file.isDirectory()) {
            // explicitly change the root model, inform the other components the model has changed
            GeoServerFileChooser.this.file.setObject(file);
            fileTable.getProvider().setDirectory(new Model(file));
            breadcrumbs.setSelection(file);
            
            target.addComponent(fileTable);
            target.addComponent(breadcrumbs);
        }
    }

    private boolean isSubfile(File root, File selection) {
        if(selection == null || "".equals(selection.getPath()))
            return false;
        if(selection.equals(root))
            return true;
        
        return isSubfile(root, selection.getParentFile());
    }
    
    /**
     * 
     * @param fileFilter
     */
    public void setFilter(IModel fileFilter) {
        fileTable.provider.setFileFilter(fileFilter);
    }
    
    class FileRenderer implements IChoiceRenderer {

		public Object getDisplayValue(Object o) {
			File f = (File) o;
			return FileSystemView.getFileSystemView().getSystemDisplayName(f);
		}

		public String getIdValue(Object o, int count) {
			File f = (File) o;
			return "" + count;
		}
    	
    }
}
