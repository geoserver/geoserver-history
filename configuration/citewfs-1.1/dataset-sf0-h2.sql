-- H2 setup script for wfs 1.1 sf0 cite dataset

drop table "PrimitiveGeoFeature";
create table "PrimitiveGeoFeature" ( "description" varchar, "name" varchar );

alter table "PrimitiveGeoFeature" add "surfaceProperty" blob; 
alter table "PrimitiveGeoFeature" add "pointProperty" blob; 
alter table "PrimitiveGeoFeature" add "curveProperty" blob; 
alter table "PrimitiveGeoFeature" add "intProperty" int8 not null;
alter table "PrimitiveGeoFeature" add "uriProperty" varchar;
alter table "PrimitiveGeoFeature" add "measurand" float not null;
alter table "PrimitiveGeoFeature" add "dateTimeProperty" datetime;
alter table "PrimitiveGeoFeature" add "dateProperty" datetime;
alter table "PrimitiveGeoFeature" add "decimalProperty" decimal(4,2) not null;
alter table "PrimitiveGeoFeature" add "relatedFeature" varchar; 
alter table "PrimitiveGeoFeature" add "id" varchar not null;
alter table "PrimitiveGeoFeature" add primary key ( "id" );
alter table "PrimitiveGeoFeature" add constraint "relatedFeature_fk" foreign key ("relatedFeature") references "PrimitiveGeoFeature" ("id");

INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f001', 'name-f001', NULL, GeomFromText('POINT(2.00342 39.73245)',4326), NULL, 155, 'http://www.opengeospatial.org/', 12765, NULL, ParseDateTime('2006-10-25 GMT','yyyy-MM-dd','en','GMT'), 5.03, NULL, 'f001');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f002', 'name-f002', NULL, GeomFromText('POINT(0.22601 59.41276)',4326), NULL, 154, 'http://www.opengeospatial.org/', 12769, NULL, ParseDateTime('2006-10-23 GMT','yyyy-MM-dd','en','GMT'), 4.02, NULL, 'f002');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f003', 'name-f003',  NULL, NULL, GeomFromText('LINESTRING(9.799 46.074,10.466 46.652,11.021 47.114)',4326) , 180, NULL, 672.1, NULL, ParseDateTime('2006-09-01 GMT','yyyy-MM-dd','en','GMT'), 12.92, NULL, 'f003');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f008', 'name-f008',  GeomFromText('POLYGON((30.899 45.174,30.466 45.652,30.466 45.891,30.899 45.174))',4326) , NULL, NULL, 300, NULL, 783.5, '2006-06-28T07:08:00+02:00', ParseDateTime('2006-12-12 GMT','yyyy-MM-dd z','en','GMT'), 18.92, NULL, 'f008');
INSERT INTO "PrimitiveGeoFeature" VALUES (NULL, 'name-f015',  NULL, GeomFromText('POINT(-10.52 34.94)',4326), NULL, -900, NULL, 2.4, NULL, NULL, 7.90, NULL, 'f015');

-- xlink
--INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f201', 'name-f201',  NULL, GeomFromText('POINT(-2.19433 49.84136)',4326), NULL, -12678967543233, NULL, -0.1278, NULL, NULL, 21980.02, 'f003','f201');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f201', 'name-f201',  NULL, GeomFromText('POINT(-2.19433 49.84136)',4326), NULL, -12678967543233, NULL, -0.1278, NULL, NULL, 21.98, 'f003','f201');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f202', 'name-f202',  NULL, NULL, NULL, 4567, NULL, 1445.57, '2007-08-21T14:47:24+01:00', NULL, 3.14, 'f201','f202');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f205', 'name-f205', NULL, GeomFromText('POINT(9.19251 49.63245)',4326), NULL, -1234, NULL, 1594.01, NULL, NULL, 9.86, NULL,'f205');
--INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f206', 'name-f206', NULL, NULL, GeomFromText('LINESTRING(8.799 36.074,9.466 36.652,10.021 37.114)',4326), 0, NULL, 2.03391e4, NULL, NULL, 11.45, NULL,'f206');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f206', 'name-f206', NULL, NULL, GeomFromText('LINESTRING(8.799 36.074,9.466 36.652,10.021 37.114)',4326), 0, NULL, 2.03, NULL, NULL, 11.45, NULL,'f206');
--INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f207', 'name-f207', NULL, NULL, NULL, 5211, NULL, 20.54, NULL, NULL, 2007.08, 'f205','f207');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f207', 'name-f207', NULL, NULL, NULL, 5211, NULL, 20.54, NULL, NULL, 20.07, 'f205','f207');
INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f208', 'name-f208', NULL, NULL, NULL, 2100, NULL, 19.43, NULL, NULL, 2.8, 'f205','f208');
UPDATE "PrimitiveGeoFeature" SET "relatedFeature" = 'f206' WHERE "id" = 'f205';
UPDATE "PrimitiveGeoFeature" SET "relatedFeature" = 'f207' WHERE "id" = 'f206';
--INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f209', 'name-f209', NULL, NULL, NULL, 882, NULL, 26.33, NULL, NULL, 1000.00 'f209');
--INSERT INTO "PrimitiveGeoFeature" VALUES ('description-f210', 'name-f210', NULL, NULL, NULL, 967, NULL, 26.56, NULL, NULL, 8.57 'f210');

