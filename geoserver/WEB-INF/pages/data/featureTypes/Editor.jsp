<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:form action="/config/data/featureTypeSubmit">
  <table class="info">
	<tr>
      <td class="label">
        <bean:message key="label.name"/>:
      </td>
      <td class="datum">
		<bean:write name="dataFeatureTypesEditorForm" property="name"/>
      </td>
    </tr>
	<tr>
      <td class="label">
		<bean:message key="label.SRS"/>:
      </td>
	  <td class="datum">
		<html:text property="SRS" size="60"/>
	</td></tr>

    <tr>
      <td class="label">
		<bean:message key="label.title"/>:
	  </td>
	  <td class="datum">
		<html:text property="title" size="60"/>
	</td></tr>

    <tr>
      <td class="label">
		<bean:message key="label.latLonBoundingBox"/>:
	  </td>
	  <td class="datum">
		<html:text property="latLonBoundingBoxMinX" size="30"/>
		<html:text property="latLonBoundingBoxMinY" size="30"/>
        <br/>
		<html:text property="latLonBoundingBoxMaxX" size="30"/>
		<html:text property="latLonBoundingBoxMaxY" size="30"/>
	  </td>
    </tr>

    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.dataFeatureTypeKeywords"/>">
			<bean:message key="label.keywords"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="keywords" cols="60" rows="2"/>
	  </td>
    </tr>

    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.dataFeatureTypeAbstract"/>">
			<bean:message key="label.abstract"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="_abstract" cols="60" rows="3"/>
      </td>
    </tr>
	<tr>
      <td class="label">
		<bean:message key="label.default"/>:
	  </td>
	  <td class="datum">
		<html:checkbox property="_default"/>
	  </td>
    </tr>

    <tr>
      <td class="label">
        &nbsp;
      </td>
	  <td class="datum">

		<html:submit>
			<bean:message key="label.submit"/>
		</html:submit>
		
		<html:reset>
			<bean:message key="label.reset"/>
		</html:reset>

	  </td>
    </tr>
  </table>
</html:form>