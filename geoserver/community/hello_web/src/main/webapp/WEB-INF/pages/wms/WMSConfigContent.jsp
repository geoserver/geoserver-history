<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	
	<html:form action="/config/wms/contentSubmit" focus="serviceType">
	
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.enabled"/>">
			<bean:message key="label.enabled"/>:
		</span>
		</td><td colspan=2>
			<html:checkbox property="enabled"/>
	</td></tr>
		
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.onlineResource_service"/>">
			<bean:message key="label.onlineResource"/>:
		</span>
	</td><td colspan=2>
		<html:text property="onlineResource" size="60"/>
	</td></tr>
	
	
	<!-- put this in a loop for every layer-group they specify -->
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.baseMapTitle"/>">
			&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="label.baseMapTitle"/>:
		</span>
	</td><td colspan=2>
		<html:text property="baseMapTitle" size="15"/>
	</td></tr>
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.baseMapLayers"/>">
			<bean:message key="label.baseMapLayers"/>:
		</span>
	</td><td colspan=2>
		<html:text property="baseMapLayers" size="60"/>
	</td></tr>
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.baseMapStyles"/>">
			<bean:message key="label.baseMapStyles"/>:
		</span>
	</td><td colspan=2>
		<html:text property="baseMapStyles" size="60"/>
	</td></tr>

	<tr><td align="right">&nbsp;</td><td>
		<html:submit>
			<bean:message key="label.submit"/>
		</html:submit>
		
		<html:reset>
			<bean:message key="label.reset"/>
		</html:reset>
	</td></tr>
	</html:form>
</table>