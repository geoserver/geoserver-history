<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html>
<head></head>

<!-- color chooser -->
<link rel="stylesheet" href="js_color_picker_v2.css" media="screen">
<script type="text/javascript" src="color_functions.js"></script>
<script type="text/javascript" src="js_color_picker_v2.js"></script>

<!-- ================================ -->
<!-- ---------- JAVASCRIPT ---------- -->
<!-- ================================ -->
<script language="JavaScript" type="text/JavaScript">
<!--


/** global variables **/
var SERVERHOSTNAME = "sigma.openplans.org";//window.location.host;

var SERVERURL = "http://"+SERVERHOSTNAME+"/geoserver/";

var geo_xmlhttp = null;	// AJAX-ness


var featureTypeName = "";	// our feature type that the style will go to

var geomType = "";	// the type of geometry we are making a style for

var columnNames;	// the names of the columns of the feature type


/**
 * Setup
 *
 * This will search through the feature type info and determine 
 * what kind of geometry it uses. This is based on geoserver 
 * output and not tested with anything else.
 */
function setup()
{
	log("setup");
	
	// we need an interval to read the jsp results.
	// javascript will load before the jsp code so we need to wait to use it.
	var myInterval = window.setInterval(function cheese(a,b) {
		ft = document.getElementById('span_ftName').innerHTML;
		// if doesn't equal 'undefined'
		log("ft = "+ft);
		if (ft != "undefined")
		{
			featureTypeName = document.getElementById('span_ftName').innerHTML;
			log("interval; featureTypeName = "+featureTypeName);
			ftInfo = document.getElementById('hidden_ft_attrs').innerHTML;
			log("ft info: "+ftInfo);
			columnNames = document.getElementById('hidden_ft_attrNames').innerHTML;

			ftInfoSplit = ftInfo.split(",");
			for(i=0;i<ftInfoSplit.length;i++)
			{
				var g = ftInfoSplit[i].split(":")[1];
				if (g == "multiPointProperty" || g == "pointProperty" ) {
					geomType = "point";
					break;
				}
				else if (g == "multiLineStringProperty" || g == "lineStringProperty" ) {
					geomType = "line";
					break;
				}
				else if (g == "multiPolygonProperty" || g == "polygonProperty" ) {
					geomType = "polygon";
					break;
				}
			}
			log("geomType = "+geomType);


			setScreenEditMenu(geomType); // set the appropriate html for editing

			clearInterval(myInterval); // cancel the interval timer
		}
		else
			log("undefined");
		
	},300);
	
}


function setScreenEditMenu(geomType)
{
	editForm = "";
	if (geomType == "point")
		editForm = generatePointForm();
	else if (geomType == "line")
		editForm = generateLineForm();
	else if (geomType == "polygon")
		editForm = generatePolygonForm();
	
	document.getElementById('mainEditDiv').innerHTML = editForm;
}

function generatePointForm()
{
	result = "";
	result += '<tr><td colspan=4><b><u><font size="+1">Label names for the points:</font></u></b><br></td></tr>';
	result += '<tr><td width=200 colspan=4>Name Field: '+generateNameSelect()+' <i><font color="#92AEFF" size="-1">(This field is the label that will appear on the geometry.)</font></i></td></tr>';

	result += '<tr><td colspan=4><br><hr><b><u><font size="+1">Color of the points:</font></u></b></td></tr>';

	result += '<tr><td width=120 align="right"><b>Color:</b></td><td width=100><input type="text" id="fillColor" size=6 maxlength=6></input></td>';
	result += '<td width=120 align="right"><b>Opacity:</b></td><td width=140><input type="text" id="fillOpacity" size=3 maxlength=3></input> <i><font color="#92AEFF" size="-1">(0.0 - 1.0)</font></i></td></tr>';

	result += '<tr><td colspan=4><br><hr><b><u><font size="+1">Point size and shape:</font></u></b></td></tr>';

	result += '<tr><td width=200 colspan=4>Point Size: <input type="text" id="pointSize" size=6 maxlength=6></input> <i><font color="#92AEFF" size="-1">(The diameter of the point in pixels.)</font></i><br>&nbsp;</td></tr>';
	result += '<tr><td width=200 colspan=4>Point Shape: <select name="pointShape" id="pointShape"> ';
		result += '<option>circle</option>';
		result += '<option>square</option>';
		result += '</select>';
	result += '<i><font color="#92AEFF" size="-1">(The size or diameter of the point in pixels.)</font></i><br>&nbsp;</td></tr>';

	return result;
}

