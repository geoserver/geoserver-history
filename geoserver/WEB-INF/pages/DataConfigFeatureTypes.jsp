<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="DataConfigFeatureTypesSubmit">
	
	<tr><td>
		<bean:message key="label.featureTypes"/>:
	</td><td>
		<html:select property="selectedFeatureType">
			<html:options property="featureTypes"/>
		</html:select>
		
	</td><td>
		
		<html:submit property="action" value="new">
			<bean:message key="label.new"/>
		</html:submit>
		<BR>
		<html:submit property="action" value="edit">
			<bean:message key="label.edit"/>
		</html:submit>
		<BR>
		<html:submit property="action" value="delete">
			<bean:message key="label.delete"/>
		</html:submit>
		<BR>
			
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.name"/>:
	</td><td colspan=2 align="left">
		<html:text property="name" size="60"/>
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
		<html:text property="latlonBoundingBox" size="60"/>
	</td></tr>

	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataFeatureTypeKeywords"/>">
			<bean:message key="label.keywords"/>:
		</span>
	</td><td>
		<html:textarea property="keywords" cols="60" rows="10"/>
	</td></tr>

	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataFeatureTypeAbstract"/>">
			<bean:message key="label.abstract"/>:
		</span>
	</td><td>
		<html:textarea property="_abstract" cols="60" rows="6"/>
	</td></tr>

	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit property="action" value="submit"/><html:reset/></td></tr>						
	
	</html:form>
</table>