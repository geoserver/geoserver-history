<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="status">
	<tr>
		<td class="module">
			<bean:message key="config.wfs.label"/>:
		</td>
		<td style="health">
			<table class="progress">
			  <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsGood" value="0">
		    	    <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wfsGood"/>%; background-color: green;">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsBad" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wfsBad"/>%; background-color: red;">
		            </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsDisabled" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wfsDisabled"/>%; background-color: gray;">
		            </td>     
		    	</logic:notEqual>		                   
		        </tr>
		      </tbody>
		     </table>
		</td>
	</tr>

	<tr>
		<td class="module">
			<bean:message key="config.wms.label"/>:
		</td>
		<td class="health">
			<table class="progress">
			    <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsGood" value="0">
		    	    <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wmsGood"/>%; background-color: green;">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsBad" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wmsBad"/>%; background-color: red;">
		            </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsDisabled" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wmsDisabled"/>%; background-color: gray;">
		            </td>     
		    	</logic:notEqual>		                   
		        </tr>
		        </tbody>
		     </table>
		</td>
	</tr>

	<tr>
		<td class="module">
			<bean:message key="data.label"/>:
		</td>
		<td class="health">
			<table class="progress">
			    <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataGood" value="0">
		    	    <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="dataGood"/>%; background-color: green;">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataBad" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="dataBad"/>%; background-color: red;">
		            </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataDisabled" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="dataDisabled"/>%; background-color: gray;">
		            </td>     
		    	</logic:notEqual>		                   
		        </tr>
		        </tbody>
		     </table>
		</td>
	</tr>
</table>