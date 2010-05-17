dojo.require("dojo.event.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.io.*");
dojo.require("dojo.widget.Button");
dojo.require("dojo.widget.TabContainer");
dojo.require("dojo.widget.ContentPane");
dojo.require("dojo.widget.SplitContainer");
dojo.require("dojo.dom.*");
dojo.require("dojo.widget.Tree");
dojo.require("dojo.widget.TreeNode");
dojo.require("dojo.widget.TreeSelector");
dojo.require("dojo.widget.TreeContextMenu");
dojo.require("dojo.widget.ResizableTextarea");
dojo.require("dojo.widget.Textbox");
dojo.require("dojo.widget.Select");

// holds the SLD being edited
var xmlDoc = null;

// Holds the current nodes
var focusNode = '';
var unselectNode = '';
var data = null;
// Holds the elements we want to show in the tree and be able to select
var importantElements = new Array();


function loadXMLFromString(){
    var text = document.getElementById('pastedXML').value;
    if(text){
	if(xmlDoc){
	    if(confirm("Do you really want to load a new SLD?")){
		xmlDoc = null;
	    } else {
		return;
	    }
	}
	xmlDoc = dojo.dom.createDocumentFromText(text);
	sldLoaded();
    }
}

function loadXMLFromFile(){
    var file = document.loadform.sldfile.value;
    if(file){
	if(xmlDoc){
	    if(confirm("Do you really want to load a new SLD?")){
		xmlDoc = null;
	    } else {
		return;
	    }
	}
	xmlDoc = dojo.dom.createDocument();
	xmlDoc.async = false;
	xmlDoc.load(file);
	sldLoaded();
	
    }
    // does not work in Safari... possibly not IE or PC Firefox
    // do I need something like:
    // netscape.security.PrivilegeManager.enablePrivilege('UniversalFileRead');
}

function createSLD(){
    if(xmlDoc){
	if(confirm("Do you really want to create a new SLD?")){
	    xmlDoc = null;
	} else {
	    return;
	}
    }
    //I'm lazy
    //How should this encoding actually be chosen?
    var text = '<?xml version="1.0" encoding="ISO-8859-1"?>\n<StyledLayerDescriptor version="1.0.0"\n xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"\n xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"\n xmlns:xlink="http://www.w3.org/1999/xlink"\n xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">\n </StyledLayerDescriptor>';
    xmlDoc = dojo.dom.createDocumentFromText(text);
    sldLoaded();
}

function generateXML(){
    if (xmlDoc){
	document.getElementById('generatedXML').value = dojo.dom.innerXML(xmlDoc);
    } else {
	document.getElementById('generatedXML').value = "No SLD Loaded.";
    }
}

// DOM Notes
// nodeName
// nodeName of Text nodes is always #text
// nodeName of element nodes is tag name
// nodeName of attribute nodes is attribute name
// Element node: nodeType = 1
// Attribute node: nodeType = 2
// attribute and text nodes have nodeValue
// Text node: nodeType = 3
// Comment node: nodeType = 8
// Document node: nodeType = 9


// TREE CODE
function expandAll(){
    //expandAllHelper(dojo.widget.byId('firstTree').children[0]);
    expandAllHelper(focusNode);
}

function expandAllHelper(treeNode){
    treeNode.expand();
    for(var i=0; i<treeNode.children.length; i++){
	expandAllHelper(treeNode.children[i]);
    }
}

function collapseAll(){
    //collapseAllHelper(dojo.widget.byId('firstTree').children[0]);
    for(var i=0; i<focusNode.children.length; i++){
	collapseAllHelper(focusNode.children[i]);
    }
}

function collapseAllHelper(treeNode){
    treeNode.collapse();
    for(var i=0; i<treeNode.children.length; i++){
	collapseAllHelper(treeNode.children[i]);
    }
}

function nodeSelectedHandler(message){
    var node = message.node;
    
    // if we didn't select the same node again
    if(unselectNode && unselectNode != message.node){
	// take care of the unselect node
	unselectNode.unMarkSelected();
	unselectNode='';
    }
    
    //set it as the focus
    focusNode = node;
       
    // set the Current node label
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
    // update the tab list
    updateData();

}

function updateData(){
    var dataArea = document.getElementById('mainDataArea');
    var hiddenArea = document.getElementById("hiddenArea");
    var newData = document.getElementById(focusNode.object.tagName);
    
    // if the main window is showing and the focusNode exists
    if(dojo.widget.byId('tabMenu').selectedChild=="mainArea" && focusNode){
	// swap the correct hiddenArea in
	if(data != newData){
	    if(data != null){
		hiddenArea.appendChild(data);
	    }
	    data = newData;
	    if(data != null){
		dataArea.appendChild(data);
	    }
	}
	// update the data from the DOM here
	switch(focusNode.object.tagName){
	    
	case 'StyledLayerDescriptor':
	    loadStyledLayerDescriptor();
	    break
	case 'NamedLayer':
		loadNamedLayer();
	    break
	case 'LayerFeatureConstraints':
	    
	    break
	case 'UserStyle':
		loadUserStyle();
	    break
	case 'FeatureTypeStyle':
		loadFeatureTypeStyle();
	    break
	case 'UserLayer':
	    
	    break
	case 'Rule':
		loadRule();
	    break
	case 'Graphic':
	    
	    break
	case 'Filter':
	    
	    break
	case 'Elsefilter':
	    
	    break
	case 'LineSymbolizer':
		loadLineSymbolizer();
	    break
	case 'PolygonSymbolizer':
	    
	    break
	case 'PointSymbolizer':
	    
	    break
	case 'TextSymbolizer':
	    
	    break
	case 'RasterSymbolizer':
	    
	    break
	default:
	    alert("Mismatch in updateData Switch.");
	}
    }
}

