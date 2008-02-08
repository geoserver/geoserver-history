-- H2 setup script for wfs 1.1 sf2 cite dataset

-- association tables
CREATE TABLE "geometry" ("id" varchar, "name" varchar, "description" varchar, "type" varchar, "geometry" blob ); 
CREATE TABLE "multi_geometry" ( "id" varchar, "mgid" varchar, "ref" boolean );
CREATE TABLE "geometry_associations" ( "fid" varchar, "gname" varchar, "gid" varchar, "ref" boolean );

CREATE TABLE "feature_relationships" ("table" varchar, "col" varchar);
CREATE TABLE "feature_associations" ( "fid" varchar, "rtable" varchar, "rcol" varchar, "rfid" varchar );

-- PrimitiveGeoFeature
--DROP TABLE "PrimitiveGeoFeature";
CREATE TABLE "PrimitiveGeoFeature" ( "description" varchar, "name" varchar );

ALTER TABLE "PrimitiveGeoFeature" add "surfaceProperty" blob; 
ALTER TABLE "PrimitiveGeoFeature" add "pointProperty" blob; 
ALTER TABLE "PrimitiveGeoFeature" add "curveProperty" blob; 
ALTER TABLE "PrimitiveGeoFeature" add "intProperty" int8 not null;
ALTER TABLE "PrimitiveGeoFeature" add "uriProperty" varchar;
ALTER TABLE "PrimitiveGeoFeature" add "measurand" float not null;
ALTER TABLE "PrimitiveGeoFeature" add "dateTimeProperty" datetime;
ALTER TABLE "PrimitiveGeoFeature" add "dateProperty" datetime;
ALTER TABLE "PrimitiveGeoFeature" add "decimalProperty" decimal(4,2) not null;
ALTER TABLE "PrimitiveGeoFeature" add "relatedFeature" varchar; 
ALTER TABLE "PrimitiveGeoFeature" ADD "id" int AUTO_INCREMENT(1);
ALTER TABLE "PrimitiveGeoFeature" ADD CONSTRAINT "surfacePropertyGeometryType" CHECK GeometryType("surfaceProperty") IS NULL OR GeometryType("surfaceProperty") = 'POLYGON';
ALTER TABLE "PrimitiveGeoFeature" ADD CONSTRAINT "pointPropertyGeometryType" CHECK GeometryType("pointProperty") IS NULL OR GeometryType("pointProperty") = 'POINT';
ALTER TABLE "PrimitiveGeoFeature" ADD CONSTRAINT "curvePropertyGeometryType" CHECK GeometryType("curveProperty") IS NULL OR GeometryType("curveProperty") = 'LINESTRING';


--ALTER TABLE "PrimitiveGeoFeature" add constraint "relatedFeature_fk" foreign key ("relatedFeature") references "PrimitiveGeoFeature" ("id");
INSERT INTO "feature_relationships" VALUES ('PrimitiveGeoFeature', 'relatedFeature' );

-- PrimitiveGeoFeature.f001
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f001', 'name-f001', NULL, NULL, NULL, 155, 'http://www.opengeospatial.org/', 12765, NULL, ParseDateTime('2006-10-25 GMT','yyyy-MM-dd','en','GMT'), 5.03, NULL, 1);
INSERT INTO "geometry" values ('g003', NULL, 'description-g003', 'POINT', GeomFromText('POINT(2.00342 39.73245)',4326) );
INSERT INTO "geometry_associations" VALUES ('PrimitiveGeoFeature.1', 'pointProperty', 'g003', false );

-- PrimitiveGeoFeature.f002
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f002', 'name-f002', NULL, NULL, NULL, 154, 'http://www.opengeospatial.org/', 12769, NULL, ParseDateTime('2006-10-23 GMT','yyyy-MM-dd','en','GMT'), 4.02, NULL, 2);
INSERT INTO "geometry" values ('g001', NULL, 'description-g001', 'POINT', GeomFromText('POINT(0.22601 59.41276)',4326));
INSERT INTO "geometry_associations" VALUES ('PrimitiveGeoFeature.2', 'pointProperty', 'g001', false );

-- PrimitiveGeoFeature.f003
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f003', 'name-f003',  NULL, NULL, GeomFromText('LINESTRING(9.799 46.074,10.466 46.652,11.021 47.114)',4326) , 180, NULL, 672.1, NULL, ParseDateTime('2006-09-01 GMT','yyyy-MM-dd','en','GMT'), 12.92, NULL, 3);

