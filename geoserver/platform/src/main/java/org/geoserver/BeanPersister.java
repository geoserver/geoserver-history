/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.propertyeditors.ByteArrayPropertyEditor;
import org.springframework.beans.propertyeditors.CharacterEditor;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.FileEditor;
import org.springframework.beans.propertyeditors.InputStreamEditor;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Persists java beans.
 * <p>
 * Instances of this class are declared in a spring context and reference the
 * id's of beans they are to persist. Example:
 *
 * <pre>
 *         <code>
 *                 <bean id="myBean" class="org.xyz.MyBean"/>
 *
 *                 <bean id="myBeanPersister" class="org.geoserver.BeanPersister">
 *                         <constructor-arg ref="resourceLoader"/>
 *                         <property name="beans">
 *                                 <list>
 *                                         <value>myBean</value>
 *                                 </list>
 *                         </property>
 *                 </bean>
 *
 *         </code>
 * </pre>
 *
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class BeanPersister implements BeanFactoryPostProcessor, ApplicationListener {
    static Map editors;

    static {
        editors = new HashMap();

        //Simple editors, without parameterization capabilities.
        // The JDK does not contain a default editor for any of these target types.
        editors.put(byte[].class, new ByteArrayPropertyEditor());
        editors.put(Class.class, new ClassEditor());
        editors.put(File.class, new FileEditor());
        editors.put(InputStream.class, new InputStreamEditor());
        editors.put(Locale.class, new LocaleEditor());
        editors.put(Properties.class, new PropertiesEditor());
        editors.put(Resource[].class, new ResourceArrayPropertyEditor());
        editors.put(String[].class, new StringArrayPropertyEditor());
        editors.put(URL.class, new URLEditor());

        // Default instances of collection editors.
        // Can be overridden by registering custom instances of those as custom editors.
        editors.put(Collection.class, new CustomCollectionEditor(Collection.class));
        editors.put(Set.class, new CustomCollectionEditor(Set.class));
        editors.put(SortedSet.class, new CustomCollectionEditor(SortedSet.class));
        editors.put(List.class, new CustomCollectionEditor(List.class));

        // Default instances of character and boolean editors.
        // Can be overridden by registering custom instances of those as custom editors.
        PropertyEditor characterEditor = new CharacterEditor(false);
        PropertyEditor booleanEditor = new CustomBooleanEditor(false);

        // The JDK does not contain a default editor for char!
        editors.put(char.class, characterEditor);
        editors.put(Character.class, characterEditor);

        // Spring's CustomBooleanEditor accepts more flag values than the JDK's default editor.
        editors.put(boolean.class, booleanEditor);
        editors.put(Boolean.class, booleanEditor);

        // The JDK does not contain default editors for number wrapper types!
        // Override JDK primitive number editors with our own CustomNumberEditor.
        PropertyEditor byteEditor = new CustomNumberEditor(Byte.class, false);
        PropertyEditor shortEditor = new CustomNumberEditor(Short.class, false);
        PropertyEditor integerEditor = new CustomNumberEditor(Integer.class, false);
        PropertyEditor longEditor = new CustomNumberEditor(Long.class, false);
        PropertyEditor floatEditor = new CustomNumberEditor(Float.class, false);
        PropertyEditor doubleEditor = new CustomNumberEditor(Double.class, false);

        editors.put(byte.class, byteEditor);
        editors.put(Byte.class, byteEditor);

        editors.put(short.class, shortEditor);
        editors.put(Short.class, shortEditor);

        editors.put(int.class, integerEditor);
        editors.put(Integer.class, integerEditor);

        editors.put(long.class, longEditor);
        editors.put(Long.class, longEditor);

        editors.put(float.class, floatEditor);
        editors.put(Float.class, floatEditor);

        editors.put(double.class, doubleEditor);
        editors.put(Double.class, doubleEditor);

        editors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, false));
        editors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, false));
    }

    /**
     * Logger
     */
    Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Loading resources.
     */
    GeoServerResourceLoader loader;

    /**
     * List of bean id's to persist.
     */
    List beans;

    public BeanPersister(GeoServerResourceLoader loader) {
        this.loader = loader;
        beans = new ArrayList();
    }

    /**
     * Sets the list of beans to persist.
     *
     * @param beans List of bean id's, as strings.
     */
    public void setBeans(List beans) {
        this.beans = beans;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
        //TODO: register custom property editors
        try {
            //all bean info is kept in services directory
            File services = loader.find("services");

            if (services == null) {
                return;
            }

            if (services.isDirectory()) {
                for (Iterator b = beans.iterator(); b.hasNext();) {
                    String bean = (String) b.next();
                    loadBean(bean, beanFactory);
                }
            }
        } catch (IOException e) {
            String msg = "Error loading services";
            throw new BeanInitializationException(msg, e);
        }
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            ApplicationContext context = (ApplicationContext) event.getSource();

            //look up beans and persist them
            for (Iterator b = beans.iterator(); b.hasNext();) {
                String beanId = (String) b.next();
                Object bean = context.getBean(beanId);

                if (bean != null) {
                    saveBean(beanId, bean, context);
                } else {
                    logger.warning("Could not locate " + beanId);
                }
            }
        }
    }

    void saveBean(String id, Object bean, ApplicationContext context) {
        OutputStream os = null;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = builder.newDocument();
            Element root = doc.createElement("bean");
            root.setAttribute("id", id);
            doc.appendChild(root);

            Method[] methods = bean.getClass().getDeclaredMethods();

            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];

                //is hte method a getter?
                if (method.getName().startsWith("get") && (method.getParameterTypes().length == 0)
                        && (method.getReturnType() != null)) {
                    String propName = method.getName().substring(3);

                    if ("".equals(propName)) {
                        continue;
                    }

                    //shere needs to be a setter as well
                    Method setter = bean.getClass()
                                        .getMethod("set" + propName,
                            new Class[] { method.getReturnType() });

                    if (setter == null) {
                        continue;
                    }

                    propName = Character.toLowerCase(propName.charAt(0)) + propName.substring(1);

                    Class type = method.getReturnType();

                    //TODO: register any custom editors
                    PropertyEditor editor = editor(type);

                    if (editor != null) {
                        editor.setValue(method.invoke(bean, null));

                        String text = editor.getAsText();

                        Element propElement = doc.createElement("property");
                        propElement.setAttribute("name", propName);
                        propElement.setAttribute("value", text);
                        propElement.setAttribute("type", type.getName());
                        root.appendChild(propElement);
                    } else {
                        String msg = "Could not find editor for bean=" + id + ", property="
                            + propName;
                        logger.warning(msg);
                    }
                }
            }

            //save out the bean
            Transformer tx = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(root);

            File services = loader.createDirectory("services");
            os = new BufferedOutputStream(new FileOutputStream(new File(services, id + ".xml")));

            StreamResult result = new StreamResult(os);

            tx.transform(source, result);
            os.close();
        } catch (Exception e) {
            String msg = "Error persisting bean: " + id;
            logger.log(Level.WARNING, msg, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }

    void loadBean(String id, ConfigurableListableBeanFactory factory) {
        try {
            File xml = loader.find("services" + File.separator + id + ".xml");

            if (xml == null) {
                logger.warning("Could not persistance file for bean: " + id);

                return;
            }

            BeanDefinition beanDef = factory.getBeanDefinition(id);

            if (beanDef == null) {
                logger.warning("Could not find bean definition for: " + id);
            }

            //read property file
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xml);

            //read all properties
            NodeList propList = doc.getElementsByTagName("property");

            for (int i = 0; i < propList.getLength(); i++) {
                Element propElement = (Element) propList.item(i);
                String name = propElement.getAttribute("name");
                String valu = propElement.getAttribute("value");
                String type = propElement.getAttribute("type");

                //TODO: classloading issues?
                Class clazz = Class.forName(type);

                beanDef.getPropertyValues().removePropertyValue(name);
                beanDef.getPropertyValues().addPropertyValue(name, new TypedStringValue(valu, clazz));
            }
        } catch (Exception e) {
            String msg = "Error reading bean definition for " + id;
            logger.log(Level.WARNING, msg, e);
        }
    }

    PropertyEditor editor(Class type) {
        if (editors.containsKey(type)) {
            return (PropertyEditor) editors.get(type);
        }

        return PropertyEditorManager.findEditor(type);
    }
}
