<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0 width=100%>
	<html:form action="DataConfigDataStoresSubmit">
	
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataStore_id"/>">
			<bean:message key="label.dataStoreID"/>
		</span>
	</td><td colspan=2 align="left">
		<html:text property="dataStoreId" size="60"/>
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

<logic:iterate id="param" indexId="ctr" name="dataDataStoresEditorForm" property="connectionParamKeys">
	<logic:notEqual name="dataDataStoresEditorForm" property='<%= "connectionParamKey[" + ctr + "]"%>' value="dbtype">
	<tr><td align="right">
		<bean:write name="dataDataStoresEditorForm" property='<%= "connectionParamKey[" + ctr + "]"%>'/>
	</td><td colspan=2 align="left">
		<html:text property='<%= "connectionParamValue[" + ctr + "]"%>' size="60"/>
	</td></tr>
	</logic:notEqual>	
</logic:iterate>	
	
	<tr><td align="right">&nbsp;</td><td colspan=2><html:submit/><html:reset/></td></tr>						
	
	</html:form>
</table>