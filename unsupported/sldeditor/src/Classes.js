// finish adding all classes
// add Validation methods
// figure out when to fill in the data/go up the hierarchy

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
    var str = '<?xml version="1.0" encoding="UTF-8"?>\n<StyledLayerDescriptor version="1.0.0">\n';
    str += this.name.toString();
    str += this.title.toString();
    str += this.abs.toString();
    str += this.namedLayer.toString();
    str += this.userLayer.toString();
    str += '</StyledLayerDescriptor>';
    return str;
}
    
// Name Class
    function Name(name) {
	this.value = name.toString();
    }
Name.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Name>' + this.value + '</Name>\n';
    else
	return '';
}
    
// Title Class
    function Title(title) {
	this.value = title.toString();
    }
Name.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Title>' + this.value + '</Title>\n';
    else
	return '';
}
    
// Abstract Class
    function Abs(abs) {
	this.value = abs.toString();
    }
Name.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<Abstract>' + this.value + '</Abstract>\n';
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
    var str = '<NamedLayer>\n';
    str += this.name.toString();
    str += this.layerFeatureConstraints.toString();
    str += this.namedStyle.toString();
    str += this.userStyle.toString();
    str += '</NamedLayer>\n';
    return str;
}

// UserLayer Class

// RemoteOWS Class

// Service Class

// LayerFeatureConstraings Class
    function LayerFeatureConstraints() {
	// <FeatureTypeConstraint>
	// multiple
	// TODO: How to implement multiple instances of the same element?
	this.FeatureTypeConstraint;
    }
LayerFeatureConstraints.prototype.toString = function() {
    // ???
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
    var str = '<FeatureTypeConstraint>\n';
    str += this.featureTypeName.toString();
    str += this.filter.toString();
    str += this.extent.toString();
    str += '</FeatureTypeConstraint>\n';
    return str;
}

// FeatureTypeName Class
    function FeatureTypeName(name) {
	this.value = name.toString();
    }
FeatureTypeName.prototype.toString = function() {
    if (this.value.length != 0) 
	return '<FeatureTypeName>' + this.value + '</FeatureTypeName>\n';
    else
	return '';
}

// Filter Class

// ElseFilter Class

// Expression Class

// Extent Class



// NamedStyle Class

// UserStyle Class

// IsDefault Class

// FeatureTypeStyle Class

// SemanticTypeIdentifier Class

// Rule Class

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
