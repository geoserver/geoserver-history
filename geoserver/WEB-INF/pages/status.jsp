<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="status">

<logic:equal name="WFS" property="enabled" value="true">
	<tr>
		<td class="module">
			<bean:message key="config.wfs.label"/>:
		</td>
		<td style="health">
			<table class="progress">
			  <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsGood" value="0">
		    	    <td class="good"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="wfsGood"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="wfsGood"/>%">
		    	    <td/>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsBad" value="0">		    	
		    	    <td class="bad"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="wfsBad"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="wfsBad"/>%">
		    	    </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsDisabled" value="0">		    	
		    	    <td class="disabled"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="wfsDisabled"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="wfsDisabled"/>%">
		    	    </td>
		    	</logic:notEqual>		                   
		        </tr>
		      </tbody>
		     </table>
		</td>
	</tr>
</logic:equal>

<logic:equal name="WMS" property="enabled" value="true">
	<tr>
		<td class="module">
			<bean:message key="config.wms.label"/>:
		</td>
		<td class="health">
			<table class="progress">
			  <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsGood" value="0">
		    	    <td class="good"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="wmsGood"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="wmsGood"/>%">
		    	    </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsBad" value="0">		    	
		    	    <td class="bad"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="wmsBad"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="wmsBad"/>%">
		    	    </td>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wmsDisabled" value="0">		    	
		    	    <td class="disabled"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="wmsDisabled"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="wmsDisabled"/>%">
		    	    </td>
		    	</logic:notEqual>		                   
		        </tr>
		      </tbody>
		     </table>
		</td>
	</tr>
</logic:equal>

	<tr>
		<td class="module">
			<bean:message key="data.label"/>:
		</td>
		<td class="health">
            <table class="progress">
			  <tbody>
		        <tr>
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataGood" value="0">
		    	    <td class="good"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="dataGood"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="dataGood"/>%">
		    	        </td>
		    	</logic:notEqual>
		        <logic:notEqual name="GeoServer.ApplicationState" property="dataBad" value="0">		    	
		    	    <td class="bad"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="dataBad"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="dataBad"/>%">
		    	    </td/>
		    	</logic:notEqual>		            
		        <logic:notEqual name="GeoServer.ApplicationState" property="wfsDisabled" value="0">		    	
		    	    <td class="disabled"
		    	        width="<bean:write name="GeoServer.ApplicationState" property="dataDisabled"/>%"
		    	        title="<bean:write name="GeoServer.ApplicationState" property="dataDisabled"/>%">
		    	    <td/>
		    	</logic:notEqual>		                   
		        </tr>
		      </tbody>
		    </table>
		</td>
	</tr>
</table>