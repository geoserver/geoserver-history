<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:form action="/config/data/styleEditorSubmit" enctype="multipart/form-data">

<table class="info">
  <tr>
    <td class="label"><bean:message key="label.styleID"/>:</td>
    <td class="datum"><html:text property="styleID" size="60"/></td>
  </tr>
  <tr>
    <td class="label"><bean:message key="label.filename"/></td>
    <td class="datum"><html:file size="60" property="sldFile"/></td>
  </tr>
   <!--- add the "fully validate" option-->
  <tr>
    <td class="label">&nbsp;</td>
    <td><html:checkbox property="fullyValidate">Fully Validate against the SLD schema</html:checkbox></td>
    
  </tr>
  <tr>
    <td class="label">&nbsp;</td>
    <td class="datum">
      <html:submit property="action"><bean:message key="label.submit"/></html:submit>
      <html:reset><bean:message key="label.reset"/></html:reset>
    </td>
  </tr>
 
</table>

</html:form>
