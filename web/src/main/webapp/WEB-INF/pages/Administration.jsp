<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<table class="info">
  <tbody>
    <tr>
      <td class="label"></td>
      <td class="datum">      
        <bean:message key="text.admin"/>
      </td>      
    </tr>  
    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.locks"/>">
			<bean:message key="label.locks"/>:
		</span>
      </td>
      <td class="datum">
        <bean:write name="DATA" property="lockCount"/>
      </td>      
    </tr>
    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.connections"/>">
			<bean:message key="label.connections"/>:
		</span>
      </td>
      <td class="datum">
        <bean:write name="DATA" property="connectionCount"/>
      </td>      
    </tr>
    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.memory"/>">
			<bean:message key="label.memory"/>:
		</span>
      </td>
      <td class="datum">
        <%= Runtime.getRuntime().freeMemory()/1024 %>K
      </td>      
    </tr>
    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.jvm"/>">
			<bean:message key="label.jvm"/>:
		</span>
      </td>
      <td class="datum">
        <%= System.getProperty("java.vendor") %> : <%= System.getProperty("java.version") %>
      </td>      
    </tr>
    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.jai"/>">
			<bean:message key="label.jai"/>:
		</span>
      </td>
      <td class="datum">
        <%= ClassLoader.getSystemClassLoader().getResource("javax/media/jai/buildVersion") != null %>
      </td>      
    </tr>
  </tbody>
</table>