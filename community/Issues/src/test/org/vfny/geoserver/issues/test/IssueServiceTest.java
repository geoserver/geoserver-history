package org.vfny.geoserver.issues.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.vfny.geoserver.issues.IIssue;
import org.vfny.geoserver.issues.IIssueService;
import org.vfny.geoserver.issues.ITarget;
import org.vfny.geoserver.issues.Issue;
import org.vfny.geoserver.issues.Target;
import org.vfny.geoserver.issues.enums.Priority;
import org.vfny.geoserver.issues.enums.Resolution;

public class IssueServiceTest extends TestCase {
   
    private IIssueService issueService;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] {
                "spring-client-ctx.xml"
            });
        issueService = (IIssueService)appContext.getBean("IssueService");
    }

    /*public void testAdding(){
        //add an issue
        IIssue newIssue = new Issue();
        newIssue.setDescription("test");
        newIssue.setPriority(Priority.HIGH);
        newIssue.setResolution(Resolution.IN_PROGRESS);
        newIssue.setTarget(new Target("Diagram","1"));
        List<IIssue> list = new LinkedList<IIssue>();
        list.add(newIssue);
        try {
            issueService.addIssues(list);
        } catch (IOException e) {
            fail("Error thrown trying to add issues");
        }
        
        //now try get is back
        try {
            List<IIssue> newList = (List<IIssue>)issueService.getIssues();
            assertNotNull(newList);
            assertEquals(newList.get(0).getPriority(),Priority.HIGH);
            assertEquals(newList.get(0).getResolution(),Resolution.IN_PROGRESS);
            assertEquals(newList.get(0).getTarget().getType(),"Diagram");
            assertEquals(newList.get(0).getTarget().getId(),"1");
            assertEquals(newList.get(0).getDescription(),"test");
        } catch (IOException e) {
            fail("Error thrown trying to get the issues back");
        }
        
    }*/
    
    public void testModify(){
        //add one issue
        ITarget myTarget = new Target("Diagram","2");
        IIssue newIssue = new Issue();
        newIssue.setDescription("test");
        newIssue.setPriority(Priority.HIGH);
        newIssue.setResolution(Resolution.IN_PROGRESS);
        newIssue.setTarget(myTarget);
        List<IIssue> list = new LinkedList<IIssue>();
        list.add(newIssue);
        try {
            issueService.addIssues(list);
        } catch (IOException e) {
            fail("Error thrown trying to add issues");
        }
        int id = 0;//temp to store the id of the issue
        //get the issue back and modify it
        try {
            List<IIssue> newList = (List<IIssue>)issueService.getIssues(myTarget);
            assertNotNull(newList);
            IIssue savedIssue = newList.get(0);
            assertNotNull(savedIssue);
            id = savedIssue.getId();
            savedIssue.setDescription("newDescription");
            savedIssue.setResolution(Resolution.RESOLVED);
            
            issueService.modifyIssue(savedIssue);
        } catch (IOException e) {
            fail("Error thrown trying to modify the issue: " + e.getMessage());
        }
        
        //get it back and check it
        try {
            List<IIssue> newList = (List<IIssue>)issueService.getIssues(myTarget);
            assertNotNull(newList);
            Iterator<IIssue> it = newList.iterator();
            IIssue savedIssue = null;
            while(it.hasNext()){
                IIssue temp = it.next();
                if(temp.getId() == id){
                    savedIssue = temp;
                }
            }
            assertNotNull(savedIssue);
            assertEquals(savedIssue.getDescription(),"newDescription");
            assertEquals(savedIssue.getResolution(),Resolution.RESOLVED);
        } catch (IOException e) {
            fail("Error thrown trying to modify the issue: " + e.getMessage());
        }
    }
    
    public void testRemove(){
        //add an issue
        ITarget myTarget = new Target("Diagram","3");
        IIssue newIssue = new Issue();
        newIssue.setDescription("test");
        newIssue.setPriority(Priority.HIGH);
        newIssue.setResolution(Resolution.IN_PROGRESS);
        newIssue.setTarget(myTarget);
        List<IIssue> list = new LinkedList<IIssue>();
        list.add(newIssue);
        try {
            issueService.addIssues(list);
        } catch (IOException e) {
            fail("Error thrown trying to add issues");
        }
        
        //remove it
        try {
            issueService.removeIssues(list);
        } catch (IOException e) {
            fail("Error thrown trying to remove issues: "+e.getMessage());
        }
        
        //check that it is not there
        try {
            List<IIssue> newList = (List<IIssue>)issueService.getIssues(myTarget);
            assertNotNull(newList);
            assertEquals(newList.size(),0);
        } catch (IOException e) {
            fail("Error thrown trying to get issues: "+e.getMessage());
        }
    }
    
    public static void main( String[] args ) {
        junit.textui.TestRunner.run(IssueServiceTest.class);
    }

}
