<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<span class="loginStatus">
	<logic:present name="GEOSERVER.USER">
			<html:link forward="logout">
				<bean:message key="label.logout"/>
			</html:link>
	</logic:present>
	<logic:notPresent name="GEOSERVER.USER">
			<html:link forward="login">
				<bean:message key="label.login"/>
			</html:link>
	</logic:notPresent>
</span>