function saveData(){
    switch(focusNode.object.tagName){
	
    case 'StyledLayerDescriptor':
	saveStyledLayerDescriptor();
	break
    case 'NamedLayer':
	    saveNamedLayer();
	break
    case 'LayerFeatureConstraints':
	    
	    break
    case 'UserStyle':
	    saveUserStyle();
	break
    case 'FeatureTypeStyle':
	    saveFeatureTypeStyle();
	break
    case 'UserLayer':
	    
	    break
    case 'Rule':
	    saveRule();
	break
    case 'Graphic':
	    
	    break
    case 'Filter':
	    
	    break
    case 'Elsefilter':
	    
	    break
    case 'LineSymbolizer':
	    saveLineSymbolizer();
	    break
    case 'PolygonSymbolizer':
	    
	    break
    case 'PointSymbolizer':
	    
	    break
    case 'TextSymbolizer':
	    
	    break
    case 'RasterSymbolizer':
	    
	    break
    default:
	    alert("Mismatch in saveData Switch.");
    }
}

function loadStyledLayerDescriptor(){
    var name = dojo.widget.byId('SLDName'); 
    var title = dojo.widget.byId('SLDTitle');
    var abst = document.getElementById('SLDAbstract');
    var NL = dojo.widget.byId('SLDNamedLayer');
    var UL = dojo.widget.byId('SLDUserLayer');
    
    // Set to defaults
    name.textbox.value = '';
    title.textbox.value = '';
    abst.value = '';
    NL.setValue(0);
    UL.setValue(0);
    
    // Update with existing values
    var node = dojo.dom.firstElement(focusNode.object);
    while(node){
	switch(node.tagName){
	case 'Name':
	    name.textbox.value = node.textContent;
	    break
	case 'Title':
	    title.textbox.value = node.textContent;
	    break
	case 'Abstract':
	    abst.value = node.textContent;
	    break
	case 'NamedLayer':
	    NL.setValue(1);
	    break
	case 'UserLayer':
	    UL.setValue(1);
	    break
	default:
	}
	node = dojo.dom.nextElement(node);
    }
}

