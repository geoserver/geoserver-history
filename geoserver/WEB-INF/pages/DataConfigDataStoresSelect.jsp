<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="DataConfigDataStoresSelect">
	<tr><td valign="top">


		<html:select property="selectedDataStoreId">
			<html:options name="Config.Data" property="dataStoreIds"/>
		</html:select>
				
		</td><td valign="top">

		<BR>
		<html:submit property="buttonAction" value="edit">
			<bean:message key="label.edit"/>
		</html:submit>
		<BR>
		<html:submit property="buttonAction" value="delete">
			<bean:message key="label.delete"/>
		</html:submit>
		<BR>			
		
		</td><td valign="top">

	</td></tr>
	</html:form>
</table>