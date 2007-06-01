<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core'%>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter"%>
<%@ page
	import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter"%>
<%@ page import="org.acegisecurity.AuthenticationException"%>


<c:if test="${not empty param.login_error}">
	<font color="red"> Your login attempt was not successful, try
	again.<BR>
	<BR>
	Reason: <%=((AuthenticationException) session
                                    .getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY))
                                    .getMessage()%> </font>
</c:if>

<form action="<c:url value='/j_acegi_security_check'/>" method="POST">
<table class="info">
	<tbody>
		<tr>
			<td class="label"><bean:message key="label.username" />:</td>
			<td><input type='text' name='username' size='60'
				<c:if test="${not empty param.login_error}">value='<%= session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY) %>'</c:if>></td>
		</tr>
		<tr>
			<td class="label"><bean:message key="label.password" />:</td>
			<td><input type='password' name='password' size='60'/></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="checkbox" name="_acegi_security_remember_me"/>
			<bean:message key="label.rememberLogin" /></td>
		</tr>

		<tr>
			<td class="label"></td>
			<td class="datum"><html:submit>
				<bean:message key="label.submit" />
			</html:submit><html:reset>
				<bean:message key="label.reset" />
			</html:reset></td>
		</tr>
	</tbody>
</table>

</form>
