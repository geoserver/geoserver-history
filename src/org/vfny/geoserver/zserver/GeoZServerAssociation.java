/**
 * Title:       GeoZServerAssociation backend Z3950 association
 * @author:     Ian Ibbotson (ibbo@k-int.com)
 * Company:     KI
 * @author: Chris Holmes, TOPP
 */

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
     

/*
 * $Log: GeoZServerAssociation.java,v $
 * Revision 1.1  2002/11/15 22:25:33  cholmesny
 * adding zserver source files
 *
 * Revision 1.51  2002/07/03 17:17:55  ianibbo
 * First attempt at merging ralphs assoc changes with setprefrecsyn on search task.
 *
 * Revision 1.50  2002/07/01 14:28:14  ianibbo
 * Updated a2jruntime that understands new marc variants and added appropriate
 * lines into Z client.
 *
 * Revision 1.49  2002/06/19 12:44:24  ianibbo
 * Included ralphs changes to the ZServerAssociation that provide diagnostics
 * when record conversion fails.
 *
 */

//package com.k_int.z3950.server;
package org.vfny.geoserver.zserver;


import java.net.*;
import java.io.*;
import java.util.Stack;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Properties;
import java.util.Enumeration;

import com.k_int.util.ISO2709Builder;
import com.k_int.z3950.util.*;

import com.k_int.gen.AsnUseful.*;
import com.k_int.gen.Z39_50_APDU_1995.*;
import com.k_int.gen.NegotiationRecordDefinition_charSetandLanguageNegotiation_3.*;
 
import java.math.BigInteger;
 
// Used to represent arbritray query structures
import com.k_int.util.RPNQueryRep.*;
import com.k_int.util.PrefixLang.*;

// for OID Register
import com.k_int.codec.util.*;

import com.k_int.IR.*;
import com.k_int.IR.Syntaxes.Conversion.*;

// For XML components
import  org.w3c.dom.Document;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.Serializer;
import  org.apache.xml.serialize.SerializerFactory;
import  org.apache.xml.serialize.XMLSerializer;


// For log4j logging
import com.k_int.util.LoggingFacade.*;

public class GeoZServerAssociation implements TargetAPDUListener
{
  private Searchable search_service = null;
  private ZTargetEndpoint assoc = null;   
  private Hashtable active_searches = new Hashtable();
  private OIDRegister reg = OIDRegister.getRegister();
  private static LoggingContext cat = LogContextFactory.getContext("ZServerAssociation");
  private XSLConverter record_schema_convertor;
  private int dbg_counter=0;
  private GenericEventToTargetListenerAdapter event_adapter = null;
    private Properties serverProps;

  public GeoZServerAssociation(Socket s, Class search_service_class, Properties p)
  {

      //TODO: put this in nicer place...this is just a hack to get old html
      //oid working...should do something like Isite where it translates
      //old oids into the new ones.
      reg.register_oid(new OIDRegisterEntry("html", "{1,2,840,10003,5,1000,34,1}", "record forma", null));
      //untested....just changing oid in entry form to the new html, but we
      //should be able to handle and translate the old formats.

    dbg_counter++;
    try
    {
      this.search_service = (Searchable)(search_service_class.newInstance());   
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }

    serverProps = p;
    search_service.init(p);
    record_schema_convertor=XSLConverter.getConvertor(p);

    assoc = new ZTargetEndpoint(s);
    event_adapter = new GenericEventToTargetListenerAdapter(this);
    assoc.getPDUAnnouncer().addObserver( event_adapter );
    assoc.start();
  }

  protected void finalize() throws Throwable
  {
    dbg_counter--;
    cat.debug("ZServerAssociation::finalize() - "+dbg_counter+" remaining");

    
  }

