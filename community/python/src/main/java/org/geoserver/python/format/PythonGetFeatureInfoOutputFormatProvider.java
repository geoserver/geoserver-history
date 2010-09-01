package org.geoserver.python.format;

import org.geoserver.python.Python;
import org.vfny.geoserver.wms.responses.featureInfo.GetFeatureInfoDelegate;

public class PythonGetFeatureInfoOutputFormatProvider 
    extends PythonVectorOutputFormatProvider<GetFeatureInfoDelegate> {

    public PythonGetFeatureInfoOutputFormatProvider(Python py) {
        super(py);
    }

    public Class<GetFeatureInfoDelegate> getExtensionPoint() {
        return GetFeatureInfoDelegate.class;
    }
    
    @Override
    protected GetFeatureInfoDelegate createOutputFormat(PythonVectorFormatAdapter adapter) {
        return new PythonGetFeatureInfoOutputFormat(adapter);
    }

}
