<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0><tr><td>
<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <font color="red">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </font>
</logic:notPresent>

<h3><bean:message key="welcome.heading"/></h3>
<p><html:link forward="mainmenu"><bean:message key="label.mainMenu"/></html:link></p>

<H2>Contact Information</h2><BR>

<%
	org.vfny.geoserver.config.GlobalConfig global = (org.vfny.geoserver.config.GlobalConfig) application.getAttribute(org.vfny.geoserver.config.GlobalConfig.CONFIG_KEY);
	org.vfny.geoserver.config.ContactConfig contact = global.getContact();
	
%>

<%= (contact.getContactPerson() != null) ? contact.getContactPerson()+"<BR>" : "" %>
<%= (contact.getContactPosition() != null) ? contact.getContactPosition()+"<BR>" : "" %>
<%= (contact.getContactOrganization() != null) ? contact.getContactOrganization()+"<BR><BR>" : "" %>
<%= (contact.getAddressType() != null) ? contact.getAddressType()+"<BR>" : "" %>
<%= (contact.getAddress() != null) ? contact.getAddress()+"<BR>" : "" %>
<%= (contact.getAddressCity() != null) ? contact.getAddressCity()+", " : "" %>
<%= (contact.getAddressState() != null) ? contact.getAddressState() : "" %>&nbsp;&nbsp;
<%= (contact.getAddressPostalCode() != null) ? contact.getAddressPostalCode()+"<BR>" : "" %>
<%= (contact.getAddressCountry() != null) ? contact.getAddressCountry()+"<BR>" : "" %>
<%= (contact.getContactVoice() != null) ? "Phone Number: "+contact.getContactVoice()+"<BR>" : "" %>
<%= (contact.getContactFacsimile() != null) ? "Fax: "+contact.getContactFacsimile()+"<BR>" : "" %>
<%= (contact.getContactEmail() != null) ? 
         "Email: <A HREF=\"mailto:"+contact.getContactEmail()+"\">"+contact.getContactEmail()+"</A><BR>" : "" %>

<%
	org.vfny.geoserver.config.WFSConfig wfsConfig = (org.vfny.geoserver.config.WFSConfig) application.getAttribute(org.vfny.geoserver.config.WFSConfig.CONFIG_KEY);
	org.vfny.geoserver.config.WMSConfig wmsConfig = (org.vfny.geoserver.config.WMSConfig) application.getAttribute(org.vfny.geoserver.config.WMSConfig.CONFIG_KEY);
%>
<BR>
<u><%= wfsConfig.getName() %>: <%= wfsConfig.getTitle() %></u><BR>
<%= wfsConfig.getAbstract() %><BR>
<HR>
<u><%= wmsConfig.getName() %>: <%= wmsConfig.getTitle() %></u><BR>
<%= wmsConfig.getAbstract() %><BR>
             
</td></tr></table>