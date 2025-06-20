-- Migration Flyway V1__Create_initial_schema.sql
-- Création de la base de données PostgreSQL pour le projet SAE

-- Table des bâtiments
CREATE TABLE t_e_building_bui (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Table des types de pièces
CREATE TABLE t_e_room_type_rty (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Table des pièces
CREATE TABLE t_e_room_roo (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    width decimal NOT NULL,
    length decimal NOT NULL,
    height decimal NOT NULL,
    fk_building_id INTEGER,
    fk_room_type_id INTEGER,
    CONSTRAINT fk_room_building FOREIGN KEY (fk_building_id) REFERENCES t_e_building_bui(id) ON DELETE SET NULL,
    CONSTRAINT fk_room_type FOREIGN KEY (fk_room_type_id) REFERENCES t_e_room_type_rty(id) ON DELETE SET NULL
);

-- Table des tables
CREATE TABLE t_e_table_tab (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    tab_posx decimal,
    tab_posy decimal,
    tab_posz decimal,
    tab_sizex decimal,
    tab_sizey decimal,
    tab_sizez decimal,
    CONSTRAINT fk_table_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des portes
CREATE TABLE t_e_door_doo (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    doo_posx decimal,
    doo_posy decimal,
    doo_posz decimal,
    doo_sizex decimal,
    doo_sizey decimal,
    doo_sizez decimal,
    CONSTRAINT fk_door_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des radiateurs
CREATE TABLE t_e_heater_hea (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    hea_posx decimal,
    hea_posy decimal,
    hea_posz decimal,
    hea_sizex decimal,
    hea_sizey decimal,
    hea_sizez decimal,
    CONSTRAINT fk_heater_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des lampes
CREATE TABLE t_e_lamp_lam (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    lam_posx decimal,
    lam_posy decimal,
    lam_posz decimal,
    CONSTRAINT fk_lamp_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des prises
CREATE TABLE t_e_plug_plu (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    plu_posx decimal,
    plu_posy decimal,
    plu_posz decimal,
    CONSTRAINT fk_plug_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des capteurs 6 en 1
CREATE TABLE t_e_sensor6in1_sio (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    sio_posx decimal,
    sio_posy decimal,
    sio_posz decimal,
    CONSTRAINT fk_sensor6in1_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des capteurs 9 en 1
CREATE TABLE t_e_sensor9in1_nio (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    nio_posx decimal,
    nio_posy decimal,
    nio_posz decimal,
    CONSTRAINT fk_sensor9in1_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des capteurs CO2
CREATE TABLE t_e_sensorco2_co2 (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    co2_posx decimal,
    co2_posy decimal,
    co2_posz decimal,
    CONSTRAINT fk_sensorco2_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des sirènes
CREATE TABLE t_e_siren_sir (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    sir_posx decimal,
    sir_posy decimal,
    sir_posz decimal,
    CONSTRAINT fk_siren_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Table des fenêtres
CREATE TABLE t_e_window_win (
    id SERIAL PRIMARY KEY ,
    rob_name VARCHAR(255),
    id_room INTEGER,
    win_posx decimal,
    win_posy decimal,
    win_posz decimal,
    win_sizex decimal,
    win_sizey decimal,
    win_sizez decimal,
    CONSTRAINT fk_window_room FOREIGN KEY (id_room) REFERENCES t_e_room_roo(id) ON DELETE CASCADE
);

-- Index pour améliorer les performances des jointures
CREATE INDEX idx_room_building ON t_e_room_roo(fk_building_id);
CREATE INDEX idx_room_type ON t_e_room_roo(fk_room_type_id);
CREATE INDEX idx_table_room ON t_e_table_tab(id_room);
CREATE INDEX idx_door_room ON t_e_door_doo(id_room);
CREATE INDEX idx_heater_room ON t_e_heater_hea(id_room);
CREATE INDEX idx_lamp_room ON t_e_lamp_lam(id_room);
CREATE INDEX idx_plug_room ON t_e_plug_plu(id_room);
CREATE INDEX idx_sensor6in1_room ON t_e_sensor6in1_sio(id_room);
CREATE INDEX idx_sensor9in1_room ON t_e_sensor9in1_nio(id_room);
CREATE INDEX idx_sensorco2_room ON t_e_sensorco2_co2(id_room);
CREATE INDEX idx_siren_room ON t_e_siren_sir(id_room);
CREATE INDEX idx_window_room ON t_e_window_win(id_room);