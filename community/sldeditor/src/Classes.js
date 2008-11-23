// finish adding all classes
// add Validation methods
// add utility methods
// figure out when to fill in the data and go up the hierarchy

// StyledLayerDescriptor Class 
function StyledLayerDescriptor() {
    // <Name>
    // optional
    // string: Name of the SLD
    this.name;
    
    // <Title>
    // optional
    // string: Title of the SLD
    this.title;
    
    // <Abstract>
    // optional
    // string: Description of the SLD
    this.abs;
    
    // <NamedLayer>
    // optional
    this.namedLayer;
    
    //<UserLayer>
    // optional
    this.userLayer;
}
StyledLayerDescriptor.prototype.toString = function() {
    var str = this.name.toString() + this.title.toString() + this.abs.toString() + this.namedLayer.toString() + this.userLayer.toString();
    if (str.length != 0)
	// what encoding? UTF-8, ISO-8859-1
	return '<?xml version="1.0" encoding="UTF-8"?>\n<StyledLayerDescriptor version="1.0.0" xsi:schemalocation="http://www.opengis.net/sld/StyledLayerDescriptor.xsd" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n' + str + '</StyledLayerDescriptor>\n';
    else
	return '';
}
    
// Name Class
    function Name(name) {
	this.value = name.toString();
    }
Name.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Name>\n' + this.value + '\n</Name>\n';
    else
	return '';
}
    
// Title Class
    function Title(title) {
	this.value = title.toString();
    }
Title.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Title>\n' + this.value + '\n</Title>\n';
    else
	return '';
}
    
// Abstract Class
    function Abs(abs) {
	this.value = abs.toString();
    }
Abs.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Abstract>\n' + this.value + '\n</Abstract>\n';
    else
	return '';
}

// Value Class
    function Value(value) {
	this.value = value.toString();
    }
Value.prototype.toString = function() {
    if (this.value.length != 0)
	return '<Value>\n' + this.value + '\n</Value>\n';
    else
	return '';
}

// NamedLayer Class
    function NamedLayer() {
	// <Name>
	// optional
	// string: name of the NamedLayer
	this.name;
	
	// <LayerFeatureConstraints>
	// optional
	this.layerFeatureConstraints;
	
	// <NamedStyle>
	// optional
	// Specifies the name of a predefined style
	this.namedStyle;
	
	// <UserStyle>
	// optional
	// Specifies a user-defined style
	this.userStyle;
	
    }
NamedLayer.prototype.toString = function() {
    var str = this.name.toString() + this.layerFeatureConstraints.toString() + this.namedStyle.toString() + this.userStyle.toString();
    if (str.length != 0) 
	return '<NamedLayer>\n' + str + '</NamedLayer>\n';
    else 
	return '';
}

// UserLayer Class
    function UserLayer() {
	// <Name>
	// optional
	// name of the UserLayer
	this.name;
	
	// <RemoteOWS>
	// optional
	// Specifies an external feature/coverage server
	this.remoteOWS;

	// <LayerFeatureConstraints>
	// required
	this.layerFeatureConstraints;
	
	// <UserStyle>
	// multiple
	this.userStyle
    }
UserLayer.prototype.toString = function() {
    var str = this.name.toString() + this.remoteOWS.toString() + this.layerFeatureConstraints.toString() + userStyle.toString();
    if (str.length != 0)
	return '<UserLayer>\n' + str + '</UserLayer>\n';
    else 
	return '';
}
    
// RemoteOWS Class
    function RemoteOWS() {
	// <Service>
	// required
	// WFS or WCS
	this.service;

	// <OnlineResource>
	// required
	// xlink URI
	this.onlineResource;
    }
RemoteOWS.prototype.toString = function() {
    var str = this.service.toString() + this.onlineResource.toString();
    if (str.length != 0) 
	return '<RemoteOWS>\n' + str + '</RemoteOWS>\n';
    else
	return '';
}

// Service Class
    function Service(service) {
	// should be WFS or WCS
	this.value = service.toString();
    }