  public void incomingInitRequest(APDUEvent e)
  {
    cat.debug("Incoming initRequest....");
    //cat.debug("oid reg :" + reg.printAll());
    InitializeRequest_type init_request = (InitializeRequest_type) (e.getPDU().o); 

    for ( int i=0; i<Z3950Constants.z3950_option_names.length; i++ )
    {
      if ( init_request.options.isSet(i) )
        cat.info("Origin requested service: "+Z3950Constants.z3950_option_names[i]);
    }

    // Did the origin request scan?
    if ( init_request.options.isSet(7) )
    {
      // Does our backend support scan?
      if ( ! ( this.search_service instanceof Scanable ) )
      {
        init_request.options.clearBit(7);
        cat.info("Origin requested scan, not supported by this backend realisation.");
      }
      else
      {
        Scanable s = (Scanable)this.search_service;
        if ( ! s.isScanSupported() )
        {
          init_request.options.clearBit(7);
          cat.info("Origin requested scan, not supported by this instance of the search backend");
        }
      }
    }

    // If we are talking v2, userInformationField may contain a character Set Negotiation
    // field, if v3, otherInfo may contain a charset/language negotiation feature.
    if ( init_request.userInformationField != null )
    {
      OIDRegisterEntry ent = reg.lookupByOID(init_request.userInformationField.direct_reference);
      if ( ent != null )
        cat.debug("Init Request contains userInformationField oid="+ent.getName());
      else
        cat.debug("Unkown external in userInformationField");
        // The OID for the external should be found in userInformationField.direct_reference
    }

    if ( init_request.otherInfo != null )
    {
      cat.debug("Init Request contains otherInfo entries");
      for ( Enumeration other_info_enum = init_request.otherInfo.elements(); other_info_enum.hasMoreElements(); )
      {
        cat.debug("Processing otherInfo entry...");
        // Process the external at other_info_enum.nextElement();
        OtherInformationItem43_type oit = (OtherInformationItem43_type)(other_info_enum.nextElement());

        cat.debug("Processing OtherInformationItem43_type");

        switch ( oit.information.which )
        {
          case information_inline45_type.externallydefinedinfo_CID:
            EXTERNAL_type et = (EXTERNAL_type)(oit.information.o);
            if ( et.direct_reference != null )
            {
              OIDRegisterEntry ent = reg.lookupByOID(et.direct_reference);
              cat.debug("External with direct reference, oid="+ent.getName());

              // Are we dealing with character set negotiation.
              if ( ent.getName().equals("z_charset_neg_3") )
                handleNLSNegotiation((CharSetandLanguageNegotiation_type)(et.encoding.o));
            }
            break;
          default:
            cat.debug("Currently unhandled OtherInformationType");
            break;
        }
      }
    }

    try
    {
      assoc.sendInitResponse(init_request.referenceId,
                             init_request.protocolVersion,
                             init_request.options,
                             init_request.preferredMessageSize.longValue(),
                             init_request.exceptionalRecordSize.longValue(),
                             true,
                             "174",
                             "K-Int Generic Z Server",
                             "$Id: GeoZServerAssociation.java,v 1.1 2002/11/15 22:25:33 cholmesny Exp $",
                             null,
                             null);
    }
    catch ( java.io.IOException ioe )
    {
      ioe.printStackTrace();
    }
  }

  private void handleNLSNegotiation(CharSetandLanguageNegotiation_type neg)
  {
    cat.debug("Handle Character Set and Language Negotiation");

    if ( neg.which == CharSetandLanguageNegotiation_type.proposal_CID )
    {
      OriginProposal_type op = (OriginProposal_type)(neg.o);

      // Deal with any proposed character sets.
      if ( op.proposedCharSets != null )
      {
        for ( Enumeration prop_charsets = op.proposedCharSets.elements(); prop_charsets.hasMoreElements();)
        {
          proposedCharSets_inline0_choice1_type c = (proposedCharSets_inline0_choice1_type)(prop_charsets.nextElement());
          switch ( c.which )
          {
            case proposedCharSets_inline0_choice1_type.iso10646_CID:
              // The client proposes an iso 10646 id for a character set
              Iso10646_type iso_type = (Iso10646_type)(c.o);
              OIDRegisterEntry ent = reg.lookupByOID(iso_type.encodingLevel);
              cat.debug("Client proposes iso10646 charset: "+ent.getName());
              break;
            default:
              cat.warn("Unhandled character set encoding");
              break;
          }
        }
      }
    }
  }

