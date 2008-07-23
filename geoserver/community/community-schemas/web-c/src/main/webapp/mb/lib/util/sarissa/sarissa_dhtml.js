Sarissa.updateContentFromURI=function(sFromUrl,oTargetElement,xsltproc){
try{
oTargetElement.style.cursor="wait";
var xmlhttp=new XMLHttpRequest();
xmlhttp.open("GET",sFromUrl);
function sarissa_dhtml_loadHandler(){
if(xmlhttp.readyState==4){
oTargetElement.style.cursor="auto";
Sarissa.updateContentFromNode(xmlhttp.responseXML,oTargetElement,xsltproc);
};
};
xmlhttp.onreadystatechange=sarissa_dhtml_loadHandler;
xmlhttp.send(null);
oTargetElement.style.cursor="auto";
}
catch(e){
oTargetElement.style.cursor="auto";
throw e;
};
};
Sarissa.updateContentFromNode=function(oNode,oTargetElement,xsltproc){
try{
oTargetElement.style.cursor="wait";
Sarissa.clearChildNodes(oTargetElement);
var ownerDoc=oNode.nodeType==Node.DOCUMENT_NODE?oNode:oNode.ownerDocument;
if(ownerDoc.parseError&&ownerDoc.parseError!=0){
var pre=document.createElement("pre");
pre.appendChild(document.createTextNode(Sarissa.getParseErrorText(ownerDoc)));
oTargetElement.appendChild(pre);
}
else{
if(xsltproc){
oNode=xsltproc.transformToDocument(oNode);
};
if(oTargetElement.tagName.toLowerCase=="textarea"||oTargetElement.tagName.toLowerCase=="input"){
oTargetElement.value=Sarissa.serialize(oNode);
}
else{
if(oNode.nodeType==Node.DOCUMENT_NODE||oNode.ownerDocument.documentElement==oNode){
oTargetElement.innerHTML=Sarissa.serialize(oNode);
}
else{
oTargetElement.appendChild(oTargetElement.ownerDocument.importNode(oNode,true));
};
}; 
};
}
catch(e){
throw e;
}
finally{
oTargetElement.style.cursor="auto";
};
};
