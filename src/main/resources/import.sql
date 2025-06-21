-- Primeiro inserir os clientes (com password e login obrigatórios)
INSERT INTO client (name, email, phone, password, login) VALUES ('João Silva', 'joao@email.com', '11999999999', 'senha123', 'joao.silva');
INSERT INTO client (name, email, phone, password, login) VALUES ('Maria Santos', 'maria@email.com', '11888888888', 'senha456', 'maria.santos');
INSERT INTO client (name, email, phone, password, login) VALUES ('Pedro Oliveira', 'pedro@email.com', '11777777777', 'senha789', 'pedro.oliveira');

-- Depois inserir os quartos (com description ao invés de number e type)
INSERT INTO room (description, price, image_url) VALUES ('Quarto Standard 101 - Confortável e bem localizado', 150.00, 'https://exemplo.com/quarto101.jpg');
INSERT INTO room (description, price, image_url) VALUES ('Quarto Deluxe 102 - Luxuoso com vista para o mar', 250.00, 'https://exemplo.com/quarto102.jpg');
INSERT INTO room (description, price, image_url) VALUES ('Suíte 103 - Ampla e elegante com hidromassagem', 400.00, 'https://exemplo.com/quarto103.jpg');

-- Por último inserir as estadias (referenciando os IDs corretos)
INSERT INTO stay (check_in, check_out, room_id, client_id) VALUES ('2024-01-15T14:00:00', '2024-01-20T12:00:00', 1, 1);
INSERT INTO stay (check_in, check_out, room_id, client_id) VALUES ('2024-02-10T14:00:00', '2024-02-15T12:00:00', 2, 2);
INSERT INTO stay (check_in, check_out, room_id, client_id) VALUES ('2024-03-05T14:00:00', '2024-03-12T12:00:00', 3, 3);