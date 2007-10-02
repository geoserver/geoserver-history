package org.geoserver.usermanagement;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.geoserver.security.EditableUserDAO;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.StringRepresentation;
import org.springframework.dao.DataAccessException;

/**
  * The UserRestlet provides the basic user query and editing features of the 
  * user management API.  Basically, it handles the requests that actually deal
  * with users.
  * @author David Winslow <dwinslow@openplans.org>
  */
public class UserRestlet extends Restlet {
  private EditableUserDAO myUserService;

  /**
    * Currently, the UserRestlet constructor requires an EditableUserDAO rather
    * than any UserDetailsService.  Maybe it would make sense to have it hide the 
    * user modification features when using other UserDetailsServices instead?
    */
  public UserRestlet(EditableUserDAO eud) {
	  myUserService = eud;
  }

  public void handle(Request request, Response response){
    // what to do?
    String username = request.getAttributes().get("name").toString();
    
    
    if (request.getMethod().equals(Method.POST)){
      String roles = request.getEntityAsForm().getFirst("roles").getValue();
      response.setEntity(new StringRepresentation(
                         roles,
                         MediaType.TEXT_PLAIN));      
    } else if (request.getMethod().equals(Method.GET)){
        response.setEntity(new StringRepresentation(
                           fetchDetailsByUserName(username),
            MediaType.TEXT_PLAIN) );
    }
 

  }


//   public void userExists(HttpServletRequest request, HttpServletResponse response)
//       throws ServletException, IOException {
// 
//     Map kvPairs = KvpRequestReader.parseKvpSet(request.getQueryString());
// 
//     String message = "If you see this someone screwed up";
//     String username = kvPairs.get("USERNAME").toString();
//     try{
//       UserDetails user = myUserService.loadUserByUsername(username);
//       GrantedAuthority[] auths = user.getAuthorities();
//       message = user.getUsername() + ": " ;	
//       for (int i = 0; i < auths.length; i++){
//         message += auths[i].toString() + "; ";
//       }
//     } catch (UsernameNotFoundException unfe){
//       message = "User " + username + " does not exist.";
//     } catch (DataAccessException dae){
//       message = "Could not access database, please try again later.";
//     } 
// 		
//     response.getOutputStream().write( message.getBytes() ); 
//   }

  /**
    * Get user information from the UserDetailsService and return it as a String
    * containing the granted authorities for the user.
    */
  private String fetchDetailsByUserName(String username){
    String message = "If you see this someone screwed up";
    
    try{
      UserDetails user = myUserService.loadUserByUsername(username);
      GrantedAuthority[] auths = user.getAuthorities();
      message = user.getUsername() + ": " ; 
      for (int i = 0; i < auths.length; i++){
        message += auths[i].toString() + "; ";
      }
    } catch (UsernameNotFoundException unfe){
      message = "User " + username + " does not exist.";
    } catch (DataAccessException dae){
      message = "Could not access database, please try again later.";
    } 
        
    return message;
    }
}