Service.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Service>\n' + this.value + '\n</Service>\n';
    else
	return '';
}

// LayerFeatureConstraings Class
    function LayerFeatureConstraints() {
	// <FeatureTypeConstraint>
	// multiple
	// TODO: How to implement multiple instances of the same element?
	this.FeatureTypeConstraint;
    }
LayerFeatureConstraints.prototype.toString = function() {
    // ??? TODO
}
    
// FeatureTypeConstraint Class
    function FeatureTypeConstraint() {
	// <FeatureTypeName>
	// optional
	// name of the featuretype
	this.featureTypeName;
	
	// <Filter>
	// optional
	// ogc:Filter
	// no idea how to deal with this yet
	this.filter;
	
	// <Extent>
	// multiple, optional
	this.extent;
    }
FeatureTypeConstraint.prototype.toString = function() {
    var str = this.featureTypeName.toString() + this.filter.toString() + this.extent.toString();
    if (str.length != 0)
	return '<FeatureTypeConstraint>\n' + str + '</FeatureTypeConstraint>\n';
    else
	return '';
}

// FeatureTypeName Class
    function FeatureTypeName(name) {
	this.value = name.toString();
    }
FeatureTypeName.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<FeatureTypeName>\n' + this.value + '\n</FeatureTypeName>\n';
    else
	return '';
}

// ogc:Filter Class
// ??? TODO

// ElseFilter Class
    function ElseFilter() {
	// doesn't need to do anything
    }
ElseFilter.prototype.toString = function() {
    return '\n<ElseFilter/>\n';
}

// ogc:Expression Class
// ??? TODO

// Extent Class
    function Extent() {
	// <Name>
	// required
	// name of the extent
	this.name;
	
	// <Value>
	// required
	// string data value
	this.value;
    }
Extent.prototype.toString = function () {
    var str = this.name.toString() + this.value.toString();
    if (str.length != 0) 
	return '<Extent>\n' + str + '</Extent>\n';
    else
	return '';
}

// NamedStyle Class
    function NamedStyle() {
	// <Name>
	// required
	// name of the style
	this.name;
    }
NamedStyle.prototype.toString = function () {
    var str = this.name.toString();
    if (str.length != 0)
	return '<NamedStyle>\n' + str + '</NamedStyle>\n';
    else 
	return '';
}

// UserStyle Class
    function UserStyle() {
	// <Name>
	// optional
	// name of the UserStyle
	this.name;
	
	// <Title>
	// optional
	// title of the UserStyle
	this.title;
	
	// <Abstract>
	// optional
	// description of the UserStyle
	this.abs;
	
	// <IsDefault>
	// optional
	// is this the default style for the layer?
	this.isDefault;
	
	// <FeatureTypeStyle>
	// multiple
	this.featureTypeStyle;
    }
UserStyle.prototype.toString = function () {
    var str = this.name.toString() + this.title.toString() + this.abs.toString() + this.isDefault.toString() + this.featureTypeStyle.toString();
    if (str.length != 0)
	return '<UserStyle>\n' + str + '</UserStyle>\n';
    else
	return '';
}

// IsDefault Class
    function IsDefault(value) {
	// should be 1 or 0
	this.value = value.toString();
    }
IsDefault.prototype.toString = function () {
    if (this.value.length != 0)
	return '<IsDefault>\n' + this.value + '\n</IsDefault>\n';
    else
	return '';
}

// FeatureTypeStyle Class
    function FeatureTypeStyle() {
	// <Name>
	// optional
	this.name;

	// <Title>
	// optional
	this.title;

	// <Abstract>
	// optional
	this.abs;

	// <FeatureTypeName>
	// optional
	this.featureTypeName;

	// SemanticTypeIdentifier
	// optional, multiple
	this.semanticTypeIdentifier;

	// <Rule>
	// multiple
	this.rule;
    }
