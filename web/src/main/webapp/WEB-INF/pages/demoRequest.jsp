<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>



<table class="info" height="100%" width="100%">
  <tbody>
    <tr>
      <td class="label">Request:</td>
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
    <form action="../../../TestWfsPost" method="POST">    
    </tr>
      <tr>
        <td class="label">URL:</td>
        <td class="datum">
          <input id="url" type="text" size="90" name="url" value="<bean:write name="demoRequestForm" property="url"/>">
        </td>
      </tr>
      <tr>
        <td class="label">Body:</td>
        <td class="datum">
          <textarea rows="6" cols="90" name="body" id="body"><bean:write name="demoRequestForm" property="body"/></textarea>
        </td>
      </tr>    
      <tr>
        <td class="label"><bean:message key="label.username"/>:</td>
        <td class="datum">
          <input type="text" size="30" name="username" id="username"><bean:write name="demoRequestForm" property="username"/>
        </td>      
      </tr>
      <tr>
        <td class="label"><bean:message key="label.password"/>:</td>
        <td class="datum">
          <input type="password" size="30" name="password" id="password"><bean:write name="demoRequestForm" property="username"/>
        </td>      
      </tr>    
      <tr>
        <td class="label" width="1%"></td>
        <td class="datum" width="99%">
          <html:submit onclick="loadResults();return false;"><bean:message key="label.submit"/></html:submit>
          <html:submit><bean:message key="label.submitNew"/></html:submit>
        </td>
      </tr>
    </form>
    <tr>
      <td/>
      <td width="99%"><iframe id="response" width="100%" height="300px" frameborder="0"/></td>
    </tr>      
  </tbody>
</table>
