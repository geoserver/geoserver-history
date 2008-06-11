package org.geoserver.wfs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.geoserver.config.ServiceInfo;

public interface WFSInfo extends ServiceInfo {

    static enum Version {
        V_10,
        V_11;
    };
    
    static enum Operation {
        GETCAPABILITIES {
            public int getCode() {
                return 0;
            }
        },
        DESCRIBEFEATURETYPE {
            public int getCode() {
                return 0;
            }
        },
        GETFEATURE{
            public int getCode() {
                return 1;
            }
        } ,
        LOCKFEATURE{
            public int getCode() {
                return 2;
            }
        } ,
        TRANSACTION_INSERT {
            public int getCode() {
                return 4;
            }
        },
        TRANSACTION_UPDATE {
            public int getCode() {
                return 8;
            }
        },
        TRANSACTION_DELETE {
            public int getCode() {
                return 16;
            }
        };
        
        abstract public int getCode();
    }
    
    static enum ServiceLevel {
        BASIC {
            public int getCode() {
                return 1;
            } 
            public List<Operation> getOps() {
                return Arrays.asList(
                    Operation.GETCAPABILITIES,Operation.DESCRIBEFEATURETYPE,
                    Operation.GETFEATURE
                );
            }
        }, 
        TRANSACTIONAL {
            public int getCode() {
                return 15;
            }
            public List<Operation> getOps() {
                return Arrays.asList(
                    Operation.GETCAPABILITIES,Operation.DESCRIBEFEATURETYPE,
                    Operation.GETFEATURE, Operation.TRANSACTION_INSERT, 
                    Operation.TRANSACTION_UPDATE, Operation.TRANSACTION_DELETE
                );
            }
        }, 
        COMPLETE {
            public int getCode() {
                return 31;
            }
            public List<Operation> getOps() {
                return Arrays.asList(
                    Operation.GETCAPABILITIES,Operation.DESCRIBEFEATURETYPE,
                    Operation.GETFEATURE, Operation.TRANSACTION_INSERT, 
                    Operation.TRANSACTION_UPDATE, Operation.TRANSACTION_DELETE,
                    Operation.LOCKFEATURE
                );
            }
        };
        
        abstract public int getCode();
        abstract public List<Operation> getOps();
        
        static public ServiceLevel get( int code ) {
            for ( ServiceLevel s : values() ) {
                if ( s.getCode() == code ) {
                    return s;
                }
            }
            
            return null;
        }
    };
    
    
    
    /**
     * A map of wfs version to gml encoding configuration.
     */
    Map<Version,GMLInfo> getGML();
    
    ServiceLevel getServiceLevel();
    
    void setServiceLevel( ServiceLevel serviceLevel );
}