  public void incomingSearchRequest(APDUEvent e)
  {
    cat.debug("Processing incomingSearch_Request");



    SearchRequest_type search_request = (SearchRequest_type) (e.getPDU().o);


    //    if ( search_request.mediumSetElementSetNames.which == mediumSetElementSetNames.databasespecific

    if (search_request.mediumSetElementSetNames != null) {
    cat.debug("Element set medium = " + search_request.mediumSetElementSetNames.which + ", " 
	      + search_request.mediumSetElementSetNames.o);
    } else {
	cat.debug("medium set is null");
    }
    if (search_request.smallSetElementSetNames != null) {
	cat.debug("small obj = " + 
	       search_request.smallSetElementSetNames.o + ", which =" +
	      search_request.smallSetElementSetNames.which);
    } else {
	cat.debug("small set is null");
    }

    // Create a search response
    PDU_type pdu = new PDU_type();
    pdu.which=PDU_type.searchresponse_CID;
    SearchResponse_type response = new SearchResponse_type();
    pdu.o = response;

   

    int ssub = search_request.smallSetUpperBound.intValue();
    int lslb = search_request.largeSetLowerBound.intValue();
    int mspn = search_request.mediumSetPresentNumber.intValue();
    
    cat.debug("ssub = " + ssub + " lslb = " + lslb + " mspn = " + mspn);

    response.referenceId = search_request.referenceId; 


    System.out.println("respnse refrence ID = " + response.referenceId);

    // Assume failure unless something below sets to true
    response.searchStatus = Boolean.FALSE;

    RootNode rn = null;

    try
    {
      switch(search_request.query.which)
      {
        case Query_type.type_0_CID:
          cat.debug("Processing Any Query");
          // Any
          break;
        case Query_type.type_1_CID:
        case Query_type.type_101_CID:
          // RPN query
          cat.debug("Processing RPN Query");
          rn = com.k_int.z3950.util.RPN2Internal.zRPNStructure2RootNode((RPNQuery_type)(search_request.query.o));
          break;
        case Query_type.type_2_CID:
        case Query_type.type_100_CID:
        case Query_type.type_102_CID:
          cat.debug("Processing OctetString Query");
          // Octet String
          break;
      }

      if ( rn != null )
      {
        cat.debug("Got root node");

        IRQuery q = new IRQuery();
        q.collections = search_request.databaseNames;
        q.query = new com.k_int.IR.QueryModels.RPNTree(rn);

        // Process the query
        cat.debug("Create Search Task with query:" + q);
        SearchTask st = search_service.createTask(q, search_request.referenceId);
	

        // Evaluate the query, waiting 10 seconds for the task to complete. We really want to be
        // able to pass in a predicate to wait for here, e.g. evaluate(10000, "NumHits > 100 OR Status=Complete");
        cat.debug("Evaluate Search Task");
        try
        {
          st.evaluate(10000);
        }
        catch ( TimeoutExceededException tee )
        {
          cat.info("Timeout exceeded waiting for search to complete");
        }

        if ( search_request.resultSetName != null ) {
              System.out.println("putting st with name " + search_request.resultSetName);
	    active_searches.put(search_request.resultSetName, st);
        //} else {
        }
	active_searches.put("Default", st);
	//System.out.println("putting ST with Default name " + st.getTaskIdentifier());
	//}
        // Result records processing
        int result_count = st.getTaskResultSet().getFragmentCount();
        cat.debug("result count is "+result_count);

        // Number of hits
        response.resultCount = BigInteger.valueOf(result_count);

        // Figure out Search Status
        if ( ( st.getTaskStatusCode() != SearchTask.TASK_FAILURE ) )
        {
          response.searchStatus = Boolean.TRUE;
        }
        else
        {
          // Figure out Result Set Status
          switch ( st.getTaskStatusCode() )
          {
            case SearchTask.TASK_EXECUTING_ASYNC:
            case SearchTask.TASK_EXECUTING_SYNC:
              // resultSetStatus = 2 (interim) Partial results available, not necessarily valid
              response.resultSetStatus = BigInteger.valueOf(2);
              break;
            case SearchTask.TASK_FAILURE:
              // resultSetStatus = 3 (none) No result set
              response.resultSetStatus = BigInteger.valueOf(3);
              break;
          }
        }

        cat.debug("Is "+result_count+" <= "+lslb +" or <= "+ssub);

        if ( ( result_count <= lslb ) || ( result_count <= ssub ) )
        {
          cat.debug("Yep");

          int start = 1;
          int count = ( result_count <= ssub ? result_count : mspn );

          if ( count > 0 )
          {
            cat.debug("Asking for "+count+" response records from "+start);

	    //	    RecordFormatSpecification searchSpec = new RecordFormatSpecification("xml",null,"B");
	    //possibly make this a constant?  
            System.out.println("Search request default = " + search_request.preferredRecordSyntax);
	    //TODO: instead of assumning B for breif actually figure out the
	    //requested element set.
	    response.records = createRecordsFor(st,
                search_request.preferredRecordSyntax, start, count, "B");

 
            if ( response.records.which == Records_type.responserecords_CID )
            {
              cat.debug("We have records");
  
              // Looks like we managed to present some records OK..
              response.numberOfRecordsReturned = BigInteger.valueOf(((Vector)(response.records.o)).size());
              response.nextResultSetPosition = BigInteger.valueOf(count+1);
              response.presentStatus = BigInteger.valueOf(0);
            }
            else  // Non surrogate diagnostics ( Single or Multiple )
            {
              cat.debug("Diagnostics");
  
              // PresentStatus, 0=0K, 1,2,3,4 = Partial, 5=Failure, None surrogate diag
              response.presentStatus = BigInteger.valueOf(5);
              response.numberOfRecordsReturned = BigInteger.valueOf(0);
              response.nextResultSetPosition = BigInteger.valueOf(1);
            }
          }
          else
          {
            cat.debug("No need to piggyback records....");
            response.numberOfRecordsReturned = BigInteger.valueOf(0);
            response.nextResultSetPosition = BigInteger.valueOf(1);
            response.presentStatus = BigInteger.valueOf(0);
          }
        }
        else
        {
          response.presentStatus = BigInteger.valueOf(5);
          response.numberOfRecordsReturned = BigInteger.valueOf(0);
          response.nextResultSetPosition = BigInteger.valueOf(1);                                                               
        }
      }
      else
        cat.debug("Unable to process query into root node");
    }
    catch ( com.k_int.IR.InvalidQueryException iqe )
    {
      // Need to send a diagnostic
      iqe.printStackTrace();
      response.resultCount = BigInteger.valueOf(0);
      response.presentStatus = BigInteger.valueOf(5);            // Failure
      response.numberOfRecordsReturned = BigInteger.valueOf(0);
      response.nextResultSetPosition = BigInteger.valueOf(0);
      response.resultSetStatus = BigInteger.valueOf(3);          // No result set available
      response.records = createNSD(null, iqe.toString());
    }
    catch ( SearchException se )
    {
      // We need to populate a diagnostic here
      cat.info("Search returning diagnostic. Reason:"+se.toString());
      se.printStackTrace();
      response.resultCount = BigInteger.valueOf(0);
      response.presentStatus = BigInteger.valueOf(5);            // Failure
      response.numberOfRecordsReturned = BigInteger.valueOf(0);
      response.nextResultSetPosition = BigInteger.valueOf(0);
      response.resultSetStatus = BigInteger.valueOf(3);          // No result set available
      response.records = createNSD((String)(se.additional), se.toString());
    }

    cat.debug("Send search response : ");
    try
    {
      assoc.encodeAndSend(pdu);
    }
    catch ( java.io.IOException ioe )
    {
      ioe.printStackTrace();
    }  
  }

