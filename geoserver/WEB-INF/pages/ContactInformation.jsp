<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <span class="error">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </span>
</logic:notPresent>

<table class="info">
  <tbody>
<logic:notEmpty name="Config.Global" property="contact.contactPerson">  
    <tr>
      <td class="label"><bean:message key="label.contactPerson"/>:</td>
      <td class="datum">
        <bean:write name="Config.Global" property="contact.contactPerson"/>
      </td>      
    </tr>
</logic:notEmpty>    

<logic:notEmpty name="Config.Global" property="contact.contactPosition">  
    <tr>
      <td class="label"><bean:message key="label.contactPosition"/>:</td>
      <td class="datum">
        <bean:write name="Config.Global" property="contact.contactPosition"/>
      </td>
    </tr>
</logic:notEmpty>

<logic:notEmpty name="Config.Global" property="contact.contactOrganization">
    <tr>
      <td class="label"><bean:message key="label.contactOrganization"/>:</td>
      <td class="datum">   
        <bean:write name="Config.Global" property="contact.contactOrganization"/>
      </td>
    </tr>
</logic:notEmpty>

<logic:notEmpty name="Config.Global" property="contact.address">    
    <tr>
      <td class="label">
        Address:
      </td>
      <td class="datum">   
        <bean:write name="Config.Global" property="contact.addressType"/><br>
        <bean:write name="Config.Global" property="contact.address"/><br>        
        <bean:write name="Config.Global" property="contact.addressCity"/><br>
        <bean:write name="Config.Global" property="contact.addressState"/>&nbsp;&nbsp;
        <bean:write name="Config.Global" property="contact.addressPostalCode"/><br>
        <bean:write name="Config.Global" property="contact.addressCountry"/><br>        
      </td>
    </tr>
</logic:notEmpty>

<logic:notEmpty name="Config.Global" property="contact.contactVoice">
    <tr>
      <td class="label"><bean:message key="label.phoneNumber"/>:</td>
      <td class="datum">
        <bean:write name="Config.Global" property="contact.contactVoice"/>
      </td>
    </tr>
</logic:notEmpty>

<logic:notEmpty name="Config.Global" property="contact.contactFacsimile">
    <tr>
      <td class="label"><bean:message key="label.contactFacsimile"/>:</td>
      <td class="datum">
        <bean:write name="Config.Global" property="contact.contactFacsimile"/>
      </td>
    </tr>
</logic:notEmpty>

<logic:notEmpty name="Config.Global" property="contact.contactEmail">    
    <tr>
      <td class="label"><bean:message key="label.contactEmail"/>:</td>
      <td class="datum">
        <bean:write name="Config.Global" property="contact.contactEmail"/>
      </td>
    </tr>
</logic:notEmpty>
    
  </tbody>
</table>