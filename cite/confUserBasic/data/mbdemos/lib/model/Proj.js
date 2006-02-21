function Proj(srs){
this.srs=srs.toUpperCase();
switch(this.srs){
case "EPSG:4326":case "EPSG:4269":case "CRS:84":case "EPSG:4965":case new String("http://www.opengis.net/gml/srs/epsg.xml#4326").toUpperCase():
this.Forward=identity;
this.Inverse=identity;
this.units="degrees";
this.title="Lat/Long";
break;
case "EPSG:42101":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378137.0,6356752.314245,49.0,77.0,-95.0,0.0,0.0,-8000000));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:42304":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378137.0,6356752.314,49.0,77.0,-95.0,49.0,0.0,0));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:26986":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378137.0,6356752.314,42.68333333333333,41.71666666666667,-71.5,41.0,200000,750000));
this.units="meters";
this.title="Massachusetts Mainland (LCC)";
break;
case "EPSG:32761":case "EPSG:32661":
this.Init=psinit;
this.Forward=ll2ps;
this.Inverse=ps2ll;
this.Init(new Array(6378137.0,6356752.314245,0.0,-90.0,2000000,2000000));
this.units="meters";
this.title="Polar Stereographic";
break;
case "EPSG:27561":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378249.2,6356515.0,49.50,49.50,2.33722916655,49.50,600000.0,200000.0));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:27562":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378249.2,6356515.0,46.80,46.80,2.33722916655,46.8,600000.0,200000.0));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:27572":case "EPSG:27582":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378249.2,6356515.0,46.80,46.80,2.33722916655,46.8,600000.0,2200000.0));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:27563":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378249.2,6356515.0,44.10,44.10,2.33722916655,44.10,600000.0,200000.0));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:27564":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378249.2,6356515.0,42.17,42.17,2.33722916655,42.17,234.358,185861.369));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "EPSG:2154":this.Init=lccinit;
this.Forward=ll2lcc;
this.Inverse=lcc2ll;
this.Init(new Array(6378137.0,6356752.3141,44.00,49.00,3.00000000001,46.50,700000.0,6600000.0));
this.units="meters";
this.title="Lambert Conformal Conic";
break;
case "SCENE":this.Init=sceneInit;
this.Forward=ll2scene;
this.Inverse=scene2ll;
this.GetXYCoords=identity;this.GetPLCoords=identity;break;
case "PIXEL":
this.Forward=ll2pixel;
this.Inverse=pixel2ll;
this.units="pixels";
this.GetXYCoords=identity;this.GetPLCoords=identity;break;
default:
alert("unsupported map projection: "+this.srs);
}
this.matchSrs=function(otherSrs){
if(this.srs==otherSrs.toUpperCase())return true;
return false;
}
}
function identity(coords){
return coords;
}
function ll2scene(coords){
alert("ll2scene not defined");
return null;
}
function scene2ll(coords){
var xpct=(coords[0]-this.ul[0])/(this.lr[0]-this.ul[0]);
var ypct=(coords[1]-this.ul[1])/(this.lr[1]-this.ul[1]);
var lon=bilinterp(xpct,ypct,this.cul[0],this.cur[0],this.cll[0],this.clr[0])
var lat=bilinterp(xpct,ypct,this.cul[1],this.cur[1],this.cll[1],this.clr[1])
return new Array(lon,lat);
}
function sceneInit(param){
this.cul=param[0];
this.cur=param[1];
this.cll=param[2];
this.clr=param[3];
}
function bilinterp(x,y,a,b,c,d){
var top=x*(b-a)+a;
var bot=x*(d-c)+c;
return y*(bot-top)+top;
}
function ll2pixel(coords){
alert("ll2pixel not defined");
return null;
}
function pixel2ll(coords){
alert("pixel2ll not defined");
return null;
}
var PI=Math.PI;
var HALF_PI=PI*0.5;
var TWO_PI=PI*2.0;
var EPSLN=1.0e-10;
var R2D=57.2957795131;
var D2R=0.0174532925199;
var R=6370997.0; 
function lccinit(param){
this.r_major=param[0];
this.r_minor=param[1];
var lat1=param[2]*D2R;
var lat2=param[3]*D2R;
this.center_lon=param[4]*D2R;
this.center_lat=param[5]*D2R;
this.false_easting=param[6];
this.false_northing=param[7];
if(Math.abs(lat1+lat2)<EPSLN){
alert("Equal Latitiudes for St. Parallels on opposite sides of equator - lccinit");
return;
}
var temp=this.r_minor/this.r_major;
this.e=Math.sqrt(1.0-temp*temp);
var sin1=Math.sin(lat1);
var cos1=Math.cos(lat1);
var ms1=msfnz(this.e,sin1,cos1);
var ts1=tsfnz(this.e,lat1,sin1);
var sin2=Math.sin(lat2);
var cos2=Math.cos(lat2);
var ms2=msfnz(this.e,sin2,cos2);
var ts2=tsfnz(this.e,lat2,sin2);
var ts0=tsfnz(this.e,this.center_lat,Math.sin(this.center_lat));
if(Math.abs(lat1-lat2)>EPSLN){
this.ns=Math.log(ms1/ms2)/Math.log(ts1/ts2);
}else{
this.ns=sin1;
}
this.f0=ms1/(this.ns*Math.pow(ts1,this.ns));
this.rh=this.r_major*this.f0*Math.pow(ts0,this.ns);
}
function ll2lcc(coords){
var lon=coords[0];
var lat=coords[1];
if(lat<=90.0&&lat>=-90.0&&lon<=180.0&&lon>=-180.0){
lat*=D2R;
lon*=D2R;
}else{
alert("*** Input out of range ***: lon: "+lon+" - lat: "+lat);
return null;
}
var con=Math.abs(Math.abs(lat)-HALF_PI);
var ts;
if(con>EPSLN){
ts=tsfnz(this.e,lat,Math.sin(lat));
rh1=this.r_major*this.f0*Math.pow(ts,this.ns);
}else{
con=lat*this.ns;
if(con<=0){
alert("Point can not be projected - ll2lcc");
return null;
}
rh1=0;
}
var theta=this.ns*adjust_lon(lon-this.center_lon);
var x=rh1*Math.sin(theta)+this.false_easting;
var y=this.rh-rh1*Math.cos(theta)+this.false_northing;
return new Array(x,y);
}
function lcc2ll(coords){
var rh1,con,ts;
var lat,lon;
x=coords[0]-this.false_easting;
y=this.rh-coords[1]+this.false_northing;
if(this.ns>0){
rh1=Math.sqrt(x*x+y*y);
con=1.0;
}else{
rh1=-Math.sqrt(x*x+y*y);
con=-1.0;
}
var theta=0.0;
if(rh1!=0){
theta=Math.atan2((con*x),(con*y));
}
if((rh1!=0)||(this.ns>0.0)){
con=1.0/this.ns;
ts=Math.pow((rh1/(this.r_major*this.f0)),con);
lat=phi2z(this.e,ts);
if(lat==-9999)return null;
}else{
lat=-HALF_PI;
}
lon=adjust_lon(theta/this.ns+this.center_lon);
return new Array(R2D*lon,R2D*lat);
}
function psinit(param){
this.r_major=param[0];
this.r_minor=param[1];
this.center_lon=param[2]*D2R;
this.center_lat=param[3]*D2R;
this.false_easting=param[4];
this.false_northing=param[5];
var temp=this.r_minor/this.r_major;
this.e=1.0-temp*temp;
this.e=Math.sqrt(this.e);
var con=1.0+this.e;
var com=1.0-this.e;
this.e4=Math.sqrt(Math.pow(con,con)*Math.pow(com,com));
this.fac=(this.center_lat<0)?-1.0:1.0;
this.ind=0;
if(Math.abs(Math.abs(this.center_lat)-HALF_PI)>EPSLN){
this.ind=1;
var con1=this.fac*this.center_lat; 
var sinphi=Math.sin(con1);
this.mcs=msfnz(this.e,sinphi,Math.cos(con1));
this.tcs=tsfnz(this.e,con1,sinphi);
}
}
function ll2ps(coords){
var lon=coords[0];
var lat=coords[1];
var con1=this.fac*adjust_lon(lon-this.center_lon);
var con2=this.fac*lat;
var sinphi=Math.sin(con2);
var ts=tsfnz(this.e,con2,sinphi);
if(this.ind!=0){
rh=this.r_major*this.mcs*ts/this.tcs;
}else{
rh=2.0*this.r_major*ts/this.e4;
}
var x=this.fac*rh*Math.sin(con1)+this.false_easting;
var y=-this.fac*rh*Math.cos(con1)+this.false_northing;;
return new Array(x,y);
}
function ps2ll(coords){
x=(coords[0]-this.false_easting)*this.fac;
y=(coords[1]-this.false_northing)*this.fac;
var rh=Math.sqrt(x*x+y*y);
if(this.ind!=0){
ts=rh*this.tcs/(this.r_major*this.mcs);
}else{
ts=rh*this.e4/(this.r_major*2.0);
}
var lat=this.fac*phi2z(this.e,ts);
if(lat==-9999)return null;
var lon=0;
if(rh==0){
lon=this.fac*this.center_lon;
}else{
lon=adjust_lon(this.fac*Math.atan2(x,-y)+this.center_lon);
}
return new Array(R2D*lon,R2D*lat);
}
function msfnz(eccent,sinphi,cosphi){
var con=eccent*sinphi;
return cosphi/(Math.sqrt(1.0-con*con));
}
function tsfnz(eccent,phi,sinphi){
var con=eccent*sinphi;
var com=.5*eccent; 
con=Math.pow(((1.0-con)/(1.0+con)),com);
return(Math.tan(.5*(HALF_PI-phi))/con);
}
function phi2z(eccent,ts){
var eccnth=.5*eccent;
var con,dphi;
var phi=HALF_PI-2*Math.atan(ts);
for(i=0;i<=15;i++){
con=eccent*Math.sin(phi);
dphi=HALF_PI-2*Math.atan(ts*(Math.pow(((1.0-con)/(1.0+con)),eccnth)))-phi;
phi+=dphi; 
if(Math.abs(dphi)<=.0000000001)return phi;
}
alert("Convergence error - phi2z");
return-9999;
}
function sign(x){if(x<0.0)return(-1);else return(1);}
function adjust_lon(x){x=(Math.abs(x)<PI)?x:(x-(sign(x)*TWO_PI));return(x);}