FeatureTypeStyle.prototype.toString = function () {
    var str = this.name.toString() + this.title.toString() + this.abs.toString() + this.featureTypeName.toString() + this.semanticTypeIdentifier.toString() + this.rule.toString();
    if (str.length != 0)
	return '<FeatureTypeStyle>\n' + str + '</FeatureTypeStyle>\n';
    else
	return '';
}

// SemanticTypeIdentifier Class
    function SemanticTypeIdentifier(value) {
	// undefined string
	// use these reserved strings to indicate that a FeatureTypeStyle may be used
	// with any feature type with the corresponding default geometry type
	// i.e. no feature properties are referenced in the FeatureTypeStyle
	// generic:line
	// generic:polygon
	// generic:point
	// generic:text
	// generic:raster
	// generic:any
	this.value = value.toString;
    }
SemanticTypeIdentifier.prototype.toString = function () {
    if (this.value.length != 0)
	return '<SemanticTypeIdentifier>\n' + this.value + '\n</SemanticTypeIdentifier>\n';
    else
	return '';
}

// Rule Class
    function Rule() {
	// <Name>
	// optional
	// name of the rule
	this.name;
	
	// <Title>
	// optional
	// title of the rule
	this.title;
	
	// <Abstract>
	// optional
	// description of the rule
	this.abs;
	
	// <LegendGraphic>
	// optional
	// defines a graphic symbol to be displayed in the legend for this rule
	this.legendGrahpic;
	
	// choice: may only have 1 filter OR 1 ElseFilter
	// <Filter>
	// optional
	this.filter;
	
	// <ElseFilter>
	// optional
	this.elseFilter;
	
	// <MinScaleDenominator>
	// optional
	// defines the minimum map-rendering scale for which the rule should be applied
	this.minScaleDenominator;
	
	// <MaxScaleDenominator>
	// optional
	// defines the maximum map-rendering scale for which the rule should be applied
	this.maxScaleDenominator;
	
	// <LineSymbolizer>
	// optional
	// used to style a stroke along a linear geometry type
	this.lineSymbolizer;
	
	// <PolygonSymbolizer>
	// optional
	// used to draw a polygon or other area-type geometry
	// including filling its interior and stroking its border (outline)
	this.polygonSymbolizer;
	
	// <PointSymbolizer>
	// optional
	// used to draw a graphic at a point
	this.pointSymbolizer;
	
	// <TextSymbolizer>
	// optional
	// used to style text labels
	this.textSymbolizer;
	
	// <RasterSymbolizer>
	// optional
	// describes how to render raster/matrix-coverage data
	// e.g. satellite photos, DEMs
	this.rasterSymbolizer;

    }
Rule.prototype.toString = function () {
    var str = this.name.toString() + this.title.toString() + this.abs.tostring() + this.legendGraphic.toString() + this.filter.toString() + this.elseFilter.toString() + this.minScaleDenominator.toString() + this.maxScaleDenominator.toString() + this.lineSymbolizer.toString() + this.polygonSymbolizer.toString() + this.pointSymbolizer.toString() + this.textSymbolizer.toString() + this.rasterSymbolizer.toString();
    if (str.length != 0)
	return '<Rule>\n' + str + '</Rule>\n';
    else
	return '';
}

// LegendGraphic Class
    function LegendGraphic() {
	// <Graphic>
	// required
	this.graphic;
    }

LegendGraphic.prototype.toString = function () {
    var str = this.graphic.toString();
    if (str.length != 0)
	return '<LegendGraphic>\n' + str + '</LegendGraphic>\n';
    else 
	return '';
}

// Graphic Class
    function Graphic() {
	// <ExternalGraphic>
	// optional
	// specifies a URI for an external image
	this.externalGraphic;
	
	// <Mark>
	// optional
	// describes an image
	this.mark;

	// <Opacity>
	// optional
	// percentage opacity
	this.opacity;
	
	// <Size>
	// optional
	// size in pixels
	this.size;
	
	// <Rotation>
	// optional
	// clockwise rotation in degrees
	this.rotation;
    }

