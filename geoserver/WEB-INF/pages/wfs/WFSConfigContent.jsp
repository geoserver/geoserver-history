<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:form action="WFSConfigContentSubmit">
	
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
	
	<tr><td>&nbsp;</td><td align="left">
		<span class="help" title="<bean:message key="help.featureList"/>">
			<bean:message key="label.featureList"/>
		</span>
	</td><td align="left">
		<bean:message key="label.namespace"/>
	</td></tr>

<logic:iterate id="feature" name="wfsContentForm" property="features">
	<tr><td>&nbsp;</td>
	<td align="left">

			<html:multibox property="selectedFeatures">
				<bean:write name="feature"/>
			</html:multibox>
				<bean:write name="feature"/>

	</td>
	<td align="left">
			NAMESPACE-<bean:write name="feature"/>
	</td>
	</tr>
</logic:iterate>

	<tr><td align="right">&nbsp;</td><td><html:submit/><html:reset/></td></tr>
	</html:form>
</table>