<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:present name="GEOSERVER.USER" property="dataFormatConfig" scope="session">

<html:form action="/config/data/formatSubmit">
  <table class="info">	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataFormat_id"/>">
			<bean:message key="label.dataFormatID"/>:
		</span>
	  </td>
	  <td class="datum">
		<bean:write name="dataFormatsEditorForm" property="dataFormatId"/>
	  </td>
	</tr>	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataFormat_enabled"/>">
          <bean:message key="label.enabled"/>:
        </span>
      </td>
	  <td class="datum">
		<html:checkbox property="enabled"/>
	  </td>
	</tr>	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataFormat_type"/>">
			<bean:message key="label.type"/>:
		</span>
      </td>
	  <td class="datum">
          <html:text property="type" size="60" readonly="true"/>
	  </td>
	</tr>	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataFormat_url"/>">
			<bean:message key="label.url"/>:
		</span>
      </td>
	  <td class="datum">
          <html:text property="url" size="60"/>
	  </td>
	</tr>	
	<tr>
	  <td class="label">
		<span class="help" title="<bean:message key="help.dataFormat_description"/>">
			<bean:message key="label.description"/>:
		</span>
      </td>
	  <td class="datum">
		<html:textarea property="description" cols="60" rows="2"/>
	  </td>
	</tr>


<logic:notEmpty name="dataFormatsEditorForm"
               property="paramKeys">
<logic:iterate id="param"
               indexId="ctr"
               name="dataFormatsEditorForm"
               property="paramKeys">
<logic:notEqual name="dataFormatsEditorForm"
                property='<%= "paramKey[" + ctr + "]"%>'
                value="dbtype">
    <tr>
	  <td class="label">
        <span class="help"
		      title="<bean:write name="dataFormatsEditorForm"
		      property='<%= "paramHelp[" + ctr + "]" %>'/>">
          <bean:write name="dataFormatsEditorForm"
                      property='<%= "paramKey[" + ctr + "]"%>'/>:
		</span>
      </td>
	  <td class="datum">
<logic:notEqual name="dataFormatsEditorForm"
	    	        property='<%= "paramKey[" + ctr + "]"%>'
			        value="passwd">
          <html:text property='<%= "paramValues[" + ctr + "]"%>' size="60"/>
</logic:notEqual>
<logic:equal name="dataFormatsEditorForm"
   		     property='<%= "paramKey[" + ctr + "]"%>'
             value="passwd">
          <html:password property='<%= "paramValues[" + ctr + "]"%>' size="12" maxlength="10"/>
</logic:equal>			             
	  </td>
	</tr>
</logic:notEqual>
</logic:iterate>
</logic:notEmpty>

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