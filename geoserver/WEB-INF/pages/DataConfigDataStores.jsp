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
		<span class="help" title="<bean:message key="help.dataStore_id"/>">
			<bean:message key="label.dataStoreID"/>
		</span>
	</td><td colspan=2 align="left">
		<html:text property="dataStoreID" size="60"/>
	</td></tr>
	
	<tr><td align="right">
		<bean:message key="label.enabled"/>:
	</td><td colspan=2 align="left">
		<html:checkbox property="enabled"/>
	</td></tr>
	
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataStore_nameSpace"/>">
			<bean:message key="label.namespace"/>
		</span>
	</td><td colspan=2 align="left">
		<html:select property="namespace">
			<html:options property="namespaces"/>
		</html:select>
	</td></tr>
	
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataStore_description"/>">
			<bean:message key="label.description"/>
		</span>
	</td><td colspan=2 align="left">
		<html:text property="description" size="60"/>
	</td></tr>
	
	<!-- Connection Parameters here -->
	
	<tr><td colspan=2>
		<b><bean:message key="label.connectionParameters"/>
	</td></tr>

<logic:iterate id="param" indexId="ctr" name="dataDataStoresForm" property="connectionParamKeys">
	
	<tr><td align="right">
		<bean:write name="dataDataStoresForm" property='<%= "connectionParamKey[" + ctr + "]"%>'/>
	</td><td colspan=2 align="left">
		<html:text property='<%= "connectionParamValue[" + ctr + "]"%>' size="60"/>
	</td></tr>
	
</logic:iterate>	
	
	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit property="action" value="submit"/><html:reset/></td></tr>						
	
	</html:form>
</table>