function MGRS(){
var NUM_100K_SETS=6;
var SET_ORIGIN_COLUMN_LETTERS=new Array('A','J','S','A','J','S');
var SET_ORIGIN_ROW_LETTERS=new Array('A','F','A','F','A','F');
var BESSEL_SET_ORIGIN_COLUMN_LETTERS=new Array('A','J','S','A','J','S');
var BESSEL_SET_ORIGIN_ROW_LETTERS=new Array('L','R','L','R','L','R');
var SET_NORTHING_ROLLOVER=20000000;
var ACCURACY_1_METER=5;
var ACCURACY_10_METER=4;
var ACCURACY_100_METER=3;
var ACCURACY_1000_METER=2;
var ACCURACY_10000_METER=1;
var originColumnLetters=SET_ORIGIN_COLUMN_LETTERS;
var originRowLetters=SET_ORIGIN_ROW_LETTERS;
var A=65;var I=73;var O=79;var V=86;var Z=90; 
var DEBUG=false;
var mgrs_;
var lat_;
var lon_;
var radlat_;
var radlon_;
var northing_;
var easting_;
var zone_number_;
var zone_letter_;
this.convert=function(latitude,longitude){
lat_=parseFloat(latitude);
lon_=parseFloat(longitude);
radlat_=degToRad(lat_);
radlon_=degToRad(lon_);
LLtoUTM();
return formatMGRS();
}
function degToRad(deg){
return(deg*(Math.PI/180.0));
}
function LLtoUTM(){
var Lat=lat_;
var Long=lon_;
var a=6378137.0;var eccSquared=0.00669438;var k0=0.9996;
var LongOrigin;
var eccPrimeSquared;
var N,T,C,A,M;
var LatRad=radlat_;
var LongRad=radlon_;
var LongOriginRad;
var ZoneNumber;
ZoneNumber=Math.floor((Long+180)/6)+1;
if(Long==180){
ZoneNumber=60;
}
if(Lat>=56.0&&Lat<64.0&&Long>=3.0&&Long<12.0){
ZoneNumber=32;
}
if(Lat>=72.0&&Lat<84.0){
if(Long>=0.0&&Long<9.0)
ZoneNumber=31;
else if(Long>=9.0&&Long<21.0)
ZoneNumber=33;
else if(Long>=21.0&&Long<33.0)
ZoneNumber=35;
else if(Long>=33.0&&Long<42.0)
ZoneNumber=37;
}
LongOrigin=(ZoneNumber-1)*6-180+3;LongOriginRad=degToRad(LongOrigin);
eccPrimeSquared=(eccSquared)/(1-eccSquared);
N=a/Math.sqrt(1-eccSquared*Math.sin(LatRad)*Math.sin(LatRad));
T=Math.tan(LatRad)*Math.tan(LatRad);
C=eccPrimeSquared*Math.cos(LatRad)*Math.cos(LatRad);
A=Math.cos(LatRad)*(LongRad-LongOriginRad);
M=a
*((1-eccSquared/4-3*eccSquared*eccSquared/64-5
*eccSquared*eccSquared*eccSquared/256)
*LatRad
-(3*eccSquared/8+3*eccSquared*eccSquared
/32+45*eccSquared*eccSquared
*eccSquared/1024)
*Math.sin(2*LatRad)
+(15*eccSquared*eccSquared/256+45*eccSquared
*eccSquared*eccSquared/1024)
*Math.sin(4*LatRad)-(35*eccSquared*eccSquared
*eccSquared/3072)
*Math.sin(6*LatRad));
var UTMEasting=(k0
*N
*(A+(1-T+C)*A*A*A/6.0+(5-18*T+T*T
+72*C-58*eccPrimeSquared)
*A*A*A*A*A/120.0)+500000.0);
var UTMNorthing=(k0*(M+N
*Math.tan(LatRad)
*(A*A/2+(5-T+9*C+4*C*C)*A*A*A*A
/24.0+(61-58*T+T*T+600*C-330*eccPrimeSquared)
*A*A*A*A*A*A/720.0)));
if(Lat<0.0){
UTMNorthing+=10000000.0;}
northing_=Math.round(UTMNorthing);
easting_=Math.round(UTMEasting);
zone_number_=ZoneNumber;
zone_letter_=getLetterDesignator(Lat);
}
function getLetterDesignator(lat){
var LetterDesignator='Z';
if((84>=lat)&&(lat>=72))
LetterDesignator='X';
else if((72>lat)&&(lat>=64))
LetterDesignator='W';
else if((64>lat)&&(lat>=56))
LetterDesignator='V';
else if((56>lat)&&(lat>=48))
LetterDesignator='U';
else if((48>lat)&&(lat>=40))
LetterDesignator='T';
else if((40>lat)&&(lat>=32))
LetterDesignator='S';
else if((32>lat)&&(lat>=24))
LetterDesignator='R';
else if((24>lat)&&(lat>=16))
LetterDesignator='Q';
else if((16>lat)&&(lat>=8))
LetterDesignator='P';
else if((8>lat)&&(lat>=0))
LetterDesignator='N';
else if((0>lat)&&(lat>=-8))
LetterDesignator='M';
else if((-8>lat)&&(lat>=-16))
LetterDesignator='L';
else if((-16>lat)&&(lat>=-24))
LetterDesignator='K';
else if((-24>lat)&&(lat>=-32))
LetterDesignator='J';
else if((-32>lat)&&(lat>=-40))
LetterDesignator='H';
else if((-40>lat)&&(lat>=-48))
LetterDesignator='G';
else if((-48>lat)&&(lat>=-56))
LetterDesignator='F';
else if((-56>lat)&&(lat>=-64))
LetterDesignator='E';
else if((-64>lat)&&(lat>=-72))
LetterDesignator='D';
else if((-72>lat)&&(lat>=-80))
LetterDesignator='C';
return LetterDesignator;
}
function formatMGRS(){
var seasting=""+easting_;
var snorthing=""+northing_;
while(snorthing.length>6)
snorthing=snorthing.substr(1,snorthing.length-1);
var str=zone_number_+""+zone_letter_+
get100kID(easting_,northing_,zone_number_)+
seasting.substr(1,4)+snorthing.substr(1,4);
return str;
}
function get100kID(easting,northing,zone_number){
var setParm=get100kSetForZone(zone_number);
var setColumn=Math.floor(easting/100000);
var setRow=Math.floor(northing/100000)%20;
return getLetter100kID(setColumn,setRow,setParm);
}
function get100kSetForZone(i){
var setParm=i%NUM_100K_SETS;
if(setParm==0)
setParm=NUM_100K_SETS;
return setParm;
}
function getLetter100kID(column,row,parm){
var index=parm-1;
var colOrigin=AsciiToNum(SET_ORIGIN_COLUMN_LETTERS[index]);
var rowOrigin=AsciiToNum(SET_ORIGIN_ROW_LETTERS[index]);
var colInt=colOrigin+column-1;
var rowInt=rowOrigin+row;
var rollover=false;
if(colInt>Z){
colInt=colInt-Z+A-1;
rollover=true;
}
if(colInt==I||(colOrigin<I&&colInt>I)
||((colInt>I||colOrigin<I)&&rollover)){
colInt++;
}
if(colInt==O||(colOrigin<O&&colInt>O)
||((colInt>O||colOrigin<O)&&rollover)){
colInt++;
if(colInt==I){
colInt++;
}
}
if(colInt>Z){
colInt=colInt-Z+A-1;
}
if(rowInt>V){
rowInt=rowInt-V+A-1;
rollover=true;
}else{
rollover=false;
}
if(((rowInt==I)||((rowOrigin<I)&&(rowInt>I)))
||(((rowInt>I)||(rowOrigin<I))&&rollover)){
rowInt++;
}
if(((rowInt==O)||((rowOrigin<O)&&(rowInt>O)))
||(((rowInt>O)||(rowOrigin<O))&&rollover)){
rowInt++;
if(rowInt==I){
rowInt++;
}
}
if(rowInt>V){
rowInt=rowInt-V+A-1;
}
var twoLetter=NumToAscii(colInt)+""+NumToAscii(rowInt);
return twoLetter;
}
function AsciiToNum(ascii){
switch(ascii){
case 'A':
return 65;
case 'B':
return 66;
case 'C':
return 67;
case 'D':
return 68;
case 'E':
return 69;
case 'F':
return 70;
case 'G':
return 71;
case 'H':
return 72;
case 'I':
return 73;
case 'J':
return 74;
case 'K':
return 75;
case 'L':
return 76;
case 'M':
return 77;
case 'N':
return 78;
case 'O':
return 79;
case 'P':
return 80;
case 'Q':
return 81;
case 'R':
return 82;
case 'S':
return 83;
case 'T':
return 84;
case 'U':
return 85;
case 'V':
return 86;
case 'W':
return 87;
case 'X':
return 88;
case 'Y':
return 89;
case 'Z':
return 90;
}
}
function NumToAscii(num){
switch(num){
case 65:
return 'A';
case 66:
return 'B';
case 67:
return 'C';
case 68:
return 'D';
case 69:
return 'E';
case 70:
return 'F';
case 71:
return 'G';
case 72:
return 'H';
case 73:
return 'I';
case 74:
return 'J';
case 75:
return 'K';
case 76:
return 'L';
case 77:
return 'M';
case 78:
return 'N';
case 79:
return 'O';
case 80:
return 'P';
case 81:
return 'Q';
case 82:
return 'R';
case 83:
return 'S';
case 84:
return 'T';
case 85:
return 'U';
case 86:
return 'V';
case 87:
return 'W';
case 88:
return 'X';
case 89:
return 'Y';
case 90:
return 'Z';
}
}
}
