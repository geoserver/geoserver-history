-- This file inserts all the appropriate cite data into postgis to run with
-- geoserver.  To work with the tar file of featureType directories it
-- should be run in a database called cite, that either has a user with
-- a password of cite, cite, or that allows anyone read/write access.
-- You can also just run this script in whatever database you like with
-- your user name, but then you have to modify all the user access info
-- of the info.xml files.  Uncommenting lines at 253 will create the cite
-- user and grant access to it on all the relavant tables, the user you are
-- connecting with must have the appropriate permissions to do that.  


-- Uncomment and change this to the user who has permissions to drop and
-- create tables in this db.
-- \connect - cite




--uncomment these if you want to reset everything.
--drop table "Nulls";
--drop table "Points";
--drop table "Other";
--drop table "Lines";
--drop table "Polygons";
--drop table "MLines";
--drop table "MPolygons";
--drop table "MPoints";
--drop table "Seven";
--drop table "Fifteen";
--drop table "Updates";
--drop table "Inserts";
--drop table "Deletes";
--drop table "Locks";
--delete from "geometry_columns" where srid=32615;




--
-- TOC Entry ID 23 (OID 312261)
--
-- Name: SevenFeature Type: TABLE Owner: ciesin
--

CREATE TABLE "Seven" (
	"boundedBy" geometry,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);

--
-- TOC Entry ID 24 (OID 312275)
--
-- Name: NullFeature Type: TABLE Owner: cite
--

CREATE TABLE "Nulls" (
	"gmldescription" character varying,
	"name" character varying,
	"boundedBy" geometry,
	"integers" integer,
	"dates" date,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);

--
-- TOC Entry ID 25 (OID 312300)
--
-- Name: DeleteFeature Type: TABLE Owner: cite
--

CREATE TABLE "Deletes" (
	"boundedBy" geometry,
	"id" character varying,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))

);

--
-- TOC Entry ID 26 (OID 312305)
--
-- Name: InsertFeature Type: TABLE Owner: cite
--

CREATE TABLE "Inserts" (
	"boundedBy" geometry,
	"id" character varying,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);


--
-- TOC Entry ID 27 (OID 312310)
--
-- Name: UpdateFeature Type: TABLE Owner: cite
--

CREATE TABLE "Updates" (
	"boundedBy" geometry,
	"id" character varying,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);

--
-- TOC Entry ID 28 (OID 312315)
--
-- Name: PointFeature Type: TABLE Owner: cite
--

CREATE TABLE "Points" (
	"id" character varying,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL)))
);

--
-- TOC Entry ID 29 (OID 312322)
--
-- Name: LineStringFeature Type: TABLE Owner: cite
--

CREATE TABLE "Lines" (
	"id" character varying,
	"lineStringProperty" geometry,
	CHECK ((srid("lineStringProperty") = 32615)),
	CHECK (((geometrytype("lineStringProperty") = 'LINESTRING'::text) OR ("lineStringProperty" IS NULL)))
);

--
-- TOC Entry ID 30 (OID 312329)
--
-- Name: PolygonFeature Type: TABLE Owner: cite
--

CREATE TABLE "Polygons" (
	"id" character varying,
	"polygonProperty" geometry,
	CHECK ((srid("polygonProperty") = 32615)),
	CHECK (((geometrytype("polygonProperty") = 'POLYGON'::text) OR ("polygonProperty" IS NULL)))
);

--
-- TOC Entry ID 31 (OID 312335)
--
-- Name: MultiPointFeature Type: TABLE Owner: cite
--

CREATE TABLE "MPoints" (
	"id" character varying,
	"multiPointProperty" geometry,
	CHECK ((srid("multiPointProperty") = 32615)),
	CHECK (((geometrytype("multiPointProperty") = 'MULTIPOINT'::text) OR ("multiPointProperty" IS NULL)))
);

--
-- TOC Entry ID 32 (OID 312341)
--
-- Name: MultiLineStringFeature Type: TABLE Owner: cite
--

CREATE TABLE "MLines" (
	"id" character varying,
	"multiLineStringProperty" geometry,
	CHECK ((srid("multiLineStringProperty") = 32615)),
	CHECK (((geometrytype("multiLineStringProperty") = 'MULTILINESTRING'::text) OR ("multiLineStringProperty" IS NULL)))
);

--
-- TOC Entry ID 33 (OID 312348)
--
-- Name: MultiPolygonFeature Type: TABLE Owner: cite
--

