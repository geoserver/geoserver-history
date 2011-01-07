/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;


/** 
 * The combination of access level granted and response policy (lists only possible cases)
 */
public class WrapperPolicy {
//    HIDE(AccessLevel.HIDDEN, Response.HIDE),
//    METADATA(AccessLevel.METADATA, Response.CHALLENGE),
//    RO_CHALLENGE(AccessLevel.READ_ONLY, Response.CHALLENGE),
//    RO_HIDE(AccessLevel.READ_ONLY, Response.HIDE),
//    RW(AccessLevel.READ_WRITE, Response.HIDE);
    
    
    // TODO: turn these into private fields
    public final AccessLevel level; // needed, depends on catalog mode and request type
    public final Response response; // needed, by catalog mode
    public final AccessLimits limits;
    
    public static final WrapperPolicy hide(AccessLimits limits) {
        return new WrapperPolicy(AccessLevel.HIDDEN, Response.HIDE, limits);
    }
    
    public static final WrapperPolicy metadata(AccessLimits limits) {
        return new WrapperPolicy(AccessLevel.METADATA, Response.CHALLENGE, limits);
    }
    
    public static final WrapperPolicy readOnlyChallenge(AccessLimits limits) {
        return new WrapperPolicy(AccessLevel.READ_ONLY, Response.CHALLENGE, limits);
    }
    
    public static final WrapperPolicy readOnlyHide(AccessLimits limits) {
        return new WrapperPolicy(AccessLevel.READ_ONLY, Response.HIDE, limits);
    }
    
    public static final WrapperPolicy readWrite(AccessLimits limits) {
        return new WrapperPolicy(AccessLevel.READ_WRITE, Response.HIDE, limits);
    }
    
    
    WrapperPolicy(AccessLevel level, Response response, AccessLimits limits) {
        this.level = level;
        this.response = response;
        this.limits = limits;
    }
    
    public Response getResponse() {
        return response;
    }
    
    public AccessLimits getLimits() {
        return limits;
    }
    
    public AccessLevel getAccessLevel() {
        return level;
    }
    
    public boolean isHide() {
        return level == AccessLevel.HIDDEN && response == Response.HIDE;
    }
    
    public boolean isMetadata() {
        return level == AccessLevel.METADATA && response == Response.CHALLENGE;
    }
    
    public boolean isReadOnlyChallenge() {
        return level == AccessLevel.READ_ONLY && response == Response.CHALLENGE;
    }
    
    public boolean isReadOnlyHide() {
        return level == AccessLevel.READ_ONLY && response == Response.HIDE;
    }
    
    public boolean isReadWrite() {
        return level == AccessLevel.READ_ONLY && response == Response.CHALLENGE;
    }
}