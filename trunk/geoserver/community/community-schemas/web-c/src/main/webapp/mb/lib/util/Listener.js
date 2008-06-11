function Listener(){
this.listeners=new Array();
this.values=new Array();
this.addListener=function(param,listener,target){
if(window.logger)logger.logEvent("addListener: "+param,this.id,target.id);
if(!this.listeners[param]){
this.listeners[param]=new Array();
}
this.removeListener(param,listener,target);
this.listeners[param].push(new Array(listener,target));
}
this.addFirstListener=function(param,listener,target){
if(window.logger)logger.logEvent("addFirstListener: "+param,this.id,target.id);
if(!this.listeners[param]){
this.listeners[param]=new Array();
}
this.removeListener(param,listener,target);
this.listeners[param].unshift(new Array(listener,target));
}
this.removeListener=function(param,listener,target){
if(this.listeners[param]){
for(var i=0;i<this.listeners[param].length;i++){
if(this.listeners[param][i][0]==listener&&this.listeners[param][i][1]==target){
for(var j=i;j<this.listeners[param].length-1;j++){
this.listeners[param][j]=this.listeners[param][j+1];
}
this.listeners[param].pop();
return;
}
}
}
}
this.callListeners=function(param,value){
if(this.listeners[param]){
var count=this.listeners[param].length;
for(var i=0;i<count;i++){
if(window.logger)logger.logEvent(param,this.id,this.listeners[param][i][1].id,value);
if(this.listeners[param][i][0]){
this.listeners[param][i][0](this.listeners[param][i][1],value);
}else{
alert("Listener error: param="+param+", target="+this.listeners[param][i][1].id+", callBackFunction="+this.listeners[param][i][0]);
}
}
}
}
this.setParam=function(param,value){
this.values[param]=value;
this.callListeners(param,value);
}
this.getParam=function(param){
return this.values[param];
}
}