CREATE TABLE "MPolygons" (
	"id" character varying,
	"multiPolygonProperty" geometry,
	CHECK ((srid("multiPolygonProperty") = 32615)),
	CHECK (((geometrytype("multiPolygonProperty") = 'MULTIPOLYGON'::text) OR ("multiPolygonProperty" IS NULL)))
);

--
-- TOC Entry ID 34 (OID 312391)
--
-- Name: FifteenFeature Type: TABLE Owner: cite
--

CREATE TABLE "Fifteen" (
	"boundedBy" geometry,
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);

--
-- TOC Entry ID 35 (OID 312430)
--
-- Name: LockFeature Type: TABLE Owner: cite
--

CREATE TABLE "Locks" (
	"gmlname" character varying,
	"id" character varying
);

--
-- TOC Entry ID 36 (OID 312570)
--
-- Name: OtherFeature Type: TABLE Owner: cite
--

CREATE TABLE "Other" (
	"gmldescription" character varying,
	"gmlname" character varying,
	"boundedBy" geometry,
	"pointProperty" geometry,
	"string1" character varying NOT NULL,
	"string2" character varying,
	"integers" integer,
	"dates" date,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);

--
-- Data for TOC Entry ID 37 (OID 16560)
--
-- Name: spatial_ref_sys Type: TABLE DATA Owner: cite
--

--uncomment this line if cite user has not yet been created.
--create user cite;

--uncomment these to grant permissions to cite.  These occur here
-- as the tables need to be created before granting privelages.
--GRANT ALL ON "Nulls" TO cite;
--GRANT ALL ON "Points" TO cite;
--GRANT ALL ON "Other" TO cite;
--GRANT ALL ON "Lines" TO cite;
--GRANT ALL ON "Polygons" TO cite;
--GRANT ALL ON "MLines" TO cite;
--GRANT ALL ON "MPolygons" TO cite;
--GRANT ALL ON "MPoints" TO cite;
--GRANT ALL ON "Seven" TO cite;
--GRANT ALL ON "Fifteen" TO cite;
--GRANT ALL ON "Updates" TO cite;
--GRANT ALL ON "Deletes" TO cite;
--GRANT ALL ON "Inserts" TO cite;
--GRANT ALL ON "Locks" TO cite;
--GRANT ALL ON "geometry_columns" TO cite;


COPY "geometry_columns" FROM stdin;
	cite	Nulls	pointProperty	2	32615	POINT
 	cite	Nulls	boundedBy	2	32615	POLYGON
 	cite	Points	pointProperty	2	32615	POINT
 	cite	Other	pointProperty	2	32615	POINT
	cite	Lines	lineStringProperty	2	32615	LINESTRING
 	cite	Polygons	polygonProperty	2	32615	POLYGON
	cite	MPolygons	multiPolygonProperty	2	32615	MULTIPOLYGON
	cite	MPoints	multiPointProperty	2	32615	MULTIPOINT
	cite	MLines	multiLineStringProperty	2	32615	MULTILINESTRING
 	cite	Other	boundedBy	2	32615	POLYGON
 	cite	Seven	pointProperty	2	32615	POINT
 	cite	Seven	boundedBy	2	32615	POLYGON
 	cite	Fifteen	pointProperty	2	32615	POINT
 	cite	Fifteen	boundedBy	2	32615	POLYGON
 	cite	Updates	pointProperty	2	32615	POINT
 	cite	Updates	boundedBy	2	32615	POLYGON
 	cite	Inserts	pointProperty	2	32615	POINT
 	cite	Inserts	boundedBy	2	32615	POLYGON
 	cite	Deletes	pointProperty	2	32615	POINT
 	cite	Deletes	boundedBy	2	32615	POLYGON
\.
--
-- Data for TOC Entry ID 39 (OID 113496)
--
-- Name: county Type: TABLE DATA Owner: cite
--


 

COPY "Seven" FROM stdin;
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
\.
--
-- Data for TOC Entry ID 59 (OID 312275)
--
-- Name: NullFeature Type: TABLE DATA Owner: cite
--


COPY "Nulls" FROM stdin;
nullFeature	\N	SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	\N	\N	\N
\.
--
-- Data for TOC Entry ID 60 (OID 312300)
--
-- Name: DeleteFeature Type: TABLE DATA Owner: cite
--


COPY "Deletes" FROM stdin;
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	td0001	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	td0002	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	td0003	SRID=32615;POINT(500050 500050)
\.
--
-- Data for TOC Entry ID 61 (OID 312305)
--
-- Name: InsertFeature Type: TABLE DATA Owner: cite
--


COPY "Inserts" FROM stdin;
\.
--
-- Data for TOC Entry ID 62 (OID 312310)
--
-- Name: UpdateFeature Type: TABLE DATA Owner: cite
--


