<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="WFSConfigContentSubmit" focus="serviceType">
	
	
	<tr><td align="right"><span class="help" title="<bean:message key="help.serviceType"/>"><bean:message key="label.serviceType"/>:</span></td><td><html:text property="serviceType" size="60"/></td></tr>
	<tr><td align="right">Enabled:<html:checkbox property="enabled"/>
	<tr><td align="right">
	
		<logic:iterate id="_bean" name="wfsContentForm" property="features">
			<html:multibox property="selectedFeatures">
				<bean:write name="_bean"/>
			</html:multibox>
				<bean:write name="_bean"/>
		</logic:iterate>
		
		
	</td></tr>
	<tr><td align="right">&nbsp;</td><td><html:submit/><html:reset/></td></tr>
	</html:form>
</table>