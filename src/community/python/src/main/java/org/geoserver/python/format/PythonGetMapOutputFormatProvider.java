package org.geoserver.python.format;

import org.geoserver.python.Python;
import org.vfny.geoserver.wms.GetMapProducer;

public class PythonGetMapOutputFormatProvider extends PythonVectorOutputFormatProvider<GetMapProducer> {

    public PythonGetMapOutputFormatProvider(Python py) {
        super(py);
    }
    
    public Class<GetMapProducer> getExtensionPoint() {
        return GetMapProducer.class;
    }

    @Override
    protected GetMapProducer createOutputFormat(PythonVectorFormatAdapter adapter) {
        return new PythonGetMapOutputFormat(adapter);
    }

}