Graphic.prototype.toString = function () {
    var str = this.externalGraphic.toString() + this.mark.toString() + this.opacity.toString() + this.size.toString() + this.rotation.toString();
    if (str.length != 0)
	return '<Graphic>\n' + str + '</Graphic>\n';
    else
	return '';
}
  
// ExternalGraphic Class
    function ExternalGraphic() {
	// <OnlineResource>
	// required
	// defines the URI
	this.onlineResource;
	
	// <Format>
	// required
	// string describing the type of the resource
	// i.e. image/png
	this.format;
    }

ExternalGraphic.prototype.toString = function () {
    var str = this.onlineResource.toString() + this.format.toString();
    if (str.length != 0)
	return '<ExternalGraphic>\n' + str + '</ExternalGraphic>\n';
    else
	return '';
}

// OnlineResource Class
    function OnlineResource(value) {
	// href
	this.value = value.toString();
    }

OnlineResource.prototype.toString = function () {
    var str = '<OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="' + this.value + '"/>\n'
    if (this.value.length != 0)
	return str;
    else
	return '';
}

// Format Class
    function Format(value) {
	// format string
	// e.g. image/png
	this.value = value.toString();
    }

Format.prototype.toString = function () {
    if (this.value.length != 0)
	return '<Format>' + this.value + '</Format>\n';
    else
	return '';
}

// Mark Class
    function Mark() {
	// <WellKnownName>
	// optional
	// identifies an existing mark by name
	this.wellKnownName;
	
	// <Fill>
	// optional
	// describes a fill for the Mark
	this.fill;
	
	// <Stroke>
	// optional
	// describes a stroke for the Mark
	this.stroke;
    }

Mark.prototype.toString = function () {
    var str = this.wellKnownName.toString() + this.fill.toString() + this.stroke.toString();
    if (this.str.length != 0) 
	return '<Mark>\n' + str + '</Mark>\n';
    else
	return '';
}

// WellKnownName Class
    function WellKnownName(value) {
	this.value = value.toString();
    }

WellKnownName.prototype.toString = function () {
    if (this.value.length != 0)
	return '<WellKnownName>' + this.value + '</WellKnownName>\n';
    else
	return '';
}

// Fill Class
    function Fill() {
	// <GraphicFill>
	this.graphicFill;
	
	// CssParameters
	this.cssParameterFill;
	this.cssParameterFillOpacity;
    }

Fill.prototype.toString() = function () {
    var str = this.graphicFill.toString() + this.cssParameterFill.toString() + this.cssParameterFillOpacity.toString();
    if (str.length != 0)
	return '<Fill>\n' + str + '</Fill>\n';
    else 
	return '';
}
    
// GraphicFill Class
    function GraphicFill() {
	// <Graphic>
	this.graphic;
    }

GraphicFill.prototype.toString() = function () {
    var str = this.graphic.toString();
    if (str.length != 0)
	return '<GraphicFill>\n' + str + '</GraphicFill>\n';
    else 
	return '';    
}

// CssParameter Classes (for fill, stroke, and font parameters)
    function CssParameterFill(value) {
	this.value = value.toString();
    }

CssParameterFill.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="fill">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterFillOpacity(value) {
	this.value = value.toString();
    }

CssParameterFillOpacity.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="fill-opacity">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStroke(value) {
	this.value = value.toString();
    }

CssParameterStroke.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStrokeOpacity(value) {
	this.value = value.toString();
    }

CssParameterStrokeOpacity.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke-opacity">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStrokeWidth(value) {
	this.value = value.toString();
    }

CssParameterStrokeWidth.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke-width">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStrokeLineJoin(value) {
	this.value = value.toString();
    }

CssParameterStrokeLineJoin.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke-linejoin">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStrokeLineCap(value) {
	this.value = value.toString();
    }

CssParameterStrokeLineCap.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke-linecap">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStrokeDashArray(value) {
	this.value = value.toString();
    }

CssParameterStrokeDashArray.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke-dasharray">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterStrokeDashOffset(value) {
	this.value = value.toString();
    }

CssParameterStrokeDashOffset.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="stroke-dashoffset">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterFontFamily(value) {
	this.value = value.toString();
    }

