<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=1 width=100%>
<tr><td>
<table border=0 width=100%>
	<html:form action="/config/data/featureTypeSelect">

	<tr><td align="right" valign="top">
		<bean:message key="label.featureTypes"/>:
	</td><td>
		<html:select property="selectedFeatureTypeName">
			<html:options property="typeNames"/>
		</html:select>
		
	</td></tr>
	<tr><td>&nbsp;</td>
	<td align="left" valign="top">

		<html:submit property="buttonAction">
			<bean:message key="label.edit"/>
		</html:submit>
		<html:submit property="buttonAction">
			<bean:message key="label.delete"/>
		</html:submit>
					
	</td></tr>
	</html:form>
</table>
</td></tr>
</table>