COPY "Updates" FROM stdin;
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	tu0001	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	tu0002	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	tu0003	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	tu0004	SRID=32615;POINT(500050 500050)
\.
--
-- Data for TOC Entry ID 63 (OID 312315)
--
-- Name: PointFeature Type: TABLE DATA Owner: cite
--


COPY "Points" FROM stdin;
t0000	SRID=32615;POINT(500050 500050)
\.
--
-- Data for TOC Entry ID 64 (OID 312322)
--
-- Name: LineStringFeature Type: TABLE DATA Owner: cite
--


COPY "Lines" FROM stdin;
t0001	SRID=32615;LINESTRING(500125 500025,500175 500075)
\.
--
-- Data for TOC Entry ID 65 (OID 312329)
--
-- Name: PolygonFeature Type: TABLE DATA Owner: cite
--


COPY "Polygons" FROM stdin;
t0002	SRID=32615;POLYGON((500225 500025,500225 500075,500275 500050,500275 500025,500225 500025))
\.
--
-- Data for TOC Entry ID 66 (OID 312335)
--
-- Name: MultiPointFeature Type: TABLE DATA Owner: cite
--


COPY "MPoints" FROM stdin;
t0003	SRID=32615;MULTIPOINT(500325 500025,500375 500075)
\.
--
-- Data for TOC Entry ID 67 (OID 312341)
--
-- Name: MultiLineStringFeature Type: TABLE DATA Owner: cite
--


COPY "MLines" FROM stdin;
t0004	SRID=32615;MULTILINESTRING((500425 500025,500475 500075),(500425 500075,500475 500025))
\.
--
-- Data for TOC Entry ID 68 (OID 312348)
--
-- Name: MultiPolygonFeature Type: TABLE DATA Owner: cite
--


COPY "MPolygons" FROM stdin;
t0005	SRID=32615;MULTIPOLYGON(((500525 500025,500550 500050,500575 500025,500525 500025)),((500525 500050,500525 500075,500550 500075,500550 500050,500525 500050)))
\.
--
-- Data for TOC Entry ID 69 (OID 312391)
--
-- Name: FifteenFeature Type: TABLE DATA Owner: cite
--


COPY "Fifteen" FROM stdin;
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)
\.
--
-- Data for TOC Entry ID 70 (OID 312430)
--
-- Name: LockFeature Type: TABLE DATA Owner: cite
--


COPY "Locks" FROM stdin;
\N	lfla0001
\N	lfla0002
\N	lfla0003
\N	lfla0004
\N	gfwlla0001
\N	gfwlla0002
\N	gfwlla0003
\N	gfwlla0004
\N	lfbt0001
\N	lfbt0002
\N	lfbt0003
\N	lfbt0004
\N	lfbt0005
\N	lfbt0006
\N	gfwlbt0001
\N	gfwlbt0002
\N	gfwlbt0003
\N	gfwlbt0004
\N	gfwlbt0005
\N	gfwlbt0006
\N	lfe0001
\N	lfe0002
\N	lfe0003
\N	lfe0004
\N	gfwle0001
\N	gfwle0002
\N	gfwle0003
\N	gfwle0004
\N	lfra0001
\N	lfra0002
\N	lfra0003
\N	lfra0004
\N	lfra0005
\N	lfra0006
\N	lfra0007
\N	lfra0008
\N	lfra0009
\N	lfra0010
\N	gfwlra0001
\N	gfwlra0002
\N	gfwlra0003
\N	gfwlra0004
\N	gfwlra0005
\N	gfwlra0006
\N	gfwlra0007
\N	gfwlra0008
\N	gfwlra0009
\N	gfwlra0010
\N	lfrs0001
\N	lfrs0002
\N	lfrs0003
\N	lfrs0004
\N	lfrs0005
\N	lfrs0006
\N	lfrs0007
\N	lfrs0008
\N	lfrs0009
\N	lfrs0010
\N	gfwlrs0001
\N	gfwlrs0002
\N	gfwlrs0003
\N	gfwlrs0004
\N	gfwlrs0005
\N	gfwlrs0006
\N	gfwlrs0007
\N	gfwlrs0008
\N	gfwlrs0009
\N	gfwlrs0010
\.
--
-- Data for TOC Entry ID 71 (OID 312570)
--
-- Name: OtherFeature Type: TABLE DATA Owner: cite
--


COPY "Other" FROM stdin;
A Single Feature used to test returning of properties	singleFeature	SRID=32615;POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))	SRID=32615;POINT(500050 500050)	always	sometimes	7	2002-12-02
\.
