-- Primeiro inserir os clientes (com password e login obrigatórios)
INSERT INTO client (name,email,phone,password,login,created_at,updated_at) VALUES ('João Silva','lucasgontijo111@gmail.com','11999999999', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG','lucas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- hash de "senha123"

INSERT INTO client (name,email,phone,password,login,created_at,updated_at) VALUES ('Maria Santos','maria@email.com','11888888888', '$2a$10$AbCdEfGhIjKlMnOpQrStUvWxYz1234567890abcdefghiJklmno', 'maria.santos', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);  -- hash de "senha456"

INSERT INTO client (name,email,phone,password,login,created_at,updated_at) VALUES ('Pedro Oliveira','pedro@email.com','11777777777', '$2a$10$ZyXwVuTsRqPoNmLkJiHgFeDcBa0987654321lkjihgFedcbaZYX', 'pedro.oliveira', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); -- hash de "senha789"
-- Depois inserir os quartos (com description ao invés de number e type)
INSERT INTO room (description, price, image_url) VALUES ('Quarto Standard 101 - Confortável e bem localizado', 150.00, 'https://exemplo.com/quarto101.jpg');
INSERT INTO room (description, price, image_url) VALUES ('Quarto Deluxe 102 - Luxuoso com vista para o mar', 250.00, 'https://exemplo.com/quarto102.jpg');
INSERT INTO room (description, price, image_url) VALUES ('Suíte 103 - Ampla e elegante com hidromassagem', 400.00, 'https://exemplo.com/quarto103.jpg');

-- Por último inserir as estadias (referenciando os IDs corretos)
INSERT INTO stay (check_in, check_out, room_id, client_id) VALUES ('2024-01-15T14:00:00', '2024-01-20T12:00:00', 1, 1);
INSERT INTO stay (check_in, check_out, room_id, client_id) VALUES ('2024-02-10T14:00:00', '2024-02-15T12:00:00', 2, 2);
INSERT INTO stay (check_in, check_out, room_id, client_id) VALUES ('2024-03-05T14:00:00', '2024-03-12T12:00:00', 3, 3);

-- Inserir roles
INSERT INTO role (id, authority) VALUES (1, 'ROLE_CLIENT');
INSERT INTO role (id, authority) VALUES (2, 'ROLE_ADMIN');

-- Associar roles aos clientes
INSERT INTO client_role (client_id, role_id) VALUES (1, 2); -- João Silva -> ROLE_USER
INSERT INTO client_role (client_id, role_id) VALUES (2, 1); -- Maria Santos -> ROLE_USER
INSERT INTO client_role (client_id, role_id) VALUES (3, 2); -- Pedro Oliveira -> ROLE_ADMIN