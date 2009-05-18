package org.geoserver.web.data.resource;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.geoserver.web.data.resource.BasicResourceConfig.ResourceNameValidator;

import junit.framework.TestCase;

public class ResourceNameValidatorTest extends TestCase {

    static class StringValidatable implements IValidatable {
        List<IValidationError> errors = new ArrayList<IValidationError>();
        String value;
        
        public StringValidatable(String value) {
            this.value = value;
        }

        public void error(IValidationError error) {
            errors.add(error);            
        }

        public Object getValue() {
            return value;
        }

        public boolean isValid() {
            return errors.size() == 0;
        }
        
    }
    
    public void testValidUnderscoreMiddle() {
        StringValidatable validatable = new StringValidatable("abc_def");
        new ResourceNameValidator().validate(validatable);
        assertTrue(validatable.isValid());
    }
    
    public void testValidUnderscoreStart() {
        StringValidatable validatable = new StringValidatable("_def");
        new ResourceNameValidator().validate(validatable);
        assertTrue(validatable.isValid());
    }
    
    public void testValidPoint() {
        StringValidatable validatable = new StringValidatable("abc.def");
        new ResourceNameValidator().validate(validatable);
        assertTrue(validatable.isValid());
    }
    
    public void testEmpty() {
        StringValidatable validatable = new StringValidatable("");
        new ResourceNameValidator().validate(validatable);
        assertFalse(validatable.isValid());
    }
    
    public void testSpace() {
        StringValidatable validatable = new StringValidatable("abc def");
        new ResourceNameValidator().validate(validatable);
        assertFalse(validatable.isValid());
    }
}