function saveStyledLayerDescriptor(){
    var name = dojo.widget.byId('SLDName');
    var title = dojo.widget.byId('SLDTitle');
    var abst = document.getElementById('SLDAbstract');
    var NL = dojo.widget.byId('SLDNamedLayer');
    var UL = dojo.widget.byId('SLDUserLayer');
    var isName = 0, isTitle = 0, isAbstract = 0, isUL = 0, isNL = 0;
    var newel = '', nodeid = '', locnode = '';
    
    var node = dojo.dom.firstElement(focusNode.object);
    var nextnode = '';
    
    while(node){
	nextnode = dojo.dom.nextElement(node);
	switch(node.tagName){
	case 'Name':
	    if(name.textbox.value == ''){
		node.parentNode.removeChild(node);
		//update tree title
		focusNode.title = focusNode.object.tagName;
		focusNode.edit(focusNode);
	    }else{
		node.firstChild.data = name.textbox.value;
		//update tree title
		focusNode.title = focusNode.object.tagName + ': ' + node.firstChild.data;
		focusNode.edit(focusNode);
		isName = 1;
	    }
	    break
	case 'Title':
		if(title.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = title.textbox.value;
		    isTitle = 1;
		}
	    break
	case 'Abstract':
		if(abst.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = abst.value;
		    isAbstract = 1;
		}
	    break
	case 'NamedLayer':
		if(NL.checked == 0){
		    if(confirm("Do you really want to delete this NamedLayer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isNL = 1;
		}
	    break
	case 'UserLayer':
		if(UL.checked == 0){
		    if(confirm("Do you really want to delete this UserLayer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isUL = 1;
		}
	    break
	default:
	}
	node = nextnode;
    }
    //initialize the position element to keep track of order
    locnode = dojo.dom.firstElement(focusNode.object);
    // add a Name element
    if(isName == 0 && name.textbox.value != ''){
	newel = xmlDoc.createElement('Name');
	newel.appendChild(xmlDoc.createTextNode(name.textbox.value));
	//insert it as the first element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	//update the tree title
	focusNode.title = focusNode.object.tagName + ': ' + newel.firstChild.data;
	focusNode.edit(focusNode);
    }
    //update the title display area
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
     // add a Title element
    if(isTitle == 0 && title.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Title');
	newel.appendChild(xmlDoc.createTextNode(title.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isTitle){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // add an Abstract element
    if(isAbstract == 0 && abst.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Abstract');
	newel.appendChild(xmlDoc.createTextNode(abst.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isAbstract){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // if the NL box is checked but there is no NL element
    if(isNL == 0 && NL.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
        newel = xmlDoc.createElement('NamedLayer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update the tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index 0 in tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'NamedLayer',
			widgetId:nodeid,
			object:newel
			}),
	    0
	    );
    }else if(isNL){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // same for UL
    if(isUL == 0 && UL.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('UserLayer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index 1 if a child already exists, else 0
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'UserLayer',
			widgetId:nodeid,
			object:newel
			}),
	    focusNode.children.length > 0 ? 1 : 0
	    );
    }
}

function loadNamedLayer(){
    var name = dojo.widget.byId('NamedLayerName'); 
    // TODO: LayerFeatureConstraints::FeatureTypeConstraint (multiple)
    var namedstyle = dojo.widget.byId('NamedLayerNamedStyle');
    var US = dojo.widget.byId('NLUserStyle');
    
    // Set to defaults
    name.textbox.value = '';
    namedstyle.textbox.value = '';
    US.setValue(0);
    
    // Update with existing values
    var node = dojo.dom.firstElement(focusNode.object);
    while(node){
	switch(node.tagName){
	case 'Name':
	    name.textbox.value = node.textContent;
	    break
	case 'LayerFeatureConstraints':
		//TODO
		break
	case 'NamedStyle':
		namedstyle.textbox.value = dojo.dom.firstElement(node).textContent;
	    break
	case 'UserStyle':
		US.setValue(1);
	    break
	default:
	}
	node = dojo.dom.nextElement(node);
    }
}

function saveNamedLayer(){
    var name = dojo.widget.byId('NamedLayerName');
    // TODO: LayerFeatureConstraints::FeatureTypeConstraint (multiple)
    var namedstyle = dojo.widget.byId('NamedLayerNamedStyle');
    var US = dojo.widget.byId('NLUserStyle');
    
    var isName = 0, isNamedStyle = 0, isUS = 0;
    var newel = '', nodeid = '', locnode = '';
    
    var node = dojo.dom.firstElement(focusNode.object);
    var nextnode = '';
    
    while(node){
	nextnode = dojo.dom.nextElement(node);
	switch(node.tagName){
	case 'Name':
	    if(name.textbox.value == ''){
		node.parentNode.removeChild(node);
		//update tree title
		focusNode.title = focusNode.object.tagName;
		focusNode.edit(focusNode);
	    }else{
		node.firstChild.data = name.textbox.value;
		//update tree title
		focusNode.title = focusNode.object.tagName + ': ' + node.firstChild.data;
		focusNode.edit(focusNode);
		isName = 1;
	    }
	    break
	case 'LayerFeatureConstraints':
		//TODO
		break
	case 'NamedStyle':
		if(namedstyle.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    dojo.dom.firstElement(node).firstChild.data = namedstyle.textbox.value;
		    isNamedStyle = 1;
		}
	    break
	case 'UserStyle':
		if(US.checked == 0){
		    if(confirm("Do you really want to delete this UserStyle?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isUS = 1;
		}
	    break
	default:
	}
	node = nextnode;
    }
    //initialize the position element to keep track of order
    locnode = dojo.dom.firstElement(focusNode.object);
    // add a Name element
    if(isName == 0 && name.textbox.value != ''){
	newel = xmlDoc.createElement('Name');
	newel.appendChild(xmlDoc.createTextNode(name.textbox.value));
	//insert it as the first element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	//update the tree title
	focusNode.title = focusNode.object.tagName + ': ' + newel.firstChild.data;
	focusNode.edit(focusNode);
    }
    //update the title display area
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
     // add a NamedStyle element
    if(isNamedStyle == 0 && namedstyle.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('NamedStyle');
	var tmp = xmlDoc.createElement('Name');
	tmp.appendChild(xmlDoc.createTextNode(namedstyle.textbox.value));
	newel.appendChild(tmp);
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isNamedStyle){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    //TODO LayerFeatureConstraints
    
    // if the US box is checked but there is no US element
    if(isUS == 0 && US.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
        newel = xmlDoc.createElement('UserStyle');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update the tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index 0 in tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'UserStyle',
			widgetId:nodeid,
			object:newel
			}),
	    0
	    );
    }else if(isUS){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
}

function loadUserStyle(){
    var name = dojo.widget.byId('UserStyleName'); 
    var title = dojo.widget.byId('UserStyleTitle');
    var abst = document.getElementById('UserStyleAbstract');
    var IsDefault = dojo.widget.byId('UserStyleIsDefault');
    //TODO: make FeatureTypeStyle multiple
    var FTS = dojo.widget.byId('UserStyleFTS');
        
    // Set to defaults
    name.textbox.value = '';
    title.textbox.value = '';
    abst.value = '';
    IsDefault.setValue(0);
    FTS.setValue(0);
    
    // Update with existing values
    var node = dojo.dom.firstElement(focusNode.object);
    while(node){
	switch(node.tagName){
	case 'Name':
	    name.textbox.value = node.textContent;
	    break
	case 'Title':
	    title.textbox.value = node.textContent;
	    break
	case 'Abstract':
	    abst.value = node.textContent;
	    break
	case 'IsDefault':
	    IsDefault.setValue(1);
	    break
	case 'FeatureTypeStyle':
	    FTS.setValue(1);
	    break
	default:
	}
	node = dojo.dom.nextElement(node);
    }
}

function saveUserStyle(){
    var name = dojo.widget.byId('UserStyleName');
    var title = dojo.widget.byId('UserStyleTitle');
    var abst = document.getElementById('UserStyleAbstract');
    var IsDefault = dojo.widget.byId('UserStyleIsDefault');
    //TODO: make FeatureTypeStyle multiple
    var FTS = dojo.widget.byId('UserStyleFTS');
   
    var isName = 0, isTitle = 0, isAbstract = 0, isIsDefault = 0, isFTS = 0;
    var newel = '', nodeid = '', locnode = '';
    
    var node = dojo.dom.firstElement(focusNode.object);
    var nextnode = '';
    
    while(node){
	nextnode = dojo.dom.nextElement(node);
	switch(node.tagName){
	case 'Name':
	    if(name.textbox.value == ''){
		node.parentNode.removeChild(node);
		//update tree title
		focusNode.title = focusNode.object.tagName;
		focusNode.edit(focusNode);
	    }else{
		node.firstChild.data = name.textbox.value;
		//update tree title
		focusNode.title = focusNode.object.tagName + ': ' + node.firstChild.data;
		focusNode.edit(focusNode);
		isName = 1;
	    }
	    break
	case 'Title':
		if(title.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = title.textbox.value;
		    isTitle = 1;
		}
	    break
	case 'Abstract':
		if(abst.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = abst.value;
		    isAbstract = 1;
		}
	    break
	case 'IsDefault':
		if(IsDefault.checked == 0){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = '1';
		    isIsDefault = 1;
		}
	    break
	case 'FeatureTypeStyle':
		if(FTS.checked == 0){
		    if(confirm("Do you really want to delete this FeatureTypeStyle?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isFTS = 1;
		}
	    break
	default:
	}
	node = nextnode;
    }
    //initialize the position element to keep track of order
    locnode = dojo.dom.firstElement(focusNode.object);
    // add a Name element
    if(isName == 0 && name.textbox.value != ''){
	newel = xmlDoc.createElement('Name');
	newel.appendChild(xmlDoc.createTextNode(name.textbox.value));
	//insert it as the first element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	//update the tree title
	focusNode.title = focusNode.object.tagName + ': ' + newel.firstChild.data;
	focusNode.edit(focusNode);
    }
    //update the title display area
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
     // add a Title element
    if(isTitle == 0 && title.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Title');
	newel.appendChild(xmlDoc.createTextNode(title.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isTitle){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // add an Abstract element
    if(isAbstract == 0 && abst.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Abstract');
	newel.appendChild(xmlDoc.createTextNode(abst.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isAbstract){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // if the IsDefault box is checked but there is no IsDefault element
    if(isIsDefault == 0 && IsDefault.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
        newel = xmlDoc.createElement('IsDefault');
	newel.appendChild(xmlDoc.createTextNode('1'));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isIsDefault){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // same for FTS
    if(isFTS == 0 && FTS.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('FeatureTypeStyle');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index 0 in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'FeatureTypeStyle',
			widgetId:nodeid,
			object:newel
			}),
	    0
	    );
    }
}

function loadFeatureTypeStyle(){
    var name = dojo.widget.byId('FeatureTypeStyleName'); 
    var title = dojo.widget.byId('FeatureTypeStyleTitle');
    var abst = document.getElementById('FeatureTypeStyleAbstract');
    var ftname = dojo.widget.byId('FTSFeatureTypeName');
    var ftsem = dojo.widget.byId('FTSSemanticTypeIdentifier');
    //TODO: make Rule multiple
    var rule = dojo.widget.byId('FTSRule');
        
    // Set to defaults
    name.textbox.value = '';
    title.textbox.value = '';
    abst.value = '';
    ftname.textbox.value = '';
    ftsem.textbox.value = '';
    rule.setValue(0);
    
    // Update with existing values
    var node = dojo.dom.firstElement(focusNode.object);
    while(node){
	switch(node.tagName){
	case 'Name':
	    name.textbox.value = node.textContent;
	    break
	case 'Title':
		title.textbox.value = node.textContent;
	    break
	case 'Abstract':
		abst.value = node.textContent;
	    break
	case 'FeatureTypeName':
		ftname.textbox.value = node.textContent;
	    break
	case 'SemanticTypeIdentifier':
		ftsem.textbox.value = node.textContent;
	    break
	case 'Rule':
		rule.setValue(1);
	    break
	default:
	}
	node = dojo.dom.nextElement(node);
    }
}

function saveFeatureTypeStyle(){
    var name = dojo.widget.byId('FeatureTypeStyleName');
    var title = dojo.widget.byId('FeatureTypeStyleTitle');
    var abst = document.getElementById('FeatureTypeStyleAbstract');
    var ftname = dojo.widget.byId('FTSFeatureTypeName');
    var ftsem = dojo.widget.byId('FTSSemanticTypeIdentifier');
    //TODO: make Rule multiple
    var rule = dojo.widget.byId('FTSRule');
    
    var isName = 0, isTitle = 0, isAbstract = 0, isFtname = 0, isFtsem = 0, isRule = 0;
    var newel = '', nodeid = '', locnode = '';
    
    var node = dojo.dom.firstElement(focusNode.object);
    var nextnode = '';
    
    while(node){
	nextnode = dojo.dom.nextElement(node);
	switch(node.tagName){
	case 'Name':
	    if(name.textbox.value == ''){
		node.parentNode.removeChild(node);
		//update tree title
		focusNode.title = focusNode.object.tagName;
		focusNode.edit(focusNode);
	    }else{
		node.firstChild.data = name.textbox.value;
		//update tree title
		focusNode.title = focusNode.object.tagName + ': ' + node.firstChild.data;
		focusNode.edit(focusNode);
		isName = 1;
	    }
	    break
	case 'Title':
		if(title.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = title.textbox.value;
		    isTitle = 1;
		}
	    break
	case 'Abstract':
		if(abst.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = abst.value;
		    isAbstract = 1;
		}
	    break
	case 'FeatureTypeName':
		if(ftname.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = ftname.textbox.value;
		    isFtname = 1;
		}
	    break
	case 'SemanticTypeIdentifier':
		if(ftsem.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = ftsem.textbox.value;
		    isFtsem = 1;
		}
	    break
	case 'Rule':
		if(rule.checked == 0){
		    if(confirm("Do you really want to delete this Rule?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isRule = 1;
		}
	    break
	default:
	}
	node = nextnode;
    }
    //initialize the position element to keep track of order
    locnode = dojo.dom.firstElement(focusNode.object);
    // add a Name element
    if(isName == 0 && name.textbox.value != ''){
	newel = xmlDoc.createElement('Name');
	newel.appendChild(xmlDoc.createTextNode(name.textbox.value));
	//insert it as the first element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	//update the tree title
	focusNode.title = focusNode.object.tagName + ': ' + newel.firstChild.data;
	focusNode.edit(focusNode);
    }
    //update the title display area
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
     // add a Title element
    if(isTitle == 0 && title.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Title');
	newel.appendChild(xmlDoc.createTextNode(title.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isTitle){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // add an Abstract element
    if(isAbstract == 0 && abst.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Abstract');
	newel.appendChild(xmlDoc.createTextNode(abst.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isAbstract){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // add a FeatureTypeName element
    if(isFtname == 0 && ftname.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
        newel = xmlDoc.createElement('FeatureTypeName');
	newel.appendChild(xmlDoc.createTextNode(ftname.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isFtname){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // if the Rule box is checked but there is no Rule element
    if(isRule == 0 && rule.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('Rule');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index 0 in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'Rule',
			widgetId:nodeid,
			object:newel
			}),
	    0
	    );
    }
}

function loadRule(){
    var name = dojo.widget.byId('RuleName'); 
    var title = dojo.widget.byId('RuleTitle');
    var abst = document.getElementById('RuleAbstract');
    var min = dojo.widget.byId('RuleMin');
    var max = dojo.widget.byId('RuleMax');
    //TODO: Filter/Elsefilter
    var line = dojo.widget.byId('RuleLine');
    var polygon = dojo.widget.byId('RulePolygon');
    var point = dojo.widget.byId('RulePoint');
    var text = dojo.widget.byId('RuleText');
    var raster = dojo.widget.byId('RuleRaster');
    
    // Set to defaults
    name.textbox.value = '';
    title.textbox.value = '';
    abst.value = '';
    min.textbox.value = '';
    max.textbox.value = '';
    line.setValue(0);
    polygon.setValue(0);
    point.setValue(0);
    text.setValue(0);
    raster.setValue(0);
    
    // Update with existing values
    var node = dojo.dom.firstElement(focusNode.object);
    while(node){
	switch(node.tagName){
	case 'Name':
	    name.textbox.value = node.textContent;
	    break
	case 'Title':
		title.textbox.value = node.textContent;
	    break
	case 'Abstract':
		abst.value = node.textContent;
	    break
	case 'MinScaleDenominator':
		min.textbox.value = node.textContent;
	    break
	case 'MaxScaleDenominator':
		max.textbox.value = node.textContent;
	    break
	case 'LineSymbolizer':
		line.setValue(1);
	    break
	case 'PolygonSymbolizer':
		polygon.setValue(1);
	    break
	case 'PointSymbolizer':
		point.setValue(1);
	    break
	case 'TextSymbolizer':
		text.setValue(1);
	    break
	case 'RasterSymbolizer':
		raster.setValue(1);
	    break
		
	default:
	}
	node = dojo.dom.nextElement(node);
    }
}

function saveRule(){
    var name = dojo.widget.byId('RuleName'); 
    var title = dojo.widget.byId('RuleTitle');
    var abst = document.getElementById('RuleAbstract');
    var min = dojo.widget.byId('RuleMin');
    var max = dojo.widget.byId('RuleMax');
    //TODO: Filter/Elsefilter
    var line = dojo.widget.byId('RuleLine');
    var polygon = dojo.widget.byId('RulePolygon');
    var point = dojo.widget.byId('RulePoint');
    var text = dojo.widget.byId('RuleText');
    var raster = dojo.widget.byId('RuleRaster');
    
    var isName = 0, isTitle = 0, isAbstract = 0, isMin = 0, isMax = 0, isLine = 0, isPolygon = 0, isPoint = 0, isText = 0, isRaster = 0;
    var newel = '', nodeid = '', locnode = '';
    
    var node = dojo.dom.firstElement(focusNode.object);
    var nextnode = '';
    
    while(node){
	nextnode = dojo.dom.nextElement(node);
	switch(node.tagName){
	case 'Name':
	    if(name.textbox.value == ''){
		node.parentNode.removeChild(node);
		//update tree title
		focusNode.title = focusNode.object.tagName;
		focusNode.edit(focusNode);
	    }else{
		node.firstChild.data = name.textbox.value;
		//update tree title
		focusNode.title = focusNode.object.tagName + ': ' + node.firstChild.data;
		focusNode.edit(focusNode);
		isName = 1;
	    }
	    break
	case 'Title':
		if(title.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = title.textbox.value;
		    isTitle = 1;
		}
	    break
	case 'Abstract':
		if(abst.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = abst.value;
		    isAbstract = 1;
		}
	    break
	case 'MinScaleDenominator':
		if(min.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = min.textbox.value;
		    isMin = 1;
		}
	    break
	case 'MaxScaleDenominator':
		if(max.textbox.value == ''){
		    node.parentNode.removeChild(node);
		}else{
		    node.firstChild.data = max.textbox.value;
		    isMax = 1;
		}
	    break
	case 'LineSymbolizer':
		if(line.checked == 0){
		    if(confirm("Do you really want to delete this LineSymbolizer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isLine = 1;
		}
	    break
	case 'LineSymbolizer':
		if(line.checked == 0){
		    if(confirm("Do you really want to delete this LineSymbolizer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isLine = 1;
		}
	    break
	case 'PolygonSymbolizer':
		if(polygon.checked == 0){
		    if(confirm("Do you really want to delete this PolygonSymbolizer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isPolygon = 1;
		}
	    break
	case 'PointSymbolizer':
		if(line.checked == 0){
		    if(confirm("Do you really want to delete this PointSymbolizer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isPoint = 1;
		}
	    break
	case 'TextSymbolizer':
		if(text.checked == 0){
		    if(confirm("Do you really want to delete this TextSymbolizer?")){
			//remove it from the tree
			for (var i=0; i<focusNode.children.length; i++){
			    if(focusNode.children[i].object == node){
				focusNode.removeNode(focusNode.children[i]);
				break;
			    }
			}
			//remove it from the DOM
			node.parentNode.removeChild(node);
		    }
		}else{
		    isText = 1;
		}
	    break

	default:
	}
	node = nextnode;
    }
    //initialize the position element to keep track of order
    locnode = dojo.dom.firstElement(focusNode.object);
    // add a Name element
    if(isName == 0 && name.textbox.value != ''){
	newel = xmlDoc.createElement('Name');
	newel.appendChild(xmlDoc.createTextNode(name.textbox.value));
	//insert it as the first element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	//update the tree title
	focusNode.title = focusNode.object.tagName + ': ' + newel.firstChild.data;
	focusNode.edit(focusNode);
    }
    //update the title display area
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
     // add a Title element
    if(isTitle == 0 && title.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Title');
	newel.appendChild(xmlDoc.createTextNode(title.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isTitle){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // add an Abstract element
    if(isAbstract == 0 && abst.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Abstract');
	newel.appendChild(xmlDoc.createTextNode(abst.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isAbstract){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    
    // add a MinScaleDenominator element
    if(isMin == 0 && min.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
        newel = xmlDoc.createElement('FeatureTypeName');
	newel.appendChild(xmlDoc.createTextNode(min.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isMin){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    var ind = 0;
    // if the LineSymbolizer box is checked but there is no LineSymbolizer element
    if(isLine == 0 && line.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('LineSymbolizer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'LineSymbolizer',
			widgetId:nodeid,
			object:newel
			}),
	    ind
	    );
	//update the index
	ind = ind + 1;
    }else if(isLine){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // if the PolygonSymbolizer box is checked but there is no PolygonSymbolizer element
    if(isPolygon == 0 && polygon.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('PolygonSymbolizer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'PolygonSymbolizer',
			widgetId:nodeid,
			object:newel
			}),
	    ind
	    );
	//update the index
	ind = ind + 1;
    }else if(isPolygon){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // if the PointSymbolizer box is checked but there is no PointSymbolizer element
    if(isPoint == 0 && point.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('PointSymbolizer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'PointSymbolizer',
			widgetId:nodeid,
			object:newel
			}),
	    ind
	    );
	//update the index
	ind = ind + 1;
    }else if(isPoint){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // if the TextSymbolizer box is checked but there is no TextSymbolizer element
    if(isText == 0 && text.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('TextSymbolizer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'TextSymbolizer',
			widgetId:nodeid,
			object:newel
			}),
	    ind
	    );
	//update the index
	ind = ind + 1;
    }else if(isText){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
        // if the RasterSymbolizer box is checked but there is no RasterSymbolizer element
    if(isRaster == 0 && raster.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('RasterSymbolizer');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	// update tree
	nodeid = dojo.dom.getUniqueId();
	//insert at index in the tree
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'RasterSymbolizer',
			widgetId:nodeid,
			object:newel
			}),
	    ind
	    );
    }
}

function loadLineSymbolizer(){
    var geom = dojo.widget.byId('LineGeometry'); 
    var solid = document.getElementById('LineSolid');
    var fill = document.getElementById('LineFill');
    var stroke = document.getElementById('LineStroke');
    var color = dojo.widget.byId('LineColor');
    var opacity = dojo.widget.byId('LineOpacity');
    var width = dojo.widget.byId('LineWidth');
    var linejoin = dojo.widget.byId('LineLinejoin');
    var linecap = dojo.widget.byId('LineLinecap');
    var dasharray = dojo.widget.byId('LineDasharray');
    var dashoffset = dojo.widget.byId('LineDashoffset');
    
    // Set to defaults
    geom.textbox.value = '';
    solid.checked = true;
    color.textbox.value = '';
    opacity.textbox.value = '';
    width.textbox.value = '';
    linejoin.textbox.value = '';
    linecap.textbox.value = '';
    dasharray.textbox.value = '';
    dashoffset.textbox.value = '';
    
    // Update with existing values
    var node = dojo.dom.firstElement(focusNode.object);
    var node2;
    while(node){
	switch(node.tagName){
	case 'Geometry':
	    geom.textbox.value = dojo.dom.firstElement(node).textContent;
	    break
	case 'Stroke':
		node2 = dojo.dom.firstElement(node);
	    while(node2){
		switch (node2.tagName){
		case 'GraphicFill':
		    fill.checked = true;
		    break
		case 'GraphicStroke':
			stroke.checked = true;
		    break
		case 'CssParameter':
			switch(node2.getAttribute('name')){
			case 'stroke':
			    color.textbox.value = node2.textContent;
			    break
			case 'stroke-opacity':
				opacity.textbox.value = node2.textContent;
			    break
			case 'stroke-width':
				width.textbox.value = node2.textContent;
			    break
			case 'stroke-linejoin':
				linejoine.textbox.value = node2.textContent;
			    break
			case 'stroke-linecap':
				linecap.textbox.value = node2.textContent;
			    break
			case 'stroke-dasharray':
				dasharray.textbox.value = node2.textContent;
			    break
			case 'stroke-dashoffset':
				dashoffset.textbox.value = node2.textContent;
			    break
			default:
			}
		    break
		default:
		}
		node2 = dojo.dom.nextElement(node2);
	    }
	    break
	default:
	}
	node = dojo.dom.nextElement(node);
    }
}

function saveLineSymbolizer(){
    var geom = dojo.widget.byId('LineGeometry'); 
    var solid = document.getElementById('LineSolid');
    var fill = document.getElementById('LineFill');
    var stroke = document.getElementById('LineStroke');
    var color = dojo.widget.byId('LineColor');
    var opacity = dojo.widget.byId('LineOpacity');
    var width = dojo.widget.byId('LineWidth');
    var linejoin = dojo.widget.byId('LineLinejoin');
    var linecap = dojo.widget.byId('LineLinecap');
    var dasharray = dojo.widget.byId('LineDasharray');
    var dashoffset = dojo.widget.byId('LineDashoffset');
    
    var parentStroke = 0, strokenode = '';
    var isGeom = 0, isSolid = solid.checked, isFill = 0, isStroke = 0, isGraphic = 0, isColor = 0, isOpacity = 0, isWidth = 0, isLinejoin = 0, isLinecap = 0, isDasharray = 0, isDashoffset = 0;
    var newel = '', nodeid = '', locnode = '';
    
    var node = dojo.dom.firstElement(focusNode.object);
    var node2;
    var nextnode = '', graphicnode = '';
    
    while(node){
	nextnode = dojo.dom.nextElement(node);
	switch(node.tagName){
	case 'Geometry':
	    if(geom.textbox.value == ''){
		node.parentNode.removeChild(node);
	    }else{
		dojo.dom.firstElement(node).firstChild.data = geom.textbox.value;
		isGeom = 1;
	    }
	    break
	case 'Stroke':
		parentStroke = 1;
	    strokenode = node;
		node2 = dojo.dom.firstElement(node);
	    while(node2){
		switch (node2.tagName){
		case 'GraphicFill':
		    if(solid.checked){
			if(confirm("Do you really want to remove this GraphicFill?")){
			    node.parentNode.removeChild(node);
			}
		    }else if(stroke.checked){
			graphicnode = dojo.dom.firstElement(node);
		    }else{
			isFill = 1;
		    }
		    break
		case 'GraphicStroke':
			if(stroke.checked){
			    if(confirm("Do you really want to remove this GraphicStroke?")){
				node.parentNode.removeChild(node);
			    }
			}else if(fill.checked){
			    graphicnode = dojo.dom.firstElement(node);
			}else {
			    isStroke = 1;
			}
		    break
		case 'CssParameter':
			switch(node2.getAttribute('name')){
			case 'stroke':
			    if(color.textbox.value == ''){
				node2.parentNode.removeChild(node2);
			    } else {
				isColor = 1;
			    }
			    break
			case 'stroke-opacity':
				if(opacity.textbox.value == ''){
				    node2.parentNode.removeChild(node2);
				} else {
				    isOpacity = 1;
			    }
			    break
			case 'stroke-width':
				if(width.textbox.value == ''){
				    node2.parentNode.removeChild(node2);
				} else {
				    isWidth = 1;
			    }
			    break
			case 'stroke-linejoin':
				if(linejoin.textbox.value == ''){
				    node2.parentNode.removeChild(node2);
				} else {
				    isLinejoin = 1;
			    }
			    break
			case 'stroke-linecap':
				if(linecap.textbox.value == ''){
				    node2.parentNode.removeChild(node2);
				} else {
				    isLinecap = 1;
			    }
			    break
			case 'stroke-dasharray':
				if(dasharray.textbox.value == ''){
				    node2.parentNode.removeChild(node2);
				} else {
				    isDasharray = 1;
			    }
			    break
			case 'stroke-dashoffset':
				if(dashoffset.textbox.value == ''){
				    node2.parentNode.removeChild(node2);
				} else {
				    isDashoffset = 1;
			    }
			    break
			default:
			}
		    break
		default:
		}
		node2 = dojo.dom.nextElement(node2);
	    }
	    break
	default:
	}
	node = nextnode;
    }
    //initialize the position element to keep track of order
    locnode = dojo.dom.firstElement(focusNode.object);
    // add a Geometry element
    if(isGeom == 0 && geom.textbox.value != ''){
	newel = xmlDoc.createElement('Geometry');
	var tmp = xmlDoc.createElement('ogc:PropertyName');
	tmp.appendChild(xmlDoc.createTextNode(geom.textbox.value));
	newel.appendChild(tmp);
	//insert it as the first element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }
    // add a Stroke element
    if(parentStroke == 0){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Stroke');
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = dojo.dom.firstElement(newel);
    }else if(parentStroke){
	//increment the position counter
	//The rest of the nodes are children of Stroke
	locnode = dojo.dom.firstElement(strokenode);
    }
    // add a Graphic node if there isn't one
    if(graphicnode == ''){
	graphicnode = xmlDoc.createElement('Graphic');
	nodeid = dojo.dom.getUniqueId();
	focusNode.addChild(dojo.widget.createWidget("TreeNode", {title:'Graphic',
			widgetId:nodeid,
			object:graphicnode
			}),
	    0
	    );
    }
    // add a GraphicFill element
    if(isFill == 0 &&  fill.checked){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('GraphicFill');
	newel.appendChild(graphicnode);
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isFill){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a GraphicStroke element
    if(isStroke == 0 &&  stroke.checked){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('GraphicStroke');
	newel.appendChild(graphicnode);
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isStroke){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a CssParameter Color element
    if(isColor == 0 && color.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke');
	newel.appendChild(xmlDoc.createTextNode(color.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isColor){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a CssParameter Opacity element
    if(isOpacity == 0 && opacity.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke-opacity');
	newel.appendChild(xmlDoc.createTextNode(opacity.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isOpacity){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
        // add a CssParameter Width element
    if(isWidth == 0 && width.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke-width');
	newel.appendChild(xmlDoc.createTextNode(width.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isWidth){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a CssParameter linejoin element
    if(isLinejoin == 0 &&  linejoin.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke-linejoin');
	newel.appendChild(xmlDoc.createTextNode(linejoin.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isLinejoin){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a CssParameter linecap element
    if(isLinecap == 0 &&  linecap.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke-linecap');
	newel.appendChild(xmlDoc.createTextNode(linecap.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isLinecap){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a CssParameter dasharray element
    if(isDasharray == 0 &&  dasharray.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke-dasharray');
	newel.appendChild(xmlDoc.createTextNode(dasharray.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isDasharray){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
    // add a CssParameter dashoffset element
    if(isDashoffset == 0 &&  dashoffset.textbox.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('CssParameter');
	newel.setAttribute('name','stroke-dashoffset');
	newel.appendChild(xmlDoc.createTextNode(dashoffset.textbox.value));
	//insert it as the next element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }else if(isDashoffset){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);
    }
}


// consider only building tree levels that are expanded
function buildTree(node){
    var tree = dojo.widget.byId("firstTree");
    tree.destroyChildren();
    buildTreeHelper(node, tree);
}

function buildTreeHelper(node, tree){
    var curnode = dojo.dom.firstElement(node);
    var strtitle;
    var nodeid;
    var newnode;
    var childnode;
    var namenode;
    var name;
    while(curnode){
	name = '';
	if(importantElements[curnode.tagName]){
	    namenode = dojo.dom.firstElement(curnode);
	    while(namenode){
		if(namenode.tagName == "Name"){
		    name = namenode.firstChild.data;
		    break;
		}
		namenode = dojo.dom.nextElement(namenode);
	    }
	    if(name != ''){
		strtitle = curnode.tagName + ': ' + name;
	    } else {
		strtitle = curnode.tagName;
	    }    
	    nodeid = dojo.dom.getUniqueId();
	    newnode = dojo.widget.createWidget("TreeNode", {
		    title:strtitle,
		    widgetId:nodeid,
		    object:curnode
		});
	    tree.addChild(newnode);
	    childnode = dojo.dom.firstElement(curnode);
	    if(childnode){
		buildTreeHelper(curnode, newnode);
	    }
	}
	curnode = dojo.dom.nextElement(curnode);
    }
}

// INIT CODE
// runs on SLD Loaded
function sldLoaded(){
    buildTree(xmlDoc);
    // Set the root node as the focusNode and select it in the tree
    var tree = dojo.widget.byId("firstTree");
    focusNode = tree.children[0];
    var message = {node:focusNode};
    unselectNode = focusNode;
    focusNode.markSelected();
    nodeSelectedHandler(message);
    alert("SLD Loaded.");        
}

// Runs on page load
function init(){
    
    // Init Save/Load
    var createSLDButton = dojo.widget.byId('createSLDButton');
    dojo.event.connect(createSLDButton, 'onClick', 'createSLD');

    var loadFileButton = dojo.widget.byId('loadFileButton');
    dojo.event.connect(loadFileButton, 'onClick', 'loadXMLFromFile');
    
    var loadStringButton = dojo.widget.byId('loadStringButton');
    dojo.event.connect(loadStringButton, 'onClick', 'loadXMLFromString');
    
    var generateButton = dojo.widget.byId('generateButton');
    dojo.event.connect(generateButton, 'onClick', 'generateXML');
    
    var expandButton = dojo.widget.byId('expandButton');
    dojo.event.connect(expandButton, 'onClick', 'expandAll');

    var collapseButton = dojo.widget.byId('collapseButton');
    dojo.event.connect(collapseButton, 'onClick', 'collapseAll');
    
    var mainArea = dojo.widget.byId('mainArea');
    dojo.event.connect(mainArea, 'onShow', 'updateData');
    
    var applyButton = dojo.widget.byId('mainApplyButton');
    dojo.event.connect(applyButton, 'onClick', 'saveData');

    var revertButton = dojo.widget.byId('mainRevertButton');
    dojo.event.connect(revertButton, 'onClick', 'updateData');
    
    // initialize the elements we want to show in the tree and be able to select
    importantElements["StyledLayerDescriptor"] = true;
    importantElements["NamedLayer"] = true;
    importantElements["LayerFeatureConstraints"] = true;
    importantElements["UserStyle"] = true;
    importantElements["FeatureTypeStyle"] = true;
    importantElements["UserLayer"] = true;
    importantElements["Rule"] = true;
    importantElements["Graphic"] = true;
    importantElements["Filter"] = true;
    importantElements["Elsefilter"] = true;
    importantElements["LineSymbolizer"] = true;
    importantElements["PolygonSymbolizer"] = true;
    importantElements["PointSymbolizer"] = true;
    importantElements["TextSymbolizer"] = true;
    importantElements["RasterSymbolizer"] = true;
        
}

dojo.addOnLoad(init);
dojo.addOnLoad(function() {
	dojo.event.topic.subscribe("nodeSelected",
				   function(message) { nodeSelectedHandler(message);
				   });});



/* COMMENT
    <script type="text/javascript">
    // Sample Dojo Code
    function helloPressed()
{
    alert('You pressed the button');
}
var exampleObj = {
    counter: 0,
    foo: function(){
	alert ("foo");
	this.counter++;
    },
    bar: function(){
	var str = "bar " + this.counter;
	alert(str);
	this.counter++;
    }
};
function init()
{
    var helloButton = dojo.widget.byId('helloButton');
    dojo.event.connect(helloButton, 'onClick', 'helloPressed');
    dojo.event.connect(helloButton, 'onClick', exampleObj, 'foo');
    dojo.event.connect(exampleObj, "foo", exampleObj, "bar");
}
//dojo.addOnLoad(init);
</script>
*/