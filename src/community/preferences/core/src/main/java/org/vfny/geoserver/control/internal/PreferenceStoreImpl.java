/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
package org.vfny.geoserver.control.internal;

import org.vfny.geoserver.control.IPreferenceStore;
import org.vfny.geoserver.control.IValidator;


/**
 * <b>NOT API.</b>  Default implementation that backs onto a {@link
 * IPreferenceStoreStrategy}.
 *
 * @author Jesse
 */
public class PreferenceStoreImpl implements IPreferenceStore {
    private static final IValidator TRUE_VALIDATOR = new IValidator(){

		public boolean isValid(String key, String value) {
			return true;
		}
    	
    };
	private IPreferenceStoreStrategy store;
    private IValidator validator;

    public PreferenceStoreImpl(IPreferenceStoreStrategy store) {
        this.store = store;
        store.setValidator(TRUE_VALIDATOR);
    }

    public IValidator getValidator() {
        return validator;
    }

    public void setValidator(IValidator validator) {
        store.setValidator(validator);
        this.validator = validator;
    }

    public boolean getBoolean(String key) {
        String tmp = store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return false;
        }

        if (tmp.equals("false")) {
            return false;
        }

        if (tmp.equals("true")) {
            return true;
        }

        throw new IllegalArgumentException(key + " is not a boolean. value="
            + tmp);
    }

    public char getChar(String key) {
        String tmp = store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return ' ';
        }

        if (tmp.length() > 1) {
            throw new IllegalArgumentException(key
                + "is not a character.  value=" + tmp);
        }

        return tmp.charAt(0);
    }

    public double getDouble(String key) {
        String tmp = store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return 0;
        }

        double d;

        try {
            d = Double.parseDouble(tmp);
        } catch (Exception e) {
            throw new IllegalArgumentException(key + "is not a double it is "
                + tmp);
        }

        return d;
    }

    public float getFloat(String key) {
        String tmp = store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return 0;
        }

        float d;

        try {
            d = Float.parseFloat(tmp);
        } catch (Exception e) {
            throw new IllegalArgumentException(key + "is not a float it is "
                + tmp);
        }

        return d;
    }

    public int getInt(String key) {
        String tmp = store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return 0;
        }

        int d;

        try {
            d = Integer.parseInt(tmp);
        } catch (Exception e) {
            throw new IllegalArgumentException(key + "is not a int it is "
                + tmp);
        }

        return d;
    }

    public long getLong(String key) {
        String tmp = store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return 0;
        }

        long d;

        try {
            d = Long.parseLong(tmp);
        } catch (Exception e) {
            throw new IllegalArgumentException(key + "is not a long it is "
                + tmp);
        }

        return d;
    }

    public String getString(String key) {
        String tmp = this.store.get(getKey(key, false));

        if (tmp == null) {
            tmp = this.store.get(getKey(key, true));
        }

        if (tmp == null) {
            return "";
        }

        return tmp;
    }

    public void set(String key, String value) {
        store.put(getKey(key, false), value);
    }

    public void set(String key, int value) {
        store.put(getKey(key, false), String.valueOf(value));
    }

    public void set(String key, float value) {
        store.put(getKey(key, false), String.valueOf(value));
    }

    public void set(String key, boolean value) {
        store.put(getKey(key, false), String.valueOf(value));
    }

    public void set(String key, char value) {
        store.put(getKey(key, false), String.valueOf(value));
    }

    public void set(String key, double value) {
        store.put(getKey(key, false), String.valueOf(value));
    }

    public void set(String key, long value) {
        store.put(getKey(key, false), String.valueOf(value));
    }

    private String getKey(String key, boolean getDefault) {
        if (getDefault) {
            return "@#$@3default__" + key;
        } else {
            return key;
        }
    }

    public void unset(String key) {
        store.unset(key);
    }

    public void setDefault(String key, String value) {
        store.put(getKey(key, true), value);
    }

    public void setDefault(String key, int value) {
        store.put(getKey(key, true), String.valueOf(value));
    }

    public void setDefault(String key, float value) {
        store.put(getKey(key, true), String.valueOf(value));
    }

    public void setDefault(String key, boolean value) {
        store.put(getKey(key, true), String.valueOf(value));
    }

    public void setDefault(String key, char value) {
        store.put(getKey(key, true), String.valueOf(value));
    }

    public void setDefault(String key, double value) {
        store.put(getKey(key, true), String.valueOf(value));
    }

    public void setDefault(String key, long value) {
        store.put(getKey(key, true), String.valueOf(value));
    }

    public String[] keys() {
        return store.keys();
    }
}
