<table width=100%>
	<tr><td class="head"><font class="pn-title"><bean:message key="label.status"/></font></td></tr>
	<tr><td class="main">

	<bean:message key="label.configStatus"/>	
	<logic:equals name="GeoServer.ApplicationState" property="configChanged" value="true">
		<bean:message key="label.configChangedTrue"/>	
	</logic:equals>
	<logic:notEquals name="GeoServer.ApplicationState" property="configChanged" value="true">
		<bean:message key="label.configChangedFalse"/>	
	</logic:notEquals>
	
	<BR>
	
	<bean:message key="label.geoServerStatus"/>
	<logic:equals name="GeoServer.ApplicationState" property="geoServerChanged" value="true">
		<bean:message key="label.geoServerChangedTrue"/>
	</logic:equals>
	<logic:notEquals name="GeoServer.ApplicationState" property="geoServerChanged" value="true">
		<bean:message key="label.geoServerChangedFalse"/>
	</logic:notEquals>
		
	</tr></td>
</table>