<%@ taglib uri="/tags/struts-bean" prefix="bean" %>

<table width=100%>
	<tr><td class="head"><font class="pn-title"> <bean:message key="label.buttons"/></font></td></tr>
	<tr><td class="main">
	
	<html:form action="CatalogConfigDatastoreSubmit">	
		<html:submit property="toGeoServer" value="<bean:message key="label.toGeoserver"/>"/><BR>
		<html:submit property="saveXML" value="<bean:message key="label.saveXML"/>"/><BR>
		<html:submit property="loadXML" value="<bean:message key="label.loadXML"/>"/><BR>	
	</html:form>
	
	</tr></td>
</table>