<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<bean:define id="titleKey">
	<tiles:getAsString name='title'/>
</bean:define>

<head>
<title><bean:message key="<%= titleKey %>"/></title>

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

<body bgcolor="white">
<table class="main">
	<tr>
		<td>
			<tiles:insert attribute="logo"/>
		</td>
		<td>
			<tiles:insert attribute="serviceName"/>
		</td>	
		<td>
			<tiles:insert attribute="contact"/>
        </td>
	</tr>
	<tr>
		<td class="sidebar">
			<table class="sidebar">
				<tr>
					<td>
						<tiles:insert attribute="status"/>
					</td>
				</tr>
				<tr>
					<td>
						<tiles:insert attribute="buttons"/>
					</td>
				</tr>
				<tr>
					<td>
						<tiles:insert attribute="actions"/>
					</td>
				</tr>
				<tr>
					<td>
						<tiles:insert attribute="messages"/>
					</td>
				</tr>
			</table>
		</td>
		<td>
			<table class="body">
				<tbody>	
				<tr>
            		<td class="location">
            			<tiles:insert attribute="location"/>
            		</td>
            		<td class="login">
            			<tiles:insert attribute="loginStatus"/>
            		</td>
          		</tr>
          		<tr>
            		<td>
            			<tiles:insert attribute="body"/>
            		</td>
	          </tr>
    		  </tbody>
	      </table>
		</td>
	</tr>
</table>

</body>
</html:html>