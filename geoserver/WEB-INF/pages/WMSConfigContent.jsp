<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="WMSConfigContentSubmit" focus="serviceType">
	
	<tr><td align="right"><bean:message key="label.enabled"/>:</td><td colspan=2><html:checkbox property="enabled"/>
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.serviceType"/>">
			<bean:message key="label.onlineResource"/>:
		</span>
	</td><td colspan=2>
		<html:text property="onlineResource" size="60"/>
	</td></tr>

	<tr><td align="right"><span class="help" title="<bean:message key="help.serviceType"/>"><bean:message key="label.updateTime"/>:</span></td><td colspan=2><html:text property="updateTime" size="60"/></td></tr>
	
	<tr><td></td><td align="left"><bean:message key="label.featureList"/></td><td align="left"><bean:message key="label.namespace"/></tr>

<logic:iterate id="feature" name="wmsContentForm" property="features">
	<tr>

	<td></td>
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