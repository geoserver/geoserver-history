/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.xml;

import junit.framework.TestCase;
import java.io.*;
import java.net.*;
import org.geotools.validation.dto.*;
/**
 * XMLReaderTest purpose.
 * <p>
 * Description of XMLReaderTest ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * XMLReaderTest x = new XMLReaderTest(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: XMLReaderTest.java,v 1.1 2004/01/20 00:52:30 dmzwiers Exp $
 */
public class XMLReaderTest extends TestCase {
	public XMLReaderTest(){
		super("XMLReaderTest");
	}
	public XMLReaderTest(String s){
		super(s);
	}
	
	public void testReadPlugIn(){
		try{
			FileReader fr = new FileReader("C:/Java/workspace/geoserver/conf/plugins/Constraint.xml");;
			PlugInDTO dto = XMLReader.readPlugIn(fr);
			assertNotNull("Error if null",dto);
			assertTrue("Name read","Constraint".equals(dto.getName()));
			assertTrue("Description read","All features must pass the provided filter".equals(dto.getDescription()));
			assertTrue("ClassName read","org.geoserver.validation.plugins.filter.OGCFilter".equals(dto.getClassName()));
			assertNotNull("Should be one arg.",dto.getArgs());
			assertTrue("Should be one arg.",dto.getArgs().size()==1);
			assertTrue("Arg. name",dto.getArgs().containsKey("tempDirectory"));
			assertTrue("Arg. value : "+dto.getArgs().get("tempDirectory"),dto.getArgs().containsValue(new URI("file:///c:/temp")));
		}catch(Exception e){
			fail(e.toString());
		}
	}
}
