<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<logic:present name="selectedFeatureType" scope="session">

<table width=100%>
	<tr><td class="head"><font class="pn-title"> <bean:message key="label.actions"/></font></td></tr>
	<tr><td class="main">
		<html:form action="CalculateBoundingBox">
			<html:submit>
				<bean:message key="label.calculateBoundingBox"/>
			</html:submit>
		</html:form>
	</tr></td>
</table>

</logic:present>