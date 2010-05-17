package org.geoserver.hibernate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.geotools.util.logging.Logging;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class HibTestSupport extends AbstractTransactionalSpringContextTests {

    private static final Logger LOGGER = Logging.getLogger(HibTestSupport.class);

    public HibTestSupport() {
        setDefaultRollback(false);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath*:applicationContext-hibernateTest.xml",
                "classpath*:applicationContext.xml" // hope it will get first the appcontext defined
                                                    // in this very module
                , "classpath*:applicationSecurityContext.xml" };
    }

    @Override
    protected GenericApplicationContext createApplicationContext(String[] locations) {
        try {
            ServletContext ctx = createServletContext();

            GenericWebApplicationContext context = new GenericWebApplicationContext(
                    new UnoverridingBeanFactory());
            context.setServletContext(ctx);
            prepareApplicationContext(context);
            customizeBeanFactory(context.getDefaultListableBeanFactory());
            createBeanDefinitionReader(context).loadBeanDefinitions(locations);
            context.refresh();
            return context;
        } catch (Exception orig) {
            for (Throwable loop = orig; loop != null; loop = loop.getCause()) {
                if (loop instanceof SQLException) {
                    LOGGER.warning("Found a SQLException. Unrolling stacktrace.");
                    for (SQLException sqle = (SQLException) loop; sqle != null; sqle = sqle
                            .getNextException()) {
                        if (sqle instanceof PSQLException) {
                            PSQLException psqle = (PSQLException) sqle;
                            LOGGER.warning("Server msg: " + psqle.getServerErrorMessage());
                            if(psqle.getServerErrorMessage() != null) {
                                LOGGER.warning("Server iqry: " 
                                        + psqle.getServerErrorMessage().getInternalQuery());
                                LOGGER.warning("Server hint: "
                                        + psqle.getServerErrorMessage().getHint());
                            }
                        }

                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw, true);
                        sqle.printStackTrace(pw);
                        pw.close();
                        sw.flush();
                        LOGGER.warning("Stacktrace --> " + sw.getBuffer().toString());
                    }
                    break;
                }
            }
            throw new RuntimeException("Rethrowing " + orig.getMessage(), orig);
        }
    }

    static class UnoverridingBeanFactory extends DefaultListableBeanFactory {

        public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
                throws BeanDefinitionStoreException {

            if (containsBeanDefinition(beanName)) {
                String oldbd = getBeanDefinition(beanName).getBeanClassName();
                String newbd = beanDefinition.getBeanClassName();
                String note = oldbd.equals(newbd) ? " (same class " + oldbd + ")" : " from "
                        + oldbd + " into " + newbd;
                logger.info("Not overriding " + beanName + note);
            } else {
                super.registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }

    private ServletContext createServletContext() {
        MockServletContext ctx = new MockServletContext();
        File testDataDir = null;
        ;

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();

        Resource testDataResource = context.getResource("testdata");
        if (testDataResource == null)
            throw new RuntimeException("Can't find test data.");

        try {
            testDataDir = testDataResource.getFile();

            File services = new File(testDataDir, "services.xml");
            if (!services.exists())
                throw new FileNotFoundException(services.getPath());

            ctx.addInitParameter("GEOSERVER_DATA_DIR", testDataDir.getAbsolutePath());
            ctx.addInitParameter("serviceStrategy", "PARTIAL-BUFFER2");

        } catch (IOException ex) {
            LOGGER.severe("Error in test files: " + ex.getMessage());
            throw new RuntimeException("Error in test files", ex);
        }

        return ctx;
    }
}