drop table "AggregateGeoFeature";
create table "AggregateGeoFeature" ( "description" varchar, "name" varchar );

alter table "AggregateGeoFeature" add "multiPointProperty" blob;
alter table "AggregateGeoFeature" add "multiCurveProperty" blob;
alter table "AggregateGeoFeature" add "multiSurfaceProperty" blob;
alter table "AggregateGeoFeature" add "doubleProperty" float not null;
alter table "AggregateGeoFeature" add "intRangeProperty" varchar;
alter table "AggregateGeoFeature" add "strProperty" varchar not null;
alter table "AggregateGeoFeature" add "featureCode" varchar not null;
alter table "AggregateGeoFeature" add "id" varchar not null; 
alter table "AggregateGeoFeature" add primary key ( "id" );

INSERT INTO "AggregateGeoFeature" VALUES ('description-f005','name-f005',GeomFromText('MULTIPOINT(29.86 70.83,31.08 68.87,32.19 71.96)',4326),NULL,NULL,2012.78,NULL,'Ma quande lingues coalesce, li grammatica del resultant lingue es plu simplic e regulari quam ti del coalescent lingues. Li nov lingua franca va esser plu simplic e regulari quam li existent Europan lingues.','BK030','f005');
INSERT INTO "AggregateGeoFeature" VALUES ('description-f009','name-f009',NULL,GeomFromText('MULTILINESTRING((-5.899 55.174,-5.466 55.652,-5.899 55.891,-5.899 58.174,-5.466 58.652,-5.899 58.891),(-5.188 53.265,-4.775 54.354,-4.288 52.702,-4.107 53.611,-4.010 55.823))',4326),NULL,20.01,NULL,'Ma quande lingues coalesce, li grammatica del resultant.','GB007','f009');
INSERT INTO "AggregateGeoFeature" VALUES ('description-f010','name-f010',NULL,NULL,GeomFromText('MULTIPOLYGON(((20 50,19 54,20 55,30 60,28 52,27 51,29 49,27 47,20 50),(25 55,25.2 56,25.1 56,25 55)),((20.0 35.5,24.0 35.0,28.0 35.0,27.5 39.0,22.0 37.0,20.0 35.5),(26.0 36.0,25.0 37.0,27.0 36.8,26.0 36.0)))',4326),24510,NULL,' Ma quande lingues coalesce, li grammatica del resultant lingue es plu simplic e regulari quam ti del coalescent lingues. Li nov lingua franca va esser plu simplic e regulari quam li existent Europan lingues.','AK020','f010');
INSERT INTO "AggregateGeoFeature" VALUES (NULL,'name-f016',NULL,NULL,GeomFromText('MULTIPOLYGON(((6.0 57.5, 8.0 57.5, 8.0 60.0, 9.0 62.5, 5.0 62.5,6.0 60.0,6.0 57.5),(6.5 58.0,6.5 59.0,7.0 59.0,6.5 58.0)))',4326),-182.9,NULL,'In rhoncus nisl sit amet sem.','EE010','f016');

--xlink
INSERT INTO "AggregateGeoFeature" VALUES ('description-f203','name-f203',NULL,NULL,NULL,1267.43233E12,NULL,'tlhIngan Hol yejHaD jInmol ''oH Qo''noS QonoS''e''.','AA050','f203');

drop table "EntitéGénérique";
create table "EntitéGénérique" ( "description" varchar, "name" varchar );

alter table "EntitéGénérique" add "attribut.Géométrie" blob;
alter table "EntitéGénérique" add "boolProperty" boolean not null;
alter table "EntitéGénérique" add "str4Property" varchar not null;
alter table "EntitéGénérique" add "featureRef" varchar;
alter table "EntitéGénérique" add "id" varchar not null; 
alter table "EntitéGénérique" add primary key ( "id" );

INSERT INTO "EntitéGénérique" VALUES ('description-f004','name-f004',GeomFromText('POLYGON((0 60.5,0 64,6.25 64,6.25 60.5,0 60.5),(2 61.5,2 62.5,4 62,2 61.5))',4326),true,'abc3','name-f003', 'f004');
INSERT INTO "EntitéGénérique" VALUES ('description-f007','name-f007',GeomFromText('POLYGON((15 35,16 40,20 39,22.5 37,18 36,15 35),(17.5 37.1,17.6 37.2,17.7 37.3,17.8 37.4,17.9 37.5,17.9 37,17.5 37.1))',4326),false,'def4',NULL,'f007');
INSERT INTO "EntitéGénérique" VALUES ('description-f017','name-f017',GeomFromText('LINESTRING(4.899 50.174,5.466 52.652,6.899 53.891,7.780 54.382,8.879 54.982)',4326),false,'qrst','name-f015','f017');

--xlink
INSERT INTO "EntitéGénérique" VALUES (NULL,'name-f204',GeomFromText('LINESTRING(4.899 50.174,5.466 52.652,6.899 53.891,7.780 54.382,8.879 54.982)',4326),false,'~a2+',NULL,'f204');
