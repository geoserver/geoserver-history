<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table width=100%>
	<tr>
		<td style="vertical-align: top; text-align: right; white-space: nowrap; width: 10%;">
			<small><bean:message key="label.wfs"/>:<br></small>
		</td>
		<td style="vertical-align: top;">
			<table cellpadding="0" cellspacing="0" border="1" style="text-align: left; width: 100%; height: 70%;">
			    <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsGood" value="0">
		    	    <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wfsGood"/>%; background-color: rgb(0, 255, 0);">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsBad" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wfsBad"/>%; background-color: rgb(255, 0, 0);">
		            </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsDisabled" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wfsDisabled"/>%; background-color: rgb(255, 0, 0);">
		            </td>     
		    	</logic:notEqual>		                   
		        </tr>
		        </tbody>
		     </table>
		</td>
	</tr>

	<tr>
		<td style="vertical-align: top; text-align: right; white-space: nowrap; width: 10%;">
			<small><bean:message key="label.wms"/>:<br></small>
		</td>
		<td style="vertical-align: top;">
			<table cellpadding="0" cellspacing="0" border="1" style="text-align: left; width: 100%; height: 70%;">
			    <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsGood" value="0">
		    	    <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wmsGood"/>%; background-color: rgb(0, 255, 0);">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsBad" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wmsBad"/>%; background-color: rgb(255, 0, 0);">
		            </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsDisabled" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="wmsDisabled"/>%; background-color: rgb(100, 100, 100);">
		            </td>     
		    	</logic:notEqual>		                   
		        </tr>
		        </tbody>
		     </table>
		</td>
	</tr>

	<tr>
		<td style="vertical-align: top; text-align: right; white-space: nowrap; width: 10%;">
			<small><bean:message key="label.data"/>:<br></small>
		</td>
		<td style="vertical-align: top;">
			<table cellpadding="0" cellspacing="0" border="1" style="text-align: left; width: 100%; height: 70%;">
			    <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataGood" value="0">
		    	    <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="dataGood"/>%; background-color: rgb(0, 255, 0);">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataBad" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="dataBad"/>%; background-color: rgb(255, 0, 0);">
		            </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataDisabled" value="0">		    	
		            <td style="vertical-align: top; width: <bean:write name="GeoServer.ApplicationState" property="dataDisabled"/>%; background-color: rgb(100, 100, 100);">
		            </td>     
		    	</logic:notEqual>		                   
		        </tr>
		        </tbody>
		     </table>
		</td>
	</tr>
</table>