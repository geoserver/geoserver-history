var _SARISSA_HAS_DOM_IMPLEMENTATION=document.implementation&&true;
var _SARISSA_HAS_DOM_FEATURE=_SARISSA_HAS_DOM_IMPLEMENTATION&&document.implementation.hasFeature;
if(_SARISSA_HAS_DOM_FEATURE&&document.implementation.hasFeature("XPath","3.0")){
function SarissaNodeList(i){
this.length=i;
};
SarissaNodeList.prototype=new Array(0);
SarissaNodeList.prototype.constructor=Array;
SarissaNodeList.prototype.item=function(i){
return(i<0||i>=this.length)?null:this[i];
};
SarissaNodeList.prototype.expr="";
XMLDocument.prototype.setProperty=function(x,y){};
Sarissa.setXpathNamespaces=function(oDoc,sNsSet){
oDoc._sarissa_useCustomResolver=true;
var namespaces=sNsSet.indexOf(" ")>-1?sNsSet.split(" "):new Array(sNsSet);
oDoc._sarissa_xpathNamespaces=new Array(namespaces.length);
for(var i=0;i<namespaces.length;i++){
var ns=namespaces[i];
var colonPos=ns.indexOf(":");
var assignPos=ns.indexOf("=");
if(colonPos==5&&assignPos>colonPos+2){
var prefix=ns.substring(colonPos+1,assignPos);
var uri=ns.substring(assignPos+2,ns.length-1);
oDoc._sarissa_xpathNamespaces[prefix]=uri;
}else{
throw "Bad format on namespace declaration(s) given";
};
};
};
XMLDocument.prototype._sarissa_useCustomResolver=false;
XMLDocument.prototype._sarissa_xpathNamespaces=new Array();
XMLDocument.prototype.selectNodes=function(sExpr,contextNode){
var nsDoc=this;
var nsresolver=this._sarissa_useCustomResolver
?function(prefix){
var s=nsDoc._sarissa_xpathNamespaces[prefix];
if(s)return s;
else throw "No namespace URI found for prefix: '"+prefix+"'";
}
:this.createNSResolver(this.documentElement);
var oResult=this.evaluate(sExpr,
(contextNode?contextNode:this),
nsresolver,
XPathResult.ORDERED_NODE_SNAPSHOT_TYPE,null);
var nodeList=new SarissaNodeList(oResult.snapshotLength);
nodeList.expr=sExpr;
for(var i=0;i<nodeList.length;i++)
nodeList[i]=oResult.snapshotItem(i);
return nodeList;
};
Element.prototype.selectNodes=function(sExpr){
var doc=this.ownerDocument;
if(doc.selectNodes)
return doc.selectNodes(sExpr,this);
else
throw "Method selectNodes is only supported by XML Elements";
};
XMLDocument.prototype.selectSingleNode=function(sExpr,contextNode){
var ctx=contextNode?contextNode:null;
sExpr="("+sExpr+")[1]";
var nodeList=this.selectNodes(sExpr,ctx);
if(nodeList.length>0)
return nodeList.item(0);
else
return null;
};
Element.prototype.selectSingleNode=function(sExpr){
var doc=this.ownerDocument;
if(doc.selectSingleNode)
return doc.selectSingleNode(sExpr,this);
else
throw "Method selectNodes is only supported by XML Elements";
};
Sarissa.IS_ENABLED_SELECT_NODES=true;
};