  public void incomingPresentRequest(APDUEvent e)
  {
    cat.debug("Incoming present_Request");

    PresentRequest_type present_request = (PresentRequest_type) (e.getPDU().o);

    cat.debug("element obj  = " + ((ElementSetNames_type)present_request.recordComposition.o).o + 
	      "which = " +  present_request.recordComposition.which);
    String requested_element_set = (String)((ElementSetNames_type)present_request.recordComposition.o).o;
    if (requested_element_set == null) {
	//default to full record.
	requested_element_set = "F";
    }

    // Create a present response
    PDU_type pdu = new PDU_type();
    pdu.which=PDU_type.presentresponse_CID;
    PresentResponse_type response = new PresentResponse_type();
    pdu.o = response;
    response.referenceId = present_request.referenceId;
    response.otherInfo = null;

    System.out.println("result Set ID = " + present_request.resultSetId);
     System.out.println("refrence ID = " + present_request.referenceId);

    SearchTask st = (SearchTask)(active_searches.get(present_request.resultSetId));
    if (st == null)  st = (SearchTask)(active_searches.get("Default"));
    //the last search done is always stored in the hashtable 
    //with both the resultSetName of the searchRequest and the value "Default"
    //So if the resultSetId can't be found the last search task is used.

    int start = present_request.resultSetStartPoint.intValue();
    int count = present_request.numberOfRecordsRequested.intValue();
    
    //RecordFormatSpecification spec = new RecordFormatSpecification(
    response.records = createRecordsFor(st,
      present_request.preferredRecordSyntax, start, count, requested_element_set);

    if ( response.records.which == Records_type.responserecords_CID )
    {
      // Looks like we managed to present some records OK..
      response.numberOfRecordsReturned = BigInteger.valueOf(((Vector)(response.records.o)).size());
      if ( start+count >= st.getTaskResultSet().getFragmentCount() )
        response.nextResultSetPosition = BigInteger.valueOf(0);
      else
        response.nextResultSetPosition = BigInteger.valueOf(start+count);

      response.presentStatus = BigInteger.valueOf(0);
    }
    else  // Non surrogate diagnostics ( Single or Multiple )
    {
      response.numberOfRecordsReturned = BigInteger.valueOf(0);
      response.nextResultSetPosition = present_request.resultSetStartPoint;

      // PresentStatus, 0=0K, 1,2,3,4 = Partial, 5=Failure, None surrogate diag
      response.presentStatus = BigInteger.valueOf(5);
    }

    try
    {
      assoc.encodeAndSend(pdu);
    }
    catch ( java.io.IOException ioe )
    {
      ioe.printStackTrace();
    } 
  }

