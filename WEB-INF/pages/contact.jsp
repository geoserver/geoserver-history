<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<span class="contact">		
	<bean:message key="label.contact"/>: 	
    <html:link forward="contact">      
      <logic:notEmpty name="Config.Global" property="contact.contactParty">
        <bean:write name="Config.Global" property="contact.contactParty"/>
      </logic:notEmpty>      
      <logic:empty name="Config.Global" property="contact.contactParty">
        info
      </logic:empty>      
	</html:link>
</span>
