<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<span class="buttons">
	<span class="buttonLabel">
		<html:link forward="mainmenu"><bean:message key="label.server"/></html:link><br>
		<html:link forward="validation"><bean:message key="label.validation"/></html:link>
	</span>
	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configChanged">
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configUnchanged">
	</logic:notEqual>
			<html:link forward="wfsConfigMenu"><bean:message key="label.wfs"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</span>
	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">		
		<span class="configChanged">
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configUnchanged">
	</logic:notEqual>	
			<html:link forward="wmsConfigMenu"><bean:message key="label.wms"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</span>
	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configChanged">
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configUnchanged">
	</logic:notEqual>
		<html:link forward="dataConfigMenu"><bean:message key="label.data"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</span>
	
	<span class="saveButtons">
		<html:form action="SaveToGeoServer">
			<html:submit>
				<bean:message key="label.apply"/>
			</html:submit>
		</html:form>
		<html:form action="SaveToXML">	
			<html:submit>
				<bean:message key="label.save"/>
			</html:submit>
		</html:form>
		<html:form action="LoadFromXML">			
			<html:submit>
				<bean:message key="label.load"/>
			</html:submit>
		</html:form>	
	</span>
</span>