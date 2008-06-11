function ContextCollection(modelNode,parent){
ModelBase.apply(this,new Array(modelNode,parent));
this.insertContext=function(context,zindex){
}
this.deleteContext=function(id){
}
this.reorderContext=function(context,zindex){
}
this.selectContext=function(context,selected){
for(var i=0;i<this.listeners["select"].length;i++){
this.listeners["select"][i][0](context,this.listeners["select"][i][1]);
}
}
}
