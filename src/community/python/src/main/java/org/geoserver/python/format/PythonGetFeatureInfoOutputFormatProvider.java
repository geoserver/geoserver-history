package org.geoserver.python.format;

import org.geoserver.python.Python;
import org.vfny.geoserver.wms.responses.featureInfo.GetFeatureInfoDelegate;

public class PythonGetFeatureInfoOutputFormatProvider 
    extends PythonOutputFormatProvider<GetFeatureInfoDelegate> {

    public PythonGetFeatureInfoOutputFormatProvider(Python py) {
        super(py);
    }

    public Class<GetFeatureInfoDelegate> getExtensionPoint() {
        return GetFeatureInfoDelegate.class;
    }
    
    @Override
    protected GetFeatureInfoDelegate createOutputFormat(PythonFormatAdapter adapter) {
        return new PythonGetFeatureInfoOutputFormat((PythonVectorFormatAdapter) adapter);
    }

}