function generateLineForm()
{
	result = "";
	result += '<tr><td colspan=4><b><u><font size="+1">Label names for the lines:</font></u></b><br></td></tr>';
	result += '<tr><td width=200 colspan=4>Name Field: '+generateNameSelect()+' <i><font color="#92AEFF" size="-1">(This field is the label that will appear on the geometry.)</font></i></td></tr>';

	result += '<tr><td colspan=4><br><hr><b><u><font size="+1">Color of the lines:</font></u></b></td></tr>';

	result += '<tr><td width=120 align="right"><b>Color:</b></td><td width=100><input type="text" id="fillColor"size=6 maxlength=6></input></td>';
	result += '<td width=120 align="right"><b>Opacity:</b></td><td width=140><input type="text" id="fillOpacity" size=3 maxlength=3></input> <i><font color="#92AEFF" size="-1">(0.0 - 1.0)</font></i></td></tr>';

	return result;
}

function generatePolygonForm()
{
	result = "";
	result += '<tr><td colspan=4><b><u><font size="+1">Label names for the polygons:</font></u></b><br></td></tr>';
	result += '<tr><td width=200 colspan=4><b>Name Field:</b> '+generateNameSelect()+' <i><font color="#92AEFF" size="-1">(This field is the label that will appear on the geometry.)</font></i><br>&nbsp;</td></tr>';
	result += '<tr><td width=200 colspan=4><b>Text Color:</b> <input  type="text" id="labelColor" size=6 maxlength=6></input> <i><font color="#92AEFF" size="-1">(This is the color of the label.)</font></i></td></tr>';

	result += '<tr><td colspan=4><br><hr><b><u><font size="+1">Color inside the polygon:</font></u></b></td></tr>';

	result += '<tr><td width=120 align="right"><b>Fill Color:</b></td><td width=100><input type="text" id="fillColor"size=6 maxlength=6></input></td>';
	result += '<td width=120 align="right"><b>Fill Opacity:</b></td><td><input type="text" id="fillOpacity" size=3 maxlength=3></input> <i><font color="#92AEFF" size="-1">(0.0 - 1.0)</font></i></td></tr>';
	
	result += '<tr><td colspan=4><br><hr><b><u><font size="+1">Outline color of the polygons:</font></u></b></td></tr>';

	result += '<tr><td width=120 align="right"><b>Outline Color:</b></td><td width=100><input type="text" id="lineColor"size=6 maxlength=6></input></td>';
	result += '<td width=120 align="right"><b>Outline Opacity:</b></td><td><input type="text" id="lineOpacity" size=3 maxlength=3></input> <i><font color="#92AEFF" size="-1">(0.0 - 1.0)</font></i></td></tr>';

	return result;
}

/**
 * Generates the <select> form field for column names based on feature type column names.
 */
function generateNameSelect()
{
	formCode = '<select name="propertyName" id="propertyName">';
	formCode += '<option>none</option>';

	colNamesSplit = columnNames.split(",");
	for(i=0;i<colNamesSplit.length-1;i++)
	{
		if (colNamesSplit[i] != null && colNamesSplit[i] != "")
		{
			formCode += '<option>'+colNamesSplit[i]+'</option>';
		}
	}
	
	formCode += "</select>";

	return formCode;
}


/**
 *
 * This is the main function that reads in the form fields and generates 
 * the SLD.
 *
 */
function generateSLD()
{
	featureTypeName = document.getElementById('span_ftName').innerHTML;
	//alert("featureTypeName: "+featureTypeName);

	SLD = createSLDHeader(featureTypeName);

	SLDcore = ""
	if (geomType == "point")
		SLDcore += generatePointSLD();
	else if (geomType == "line")
		SLDcore += generateLineSLD();
	else if (geomType == "polygon")
		SLDcore += generatePolygonSLD();
	
	matches = SLDCore.match("ERROR:");
	if (matches != null && matches.length > 0)
	{
		alert(SLDCore);	// alert te user with the error and return
		return;
	}

	SLD += SLDcore; // append the code if there were no errors

	SLD += createSLDFooter();

	log("SLD:\n"+SLD);
	saveStyle(SLD); // send it using PutStyles request
}

