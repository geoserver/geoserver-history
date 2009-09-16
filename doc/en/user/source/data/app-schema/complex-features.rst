.. _complex_features:

Complex Features
================


Simple features
---------------

Simple features contain a list of properties that each have one piece of simple information such as a string or number.
Special provision is made for geometry objects.

The Open Geospatial Consortium (OGC) defines three Simple Feature profiles; SF-0, SF-1, and SF-2. GeoServer simple features are close to OGC SF-0.

For example, if we had a database table ``stations``::

    | id | code |      name      |         location         |
    +----+------+----------------+--------------------------+
    | 27 | ALIC | Alice Springs  | POINT(133.8855 -23.6701) |
    | 4  | NORF | Norfolk Island | POINT(167.9388 -29.0434) |
    | 12 | COCO | Cocos          | POINT(96.8339 -12.1883)  |
    | 31 | ALBY | Albany         | POINT(117.8102 -34.9502) |

GeoServer would then be able to create the following simple features WFS response fragment::

    <gps:stations gml:id="stations.27">
        <gps:code>ALIC</gps:code>
        <gps:name>Alice Springs</gps:name>
        <gps:location>
            <gml:Point srsName="urn:x-ogc:def:crs:EPSG:4326">
                <gml:pos>-23.6701 133.8855</gml:pos>
            </gml:Point>
        </gps:location>
    </gps:stations>

* Every row in the table is converted into a feature.
* Every column in the table is converted into an element, which contains the value for that row.
* Automatic conversions are applied to some special types like geometries.


GeoServer simple features provide a straightforward mapping from a database table or similar structure to a "flat" XML representation where every column of the 
 
What simple features can do
```````````````````````````

* Easy to implement
* Fast
* Query on properies
* Spatial queries on geometries.

What is wrong with simple features?
```````````````````````````````````

* XML format is tied to the database schema
* As more data owners with different data are added to a community, the number of columns in the table becomes unmanageable.


Complex features
----------------


Complex features contain properties that can contain further nested properties to arbitrary depth.


* Define information model as an object-oriented structure, an *application schema*.
* Information is modelled not as a single table but as a collection of related objects.

Let us return to our ``stations`` table and supplement it with a foreign key that describes the relationship between the GPS station and the geologic unit to which it is attached::

    | id | code |      name      |         location         | gu_id |
    +----+------+----------------+--------------------------+-------+
    | 27 | ALIC | Alice Springs  | POINT(133.8855 -23.6701) | 32785 |
    | 4  | NORF | Norfolk Island | POINT(167.9388 -29.0434) | 10237 | 
    | 12 | COCO | Cocos          | POINT(96.8339 -12.1883)  | 19286 |
    | 31 | ALBY | Albany         | POINT(117.8102 -34.9502) | 92774 |


``geologicunit``::

    |  id   |                       urn                        |         text        |
    +-------+--------------------------------------------------+---------------------+
    | 32785 | urn:cgi:feature:SomeAuthority:GeologicUnit:32785 | Metamorphic bedrock |
    ...


The simple features approach would be to join the ``stations`` table with the ``geologicunit`` table into one giant view and then deliver "flat" XML that contained all the properties of both. 

The complex feature approach is to deliver the two tables as separate feature types. This allows the relationship between the entities to be represented while preserving their individual identity.



http://schemas.opengis.net/sampling/1.0.0/sampling.xsd


Complex::

    <sa:SamplingPoint gml:id="stations.27>
      <gml:name codeSpace="">Alice Springs</gml:name>
      <gml:name codeSpace="">ALIC</gml:name>
      <sa:sampledFeature>
         <gsml:GeologicUnit gml:id="geologicunit.32785">
             <gml:description>Metamorphic bedrock</gml:description>
             <gml:name codeSpace="">urn:cgi:feature:SomeAuthority:GeologicUnit:32785</gml:name>
         </gsml:GeologicUnit>
      </sa:sampledFeature>
      <sa:relatedObservation xlink:href="urn:cgi:feature:SomeAuthority:GeologicUnit:32785" />
      <sa:position>
          <gml:Point srsName="urn:x-ogc:def:crs:EPSG:4326">
              <gml:pos>-23.6701 133.8855</gml:pos>
          </gml:Point>
      </sa:position>
    </sa:SamplingPoint>

* The properties ``sa:sampledFeature`` can reference any other feature type, inline (included in the response) or by reference (an ``xlink:href`` URL or URN). This is a very powerful capability called polymorphism.
* Derivation of new types provides an extension point, allowing information models to be reused and extended in a way that supports backwards compatibility.

* Multiple sampling points can share a single GeologicUnit.

* Each GeologicUnit could have further properties describing in detail the properties of the rock, such as colour, weathering, lithology, or relevant geologic events. 

Relationships and interoperability
``````````````````````````````````


Application schemas
```````````````````

