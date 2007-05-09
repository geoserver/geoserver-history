var MB_IS_MOZ=(document.implementation&&document.implementation.createDocument)?true:false;
function XslProcessor(xslUrl,docNSUri){
this.xslUrl=xslUrl;
this.xslDom=Sarissa.getDomDocument();
this.xslDom.async=false;
this.xslDom.validateOnParse=false;this.xslDom.load(xslUrl);
if(this.xslDom.parseError<0)
alert("error loading XSL stylesheet: "+xslUrl);
this.processor=new XSLTProcessor();
this.processor.importStylesheet(this.xslDom);
this.docNSUri=docNSUri;
this.transformNodeToString=function(xmlNode){
try{
var newDoc=this.transformNodeToObject(xmlNode);
var s=Sarissa.serialize(newDoc);
return Sarissa.unescape(s);
}catch(e){
alert("Exception transforming doc with XSL: "+this.xslUrl);
alert("XSL="+Sarissa.serialize(this.xslDom));
alert("XML="+Sarissa.serialize(xmlNode));
}
}
this.transformNodeToObject=function(xmlNode){
var newFragment=this.processor.transformToDocument(xmlNode);
return newFragment;
}
this.setParameter=function(paramName,paramValue,nsUri){
if(!paramValue){
return;
}
this.processor.setParameter(null,paramName,paramValue);
}
}
function postLoad(sUri,docToSend,contentType){
var xmlHttp=new XMLHttpRequest();
if(sUri.indexOf("http://")==0){
xmlHttp.open("POST",config.proxyUrl,false);
xmlHttp.setRequestHeader("serverUrl",sUri);}else{
xmlHttp.open("POST",sUri,false);
}
xmlHttp.setRequestHeader("content-type","text/xml");
if(contentType)xmlHttp.setRequestHeader("content-type",contentType);
xmlHttp.send(docToSend);
if(xmlHttp.status>=400){alert("error loading document: "+sUri+" - "+xmlHttp.statusText+"-"+xmlHttp.responseText);
var outDoc=Sarissa.getDomDocument();
outDoc.parseError=-1;
return outDoc;
}else{
if(null==xmlHttp.responseXML)alert("null XML response:"+xmlHttp.responseText);
return xmlHttp.responseXML;
}
}
function getProxyPlusUrl(url){
if(url.indexOf("http://")==0){
if(config.proxyUrl){
url=config.proxyUrl+"?url="+escape(url).replace(/\+/g,'%2C').replace(/\"/g,'%22').replace(/\'/g,'%27');
}else{
alert("unable to load external document:"+url+"  Set the proxyUrl property in config.");
url=null;
}
}
return url;
}
function createElementWithNS(doc,name,nsUri){
if(_SARISSA_IS_IE){
var newElement=doc.createElement(name);
return newElement;
}else{
return doc.createElementNS(nsUri,name);
}
}
function UniqueId(){
this.lastId=0;
this.getId=function(){
this.lastId++;
return this.lastId;
}
}
var mbIds=new UniqueId();
function setISODate(isoDateStr){
var parts=isoDateStr.match(/(\d{4})-?(\d{2})?-?(\d{2})?T?(\d{2})?:?(\d{2})?:?(\d{2})?\.?(\d{0,3})?(Z)?/);
var res=null;
for(var i=1;i<parts.length;++i){
if(!parts[i]){
parts[i]=(i==3)?1:0;if(!res)res=i;
}
}
var isoDate=new Date();
isoDate.setFullYear(parseInt(parts[1],10));
isoDate.setMonth(parseInt(parts[2]-1,10));
isoDate.setDate(parseInt(parts[3],10));
isoDate.setHours(parseInt(parts[4],10));
isoDate.setMinutes(parseInt(parts[5],10));
isoDate.setSeconds(parseFloat(parts[6],10));
if(!res)res=6;
isoDate.res=res;
return isoDate;
}
function getISODate(isoDate){
var res=isoDate.res?isoDate.res:6;
var dateStr="";
dateStr+=res>1?isoDate.getFullYear():"";
dateStr+=res>2?"-"+leadingZeros(isoDate.getMonth()+1,2):"";
dateStr+=res>3?"-"+leadingZeros(isoDate.getDate(),2):"";
dateStr+=res>4?"T"+leadingZeros(isoDate.getHours(),2):"";
dateStr+=res>5?":"+leadingZeros(isoDate.getMinutes(),2):"";
dateStr+=res>6?":"+leadingZeros(isoDate.getSeconds(),2):"";
return dateStr;
}
function leadingZeros(num,digits){
var intNum=parseInt(num,10);
var base=Math.pow(10,digits);
if(intNum<base)intNum+=base;
return intNum.toString().substr(1);
}
function fixPNG(myImage,myId){
if(_SARISSA_IS_IE){
var imgID="id='"+myId+"' ";
var imgClass=(myImage.className)?"class='"+myImage.className+"' ":""
var imgTitle=(myImage.title)?"title='"+myImage.title+"' ":"title='"+myImage.alt+"' "
var imgStyle="display:inline-block;"+myImage.style.cssText 
var strNewHTML="<span "+imgID+imgClass+imgTitle
strNewHTML+=" style=\""+"width:"+myImage.width+"px; height:"+myImage.height+"px;"+imgStyle+";"
var src=myImage.src;
src=src.replace(/\(/g,'%28');
src=src.replace(/\)/g,'%29');
src=src.replace(/'/g,'%27');
src=src.replace(/%23/g,'%2523');
strNewHTML+="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader";
strNewHTML+="(src=\'"+src+"\', sizingMethod='scale'); \"></span>";
return strNewHTML;
}
}
function getAbsX(elt){
return(elt.x)?elt.x:getAbsPos(elt,"Left")+2;
}
function getAbsY(elt){
return(elt.y)?elt.y:getAbsPos(elt,"Top")+2;
}
function getAbsPos(elt,which){
iPos=0;
while(elt!=null){
iPos+=elt["offset"+which];
elt=elt.offsetParent;
}
return iPos;
}
function getPageX(e){
var posx=0;
if(!e)var e=window.event;
if(e.pageX){
posx=e.pageX;
}
else if(e.clientX){
posx=e.clientX;
}
if(document.body&&document.body.scrollLeft){
posx+=document.body.scrollLeft;
}
else if(document.documentElement&&document.documentElement.scrollLeft){
posx+=document.documentElement.scrollLeft;
}
return posx;
}
function getPageY(e){
var posy=0;
if(!e)var e=window.event;
if(e.pageY){
posy=e.pageY;
}
else if(e.clientY){
posy=e.clientY;
}
if(document.body&&document.body.scrollTop){
posy+=document.body.scrollTop;
}
else if(document.documentElement&&document.documentElement.scrollTop){
posy+=document.documentElement.scrollTop;
}
return posy;
}
function getArgs(){
var args=new Object();
var query=location.search.substring(1);
var pairs=query.split("&");
for(var i=0;i<pairs.length;i++){
var pos=pairs[i].indexOf('=');
if(pos==-1)continue;
var argname=pairs[i].substring(0,pos);
var value=pairs[i].substring(pos+1);
args[argname]=unescape(value.replace(/\+/g," "));
}
return args;
}
window.cgiArgs=getArgs();
function getScreenX(context,xCoord){
bbox=context.getBoundingBox();
width=context.getWindowWidth();
bbox[0]=parseFloat(bbox[0]);
bbox[2]=parseFloat(bbox[2]);
var xfac=(width/(bbox[2]-bbox[0]));
x=xfac*(xCoord-bbox[0]);
return x;
}
function getScreenY(context,yCoord){
var bbox=context.getBoundingBox();
var height=context.getWindowHeight();
bbox[1]=parseFloat(bbox[1]);
bbox[3]=parseFloat(bbox[3]);
var yfac=(heighteight/(bbox[3]-bbox[1]));
var y=height-(yfac*(pt.y-bbox[1]));
return y;
}
function getGeoCoordX(context,xCooord){
var bbox=context.getBoundingBox();
var width=context.getWindowWidth();
bbox[0]=parseFloat(bbox[0]);
bbox[2]=parseFloat(bbox[2]);
var xfac=((bbox[2]-bbox[0])/width);
var x=bbox[0]+xfac*(xCoord);
return x;
}
function getGeoCoordY(yCoord){
var bbox=context.getBoundingBox();
var height=context.getWindowHeight();
bbox[1]=parseFloat(bbox[1]);
bbox[3]=parseFloat(bbox[3]);
var yfac=((bbox[3]-bbox[1])/height);
var y=bbox[1]+yfac*(height-yCoord);
return y;
}
function makeElt(type){
var node=document.createElement(type);
document.getElementsByTagName("body").item(0).appendChild(node);
return node;
}
var newWindow='';
function openPopup(url,width,height){
if(width==null){
width=300;
}
if(height==null){
height=200;
}
if(!newWindow.closed&&newWindow.location){
newWindow.location.href=url;
}
else{
newWindow=window.open(url,'name','height='+height+',width='+width);
if(!newWindow.opener)newwindow.opener=self;
}
if(window.focus){newWindow.focus()}
return false;
}
function debug(output){
tarea=makeElt("textarea");
tarea.setAttribute("rows","3");
tarea.setAttribute("cols","40");
tnode=document.createTextNode(output);
tarea.appendChild(tnode);
}
function returnTarget(evt){
evt=(evt)?evt:((window.event)?window.event:"");
var elt=null;
if(evt.target){
elt=evt.target;
}
else if(evt.srcElement){
elt=evt.srcElement;
}
return elt;
}
function addEvent(elementObject,eventName,functionObject){
if(document.addEventListener){
elementObject.addEventListener(eventName,functionObject,false);
}
else if(document.attachEvent){
elementObject.attachEvent("on"+eventName,functionObject);
}
}
function handleEventWithObject(evt){
var elt=returnTarget(evt);
var obj=elt.ownerObj;
if(obj!=null)obj.handleEvent(evt);
}
