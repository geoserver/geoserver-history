<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<logic:present name="GEOSERVER.USER" property="dataStoreConfig" scope="session">
<table border=0 width=100%>
	<html:form action="DataConfigDataStoresSubmit">
	
	<tr><td align="right">
		<span class="help" title="<bean:message key="help.dataStore_id"/>">
			<bean:message key="label.dataStoreID"/>
		</span>
	</td><td colspan=2 align="left">
		<bean:write name="dataDataStoresEditorForm" property="dataStoreId"/>
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
		<html:select property="namespaceId">
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

<logic:iterate id="param"
               indexId="ctr"
               name="dataDataStoresEditorForm"
               property="paramKeys">
	<logic:notEqual name="dataDataStoresEditorForm"
	                property='<%= "paramKey[" + ctr + "]"%>'
	                value="dbtype">
  	<tr>
  		<td align="right">
			<span class="help"
		          title="<bean:write name="dataDataStoresEditorForm"
			      property='<%= "paramHelp[" + ctr + "]" %>'/>">
	        	<bean:write name="dataDataStoresEditorForm"
        	            	property='<%= "paramKey[" + ctr + "]"%>'/>
			</span>
		</td>
		<td colspan=2 align="left">
			<logic:notEqual name="dataDataStoresEditorForm"
	    	        	    property='<%= "paramKey[" + ctr + "]"%>'
			                value="passwd">
				<html:text property='<%= "paramValues[" + ctr + "]"%>' size="60"/>
			</logic:notEqual>
			<logic:equal name="dataDataStoresEditorForm"
	        		     property='<%= "paramKey[" + ctr + "]"%>'
			             value="passwd">
				<html:password property='<%= "paramValues[" + ctr + "]"%>' size="12" maxlength="10"/>
			</logic:equal>			             
		</td>
	</tr>
	</logic:notEqual>
</logic:iterate>	
	
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
</logic:present>