<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table>
	<tr><td class="head"><font class="pn-title"><bean:message key="label.context"/></font></td></tr>
	<tr><td class="main">
		<html:link forward="mainmenu">
			<bean:message key="label.mainMenu"/>
		</html:link>
		<bean:message key="label.login"/> 
		<bean:message key="label.logout"/> 
		<bean:message key="label.help"/>	
	</tr></td>
</table>