  public void incomingDeleteResultSetRequest(APDUEvent e)
  {
    cat.debug("Incoming deleteResultSetRequest");

    DeleteResultSetRequest_type delete_request = (DeleteResultSetRequest_type) (e.getPDU().o);

    // Create a DeleteResultSetResponse
    PDU_type pdu = new PDU_type();
    pdu.which=PDU_type.deleteresultsetresponse_CID;
    DeleteResultSetResponse_type response = new DeleteResultSetResponse_type();
    pdu.o = response;
    response.referenceId = delete_request.referenceId;


    if ( delete_request.deleteFunction.intValue() == 0 )
    {
      // Delete the result sets identified by delete_request.resultSetList
      for ( Enumeration task_list = delete_request.resultSetList.elements(); task_list.hasMoreElements(); )
      {
        String next_rs = (String) task_list.nextElement();
        // SearchTask st = (SearchTask)(active_searches.get(next_rs));
        active_searches.remove(next_rs);
        // search_service.deleteTask(st.getTaskIdentifier());
      }
    }
    else
    {
      // Function must be 1 : All sets in the association

      active_searches.clear();
    }

    response.deleteOperationStatus=BigInteger.valueOf(0); // 0 = Success, 1=resultSetDidNotExist, 2=previouslyDeletedByTarget, 3=systemProblemAtTarget
                                                          // 4 = accessNotAllowed, 5=resourceControlAtOrigin
    try
    {
      assoc.encodeAndSend(pdu);
    }
    catch ( java.io.IOException ioe )
    {
      ioe.printStackTrace();
    }
  }

  public void incomingAccessControlRequest(APDUEvent e)
  {
    cat.info("Incoming accessControlRequest");
  }

  public void incomingAccessControlResponse(APDUEvent e)
  {
    cat.info("Incoming AccessControlResponse");
  }

  public void incomingResourceControlRequest(APDUEvent e)
  {
    cat.info("Incoming resourceControlRequest");
  }

  public void incomingTriggerResourceControlRequest(APDUEvent e)
  {
    cat.info("Incoming triggetResourceControlRequest");
  }

