<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=1 width=100%>
<tr><td>
<table border=0 width=100%>
	<html:form action="DataConfigFeatureTypesSelect">

	<tr><td>
		<bean:message key="label.featureTypes"/>:
	</td><td>
		<html:select property="selectedFeatureTypeName">
			<html:options name="Config.Data" property="featureTypeKeySet"/>
		</html:select>
		
	</td></tr>
	<tr><td>&nbsp;</td>
	<td>

		<html:submit>
			<bean:message key="label.edit"/>
		</html:submit>
			
	</td></tr>
	</html:form>
</table>
</td></tr>
</table>