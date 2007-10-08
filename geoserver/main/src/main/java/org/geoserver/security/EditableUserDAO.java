package org.geoserver.security;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.memory.UserAttribute;
import org.acegisecurity.userdetails.memory.UserAttributeEditor;
import org.geoserver.security.PropertyFileWatcher;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * The EditableUserDAO class provides a UserDetailsService implementation that 
 * allows modifying user information programmatically.
 * @author David Winslow - <dwinslow@openplans.org>
 */
public class EditableUserDAO implements UserDetailsService {

  /*
  Things to test:
  properties file exists, but is empty (should result in no defined users)
  properties file exists and is valid  (should result in users as defined by file)
  properties file is changed by an external process (should result in users being dynamically loaded)
  properties file is deleted (should result in default admin user being created)
  */
	private Map myDetailStorage;
	private PropertyFileWatcher myWatcher;
	private GeoServer geoServer;
	
  /**
    * Find the file that should provide the user information.
    */
	private File getUserFile() throws ConfigurationException, IOException{
		File securityDir = GeoserverDataDirectory.findCreateConfigDir("security");
		File userFile = new File(securityDir, "users.properties");
		if (!userFile.exists()  && !userFile.createNewFile()){
				System.out.println("Couldn't create file: " + userFile.getAbsolutePath());
				throw new ConfigurationException("Couldn't create users.properties");
		} else {
			return userFile;
		}
	}
	
	public EditableUserDAO(){
		myDetailStorage = new HashMap();
		try {
		myWatcher = new PropertyFileWatcher(getUserFile());
		} catch (Exception e){
			// TODO:log error someplace
			createDefaultUser();
		}
		
		update();
		if (myDetailStorage.isEmpty()) createDefaultUser();
	}

  /**
    * Generate the default geoserver administrator user.
    */
	private void createDefaultUser() {
		String name = (geoServer == null ? "admin" : geoServer.getAdminUserName());
		String passwd = (geoServer == null ? "geoserver" : geoServer.getAdminPassword());
		
		myDetailStorage.put(name, new User(name,
				passwd,
				true,
				true,
				true,
				true,
				new GrantedAuthority[]{
					new GrantedAuthorityImpl("ROLE_ADMINISTRATOR")
				}
		));
	}
	
	public UserDetails loadUserByUsername(String username) {
		update();
		return (UserDetails)myDetailStorage.get(username);
	}

  /**
    * Create a user with the specified credentials and authority.  The user will
    * be automatically added to persistant storage.
    */
	public void setUserDetails(String username, UserAttribute details) 
	throws IOException, ConfigurationException{
		update();
		myDetailStorage.put(username, makeUser(username, details));
		syncChanges();
	}
	
	/**
	 * Remove a user specified by name.  If the username is not used by any 
	 * known user, nothing happens.
	 */
	public void deleteUser(String username) throws IOException, ConfigurationException {
		update();
		myDetailStorage.remove(username);
		syncChanges();
	}

  /**
    * Make sure the user data map matches the information in the user data file.
    * This should be called automatically, so that no code outside of this class
    * needs to access this method.
    */
	private void update() {
		try {
			if (myWatcher == null || myWatcher.isStale()) {
				Properties prop = myWatcher.getProperties();
				UserAttributeEditor uae = new UserAttributeEditor();
				myDetailStorage.clear();

				Iterator it = prop.keySet().iterator();
				while  (it.hasNext()){
					String username = (String)it.next();
					uae.setAsText(prop.getProperty(username));
					UserAttribute attrs = (UserAttribute) uae.getValue();
					if (attrs != null) {
						myDetailStorage.put(username, makeUser(username, attrs));
					}
				}
			}
		} catch (IOException ioe) {
			// TODO: handle the exception properly
			myDetailStorage.clear();
			createDefaultUser();
		}
	}

  /**
    * Convenience method for creating users from a UserAttribute and a username.
    */
	private UserDetails makeUser(String username, UserAttribute attrs) {
		return new User(
				username,
				attrs.getPassword(),
				attrs.isEnabled(),
				true, // account not expired
				true, // credentials not expired
				true, // account not locked
				attrs.getAuthorities());
	}
	
  /**
    * Write the changes to persistant storage.  This should happen 
    * automatically when changes are made, so no code outside of this class
    * should need to call this method.
    */
	private void syncChanges() throws IOException, ConfigurationException{
		Properties prop = new Properties();
		
		Iterator it = myDetailStorage.values().iterator();
		while (it.hasNext()){
			UserDetails details = (UserDetails)it.next();
			String key = details.getUsername();
			String value = details.getPassword();
			for (int i = 0; i < details.getAuthorities().length; i++){
				value+= "," + details.getAuthorities()[i].getAuthority();
			}
			if (!details.isEnabled()){
				value+=",disabled";
			}
			prop.setProperty(key, value);
		}

		OutputStream os = new BufferedOutputStream(new FileOutputStream(getUserFile()));
		
		prop.store(os, "Geoserver user data. Format is username=password,role1,role2,...[enabled|disabled]");
	}
	
  // this is required due to something in the Spring application context ... not sure exactly what
    public GeoServer getGeoServer() {
        return geoServer;
    }

    public void setGeoServer(GeoServer geoServer) {
        this.geoServer = geoServer;
    }
}
