<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
	org.vfny.geoserver.config.GlobalConfig global = (org.vfny.geoserver.config.GlobalConfig) application.getAttribute(org.vfny.geoserver.config.GlobalConfig.CONFIG_KEY);
	org.vfny.geoserver.config.ContactConfig contact = global.getContact();
%>

<span class="contact">		
	<bean:message key="label.contact"/>: 
    <html:link forward="geoServerConfiguration">
    	<%= (contact.getContactPerson() != null && !contact.getContactPerson().equals("")) ? contact.getContactPerson() : "None" %>
	</html:link>
</span>
