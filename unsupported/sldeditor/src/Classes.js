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
	return '<?xml version="1.0" encoding="UTF-8"?>\n<StyledLayerDescriptor version="1.0.0">\n' + str + '</StyledLayerDescriptor>\n';
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

// Filter Class
// ??? TODO

// ElseFilter Class
    function ElseFilter() {
	// doesn't need to do anything
    }
ElseFilter.prototype.toString = function() {
    return '\n<ElseFilter/>\n';
}

// Expression Class
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
	
	// may only have 1 filter OR 1 ElseFilter
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
    

// Graphic Class
    
// ExternalGraphic Class
    
// OnlineResource Class

// Format Class

// Mark Class

// WellKnownName Class

// Fill Class

// GraphicFill Class

// CssParameter Class (for fill, stroke, and font parameters)

// Stroke Class

// GraphicStroke Class

// Opacity Class

// Size Class

// Rotation Class

// MinScaleDenominator Class

// MaxScaleDenominator Class

// LineSymbolizer Class

// Geometry Class

// PropertyName Class

// PolygonSymbolizer Class

// PointSymbolizer Class

// TextSymbolizer Class

// Label Class

// Font Class

// LabelPlacement Class

// PointPlacement Class

// AnchorPoint Class

// AnchorPointX Class

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
