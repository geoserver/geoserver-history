<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:html locale="true">

<bean:define id="titleKey">
	<tiles:getAsString name='title'/>
</bean:define>

<head>
  <title>
    GeoServer - <bean:message key="<%= titleKey %>"/>
  </title>

  <meta content="text/html; charset=iso-8859-1" http-equiv="content-type">
  <meta content="text/css" http-equiv="content-style-type">
  <meta name="description"
        content="Template created on Feb 11, 2004">
  <meta name="keywords"
        content="(GeoServer) (WFS) (Web Feature Server) (GIS) (Geographic Information Systems)">
  <meta name="author" content="Jody Garnett, Richard Gould">

  <style type="text/css">
    <!-- @import url("<html:rewrite forward='baseStyle'/>"); -->
  </style>
  
  <link type="image/gif" href="gs.gif" rel="icon"><!-- mozilla --> 
  <link href="gs.ico" rel="SHORTCUT ICON"><!-- ie -->

<html:base/>
</head>
<tiles:importAttribute scope="request"/>
<body>
<table class="page">
  <tbody>
	<tr class="header">
        <td class="gutter">
          <span class="project">
            <a href="<bean:message key="link.geoserver"/>">
              <bean:message key="geoserver.logo"/>
            </a>
          </span>
          <span class="license">
            <a href="<bean:message key="link.license"/>">&copy;</a>
          </span>
		</td>
		<td style="vertical-align: bottom; white-space: nowrap;">
          <span class="site">
<logic:notEmpty name="GeoServer" property="title">
              <bean:write name="GeoServer" property="title"/>
</logic:notEmpty>
<logic:empty name="GeoServer" property="title">
              <bean:message key="message.noTitle"/>
</logic:empty>            
          </span>			
		</td>	
		<td style="vertical-align: bottom; white-space: nowrap; text-align: right;">
<logic:notEmpty name="GeoServer" property="contactParty">
            <span class="contact">		
              <bean:message key="label.contact"/>: 	
              <html:link forward="contact">
                <bean:write name="GeoServer" property="contactParty"/>
              </html:link>
            </span>            
</logic:notEmpty>                
        </td>
	</tr>
	
    <tr>
      <td class="sidebar">
        <tiles:insert attribute="status"/>	
        <tiles:insert attribute="configActions"/>
        <tiles:insert attribute="actionator"/>
        <tiles:insert attribute="messages"/>
      </td>
      <td style="vertical-align: top;"
          rowspan="1" colspan="2">
            
        <table class="main">
          <tbody>
            <tr class="bar">
              <td class="locator">
                <tiles:insert attribute="locator"/>
              </td>
              <td class="loginStatus">
                <span class="loginStatus">
                  <logic:present name="GEOSERVER.USER">
                    <html:link forward="logout">
				      <bean:message key="label.logout"/>
			        </html:link>
                  </logic:present>
                  <logic:notPresent name="GEOSERVER.USER">
                    <html:link forward="login">
                      <bean:message key="label.login"/>
                    </html:link>
                  </logic:notPresent>
                </span>
              </td>
            </tr>
          	<tr>
              <td class="<tiles:getAsString name='layer'/>"
                  rowspan="1" colspan="2">
                <h1 class="title">
                  <bean:message key="<%= titleKey %>"/>
                </h1>  
                <tiles:insert attribute="body"/>                
              </td>
	        </tr>
          </tbody>
	    </table>
      </td>
	</tr>
  </tbody>
</table>

</body>
</html:html>