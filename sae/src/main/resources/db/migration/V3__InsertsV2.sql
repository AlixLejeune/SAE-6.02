-- Sirens (avec positions et tailles)
INSERT INTO t_e_siren_sir (id, id_room, rob_name, sir_posx, sir_posy, sir_posz) VALUES 
(18, 1, 'Sirène amphithéâtre', 14.0, 19.0, 3.8),
(19, 2, 'Sirène salle cours', 7.5, 11.5, 2.9),
(20, 3, 'Sirène laboratoire', 9.5, 14.5, 3.4);

-- Heaters (avec positions et tailles)
INSERT INTO t_e_heater_hea (id, id_room, rob_name, hea_posx, hea_posy, hea_posz, hea_sizex, hea_sizey, hea_sizez) VALUES 
(21, 1, 'Radiateur amphi gauche', 1.0, 5.0, 0.1, 0.8, 0.1, 0.6),
(22, 1, 'Radiateur amphi droite', 14.0, 5.0, 0.1, 0.8, 0.1, 0.6),
(23, 2, 'Radiateur salle cours', 0.1, 6.0, 0.1, 0.1, 1.2, 0.6),
(24, 4, 'Radiateur bureau', 0.1, 3.0, 0.1, 0.1, 0.8, 0.6),
(25, 5, 'Radiateur réunion', 5.8, 0.1, 0.1, 0.8, 0.1, 0.6);

-- Windows (avec positions et tailles)
INSERT INTO t_e_window_win (id, id_room, rob_name, win_posx, win_posy, win_posz, win_sizex, win_sizey, win_sizez) VALUES 
(26, 1, 'Fenêtre amphi principale', 0.0, 10.0, 1.0, 0.1, 4.0, 2.0),
(27, 2, 'Fenêtre salle cours', 8.0, 0.0, 1.0, 2.0, 0.1, 1.5),
(28, 3, 'Fenêtre labo 1', 0.0, 7.5, 1.2, 0.1, 3.0, 1.8),
(29, 3, 'Fenêtre labo 2', 10.0, 0.0, 1.2, 2.5, 0.1, 1.8),
(30, 4, 'Fenêtre bureau', 4.0, 0.0, 1.0, 1.5, 0.1, 1.2),
(31, 5, 'Fenêtre réunion', 0.0, 4.0, 1.0, 0.1, 2.0, 1.5);

-- Doors (avec positions et tailles)
INSERT INTO t_e_door_doo (id, id_room, rob_name, doo_posx, doo_posy, doo_posz, doo_sizex, doo_sizey, doo_sizez) VALUES 
(32, 1, 'Porte amphithéâtre principale', 15.0, 2.0, 0.0, 0.1, 1.2, 2.1),
(33, 1, 'Porte amphithéâtre secours', 0.0, 18.0, 0.0, 0.1, 1.2, 2.1),
(34, 2, 'Porte salle cours', 0.0, 2.0, 0.0, 0.1, 0.9, 2.1),
(35, 3, 'Porte laboratoire', 5.0, 15.0, 0.0, 1.2, 0.1, 2.1),
(36, 4, 'Porte bureau', 4.0, 6.0, 0.0, 0.9, 0.1, 2.1),
(37, 5, 'Porte réunion', 0.0, 1.0, 0.0, 0.1, 0.9, 2.1);