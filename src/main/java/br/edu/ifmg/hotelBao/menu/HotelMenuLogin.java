package br.edu.ifmg.hotelBao.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HotelMenuLogin {

    private static final String TOKEN_URL = "http://localhost:8080/oauth2/token";
    private static final String CLIENT_ID = "myclientid";
    private static final String CLIENT_SECRET = "myclientsecret";
    private static final String API_BASE_URL = "http://localhost:8080";

    private static String accessToken = null;
    private static String username = null;
    private static List<String> userRoles = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarMenuPrincipal();
    }

    private static void mostrarMenuPrincipal() {
        while (true) {
            limparTela();
            System.out.println("=============================================");
            System.out.println("    Bem vindo ao Sistema do Hotel BAO");
            System.out.println("=============================================");

            if (accessToken == null) {
                mostrarMenuNaoAutenticado();
            } else {
                if (isAdmin()) {
                    mostrarMenuAdmin();
                } else {
                    mostrarMenuAdmin();
                }
            }
        }
    }

    private static void mostrarMenuNaoAutenticado() {
        System.out.println("Status: Não autenticado");
        System.out.println();
        System.out.println("1 - Fazer Login");
        System.out.println("2 - Recuperar Senha");
        System.out.println("3 - Prosseguir sem autenticação");
        System.out.println("0 - Sair");
        System.out.println("=============================================");
        System.out.print("Digite sua opção: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                fazerLogin();
                break;
            case 2:
                recuperarSenha();
                break;
            case 3:
                mostrarMenuSemAutenticacao();
                break;
            case 0:
                System.out.println("Saindo do sistema...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida! Pressione Enter para continuar...");
                scanner.nextLine();
        }
    }

    private static void mostrarMenuSemAutenticacao() {
        while (true) {
            limparTela();
            System.out.println("=============================================");
            System.out.println("    Sistema do Hotel BAO - Modo Visitante");
            System.out.println("=============================================");
            System.out.println("Status: Não autenticado");
            System.out.println();
            System.out.println("================ Opções Disponíveis ================");
            System.out.println("1 - Listar Quartos Disponíveis");
            System.out.println("2 - Fazer Login");
            System.out.println("3 - Recuperar Senha");
            System.out.println("0 - Voltar ao Menu Principal");
            System.out.println("=============================================");
            System.out.print("Digite sua opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    listarQuartos();
                    break;
                case 2:
                    fazerLogin();
                    if (accessToken != null) {
                        return; // Volta para o menu principal autenticado
                    }
                    break;
                case 3:
                    recuperarSenha();
                    break;
                case 0:
                    return; // Volta para o menu principal
                default:
                    System.out.println("Opção inválida! Pressione Enter para continuar...");
                    scanner.nextLine();
            }
        }
    }

    private static void mostrarMenuCliente() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("    Sistema do Hotel BAO - Cliente");
        System.out.println("=============================================");
        System.out.println("Usuário: " + username + " (CLIENTE)");
        System.out.println();
        System.out.println("================ Menu Cliente ================");
        System.out.println("1 - Listar Quartos Disponíveis");
        System.out.println("2 - Fazer Reserva");
        System.out.println("3 - Minhas Reservas");
        System.out.println("4 - Relatórios Gerenciais");
        System.out.println("5 - Alterar Senha");
        System.out.println("99 - Logout");
        System.out.println("0 - Sair");
        System.out.println("=============================================");
        System.out.print("Digite sua opção: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                listarQuartos();
                break;
            case 2:
                fazerReserva();
                break;
            case 3:
                listarMinhasReservas();
                break;
            case 4:
                mostrarRelatoriosGerenciais();
                break;
            case 5:
                alterarSenha();
                break;
            case 99:
                logout();
                break;
            case 0:
                System.out.println("Saindo do sistema...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida! Pressione Enter para continuar...");
                scanner.nextLine();
        }
    }

    private static void mostrarMenuAdmin() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("    Sistema do Hotel BAO - Administrador");
        System.out.println("=============================================");
        System.out.println("Usuário: " + username + " (ADMINISTRADOR)");
        System.out.println();
        System.out.println("================ Menu Administrador ================");
        System.out.println("1 - Cadastro de Cliente");
        System.out.println("2 - Cadastro de Quarto");
        System.out.println("3 - Lançamento de Estadias");
        System.out.println("4 - Listar dados dos Clientes");
        System.out.println("5 - Listar dados dos Quartos");
        System.out.println("6 - Listar Estadias cadastradas");
        System.out.println("7 - Emitir Nota Fiscal");
        System.out.println("8 - Limpar banco de dados");
        System.out.println("9 - Relatório - Maior valor da estadia");
        System.out.println("10 - Relatório - Menor valor da estadia");
        System.out.println("11 - Relatório - Totalizar estadias");
        System.out.println("12 - Gerenciar Reservas");
        System.out.println("13 - Alterar Senha");
        System.out.println("99 - Logout");
        System.out.println("0 - Sair");
        System.out.println("=============================================");
        System.out.print("Digite sua opção: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                cadastrarCliente();
                break;
            case 2:
                cadastrarQuarto();
                break;
            case 3:
                lancarEstadias();
                break;
            case 4:
                listarClientes();
                break;
            case 5:
                listarQuartos();
                break;
            case 6:
                listarEstadias();
                break;
            case 7:
                emitirNotaFiscal();
                break;
            case 8:
                limparBancoDados();
                break;
            case 9:
                relatorioMaiorValor();
                break;
            case 10:
                relatorioMenorValor();
                break;
            case 11:
                relatorioTotalizarEstadias();
                break;
            case 12:
                gerenciarReservas();
                break;
            case 13:
                alterarSenha();
                break;
            case 99:
                logout();
                break;
            case 0:
                System.out.println("Saindo do sistema...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida! Pressione Enter para continuar...");
                scanner.nextLine();
        }
    }

    private static void fazerLogin() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("                  LOGIN");
        System.out.println("=============================================");

        System.out.print("Usuário: ");
        String user = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        if (user.isEmpty() || senha.isEmpty()) {
            System.out.println("Usuário e senha não podem ser vazios!");
            pausar();
            return;
        }

        System.out.println("Autenticando...");

        if (autenticar(user, senha)) {
            username = user;
            System.out.println("Login realizado com sucesso!");
            System.out.println("Bem-vindo, " + username + "!");
            System.out.println("Perfil: " + (isAdmin() ? "ADMINISTRADOR" : "CLIENTE"));
            pausar();
        } else {
            System.out.println("Credenciais inválidas! Verifique usuário e senha.");
            pausar();
        }
    }

    private static boolean autenticar(String usuario, String senha) {
        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configurar a requisição
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Autenticação básica do client
            String auth = CLIENT_ID + ":" + CLIENT_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            connection.setDoOutput(true);

            // Corpo da requisição
            String requestBody = "grant_type=password&username=" + usuario + "&password=" + senha + "&scope=read write";

            // Enviar dados
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar resposta
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Ler resposta
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parse do JSON para extrair o access_token e authorities
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(response.toString());
                    accessToken = jsonNode.get("access_token").asText();

                    // Extrair as authorities do token (assumindo que estão no JWT)
                    extractUserRoles();

                    return true;
                }
            } else {
                // Ler mensagem de erro
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.out.println("Erro na autenticação: " + errorResponse.toString());
                }
                return false;
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
            System.out.println("Verifique se o servidor está rodando na porta 8080");
            return false;
        }
    }

    private static void extractUserRoles() {
        // Simulação de extração de roles do JWT
        // Em uma implementação real, você decodificaria o JWT para extrair as authorities
        // Por simplicidade, vamos assumir que admin tem username "admin"
        if ("admin".equalsIgnoreCase(username)) {
            userRoles = List.of("ROLE_ADMIN");
        } else {
            userRoles = List.of("ROLE_CLIENT");
        }
    }

    private static boolean isAdmin() {
        return userRoles != null && userRoles.contains("ROLE_ADMIN");
    }

    private static void recuperarSenha() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("             RECUPERAR SENHA");
        System.out.println("=============================================");

        System.out.print("Digite seu email: ");
        String email = scanner.nextLine().trim();

        if (email.isEmpty()) {
            System.out.println("Email não pode ser vazio!");
            pausar();
            return;
        }

        try {
            URL url = new URL(API_BASE_URL + "/auth/recover-token");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Corpo da requisição JSON
            String requestBody = "{\"email\":\"" + email + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == 204) {
                System.out.println("Token de recuperação enviado para seu email!");
                System.out.println("Verifique sua caixa de entrada.");

                // Opção para inserir nova senha com token
                System.out.println();
                System.out.print("Deseja inserir o token agora? (s/n): ");
                String resposta = scanner.nextLine().trim();

                if ("s".equalsIgnoreCase(resposta)) {
                    inserirNovaSenha();
                }
            } else {
                System.out.println("Erro ao solicitar recuperação de senha.");
                System.out.println("Verifique se o email está correto.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        }

        pausar();
    }

    private static void inserirNovaSenha() {
        System.out.println();
        System.out.println("=== DEFINIR NOVA SENHA ===");

        System.out.print("Digite o token recebido por email: ");
        String token = scanner.nextLine().trim();

        System.out.print("Digite a nova senha: ");
        String novaSenha = scanner.nextLine().trim();

        if (token.isEmpty() || novaSenha.isEmpty()) {
            System.out.println("Token e nova senha são obrigatórios!");
            return;
        }

        try {
            URL url = new URL(API_BASE_URL + "/auth/new-password");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Corpo da requisição JSON
            String requestBody = "{\"token\":\"" + token + "\",\"newPassword\":\"" + novaSenha + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == 204) {
                System.out.println("Senha alterada com sucesso!");
                System.out.println("Agora você pode fazer login com a nova senha.");
            } else {
                System.out.println("Erro ao alterar senha. Verifique se o token está correto.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        }
    }

    // Métodos de funcionalidades específicas
    private static void listarQuartos() {
        limparTela();
        System.out.println("================================");
        System.out.println("LISTAGEM DOS QUARTOS CADASTRADOS");
        System.out.println("================================");
        System.out.print("Deseja realmente imprimir o relatório? (S/N): ");
        String confirma = scanner.nextLine().trim().toUpperCase();

        if (!"S".equals(confirma)) {
            System.out.println("Operação cancelada.");
            pausar();
            return;
        }

        try {
            // Busca todos os quartos (sem paginação)
            String urlStr = API_BASE_URL + "/room?page=0&size=1000";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            // Se precisar de token:
            // conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(sb.toString());
                JsonNode content = root.get("content");

                if (content.isArray() && content.size() > 0) {
                    for (JsonNode roomNode : content) {
                        long id = roomNode.get("id").asLong();
                        String desc = roomNode.get("description").asText();
                        double price = roomNode.get("price").asDouble();
                        System.out.printf(
                                "Quarto - Código: %d Descrição: %s Valor: R$ %.1f%n",
                                id, desc, price
                        );
                    }
                } else {
                    System.out.println("Não existem quartos cadastrados no sistema!");
                }

            } else {
                System.out.println("Falha ao listar quartos. HTTP status: " + status);
            }
        } catch (IOException e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        }

        pausar();
    }


    private static void fazerReserva() {
        System.out.println("=== FAZER RESERVA ===");
        System.out.println("Funcionalidade: Fazer Reserva - Em desenvolvimento");
        pausar();
    }

    private static void listarMinhasReservas() {
        System.out.println("=== MINHAS RESERVAS ===");
        System.out.println("Funcionalidade: Listar Minhas Reservas - Em desenvolvimento");
        pausar();
    }

    private static void mostrarRelatoriosGerenciais() {
        System.out.println("=== RELATÓRIOS GERENCIAIS ===");
        System.out.println("Funcionalidade: Relatórios Gerenciais - Em desenvolvimento");
        pausar();
    }

    private static void alterarSenha() {
        System.out.println("=== ALTERAR SENHA ===");
        System.out.println("Funcionalidade: Alterar Senha - Em desenvolvimento");
        pausar();
    }

    private static void cadastrarCliente() {
        while (true) {
            limparTela();
            System.out.println("=============================================");
            System.out.println("         MENU DE OPÇÕES DO CLIENTE");
            System.out.println("=============================================");
            System.out.println("1 - Inserir Cliente");
            System.out.println("2 - Deletar Cliente");
            System.out.println("3 - Alterar Cliente");
            System.out.println("0 - Voltar ao menu anterior");
            System.out.println("=============================================");
            System.out.print("Digite sua opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    inserirCliente();
                    break;
                case 2:
                    deletarCliente();
                    break;
                case 3:
                    alterarCliente();
                    break;
                case 0:
                    return; // Volta ao menu anterior
                default:
                    System.out.println("Opção inválida! Pressione Enter para continuar...");
                    scanner.nextLine();
            }
        }
    }

    private static void inserirCliente() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("            INSERIR CLIENTE");
        System.out.println("=============================================");

        try {
            // Coletando dados do cliente
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Telefone: ");
            String telefone = scanner.nextLine().trim();

            System.out.print("Login/Username: ");
            String login = scanner.nextLine().trim();

            System.out.print("Senha: ");
            String senha = scanner.nextLine().trim();

            // Validações básicas
            if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() ||
                    login.isEmpty() || senha.isEmpty()) {
                System.out.println("Erro: Todos os campos são obrigatórios!");
                pausar();
                return;
            }

            // Validação de email básica
            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Erro: Email inválido!");
                pausar();
                return;
            }

            // Validação de nome (apenas letras e espaços)
            if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$")) {
                System.out.println("Erro: Nome deve conter apenas letras e espaços!");
                pausar();
                return;
            }

            // Validação de tamanho do nome
            if (nome.length() > 50) {
                System.out.println("Erro: Nome não pode ter mais de 50 caracteres!");
                pausar();
                return;
            }

            // Confirmação dos dados
            System.out.println("\n=== CONFIRMAR DADOS ===");
            System.out.println("Nome: " + nome);
            System.out.println("Email: " + email);
            System.out.println("Telefone: " + telefone);
            System.out.println("Login: " + login);
            System.out.println("Senha: " + "*".repeat(senha.length()));
            System.out.println();
            System.out.print("Confirma o cadastro? (S/N): ");

            String confirmacao = scanner.nextLine().trim().toUpperCase();
            if (!"S".equals(confirmacao)) {
                System.out.println("Cadastro cancelado.");
                pausar();
                return;
            }

            // Preparar dados para envio
            String jsonBody = String.format(
                    "{\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"login\":\"%s\",\"password\":\"%s\"}",
                    nome.replace("\"", "\\\""),
                    email.replace("\"", "\\\""),
                    telefone.replace("\"", "\\\""),
                    login.replace("\"", "\\\""),
                    senha.replace("\"", "\\\"")
            );

            // Fazer requisição para a API
            URL url = new URL(API_BASE_URL + "/client");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Adicionar token de autorização se disponível
            if (accessToken != null) {
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            connection.setDoOutput(true);

            // Enviar dados
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Verificar resposta
            int responseCode = connection.getResponseCode();

            if (responseCode == 201) {
                // Sucesso - cliente criado
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parse da resposta para mostrar dados do cliente criado
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode clienteNode = mapper.readTree(response.toString());

                    System.out.println("\n✓ Cliente cadastrado com sucesso!");
                    System.out.println("ID: " + clienteNode.get("id").asLong());
                    System.out.println("Nome: " + clienteNode.get("name").asText());
                    System.out.println("Email: " + clienteNode.get("email").asText());
                    System.out.println("Telefone: " + clienteNode.get("phone").asText());
                    System.out.println("Login: " + clienteNode.get("login").asText());
                    System.out.println("\nNota: A senha padrão foi definida conforme informado.");
                }

            } else if (responseCode == 400) {
                // Erro de validação
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {

                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }

                    System.out.println("Erro de validação:");
                    System.out.println(errorResponse.toString());
                }

            } else if (responseCode == 401) {
                System.out.println("Erro: Não autorizado. Faça login como administrador.");

            } else if (responseCode == 403) {
                System.out.println("Erro: Acesso negado. Apenas administradores podem cadastrar clientes.");

            } else if (responseCode == 422) {
                // Erro de dados - possivelmente cliente já existe
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {

                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }

                    System.out.println("Erro: Dados inválidos ou cliente já existe.");
                    System.out.println("Detalhes: " + errorResponse.toString());
                }

            } else {
                System.out.println("Erro inesperado. Código HTTP: " + responseCode);

                // Tentar ler mensagem de erro
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {

                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }

                    if (errorResponse.length() > 0) {
                        System.out.println("Erro: " + errorResponse.toString());
                    }
                } catch (Exception e) {
                    // Se não conseguir ler o erro, apenas informa o código
                    System.out.println("Não foi possível obter detalhes do erro.");
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
            System.out.println("Verifique se o servidor está rodando na porta 8080");

        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        pausar();
    }

    private static void deletarCliente() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("            DELETAR CLIENTE");
        System.out.println("=============================================");

        try {
            System.out.print("Digite o ID do cliente a ser deletado: ");
            String idStr = scanner.nextLine().trim();

            if (idStr.isEmpty()) {
                System.out.println("Erro: ID não pode ser vazio!");
                pausar();
                return;
            }

            long clienteId;
            try {
                clienteId = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Erro: ID deve ser um número válido!");
                pausar();
                return;
            }

            // Buscar cliente primeiro para mostrar dados
            URL urlGet = new URL(API_BASE_URL + "/client/" + clienteId);
            HttpURLConnection connectionGet = (HttpURLConnection) urlGet.openConnection();
            connectionGet.setRequestMethod("GET");
            connectionGet.setRequestProperty("Accept", "application/json");

            if (accessToken != null) {
                connectionGet.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            int getResponseCode = connectionGet.getResponseCode();

            if (getResponseCode == 200) {
                // Cliente encontrado, mostrar dados
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connectionGet.getInputStream(), StandardCharsets.UTF_8))) {

                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode clienteNode = mapper.readTree(response.toString());

                    System.out.println("\n=== DADOS DO CLIENTE ===");
                    System.out.println("ID: " + clienteNode.get("id").asLong());
                    System.out.println("Nome: " + clienteNode.get("name").asText());
                    System.out.println("Email: " + clienteNode.get("email").asText());
                    System.out.println("Telefone: " + clienteNode.get("phone").asText());
                    System.out.println("Login: " + clienteNode.get("login").asText());

                    System.out.print("\nConfirma a exclusão? (S/N): ");
                    String confirmacao = scanner.nextLine().trim().toUpperCase();

                    if (!"S".equals(confirmacao)) {
                        System.out.println("Exclusão cancelada.");
                        pausar();
                        return;
                    }

                    // Proceder com a exclusão
                    URL urlDelete = new URL(API_BASE_URL + "/client/" + clienteId);
                    HttpURLConnection connectionDelete = (HttpURLConnection) urlDelete.openConnection();
                    connectionDelete.setRequestMethod("DELETE");
                    connectionDelete.setRequestProperty("Accept", "application/json");

                    if (accessToken != null) {
                        connectionDelete.setRequestProperty("Authorization", "Bearer " + accessToken);
                    }

                    int deleteResponseCode = connectionDelete.getResponseCode();

                    if (deleteResponseCode == 200) {
                        System.out.println("\n✓ Cliente deletado com sucesso!");
                    } else if (deleteResponseCode == 404) {
                        System.out.println("Erro: Cliente não encontrado.");
                    } else if (deleteResponseCode == 401) {
                        System.out.println("Erro: Não autorizado. Faça login como administrador.");
                    } else if (deleteResponseCode == 403) {
                        System.out.println("Erro: Acesso negado. Apenas administradores podem deletar clientes.");
                    } else {
                        System.out.println("Erro inesperado. Código HTTP: " + deleteResponseCode);
                    }
                }

            } else if (getResponseCode == 404) {
                System.out.println("Erro: Cliente com ID " + clienteId + " não encontrado.");
            } else {
                System.out.println("Erro ao buscar cliente. Código HTTP: " + getResponseCode);
            }

        } catch (IOException e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        pausar();
    }

    private static void alterarCliente() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("            ALTERAR CLIENTE");
        System.out.println("=============================================");

        try {
            System.out.print("Digite o ID do cliente a ser alterado: ");
            String idStr = scanner.nextLine().trim();

            if (idStr.isEmpty()) {
                System.out.println("Erro: ID não pode ser vazio!");
                pausar();
                return;
            }

            long clienteId;
            try {
                clienteId = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                System.out.println("Erro: ID deve ser um número válido!");
                pausar();
                return;
            }

            // Buscar cliente primeiro para mostrar dados atuais
            URL urlGet = new URL(API_BASE_URL + "/client/" + clienteId);
            HttpURLConnection connectionGet = (HttpURLConnection) urlGet.openConnection();
            connectionGet.setRequestMethod("GET");
            connectionGet.setRequestProperty("Accept", "application/json");

            if (accessToken != null) {
                connectionGet.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            int getResponseCode = connectionGet.getResponseCode();

            if (getResponseCode == 200) {
                // Cliente encontrado, mostrar dados atuais
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connectionGet.getInputStream(), StandardCharsets.UTF_8))) {

                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode clienteNode = mapper.readTree(response.toString());

                    System.out.println("\n=== DADOS ATUAIS DO CLIENTE ===");
                    String nomeAtual = clienteNode.get("name").asText();
                    String emailAtual = clienteNode.get("email").asText();
                    String telefoneAtual = clienteNode.get("phone").asText();

                    System.out.println("Nome atual: " + nomeAtual);
                    System.out.println("Email atual: " + emailAtual);
                    System.out.println("Telefone atual: " + telefoneAtual);
                    System.out.println();

                    // Coletar novos dados (permitir manter os atuais se deixar em branco)
                    System.out.println("=== NOVOS DADOS (deixe em branco para manter o atual) ===");

                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine().trim();
                    if (novoNome.isEmpty()) {
                        novoNome = nomeAtual;
                    }

                    System.out.print("Novo email: ");
                    String novoEmail = scanner.nextLine().trim();
                    if (novoEmail.isEmpty()) {
                        novoEmail = emailAtual;
                    }

                    System.out.print("Novo telefone: ");
                    String novoTelefone = scanner.nextLine().trim();
                    if (novoTelefone.isEmpty()) {
                        novoTelefone = telefoneAtual;
                    }

                    // Validações
                    if (!novoEmail.contains("@") || !novoEmail.contains(".")) {
                        System.out.println("Erro: Email inválido!");
                        pausar();
                        return;
                    }

                    if (!novoNome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$")) {
                        System.out.println("Erro: Nome deve conter apenas letras e espaços!");
                        pausar();
                        return;
                    }

                    if (novoNome.length() > 50) {
                        System.out.println("Erro: Nome não pode ter mais de 50 caracteres!");
                        pausar();
                        return;
                    }

                    // Confirmação
                    System.out.println("\n=== CONFIRMAR ALTERAÇÕES ===");
                    System.out.println("Nome: " + novoNome);
                    System.out.println("Email: " + novoEmail);
                    System.out.println("Telefone: " + novoTelefone);
                    System.out.print("\nConfirma as alterações? (S/N): ");

                    String confirmacao = scanner.nextLine().trim().toUpperCase();
                    if (!"S".equals(confirmacao)) {
                        System.out.println("Alteração cancelada.");
                        pausar();
                        return;
                    }

                    // Preparar dados para envio
                    String jsonBody = String.format(
                            "{\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\"}",
                            novoNome.replace("\"", "\\\""),
                            novoEmail.replace("\"", "\\\""),
                            novoTelefone.replace("\"", "\\\"")
                    );

                    // Fazer requisição PUT
                    URL urlPut = new URL(API_BASE_URL + "/client/" + clienteId);
                    HttpURLConnection connectionPut = (HttpURLConnection) urlPut.openConnection();
                    connectionPut.setRequestMethod("PUT");
                    connectionPut.setRequestProperty("Content-Type", "application/json");
                    connectionPut.setRequestProperty("Accept", "application/json");

                    if (accessToken != null) {
                        connectionPut.setRequestProperty("Authorization", "Bearer " + accessToken);
                    }

                    connectionPut.setDoOutput(true);

                    try (OutputStream os = connectionPut.getOutputStream()) {
                        byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int putResponseCode = connectionPut.getResponseCode();

                    if (putResponseCode == 200) {
                        System.out.println("\n✓ Cliente alterado com sucesso!");
                    } else if (putResponseCode == 400) {
                        System.out.println("Erro: Dados inválidos.");
                    } else if (putResponseCode == 401) {
                        System.out.println("Erro: Não autorizado. Faça login como administrador.");
                    } else if (putResponseCode == 403) {
                        System.out.println("Erro: Acesso negado. Apenas administradores podem alterar clientes.");
                    } else if (putResponseCode == 404) {
                        System.out.println("Erro: Cliente não encontrado.");
                    } else {
                        System.out.println("Erro inesperado. Código HTTP: " + putResponseCode);
                    }
                }

            } else if (getResponseCode == 404) {
                System.out.println("Erro: Cliente com ID " + clienteId + " não encontrado.");
            } else {
                System.out.println("Erro ao buscar cliente. Código HTTP: " + getResponseCode);
            }

        } catch (IOException e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }

        pausar();
    }

    private static void cadastrarQuarto() {
        System.out.println("=== CADASTRAR QUARTO ===");
        System.out.println("Funcionalidade: Cadastro de Quarto - Em desenvolvimento");
        pausar();
    }

    private static void lancarEstadias() {
        System.out.println("=== LANÇAR ESTADIAS ===");
        System.out.println("Funcionalidade: Lançamento de Estadias - Em desenvolvimento");
        pausar();
    }

    private static void listarClientes() {
        System.out.println("=== LISTAR CLIENTES ===");
        System.out.println("Funcionalidade: Listar dados dos Clientes - Em desenvolvimento");
        pausar();
    }

    private static void listarEstadias() {
        System.out.println("=== LISTAR ESTADIAS ===");
        System.out.println("Funcionalidade: Listar Estadias cadastradas - Em desenvolvimento");
        pausar();
    }

    private static void emitirNotaFiscal() {
        System.out.println("=== EMITIR NOTA FISCAL ===");
        System.out.println("Funcionalidade: Emitir nota Fiscal - Em desenvolvimento");
        pausar();
    }

    private static void limparBancoDados() {
        System.out.println("=== LIMPAR BANCO DE DADOS ===");
        System.out.print("ATENÇÃO: Esta operação apagará todos os dados! Confirma? (s/n): ");
        String confirmacao = scanner.nextLine().trim();

        if ("s".equalsIgnoreCase(confirmacao)) {
            System.out.println("Funcionalidade: Limpar banco de dados - Em desenvolvimento");
        } else {
            System.out.println("Operação cancelada.");
        }
        pausar();
    }

    private static void relatorioMaiorValor() {
        System.out.println("=== RELATÓRIO - MAIOR VALOR DA ESTADIA ===");
        System.out.println("Funcionalidade: Relatório - Maior valor da estadia - Em desenvolvimento");
        pausar();
    }

    private static void relatorioMenorValor() {
        System.out.println("=== RELATÓRIO - MENOR VALOR DA ESTADIA ===");
        System.out.println("Funcionalidade: Relatório - Menor valor da estadia - Em desenvolvimento");
        pausar();
    }

    private static void relatorioTotalizarEstadias() {
        System.out.println("=== RELATÓRIO - TOTALIZAR ESTADIAS ===");
        System.out.println("Funcionalidade: Relatório - Totalizar estadias - Em desenvolvimento");
        pausar();
    }

    private static void gerenciarReservas() {
        System.out.println("=== GERENCIAR RESERVAS ===");
        System.out.println("Funcionalidade: Gerenciar Reservas - Em desenvolvimento");
        pausar();
    }

    private static void logout() {
        accessToken = null;
        username = null;
        userRoles = null;
        System.out.println("Logout realizado com sucesso!");
        pausar();
    }

    private static int lerOpcao() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Opção inválida
        }
    }

    private static void pausar() {
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }

    private static void limparTela() {
        // Limpa a tela (funciona na maioria dos terminais)
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Se não conseguir limpar, imprime linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}