-- PrimitiveGeoFeature.f008
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f008', 'name-f008', NULL, NULL, NULL, 300, NULL, 783.5, '2006-06-28T07:08:00+02:00', ParseDateTime('2006-12-12 GMT','yyyy-MM-dd z','en','GMT'), 18.92, NULL, 8);
INSERT INTO "geometry" values ('g005', 'MU3', NULL, 'POLYGON', GeomFromText('POLYGON((30.899 45.174,30.466 45.652,30.466 45.891,30.899 45.174))',4326));
INSERT INTO "geometry_associations" VALUES ('PrimitiveGeoFeature.8', 'polygonProperty', 'g005', false );

-- PrimitiveGeoFeature.f015
INSERT INTO "PrimitiveGeoFeature" VALUES (NULL, 'name-f015',  NULL, GeomFromText('POINT(-10.52 34.94)',4326), NULL, -900, NULL, 2.4, NULL, NULL, 7.90, NULL, 15);

-- PrimitiveGeoFeature.f091 (xlink)
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f091', 'name-f091',  NULL, NULL, NULL, -12678967543233, NULL, -0.1278, NULL, NULL, 21.98, '3',91);
INSERT INTO "geometry" values ('g091', NULL, NULL, 'POINT', GeomFromText('POINT(-2.19433 49.84136)',4326));
INSERT INTO "geometry_associations" VALUES ('PrimitiveGeoFeature.91', 'pointProperty', 'g091', false );
INSERT INTO "feature_associations" VALUES ('PrimitiveGeoFeature.91', 'PrimitiveGeoFeature', 'id', '3' );

-- PrimitiveGeoFeature.f092 (xlink)
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f092', 'name-f092',  NULL, NULL, NULL, 4567, NULL, 1445.57, '2007-08-21T14:47:24+01:00', NULL, 3.14, '91',92);
INSERT INTO "feature_associations" VALUES ('PrimitiveGeoFeature.92', 'PrimitiveGeoFeature', 'id', '91' );

-- PrimitiveGeoFeature.f093 (xlink)
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f093', 'name-f093', NULL, GeomFromText('POINT(9.19251 49.63245)',4326), NULL, -1234, NULL, 1594.01, NULL, NULL, 9.86, NULL,93);
INSERT INTO "feature_associations" VALUES ('PrimitiveGeoFeature.93', 'PrimitiveGeoFeature', 'id', '94' );

-- PrimitiveGeoFeature.f094 (xlink)
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f094', 'name-f094', NULL, NULL, GeomFromText('LINESTRING(8.799 36.074,9.466 36.652,10.021 37.114)',4326), 0, NULL, 2.03, NULL, NULL, 11.45,'http://vancouver1.demo.galdosinc.com/wfs/http?request=GetFeature&service=WFS&version=1.1.0&typename=sf:PrimitiveGeoFeature#f205',94);
INSERT INTO "feature_associations" VALUES ('PrimitiveGeoFeature.94', 'PrimitiveGeoFeature', 'id', 'http://vancouver1.demo.galdosinc.com/wfs/http?request=GetFeature&service=WFS&version=1.1.0&typename=sf:PrimitiveGeoFeature#f205' );

-- AggregateGeoFeature
--DROP TABLE "AggregateGeoFeature";
CREATE TABLE "AggregateGeoFeature" ( "description" varchar, "name" varchar );

ALTER TABLE "AggregateGeoFeature" add "multiPointProperty" blob;
ALTER TABLE "AggregateGeoFeature" add "multiCurveProperty" blob;
ALTER TABLE "AggregateGeoFeature" add "multiSurfaceProperty" blob;
ALTER TABLE "AggregateGeoFeature" add "doubleProperty" float not null;
ALTER TABLE "AggregateGeoFeature" add "intRangeProperty" varchar;
ALTER TABLE "AggregateGeoFeature" add "strProperty" varchar not null;
ALTER TABLE "AggregateGeoFeature" add "featureCode" varchar not null;
ALTER TABLE "AggregateGeoFeature" ADD "id" int AUTO_INCREMENT(1);
ALTER TABLE "AggregateGeoFeature" ADD CONSTRAINT "multiPointPropertyGeometryType" CHECK GeometryType("multiPointProperty") IS NULL OR GeometryType("multiPointProperty") = 'MULTIPOINT';
ALTER TABLE "AggregateGeoFeature" ADD CONSTRAINT "multiCurvePropertyGeometryType" CHECK GeometryType("multiCurveProperty") IS NULL OR GeometryType("multiCurveProperty") = 'MULTILINESTRING';
ALTER TABLE "AggregateGeoFeature" ADD CONSTRAINT "multiSurfacePropertyGeometryType" CHECK GeometryType("multiSurfaceProperty") IS NULL OR GeometryType("multiSurfaceProperty") = 'MULTIPOLYGON';


