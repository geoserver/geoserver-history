<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:errors/><BR>
	
	<html:form action="DataConfigDataStoresSubmit">

	<tr><td valign="top">
		<html:select property="selectedDataStore">
			<html:options property="dataStores"/>
		</html:select>
		
		</td><td valign="top">
		
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
		
		</td><td valign="top">
		<html:select property="selectedDataStoreType">
			<html:options property="dataStoreTypes"/>
		</html:select>
	</td></tr>

</table><table border=0 width=100%>

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
			<html:options property="namespaces"/>
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

	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit property="action" value="submit"/><html:reset/></td></tr>						
	
	</html:form>
</table>