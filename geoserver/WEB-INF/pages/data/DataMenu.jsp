<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<span class="dataMenu">

<html:link forward="/config/data/stores"><bean:message key="label.dataStores"/></html:link><br>
<html:link forward="/config/data/namespaces"><bean:message key="label.namespaces"/></html:link><br>
<html:link forward="/config/data/styles"><bean:message key="label.styles"/></html:link><br>
<html:link forward="/config/data/featureTypes"><bean:message key="label.featureTypes"/></html:link><br>

</span>