<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

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

<body>
<table class="page">
  <tbody>
	<tr class="header">
        <td class="gutter">
			<tiles:insert attribute="logo"/>
		</td>
		<td style="vertical-align: bottom; white-space: nowrap;">
			<tiles:insert attribute="serviceName"/>
		</td>	
		<td style="vertical-align: bottom; white-space: nowrap; text-align: right;">
			<tiles:insert attribute="contact"/>
        </td>
	</tr>
	
    <tr style="vertical-align: top;">
      <td class="sidebar">
        <tiles:insert attribute="status"/>		
        <tiles:insert attribute="buttons"/>          
		<tiles:insert attribute="actions"/>
		<tiles:insert attribute="messages"/>
      </td>
      <td style="vertical-align: top;"
          rowspan="1" colspan="2">
            
        <table cellpadding="0" cellspacing="0" border="0"
               style="text-align: left; width: 100%;">
          <tbody>
            <tr class="bar">
              <td class="locator">                        
                <tiles:insert attribute="location"/>
              </td>
              <td class="login">
                <tiles:insert attribute="loginStatus"/>
              </td>
            </tr>
          	<tr>
              <td class="<tiles:getAsString name='layer'/>"
                  rowspan="1" colspan="2">
                <div class="title"><tiles:getAsString name='title'/></div>  
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