.. _coverage_xml:

.. code-block:: xml

   <coverage>
     <name>Arc_Sample</name>
     <nativeName>Arc_Sample</nativeName>
     <alias/>
     <title>Global annual rainfall</title>
     <description>Global annual rainfall in ArcGrid format</description>
     <keywords>
       <string>WCS</string>
       <string>arcGridSample</string>
       <string>arcGridSample_Coverage</string>
     </keywords>
     <metadataLinks/>
     <srs>EPSG:4326</srs>
     <nativeBoundingBox>
       <minx>-180.0</minx>
       <maxx>180.0</maxx>
       <miny>-90.0</miny>
       <maxy>90.0</maxy>
       <crs>EPSG:4326</crs>
     </nativeBoundingBox>
     <latLonBoundingBox>
       <minx>-180.0</minx>
       <maxx>180.0</maxx>
       <miny>-90.0</miny>
       <maxy>90.0</maxy>
       <crs>EPSG:4326</crs>
     </latLonBoundingBox>
     <enabled>true</enabled>
     <nativeFormat>ArcGrid</nativeFormat>
     <grid dimension="2">
       <range>
         <low>0 0</low>
         <high>720 360</high>
       </range>
       <transform>
         <scaleX>0.5</scaleX>
         <scaleY>-0.5</scaleY>
         <shearX>0.0</shearX>
         <shearX>0.0</shearX>
         <translateX>-179.75</translateX>
         <translateY>89.75</translateY>
       </transform>
       <crs>EPSG:4326</crs>
     </grid>
     <supportedFormats>
       <string>ARCGRID</string>
       <string>ARCGRID-GZIP</string>
       <string>GEOTIFF</string>
       <string>PNG</string>
       <string>GIF</string>
       <string>TIFF</string>
     </supportedFormats>
     <interpolationMethods>
       <string>nearest neighbor</string>
     </interpolationMethods>
     <defaultInterpolationMethod>nearest neighbor</defaultInterpolationMethod>
     <dimensions>
       <coverageDimension>
         <name>precip30min</name>
         <description>GridSampleDimension[-9999.0,8849.000000000002]</description>
         <range>
           <elementClass>java.lang.Double</elementClass>
           <minValue class="double">-9999.0</minValue>
           <maxValue class="double">8849.0</maxValue>
           <isMinIncluded>true</isMinIncluded>
           <isMaxIncluded>true</isMaxIncluded>
         </range>
         <nullValues/>
       </coverageDimension>
     </dimensions>
     <requestSRS>
       <string>EPSG:4326</string>
     </requestSRS>
     <responseSRS>
       <string>EPSG:4326</string>
     </responseSRS>
     <parameters/>
     <nativeCRS>GEOGCS[&quot;WGS 84&quot;, 
     DATUM[&quot;World Geodetic System 1984&quot;, 
       SPHEROID[&quot;WGS 84&quot;, 6378137.0, 298.257223563, AUTHORITY[&quot;EPSG&quot;,&quot;7030&quot;]], 
       AUTHORITY[&quot;EPSG&quot;,&quot;6326&quot;]], 
     PRIMEM[&quot;Greenwich&quot;, 0.0, AUTHORITY[&quot;EPSG&quot;,&quot;8901&quot;]], 
     UNIT[&quot;degree&quot;, 0.017453292519943295], 
     AXIS[&quot;Geodetic longitude&quot;, EAST], 
     AXIS[&quot;Geodetic latitude&quot;, NORTH], 
     AUTHORITY[&quot;EPSG&quot;,&quot;4326&quot;]]</nativeCRS>
     <store>arcGridSample</store>
     <namespace>nurc</namespace>
     <metadata>
       <dirName>
         <string>arcGridSample_Arc_Sample</string>
       </dirName>
     </metadata>
   </coverage>
