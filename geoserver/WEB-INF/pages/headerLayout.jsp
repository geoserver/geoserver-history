<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<table width=100% border=0>
<tr>
	<td width=378>
		<tiles:insert attribute="logo"/>
	</td>
	<td valign="bottom" align="left">
		<tiles:insert attribute="location"/>
	</td>
	<td align="right" valign="bottom">
		<tiles:insert attribute="menu"/>
	</td>
</tr>
<tr>
<td colspan=3><hr></td>
</tr>
</table>