package org.geoserver.monitor.web;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.validation.validator.NumberValidator;

public class DateField extends FormComponentPanel {

    TextField year;
    DropDownChoice month;
    TextField day;
    TextField hour;
    TextField minute;
    
    public DateField(String id) {
        this(id, Calendar.getInstance());
    }
    
    public DateField(String id, Date date) {
        this(id, toCalendar(date));
    }
    
    static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal; 
    }
    
    public DateField(String id, Calendar cal) {
        super(id, new Model(cal));
        
        add( year = field("year", "YEAR", new int[]{0, 9999}, -1, cal));
        
        add(month = new DropDownChoice("month", new CalendarPropertyModel(cal, "MONTH"), 
            Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11), new IChoiceRenderer() {
                
            public String getIdValue(Object object, int index) {
                return (String) getDisplayValue(object);
            }
            
            public Object getDisplayValue(Object object) {
                Integer i = Integer.parseInt(object.toString());
                switch(i) {
                    case 0: return "Jan";
                    case 1: return "Feb";
                    case 2: return "Mar";
                    case 3: return "Apr";
                    case 4: return "May";
                    case 5: return "Jun";
                    case 6: return "Jul";
                    case 7: return "Aug";
                    case 8: return "Sep";
                    case 9: return "Oct";
                    case 10: return "Nov";
                    case 11: return "Dec";
                    default: throw new IllegalArgumentException();
                }
            }
        }));
        
        add( day = field("day", "DAY_OF_MONTH", new int []{1, 31}, 2, cal));
        add( hour = field("hour", "HOUR_OF_DAY", new int []{0, 23}, 2, cal));
        add( minute = field("minute", "MINUTE", new int[]{0,59}, 2, cal)); 
    }
    
    public Calendar getDate() {
        return (Calendar) getModel().getObject();
    }
    
    TextField field(String id, String field, int[] range, int len, Calendar cal) {
        IModel model = new CalendarPropertyModel(cal, field);
        if (len > 0) {
            model = new PrePaddingModel(model, len); 
        }
        return (TextField) new TextField( id, model, Integer.class)
            .add(new NumberValidator.RangeValidator(range[0], range[1]));
    }
    
    static class PrePaddingModel extends Model {
        
        IModel delegate;
        int len;
        
        public PrePaddingModel(IModel delegate, int len) {
            this.delegate = delegate;
            this.len = len;
        }
        
        @Override
        public void setObject(Object object) {
            delegate.setObject(Integer.parseInt(object.toString()));
        }
        
        @Override
        public Object getObject() {
            return prepad(delegate.getObject());
        }
        
        Object prepad(Object val) {
            StringBuffer sb = new StringBuffer(val.toString());
            while(sb.length() < len) {
                sb.insert(0,0);
            }
            return sb.toString();
        }
    }
    static class CalendarPropertyModel extends AbstractPropertyModel {

        String property;
        
        public CalendarPropertyModel(Calendar cal, String property) {
            super(cal);
            this.property = property;
        }

        @Override
        protected String propertyExpression() {
            return property;
        }
        
        @Override
        public Object getObject() {
            Calendar cal = (Calendar) getTarget();
            return cal.get(field());
        }
        
        @Override
        public void setObject(Object object) {
            Calendar cal = (Calendar) getTarget();
            cal.set(field(), (Integer) object);
        }
        
        int field() {
            try {
                Field field = Calendar.class.getField(property);
                return (Integer) field.get(null);
            } 
            catch(Exception e) {
                throw new WicketRuntimeException("No such calendar field " + property, e);
            }
            
        }
    }
}
