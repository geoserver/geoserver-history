/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.wms.GetMapProducer;

/**
 * Unit test for {@link WMSExtensions}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 */
public class WMSExtensionsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFindMapProducers() {
        GetMapProducer mockProducer1 = createMock(GetMapProducer.class);
        GetMapProducer mockProducer2 = createMock(GetMapProducer.class);

        ApplicationContext mockContext = EasyMock.createMock(ApplicationContext.class);
        // I'm not so pleasant with this block of code as it implies knowing how
        // the internals of GeoServerExtensions work
        expect(mockContext.getBeanNamesForType(GetMapProducer.class)).andReturn(
                new String[] { "producer1", "producer2" });
        expect(mockContext.getBean("producer1")).andReturn(mockProducer1);
        expect(mockContext.getBean("producer2")).andReturn(mockProducer2);
        // end of unpleasant block

        replay(mockContext);

        List<GetMapProducer> mapProducers = WMSExtensions.findMapProducers(mockContext);
        assertNotNull(mapProducers);
        assertEquals(2, mapProducers.size());
        assertTrue(mapProducers.contains(mockProducer1));
        assertTrue(mapProducers.contains(mockProducer2));
    }

    public void testFindMapProducersEmpty() {
        ApplicationContext mockContext = EasyMock.createMock(ApplicationContext.class);
        // I'm not so pleasant with this block of code as it implies knowing how
        // the internals of GeoServerExtensions work
        expect(mockContext.getBeanNamesForType(GetMapProducer.class)).andReturn(new String[] {});
        // end of unpleasant block

        replay(mockContext);

        List<GetMapProducer> mapProducers = WMSExtensions.findMapProducers(mockContext);
        assertNotNull(mapProducers);
        assertEquals(0, mapProducers.size());
    }

    public void testFindMapProducer() {
        GetMapProducer mockProducer = createMock(GetMapProducer.class);

        ApplicationContext mockContext = EasyMock.createMock(ApplicationContext.class);
        // I'm not so pleasant with this block of code as it implies knowing how
        // the internals of GeoServerExtensions work
        expect(mockContext.getBeanNamesForType(GetMapProducer.class)).andReturn(
                new String[] { "producer1" }); // call#1
        expect(mockContext.getBean("producer1")).andReturn(mockProducer); // call#1

        expect(mockContext.getBeanNamesForType(GetMapProducer.class)).andReturn(
                new String[] { "producer1" }); // call#2
        expect(mockContext.getBean("producer1")).andReturn(mockProducer); // call#2

        // end of unpleasant block

        Set<String> testFormatNames = new HashSet<String>();
        testFormatNames.add("image/fakeformat");
        testFormatNames.add("image/dummy");

        expect(mockProducer.getOutputFormatNames()).andReturn(testFormatNames);// call#1
        expect(mockProducer.getOutputFormatNames()).andReturn(testFormatNames);// call#2

        replay(mockContext);
        replay(mockProducer);

        // note the lookup shall be case insensitive..
        GetMapProducer producer;
        producer = WMSExtensions.findMapProducer("ImaGe/FaKeForMat", mockContext);// call#1
        assertSame(mockProducer, producer);

        producer = WMSExtensions.findMapProducer("notARegisteredFormat", mockContext);// call#2
        assertNull(producer);

        verify(mockProducer);
        verify(mockContext);
    }
}