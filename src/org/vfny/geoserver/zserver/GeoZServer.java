// Title:       GeoProfile extension of Base Z3950 Server from k-int.com
// @author:     Ian Ibbotson (ibbo@k-int.com)
// Company:     KI
// @author:     Chris Holmes
//
 
 
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation; either version 2.1 of
// the license, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite
// 330, Boston, MA  02111-1307, USA.
//
     

/**
 * GeoZServer : Controller class for a Z39.50 Server
 *
 * @author Ian Ibbotson
 * 
 * This class listens on an identified socket. On new connections, a new instance
 * of ZServerAssociation is created with a parameter that identifies a concrete 
 * realisation of the IR Searchable class. The ZServerAssociation talks with the
 * abstract Searchable interface and uses that service to resolve Z39.50 services.
 */


//package com.k_int.z3950.server;
package org.vfny.geoserver.zserver;


import java.net.*;
import java.io.*;
import java.util.Properties;
import com.k_int.codec.util.*; 
import com.k_int.util.LoggingFacade.*;


public class GeoZServer extends Thread
{
  private static LoggingContext cat = LogContextFactory.getContext("ZServer");

  private int socket_timeout = 300000;  // 300 second default timeout
  private boolean running = true;
  private ServerSocket server_socket = null;
  private Class search_service = null;
  private Properties server_properties = null;

  public static void main (String args[]) throws IOException 
  {
    Properties defaults = new Properties();
    Properties props = new Properties(defaults);
    OIDRegister reg = OIDRegister.getRegister();
    Class thisClass = reg.getClass();
    

    // Load default properties
    try
    {
	System.out.println("Class is " + thisClass);
      // read from top of any classpath entry
      InputStream is = reg.getClass().getResourceAsStream("/com/k_int/z3950/server/default.props");
      defaults.load(is);
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }

    if (args.length >= 1) 
    {
        // Parameter should be name of a properties file
        try
        {
          String props_file_name = args[0];
          cat.debug("Attempting to load local properties file "+props_file_name);

          InputStream is = new FileInputStream(props_file_name);
          props = new Properties(defaults);
          props.load(is);

          cat.debug("Done loading local props");
        }
        catch ( Exception e )
        {
          e.printStackTrace();
	  System.err.println("No system properties parameter given");
	  System.err.println("Usage: java com.k_int.z3950.server.ZServer <<propsfile>>");
	  System.exit(0);
        }
    }

    GeoZServer server = new GeoZServer(props);
    server.start();
  }

  public GeoZServer(Properties props) throws java.io.IOException
  {
    server_properties = props;

    String port_str = server_properties.getProperty("port");
    String timeout_str = server_properties.getProperty("timeout");

    if ( timeout_str != null )
      socket_timeout = Integer.parseInt(timeout_str);

    String searchable_object_class = "GeoSearchable";
	//server_properties.getProperty("evaluator");

    try
    {
      search_service = Class.forName("org.vfny.geoserver.zserver.GeoSearchable");
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }

    cat.debug("Creating ZServer on port: "+port_str+
             " (timeout="+socket_timeout+") evaluator is "+searchable_object_class);

    server_socket = new ServerSocket(Integer.parseInt(port_str));
    GeoIndexer indexer = new GeoIndexer(server_properties);
    indexer.update();

  }       

  public void run()
  {
    try 
    {
      while ( running )
      {
        cat.debug("Waiting for connection...");
        Socket socket = (Socket)server_socket.accept();
        socket.setSoTimeout(socket_timeout);
	
        GeoZServerAssociation za = new GeoZServerAssociation(socket, search_service, server_properties);
      } 

      server_socket.close();
    }
    catch (java.io.IOException e)
    {
      e.printStackTrace();
    }
  
  }

  public void shutdown(int shutdown_type)
  {
    this.running = false;

    switch ( shutdown_type )
    {
      default:
        // Currently no special processing to join with or shutdown active associations.
	try
	{
	  server_socket.close();
	}
	catch ( java.io.IOException ioe )
	{
          // No special action
	}
	break;
    }

    try
    {
      this.join();
    }
    catch ( java.lang.InterruptedException ie )
    {
      // No action
    }
  }
}      

