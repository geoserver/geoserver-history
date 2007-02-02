if(!Sarissa.IS_ENABLED_TRANSFORM_NODE&&window.XSLTProcessor){
Element.prototype.transformNodeToObject=function(xslDoc,oResult){
var oDoc=document.implementation.createDocument("","",null);
Sarissa.copyChildNodes(this,oDoc);
oDoc.transformNodeToObject(xslDoc,oResult);
};
Document.prototype.transformNodeToObject=function(xslDoc,oResult){
var xsltProcessor=null;
try{
xsltProcessor=new XSLTProcessor();
if(xsltProcessor.reset){
xsltProcessor.importStylesheet(xslDoc);
var newFragment=xsltProcessor.transformToFragment(this,oResult);
Sarissa.copyChildNodes(newFragment,oResult);
}else{
xsltProcessor.transformDocument(this,xslDoc,oResult,null);
};
}catch(e){
if(xslDoc&&oResult)
throw "Failed to transform document. (original exception: "+e+")";
else if(!xslDoc)
throw "No Stylesheet Document was provided. (original exception: "+e+")";
else if(!oResult)
throw "No Result Document was provided. (original exception: "+e+")";
else if(xsltProcessor==null)
throw "Could not instantiate an XSLTProcessor object. (original exception: "+e+")";
else
throw e;
};
};
Element.prototype.transformNode=function(xslDoc){
var oDoc=document.implementation.createDocument("","",null);
Sarissa.copyChildNodes(this,oDoc);
return oDoc.transformNode(xslDoc);
};
Document.prototype.transformNode=function(xslDoc){
var out=document.implementation.createDocument("","",null);
this.transformNodeToObject(xslDoc,out);
var str=null;
try{
var serializer=new XMLSerializer();
str=serializer.serializeToString(out);
}catch(e){
throw "Failed to serialize result document. (original exception: "+e+")";
};
return str;
};
Sarissa.IS_ENABLED_TRANSFORM_NODE=true;
};
Sarissa.setXslParameter=function(oXslDoc,sParamQName,sParamValue){
try{
var params=oXslDoc.getElementsByTagName(_SARISSA_IEPREFIX4XSLPARAM+"param");
var iLength=params.length;
var bFound=false;
var param;
if(sParamValue){
for(var i=0;i<iLength&&!bFound;i++){
if(params[i].getAttribute("name")==sParamQName){
param=params[i];
while(param.firstChild)
param.removeChild(param.firstChild);
if(!sParamValue||sParamValue==null){
}else if(typeof sParamValue=="string"){ 
param.setAttribute("select",sParamValue);
bFound=true;
}else if(sParamValue.nodeName){
param.removeAttribute("select");
param.appendChild(sParamValue.cloneNode(true));
bFound=true;
}else if(sParamValue.item(0)&&sParamValue.item(0).nodeType){
for(var j=0;j<sParamValue.length;j++)
if(sParamValue.item(j).nodeType)
param.appendChild(sParamValue.item(j).cloneNode(true));
bFound=true;
}else
throw "Failed to set xsl:param "+sParamQName+" (original exception: "+e+")";
};
};
};
return bFound;
}catch(e){
throw e;
return false;
};
};
