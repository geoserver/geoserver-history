<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<span class="dataMenu">

<html:link forward="dataConfigDataStores"><bean:message key="label.dataStores"/></html:link><br>
<html:link forward="dataConfigNamespaces"><bean:message key="label.namespaces"/></html:link><br>
<html:link forward="dataConfigStyles"><bean:message key="label.styles"/></html:link><br>
<html:link forward="dataConfigFeatureTypes"><bean:message key="label.featureTypes"/></html:link><br>

</span>