-- This file resets the locking table.  It should only need to be used
-- by developers who mess up the locking, since if all the locks work
-- on cite then the table does not need to be reset.  It's only if the
-- locks fail.

drop table "Locks";


-- Name: LockFeature Type: TABLE Owner: cite
--

CREATE TABLE "Locks" (
	"boundedBy" geometry,
	"id" character varying, 
	"pointProperty" geometry,
	CHECK ((srid("pointProperty") = 32615)),
	CHECK (((geometrytype("pointProperty") = 'POINT'::text) OR ("pointProperty" IS NULL))),
	CHECK ((srid("boundedBy") = 32615)),
	CHECK (((geometrytype("boundedBy") = 'POLYGON'::text) OR ("boundedBy" IS NULL)))
);


GRANT ALL ON "Locks" TO cite;


--COPY "geometry_columns" FROM stdin;
 --	public	Locks	pointProperty	2	32615	POINT	17539	21	\N
 --	public	Locks	boundedBy	2	32615	POLYGON	17539	22	\N
--\.
--
-- Data for TOC Entry ID 39 (OID 113496)
--
-- Name: county Type: TABLE DATA Owner: public
--


-- Name: LockFeature Type: TABLE DATA Owner: public
--


COPY "Locks" FROM stdin;
\N	lfla0001	\N
\N	lfla0002	\N
\N	lfla0003	\N
\N	lfla0004	\N
\N	gfwlla0001	\N
\N	gfwlla0002	\N
\N	gfwlla0003	\N
\N	gfwlla0004	\N
\N	lfbt0001	\N
\N	lfbt0002	\N
\N	lfbt0003	\N
\N	lfbt0004	\N
\N	lfbt0005	\N
\N	lfbt0006	\N
\N	gfwlbt0001	\N
\N	gfwlbt0002	\N
\N	gfwlbt0003	\N
\N	gfwlbt0004	\N
\N	gfwlbt0005	\N
\N	gfwlbt0006	\N
\N	lfe0001	\N
\N	lfe0002	\N
\N	lfe0003	\N
\N	lfe0004	\N
\N	gfwle0001	\N
\N	gfwle0002	\N
\N	gfwle0003	\N
\N	gfwle0004	\N
\N	lfra0001	\N
\N	lfra0002	\N
\N	lfra0003	\N
\N	lfra0004	\N
\N	lfra0005	\N
\N	lfra0006	\N
\N	lfra0007	\N
\N	lfra0008	\N
\N	lfra0009	\N
\N	lfra0010	\N
\N	gfwlra0001	\N
\N	gfwlra0002	\N
\N	gfwlra0003	\N
\N	gfwlra0004	\N
\N	gfwlra0005	\N
\N	gfwlra0006	\N
\N	gfwlra0007	\N
\N	gfwlra0008	\N
\N	gfwlra0009	\N
\N	gfwlra0010	\N
\N	lfrs0001	\N
\N	lfrs0002	\N
\N	lfrs0003	\N
\N	lfrs0004	\N
\N	lfrs0005	\N
\N	lfrs0006	\N
\N	lfrs0007	\N
\N	lfrs0008	\N
\N	lfrs0009	\N
\N	lfrs0010	\N
\N	gfwlrs0001	\N
\N	gfwlrs0002	\N
\N	gfwlrs0003	\N
\N	gfwlrs0004	\N
\N	gfwlrs0005	\N
\N	gfwlrs0006	\N
\N	gfwlrs0007	\N
\N	gfwlrs0008	\N
\N	gfwlrs0009	\N
\N	gfwlrs0010	\N
\.

UPDATE "Locks" SET "boundedBy" = GeometryFromText('POLYGON((500000 500000,500000 500100,500100 500100,500100 500000,500000 500000))', 32615) WHERE TRUE;
UPDATE "Locks" SET "pointProperty" = GeometryFromText('POINT(500050 500050)', 32615) WHERE TRUE;

--
-- Data for TOC Entry ID 71 (OID 312570)
--
-- Name: OtherFeature Type: TABLE DATA Owner: public
--


