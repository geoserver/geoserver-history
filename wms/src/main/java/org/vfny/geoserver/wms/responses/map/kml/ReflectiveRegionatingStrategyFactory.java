package org.vfny.geoserver.wms.responses.map.kml;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ReflectiveRegionatingStrategyFactory implements RegionatingStrategyFactory {
    private static final Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");


    String myName;
    String myClassName;
    Class myStrategyClass;

    public ReflectiveRegionatingStrategyFactory(String name, String className){
        myName = name;
        myClassName = className;
    }

    public ReflectiveRegionatingStrategyFactory(String name, Class strategy){
        myName = name;
        myStrategyClass = strategy;
    }

    public boolean canHandle(String strategyName){
        return (myName != null) && myName.equalsIgnoreCase(strategyName);
    }

    public String getName(){
        return myName;
    }

    public RegionatingStrategy createStrategy(){
        try{
            return (RegionatingStrategy)getStrategyClass().newInstance();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, 
                    "Couldn't create " + myStrategyClass + " in ReflectiveRegionatingStrategyFactory", 
                    e
                    );
        }
        return new SLDRegionatingStrategy();
    }

    protected Class getStrategyClass() {
        if (myStrategyClass != null)
            return myStrategyClass;

        try{
            myStrategyClass = Class.forName(myClassName);
        } catch (Exception e){
            LOGGER.log(Level.SEVERE,
                "Failed to find class " + myClassName + " for ReflectiveRegionatingStrategy.",
                e
                );
        }

        return myStrategyClass;
    }
}
