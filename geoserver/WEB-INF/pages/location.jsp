<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<span class="locator">
  <html:link forward="MainMenu">
    Home
  </html:link> |
  <html:link forward="geoServerConfiguration">
    Service
  </html:link> |
  <html:link forward="dataMenu">
    Data
  </html:link> |
  <html:link forward="dataConfigFeatureTypeSelect">
    FeatureType
  </html:link> |
  Editor
</span>