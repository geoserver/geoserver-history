<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table width=100%>
	<tr><td class="head" colspan=2><font class="pn-title"><bean:message key="label.buttons"/></font></td></tr>
<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
	<tr><td class="changed" align="center" valign="center">
</logic:equal>
<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
	<tr><td class="main" align="center" valign="center">
</logic:notEqual>
		<html:link forward="wfsConfigMenu"><bean:message key="label.wfs"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">		
	</td><td class="changed" align="center" valign="center">
</logic:equal>
<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
	</td><td class="main" align="center" valign="center">
</logic:notEqual>	
		<html:link forward="wmsConfigMenu"><bean:message key="label.wms"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</td></tr>
<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
	<tr><td class="changed" align="center" valign="center" colspan=2>
</logic:equal>
<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
	<tr><td class="main" align="center" valign="center" colspan=2>
</logic:notEqual>
		<html:link forward="dataConfigMenu"><bean:message key="label.data"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</td></tr>
	<tr><td class="main" align="center" valign="center" colspan=2>
		<table border=0>
		<tr><td align="center" valign="center">
		<html:form action="SaveToGeoServer">
			<html:submit>
				<bean:message key="label.apply"/>
			</html:submit>
		</html:form>
		</td><td>
		<html:form action="SaveToXML">	
			<html:submit>
				<bean:message key="label.save"/>
			</html:submit>
		</html:form>
		</td><td>
		<html:form action="LoadFromXML">			
			<html:submit>
				<bean:message key="label.load"/>
			</html:submit>
		</html:form>	
		</td></tr>
		</table>
	</td></tr>
</table>