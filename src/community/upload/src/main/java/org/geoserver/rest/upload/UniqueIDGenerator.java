package org.geoserver.rest.upload;

import java.io.File;

public class UniqueIDGenerator {
    private File myWatchedDirectory;

    public UniqueIDGenerator(){
    }

    public synchronized void setWatchedFolder(File dir){
        myWatchedDirectory = dir;
    }

    /**
     * Generate a unique identifier, allowing the caller to specify a desired
     * name.  The desired name will be returned if available, otherwise a
     * variant upon it will be generated.
     * @param desired the desired name
     * @return a unique name that is as close to the desired one as possible
     */
    public synchronized String generate(String desired){
        File f = new File(myWatchedDirectory, desired);

        while (f.exists()){
            desired = increment(desired);
            f = new File(myWatchedDirectory, desired);
        }

        return desired;
    }

    public String generate(){
        return generate("");
    }

    private String increment(String original){
        return Long.toString(System.currentTimeMillis(),36) + original;
    }
}
