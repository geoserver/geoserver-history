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

// holds the SLD being edited
var xmlDoc;

// Holds the current nodes
var focusNode = '';
var unselectNode = '';

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
	xmlDoc = dojo.dom.createDocument();
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
    expandAllHelper(dojo.widget.byId('firstTree').children[0]);
}

function expandAllHelper(treeNode){
    treeNode.expand();
    for(var i=0; i<treeNode.children.length; i++){
	expandAllHelper(treeNode.children[i]);
    }
}

function collapseAll(){
    collapseAllHelper(dojo.widget.byId('firstTree').children[0]);
}

function collapseAllHelper(treeNode){
    treeNode.collapse();
    for(var i=0; i<treeNode.children.length; i++){
	collapseAllHelper(treeNode.children[i]);
    }
}

function nodeSelectedHandler(message){
    var str = "selected: \n" + message.node.title + "\n" + message.node + "\n" + message.node.object;
    var node = message.node;
    
    // if we didn't select the same node again
    if(unselectNode && unselectNode != message.node){
	// take care of the unselect node
	unselectNode.unMarkSelected();
	unselectNode='';
    }
    
    // if this is an attribute node
    if(node.children.length == 0){
	//unselect it
	node.unMarkSelected();
	//set its parent as the focus
	focusNode = node.parent;
	//select it in the tree
	unselectNode = focusNode;
	focusNode.markSelected();
    } else {
	//otherwise, set it as the focus
	focusNode = node;
    }
    
    // set the Current node label
    document.getElementById('mainCurrentArea').innerHTML = focusNode.title;
    
    // update the tab list
	updateTabs();

}

function updateTabs(){
    // eventual flow for this function:
    //
    // get the tab container object
    // kill its children if we did not select the same node
    // update the tab container with the appropriate children for the focusNode
    // 
    if(dojo.widget.byId('tabMenu').selectedChild=="mainArea" && focusNode){
	var tabs = dojo.widget.byId('tabMain');
	tabs.destroyChildren();
	var tab = '';
	var id = '';	
	tab = new dojo.widget.createWidget("ContentPane", {widgetId:id, label:"Parent Attributes", refreshOnShow:"true"});
	var tmpContent = document.getElementById(focusNode.title);
	tab.setContent(tmpContent);
	tabs.addChild(tab);
	for (var i=0; i<focusNode.children.length; i++){
	    id = dojo.dom.getUniqueId();
	    tab = new dojo.widget.createWidget("ContentPane", {widgetId:id, label:focusNode.children[i].title, refreshOnShow:"true"});
	    tab.setContent("This is the tab for " + focusNode.children[i].title);
	    tabs.addChild(tab);
	}
    }
}

// consider only building tree levels that are expanded
function buildTree(node, tree){
    var tree = dojo.widget.byId("firstTree");
    tree.destroyChildren();
    buildTreeHelper(node, tree);
}

function buildTreeHelper(node, tree){
    var curnode = dojo.dom.firstElement(node);
    var strtitle;
    var nodeid;
    var childnode;
    while(curnode){
	//if(curnode.childNodes.length == 1){
	//    strtitle = curnode.tagName + ": " + curnode.textContent;
	//} else {
	strtitle = curnode.tagName;
	//}
	nodeid = dojo.dom.getUniqueId();
	tree.addChild(dojo.widget.createWidget("TreeNode", {
		    title:strtitle,
			widgetId:nodeid,
			object:curnode
			}));
	childnode = dojo.dom.firstElement(curnode);
	if(childnode){
	    buildTreeHelper(curnode, dojo.widget.byId(nodeid));
	}
	curnode = dojo.dom.nextElement(curnode);
    }
}

// INIT CODE
// runs on SLD Loaded
function sldLoaded()
{
    alert("SLD Loaded.");
    buildTree(xmlDoc);
    // Set the root node as the focusNode and select it in the tree
    var tree = dojo.widget.byId("firstTree");
    focusNode = tree.children[0];
    var message = {node:focusNode};
    unselectNode = focusNode;
    focusNode.markSelected();
    nodeSelectedHandler(message);
    
}

// Runs on page load
function init()
{
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
    dojo.event.connect(mainArea, 'onShow', 'updateTabs');
    
}
dojo.addOnLoad(init);
dojo.addOnLoad(function() {
	dojo.event.topic.subscribe("nodeSelected",
				   function(message) { nodeSelectedHandler(message); }
				   );
    });


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