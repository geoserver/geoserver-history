<html>
<#assign wmsUrl="../../../wms?request=GetMap&version=1.1.1" 
         wfsUrl="../../../wfs?SERVICE=WFS&VERSION=1.1.0&REQUEST=GetFeature"
         layersParam = "&layers="+name
         nameParam = "&typeName="+name
         kmlUrl="../../../wms/kml?mode=flat&superoverlay=true"
         shapefileUrl=wmsUrl+"/wfs?Format=SHAPE-ZIP&request=GetFeature&version=1.1.1"+nameParam
         gml2="GML2"
         gml3="gml3"
         json="json"
         PDFParam="&Format=application/pdf"
         coords = boundingBox?matches("-?\\d+(\\.\\d*)?")
         bboxParam="&bbox="+coords[0]+","+coords[2]+","+coords[1]+","+coords[3]
>
  <head>
    <title>${title} - Powered by GeoServer </title>

  </head>
  <body>
    <h1>${title}</h1>
    <p>${abstract}</p>
    <p></p>

    <h2>Metadata</h2>
    <table border="1">
      <tr>
        <td>Keywords</td>
        <td><#list keywords as keyword>${keyword}, </#list></td>
      </tr>
      <tr><td>Native CRS</td><td>${nativeCRS}</td><td>
      <tr><td>Declared CRS</td><td>${declaredCRS}</td><td>
    </table>

    <h2>Download Data</h2>
    Download the data as:
    <ul>
      <li><a href="${kmlUrl + layersParam}">KML</a></li>
      <li><a href="${wfsUrl + nameParam + '&outputFormat=' + 'SHAPE-ZIP'}">Shapefile</a></li>   
      <li><a href="${wfsUrl + nameParam + '&outputFormat=' + json}">JSON</a></li> 
    </ul>
  </body>
</html>
