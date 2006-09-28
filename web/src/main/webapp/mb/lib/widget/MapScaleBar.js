mapbuilder.loadScript(baseDir+"/widget/WidgetBase.js");
function MapScaleBar(widgetNode,model){
WidgetBase.apply(this,new Array(widgetNode,model));
this.scaleDenominator=1; 
this.displaySystem='metric';var displaySystem=widgetNode.selectSingleNode("mb:displaySystem");
if(displaySystem)this.displaySystem=displaySystem.firstChild.nodeValue;
this.minWidth=100;var minWidth=widgetNode.selectSingleNode("mb:minWidth");
if(minWidth)this.minWidth=minWidth.firstChild.nodeValue;
this.maxWidth=200;var maxWidth=widgetNode.selectSingleNode("mb:maxWidth");
if(maxWidth)this.maxWidth=maxWidth.firstChild.nodeValue;
this.divisions=2;
var divisions=widgetNode.selectSingleNode("mb:divisions");
if(divisions)this.divisions=divisions.firstChild.nodeValue;
this.subdivisions=2;
var subdivisions=widgetNode.selectSingleNode("mb:subdivisions");
if(subdivisions)this.subdivisions=subdivisions.firstChild.nodeValue;
this.showMinorMeasures=false;
var showMinorMeasures=widgetNode.selectSingleNode("mb:showMinorMeasures");
if(showMinorMeasures&&showMinorMeasures.firstChild.nodeValue=="true")this.showMinorMeasures=true;
this.abbreviateLabel=false;
var abbreviateLabel=widgetNode.selectSingleNode("mb:abbreviateLabel");
if(abbreviateLabel&&abbreviateLabel.firstChild.nodeValue=="true")this.abbreviateLabel=true;
this.singleLine=false;
var singleLine=widgetNode.selectSingleNode("mb:singleLine");
if(singleLine&&singleLine.firstChild.nodeValue=="true")this.singleLine=true;
this.align='center';var align=widgetNode.selectSingleNode("mb:align");
if(align)this.align=align.firstChild.nodeValue;
this.resolution=72; 
this.container=document.createElement('div');
this.container.className='sbWrapper';
this.container.style.position='relative';
this.container.setAttribute("id",this.outputNodeId);
this.labelContainer=document.createElement('div');
this.labelContainer.className='sbUnitsContainer';
this.labelContainer.style.position='absolute';
this.graphicsContainer=document.createElement('div');
this.graphicsContainer.style.position='absolute';
this.graphicsContainer.className='sbGraphicsContainer';
this.numbersContainer=document.createElement('div');
this.numbersContainer.style.position='absolute';
this.numbersContainer.className='sbNumbersContainer';
var markerMajor=document.createElement('div');
markerMajor.className='sbMarkerMajor';
this.graphicsContainer.appendChild(markerMajor);
var markerMinor=document.createElement('div');
markerMinor.className='sbMarkerMinor';
this.graphicsContainer.appendChild(markerMinor);
var barPiece=document.createElement('div');
barPiece.className='sbBar';
this.graphicsContainer.appendChild(barPiece);
var barPieceAlt=document.createElement('div');
barPieceAlt.className='sbBarAlt';
this.graphicsContainer.appendChild(barPieceAlt);
this.model.addListener("bbox",this.update,this);
this.model.addListener("refresh",this.update,this);
}
MapScaleBar.prototype.update=function(objRef){
var outputNode=document.getElementById(objRef.outputNodeId);
if(!outputNode)objRef.node.appendChild(objRef.container);
var scaleDenominator=objRef.model.extent.getScale()
if(scaleDenominator!=null){
objRef.scaleDenominator=scaleDenominator;
}
function HandsomeNumber(smallUglyNumber,bigUglyNumber,sigFigs){
var sigFigs=(sigFigs==null)?10:sigFigs;
var bestScore=Number.POSITIVE_INFINITY;
var bestTieBreaker=Number.POSITIVE_INFINITY;
var handsomeValue=smallUglyNumber;
var handsomeNumDec=3;
for(var halvingExp=0;halvingExp<3;++halvingExp){
var comelyMultiplicand=Math.pow(2,(-1*halvingExp));
var maxTensExp=Math.floor(Math.log(bigUglyNumber/comelyMultiplicand)/Math.LN10)
for(var tensExp=maxTensExp;tensExp>(maxTensExp-sigFigs+1);--tensExp){
var numDec=Math.max(halvingExp-tensExp,0);
var testMultiplicand=comelyMultiplicand*Math.pow(10,tensExp);
if((testMultiplicand*Math.floor(bigUglyNumber/testMultiplicand))>=smallUglyNumber){
if(smallUglyNumber%testMultiplicand==0){
var testMultiplier=smallUglyNumber/testMultiplicand;
}
else{
var testMultiplier=Math.floor(smallUglyNumber/testMultiplicand)+1;
}
var testScore=testMultiplier+(2*halvingExp);
var testTieBreaker=(tensExp<0)?(Math.abs(tensExp)+1):tensExp;
if((testScore<bestScore)||((testScore==bestScore)&&(testTieBreaker<bestTieBreaker))){
bestScore=testScore;
bestTieBreaker=testTieBreaker;
handsomeValue=(testMultiplicand*testMultiplier).toFixed(numDec);
handsomeNumDec=numDec;
}
}
}
}
this.value=handsomeValue;
this.score=bestScore;
this.tieBreaker=bestTieBreaker;
this.numDec=handsomeNumDec;
}
HandsomeNumber.prototype.toString=function(){
return this.value.toString();
}
HandsomeNumber.prototype.valueOf=function(){
return this.value;
}
function styleValue(aSelector,styleKey){
var aValue=0;
if(document.styleSheets){
for(var sheetIndex=document.styleSheets.length-1;sheetIndex>=0;--sheetIndex){
var aSheet=document.styleSheets[sheetIndex];
if(!aSheet.disabled){
var allRules;
if(typeof(aSheet.cssRules)=='undefined'){
if(typeof(aSheet.rules)=='undefined'){
return 0;
}
else{
allRules=aSheet.rules;
}
}
else{
allRules=aSheet.cssRules;
}
for(var ruleIndex=0;ruleIndex<allRules.length;++ruleIndex){
var aRule=allRules[ruleIndex];
if(aRule.selectorText&&(aRule.selectorText.toLowerCase()==aSelector.toLowerCase())){
if(aRule.style[styleKey]!=''){
aValue=parseInt(aRule.style[styleKey]);
}
}
}
}
}
}
return aValue?aValue:0;
}
function formatNumber(aNumber,numDecimals){
numDecimals=(numDecimals)?numDecimals:0;
var formattedInteger=''+Math.round(aNumber);
var thousandsPattern=/(-?[0-9]+)([0-9]{3})/;
while(thousandsPattern.test(formattedInteger)){
formattedInteger=formattedInteger.replace(thousandsPattern,'$1,$2');
}
if(numDecimals>0){
var formattedDecimal=Math.floor(Math.pow(10,numDecimals)*(aNumber-Math.round(aNumber)));
if(formattedDecimal==0){
return formattedInteger;
}
else{
return formattedInteger+'.'+formattedDecimal;
}
}
else{
return formattedInteger;
}
}
objRef.container.title='scale 1:'+formatNumber(objRef.scaleDenominator);
var measurementProperties=new Object();
measurementProperties.english={
units:['miles','feet','inches'],
abbr:['mi','ft','in'],
inches:[63360,12,1]
}
measurementProperties.metric={
units:['kilometers','meters','centimeters'],
abbr:['km','m','cm'],
inches:[39370.07874,39.370079,0.393701]
}
var comparisonArray=new Array();
for(var unitIndex=0;unitIndex<measurementProperties[objRef.displaySystem].units.length;++unitIndex){
comparisonArray[unitIndex]=new Object();
var pixelsPerDisplayUnit=objRef.resolution*measurementProperties[objRef.displaySystem].inches[unitIndex]/objRef.scaleDenominator;
var minSDDisplayLength=(objRef.minWidth/pixelsPerDisplayUnit)/(objRef.divisions*objRef.subdivisions);
var maxSDDisplayLength=(objRef.maxWidth/pixelsPerDisplayUnit)/(objRef.divisions*objRef.subdivisions);
for(var valueIndex=0;valueIndex<(objRef.divisions*objRef.subdivisions);++valueIndex){
var minNumber=minSDDisplayLength*(valueIndex+1);
var maxNumber=maxSDDisplayLength*(valueIndex+1);
var niceNumber=new HandsomeNumber(minNumber,maxNumber);
comparisonArray[unitIndex][valueIndex]={value:(niceNumber.value/(valueIndex+1)),score:0,tieBreaker:0,numDec:0,displayed:0};
for(var valueIndex2=0;valueIndex2<(objRef.divisions*objRef.subdivisions);++valueIndex2){
displayedValuePosition=niceNumber.value*(valueIndex2+1)/(valueIndex+1);
niceNumber2=new HandsomeNumber(displayedValuePosition,displayedValuePosition);
var isMajorMeasurement=((valueIndex2+1)%objRef.subdivisions==0);
var isLastMeasurement=((valueIndex2+1)==(objRef.divisions*objRef.subdivisions));
if((objRef.singleLine&&isLastMeasurement)||(!objRef.singleLine&&(isMajorMeasurement||objRef.showMinorMeasures))){
comparisonArray[unitIndex][valueIndex].score+=niceNumber2.score;
comparisonArray[unitIndex][valueIndex].tieBreaker+=niceNumber2.tieBreaker;
comparisonArray[unitIndex][valueIndex].numDec=Math.max(comparisonArray[unitIndex][valueIndex].numDec,niceNumber2.numDec);
comparisonArray[unitIndex][valueIndex].displayed+=1;
}
else{
comparisonArray[unitIndex][valueIndex].score+=niceNumber2.score/objRef.subdivisions;
comparisonArray[unitIndex][valueIndex].tieBreaker+=niceNumber2.tieBreaker/objRef.subdivisions;
}
}
var scoreAdjustment=(unitIndex+1)*comparisonArray[unitIndex][valueIndex].tieBreaker/comparisonArray[unitIndex][valueIndex].displayed;
comparisonArray[unitIndex][valueIndex].score*=scoreAdjustment;
}
}
var subdivisionDisplayLength=null;
var displayUnits=null;
var displayUnitsAbbr=null;
var subdivisionPixelLength=null;
var bestScore=Number.POSITIVE_INFINITY;
var bestTieBreaker=Number.POSITIVE_INFINITY;
var numDec=0;
for(var unitIndex=0;unitIndex<comparisonArray.length;++unitIndex){
for(valueIndex in comparisonArray[unitIndex]){
if((comparisonArray[unitIndex][valueIndex].score<bestScore)||((comparisonArray[unitIndex][valueIndex].score==bestScore)&&(comparisonArray[unitIndex][valueIndex].tieBreaker<bestTieBreaker))){
bestScore=comparisonArray[unitIndex][valueIndex].score;
bestTieBreaker=comparisonArray[unitIndex][valueIndex].tieBreaker;
subdivisionDisplayLength=comparisonArray[unitIndex][valueIndex].value;
numDec=comparisonArray[unitIndex][valueIndex].numDec;
displayUnits=measurementProperties[objRef.displaySystem].units[unitIndex];
displayUnitsAbbr=measurementProperties[objRef.displaySystem].abbr[unitIndex];
pixelsPerDisplayUnit=objRef.resolution*measurementProperties[objRef.displaySystem].inches[unitIndex]/objRef.scaleDenominator;
subdivisionPixelLength=pixelsPerDisplayUnit*subdivisionDisplayLength;}
}
}
var xOffsetMarkerMajor=(styleValue('.sbMarkerMajor','borderLeftWidth')+styleValue('.sbMarkerMajor','width')+styleValue('.sbMarkerMajor','borderRightWidth'))/2;
var xOffsetMarkerMinor=(styleValue('.sbMarkerMinor','borderLeftWidth')+styleValue('.sbMarkerMinor','width')+styleValue('.sbMarkerMinor','borderRightWidth'))/2;
var xOffsetBar=(styleValue('.sbBar','borderLeftWidth')+styleValue('.sbBar','border-right-width'))/2;
var xOffsetBarAlt=(styleValue('.sbBarAlt','borderLeftWidth')+styleValue('.sbBarAlt','borderRightWidth'))/2;
if(!document.styleSheets){
xOffsetMarkerMajor=0.5;
xOffsetMarkerMinor=0.5;
}
while(objRef.labelContainer.hasChildNodes()){
objRef.labelContainer.removeChild(objRef.labelContainer.firstChild);
}
while(objRef.graphicsContainer.hasChildNodes()){
objRef.graphicsContainer.removeChild(objRef.graphicsContainer.firstChild);
}
while(objRef.numbersContainer.hasChildNodes()){
objRef.numbersContainer.removeChild(objRef.numbersContainer.firstChild);
}
var aMarker,aBarPiece,numbersBox,xOffset;
var alignmentOffset={
left:0,
center:(-1*objRef.divisions*objRef.subdivisions*subdivisionPixelLength/2),
right:(-1*objRef.divisions*objRef.subdivisions*subdivisionPixelLength)
}
var xPosition=0+alignmentOffset[objRef.align];
var markerMeasure=0;
for(var divisionIndex=0;divisionIndex<objRef.divisions;++divisionIndex){
xPosition=divisionIndex*objRef.subdivisions*subdivisionPixelLength;
xPosition+=alignmentOffset[objRef.align];
markerMeasure=(divisionIndex==0)?0:((divisionIndex*objRef.subdivisions)*subdivisionDisplayLength).toFixed(numDec);
aMarker=document.createElement('div');
aMarker.className='sbMarkerMajor';
aMarker.style.position='absolute';
aMarker.style.overflow='hidden';
aMarker.style.left=Math.round(xPosition-xOffsetMarkerMajor)+'px';
aMarker.appendChild(document.createTextNode(' '));
objRef.graphicsContainer.appendChild(aMarker);
if(!objRef.singleLine){
numbersBox=document.createElement('div');
numbersBox.className='sbNumbersBox';
numbersBox.style.position='absolute';
numbersBox.style.overflow='hidden';
numbersBox.style.textAlign='center';
if(objRef.showMinorMeasures){
numbersBox.style.width=Math.round(subdivisionPixelLength*2)+'px';
numbersBox.style.left=Math.round(xPosition-subdivisionPixelLength)+'px';
}
else{
numbersBox.style.width=Math.round(objRef.subdivisions*subdivisionPixelLength*2)+'px';
numbersBox.style.left=Math.round(xPosition-(objRef.subdivisions*subdivisionPixelLength))+'px';
}
numbersBox.appendChild(document.createTextNode(markerMeasure));
objRef.numbersContainer.appendChild(numbersBox);
}
for(var subdivisionIndex=0;subdivisionIndex<objRef.subdivisions;++subdivisionIndex){
aBarPiece=document.createElement('div');
aBarPiece.style.position='absolute';
aBarPiece.style.overflow='hidden';
aBarPiece.style.width=Math.round(subdivisionPixelLength)+'px';
if((subdivisionIndex%2)==0){
aBarPiece.className='sbBar';
aBarPiece.style.left=Math.round(xPosition-xOffsetBar)+'px';
}
else{
aBarPiece.className='sbBarAlt';
aBarPiece.style.left=Math.round(xPosition-xOffsetBarAlt)+'px';
}
aBarPiece.appendChild(document.createTextNode(' '));
objRef.graphicsContainer.appendChild(aBarPiece);
if(subdivisionIndex<(objRef.subdivisions-1)){
xPosition=((divisionIndex*objRef.subdivisions)+(subdivisionIndex+1))*subdivisionPixelLength;
xPosition+=alignmentOffset[objRef.align];
markerMeasure=(divisionIndex*objRef.subdivisions+subdivisionIndex+1)*subdivisionDisplayLength;
aMarker=document.createElement('div');
aMarker.className='sbMarkerMinor';
aMarker.style.position='absolute';
aMarker.style.overflow='hidden';
aMarker.style.left=Math.round(xPosition-xOffsetMarkerMinor)+'px';
aMarker.appendChild(document.createTextNode(' '));
objRef.graphicsContainer.appendChild(aMarker);
if(objRef.showMinorMeasures&&!objRef.singleLine){
numbersBox=document.createElement('div');
numbersBox.className='sbNumbersBox';
numbersBox.style.position='absolute';
numbersBox.style.overflow='hidden';
numbersBox.style.textAlign='center';
numbersBox.style.width=Math.round(subdivisionPixelLength*2)+'px';
numbersBox.style.left=Math.round(xPosition-subdivisionPixelLength)+'px';
numbersBox.appendChild(document.createTextNode(markerMeasure));
objRef.numbersContainer.appendChild(numbersBox);
}
}
}
}
xPosition=(objRef.divisions*objRef.subdivisions)*subdivisionPixelLength;
xPosition+=alignmentOffset[objRef.align];
markerMeasure=((objRef.divisions*objRef.subdivisions)*subdivisionDisplayLength).toFixed(numDec);
aMarker=document.createElement('div');
aMarker.className='sbMarkerMajor';
aMarker.style.position='absolute';
aMarker.style.overflow='hidden';
aMarker.style.left=Math.round(xPosition-xOffsetMarkerMajor)+'px';
aMarker.appendChild(document.createTextNode(' '));
objRef.graphicsContainer.appendChild(aMarker);
if(!objRef.singleLine){
numbersBox=document.createElement('div');
numbersBox.className='sbNumbersBox';
numbersBox.style.position='absolute';
numbersBox.style.overflow='hidden';
numbersBox.style.textAlign='center';
if(objRef.showMinorMeasures){
numbersBox.style.width=Math.round(subdivisionPixelLength*2)+'px';
numbersBox.style.left=Math.round(xPosition-subdivisionPixelLength)+'px';
}
else{
numbersBox.style.width=Math.round(objRef.subdivisions*subdivisionPixelLength*2)+'px';
numbersBox.style.left=Math.round(xPosition-(objRef.subdivisions*subdivisionPixelLength))+'px';
}
numbersBox.appendChild(document.createTextNode(markerMeasure));
objRef.numbersContainer.appendChild(numbersBox);
}
var labelBox=document.createElement('div');
labelBox.style.position='absolute';
var labelText;
if(objRef.singleLine){
labelText=markerMeasure;
labelBox.className='sbLabelBoxSingleLine';
labelBox.style.top='-0.6em';
labelBox.style.left=(xPosition+10)+'px';
}
else{
labelText='';
labelBox.className='sbLabelBox';
labelBox.style.textAlign='center';
labelBox.style.width=Math.round(objRef.divisions*objRef.subdivisions*subdivisionPixelLength)+'px'
labelBox.style.left=Math.round(alignmentOffset[objRef.align])+'px';
labelBox.style.overflow='hidden';
}
if(objRef.abbreviateLabel){
labelText+=' '+displayUnitsAbbr;
}
else{
labelText+=' '+displayUnits;
}
labelBox.appendChild(document.createTextNode(labelText));
objRef.labelContainer.appendChild(labelBox);
if(!document.styleSheets){
var defaultStyle=document.createElement('style');
defaultStyle.type='text/css';
var styleText='.sbBar {top: 0px; background: #666666; height: 1px; border: 0;}';
styleText+='.sbBarAlt {top: 0px; background: #666666; height: 1px; border: 0;}';
styleText+='.sbMarkerMajor {height: 7px; width: 1px; background: #666666; border: 0;}';
styleText+='.sbMarkerMinor {height: 5px; width: 1px; background: #666666; border: 0;}';
styleText+='.sbLabelBox {top: -16px;}';
styleText+='.sbNumbersBox {top: 7px;}';
defaultStyle.appendChild(document.createTextNode(styleText));
document.getElementsByTagName('head').item(0).appendChild(defaultStyle);
}
objRef.container.appendChild(objRef.graphicsContainer);
objRef.container.appendChild(objRef.labelContainer);
objRef.container.appendChild(objRef.numbersContainer);
}
