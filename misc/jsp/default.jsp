<% String totalPath = HttpUtils.getRequestURL(request).substring( 0, HttpUtils.getRequestURL(request).length() - 11); %>
<jsp:useBean id="configuration" class="org.vfny.freefs.metadata.ConfigurationBean"/>
<jsp:useBean id="version" class="org.vfny.freefs.metadata.VersionBean"/>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Free Feature Server</title>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<style type="text/css">
<!--
.title {  font-family: Arial, Helvetica, sans-serif; font-size: 18px; font-weight: bold; color: #FFFFCC}
.bodyText {  font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: normal; color: #333333}
.bodyHeader { font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; color: #000000}
.bodyCaption { font-family: Arial, Helvetica, sans-serif; font-size: 10px; font-weight: bold; color: #333333 }
.titleSub { font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; color: #FFFFCC }
.bodyCaptionLabel { font-family: Arial, Helvetica, sans-serif; font-size: 10px; font-weight: lighter; color: #333333 }
.titleText { font-family: Arial, Helvetica, sans-serif; font-size: 18px; font-weight: bold; color: #FFFFFF}
.bodyHeader2 { font-family: Arial, Helvetica, sans-serif; font-size: 16px; font-weight: bold; color: #000000 }
-->
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
  <tr> 
    <td> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left" valign="top" class="bodyHeader"><a href="<%= totalPath %>default.jsp">[ home ]</a></td>
          <td>      <p class="bodyCaptionLabel" align="right">page automatically generated on:<span class="bodyCaption"> 
        <jsp:getProperty name="configuration" property="currentTime"/>
        </span><br>
        by: <span class="bodyCaption">FreeFeatureServer v
        <jsp:getProperty name="version" property="freeFsVersion"/>
        </span><br>
        for the URI: <span class="bodyCaption">
        <jsp:getProperty name="configuration" property="Url"/>
        </span> <br>
        maintained by: <span class="bodyCaption">
        <jsp:getProperty name="configuration" property="maintainer"/>
        </span></p></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td> 
      <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#339966">
        <tr> 
          <td class="titleSub"><span class="title">Free Feature Server&#153;:</span> 
            <span class="titleText"> 
            <jsp:getProperty name="configuration" property="title"/>
            </span> <br>
            Uniform Resource Indicator (URI) Meta-Data</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td height="2"> <!-- #BeginEditable "main" -->
      <table width="100%" border="0" cellspacing="8" cellpadding="8">
        <tr> 
          <td height="2"> 
            <p><span class="bodyHeader">Overview</span>:<br>
              <span class="bodyText">The URI you have selected serves up geographic 
              feature data, upon request, and outputs it using <a href="http://www.opengis.net/gml/01-029/GML2.html">Geographic 
              Markup Language V2.0 (GML)</a>.&nbsp;&nbsp;The server at this URI 
              implements the <a href="http://www.opengis.org">OpenGIS Consortium's</a> 
              Web Feature Server v 
              <jsp:getProperty name="version" property="wfsVersion"/>
              specification, which allows organizations and individuals to seamlessly 
              share vector-based, geographic data. &nbsp;The software that powers 
              this site is <a href="http://freefs.vfny.org">Free Feature Server</a>, 
              a free, open source software product developed by <a href="http://www.vfny.org">Vision 
              for New York</a>.&nbsp;&nbsp;Vision for New York is a 503c non-profit 
              that works to develop software tools that enhance modelling and 
              public participation in urban life.</span></p>
          </td>
        </tr>
        <tr> 
          <td> <span class="bodyHeader">Data Description:</span><br>
            <span class="bodyText"> 
            <jsp:getProperty name="configuration" property="abstract"/>
            </span> </td>
        </tr>
        <tr> 
          <td> 
            <p><span class="bodyHeader">Meta-Data Listing:</span><br>
              <span class="bodyText">Detailed metadata for all individual feature 
              types hosted at this URI may be accessed <a href="<%= totalPath %>featureTypes.jsp">here</a>.</span></p>
          </td>
        </tr>
        <tr> 
          <td height="60"> <span class="bodyHeader">Access Constraints:</span><br>
            <span class="bodyText"> 
            <jsp:getProperty name="configuration" property="accessConstraints"/>
            </span> </td>
        </tr>
        <tr> 
          <td> <span class="bodyHeader">Fees:</span><br>
            <span class="bodyText"> 
            <jsp:getProperty name="configuration" property="fees"/>
            </span> </td>
        </tr>
        <tr> 
          <td height="80"> <span class="bodyHeader">Documentation:</span><br>
            <span class="bodyText">This Free Feature Server implements the Web 
            Feature Server v 
            <jsp:getProperty name="version" property="wfsVersion"/>
            , update 
            <jsp:getProperty name="version" property="wfsUpdateSequence"/>
            specification, availible here. Documentation for version of Free Feature 
            Server that services this URI (v 
            <jsp:getProperty name="version" property="freeFsVersion"/>
            ) may be found <a href="../documentation/">here</a>. &nbsp;Documentation 
            for the current version of Free Feature Server may be found <a href="http://freefs.vfny.org/documentation/">here</a>.</span> 
          </td>
        </tr>
        <tr> 
          <td height="80"> 
            <p><span class="bodyHeader">Further Information:</span></p>
            <ul>
              <li><span class="bodyText">If you want to learn how to comminicate 
                with this server and extract data from it, you should read the 
                Web Feature Server specification.</span></li>
              <li><span class="bodyText">If you would like to learn more about 
                how to obtain and use <a href="http://freefs.vfny.org">Free Feature 
                Server</a>, you should view its website.</span></li>
              <li><span class="bodyText">If you want to learn more about the work 
                that <a href="http://www.vfny.org">Vision for New York</a> and 
                its mission, you should view its website.</span></li>
              <li><span class="bodyText">If you want to learn more about the standards 
                set forth by the OpenGIS Consortium, you should view its website.</span></li>
            </ul>
          </td>
        </tr>
      </table>
      <!-- #EndEditable --></td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
