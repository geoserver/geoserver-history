<?php
/*
License: LGPL as per: http://www.gnu.org/copyleft/lesser.html
$Id: serializer.php 2141 2006-06-28 10:07:56Z steven $
$Name$
*/

////////////////////////////////////////////////////////////////////////////////
// Description:
// Script to serialize incoming data and return a reference to the file.
//
// Author: Nedjo Rogers
////////////////////////////////////////////////////////////////////////////////


// Set save directory.  This should be set to a writable, web-accessible directory.
$outputDir = "/temp";

// Read in data.
$data = $GLOBALS["HTTP_RAW_POST_DATA"];

// Create file and save data to it.
$tmpfname = tempnam($outputDir, "cmb") . ".xml";

$handle = fopen($tmpfname, "w");
fwrite($handle, $data);
fclose($handle);

// Send xml content type header.
header("Content-type: text/xml");

// Return XML snippet with file reference.
echo '
<XmlSerializer xmlns:xlink="http://www.w3.org/1999/xlink">
  <OnlineResource xlink:type="simple" xlink:href="' . $tmpfname . '"/>
</XmlSerializer>';
?> 
