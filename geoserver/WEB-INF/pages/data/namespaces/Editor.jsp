<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:form action="/config/data/namespaceSubmit">
  <table class="info">
	<tr>
      <td class="label">
		<bean:message key="label.default"/>:
      </td>
      <td class="datum">
		<html:checkbox property="_default"/>
      </td>
    </tr>
    <tr>
      <td class="label">
		<bean:message key="label.URI"/>
      </td>
      <td class="datum">
		<html:text property="URI" size="60"/>
      </td>
    </tr>
    <tr>
      <td class="label">
		<bean:message key="label.prefix"/>
      </td>
      <td class="datum">
		<html:text property="prefix" size="60"/>
      </td>
    </tr>
    <tr>
      <td class="label">&nbsp;</td>
      <td class="datum">
		<html:submit property="action">
			<bean:message key="label.submit"/>
		</html:submit>		
		<html:reset>
			<bean:message key="label.reset"/>
        </html:reset>
	  </td>
    </tr>							
  </table>
</html:form>