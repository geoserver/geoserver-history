<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<h3>DataStore Errors</h3>

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
	
<h3>NameSpace Errors</h3>
<table class="info">
  <tr>
    <td class="label">	
		<logic:iterate id="key" indexId="ctr" name="GeoServer.ApplicationState" property="nameSpaceErrorKeys">
			<nobr><%= key %></nobr><br>
		</logic:iterate>
	</td>
	<td class="datum">
		<logic:iterate id="value" indexId="ctr" name="GeoServer.ApplicationState" property="nameSpaceErrorValues">
			<nobr><%= value %></nobr><br>
		</logic:iterate>
	</td>
  </tr>	
</table>



</table>
</td></tr></table>