CssParameterFontFamily.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="font-family">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterFontStyle(value) {
	this.value = value.toString();
    }

CssParameterFontStyle.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="font-style">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterFontWeight(value) {
	this.value = value.toString();
    }

CssParameterFontWeight.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="font-weight">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}
    function CssParameterFontSize(value) {
	this.value = value.toString();
    }

CssParameterFontSize.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<CssParameter name="font-size">' + this.value + '</CssParameter>\n';
    else 
	return '';    
}

// Stroke Class
    function Stroke() {
	// choice: 0 or 1 of 1: GraphicFill, GraphicStroke
	
	// <GraphicFill>
	this.graphicFill;
	
	// <GraphicStroke>
	this.graphicStroke;
    }

Stroke.prototype.toString() = function () {
    var str = this.graphicFill.toString() + this.graphicStroke.toString();
    if (str.length != 0)
	return '<Stroke>\n' + str + '</Stroke>\n';
    else 
	return '';    
}    

// GraphicStroke Class
    function GraphicStroke() {
	// <Graphic>
	this.graphic;
    }

GraphicStroke.prototype.toString() = function () {
    var str = this.graphic.toString();
    if (str.length != 0)
	return '<GraphicStroke>\n' + str + '</GraphicStroke>\n';
    else 
	return '';    
}

// Opacity Class
    function Opacity(value) {
	this.value = value.toString();
    }

Opacity.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<Opacity>' + this.value + '</Opacity>\n';
    else 
	return '';    
}

// Size Class
    function Size(value) {
	this.value = value.toString();
    }

Size.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<Size>' + this.value + '</Size>\n';
    else 
	return '';    
}

// Rotation Class
    function Rotation(value) {
	this.value = value.toString();
    }

Rotation.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<Rotation>' + this.value + '</Rotation>\n';
    else 
	return '';    
}
    
// MinScaleDenominator Class
    function MinScaleDenominator(value) {
	this.value = value.toString();
    }

MinScaleDenominator.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<MinScaleDenominator>' + this.value + '</MinScaleDenominator>\n';
    else 
	return '';    
}

// MaxScaleDenominator Class
    function MaxScaleDenominator(value) {
	this.value = value.toString();
    }

MaxScaleDenominator.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<MaxScaleDenominator>' + this.value + '</MaxScaleDenominator>\n';
    else 
	return '';    
}

// LineSymbolizer Class
    function LineSymbolizer() {
	// <Geometry>
	// optional
	this.geometry;
	
	// <Stroke>
	// optional
	this.stroke;
    }

LineSymbolzier.prototype.toString() = function () {
    var str = this.geometry.toString() + this.stroke.toString;
    if (str.length != 0)
	return '<LineSymbolizer>\n' + str + '</LineSymbolizer>\n';
    else 
	return '';    
}

// Geometry Class
    function Geometry() {
	// <PropertyName>
	this.propertyName;
    }

Geometry.prototype.toString() = function () {
    var str = this.propertyName.toString();
    if (str.length != 0)
	return '<Geometry>\n' + str + '</Geometry>\n';
    else 
	return '';    
}

// PropertyName Class
    function PropertyName(value) {
	this.value = value.toString();
    }

PropertyName.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<ogc:PropertyName>' + this.value + '</ogc:PropertyName>\n';
    else 
	return '';    
}

// PolygonSymbolizer Class
    function PolygonSymbolizer() {
	// <Geometry>
	// optional
	this.geometry;
	
	// <Fill>
	// optional
	this.fill;

	// <Stroke>
	// optional
	this.stroke;
    }

PolygonSymbolizer.prototype.toString() = function () {
    var str = this.geometry.toString() + this.fill.toString() + this.stroke.toString();
    if (str.length != 0)
	return '<PolygonSymbolizer>\n' + str + '</PolygonSymbolizer>\n';
    else 
	return '';    
}

// PointSymbolizer Class
    function PointSymbolizer() {
	// <Geometry>
	// optional
	this.geometry;
	
	// <Graphic>
	// optional
	this.graphic;
    }

