<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table width=100%>
	<tr><td class="head" colspan=2><font class="pn-title"><bean:message key="label.status"/></font></td></tr>

	<tr><td class="main" align="right" valign="center">
		<bean:message key="label.wms"/>:
	</td><td class="main" valign="center" align="center">		
		<table border=0 cellspacing="0"><tr>
		<td bgcolor="#00DD00" width="<bean:write name="GeoServer.ApplicationState" property="wmsGood"/>" height="7">
        </td>
        <td bgcolor="#FF0000" width="<bean:write name="GeoServer.ApplicationState" property="wmsBad"/>" height="7">
        </td>
        <td bgcolor="#AAAAAA" width="<bean:write name="GeoServer.ApplicationState" property="wmsDisabled"/>" height="7">
        </td>
        </tr></table>
	</td></tr>

</table>