-- AggregateGeoFeature.f005
INSERT INTO "AggregateGeoFeature" VALUES ('description-f005','name-f005',GeomFromText('MULTIPOINT(29.86 70.83,31.08 68.87,32.19 71.96)',4326),NULL,NULL,2012.78,NULL,'Ma quande lingues coalesce, li grammatica del resultant lingue es plu simplic e regulari quam ti del coalescent lingues. Li nov lingua franca va esser plu simplic e regulari quam li existent Europan lingues.','BK030',5);

-- AggregateGeoFeature.f009
INSERT INTO "AggregateGeoFeature" VALUES ('description-f009','name-f009',NULL,GeomFromText('MULTILINESTRING((-5.899 55.174,-5.466 55.652,-5.899 55.891,-5.899 58.174,-5.466 58.652,-5.899 58.891),(-5.188 53.265,-4.775 54.354,-4.288 52.702,-4.107 53.611,-4.010 55.823))',4326),NULL,20.01,NULL,'Ma quande lingues coalesce, li grammatica del resultant.','GB007',9);

-- AggregateGeoFeature.f010
INSERT INTO "AggregateGeoFeature" VALUES ('description-f010','name-f010',NULL,NULL,GeomFromText('MULTIPOLYGON(((20 50,19 54,20 55,30 60,28 52,27 51,29 49,27 47,20 50),(25 55,25.2 56,25.1 56,25 55)),((20.0 35.5,24.0 35.0,28.0 35.0,27.5 39.0,22.0 37.0,20.0 35.5),(26.0 36.0,25.0 37.0,27.0 36.8,26.0 36.0)))',4326),24510,NULL,' Ma quande lingues coalesce, li grammatica del resultant lingue es plu simplic e regulari quam ti del coalescent lingues. Li nov lingua franca va esser plu simplic e regulari quam li existent Europan lingues.','AK020',10);

-- AggregateGeoFeature.f016
INSERT INTO "AggregateGeoFeature" VALUES (NULL,'name-f016',NULL,NULL,GeomFromText('MULTIPOLYGON(((6.0 57.5, 8.0 57.5, 8.0 60.0, 9.0 62.5, 5.0 62.5,6.0 60.0,6.0 57.5),(6.5 58.0,6.5 59.0,7.0 59.0,6.5 58.0)))',4326),-182.9,NULL,'In rhoncus nisl sit amet sem.','EE010',16);

-- EntitéGénérique 
--DROP TABLE "EntitéGénérique";
CREATE TABLE "EntitéGénérique" ( "description" varchar, "name" varchar );

ALTER TABLE "EntitéGénérique" add "attribut.Géométrie" blob;
ALTER TABLE "EntitéGénérique" add "boolProperty" boolean not null;
ALTER TABLE "EntitéGénérique" add "str4Property" varchar not null;
ALTER TABLE "EntitéGénérique" add "featureRef" varchar;
ALTER TABLE "EntitéGénérique" ADD "id" int AUTO_INCREMENT(1);


-- EntitéGénérique.f004
INSERT INTO "EntitéGénérique" VALUES ('description-f004','name-f004',NULL,true,'abc3','name-f003', 4);
INSERT INTO "geometry" VALUES ('g002', 'MU1', NULL, 'POLYGON', GeomFromText('POLYGON((0 60.5,0 64,6.25 64,6.25 60.5,0 60.5),(2 61.5,2 62.5,4 62,2 61.5))',4326));
INSERT INTO "geometry_associations" VALUES ( 'EntitéGénérique.4', 'attribut.Géométrie', 'g002', false );

-- EntitéGénérique.f007
INSERT INTO "EntitéGénérique" VALUES ('description-f007','name-f007',NULL,false,'def4',NULL,7);
INSERT INTO "geometry" VALUES ('g004', 'MU1', NULL, 'POLYGON', GeomFromText('POLYGON((15 35,16 40,20 39,22.5 37,18 36,15 35),(17.5 37.1,17.6 37.2,17.7 37.3,17.8 37.4,17.9 37.5,17.9 37,17.5 37.1))',4326));
INSERT INTO "geometry_associations" VALUES ( 'EntitéGénérique.7', 'attribut.Géométrie', 'g004', false );

-- EntitéGénérique.f017
INSERT INTO "EntitéGénérique" VALUES ('description-f017','name-f017',GeomFromText('LINESTRING(4.899 50.174,5.466 52.652,6.899 53.891,7.780 54.382,8.879 54.982)',4326),false,'qrst','name-f015',17);

