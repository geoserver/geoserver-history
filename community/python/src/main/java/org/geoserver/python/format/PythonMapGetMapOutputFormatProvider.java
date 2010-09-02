package org.geoserver.python.format;

import org.geoserver.python.Python;
import org.vfny.geoserver.wms.GetMapProducer;

public class PythonMapGetMapOutputFormatProvider extends PythonOutputFormatProvider<GetMapProducer> {

    public PythonMapGetMapOutputFormatProvider(Python py) {
        super(py);
    }

    public Class<GetMapProducer> getExtensionPoint() {
        return GetMapProducer.class;
    }
    
    @Override
    protected GetMapProducer createOutputFormat(PythonFormatAdapter adapter) {
        return new PythonMapGetMapOutputFormat((PythonMapFormatAdapter) adapter);
    }

}
