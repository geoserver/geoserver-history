<%//ApplicationState is currently not being loaded. @ taglib uri="/tags/struts-bean" prefix="bean" %>
<%//@ taglib uri="/tags/struts-html" prefix="html" %>
<%//@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table width=100%>
	<tr><td class="head"><font class="pn-title"><bean:message key="label.status"/></font></td></tr>
	<tr><td class="main">

	<bean:message key="label.configStatus"/>	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
		<bean:message key="label.configChangedTrue"/>	
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<bean:message key="label.configChangedFalse"/>	
	</logic:notEqual>
	
	<BR>
	
	<bean:message key="label.geoServerStatus"/>
	<logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">
		<bean:message key="label.geoServerChangedTrue"/>
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="geoServerChanged" value="true">
		<bean:message key="label.geoServerChangedFalse"/>
	</logic:notEqual>
		
	</tr></td>
</table>