-- Authors
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (1, 'Freddie', 'Mercury', 'British', '1946-09-05', '1991-11-24');
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (2, 'Michael', 'Jackson', 'American', '1958-08-29', '2009-06-25');
INSERT INTO author (id, name, surname, nationality, date_of_birth, date_of_death) VALUES (3, 'David', 'Bowie', 'British', '1947-01-08', '2016-01-10');

-- CDs
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (1, 'A Night at the Opera', 19.99, 10, 1975, 'Rock', 'EMI', 1);
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (2, 'Thriller', 25.50, 5, 1982, 'Pop', 'Epic', 2);
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (3, 'The Rise and Fall of Ziggy Stardust', 22.00, 8, 1972, 'Glam Rock', 'RCA', 3);
INSERT INTO cd (id, name, price, available_quantity, year_of_release, genre, record_label, author_id) VALUES (4, 'Innuendo', 18.00, 15, 1991, 'Rock', 'Parlophone', 1);
-- Users
INSERT INTO users (id, name, surname, username) VALUES (1, 'Elena', 'Rossi', 'elena');
INSERT INTO users (id, name, surname, username) VALUES (2, 'Admin', 'Sistema', 'admin');

-- Credentials
INSERT INTO credentials (id, username, password, role, user_id) VALUES (1, 'elena', '$2b$12$8bKH.3HZ8L/R2sY/iC.j4uq9zYfQ/ldJV8YrdoFKMnnRefj.kGPAe', 'DEFAULT', 1);
INSERT INTO credentials (id, username, password, role, user_id) VALUES (2, 'admin', '$2b$12$sQTO9RVXui6RhYPGaaRlm.uABGtMaZd.7RFfhihvG0quX9MfT7klq', 'ADMIN', 2);

-- Reservations 
INSERT INTO reservation (id, date, state, user_id) VALUES (1, '2026-06-15', 'CONFIRMED', 1);
INSERT INTO reservation (id, date, state, user_id) VALUES (2, '2026-06-18', 'PENDING', 2);

-- Reservation Items
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (1, 2, 1, 1);
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (2, 1, 2, 1);
INSERT INTO reservation_item (id, quantity, cd_id, reservation_id) VALUES (3, 3, 3, 2);