  public void incomingResourceReportRequest(APDUEvent e)
  {
    cat.info("Incoming resourceReportRequest");
  }

  public void incomingScanRequest(APDUEvent e)
  {
    ScanRequest_type scan_request = (ScanRequest_type) (e.getPDU().o);

    int step_size = 0;
    int scan_status = 0;
    int number_of_entries_returned = 0;
    int position_of_term = 0;

    try
    {
      if ( this.search_service instanceof Scanable )
      {
        Scanable s = (Scanable)this.search_service;
        if ( s.isScanSupported() )
        {
          String name = null;
          OIDRegisterEntry ent = reg.lookupByOID(scan_request.attributeSet);
          if ( ent != null )
            name=ent.getName();

          RootNode rn = new RootNode();

          int i1 = ( scan_request.stepSize == null ? 0 : scan_request.stepSize.intValue() );
          int i2 = ( scan_request.numberOfTermsRequested == null ? 0 : 
                            scan_request.numberOfTermsRequested.intValue() );
          int i3 = ( scan_request.preferredPositionInResponse == null ? 0 : 
                            scan_request.preferredPositionInResponse.intValue() );

          ScanRequestInfo sri = new ScanRequestInfo();
          sri.collections = scan_request.databaseNames;
          sri.attribute_set = name;
          sri.term_list_and_start_point = com.k_int.z3950.util.RPN2Internal.convertAPT(scan_request.termListAndStartPoint,rn);
          sri.step_size = i1;
          sri.number_of_terms_requested = i2;
          sri.position_in_response = i3;

          ScanInformation scan_result = s.doScan(sri);

          assoc.sendScanResponse(scan_request.referenceId,
                                 BigInteger.valueOf(i1),
                                 BigInteger.valueOf(scan_status),
                                 BigInteger.valueOf(scan_result.position),
                                 scan_result.results,
                                 scan_request.attributeSet,
                                 null);
        }
      }
    }
    catch ( java.io.IOException ioe )
    {
      ioe.printStackTrace();
    }
  }

  public void incomingSortRequest(APDUEvent e)
  {
    cat.info("Incoming sortRequest");
  }

  public void incomingSegmentRequest(APDUEvent e)
  {
    cat.info("Incoming segmentRequest");
  }

  public void incomingExtendedServicesRequest(APDUEvent e)
  {
    cat.info("Incoming extendedServicesRequest");
  }

  public void incomingClose(APDUEvent e)
  {
    cat.debug("Close...");
    assoc.getPDUAnnouncer().deleteObserver(event_adapter);
    assoc.getPDUAnnouncer().deleteObservers();
    event_adapter = null;
    assoc.shutdown();

    try
    {
      assoc.join();
    }
    catch ( java.lang.InterruptedException ie )
    {
    }
    cat.debug("Done joining with assoc thread");

    cat.debug("Deleting tasks...");
    // Blow away any active search tasks. When these tasks finalize, they should make
    // sure they release any resources held by the creating Searchable object
    active_searches.clear();
    search_service.destroy();
    search_service = null;


    assoc = null;
  }

    Records_type createRecordsFor(SearchTask st,
				  int[] preferredRecordSyntax,
				  int start,
				  int count)
    {
	return createRecordsFor(st, preferredRecordSyntax, start, count, null);
    }
	
