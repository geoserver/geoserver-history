// Title:       GeoSearchable
// Copyright:   Copyright (C) 1999-2001 Knowledge Integration Ltd.
// @author:     Ian Ibbotson (ian.ibbotson@k-int.com)
// Company:     KI
// @author:     Chris Holmes, TOPP


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

package org.vfny.geoserver.zserver;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Observer;

import java.util.Collection;
import java.util.Iterator;  
import javax.naming.*;  

// Information Retrieval Interfaces
import com.k_int.IR.*;

// For logging
import com.k_int.util.LoggingFacade.*;

// For RPN Query representation
import com.k_int.util.RPNQueryRep.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * An implementation of searchable derived from DemoSearchable.
 * A sample implementation of searchable that returns random numbers of hits
 * and random result records
 */
public class GeoSearchable implements com.k_int.IR.Searchable, com.k_int.IR.Scanable
{
  private Properties properties = null;
  private static LoggingContext cat = LogContextFactory.getContext("com.k-int.z3950.server.demo");

  private Context naming_context = null;

  public GeoSearchable()
  {
    cat.debug("New GeoSearchable");
  }

  public void init(Properties p)
  {
    this.properties = p;
  }

  public void init(Properties p, Context naming_context)
  {
    init(p);
    this.naming_context = naming_context;
  }
  
  public void destroy()
  {
  }

  public int getManagerType()
  {
    return com.k_int.IR.Searchable.SPECIFIC_SOURCE;
  }

  // Evaluate the enquiry, waiting at most will_wait_for seconds for a response
  public SearchTask createTask(IRQuery q, 
                               Object user_data)
  {
    return this.createTask(q,user_data,null);
  }

  public SearchTask createTask(IRQuery q, 
                               Object user_data,
                               Observer[] observers)
  {
      System.out.println("requesting creation the task");
      
      //String attrMapFile = properties.getProperty("fieldmap");
      //Properties attrMap =  getAttrMap(attrMapFile);
      GeoSearchTask retval = new GeoSearchTask(this,null,q, properties);
    return retval;
  }
    //TODO: phase the scan stuff out...
  public boolean isScanSupported()
  {
    return true;
  }

    /* COMPLETELY UNTESTED.  GeoProfile does not require scan for conformance.  Should
       remove this code... */
  public ScanInformation doScan(ScanRequestInfo req)
  {

    try
    {
      java.io.StringWriter sw = new java.io.StringWriter();
      com.k_int.util.RPNQueryRep.PrefixQueryVisitor.visit(req.term_list_and_start_point,sw);
      cat.debug("Incoming scan request: "+sw.toString()+" req: "+req.number_of_terms_requested);
    }
    catch(java.io.IOException ioe)
    {
      cat.warn("Cannot parse scan start position",ioe);
    }

    Vector scan_entries = new Vector();
    scan_entries.add(new TermInformation("one",1));
    scan_entries.add(new TermInformation("Two",2));
    scan_entries.add(new TermInformation("Three",3));
    scan_entries.add(new TermInformation("Four",4));
    scan_entries.add(new TermInformation("Five",5));
    scan_entries.add(new TermInformation("Six",6));

    ScanInformation result = new ScanInformation(scan_entries,1);

    return result;
  }


}
