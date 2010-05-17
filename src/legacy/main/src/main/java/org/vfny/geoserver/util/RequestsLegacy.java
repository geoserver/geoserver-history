package org.vfny.geoserver.util;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.geoserver.config.GeoServer;
import org.vfny.geoserver.global.UserContainer;

/**
 * Legacy class which mirrors {@link Requests}.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class RequestsLegacy {

    /**
     * Returns the full url to the tile cache used by GeoServer ( if any ).
     * <p>
     * If the tile cache set in the configuration ({@link GeoServer#getTileCache()})
     * is set to an asbsolute url, it is simply returned. Otherwise the value
     * is appended to the scheme and host of the supplied <tt>request</tt>.
     * </p>
     * @param request The request.
     * @param geoServer The geoserver configuration.
     *
     * @return The url to the tile cache, or <code>null</code> if no tile
     * cache set.
     */
    public static String getTileCacheBaseUrl(HttpServletRequest request, GeoServer geoServer) {
        //first check if tile cache set
        String tileCacheBaseUrl = (String) geoServer.getGlobal().getMetadata().get( "tileCache");

        if (tileCacheBaseUrl != null) {
            //two possibilities, local path, or full remote path
            try {
                new URL(tileCacheBaseUrl);

                //full url, return it
                return tileCacheBaseUrl;
            } catch (MalformedURLException e1) {
                //try relative to the same host as request
                try {
                    String url = Requests.appendContextPath(request.getScheme() + "://" + request.getServerName(),
                            tileCacheBaseUrl);
                    new URL(url);

                    //cool return it
                    return url;
                } catch (MalformedURLException e2) {
                    //out of guesses
                }
            }
        }

        return null;
    }
    
    /**
     * Aquire type safe session information in a UserContainer.
     *
     * @param request Http Request used to aquire session reference
     *
     * @return UserContainer containing typesafe session information.
     */
    public static UserContainer getUserContainer(HttpServletRequest request) {
        HttpSession session = request.getSession();

        synchronized (session) {
            UserContainer user = (UserContainer) session.getAttribute(UserContainer.SESSION_KEY);

            // acegi variation, login is performed by the acegi subsystem, we do get
            // the information we need from it
            if (user == null) {
                user = new UserContainer();
                
                //JD: for some reason there is sometimes a string here. doing
                // an instanceof check ... although i am not sure why this occurs.
                final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication == null) {
                    Requests.LOGGER.warning("Warning, Acegi security subsystem deactivated, no user checks will be made");
                    user.setUsername("admin");
                } else {
                    Object o = authentication.getPrincipal();
                    if ( o instanceof UserDetails ) {
                        UserDetails ud = (UserDetails) o;
                        user.setUsername(ud.getUsername());        
                    }
                    else if ( o instanceof String ) {
                        user.setUsername((String)o);
                    }
                }
                request.getSession().setAttribute(UserContainer.SESSION_KEY, user);
            }

            return user;
        }
    }

    public static boolean loggedIn(HttpServletRequest request) {
        return !getUserContainer(request).getUsername().equals("anonymous");
    }
    
    /**
     * Ensures a user is logged out.
     *
     * <p>
     * Removes the UserContainer, and thus GeoServers knowledge of the current
     * user attached to this Session.
     * </p>
     *
     * @param request HttpServletRequest providing current Session
     */
    public static void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(UserContainer.SESSION_KEY);
    }

}
