package org.geoserver.catalog.util;

import java.io.Serializable;
import java.util.Map;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.Wrapper;

public class WrapperUtilsTest extends TestCase {

    private WorkspaceInfo original;

    @Override
    protected void setUp() throws Exception {
        original = EasyMock.createNiceMock(WorkspaceInfo.class);
        EasyMock.replay(original);
    }

    public void testSingleWrap() {
        WorkspaceWrapper wrapper = new WorkspaceWrapper(original);
        assertSame(original, WrapperUtils.deepUnwrap(wrapper));
    }

    public void testDeepWrap() {
        WorkspaceWrapper wrapper = new WorkspaceWrapper(original);
        for (int i = 0; i < 10; i++) {
            wrapper = new WorkspaceWrapper(wrapper);
        }
        assertSame(original, WrapperUtils.deepUnwrap(wrapper));
    }

    public void testSelfReferencing() {
        WorkspaceWrapper wrapper = new WorkspaceWrapper();
        wrapper.setWrapped(wrapper);
        try {
            WrapperUtils.deepUnwrap(wrapper);
            fail("This test should have failed with a RuntimeException, "
                    + "the wrapper is self referencing");
        } catch (RuntimeException e) {
            // fine
        }
    }
    
    public void testLooping() {
        WorkspaceWrapper wrapperOne = new WorkspaceWrapper();
        WorkspaceWrapper wrapperTwo = new WorkspaceWrapper();
        wrapperOne.setWrapped(wrapperTwo);
        wrapperTwo.setWrapped(wrapperOne);
        try {
            WrapperUtils.deepUnwrap(wrapperOne);
            fail("This test should have failed with a RuntimeException, "
                    + "the wrapper is self referencing");
        } catch (RuntimeException e) {
            // fine
        }
    }

    public void testNull() {
        assertNull(WrapperUtils.deepUnwrap(null));
    }

    /**
     * Simple catalog object wrapper used to perform tests
     * 
     * @author Andrea Aime - TOPP
     * 
     */
    private static class WorkspaceWrapper implements Wrapper<WorkspaceInfo>, WorkspaceInfo {

        WorkspaceInfo wrapped;

        public WorkspaceWrapper() {
            //
        }

        public WorkspaceWrapper(WorkspaceInfo info) {
            this.wrapped = info;
        }

        public WorkspaceInfo getWrapped() {
            return wrapped;
        }

        public void setWrapped(WorkspaceInfo wrapped) {
            this.wrapped = wrapped;
        }

        public WorkspaceInfo unwrap() {
            return wrapped;
        }

        public String getId() {
            return wrapped.getId();
        }

        public Map<String, Serializable> getMetadata() {
            return wrapped.getMetadata();
        }

        public String getName() {
            return wrapped.getName();
        }

        public void setName(String name) {
            wrapped.setName(name);
        }

        public boolean isWrapperFor(Class iface) {
            return true;
        }
    }
}
