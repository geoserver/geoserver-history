package org.geoserver.sldservice;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

public class SldDocTest {

	@Test
	public void testLoadFile() {
		SldDoc sldDoc= new SldDoc();
		File f = new File("src/test/resources/Lakes.sld");
		try {
			StyledLayerDescriptor s= sldDoc.loadFile(f);
			assertNotNull(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
