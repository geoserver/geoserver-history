/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver;

import junit.framework.TestCase;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;


public class BeanPersisterTest extends TestCase {
    File services;
    GenericApplicationContext context;

    GenericApplicationContext createContext() throws Exception {
        GeoServerResourceLoader loader = new GeoServerResourceLoader(services.getParentFile());

        context = new GenericApplicationContext();
        context.getBeanFactory().registerSingleton("resourceLoader", loader);

        URL xml = getClass().getResource("applicationContext.xml");

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new FileSystemResource(xml.getFile()));

        context.refresh();

        return context;
    }

    protected void setUp() throws Exception {
        File tmp = new File(System.getProperty("java.io.tmpdir"));
        System.out.println(tmp.getAbsolutePath());
        services = new File(tmp, "services");

        services.mkdir();

        context = createContext();
    }

    protected void tearDown() throws Exception {
        File[] files = services.listFiles();

        for (int i = 0; i < files.length; i++)
            assertTrue(files[i].delete());

        assertTrue(services.delete());
    }

    public void testSave() throws Exception {
        MyBean bean = (MyBean) context.getBean("myBean");
        bean.setFoo("bar");

        context.close();

        File beanFile = new File(services, "myBean.xml");
        assertTrue(beanFile.exists());

        Document beanDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(beanFile);
        Element beanElement = beanDoc.getDocumentElement();

        assertEquals(1, beanElement.getElementsByTagName("property").getLength());

        Element propElement = (Element) beanElement.getElementsByTagName("property").item(0);
        assertEquals("foo", propElement.getAttribute("name"));
        assertEquals("java.lang.String", propElement.getAttribute("type"));
        assertEquals("bar", propElement.getAttribute("value"));

        new File(services, "myBean.xml").delete();
    }

    public void testLoad() throws Exception {
        MyBean bean = (MyBean) context.getBean("myBean");
        assertEquals("foo", bean.getFoo());

        bean.setFoo("bar");
        //persist
        context.close();

        //recreate context, value should be loaded
        context = createContext();
        bean = (MyBean) context.getBean("myBean");
        assertEquals("bar", bean.getFoo());
    }

    public static class MyBean {
        String foo;

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public String getFoo() {
            return foo;
        }
    }
}
