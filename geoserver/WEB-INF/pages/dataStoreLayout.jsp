<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<table border=0 width=100% height=100%>
	<tr>
		<td valign="top" align="left">
			<tiles:insert attribute="select"/>
		</td>
		<td valign="top" align="left">
			<tiles:insert attribute="new"/>
		</td>
	</tr>
	<tr><td colspan=2><HR></td></tr>
	<tr>
		<td valign="top" align="left" colspan=2>
			<tiles:insert attribute="editor"/>
		</td>
	</tr>
</table>
