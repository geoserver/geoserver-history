package org.geoserver.monitor;

import org.apache.commons.dbcp.BasicDataSource;
import org.h2.tools.DeleteDbFiles;
import org.junit.AfterClass;
import org.junit.BeforeClass;


public class JDBCMonitorDAOTest extends MonitorDAOTestSupport {

    @BeforeClass
    public static void createDAO() throws Exception {
        BasicDataSource data = new BasicDataSource();
        data.setDriverClassName("org.h2.Driver");
        data.setUrl("jdbc:h2:file:target/monitoring");
        dao = new JDBCMonitorDAO(data);
        setUpData();
    }
    
    @AfterClass
    public static void destroy() throws Exception {
        DeleteDbFiles.execute("target", "monitoring", true);
    }
    
}