  // Helper functions
  Records_type createRecordsFor(SearchTask st,
                                int[] preferredRecordSyntax,
                                int start,
                                int count, String recordFormatSetname)
  {
    Records_type retval = new Records_type();

    cat.debug("createRecordsFor(st, "+start+","+count+")");
    cat.debug("pref rec syn = " + preferredRecordSyntax);
    cat.debug("record setname = " + recordFormatSetname);
    cat.debug("search task = " + st);
    
    // Try and do a normal present
    try
    {
      if ( start < 1 ) 
        throw new PresentException("Start record must be > 0","13");
      //should this be        >    ?  changed from that...

      int numRecs = st.getTaskResultSet().getFragmentCount();
   
      cat.debug("numresults = " + numRecs);
      int requestedNum = start + count - 1;
      if ( requestedNum >  numRecs ) {
	  count = numRecs - (start - 1);
	  if (start + count -1 > numRecs) {
	      cat.debug(requestedNum + " < " + numRecs);
	      throw new PresentException(
					 "Start+Count-1 (" +requestedNum + ") must be < the number of items in the result set: " 
					 +numRecs ,"13");
	  }
      }


      if ( st == null )
        throw new PresentException("Unable to locate result set","30");

      Vector v =  new Vector();
      retval.which = Records_type.responserecords_CID;
      retval.o = v;

      OIDRegisterEntry requested_syntax = null;
      String requested_syntax_name = null;
      if ( preferredRecordSyntax != null )
      {
        requested_syntax = reg.lookupByOID(preferredRecordSyntax);
        if(requested_syntax==null) { // unsupported record syntax
          StringBuffer oid=new StringBuffer();
          for(int i=0; i<preferredRecordSyntax.length; i++) {
              if(i!=0)
                  oid.append('.');
              oid.append(preferredRecordSyntax[i]);
          }
          cat.warn("Unsupported preferredRecordSyntax="+oid.toString());

          // Need to set up diagnostic in here
          retval.which = Records_type.nonsurrogatediagnostic_CID;
          DefaultDiagFormat_type default_diag = new DefaultDiagFormat_type();
          retval.o = default_diag;

          default_diag.diagnosticSetId = reg.oidByName("diag-1");
          default_diag.condition = BigInteger.valueOf(239);
          default_diag.addinfo = new addinfo_inline14_type();
          default_diag.addinfo.which = addinfo_inline14_type.v2addinfo_CID;
          default_diag.addinfo.o = (Object)(oid.toString());
          return retval;
        }
        cat.debug("requested_syntax="+requested_syntax);
        requested_syntax_name = requested_syntax.getName();
        cat.debug("requested_syntax_name="+requested_syntax_name);
      }
      else
      {
        requested_syntax_name = "xml";
        requested_syntax = reg.lookupByName(requested_syntax_name);
      }

      st.setRequestedSyntax(requested_syntax);
      st.setRequestedSyntaxName(requested_syntax_name);

      

      InformationFragment[] raw_records;

      //if (rfSpec == null) { //TODO: need to get this default behavior better organized
	 RecordFormatSpecification rfSpec = 
	     new RecordFormatSpecification(requested_syntax_name, null, recordFormatSetname);
	  //}
      cat.debug("calling getFragment("+(start)+","+count+")");
      raw_records = st.getTaskResultSet().getFragment(start, count, rfSpec);
     
      if ( raw_records == null )
        throw new PresentException("Error retrieving records","30");

      for ( int i=0; i<raw_records.length; i++ )
      {
        cat.debug("Adding record "+i+" to result");

        NamePlusRecord_type npr = new NamePlusRecord_type();
        npr.name = raw_records[i].getSourceCollectionName();
        npr.record = new record_inline13_type();
        npr.record.which = record_inline13_type.retrievalrecord_CID;
        EXTERNAL_type rec = new EXTERNAL_type();
        npr.record.o = rec;

        // com.k_int.IR.Syntaxes.Conversion.getConvertor(p).convert(Fragment, Target)

        // explain, sutrs, opac, summary, sgml, grs-1, xml, usmarc, ausmarc, ukmarc, etc

        // Or other generic encodings that can be stuffed in an octet aligned as the result of an XSL tranform
        if ( requested_syntax_name.equals(Z3950Constants.RECSYN_HTML) ||
	      requested_syntax_name.equals("sgml"))
        {
          cat.debug("Returning OctetAligned record for "+requested_syntax_name);
          //java.io.StringWriter sw = new java.io.StringWriter((String)raw_records[i].getOriginalObject());
	  //  record_schema_convertor.convert(raw_records[i].getDocument(),raw_records[i].getDocumentSchema(),requested_syntax_name,sw);

          rec.direct_reference = reg.oidByName(requested_syntax_name);
          rec.encoding = new encoding_inline0_type();
          rec.encoding.which = encoding_inline0_type.octet_aligned_CID;
          String raw_string = (String)raw_records[i].getOriginalObject();
          rec.encoding.o = raw_string.getBytes();  // Chance to change charset here
          if(raw_string.length()==0) {
            if(record_schema_convertor.getTemplateFor(
             raw_records[i].getDocumentSchema(), requested_syntax_name) ==
             null) {
              // can't make a html record
              retval.which = Records_type.nonsurrogatediagnostic_CID;
              DefaultDiagFormat_type default_diag =
                new DefaultDiagFormat_type();
              retval.o = default_diag;

              default_diag.diagnosticSetId = reg.oidByName("diag-1");
              default_diag.condition = BigInteger.valueOf(227);
              default_diag.addinfo = new addinfo_inline14_type();
              default_diag.addinfo.which = 
                addinfo_inline14_type.v2addinfo_CID;
              default_diag.addinfo.o = (Object)"1.2.840.10003.5.109.3";
               return retval;
            }
            else {
              // a surrogate diagnostic would be nice here!
            }
          }
        }
        else if ( requested_syntax_name.equals(Z3950Constants.RECSYN_XML) ) //
	
       {
          // Since XML is our canonical internal schema, all realisations of InformationFragment
          // are capable of providing an XML representation of themselves, so just use the
          // Fragments getDocument method.
          cat.debug("Returning OctetAligned XML");
          java.io.StringWriter sw = new java.io.StringWriter();

          try
          {
            OutputFormat format  = new OutputFormat( raw_records[i].getDocument() );
            XMLSerializer serial = new XMLSerializer( sw, format );
            serial.asDOMSerializer();
            serial.serialize( raw_records[i].getDocument().getDocumentElement() );
          }
          catch ( Exception e )
          {
            cat.error("Problem serializing dom tree to result record",e);
          }

          rec.direct_reference = reg.oidByName(requested_syntax_name);
          rec.encoding = new encoding_inline0_type();
          rec.encoding.which = encoding_inline0_type.octet_aligned_CID;
          rec.encoding.o = sw.toString().getBytes();  // Chance to change charset here
       } else if  ( requested_syntax_name.equals(Z3950Constants.RECSYN_SUTRS))		
	   {
	       rec.direct_reference = reg.oidByName(requested_syntax_name);
	       rec.encoding = new encoding_inline0_type();
	       rec.encoding.which = encoding_inline0_type.single_asn1_type_CID;


	        rec.encoding.o = ((String)(raw_records[i].getOriginalObject()));
	       System.out.println("about to get encoding...");
	   }
        v.add(npr);

      }
    }
    catch ( PresentException pe )
    {
      cat.warn("Processing present exception: "+pe.toString());

      // Need to set up diagnostic in here
      retval.which = Records_type.nonsurrogatediagnostic_CID;
      DefaultDiagFormat_type default_diag = new DefaultDiagFormat_type();
      retval.o = default_diag;

      default_diag.diagnosticSetId = reg.oidByName("diag-1");

      if ( pe.additional != null )
        default_diag.condition = BigInteger.valueOf( Long.parseLong(pe.additional.toString()) );
      else
        default_diag.condition = BigInteger.valueOf(0);

      default_diag.addinfo = new addinfo_inline14_type();
      default_diag.addinfo.which = addinfo_inline14_type.v2addinfo_CID;
      default_diag.addinfo.o = (Object)(pe.toString());
    } 

    System.out.println("retval = " + retval);
    return retval;
  }

    //TODO: It'd be nice to have better diagnostics, use explicit diagnostic
    // (use DiagFormat_type from jzkit) and specify exactly what the problem is.)
  private Records_type createNSD(String diag_code, String additional)
  {
    Records_type retval = new Records_type();
    retval.which = Records_type.nonsurrogatediagnostic_CID;
    DefaultDiagFormat_type default_diag = new DefaultDiagFormat_type();
    retval.o = default_diag;
 
    default_diag.diagnosticSetId = reg.oidByName("diag-1");
 
    if ( diag_code != null )
      default_diag.condition = BigInteger.valueOf( Long.parseLong(diag_code) );
    else
      default_diag.condition = BigInteger.valueOf(0);
 
    default_diag.addinfo = new addinfo_inline14_type();
    default_diag.addinfo.which = addinfo_inline14_type.v2addinfo_CID;
    default_diag.addinfo.o = (Object)(additional);

    return retval;
  }



}

