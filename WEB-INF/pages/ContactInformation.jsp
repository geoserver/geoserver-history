<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <span class="error">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </span>
</logic:notPresent>

<%
	org.vfny.geoserver.config.GlobalConfig global = (org.vfny.geoserver.config.GlobalConfig) application.getAttribute(org.vfny.geoserver.config.GlobalConfig.CONFIG_KEY);
	org.vfny.geoserver.config.ContactConfig contact = global.getContact();	
%>
<table class="info">
  <tbody>
    <tr>
      <td class="label">
        Contact:
      </td>
      <td class="datum">
<%= (contact.getContactPerson() != null) ? contact.getContactPerson() : "--" %>
      </td>
    </tr>
    <tr>
      <td class="label">
        Position:
      </td>
      <td class="datum">
<%= (contact.getContactPosition() != null) ? contact.getContactPosition() : "--" %>
      </td>
    </tr>
    <tr>
      <td class="label">
        Organization:
      </td>
      <td class="datum">   
<%= (contact.getContactOrganization() != null) ? contact.getContactOrganization()+"" : "--" %>
      </td>
    </tr>
    <tr>
      <td class="label">
        Address:
      </td>
      <td class="datum">   
<%= (contact.getAddressType() != null) ? contact.getAddressType()+"<br>" : "" %>
<%= (contact.getAddress() != null) ? contact.getAddress()+"<br>" : "" %>
<%= (contact.getAddressCity() != null) ? contact.getAddressCity()+", " : "" %>
<%= (contact.getAddressState() != null) ? contact.getAddressState() : "" %>&nbsp;&nbsp;
<%= (contact.getAddressPostalCode() != null) ? contact.getAddressPostalCode()+"<br>" : "" %>
<%= (contact.getAddressCountry() != null) ? contact.getAddressCountry()+"<BR>" : "" %>
      </td>
    </tr>
    <tr>
      <td class="label">
        Voice:
      </td>
      <td class="datum">
<%= (contact.getContactVoice() != null) ? "<bean:message key=\"label.phoneNumber\"/>: "+contact.getContactVoice() : "--" %>
      </td>
    </tr>
    <tr>
      <td class="label">
        Fax:
      </td>
      <td class="datum">
<%= (contact.getContactFacsimile() != null) ? "<bean:message key=\"label.faxNumber\"/>: "+contact.getContactFacsimile() : "--" %>
      </td>
    </tr>
    <tr>
      <td class="label">
        Email:
      </td>
      <td class="datum">
<%= (contact.getContactEmail() != null) ? 
         "<bean:message key=\"label.email\"/>: <A HREF=\"mailto:"+contact.getContactEmail()+"\">"+contact.getContactEmail()+"</A>" : "--" %>
      </td>
    </tr>
  </tbody>
</table>