-- Script d'insertion pour la base de données SAE

-- Buildings
INSERT INTO t_e_building_bui (id, name) VALUES 
(1, 'IUT Annecy'),
(2, 'Tetras'),
(3, 'Campus Principal');

-- Room Types
INSERT INTO t_e_room_type_rty (id, name) VALUES 
(1, 'Amphithéâtre'),
(2, 'Salle de cours'),
(3, 'Laboratoire'),
(4, 'Bureau'),
(5, 'Salle de réunion');

-- Rooms
INSERT INTO t_e_room_roo (id, name, width, length, height, fk_building_id, fk_room_type_id) VALUES 
(1, 'Amphithéâtre A', 15.0, 20.0, 4.0, 1, 1),
(2, 'Salle B101', 8.0, 12.0, 3.0, 1, 2),
(3, 'Lab Info', 10.0, 15.0, 3.5, 2, 3),
(4, 'Bureau Dir', 4.0, 6.0, 2.8, 2, 4),
(5, 'Réunion R1', 6.0, 8.0, 3.0, 3, 5);

-- Lamps
INSERT INTO t_e_lamp_lam (id, id_room, rob_name, lam_posx, lam_posy, lam_posz) VALUES 
(1,1, 'Éclairage scène', 10.0, 15.0, 3.5),
(2,2, 'Éclairage tableau', 4.0, 8.0, 3.0),
(3,3, 'Éclairage laboratoire', 5.0, 7.5, 3.2);

-- Plugs
INSERT INTO t_e_plug_plu (id, id_room, rob_name, plu_posx, plu_posy, plu_posz) VALUES 
(4,1, 'Prise projecteur', 12.0, 18.0, 0.3),
(5,2, 'Prise ordinateur prof', 6.0, 10.0, 0.3),
(6,3, 'Prise station 1', 2.0, 5.0, 0.3);

-- Data Tables
INSERT INTO t_e_table_tab (id, id_room, rob_name, tab_posx, tab_posy, tab_posz) VALUES 
(7,3, 'Poste étudiant 1', 2.0, 3.0, 0.8),
(8,3, 'Poste étudiant 2', 8.0, 3.0, 0.8),
(9,5, 'Poste réunion', 3.0, 4.0, 0.8);

-- Sensor CO2 (avec positions)
INSERT INTO t_e_sensorco2_co2 (id, id_room, rob_name, co2_posx, co2_posy, co2_posz) VALUES 
(10,1, 'Capteur CO2 amphi', 7.5, 10.0, 2.5),
(11,2, 'Capteur CO2 cours', 4.0, 6.0, 2.8),
(12,3, 'Capteur CO2 lab', 5.0, 7.5, 2.5);

-- Sensor 6in1 (avec positions)
INSERT INTO t_e_sensor6in1_sio (id, id_room, rob_name, sio_posx, sio_posy, sio_posz) VALUES 
(13,1, 'Capteur 6en1 amphi', 5.0, 5.0, 3.0),
(14,2, 'Capteur 6en1 cours', 2.0, 3.0, 2.8),
(15,4, 'Capteur 6en1 bureau', 2.0, 3.0, 2.5);

-- Sensor 9in1 (avec positions)
INSERT INTO t_e_sensor9in1_nio (id, id_room, rob_name, nio_posx, nio_posy, nio_posz) VALUES 
(16,3, 'Capteur 9en1 lab', 8.0, 12.0, 3.0),
(17,5, 'Capteur 9en1 réunion', 3.0, 4.0, 2.8);