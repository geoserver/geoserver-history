<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<span class="dataMenu">

<html:link forward="config.data.store"><bean:message key="label.dataStores"/></html:link><br>
<html:link forward="config.data.namespace"><bean:message key="label.namespaces"/></html:link><br>
<html:link forward="config.data.style"><bean:message key="label.styles"/></html:link><br>
<html:link forward="config.data.type"><bean:message key="label.featureTypes"/></html:link><br>

</span>