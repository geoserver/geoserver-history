<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="info">
  <tbody>
    <tr>
      <td class="label">request:</td>
      <td class="datum">
        <html:form action="/demoSubmit">
        
          <html:select property="demo">
			<html:options property="demoList"/>
		  </html:select>
		  <html:submit property="action">
			<bean:message key="label.change"/>
		  </html:submit>  
        </html:form>		  
      </td>
    </tr>
    <form action="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wfs/TestWfsPost" method="POST">    
      <tr>
        <td class="label">url:</td>
        <td class="datum">
          <input type="text" size="80" name="url" value="<bean:write name="demoForm" property="url"/>">
        </td>
      </tr>
      <tr>
        <td class="label">body:</td>
        <td class="datum">
          <textarea rows=10 cols=80 name="body"><bean:write name="demoForm" property="body"/></textarea>
        </td>
      </tr>    
      <tr>
        <td class="label"></td>
        <td class="datum">
          <input type="submit">
        </td>
      </tr>
    </form>      
  </tbody>
</table>