PointSymbolizer.prototype.toString() = function () {
    var str = this.geometry.toString() + this.graphic.toString();
    if (str.length != 0)
	return '<PointSymbolizer>\n' + str + '</PointSymbolizer>\n';
    else 
	return '';    
}

// TextSymbolizer Class
    function TextSymbolizer() {
	// <Geometry>
	// optional
	this.geometry;

	// <Label>
	// optional
	this.label;
    }

TextSymbolizer.prototype.toString() = function () {
    var str = this.geometry.toString() + this.label.toString();
    if (str.length != 0)
	return '<TextSymbolizer>\n' + str + '</TextSymbolizer>\n';
    else 
	return '';    
}

// Label Class
    function Label(value) {
	this.value = value.toString();
    }

Label.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<Label>' + this.value + '</Label>\n';
    else 
	return '';    
}

// Font Class
    function Font() {
	// Font CssParameters
	// optional
	this.cssParameterFontFamily;
	this.cssParameterFontStyle;
	this.cssParameterFontWeight;
	this.cssParameterFontSize;
    }

Font.prototype.toString() = function () {
    var str = this.cssParameterFontFamily.toString() + this.cssParameterFontStyle.toString() + this.cssParameterFontWeight.toString() + this.cssParameterFontSize.toString();
    if (str.length != 0)
	return '<Font>\n' + str + '</Font>\n';
    else 
	return '';    
}

// LabelPlacement Class
    function LabelPlacement() {
	//choice: 1 of 1: PointPlacement, LinePlacement
	// <PointPlacement>
	// optional
	this.pointPlacement;

	// <LinePlacement>
	// optional
	this.linePlacement;
    }

LabelPlacement.prototype.toString() = function () {
    var str = this.pointPlacement.toString() + this.LinePlacement.toString();
    if (str.length != 0)
	return '<LabelPlacement>\n' + str + '</LabelPlacement>\n';
    else 
	return '';    
}

// PointPlacement Class
    function PointPlacement() {
	// <AnchorPoint>
	// optional
	this.anchorPoint;

	// <Displacement>
	// optional
	this.displacement;
	
	// <Rotation>
	// optional
	this.rotation;
    }

TextSymbolizer.prototype.toString() = function () {
    var str = this.geometry.toString() + this.label.toString();
    if (str.length != 0)
	return '<TextSymbolizer>\n' + str + '</TextSymbolizer>\n';
    else 
	return '';    
}

// AnchorPoint Class
    function AnchorPoint() {
	// <AnchorPointX>
	// required
	this.anchorPointX;

	// <AnchorPointY>
	// required
	this.anchorPointY;
    }

AnchorPoint.prototype.toString() = function () {
    var str = this.anchorPointX.toString() + this.anchorPointY.toString();
    if (str.length != 0)
	return '<AnchorPoint>\n' + str + '</AnchorPoint>\n';
    else
	return '';
}

// AnchorPointX Class
    function AnchorPointX(value) {
	this.value = value.toString();
    }

AnchorPointX.prototype.toString() = function () {
    if (this.value.length != 0)
	return '<AnchorPointX>' + this.value + '</AnchorPointX>\n';
    else 
	return '';    
}

// AnchorPointY Class

// Displacement Class

// DisplacementX Class

// DisplacementY Class

// LinePlacement Class

// PerpendicularOffset Class

// Halo Class

// Radius Class

// RasterSymbolizer Class

// ChannelSelection Class

// RGBChannel Class (Red Green Blue)

// GrayChannel Class

// SourceChannelName Class

// ContrastEnhancement Class

// NormalizeClass

// HistogramClass

// GammaValueClass

// OverlapBehavior Class

// LATEST_ON_TOP Class

// EARLIEST_ON_TOP Class

// AVERAGE Class

// RANDOM Class

// ColorMap Class

// ColorMapEntry Class

// ShadedRelief Class

// BrightnessOnly Class

// ReliefFactor Class

// ImageOutline Class
