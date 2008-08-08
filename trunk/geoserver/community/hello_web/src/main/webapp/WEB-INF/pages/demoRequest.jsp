<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="info">
  <tbody>
    <tr>
      <td class="label">request:</td>
      <td class="datum">
        <html:form action="/demoRequestSubmit">
        
          <html:select property="demo">
			<html:options property="demoList"/>
		  </html:select>
		  <html:submit property="action">
			<bean:message key="label.change"/>
		  </html:submit>  
        </html:form>		  
      </td>
    </tr>
    <form action="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>TestWfsPost" method="POST">    
      <tr>
        <td class="label">url:</td>
        <td class="datum">
          <input type="text" size="90" name="url" value="<bean:write name="demoRequestForm" property="url"/>">
        </td>
      </tr>
      <tr>
        <td class="label">body:</td>
        <td class="datum">
          <textarea rows="10" cols="90" name="body"><bean:write name="demoRequestForm" property="body"/></textarea>
        </td>
      </tr>    
      <tr>
        <td class="label"></td>
        <td class="datum">
          <html:submit><bean:message key="label.submit"/></html:submit>
        </td>
      </tr>
    </form>      
  </tbody>
</table>
