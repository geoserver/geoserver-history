<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="control">
  <tbody>
    <tr>
      <td class="module">
        <html:link forward="admin">
		  <bean:message key="label.server"/>
        </html:link>
      </td>
      <td class="module">
        <html:link forward="config.validation">
		  <bean:message key="label.validation"/>
		</html:link>
      </td>
    </tr>
    <tr>
      <td
<logic:equal name="GeoServer.ApplicationState" property="wfsEdited" value="true">
          class="edited"
</logic:equal>
          >
        <html:link forward="config.wfs">
          <bean:message key="label.wfs"/>
<logic:equal name="GeoServer.ApplicationState" property="wfsChanged" value="true">
          *
</logic:equal>                    
        </html:link>
      </td>
      <td 
<logic:equal name="GeoServer.ApplicationState" property="wmsEdited" value="true">
          class="edited"
</logic:equal>
          >
        <html:link forward="config.wms">
          <bean:message key="label.wms"/>
<logic:equal name="GeoServer.ApplicationState" property="wmsChanged" value="true">
          *
</logic:equal>                    
        </html:link>
      </td>
    </tr>
    <tr>
	  <td colspan=2	    
	<logic:equal name="GeoServer.ApplicationState" property="dataEdited" value="true">
          class="edited"
 	</logic:equal>
 	      >
        <html:link forward="config.data">
          <bean:message key="label.data"/>
<logic:equal name="GeoServer.ApplicationState" property="dataChanged" value="true">
          *
</logic:equal>                    
        </html:link> 	      
      </td>
    </tr>
    <tr style="valign: bottom; height: 2em;">
      <td colspan=2>
        <html:form action="/admin/saveToGeoServer">
			<html:submit>
				<bean:message key="label.apply"/>
			</html:submit>
		</html:form>
		<html:form action="/admin/saveToXML">	
			<html:submit>
				<bean:message key="label.save"/>
			</html:submit>
		</html:form>
		<html:form action="/admin/loadFromXML">			
			<html:submit>
				<bean:message key="label.load"/>
			</html:submit>
		</html:form>      
      </td>
    </tr>
  </tbody>
<table>