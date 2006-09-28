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
case "EPSG:4326":case "EPSG:4269":case "CRS:84":case "EPSG:4965":case new String("http://www.opengis.net/gml/srs/epsg.xml#4326").toUpperCase():
this.Forward=identity;
this.Inverse=identity;
this.units="degrees";
this.title="Lat/Long";
break;
case "EPSG:102758":this.title="NAD 1983 StatePlane Wyoming West FIPS 4904 US Survey Feet";
this.Init=tminit;
this.Forward=ll2tm
this.Inverse=tm2ll;
this.Init(new Array(grs80[0],grs80[1],0.9999375,-110.0833333333333,40.5,800000,100000));
this.units="usfeet";
break;
case "EPSG:32158":this.title="NAD 1983 StatePlane Wyoming West meters";
this.Init=tminit;
this.Forward=ll2tm
this.Inverse=tm2ll;
this.Init(new Array(grs80[0],grs80[1],0.9999375,-110.0833333333333,40.5,800000,100000));
this.units="meters";
break;
case"EPSG:26903":case"EPSG:26904":case"EPSG:26905":case"EPSG:26906":case"EPSG:26907":case"EPSG:26908":case"EPSG:26909":
case"EPSG:26910":case"EPSG:26911":case"EPSG:26912":case"EPSG:26913":case"EPSG:26914":case"EPSG:26915":case"EPSG:26916":
case"EPSG:26917":case"EPSG:26918":case"EPSG:26919":case"EPSG:26920":case"EPSG:26921":case"EPSG:26922":case"EPSG:26923":
this.title="NAD83 / UTM zone "+srs.substr(8,2)+"N";
this.Init=utminit;
this.Forward=ll2tm;
this.Inverse=tm2ll;
this.Init(new Array(grs80[0],grs80[1],0.9996,srs.substr(8,2)));
this.units="meters";
break;
case"EPSG:32601":case"EPSG:32602":
case"EPSG:32603":case"EPSG:32604":case"EPSG:32605":case"EPSG:32606":case"EPSG:32607":case"EPSG:32608":case"EPSG:32609":
case"EPSG:32610":case"EPSG:32611":case"EPSG:32612":case"EPSG:32613":case"EPSG:32614":case"EPSG:32615":case"EPSG:32616":
case"EPSG:32617":case"EPSG:32618":case"EPSG:32619":case"EPSG:32620":case"EPSG:32621":case"EPSG:32622":case"EPSG:32623":
case"EPSG:32624":case"EPSG:32625":case"EPSG:32626":case"EPSG:32627":case"EPSG:32628":case"EPSG:32629":
case"EPSG:32630":case"EPSG:32631":case"EPSG:32632":case"EPSG:32633":case"EPSG:32634":case"EPSG:32635":case"EPSG:32636":
case"EPSG:32637":case"EPSG:32638":case"EPSG:32639":case"EPSG:32640":case"EPSG:32641":case"EPSG:32642":
case"EPSG:32643":case"EPSG:32644":case"EPSG:32645":case"EPSG:32646":case"EPSG:32647":case"EPSG:32648":case"EPSG:32649":
case"EPSG:32650":case"EPSG:32651":case"EPSG:32652":case"EPSG:32653":case"EPSG:32654":case"EPSG:32655":case"EPSG:32656":
case"EPSG:32657":case"EPSG:32658":case"EPSG:32659":case"EPSG:32660":
this.title="WGS84 / UTM zone "+srs.substr(8,2)+"N";
this.Init=utminit;
this.Forward=ll2tm;
this.Inverse=tm2ll;
this.Init(new Array(wgs84[0],wgs84[1],0.9996,srs.substr(8,2)));
this.units="meters";
break;
case"EPSG:32701":case"EPSG:32702":
case"EPSG:32703":case"EPSG:32704":case"EPSG:32705":case"EPSG:32706":case"EPSG:32707":case"EPSG:32708":case"EPSG:32709":
case"EPSG:32710":case"EPSG:32711":case"EPSG:32712":case"EPSG:32713":case"EPSG:32714":case"EPSG:32715":case"EPSG:32716":
case"EPSG:32717":case"EPSG:32718":case"EPSG:32719":case"EPSG:32720":case"EPSG:32721":case"EPSG:32722":case"EPSG:32723":
case"EPSG:32724":case"EPSG:32725":case"EPSG:32726":case"EPSG:32727":case"EPSG:32728":case"EPSG:32729":
case"EPSG:32730":case"EPSG:32731":case"EPSG:32732":case"EPSG:32733":case"EPSG:32734":case"EPSG:32735":case"EPSG:32736":
case"EPSG:32737":case"EPSG:32738":case"EPSG:32739":case"EPSG:32740":case"EPSG:32741":case"EPSG:32742":
case"EPSG:32743":case"EPSG:32744":case"EPSG:32745":case"EPSG:32746":case"EPSG:32747":case"EPSG:32748":case"EPSG:32749":
case"EPSG:32750":case"EPSG:32751":case"EPSG:32752":case"EPSG:32753":case"EPSG:32754":case"EPSG:32755":case"EPSG:32756":
case"EPSG:32757":case"EPSG:32758":case"EPSG:32759":case"EPSG:32760":
this.title="WGS84 / UTM zone "+srs.substr(8,2)+"S";
this.Init=utminit;
this.Forward=ll2tm;
this.Inverse=tm2ll;
this.Init(new Array(wgs84[0],wgs84[1],0.9996,"-"+srs.substr(8,2)));
this.units="meters";
break;
case "EPSG:26591":this.title="Monte Mario (Rome) / Italy zone 1";
this.Init=tminit;
this.Forward=ll2tm
this.Inverse=tm2ll;
this.Init(new Array(6378388.0,6356911.94612795,0.9996,9,0.0,1500000.0,0.0));
this.units="meters";
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
function semi_minor(a,f){return a-(a*(1/f));}
var grs80=new Array(6378137.0,6356752.31414036);var wgs84=new Array(6378137.0,6356752.31424518);
var wgs72=new Array(6378135.0,6356750.52001609);
var intl=new Array(6378388.0,6356911.94612795); 
var usfeet=1200/3937;var feet=0.3048; 
function e0fn(x){return(1.0-0.25*x*(1.0+x/16.0*(3.0+1.25*x)));}
function e1fn(x){return(0.375*x*(1.0+0.25*x*(1.0+0.46875*x)));}
function e2fn(x){return(0.05859375*x*x*(1.0+0.75*x));}
function e3fn(x){return(x*x*x*(35.0/3072.0));}
function mlfn(e0,e1,e2,e3,phi){return(e0*phi-e1*Math.sin(2.0*phi)+e2*Math.sin(4.0*phi)-e3*Math.sin(6.0*phi));}
function tminit(param){
this.r_maj=param[0];
this.r_min=param[1];
this.scale_fact=param[2];
this.lon_center=param[3]*D2R;
this.lat_origin=param[4]*D2R;
this.false_easting=param[5];
this.false_northing=param[6];
var temp=this.r_min/this.r_maj;
this.es=1.0-Math.pow(temp,2);
this.e0=e0fn(this.es);
this.e1=e1fn(this.es);
this.e2=e2fn(this.es);
this.e3=e3fn(this.es);
this.ml0=this.r_maj*mlfn(this.e0,this.e1,this.e2,this.e3,this.lat_origin);
this.esp=this.es/(1.0-this.es);
this.ind=(this.es<.00001)?1:0;
}
function utminit(param){
this.r_maj=param[0];
this.r_min=param[1];
this.scale_fact=param[2];
var zone=param[3];
this.lat_origin=0.0;
this.lon_center=((6*Math.abs(zone))-183)*D2R;
this.false_easting=500000.0;
this.false_northing=(zone<0)?10000000.0:0.0;
var temp=this.r_min/this.r_maj;
this.es=1.0-Math.pow(temp,2);
this.e0=e0fn(this.es);
this.e1=e1fn(this.es);
this.e2=e2fn(this.es);
this.e3=e3fn(this.es);
this.ml0=this.r_maj*mlfn(this.e0,this.e1,this.e2,this.e3,this.lat_origin);
this.esp=this.es/(1.0-this.es);
this.ind=(this.es<.00001)?1:0;
} 
function ll2tm(coords){
var lon=coords[0]*D2R;
var lat=coords[1]*D2R;
var delta_lon=adjust_lon(lon-this.lon_center); 
var con; 
var x,y;
var sin_phi=Math.sin(lat);
var cos_phi=Math.cos(lat);
if(this.ind!=0){
var b=cos_phi*Math.sin(delta_lon);
if((Math.abs(Math.abs(b)-1.0))<.0000000001){
alert("Error in ll2tm(): Point projects into infinity");
return(93);
}else{
x=.5*this.r_maj*this.scale_fact*Math.log((1.0+b)/(1.0-b));
con=Math.acos(cos_phi*Math.cos(delta_lon)/Math.sqrt(1.0-b*b));
if(lat<0)
con=-con;
y=this.r_maj*this.scale_fact*(con-this.lat_origin);
}
}else{
var al=cos_phi*delta_lon;
var als=Math.pow(al,2);
var c=this.esp*Math.pow(cos_phi,2);
var tq=Math.tan(lat);
var t=Math.pow(tq,2);
con=1.0-this.es*Math.pow(sin_phi,2);
var n=this.r_maj/Math.sqrt(con);
var ml=this.r_maj*mlfn(this.e0,this.e1,this.e2,this.e3,lat);
x=this.scale_fact*n*al*(1.0+als/6.0*(1.0-t+c+als/20.0*(5.0-18.0*t+Math.pow(t,2)+72.0*c-58.0*this.esp)))+this.false_easting;
y=this.scale_fact*(ml-this.ml0+n*tq*(als*(0.5+als/24.0*(5.0-t+9.0*c+4.0*Math.pow(c,2)+als/30.0*(61.0-58.0*t+Math.pow(t,2)+600.0*c-330.0*this.esp)))))+this.false_northing;
switch(this.units){
case "usfeet":
x/=usfeet;
y/=usfeet
break;
case "feet":
x=x/feet;
y=y/feet;
break;
}}
return new Array(x,y);
} 
function tm2ll(coords){
var x=coords[0];
var y=coords[1];
var con,phi; 
var delta_phi; 
var i;
var max_iter=6; 
var lat,lon;
if(this.ind!=0){ 
var f=exp(x/(this.r_maj*this.scale_fact));
var g=.5*(f-1/f);
var temp=this.lat_origin+y/(this.r_maj*this.scale_fact);
var h=cos(temp);
con=sqrt((1.0-h*h)/(1.0+g*g));
lat=asinz(con);
if(temp<0)
lat=-lat;
if((g==0)&&(h==0)){
lon=this.lon_center;
}else{
lon=adjust_lon(atan2(g,h)+this.lon_center);
}
}else{x=x-this.false_easting;
y=y-this.false_northing;
con=(this.ml0+y/this.scale_fact)/this.r_maj;
phi=con;
for(i=0;;i++){
delta_phi=((con+this.e1*Math.sin(2.0*phi)-this.e2*Math.sin(4.0*phi)+this.e3*Math.sin(6.0*phi))/this.e0)-phi;
phi+=delta_phi;
if(Math.abs(delta_phi)<=EPSLN)break;
if(i>=max_iter){
alert("Error in tm2ll(): Latitude failed to converge");
return(95);
}
}if(Math.abs(phi)<HALF_PI){
var sin_phi=Math.sin(phi);
var cos_phi=Math.cos(phi);
var tan_phi=Math.tan(phi);
var c=this.esp*Math.pow(cos_phi,2);
var cs=Math.pow(c,2);
var t=Math.pow(tan_phi,2);
var ts=Math.pow(t,2);
con=1.0-this.es*Math.pow(sin_phi,2);
var n=this.r_maj/Math.sqrt(con);
var r=n*(1.0-this.es)/con;
var d=x/(n*this.scale_fact);
var ds=Math.pow(d,2);
lat=phi-(n*tan_phi*ds/r)*(0.5-ds/24.0*(5.0+3.0*t+10.0*c-4.0*cs-9.0*this.esp-ds/30.0*(61.0+90.0*t+298.0*c+45.0*ts-252.0*this.esp-3.0*cs)));
lon=adjust_lon(this.lon_center+(d*(1.0-ds/6.0*(1.0+2.0*t+c-ds/20.0*(5.0-2.0*c+28.0*t-3.0*cs+8.0*this.esp+24.0*ts)))/cos_phi));
}else{
lat=HALF_PI*sign(y);
lon=this.lon_center;
}
}
return new Array(lon*R2D,lat*R2D);
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
