package org.vfny.geoserver.zserver;

public class GeoProfile {

    //maybe make non-static, initialize with props files?
    //then this will hold all the property maps, like for the 
    //conversions from use attribute numbers to xml fields.

    public static final int LESS_THAN = 1;
    public static final int LESS_THAN_EQUAL = 2;
    public static final int EQUALS = 3;
    public static final int GREATER_THAN_EQUAL = 4;
    public static final int GREATER_THAN = 5;
    public static final int NOT_EQUAL = 6;
    public static final int OVERLAPS = 7;
    public static final int FULLY_ENCLOSED = 8;
    public static final int ENCLOSES = 9;

    public static final int BEFORE = 14;
    public static final int BEFORE_OR_DURING = 15;
    public static final int DURING = 16;
    public static final int DURING_OR_AFTER = 17;
    public static final int AFTER = 18;

    public static final int USE = 1;
    public static final int RELATION = 2;
    public static final int STRUCTURE = 4;
    public static final int TRUNCATION = 5;

    public static final String TRUNCATE = "1";


    /**
     * Returns true if the relation is one of BEFORE,
     * BEFORE_OR_DURING, DURING, DURING_OR_AFTER, or AFTER.
     *
     * @param relation the int relation number.
     * @return true if a date range relation, false otherwise.
     */
     public static boolean isDateRange(int relation) {
	return (relation >= 14 && relation <= 18);
    }  

    //TODO: make elegant...or get from props file.
    public static boolean isFGDCnum(String name) {
	if(name.equals("//metadata/idinfo/spdom/bounding/eastbc") 
	   || name.equals("//metadata/idinfo/spdom/bounding/westbc") 
	   || name.equals("//metadata/idinfo/spdom/bounding/northbc") 
	   || name.equals("//metadata/idinfo/spdom/bounding/southbc")
	   || name.equals("extent")) {
	    return true;
	} else {
	    return false;
	}

    }
    
    //this just goes off of XML name ending in date.
    //REVISIT: props file?
    public static boolean isFGDCdate(String name) {
	int strlen = name.length();
	if (strlen > 3) {
	    String tail = name.substring(strlen - 4, strlen);
	    return tail.equals("date");
	}
	return false;
    }

    public class Diag {
	    public static final String UNSUPPORTED_ATTR = "114";
    //this should be in a diagnostic class, but jzkit, which
    //wrote most all of the diagnostics, doesn't.
    //TODO: put all diagnostic numbers in a common class to 
    //access instead of the hardcodes that currently populate
    //ZServerAssociation.
    }

    public class Attribute {
	public static final String TITLE = "4";
	public static final String PUBDATE = "31";
	public static final String BEGDATE = "2072";
	public static final String ENDDATE = "2073";
	public static final String ABSTRACT = "62";
	public static final String ORIGIN = "1005";
	public static final String ANY = "1016";
	public static final String PUBLISH = "1018";
	public static final String WESTBC = "2038";
	public static final String EASTBC = "2039";
	public static final String NORTHBC = "2040";
	public static final String SOUTHBC = "2041";
	public static final String EDITION = "3807";
	public static final String UPDATE = "3109";
	public static final String ONLINK = "2021";
	public static final String GEOFORM = "3805";
	public static final String BROWSEN = "3138";
	public static final String INDSPREF = "3301";

    }

}
