<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<span class="buttons">
	<span class="buttonLabel">
		<html:link forward="admin">
		  <bean:message key="label.server"/>
		</html:link><br>
		<html:link forward="config.validation">
		  <bean:message key="label.validation"/>
		</html:link>
	</span>
	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configChanged">
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configUnchanged">
	</logic:notEqual>
	
			<html:link forward="config.wfs">
			  <bean:message key="label.wfs"/>
			</html:link>
			
			<logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">
			  *
			</logic:equal>
	</span>
	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">		
		<span class="configChanged">
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configUnchanged">
	</logic:notEqual>	
			<html:link forward="config.wms"><bean:message key="label.wms"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</span>
	
	<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configChanged">
	</logic:equal>
	<logic:notEqual name="GeoServer.ApplicationState" property="configChanged" value="true">
		<span class="configUnchanged">
	</logic:notEqual>
		<html:link forward="config.data"><bean:message key="label.data"/></html:link><logic:equal name="GeoServer.ApplicationState" property="geoServerChanged" value="true">*</logic:equal>
	</span>
	
	<span class="saveButtons">
		<html:form action="/admin/saveToGeoServer">
			<html:submit>
				<bean:message key="label.apply"/>
			</html:submit>
		</html:form>
		<html:form action="/admin/saveToXML">	
			<html:submit>
				<bean:message key="label.save"/>
			</html:submit>
		</html:form>
		<html:form action="/admin/loadFromXML">			
			<html:submit>
				<bean:message key="label.load"/>
			</html:submit>
		</html:form>	
	</span>
</span>