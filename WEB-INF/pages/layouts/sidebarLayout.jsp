<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<table border=0 width=100% height=100%>
	<tr>
		<td valign="top" align="left">
			<tiles:insert attribute="status"/>
		</td>
	</tr>
	<tr>
		<td valign="top" align="left">
			<tiles:insert attribute="buttons"/>
		</td>
	</tr>
	<tr>
		<td valign="top" align="left">
			<tiles:insert attribute="messages"/>
		</td>
	</tr>
</table>
