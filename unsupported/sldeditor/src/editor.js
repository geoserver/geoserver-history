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

function loadXMLFromString(){
    var text = document.getElementById('pastedXML').value;
    xmlDoc = dojo.dom.createDocumentFromText(text);
    sldLoaded();
}

function loadXMLFromFile(){
    var file = document.loadform.sldfile.value;
    alert(file);
    xmlDoc = dojo.dom.createDocument();
    xmlDoc.load(file);
    sldLoaded();
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
function nodeSelectedHandler(message){
    var str = "selected: \n" + message.node.title + "\n" + message.node;
    alert(str);
}

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
	if(curnode.childNodes.length == 1){
	    strtitle = curnode.tagName + ": " + curnode.textContent;
	} else {
	    strtitle = curnode.tagName;
	}
	nodeid = dojo.dom.getUniqueId();
	tree.addChild(dojo.widget.createWidget("TreeNode", {
		    title:strtitle,
			widgetId:nodeid
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