mapbuilder.loadScript(baseDir+"/widget/WidgetBaseXSL.js");
function TabbedContent(widgetNode,model){
WidgetBaseXSL.apply(this,new Array(widgetNode,model));
this.selectedTab=null;
var textNodeXpath="/mb:WidgetText/mb:widgets/mb:TabbedContent";
this.tabLabels=config.widgetText.selectNodes(textNodeXpath);
var workspace=widgetNode.selectSingleNode("mb:htmlWorkspace");
if(workspace){
this.htmlWorkspace=workspace.firstChild.nodeValue;
}else{
alert("htmlWorkspace must be defined for TabbedContent widget");
}
this.initTabs=function(objRef){
var tabs=objRef.widgetNode.selectNodes("mb:tab");
for(var i=0;i<tabs.length;++i){
var tab=tabs[i];
var tabWidgetId=tab.firstChild.nodeValue;
var tabWidget=config.objects[tabWidgetId];
if(!tabWidget){
alert("tab widget not found:"+tabWidgetId);
return;
}
tabWidget.htmlTagId=objRef.htmlWorkspace;
tabWidget.outputNodeId=objRef.id+"_workspace";
tabWidget.node=document.getElementById(tabWidget.htmlTagId);
tabWidget.tabList=objRef;
var tabLabel=tab.getAttribute("label"); 
if(!tabLabel)tabLabel=tabWidgetId;
var textNode=config.widgetText.selectSingleNode(textNodeXpath+"/mb:"+tabWidgetId);
if(textNode)tabLabel=textNode.firstChild.nodeValue;
tab.setAttribute("label",tabLabel);
tabWidget.model.addListener("refresh",objRef.selectTab,tabWidget);
}
}
this.model.addListener("init",this.initTabs,this);
this.addWidget=function(tabWidget,tabLabel){
tabWidget.htmlTagId=this.htmlWorkspace;
tabWidget.outputNodeId=this.id+"_workspace";
tabWidget.node=document.getElementById(tabWidget.htmlTagId);
tabWidget.tabList=this;
if(!tabLabel)tabLabel=tabWidget.id;
var textNode=config.widgetText.selectSingleNode(textNodeXpath+"/mb:"+tabWidget.id);
if(textNode)tabLabel=textNode.firstChild.nodeValue;
var tabNode=this.model.doc.createElementNS(mbNS,"tab");
tabNode.appendChild(this.model.doc.createTextNode(tabWidget.id));
tabNode.setAttribute("label",tabLabel);
this.widgetNode.appendChild(tabNode);
this.paint(this);
this.selectTab(tabWidget);
}
this.selectTab=function(tabWidget){
if(!tabWidget.model.doc){
alert("no data to show yet");
return;
}
var tabList=tabWidget.tabList;
if(tabList.selectedTab!=null)tabList.selectedTab.className='';
var newAnchor=document.getElementById(tabList.id+"_"+tabWidget.id);
if(newAnchor){
newAnchor.className='current';
tabList.selectedTab=newAnchor;
tabWidget.paint(tabWidget,true);
}
}
this.prePaint=function(objRef){
objRef.resultDoc=objRef.widgetNode;
}
}