-- LinkedFeature
CREATE TABLE "LinkedFeature" ( "description" varchar, "name" varchar ); 
ALTER TABLE "LinkedFeature" ADD "reference" varchar;
ALTER TABLE "LinkedFeature" ADD "extent" blob;
ALTER TABLE "LinkedFeature" ADD "id" int AUTO_INCREMENT(1);

INSERT INTO "feature_relationships" VALUES ( 'LinkedFeature', 'reference' );

-- LinkedFeature.f201
INSERT INTO "LinkedFeature" VALUES ( NULL, 'name-f201', '91', NULL, 201 );
INSERT INTO "feature_associations" VALUES ( 'LinkedFeature.201', 'PrimitiveGeoFeature', 'id', '91' );

-- LinkedFeature.f202
INSERT INTO "LinkedFeature" VALUES ( 'description-f202', 'name-f202', '92', NULL, 202 );
INSERT INTO "geometry" VALUES ( 'g202', NULL, NULL, 'POINT', GeomFromText('POINT(-2.17433 49.86136)' ,4326));
INSERT INTO "geometry_associations" VALUES ('LinkedFeature.202', 'extent', 'g202', false);
INSERT INTO "feature_associations" VALUES ( 'LinkedFeature.202', 'PrimitiveGeoFeature', 'id', '92' );

-- LinkedFeature.f203
INSERT INTO "LinkedFeature" VALUES ( 'description-f203', 'name-f203', '204', NULL, '203' );
INSERT INTO "feature_associations" VALUES ( 'LinkedFeature.203', 'LinkedFeature', 'id', '204' );

-- LinkedFeature.f204
INSERT INTO "LinkedFeature" VALUES ( 'description-f204', 'name-f204', '201', NULL, '204' );
INSERT INTO "geometry" VALUES ('g204', NULL, NULL, 'MULTIPOINT', NULL );
INSERT INTO "multi_geometry" VALUES ('g204', 'g003', true );
INSERT INTO "multi_geometry" VALUES ('g204', 'g202', true );
INSERT INTO "geometry_associations" VALUES ('LinkedFeature.204','extent', 'g204', false );
INSERT INTO "feature_associations" VALUES ( 'LinkedFeature.204', 'LinkedFeature', 'id', '201' );

-- LinkedFeature.f205
INSERT INTO "LinkedFeature" VALUES ( NULL, 'name-f205', '203', NULL, 205 );
INSERT INTO "geometry_associations" VALUES ('LinkedFeature.205','extent','g002',true);
INSERT INTO "feature_associations" VALUES ('LinkedFeature.205', 'LinkedFeature', 'id', '203');

-- LinkedFeature.f206
INSERT INTO "LinkedFeature" VALUES ( 'description-f206', 'name-f206','207', GeomFromText('POINT(9.19251 49.63245)',4326),206);
INSERT INTO "feature_associations" VALUES ('LinkedFeature.206', 'LinkedFeature', 'id', '207');

-- LinkedFeature.f207
INSERT INTO "LinkedFeature" VALUES ( 'description-f207', 'name-f207', '210', NULL,207 );
INSERT INTO "feature_associations" VALUES ('LinkedFeature.207', 'LinkedFeature', 'id', '210');

-- LinkedFeature.f208
INSERT INTO "LinkedFeature" VALUES ( 'description-f208', 'name-f208', 'ftp://vancouver1.demo.galdosinc.com/wfs/http?request=GetFeature&service=WFS&version=1.1.0&typename=sf:LinkedFeature#f205', NULL, '208');
INSERT INTO "feature_associations" VALUES ('LinkedFeature.208', 'NonExistant', 'id', 'ftp://vancouver1.demo.galdosinc.com/wfs/http?request=GetFeature&service=WFS&version=1.1.0&typename=sf:LinkedFeature#f205' );

-- LinkedFeature.f209
INSERT INTO "LinkedFeature" VALUES ( 'description-f209', 'name-f209', '_6c566516-a435-11dc-8314-0800200c9a66', NULL,209);
INSERT INTO "feature_associations" VALUES ('LinkedFeature.209', 'NonExistant', 'id', '_6c566516-a435-11dc-8314-0800200c9a66' );

-- LinkedFeature.f210
INSERT INTO "LinkedFeature" VALUES ( 'description-f210', 'name-f210','206', NULL,210 );
INSERT INTO "feature_associations" VALUES ('LinkedFeature.210', 'LinkedFeature', 'id', '206');
