<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <span class="error">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </span>
</logic:notPresent>

<span class="welcome">
	<bean:message key="welcome.heading"/>
</span>

<html:link forward="mainmenu">
	<bean:message key="label.mainMenu"/>
</html:link>

<span class="contactHeader">
	Contact Information
</span>

<span class="contactInfo">
<%
	org.vfny.geoserver.config.GlobalConfig global = (org.vfny.geoserver.config.GlobalConfig) application.getAttribute(org.vfny.geoserver.config.GlobalConfig.CONFIG_KEY);
	org.vfny.geoserver.config.ContactConfig contact = global.getContact();
	
%>

<%= (contact.getContactPerson() != null) ? contact.getContactPerson()+"<br>" : "" %>
<%= (contact.getContactPosition() != null) ? contact.getContactPosition()+"<br>" : "" %>
<%= (contact.getContactOrganization() != null) ? contact.getContactOrganization()+"<br><br>" : "" %>
<%= (contact.getAddressType() != null) ? contact.getAddressType()+"<br>" : "" %>
<%= (contact.getAddress() != null) ? contact.getAddress()+"<br>" : "" %>
<%= (contact.getAddressCity() != null) ? contact.getAddressCity()+", " : "" %>
<%= (contact.getAddressState() != null) ? contact.getAddressState() : "" %>&nbsp;&nbsp;
<%= (contact.getAddressPostalCode() != null) ? contact.getAddressPostalCode()+"<br>" : "" %>
<%= (contact.getAddressCountry() != null) ? contact.getAddressCountry()+"<BR>" : "" %>
<%= (contact.getContactVoice() != null) ? "<bean:message key=\"label.phoneNumber\"/>: "+contact.getContactVoice()+"<BR>" : "" %>
<%= (contact.getContactFacsimile() != null) ? "<bean:message key=\"label.faxNumber\"/>: "+contact.getContactFacsimile()+"<BR>" : "" %>
<%= (contact.getContactEmail() != null) ? 
         "<bean:message key=\"label.email\"/>: <A HREF=\"mailto:"+contact.getContactEmail()+"\">"+contact.getContactEmail()+"</A><BR>" : "" %>

<%
	org.vfny.geoserver.config.WFSConfig wfsConfig = (org.vfny.geoserver.config.WFSConfig) application.getAttribute(org.vfny.geoserver.config.WFSConfig.CONFIG_KEY);
	org.vfny.geoserver.config.WMSConfig wmsConfig = (org.vfny.geoserver.config.WMSConfig) application.getAttribute(org.vfny.geoserver.config.WMSConfig.CONFIG_KEY);
%>
</span>

<span class="serviceHeader">
	<%= wfsConfig.getName() %>: <%= wfsConfig.getTitle() %>
</span>
<span class="serviceAbstract">
	<%= wfsConfig.getAbstract() %>
</span>

<hr>

<span class="serviceHeader">
	<%= wmsConfig.getName() %>: <%= wmsConfig.getTitle() %>
</span>
<span class="serviceAbstract">
	<%= wmsConfig.getAbstract() %>
</span>

<span class="loginForm">
	<html:form action="LoginSubmit">
		<bean:message key="label.username"/>: <html:text size="60" property="username"/><br>
		<bean:message key="label.password"/>: <html:password size="60" property="password"/><br>
		<html:submit/><html:reset/>
	</html:form>
</span>