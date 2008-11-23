
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.vfny.geoserver.control.IStatusReport;
import org.vfny.geoserver.control.IStatusReporter;


public class Hit {
    /**
     * 
    DOCUMENT ME!
     *
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(
                "src/test/java/spring.xml");
        IStatusReporter controller = (IStatusReporter) context.getBean(
                "statusReporter");
        
        List reports = controller.getStatusReports();
        
        for (int i = 0; i < reports.size(); i++) {
        	IStatusReport report = (IStatusReport) reports.get(i);
        	System.out.println("Module Name: " + report.getModuleName());
        	System.out.println("Status: " +report.getStatus());
        	System.out.println("Message: " + report.getMessage().getLocalizedMessage());
        }
    }
}
