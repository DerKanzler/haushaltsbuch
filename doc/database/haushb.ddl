CREATE TABLE tkonto(
		konto# IDENTITY NOT NULL,
		kuerzel VARCHAR(5) NOT NULL,
		abldat DATE NOT NULL,
		ktobez VARCHAR(50) NOT NULL,
		buchdat DATE NOT NULL,
		jreinsaldo DECIMAL(11,2) NOT NULL,
		ktotyp CHAR(1) NOT NULL,
		kzog CHAR(1) NOT NULL,
		saldo DECIMAL(11,2) NOT NULL
);

CREATE TABLE tbenutzer(
		benutzer# IDENTITY NOT NULL,
		name VARCHAR(25) NOT NULL,
		passwort VARCHAR(32) NOT NULL,
		konto1# INTEGER,
		konto2# INTEGER,
		konto3# INTEGER,
		format CHAR(2) NOT NULL
);

CREATE TABLE tkoartgruppe(
		koartgrp# IDENTITY NOT NULL,
		koartgrpbez VARCHAR(50) NOT NULL,
		koartgrpkat CHAR(1) NOT NULL,
		koartgrpart CHAR(1) NOT NULL
);

CREATE TABLE tkostenart(
		koart# IDENTITY NOT NULL,
		koartkubez VARCHAR(10) NOT NULL,
		koartbez VARCHAR(50) NOT NULL,
		koartsaldo DECIMAL(11,2) DEFAULT 0.00 NOT NULL,
		koartgrp# INTEGER NOT NULL
);

CREATE TABLE tbuchung(
		buchung# IDENTITY NOT NULL,
		buchbetr DECIMAL(9,2) NOT NULL,
		buchdat DATE NOT NULL,
		buchtext VARCHAR(52) NOT NULL,
		eindat DATE NOT NULL,
		koart# INTEGER,
		kontovon# INTEGER,
		kontonach# INTEGER
);

CREATE TABLE tkontostand(
		ktost# IDENTITY NOT NULL,
		ktostdat DATE NOT NULL,
		ktostsaldo DECIMAL(11,2) NOT NULL,
		konto# INTEGER NOT NULL
);

CREATE TABLE tkostenartsaldo(
		koartsald# IDENTITY NOT NULL,
		koartsalddat DATE NOT NULL,
		koartmonsaldo DECIMAL(11,2) NOT NULL,
		koartjrsaldo DECIMAL(11,2) NOT NULL,
		koartjrsaldovj DECIMAL(11,2) NOT NULL,
		koart# INTEGER NOT NULL
);

CREATE TABLE tvjbuchung(
		vjbuchung# IDENTITY NOT NULL,
		vjbuchbetr DECIMAL(9,2) NOT NULL,
		vjbuchdat DATE NOT NULL,
		vjbuchtext VARCHAR(52) NOT NULL,
		koart# INTEGER,
		kontovon# INTEGER,
		kontonach# INTEGER
);


ALTER TABLE tkonto ADD CONSTRAINT IDX_tkonto_PK PRIMARY KEY (konto#);
ALTER TABLE tkonto ADD CONSTRAINT UQ_tkonto_1 UNIQUE (kuerzel);

ALTER TABLE tbenutzer ADD CONSTRAINT IDX_tbenutzer_PK PRIMARY KEY (benutzer#);
ALTER TABLE tbenutzer ADD CONSTRAINT IDX_tbenutzer_FK0 FOREIGN KEY (konto2#) REFERENCES tkonto (konto#);
ALTER TABLE tbenutzer ADD CONSTRAINT IDX_tbenutzer_FK1 FOREIGN KEY (konto1#) REFERENCES tkonto (konto#);
ALTER TABLE tbenutzer ADD CONSTRAINT IDX_tbenutzer_FK2 FOREIGN KEY (konto3#) REFERENCES tkonto (konto#);

ALTER TABLE tkoartgruppe ADD CONSTRAINT IDX_tkoartgruppe_PK PRIMARY KEY (koartgrp#);
ALTER TABLE tkoartgruppe ADD CONSTRAINT UQ_tkoartgruppe_1 UNIQUE (koartgrpbez);

ALTER TABLE tkostenart ADD CONSTRAINT IDX_tkostenart_PK PRIMARY KEY (koart#);
ALTER TABLE tkostenart ADD CONSTRAINT IDX_tkostenart_FK0 FOREIGN KEY (koartgrp#) REFERENCES tkoartgruppe (koartgrp#);
ALTER TABLE tkostenart ADD CONSTRAINT UQ_tkostenart_1 UNIQUE (koartkubez);

ALTER TABLE tbuchung ADD CONSTRAINT IDX_tbuchung_PK PRIMARY KEY (buchung#);
ALTER TABLE tbuchung ADD CONSTRAINT IDX_tbuchung_FK0 FOREIGN KEY (kontonach#) REFERENCES tkonto (konto#);
ALTER TABLE tbuchung ADD CONSTRAINT IDX_tbuchung_FK1 FOREIGN KEY (koart#) REFERENCES tkostenart (koart#);
ALTER TABLE tbuchung ADD CONSTRAINT IDX_tbuchung_FK2 FOREIGN KEY (kontovon#) REFERENCES tkonto (konto#);

ALTER TABLE tkontostand ADD CONSTRAINT IDX_tkontostand_PK PRIMARY KEY (ktost#);
ALTER TABLE tkontostand ADD CONSTRAINT IDX_tkontostand_FK0 FOREIGN KEY (konto#) REFERENCES tkonto (konto#);

ALTER TABLE tkostenartsaldo ADD CONSTRAINT IDX_tkostenartsaldo_PK PRIMARY KEY (koartsald#);
ALTER TABLE tkostenartsaldo ADD CONSTRAINT IDX_tkostenartsaldo_FK0 FOREIGN KEY (koart#) REFERENCES tkostenart (koart#);

ALTER TABLE tvjbuchung ADD CONSTRAINT IDX_tvjbuchung_PK PRIMARY KEY (vjbuchung#);
ALTER TABLE tvjbuchung ADD CONSTRAINT IDX_tvjbuchung_FK0 FOREIGN KEY (kontonach#) REFERENCES tkonto (konto#);
ALTER TABLE tvjbuchung ADD CONSTRAINT IDX_tvjbuchung_FK1 FOREIGN KEY (kontovon#) REFERENCES tkonto (konto#);
ALTER TABLE tvjbuchung ADD CONSTRAINT IDX_tvjbuchung_FK2 FOREIGN KEY (koart#) REFERENCES tkostenart (koart#);

