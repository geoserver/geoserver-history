<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<html:html locale="true">

<bean:define id="titleKey">
	<tiles:getAsString name='title'/>
</bean:define>

<head>
<title><bean:message key="<%= titleKey %>"/></title>
<html:base/>
</head>

<body bgcolor="white">
<table class="main">
	<tr>
		<td>
			<tiles:insert attribute="logo"/>
		</td>
		<td>
			<tiles:insert attribute="serviceName"/>
		</td>	
		<td>
			<tiles:insert attribute="contact"/>
        </td>
	</tr>
	<tr>
		<td class="sidebar">
			<table class="sidebar">
				<tr>
					<td>
						<tiles:insert attribute="status"/>
					</td>
				</tr>
				<tr>
					<td>
						<tiles:insert attribute="buttons"/>
					</td>
				</tr>
				<tr>
					<td>
						<tiles:insert attribute="actions"/>
					</td>
				</tr>
				<tr>
					<td>
						<tiles:insert attribute="messages"/>
					</td>
				</tr>
			</table>
		</td>
		<td>
			<table class="body">
				<tbody>	
				<tr>
            		<td class="location">
            			<span class="location">
            				Configuration | Data | FeatureType | Editor
            			</span><br>
            		</td>
            		<td class="login">
            			<span class="login">login</span><br>
            		</td>
          		</tr>
          		<tr>
            		<td>
            			<table class="body_main">
				            <tbody>
                			<tr>
	                  			<td class="property_label">
	                  				SRS:<br>
				                </td>
	            			    <td class="property_control"><br>
			                    </td>
          			        </tr>
			                <tr>
            				    <td class="property_label">
            				    	<span class="error">
            				    		*Title:
            				    	</span><br>
				                </td>
                				<td class="property_control"><br>
				                </td>
                			</tr>
				            </tbody>
			            </table>
            		</td>
	          </tr>
    		  </tbody>
	      </table>
	      <tiles:insert attribute="body"/>
		</td>
	</tr>
</table>

</body>
</html:html>