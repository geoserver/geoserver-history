<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<table width=100% border=1>
<tr>
	<td>
		<tiles:insert attribute="logo"/>
	</td>
	<td>
		<tiles:insert attribute="location"/>
	</td>
	<td align="right">
		<tiles:insert attribute="menu"/>
	</td>
</tr>
</table>