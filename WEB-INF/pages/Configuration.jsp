<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="info">
  <tr>
    <td class="label">	
		<logic:iterate id="key" indexId="ctr" name="GeoServer.ApplicationState" property="dataStoreErrorKeys">
			<nobr><%= key %></nobr><br>
		</logic:iterate>
	</td>
	<td class="datum">
		<logic:iterate id="value" indexId="ctr" name="GeoServer.ApplicationState" property="dataStoreErrorValues">
			<nobr><%= value %></nobr><br>
		</logic:iterate>
	</td>
  </tr>	
</table>