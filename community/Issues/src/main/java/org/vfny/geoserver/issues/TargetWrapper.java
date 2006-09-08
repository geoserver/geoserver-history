package org.vfny.geoserver.issues;


public class TargetWrapper {
    
    private static String TARGET_TOKEN = "__";

    public static ITarget getTargetFromString(String targetString) throws Exception{
        String[] tokens = targetString.split(TARGET_TOKEN);
        if(tokens.length != 2){
            throw new Exception("Invalid target format!");
        }
        ITarget newTarget = new Target(tokens[0],tokens[1]);
        return newTarget;
        
    }   
    
    public static String getStringFromTarget(ITarget target){
        String targetString = target.getType() + TARGET_TOKEN + target.getId();
        return targetString;
    }

}
