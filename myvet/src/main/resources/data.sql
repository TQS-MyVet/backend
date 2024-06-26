-- Password is "password" --
INSERT INTO users (id, email, name, password, phone, roles) VALUES
(1, 'everything@email.com', 'John Doe', '{bcrypt}$2a$10$kmymcENSj6rd2mHHbhpNR.TXypxzKgFGFCMuAlvTXpQ4grvUQvf6C', '1234567890', '{DOCTOR, RECEPTIONIST, USER}'),
(2, 'doctor@email.com', 'Doctor Doe', '{bcrypt}$2a$10$kmymcENSj6rd2mHHbhpNR.TXypxzKgFGFCMuAlvTXpQ4grvUQvf6C', '1234567890', '{DOCTOR, USER}'),
(3, 'receptionist@email.com','Receptionist Doe', '{bcrypt}$2a$10$kmymcENSj6rd2mHHbhpNR.TXypxzKgFGFCMuAlvTXpQ4grvUQvf6C', '1234567890', '{RECEPTIONIST, USER}'),
(4, 'user@email.com', 'User Doe', '{bcrypt}$2a$10$kmymcENSj6rd2mHHbhpNR.TXypxzKgFGFCMuAlvTXpQ4grvUQvf6C', '1234567890', '{USER}');

SELECT setval('users_seq', 5);

INSERT INTO pets (id, birthdate, name, sex, species, user_id) VALUES
(1, '2024-01-01', 'Bobi', 'Male', 'Dog', 4);

SELECT setval('pets_seq', 2);
