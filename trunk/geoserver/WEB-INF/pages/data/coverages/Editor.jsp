<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<%
//This file contains some ugly JSP code.
//Be wary, ye who brave the dragon's lair.
%>

<% try { %>
<html:form action="/config/data/coverageEditorSubmit">
  <table class="info">
	<tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.coverage.name"/>">
          <bean:message key="label.name"/>:
        </span>
      </td>
      <td class="datum">
		<bean:write name="coveragesEditorForm" property="name"/>
		<html:hidden property="name"/>
		<html:hidden property="newCoverage" value="false"/>
      </td>
    </tr>
	<tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.wms.path"/>">
        <bean:message key="label.wms.path"/>:
      </span>
	  </td>
	  <td class="datum">
		<html:text property="wmsPath" size="60"/>
	  </td>
	</tr>    
	<tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.type.style"/>">
          <bean:message key="label.style"/>:
        </span>
	  </td>
	  <td class="datum">
      	<html:select property="styleId">
        	<html:options property="styles"/>
		</html:select>
	  </td>
	</tr>    
	<tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.coverage.srsName"/>">
          <bean:message key="label.SRS"/>:
        </span>
      </td>
	  <td class="datum">
		<html:text property="srsName" size="60"/>
	</td></tr>
	<!------------------------->
	<!------ This puts in the SRS WKT definition --->
	
	<tr>
	<td class="label">
		<span class="help" title="<bean:message key="help.type.srswkt"/>">
          <bean:message key="label.type.srswkt"/>:
        </span>
	  </td>
	  <td class="greyedOut2">
              <bean:write name="coveragesEditorForm" property="WKTString"/>
			  <html:hidden property="WKTString"/>
            </td>
	</tr>
	
	
	<!-------------------------->

    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.coverage.label"/>">
          <bean:message key="label.coverageLabel"/>:
        </span>
	  </td>
	  <td class="datum">
		<html:text property="label" size="60"/>
	  </td>
	</tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.description"/>">
        <bean:message key="label.copverageDescription"/>:
      </span>
	  </td>
	  <td class="datum">
		<html:text property="description" size="60"/>
	  </td>
	</tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.metadataLink"/>">
        <bean:message key="label.metadataLink"/>:
      </span>
	  </td>
	  <td class="datum">
		<html:text property="metadataLink" size="60"/>
	  </td>
	</tr>

    <tr>
      <td class="label">
		<span class="help" title="<bean:message key="help.coverage.envelope"/>">
          <bean:message key="label.envelope"/>:          
        </span>
	  </td>
	  <td class="datum">
        <html:submit property="action">
          <bean:message key="config.data.calculateBoundingBox.label"/>
        </html:submit><br/>
        <table border=0>
          <tr>
            <td style="white-space: nowrap;">
              <span class="help" title="<bean:message key="help.coverage.minx"/>">
                <bean:message key="label.coverage.minx"/>:
              </span>
            </td>
            <td>
              <html:text property="minX" size="15"/>
            </td>
            <td style="white-space: nowrap;">
              <span class="help" title="<bean:message key="help.coverage.miny"/>">
                <bean:message key="label.coverage.miny"/>:
              </span>
            </td>
            <td>
              <html:text property="minY" size="15"/>
            </td>
          </tr>
          <tr>
            <td style="white-space: nowrap;">
              <span class="help" title="<bean:message key="help.coverage.maxx"/>">
                <bean:message key="label.coverage.maxx"/>:
              </span>
            </td>
            <td>
              <html:text property="maxX" size="15"/>
            </td>
            <td style="white-space: nowrap;">
              <span class="help" title="<bean:message key="help.coverage.maxy"/>">
                <bean:message key="label.coverage.maxy"/>:
              </span>
            </td>
            <td>
              <html:text property="maxY" size="15"/>
            </td>
          </tr>
        </table>
	  </td>
    </tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.dataCoverageKeywords"/>">
			<bean:message key="label.keywords"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="keywords" cols="60" rows="2"/>
	  </td>
    </tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.requestCRSs"/>">
			<bean:message key="label.requestCRSs"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="requestCRSs" cols="60" rows="2"/>
	  </td>
    </tr>
    
    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.responseCRSs"/>">
			<bean:message key="label.responseCRSs"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="responseCRSs" cols="60" rows="2"/>
	  </td>
    </tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.nativeFormat"/>">
        <bean:message key="label.nativeFormat"/>:
      </span>
	  </td>
	  <td class="datum">
		<bean:write name="coveragesEditorForm" property="nativeFormat"/>
		<html:hidden property="nativeFormat"/>
	  </td>
	</tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.supportedFormats"/>">
			<bean:message key="label.supportedFormats"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="supportedFormats" cols="60" rows="2"/>
	  </td>
    </tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.defaultInterpolationMethod"/>">
        <bean:message key="label.defaultInterpolationMethod"/>:
      </span>
	  </td>
	  <td class="datum">
		<bean:write name="coveragesEditorForm" property="defaultInterpolationMethod"/>
		<html:hidden property="defaultInterpolationMethod"/>
	  </td>
	</tr>

    <tr>
    <td class="label">
		<span class="help" title="<bean:message key="help.coverage.interpolationMethods"/>">
			<bean:message key="label.interpolationMethods"/>:
		</span>
	  </td>
	  <td class="datum">
		<html:textarea property="interpolationMethods" cols="60" rows="2" readonly="true"/>
	  </td>
    </tr>


    <logic:notEmpty name="coveragesEditorForm"
                   property="paramKeys">
    <logic:iterate id="param"
                   indexId="ctr"
                   name="coveragesEditorForm"
                   property="paramKeys">
    <logic:notEqual name="coveragesEditorForm"
                    property='<%= "paramKey[" + ctr + "]"%>'
                    value="dbtype">
        <tr>
    	  <td class="label">
            <span class="help"
    		      title="<bean:write name="coveragesEditorForm"
    		      property='<%= "paramHelp[" + ctr + "]" %>'/>">
              <bean:write name="coveragesEditorForm"
                          property='<%= "paramKey[" + ctr + "]"%>'/>:
    		</span>
          </td>
    	  <td class="datum">
    <logic:notEqual name="coveragesEditorForm"
    	    	        property='<%= "paramKey[" + ctr + "]"%>'
    			        value="passwd">
              <html:text property='<%= "paramValue[" + ctr + "]"%>' size="60"/>
    </logic:notEqual>
    <logic:equal name="coveragesEditorForm"
       		     property='<%= "paramKey[" + ctr + "]"%>'
                 value="passwd">
              <html:password property='<%= "paramValue[" + ctr + "]"%>' size="12"/>
    </logic:equal>			             
    	  </td>
    	</tr>
    </logic:notEqual>
    </logic:iterate>
    </logic:notEmpty>

    <tr>
      <td class="label">
        &nbsp;
      </td>
	  <td class="datum">

		<html:submit property="action">
			<bean:message key="label.submit"/>
		</html:submit>
		
		<html:reset>
			<bean:message key="label.reset"/>
		</html:reset>

	  </td>
    </tr>
  </table>
</html:form>

<% } catch (Throwable hate ){
   System.err.println( "Coverage Editor problem:"+ hate );
   hate.printStackTrace();
   throw hate;
} %>