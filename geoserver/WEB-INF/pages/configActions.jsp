<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="state" width="100%">
  <tbody>
    <tr>
      <td>
        GeoServer
<logic:equal name="GeoServer.ApplicationState" property="appChanged" value="true">
        *
</logic:equal>
      </td>
      <td align="right">
<logic:notEmpty name="GeoServer.ApplicationState" property="appTimestamp">
        <bean:write name="GeoServer.ApplicationState"
                    property="appTimestamp"
                    format="MMM d, h:mm a"/>
</logic:notEmpty>
<logic:empty name="GeoServer.ApplicationState" property="appTimestamp">
        --
</logic:empty>
      </td>      
    </tr>
    <tr>
      <td>
        Configuration        
<logic:equal name="GeoServer.ApplicationState" property="configChanged" value="true">
        *
</logic:equal>        
      </td>
      <td align="right">
<logic:notEmpty name="GeoServer.ApplicationState" property="configTimestamp">
        <bean:write name="GeoServer.ApplicationState"
              property="configTimestamp"
              format="MMM d, h:mm a"/>
</logic:notEmpty>
<logic:empty name="GeoServer.ApplicationState" property="configTimestamp">
        --
</logic:empty>
      </td>
    </tr>
    <tr>
      <td>
        XML        
      </td>
      <td align="right">
<logic:notEmpty name="GeoServer.ApplicationState" property="xmlTimestamp">
        <bean:write name="GeoServer.ApplicationState"
                    property="xmlTimestamp"
                    format="MMM d, h:mm a"/>
</logic:notEmpty>
<logic:empty name="GeoServer.ApplicationState" property="xmlTimestamp">
        --
</logic:empty>
      </td>
    </tr>
  </tbody>
</table>

<table class="control">
  <tbody>
    <tr>
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
    </tr>
  </tbody>
</table>