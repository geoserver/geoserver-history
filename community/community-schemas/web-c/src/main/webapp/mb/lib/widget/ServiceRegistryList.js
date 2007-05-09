mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function ServiceRegistryList(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.submitForm=function(){
alert("submitForm");
var webServiceUrl=this.webServiceForm.action+"?";
for(var i=0;i<this.webServiceForm.elements.length;++i){
var element=this.webServiceForm.elements[i];
webServiceUrl+=element.name+"="+element.value+"&";
}
alert(webServiceUrl);
config.loadModel(this.targetModel.id,webServiceUrl);
}
this.postPaint=function(objRef){
objRef.webServiceForm=document.getElementById(objRef.formName);
}
this.formName="WebServiceForm_";this.stylesheet.setParameter("formName",this.formName);
}