/**
 * Point SLD generation
 */
function generatePointSLD()
{
	propertyName = document.getElementById('propertyName').value;
	labelColor = document.getElementById('labelColor').value;
	fillColor = document.getElementById('fillColor').value;
	fillOpacity = document.getElementById('fillOpacity').value;
	pointSize = document.getElementById('pointSize').value;
	pointShape = document.getElementById('pointShape').value;

	var font;
	var halo;

	// check values to make sure they are in range and valid
	if (propertyName != "none" && (labelColor == null || labelColor == "")
		return "ERROR: label name specified, but no text color specified.";
	if (fillColor == null || fillColor == "")
		return "ERROR: Point color cannot be empty";
	if (fillOpacity == null || fillOpacity == "")
		return "ERROR: Point opacity cannot be empty";
	if (fillOpacity < 0.0 || fillOpacity > 1.0)
		return "ERROR: Point opacity must be between 0.0 and 1.0";
	if (pointSize == null || pointSize == "")
		return "ERROR: Point size cannot be empty";


	// create stroke
	//stroke = createStroke(lineColor, lineOpacity);
	graphicMark = createGraphic();
	SLD = createSLDPointSymbolizer(graphicMark);

	textFill = createFill(labelColor, 1.0);

	if (propertyName != null && propertyName != "none")
		SLD += createTextSymbolizer(propertyName, font, halo, textFill);

	return SLD;
}


function generatePolygonSLD()
{
	propertyName = document.getElementById('propertyName').value;
	labelColor = document.getElementById('labelColor').value;
	fillColor = document.getElementById('fillColor').value;
	fillOpacity = document.getElementById('fillOpacity').value;
	lineColor = document.getElementById('lineColor').value;
	lineOpacity = document.getElementById('lineOpacity').value;
	
	var font;
	var halo;

	// check values to make sure they are in range and valid
	if (propertyName != "none" && (labelColor == null || labelColor == "")
		return "ERROR: label name specified, but no text color specified.";
	if (fillColor == null || fillColor == "")
		return "ERROR: Polygon fill color cannot be empty";
	if (fillOpacity == null || fillOpacity == "")
		return "ERROR: Polygon color opacity cannot be empty";
	if (fillOpacity < 0.0 || fillOpacity > 1.0)
		return "ERROR: Polygon fill opacity must be between 0.0 and 1.0";
	if (lineColor == null || lineColor == "")
		return "ERROR: Polygon outline color cannot be empty";
	if (lineOpacity == null || lineOpacity == "")
		return "ERROR: Polygon outline opacity cannot be empty";
	if (lineOpacity < 0.0 || lineOpacity > 1.0)
		return "ERROR: Polygon outline opacity must be between 0.0 and 1.0";

	// create fill
	polygonFill = createFill(fillColor, fillOpacity);

	// create stroke
	stroke = createStroke(lineColor, lineOpacity);

	SLD = createSLDPolygonSymbolizer(polygonFill, stroke);

	textFill = createFill(labelColor, 1.0);

	if (propertyName != null && propertyName != "none")
		SLD += createTextSymbolizer(propertyName, font, halo, textFill);

	return SLD;
}

/**
 * Perform the putStyles request
 */
function saveStyle(SLD)
{
	log("Saving style");
	//kill any current requests!
	if (geo_xmlhttp !=null)
	{
		geo_xmlhttp.abort();
		geo_xmlhttp = null;
	}

	// build XML POST query
	URL  = "/geoserver/sld";//"http://"+SERVERHOSTNAME+"/

	getXML(URL,SLD,XMLProgressFunction);
	
}

function createSLDHeader(featureType)
{
	//log("Making sld for: "+featureType);
	XML  = '<?xml version="1.0" encoding="UTF-8"?>'+"\n";
	XML += '<StyledLayerDescriptor version="1.0.0"'+"\n";
	XML += '	xmlns:gml="http://www.opengis.net/gml"'+"\n";
	XML += '	xmlns:ogc="http://www.opengis.net/ogc"'+"\n";
	XML += '	xmlns="http://www.opengis.net/sld">'+"\n";
	XML += '	<NamedLayer>'+"\n";
	XML += '		<Name>'+featureType+'</Name>'+"\n";
	XML += '		<UserStyle>'+"\n";
	XML += '			<Name>'+featureType+'_style</Name>'+"\n";
	XML += '			<Title>poly_landmarks_style in the United States</Title>'+"\n";
	XML += '			<Abstract>Generated</Abstract>'+"\n";
	XML += '			<FeatureTypeStyle>'+"\n";
	XML += '			<Rule>'+"\n";
	
	return XML;
}


function createSLDFooter()
{
	XML   = '			</Rule>'+"\n";
	XML  += '			</FeatureTypeStyle>'+"\n";
	XML  += '		</UserStyle>'+"\n";
	XML  += '	</NamedLayer>'+"\n";
	XML  += '</StyledLayerDescriptor>';

	return XML;
}

function createSLDPointSymbolizer(graphic)
{
	XML  = '				<PointSymbolier>'+"\n";
	XML += '					'+graphic+"\n";
	XML += '				</PointSymbolier>'+"\n";

	return XML;

}

function createSLDLineSymbolizer(stroke)
{
	XML  = '				<LineSymbolizer>'+"\n";
	XML += stroke;
	XML += '				</LineSymbolizer>'+"\n";

	return XML;
}

function createSLDPolygonSymbolizer(fill, stroke)
{
	XML  = '				<PolygonSymbolizer>'+"\n";
	XML += fill;
	XML += stroke;
	XML += '				</PolygonSymbolizer>'+"\n";

	return XML;
}

function createFill(color, opacity)
{
	// add # to front of 'color'
	if(color.charAt(0) != "#")
		color = "#"+color;

	XML  = '					<Fill>'+"\n";
	XML += '						<CssParameter name="fill">'+color+'</CssParameter>'+"\n";
	XML += '						<CssParameter name="fill-opacity">'+opacity+'</CssParameter>'+"\n";
	XML += '					</Fill>'+"\n";

	return XML;
}

function createStroke(color, opacity, width, linecap, linejoin, dasharray)
{
	// add # to front of 'color'
	if(color.charAt(0) != "#")
		color = "#"+color;

	XML  = '					<Stroke>'+"\n";
	if(color)	XML += '						<CssParameter name="stroke">'+color+'</CssParameter>'+"\n";
	if(opacity)	XML += '						<CssParameter name="stroke-opacity">'+opacity+'</CssParameter>'+"\n";
	if(width)	XML += '						<CssParameter name="stroke-width">'+width+'</CssParameter>'+"\n";
	if(linecap)	XML += '						<CssParameter name="stroke-linecap">'+linecap+'</CssParameter>'+"\n";
	if(linejoin)XML += '						<CssParameter name="stroke-linejoin">'+linejoin+'</CssParameter>'+"\n";
	if(dasharray)XML += '						<CssParameter name="stroke-dasharray">'+dasharray+'</CssParameter>'+"\n";
	XML += '					</Stroke>'+"\n";

	return XML;
}

// a note to figure out all possible line caps
function getLineCaps()
{

}

// a note to figure out all possible line joins
function getLineJoins()
{

}

function createGraphic(shape, fill, stroke, size, opacity)
{
	XML = '<Graphic>'+"\n";
	XML += '	<Mark>'+"\n";
	XML += '		<WellKnownName>'+shape+'</WellKnownName>'+"\n";
	XML += fill;
	if(stroke) XML += stroke;
	XML += '	</Mark>'+"\n";
	XML += '	<Opacity>'+opacity+'</Opacity>'+"\n";
	XML += '	<Size>'+size+'</Size>'+"\n";
	XML += '</Graphic>'+"\n";

	return XML;
}


function createTextSymbolizer(columnName, font, halo, fill)
{
	XML  = '				<TextSymbolizer>'+"\n";
	XML += '					<Label>'+"\n";
	XML += '						<ogc:PropertyName>'+columnName+'</ogc:PropertyName>'+"\n";
	XML += '					</Label>'+"\n";
	if(font) XML += font;
	if(halo) XML += halo;
	if(fill) XML += fill;
	XML += '				</TextSymbolizer>'+"\n";


	return XML;
}

function createFont(name, style, size, weight)
{
	XML  = '						<Font>'+"\n";
	XML += '							<CssParameter name="font-family">'+name+'</CssParameter>'+"\n";
	XML += '							<CssParameter name="font-style">'+style+'</CssParameter>'+"\n";
	XML += '							<CssParameter name="font-size">'+size+'</CssParameter>'+"\n";
	XML += '							<CssParameter name="font-weight">'+weight+'</CssParameter>'+"\n";
	XML += '						<Font>'+"\n";

	return XML;
}

function createHalo(radius, fill)
{
	XML  = '						<Halo>'+"\n";
	XML += '							<Radius>'+"\n";
	XML += '								<ogc:Literal>'+radius+'</ogc:Literal>'+"\n";
	XML += '							<Radius>'+"\n";
	XML += fill;
	XML += '						<Halo>'+"\n";

	return XML;
}


function createMinScaleDenominator(scale)
{
	XML = '<MinScaleDenominator>'+scale+'</MinScaleDenominator>'+"\n";

	return XML;
}

function createMaxScaleDenominator(scale)
{
	XML = '<MaxScaleDenominator>'+scale+'</MaxScaleDenominator>'+"\n";

	return XML;
}


/**
 * Send the XML request to GeoServer. Hands off control to procfunction method passed in.
 */
function getXML(url,post,procfunction)
{
	try {

		if (window.ActiveXObject)
		{
			// IE
			log("getXML through IE");
			geo_xmlhttp =  new ActiveXObject("Microsoft.XMLHTTP");
			geo_xmlhttp.onreadystatechange = procfunction;
			geo_xmlhttp.open("POST", url, true);
			geo_xmlhttp.setRequestHeader('Content-Type', 'text/xml');  //correct request type
			geo_xmlhttp.setRequestHeader('Cache-Control', 'no-cache');	// don't cache the requests!!!
			//geo_xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			geo_xmlhttp.send(post);
		}
		else if (window.XMLHttpRequest)
		{
			// Mozilla and others
			//log("getXML through Mozilla etc.");
			geo_xmlhttp =  new XMLHttpRequest();
			//geo_xmlhttp.overrideMimeType('text/xml');	// <- bad? caused some serious problems at one point
			geo_xmlhttp.onreadystatechange = procfunction;
			geo_xmlhttp.open("POST", url, true);
			geo_xmlhttp.setRequestHeader('Content-Type', 'text/xml');	//correct request type
			geo_xmlhttp.setRequestHeader('Cache-Control', 'no-cache');	// don't cache the requests!!!
			geo_xmlhttp.send(post);
		}
		else
			log("Invalid browser format: not expecting this kind of browser.");
	 }
	 catch(e)
	 {
		alert(e);
		alert("If you just got a security exception, its because you need to serve the .html file from the same server as where you're sending the XML requests to!");
	 }
}



/**
 * Waits for requests and handles them.
 */
function XMLProgressFunction()
{
	if (geo_xmlhttp != null)
	{	// add 'working' animation
	}

	if ( (geo_xmlhttp.readyState == 4) && (geo_xmlhttp.status == 200) )
	{
		log("XML reponse received");
		//we got a good response.  We need to process it!
		if (geo_xmlhttp.responseXML == null)
		{
			log("XMLProgressFunction(): abort 1");
			//document.getElementById('working_anim_gnis_span').innerHTML = '';	// remove 'working' animation
			return;
		}

		log("response:\n"+geo_xmlhttp.responseText);
/*
		fc_node = getElements(geo_xmlhttp.responseXML,"wfs","FeatureCollection")[0];
		if (fc_node == null)
		{
			log("XMLProgressFunction(): abort 2");
			//document.getElementById('working_anim_gnis_span').innerHTML = '';	// remove 'working' animation
			return;
		}

		fm_nodes = getElements(fc_node,"gml","featureMember");
		if (fm_nodes == null)
		{
			log("XMLProgressFunction(): abort 3");
			document.getElementById('working_anim_gnis_span').innerHTML = '';	// remove 'working' animation
			return;
		}

		if (fm_nodes.length == 0)	// Nothing found (0 results)
		{
			log("XMLProgressFunction(): No results found");
			document.getElementById('working_anim_gnis_span').innerHTML = '';	// remove 'working' animation
			document.getElementById('search_result_gnis_span').innerHTML = writeSearchResultsHeader()+' <b>'+fm_nodes.length+'</b> results. <font size=-2></font><br><table border=1 bordercolor="#7CCE4A"><tr><td><div style="overflow:auto; height:150px; width:430px;"><table cellpadding="0" cellspacing="0" style="width:410px;"><tr><td>No results matched your query.</td></tr></table></div></td></tr></table>';
			return;
		}

		queryEndTime = new Date();
		queryEndTime = queryEndTime.getTime();
		duration = (queryEndTime - queryStartTime)/1000;

		log("found "+fm_nodes.length+" nodes in "+duration+" seconds.");

		values = parseGnisNodes(fm_nodes);
		document.getElementById('search_result_gnis_span').innerHTML = writeSearchResultsHeader()+' <b>'+fm_nodes.length+'</b> results. <font size=-2><i>Search took '+duration+' seconds.</i></font><br><table border=1 bordercolor="#7CCE4A"><tr><td><div style="overflow:auto; height:150px; width:430px;"><table cellpadding="0" cellspacing="0" style="width:410px;"><tr><td>'+values+'</td></tr></table></div></td></tr></table>';

		document.getElementById('working_anim_gnis_span').innerHTML = '';	// remove 'working' animation
		*/
	}
	else
		log("waiting for response...");
}


/**
 * Log to screen
 */
function log(text)
{
	IFrameElement = document.getElementsByName("logFrame")[0];

	//get doc
	if (IFrameElement.contentDocument)
	{
		IFrameDoc = IFrameElement.contentDocument;
	}
	else if (IFrameElement.contentWindow)
	{
		IFrameDoc = IFrameElement.contentWindow.document;
	}
	else if (IFrameElement.document)
	{
		IFrameDoc = IFrameElement.document;
	}
	else
	{
		return true;
	}


     //put a <div><pre></pre></div>
	if (IFrameDoc.body.getElementsByTagName("div")[0] ==null)
	{

 	   divNode = IFrameDoc.createElement("div");
 	   preNode = IFrameDoc.createElement("pre");
 	   preNode.appendChild(IFrameDoc.createTextNode(""));
 	   divNode.appendChild(preNode);

       IFrameDoc.body.appendChild(divNode);


       //alert(IFrameDoc.body.getElementsByTagName("div")[0] );
	}


	// add the text
	//<div><pre>  text<br>  </pre></div>
	preNode = IFrameDoc.body.getElementsByTagName("pre")[0];

	textNode = IFrameDoc.createTextNode(text) ;

	preNode.appendChild(  textNode  );
	preNode.appendChild(  IFrameDoc.createElement("br")  );
	//  textNode.scrollIntoView(true);
}

/**
 * For killing form 'action' functionality. Cheap hack, I'm sure there are better ways to do it.
 * Also for killing link tags.
 */
function nothing()
{
}


-->
</script>
<!-- ================================ -->
<!-- --- END -- JAVASCRIPT ---------- -->
<!-- ================================ -->




<body onload="setup()" text="#AAC0FF">

<font color="#08809F" size="+2"><b>Create new SLD for FeatureType: &nbsp;<i><span id="span_ftName" name="span_ftName"><bean:write property="<%= "typeName" %>" name="typesEditorForm"/></span></font></i></b>
<br>&nbsp;<br>

<span id="hidden_ft_attrs" name="hidden_ft_attrs" style="display:none"><bean:write property="<%= "attributes" %>" name="typesEditorForm" /></span>

<span id="hidden_ft_attrNames" name="hidden_ft_attrNames" style="display:none">
<logic:iterate id="attribute" indexId="index" name="typesEditorForm" property="attributes">
	<bean:write name="attribute" property="name"/>,
</logic:iterate>
</span>


<!--bean:write property="<%= "attributes" %>" name="typesEditorForm" /-->
<form action="javascript:nothing()">
	<table width=550 bgcolor="#7C8DBF">
		<tr><td>
			<div id="mainEditDiv" name="mainEditDiv">
			</div>
		</td></tr>
	</table>
	<br>&nbsp;<br>
	
	<input type="submit" value="Apply Style" onclick="generateSLD()">
</form>


<i>You must apply the style before it will be saved.<br>
Hit the 'Apply Style' button above'.</i>
<!-- finished button "back to FeatureType editor" -->
<html:form action="/config/data/typeSelectSubmit">
	<html:submit property="action">
		<bean:message key="label.edit"/>
	</html:submit>
</html:form>

<p>&nbsp;<br>
<iframe width=90% height=150px name=logFrame></iframe>
<br>


</body>

</html>