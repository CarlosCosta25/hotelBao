package br.edu.ifmg.hotelBao.service;

import br.edu.ifmg.hotelBao.dto.EmailDTO;
import br.edu.ifmg.hotelBao.dto.NewPasswordDTO;
import br.edu.ifmg.hotelBao.dto.RequestTokenDTO;
import br.edu.ifmg.hotelBao.entitie.PasswordRecover;
import br.edu.ifmg.hotelBao.entitie.Client;
import br.edu.ifmg.hotelBao.repository.PasswordRecoverRepository;
import br.edu.ifmg.hotelBao.repository.ClientRepository;

import br.edu.ifmg.hotelBao.service.exceptions.ResourceNotFound;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    @Value("${email.password-recover.token.minutes}")
    private int tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String uri;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void createRecoverToken(RequestTokenDTO dto) {
        // Pelo email, buscar o usuário
        Client client = clientRepository.findByEmail(dto.getEmail());

        if (client == null) {
            throw new ResourceNotFound("Email inválido.");
        }

        // Gerar o token
        String token = UUID.randomUUID().toString();

        // Inserir no BD
        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setToken(token);
        passwordRecover.setEmail(client.getEmail());
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));

        passwordRecoverRepository.save(passwordRecover);

        // Enviar o email com o token incluído no corpo da mensagem
        String body = "Token para gerar nova senha (válido por " + tokenMinutes + " minutos).\n\n" + uri + token;

        emailService.sendEmail(new EmailDTO(client.getEmail(), "Recuperação de senha", body));
    }

    public void saveNewPassword(NewPasswordDTO dto) {
        List<PasswordRecover> list = passwordRecoverRepository.searchValidToken(dto.getToken(), Instant.now());

        if (list.isEmpty()) {
            throw new ResourceNotFound("Token not found.");
        }

        Client client = clientRepository.findByEmail(list.getFirst().getEmail());
        client.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        clientRepository.save(client);
    }
}
