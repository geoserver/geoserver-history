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
          <td align="left" valign="top" class="bodyHeader"><a href="default.jsp">[ home ]</a></td>
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
      <p class="bodyHeader2">Feature Type Listing</p>
<table width="100%" border="0" cellspacing="2" cellpadding="2" class="bodyText">
  <tr>
          <td class="bodyHeader" width="19%">Name</td>
          <td class="bodyHeader" width="19%">Host</td>
          <td class="bodyHeader" width="62%">Description</td>
  </tr><tr>
<%@ page import="java.io.*,java.util.*,org.vfny.freefs.metadata.*" %>
        <%	ReadFeatureTypes featureTypes = new ReadFeatureTypes();%>
        <%=	featureTypes.listFilesHtml("/home/rob/wfs/documents/featureTypes/") %>
</tr></table>
      <!-- #EndEditable --></td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
