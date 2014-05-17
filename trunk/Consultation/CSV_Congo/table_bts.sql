-- Table: table_bts

-- DROP TABLE table_bts;

CREATE TABLE table_bts
(
  nom character varying(30),
  localite character varying(30),
  longitude numeric,
  latitude numeric,
  the_geom geometry,
  id_pkm bigserial,
  region character varying(40),
  tchm real,
  tchmbh real,
  bh real,
  bhtr real,
  crd real,
  cst real,
  cssr real,
  tchbr real,
  tchcr real,
  tchdr real,
  tchbrbh real,
  tchcrbh real,
  tchdrbh real,
  sdcchbrbh real,
  sdcchcrbh real,
  sdcchdrbh real,
  csr real,
  cdr real,
  cdrbh real,
  hosucces real,
  hodlqr real,
  houlqr real,
  hodllr real,
  houllr real,
  intrabsshodr real,
  interbsshodr real,
  hopb real,
  rxl real,
  rxq real,
  smsasr real,
  rsmsr real,
  smslr real,
  csrbh real,
  tchhm real,
  identifiant bigserial,
  CONSTRAINT id_pkm_bts PRIMARY KEY (id_pkm),
  CONSTRAINT enforce_dims_the_geom CHECK (st_ndims(the_geom) = 2),
  CONSTRAINT enforce_geotype_the_geom CHECK (geometrytype(the_geom) = 'POINT'::text OR the_geom IS NULL),
  CONSTRAINT enforce_srid_the_geom CHECK (st_srid(the_geom) = -1)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE table_bts OWNER TO postgres;
