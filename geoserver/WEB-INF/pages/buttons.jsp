<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table width=100%>
	<tr><td class="head"><font class="pn-title"> <bean:message key="label.buttons"/></font></td></tr>
	<tr><td class="main" align="center" valign="top">
	<html:form action="SaveToGeoServer">
		<html:submit>
			<bean:message key="label.toGeoserver"/>
		</html:submit><BR>
	</html:form>
	</td></tr><tr><td class="main" align="center" valign="top">
	<html:form action="SaveToXML">	
		<html:submit>
			<bean:message key="label.saveXML"/>
		</html:submit><BR>
	</html:form>
	</td></tr><tr><td class="main" align="center" valign="top">
	<html:form action="LoadFromXML">			
		<html:submit>
			<bean:message key="label.loadXML"/>
		</html:submit><BR>	
	</html:form>	
	</tr></td>
</table>