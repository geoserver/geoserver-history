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
	    
	    break
	case 'LayerFeatureConstraints':
	    
	    break
	case 'UserStyle':
	    
	    break
	case 'FeatureTypeStyle':
	    
	    break
	case 'UserLayer':
	    
	    break
	case 'Rule':
	    
	    break
	case 'Graphic':
	    
	    break
	case 'Filter':
	    
	    break
	case 'Elsefilter':
	    
	    break
	case 'LineSymbolizer':
	    
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
	    
	    break
    case 'LayerFeatureConstraints':
	    
	    break
    case 'UserStyle':
	    
	    break
    case 'FeatureTypeStyle':
	    
	    break
    case 'UserLayer':
	    
	    break
    case 'Rule':
	    
	    break
    case 'Graphic':
	    
	    break
    case 'Filter':
	    
	    break
    case 'Elsefilter':
	    
	    break
    case 'LineSymbolizer':
	    
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
		focusNode.edit(FocusNode);
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
	//insert it as the second element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
    }
    
    // add an Abstract element
    if(isAbstract == 0 && abst.value != ''){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	newel = xmlDoc.createElement('Abstract');
	newel.appendChild(xmlDoc.createTextNode(abst.value));
	//insert it as the third element
	focusNode.object.insertBefore(newel, locnode);
	locnode = newel;
	focusNode.object.appendChild(newel);
    }
    
    // if the NL box is checked but there is no NL element
    if(isNL == 0 && NL.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
        newel = xmlDoc.createElement('NamedLayer');
	//insert it as the second element
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
    }
    // same for UL
    if(isUL == 0 && UL.checked == 1){
	//increment the position counter
	locnode = dojo.dom.nextElement(locnode);   
	//create one
	newel = xmlDoc.createElement('UserLayer');
	//insert it as the second element
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