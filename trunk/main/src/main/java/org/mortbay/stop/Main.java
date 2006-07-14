// ========================================================================
// Copyright (c) 2002 Mort Bay Consulting (Australia) Pty. Ltd.
// $Id: Main.java,v 1.1 2004/08/23 14:00:33 cholmesny Exp $
// ========================================================================

package org.mortbay.stop;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;



/*-------------------------------------------*/
/** Main stop class.
 * This class is intended to be the main class listed in the MANIFEST.MF  of
 * the stop.jar archive. It allows an application started with the
 * command "java -jar start.jar" to be stopped.
 *
 * Programs started with start.jar may be stopped with the stop.jar, which connects
 * via a local port to stop the server. The default port can be set with the 
 * STOP.PORT system property (a port of < 0 disables the stop mechanism). If the STOP.KEY 
 * system property is set, then a random key is generated and written to stdout. This key 
 * must be passed to the stop.jar.
 *
 * @author Greg Wilkins
 * @version $Revision: 1.1 $
 */
 
public class Main
{
    private boolean _debug = System.getProperty("DEBUG",null)!=null;
    private String _config = System.getProperty("START","org/mortbay/start/start.config");
    private int _port = Integer.getInteger("STOP.PORT",8079).intValue();
    private String _key = System.getProperty("STOP.KEY","mortbay");
       
    public static void main(String[] args)
    {
        new Main().stop();
    }

    void stop()
    {
        try
        {
            if (_port<=0)
                System.err.println("START.PORT system property must be specified");
            if (_key==null)
            {
                _key="";
                System.err.println("Using empty key");
            }

            Socket s=new Socket(InetAddress.getByName("127.0.0.1"),_port);
            OutputStream out=s.getOutputStream();
            out.write((_key+"\r\nstop\r\n").getBytes());
            out.flush();
            s.shutdownOutput();
            s.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
