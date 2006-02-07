<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head>
<link rel="stylesheet" href="/qwms/inc/print.css">

<script language='Javascript'>
  var path = "/geoserver/data/quickWMS/";
  var path_skin = path + "blue_skin/";
</script>
<script language="Javascript" src="inc/WMSbrowsers.js"></script>
<script language="Javascript" src="inc/WMSlayer.js"></script>
<script language="Javascript" src="inc/WMSmap.js"></script>
<script language="Javascript" src="inc/WMStools.js"></script>
<script language="Javascript" src="inc/WMSquick.js"></script>
<script language="Javascript" src="inc/WMSnavigation.js"></script>
<script language="Javascript" src="inc/KDVZadds.js"></script>
<title>WMS Client - PrintPage</title>
</head>
<body>

<script LANGUAGE="JavaScript">
	var aRef = self.opener.map
	var imgContent = '';
	var hrline = "<hr>";
	var header = "<span>- Printout -</span>";
	var imgHeader = GetImgHTML('Header', 'images/quickWMS.gif', '0px', '0px', '420px', '39px', "");

	openLayer('Header', '', 'text-align:center; vertical-align:top; border:0px; padding:0px;', 20, 20, 650, 10, true);
	document.write(imgHeader);
	closeLayer();

	openLayer('Linie01', 'header', 'text-align:center; vertical-align:top; border-bottom:2px solid #000066;', 20, 60, 650, 25, true);
	document.write(header);
	closeLayer();
</script>

<script LANGUAGE="JavaScript">
	var aRef = self.opener.map
	var imgContent = '';

	for (i = 0; i<aRef.layers.length; i++) {
		aSrc = aRef.layers[i].url(aRef.BBox);
		imgContent = GetImgHTML('Map', aSrc, '20px', '100px', aRef.width, aRef.height, "text-align:center; vertical-align:top; border:0px;")
		document.write(imgContent);
		}

</script>

<script LANGUAGE="JavaScript">
	var jetzt = new Date();
	var TagInWoche = jetzt.getDay();
	var TagInMonat = jetzt.getDate();
	var MonatImJahr = jetzt.getMonth();
	var Jahr = jetzt.getFullYear();
	var Wochentag = new Array("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");
	var content = "";

	openLayer('Datum', 'text10', 'text-align:center; vertical-align:bottom; border-top:2px solid #000066;', 20, 120+aRef.height, aRef.width, 25, true);
	content += "<span>Map provided at: ";
	content += Wochentag[TagInWoche]+", "+TagInMonat+"."+(MonatImJahr+1)+"."+Jahr+"</span>";
	document.write(content);
	closeLayer();
</script>

</body>
</html>