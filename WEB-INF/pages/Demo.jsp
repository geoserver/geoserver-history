<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<ul>
  <li>
    <a href="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wfs/GetCapabilities">
      getCapabilities
    </a>
  </li>
  <li>
    <a href="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wfs/TestWfsPost">
      TestWfsPost
    </a>
  </li>
</ul>

<html:form action="/admin/demoSubmit">
  <table class="info">
    <tbody>
      <tr>
        <td class="label">demo:</td>
        <td class="datum">
          <html:select property="demo">
			<html:options property="demoList"/>
		  </html:select>
		  <html:submit property="action">
			<bean:message key="label.change"/>
		  </html:submit>  
        </td>
      </tr>
    </tbody>
  </table>
</html:form>

<form action="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wfs/TestWfsPost" method="POST">
  <table class="info">
    <tbody>      
      <tr>
        <td class="label">url:</td>
        <td class="datum">
          <input type="text" size="80" name="url">
        </td>
      </tr>
      <tr>
        <td class="label">body:</td>
        <td class="datum">
          <textarea rows=4 cols=80 name="body"></textarea>
        </td>
      </tr>    
      <tr>
        <td class="label"></td>
        <td class="datum">
          <input type="submit">
        </td>
      </tr>
    </tbody>
  </table>
</form>