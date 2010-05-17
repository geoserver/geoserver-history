/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.hibernate;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geoserver.config.GeoServerLoader;
import org.geotools.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Base class for initializing based on GeoServer configuration.
 * <p>
 * This class should be subclassed in cases where some sort of system initialization must be carried
 * out, and aspects of the initialization depend on GeoServer configuration.
 * </p>
 * <p>
 * Instances of this class should be registered in a spring context. Example:
 * 
 * <pre>
 *  &lt;bean id=&quot;geoServerLoader&quot; class=&quot;GeoServerLoader&quot;/&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class HibBeanPostProcessor implements BeanPostProcessor, DisposableBean,
        ApplicationContextAware {

    private final static Logger LOGGER = Logging.getLogger(HibBeanPostProcessor.class);

    ApplicationContext applicationContext;

    public HibBeanPostProcessor() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    public final Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        if (LOGGER.isLoggable(Level.FINER))
            LOGGER.finer("postProcessAfterInitialization " + beanName + " : "
                    + bean.getClass().getSimpleName());
        return bean;
    }

    public final Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        if (LOGGER.isLoggable(Level.FINER))
            LOGGER.finer("postProcessBeforeInitialization " + beanName + " : "
                    + bean.getClass().getSimpleName());
        if (bean instanceof GeoServerLoader) {
            if (LOGGER.isLoggable(Level.FINER))
                LOGGER.finer("postProcessBeforeInitialization FOUND !!!!!!!!! " + beanName + " : "
                        + bean.getClass().getSimpleName());
        }
        return bean;
    }

    public void destroy() throws Exception {
        if (LOGGER.isLoggable(Level.FINER))
            LOGGER.finer("destroy()");
    }
}
