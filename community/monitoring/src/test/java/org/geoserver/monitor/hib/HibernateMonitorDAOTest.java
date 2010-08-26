package org.geoserver.monitor.hib;

import org.geoserver.monitor.MonitorDAO;
import org.geoserver.monitor.MonitorDAOTestSupport;
import org.h2.tools.DeleteDbFiles;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class HibernateMonitorDAOTest extends MonitorDAOTestSupport {

    @BeforeClass
    public static void initHibernate() throws Exception {
        XmlWebApplicationContext ctx = new XmlWebApplicationContext() {
            public String[] getConfigLocations() {
                return new String[]{
                    "classpath*:applicationContext-hibtest.xml",
                    "classpath*:applicationContext-hib.xml"};
            }
        };
        ctx.refresh();
        dao = (MonitorDAO) ctx.getBean("hibMonitorDAO");
        setUpData();
    }
    
    @AfterClass
    public static void destroy() throws Exception {
        DeleteDbFiles.execute("target/monitoring", "monitoring", false);
    }
    
    @Override
    public void testUpdate() throws Exception {
        //super.testUpdate();
    }
}
