<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>

	<html:form action="DataConfigFeatureTypesSubmit">
	
	<tr><td align="right">
		<bean:message key="label.name"/>:
	</td><td colspan=2 align="left">
		<bean:write name="dataFeatureTypesEditorForm" property="name"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.SRS"/>:
	</td><td colspan=2 align="left">
		<html:text property="SRS" size="60"/>
	</td></tr>

	<tr><td align="right">
		<bean:message key="label.title"/>:
	</td><td colspan=2 align="left">
		<html:text property="title" size="60"/>
	</td></tr>

	<tr><td align="right">
		<bean:message key="label.latLonBoundingBox"/>:
	</td><td colspan=2 align="left">
		<html:text property="latLonBoundingBoxMinX" size="30"/>
		<html:text property="latLonBoundingBoxMinY" size="30"/>
	</td></tr>
	<tr><td align="right">

	</td><td colspan=2 align="left">
		<html:text property="latLonBoundingBoxMaxX" size="30"/>
		<html:text property="latLonBoundingBoxMaxY" size="30"/>
	</td></tr>

	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataFeatureTypeKeywords"/>">
			<bean:message key="label.keywords"/>:
		</span>
	</td><td>
		<html:textarea property="keywords" cols="60" rows="2"/>
	</td></tr>

	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataFeatureTypeAbstract"/>">
			<bean:message key="label.abstract"/>:
		</span>
	</td><td>
		<html:textarea property="_abstract" cols="60" rows="3"/>
	</td></tr>
	<tr><td align="right" valign="top">
		<bean:message key="label.default"/>:
	</td><td valign="top" align="left">
		<html:checkbox property="_default"/>
	</td></tr>

	<tr><td align="right">&nbsp;</td><td colspan=2>
		<html:submit>
			<bean:message key="label.submit"/>
		</html:submit>
		
		<html:reset>
			<bean:message key="label.reset"/>
		</html:reset>
	</td></tr>						
	
	</html:form>
</table>