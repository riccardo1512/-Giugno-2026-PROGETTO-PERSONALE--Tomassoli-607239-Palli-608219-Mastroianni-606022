-- Authors
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (nextval('author_seq'), 'Freddie', 'Mercury', 'British', '1946-09-05', '1991-11-24');
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (nextval('author_seq'), 'Michael', 'Jackson', 'American', '1958-08-29', '2009-06-25');
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (nextval('author_seq'), 'David', 'Bowie', 'British', '1947-01-08', '2016-01-10');

-- CDs
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (nextval('cd_seq'), 'A Night at the Opera', 19.99, 10, 1975, 'ROCK', 'EMI', (select id from author where name = 'Freddie' and surname = 'Mercury'));
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (nextval('cd_seq'), 'Thriller', 25.50, 5, 1982, 'POP', 'Epic', (select id from author where name = 'Michael' and surname = 'Jackson'));
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (nextval('cd_seq'), 'The Rise and Fall of Ziggy Stardust', 22.00, 8, 1972, 'GLAM_ROCK', 'RCA', (select id from author where name = 'David' and surname = 'Bowie'));
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (nextval('cd_seq'), 'Innuendo', 18.00, 15, 1991, 'ROCK', 'Parlophone', (select id from author where name = 'Freddie' and surname = 'Mercury'));
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
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (nextval('reservation_item_seq'), 2, (select id from cd where name = 'A Night at the Opera'), (select id from reservation where date = '2026-06-15'));
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (nextval('reservation_item_seq'), 1, (select id from cd where name = 'Thriller'), (select id from reservation where date = '2026-06-15'));
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (nextval('reservation_item_seq'), 3, (select id from cd where name = 'The Rise and Fall of Ziggy Stardust'), (select id from reservation where date = '2026-06-18'));

-- Reviews
INSERT INTO review (id, rating, text, title, author_id, cd_id) VALUES (nextval('review_seq'), 5, 'Un capolavoro assoluto, voce fantastica', 'Eccezionale', (select id from users where username = 'mario'), (select id from cd where name = 'A Night at the Opera'));
INSERT INTO review (id, rating, text, title, author_id, cd_id) VALUES (nextval('review_seq'), 4, 'Molto bello, ma preferisco altri album', 'Bello', (select id from users where username = 'luigi'), (select id from cd where name = 'A Night at the Opera'));
INSERT INTO review (id, rating, text, title, author_id, cd_id) VALUES (nextval('review_seq'), 5, 'Il re del pop non delude mai', 'Incredibile', (select id from users where username = 'elena'), (select id from cd where name = 'Thriller'));
INSERT INTO review (id, rating, text, title, author_id, cd_id) VALUES (nextval('review_seq'), 3, 'Poteva fare di meglio secondo me', 'Nella media', (select id from users where username = 'paolo'), (select id from cd where name = 'Thriller'));
