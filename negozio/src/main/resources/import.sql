-- Authors
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (1, 'Freddie', 'Mercury', 'British', '1946-09-05', '1991-11-24');
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (2, 'Michael', 'Jackson', 'American', '1958-08-29', '2009-06-25');
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (3, 'David', 'Bowie', 'British', '1947-01-08', '2016-01-10');

-- CDs
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (1, 'A Night at the Opera', 19.99, 10, 1975, 'ROCK', 'EMI', 1);
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (2, 'Thriller', 25.50, 5, 1982, 'POP', 'Epic', 2);
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (3, 'The Rise and Fall of Ziggy Stardust', 22.00, 8, 1972, 'GLAM_ROCK', 'RCA', 3);
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (4, 'Innuendo', 18.00, 15, 1991, 'ROCK', 'Parlophone', 1);
-- Users
INSERT INTO users (id, name, surname, username) VALUES (nextval('users_seq'), 'Elena', 'Rossi', 'elena');
INSERT INTO users (id, name, surname, username) VALUES (nextval('users_seq'), 'Admin', 'Sistema', 'admin');
INSERT INTO users (id, name, surname, username) VALUES (nextval('users_seq'), 'Mario', 'Rossi', 'mario');
INSERT INTO users (id, name, surname, username) VALUES (nextval('users_seq'), 'Luigi', 'Bianchi', 'luigi');
INSERT INTO users (id, name, surname, username) VALUES (nextval('users_seq'), 'Paolo', 'Verdi', 'paolo');

-- Credentials (password per tutti: "paolo")
INSERT INTO credentials (id, username, password, role, user_id) VALUES (nextval('credentials_seq'), 'elena', '$2a$10$yWAIDyuEr78BBBFZ5cYh8.Nw4gUHFTRG5FwaWqNCGeOD8M4mh3.xy', 'DEFAULT', (select id from users where username = 'elena'));
INSERT INTO credentials (id, username, password, role, user_id) VALUES (nextval('credentials_seq'), 'admin', '$2a$10$yWAIDyuEr78BBBFZ5cYh8.Nw4gUHFTRG5FwaWqNCGeOD8M4mh3.xy', 'ADMIN', (select id from users where username = 'admin'));
INSERT INTO credentials (id, username, password, role, user_id) VALUES (nextval('credentials_seq'), 'mario', '$2a$10$yWAIDyuEr78BBBFZ5cYh8.Nw4gUHFTRG5FwaWqNCGeOD8M4mh3.xy', 'DEFAULT', (select id from users where username = 'mario'));
INSERT INTO credentials (id, username, password, role, user_id) VALUES (nextval('credentials_seq'), 'luigi', '$2a$10$yWAIDyuEr78BBBFZ5cYh8.Nw4gUHFTRG5FwaWqNCGeOD8M4mh3.xy', 'DEFAULT', (select id from users where username = 'luigi'));
INSERT INTO credentials (id, username, password, role, user_id) VALUES (nextval('credentials_seq'), 'paolo', '$2a$10$yWAIDyuEr78BBBFZ5cYh8.Nw4gUHFTRG5FwaWqNCGeOD8M4mh3.xy', 'ADMIN', (select id from users where username = 'paolo'));

-- Reservations 
INSERT INTO reservation (id, date, state, user_id) VALUES (nextval('reservation_seq'), '2026-06-15', 'CONFIRMED', (select id from users where username = 'elena'));
INSERT INTO reservation (id, date, state, user_id) VALUES (nextval('reservation_seq'), '2026-06-18', 'PENDING', (select id from users where username = 'admin'));

-- Reservation Items
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (nextval('reservation_item_seq'), 2, 1, (select id from reservation where date = '2026-06-15'));
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (nextval('reservation_item_seq'), 1, 2, (select id from reservation where date = '2026-06-15'));
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (nextval('reservation_item_seq'), 3, 3, (select id from reservation where date = '2026-06-18'));
