mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function WebServiceForm(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.formElements=new Object();
this.submitForm=function(){
this.webServiceForm=document.getElementById(this.formName);
var webServiceUrl=this.webServiceForm.action+"?";
for(var i=0;i<this.webServiceForm.elements.length;++i){
var element=this.webServiceForm.elements[i];
webServiceUrl+=element.name+"="+element.value+"&";
this.formElements[element.name]=element.value;
}
if(this.debug)alert(webServiceUrl);
var httpPayload=new Object();
httpPayload.method="get";
httpPayload.url=webServiceUrl;
this.targetModel.newRequest(this.targetModel,httpPayload);
}
this.handleKeyPress=function(event){
var keycode;
var target;
if(event){
keycode=event.which;
target=event.currentTarget;
}else{
keycode=window.event.keyCode;
target=window.event.srcElement.form;
}
if(keycode==13){target.parentWidget.submitForm();
return false;
}
}
this.postPaint=function(objRef){
objRef.webServiceForm=document.getElementById(objRef.formName);
objRef.webServiceForm.parentWidget=objRef;
objRef.webServiceForm.onkeypress=objRef.handleKeyPress;
}
this.formName="WebServiceForm_"+mbIds.getId();
this.stylesheet.setParameter("formName",this.formName);
this.prePaint=function(objRef){
for(var elementName in objRef.formElements){
objRef.stylesheet.setParameter(elementName,objRef.formElements[elementName]);
}
}
}
