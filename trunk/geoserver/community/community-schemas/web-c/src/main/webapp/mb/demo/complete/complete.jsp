<%@page contentType="text/html"%>

<!--
Description: A Browser based Web Map Server Client based on javascript and XSL
             libraries from http://mapbuilder.sourceforge.net .
Licence:     LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id: complete.jsp 1608 2005-08-03 19:07:09Z mattdiez $
$Name$
-->
<%
  request.setAttribute("mbConfigUrl", "/demo/complete/completeConfig.xml");
%>


<html>
  <head>
    <title>Mapbuilder Demo</title>

    <link rel="stylesheet" href="<%= request.getContextPath() %>/lib/skin/default/mapStyle.css" type="text/css">

  <jsp:include page="/MapbuilderHead.jsp" flush="true"/>

  </head>

  <body onload="mbDoLoad()">
    <!-- Layout mapbuilder widgets and HTML -->
    <h1><a href="http://mapbuilder.sourceforge.net">Community Map Builder</a> Demo</h1>
    <table>
      <tr>
        <td valign="top" style="padding:6px;width:180">
        <div id="locatorMap" >
        </td>
        <td>
          <h2 id="mapTitle"></h2>
          <div id="mainMapPane"/>
        </td>
      </tr>
      <tr>
        <td/>
        <td valign="top">
          <table width="100%">
            <tr>
              <td align="left">
                <div id="mainButtonBar"/>
              </td>
              <td align="center" id="mapScaleText"/>
              <td align="right" id="cursorTrack" />
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td id="legend"/>
        <td id="collectionList"/>
      </tr>
      <tr>
        <td id="locationsSelect"/>
        <td id="ntsLookupForm"/>
      </tr>
      <tr>
        <td id="urlInput"/>
        <td id="abstract"/>
      </tr>
      <tr>
        <td id="saveModel"/>
        <td id="wmsCapabilitiesImport"/>
      </tr>
      <tr>
        <td/>
        <td id="aoiForm"/>
      </tr>
      <tr>
        <td id="featureList"/>
        <td id="transactionResponse"/>
      </tr>
      <tr>
        <td colspan="2">
          <div id="eventLog"/>
        </td>
      </tr>
    </table>
    <br/>
    <p align="right"><a href="http://mapbuilder.sourceforge.net">Community Map Builder</a><br/>$Name$</p>

  </body>
</html>
