/* Copyright (c) 2001 - 2007 TOPP - http://topp.openplans.org.
 * All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible in the
 * license.txt file of the documents directory off the root directory.
 */
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.vfny.geoserver.control.IPreferenceStore;


public class Hit {
    /**
     * 
  	 * NOTE: This only works against the SENS modules! Not vanilla geoserver!
     *
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(
                "src/test/java/spring.xml");
        IPreferenceStore controller = (IPreferenceStore) context.getBean(
                "preferenceService");
        System.out.println("Before setDefault: " + controller.getString("test"));
        controller.setDefault("test", "defaultValue");
        System.out.println("After setDefault: " + controller.getString("test"));
        controller.set("test", "setValue");
        System.out.println("After set: " + controller.getString("test"));
    }
}
