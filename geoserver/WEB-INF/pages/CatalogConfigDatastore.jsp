<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="CatalogConfigDatastoreSubmit">
	
	<tr><td>
		<html:select property="selectedDataStore">
			<html:options collection="dataStores"/>
		</html:select>
		</td><td>
		<html:submit property="new" value="<bean:message key="label.new"/>"/><BR>
		<html:submit property="edit" value="<bean:message key="label.edit"/>"/><BR>
		<html:submit property="delete" value="<bean:message key="label.delete"/>"/><BR>			
		</td><td>
		<html:select property="selectedDataStoreType">
			<html:options collection="dataStoresType"/>
		</html:select>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.dataStoreID"/>
	</td><td colspan=2 align="left">
		<html:text property="dataStoreID" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.enabled"/>:
	</td><td colspan=2 align="left">
		<html:checkbox property="enabled"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.namespace"/>
	</td><td colspan=2 align="left">
		<html:select property="namespace">
			<html:options collection="namespaces"/>
		</html:select>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.description"/>
	</td><td colspan=2 align="left">
		<html:text property="description" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.server"/>
	</td><td colspan=2 align="left">
		<html:text property="server" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.port"/>
	</td><td colspan=2 align="left">
		<html:text property="port" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.username"/>
	</td><td colspan=2 align="left">
		<html:text property="username" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.password"/>
	</td><td colspan=2 align="left">
		<html:password property="password" size="60"/>
	</td></tr>

	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit/><html:reset/></td></tr>						
	
	</html:form>
</table>