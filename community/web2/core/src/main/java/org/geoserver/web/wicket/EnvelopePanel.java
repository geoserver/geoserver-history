package org.geoserver.web.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A form component for a {@link Envelope} object.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class EnvelopePanel extends FormComponentPanel {

    Form form;
    Double minX,minY,maxX,maxY;
    
    public EnvelopePanel(String id, Envelope e) {
        super(id, new Model(e));
        
        this.minX = e.getMinX();
        this.minY = e.getMinY();
        this.maxX = e.getMaxX();
        this.maxY = e.getMaxY();
        
        form = new Form( "form", new CompoundPropertyModel( this ) );
        add(form);
        
        form.add( new TextField( "minX", Double.class ) );
        form.add( new TextField( "minY", Double.class ) );
        form.add( new TextField( "maxX", Double.class ) );
        form.add( new TextField( "maxY", Double.class ) );
    }
   
    public void setReadOnly( final boolean readOnly ) {
        form.visitChildren( TextField.class, new org.apache.wicket.Component.IVisitor() {

            public Object component(Component component) {
                component.setEnabled( !readOnly );
                return null;
            }
        });
    }
    
    @Override
    public void updateModel() {
        form.process();
        setModelObject(new Envelope( minX, maxX, minY, maxY ));
    }
}
