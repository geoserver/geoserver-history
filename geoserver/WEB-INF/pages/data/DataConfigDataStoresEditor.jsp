<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:present name="GEOSERVER.USER" property="dataStoreConfig" scope="session">

<html:form action="/config/data/storeSubmit">
  <table class="info">
	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataStore_id"/>">
			<bean:message key="label.dataStoreID"/>:
		</span>
	  </td>
	  <td class="datum">
		<bean:write name="dataDataStoresEditorForm" property="dataStoreId"/>
	  </td>
	</tr>
	
	<tr>
	  <td class="label">
		<bean:message key="label.enabled"/>:
      </td>
	  <td class="datum">
		<html:checkbox property="enabled"/>
	  </td>
	</tr>
	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataStore_nameSpace"/>">
			<bean:message key="label.namespace"/>:
		</span>
      </td>
	  <td class="datum">
		<html:select property="namespaceId">
			<html:options property="namespaces"/>
		</html:select>
	  </td>
	</tr>
	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataStore_description"/>">
			<bean:message key="label.description"/>:
		</span>
      </td>
	  <td class="datum">
		<html:textarea property="description" cols="60" rows="2"/>
	  </td>
	</tr>
<logic:iterate id="param"
               indexId="ctr"
               name="dataDataStoresEditorForm"
               property="paramKeys">
<logic:notEqual name="dataDataStoresEditorForm"
                property='<%= "paramKey[" + ctr + "]"%>'
                value="dbtype">
    <tr>
	  <td class="label">
        <span class="help"
		      title="<bean:write name="dataDataStoresEditorForm"
		      property='<%= "paramHelp[" + ctr + "]" %>'/>">
          <bean:write name="dataDataStoresEditorForm"
                      property='<%= "paramKey[" + ctr + "]"%>'/>:
		</span>
      </td>
	  <td class="datum">
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
	<tr>
	  <td class="label">&nbsp;</td>
	  <td class="datum">
		<html:submit>
			<bean:message key="label.submit"/>
		</html:submit>
		
		<html:reset>
			<bean:message key="label.reset"/>
		</html:reset>
	  </td>
	</tr>						
  </table>
</html:form>
</logic:present>