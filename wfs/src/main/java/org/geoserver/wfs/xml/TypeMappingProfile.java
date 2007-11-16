/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.geotools.feature.type.ProfileImpl;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * A special profile of a pariticular {@link Schema} which maintains a unique
 * mapping of java class to xml schema type.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class TypeMappingProfile /*extends ProfileImpl*/ {
    
    /**
     * Set of profiles to do mappings from.
     */
    Set/*<Profile>*/ profiles;
    
//    public TypeMappingProfile(Schema schema, Set profile) {
//        super(schema, profile);
//    }

    public TypeMappingProfile(Set profiles) {
        this.profiles = profiles;
    }
    
    /**
     * Obtains the {@link AttributeDescriptor} mapped to a particular class.
     * <p>
     * If an exact match cannot be made, then those types which are supertypes
     * of clazz are examined.
     * </p>
     * @param clazz The class.
     *
     * @return The AttributeDescriptor, or <code>null</code> if no atttribute
     * type mapped to <code>clazz</code>
     */
    public AttributeDescriptor type(Class clazz) {
        ArrayList assignable = new ArrayList();

        for (Iterator p = profiles.iterator(); p.hasNext(); ) {
            ProfileImpl profile = (ProfileImpl) p.next();
            
            for (Iterator i = profile.values().iterator(); i.hasNext();) {
                AttributeDescriptor type = (AttributeDescriptor) i.next();
    
                if (type.getType().getBinding().isAssignableFrom(clazz)) {
                    assignable.add(type);
                }
    
                if (clazz.equals(type.getType())) {
                    return type;
                }
            }
        }

        if (assignable.isEmpty()) {
            return null;
        }

        if (assignable.size() == 1) {
            return (AttributeDescriptor) assignable.get(0);
        } else {
            //sort
            Comparator comparator = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        AttributeDescriptor a1 = (AttributeDescriptor) o1;
                        AttributeDescriptor a2 = (AttributeDescriptor) o2;

                        Class c1 = a1.getType().getBinding();
                        Class c2 = a2.getType().getBinding();
                        
                        if (c1.equals(c2)) {
                            return 0;
                        }

                        if (c1.isAssignableFrom(c2)) {
                            return 1;
                        }

                        return -1;
                    }
                };

            Collections.sort(assignable, comparator);

            if (!assignable.get(0).equals(assignable.get(1))) {
                return (AttributeDescriptor) assignable.get(0);
            }
        }

        return null;
    }

    /**
     * Obtains the {@link Name} of the {@link AttributeDescriptor} mapped
     * to a particular class.
     *
     * @param clazz The class.
     *
     * @return The Name, or <code>null</code> if no atttribute type mapped
     * to <code>clazz</code>
     */
    public Name name(Class clazz) {
        ArrayList assignable = new ArrayList();

        for (Iterator p = profiles.iterator(); p.hasNext(); ) {
            ProfileImpl profile = (ProfileImpl) p.next();
            
            for (Iterator i = profile.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                AttributeDescriptor type = (AttributeDescriptor) entry.getValue();
        
                if (type.getType().getBinding().isAssignableFrom(clazz)) {
                    assignable.add(entry);
                }
        
                if (clazz.equals(type.getType())) {
                    return (Name) entry.getKey();
                }
            }
        }

        if (assignable.isEmpty()) {
            return null;
        }

        if (assignable.size() == 1) {
            return (Name) ((Map.Entry) assignable.get(0)).getKey();
        } else {
            //sort
            Comparator comparator = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Map.Entry e1 = (Map.Entry) o1;
                        Map.Entry e2 = (Map.Entry) o2;

                        AttributeDescriptor a1 = (AttributeDescriptor) e1.getValue();
                        AttributeDescriptor a2 = (AttributeDescriptor) e2.getValue();

                        Class c1 = a1.getType().getBinding();
                        Class c2 = a2.getType().getBinding();
                        
                        if (c1.equals(c2)) {
                            return 0;
                        }

                        if (c1.isAssignableFrom(c2)) {
                            return 1;
                        }

                        return -1;
                    }
                };

            Collections.sort(assignable, comparator);

            Map.Entry e1 = (Map.Entry) assignable.get(0);
            Map.Entry e2 = (Map.Entry) assignable.get(1);
            AttributeDescriptor a1 = (AttributeDescriptor) e1.getValue();
            AttributeDescriptor a2 = (AttributeDescriptor) e2.getValue();

            if (!a1.getType().equals(a2.getType())) {
                return (Name) e1.getKey();
            }
        }

        return null;
    }
}
