package br.edu.ifmg.hotelBao.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HotelMenuLogin {

    private static final String TOKEN_URL = "http://localhost:8080/oauth2/token";
    private static final String CLIENT_ID = "myclientid";
    private static final String CLIENT_SECRET = "myclientsecret"; 

    private static String accessToken = null;
    private static String username = null;
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
                System.out.println("Usuario: ");
                System.out.println("Senha: ");
                System.out.println();
                System.out.println("1 - Fazer Login");
                System.out.println("0 - Sair");
                System.out.println("=============================================");
                System.out.print("Digite sua opcao: ");

                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        fazerLogin();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opcao invalida! Pressione Enter para continuar...");
                        scanner.nextLine();
                }
            } else {
                mostrarMenuLogado();
            }
        }
    }

    private static void fazerLogin() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("                  LOGIN");
        System.out.println("=============================================");

        System.out.print("Usuario: ");
        String user = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        if (user.isEmpty() || senha.isEmpty()) {
            System.out.println("Usuario e senha nao podem ser vazios!");
            System.out.println("Pressione Enter para continuar...");
            scanner.nextLine();
            return;
        }

        System.out.println("Autenticando...");

        if (autenticar(user, senha)) {
            username = user;
            System.out.println("Login realizado com sucesso!");
            System.out.println("Bem-vindo, " + username + "!");
            System.out.println("Pressione Enter para continuar...");
            scanner.nextLine();
        } else {
            System.out.println("Credenciais invalidas! Verifique usuario e senha.");
            System.out.println("Pressione Enter para continuar...");
            scanner.nextLine();
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

                    // Parse do JSON para extrair o access_token
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(response.toString());
                    accessToken = jsonNode.get("access_token").asText();
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
                    System.out.println("Erro na autenticacao: " + errorResponse.toString());
                }
                return false;
            }

        } catch (Exception e) {
            System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
            System.out.println("Verifique se o servidor esta rodando na porta 8080");
            return false;
        }
    }

    private static void mostrarMenuLogado() {
        limparTela();
        System.out.println("=============================================");
        System.out.println("    Bem vindo ao Sistema do Hotel BAO");
        System.out.println("=============================================");
        System.out.println("Usuario: " + username);
        System.out.println("Senha: ********");
        System.out.println("================Menu de opcoes================");
        System.out.println("1 - Cadastro de Cliente");
        System.out.println("2 - Cadastro de Quarto");
        System.out.println("3 - Lancamento de Estadias");
        System.out.println("4 - Listar dados dos Clientes");
        System.out.println("5 - Listar dados dos Quartos");
        System.out.println("6 - Listar Estadias cadastradas");
        System.out.println("7 - Emitir nota Fiscal");
        System.out.println("8 - Limpar banco de dados");
        System.out.println("9 - Relatorio - Maior valor da estadia do cliente");
        System.out.println("10 - Relatorio - Menor valor da estadia do cliente");
        System.out.println("11 - Relatorio - Totalizar as estadias do cliente");
        System.out.println("99 - Logout");
        System.out.println("Digite zero para terminar");
        System.out.println("=============================================");
        System.out.print("Digite sua opcao: ");

        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                System.out.println("Funcionalidade: Cadastro de Cliente - Em desenvolvimento");
                pausar();
                break;
            case 2:
                System.out.println("Funcionalidade: Cadastro de Quarto - Em desenvolvimento");
                pausar();
                break;
            case 3:
                System.out.println("Funcionalidade: Lancamento de Estadias - Em desenvolvimento");
                pausar();
                break;
            case 4:
                System.out.println("Funcionalidade: Listar dados dos Clientes - Em desenvolvimento");
                pausar();
                break;
            case 5:
                System.out.println("Funcionalidade: Listar dados dos Quartos - Em desenvolvimento");
                pausar();
                break;
            case 6:
                System.out.println("Funcionalidade: Listar Estadias cadastradas - Em desenvolvimento");
                pausar();
                break;
            case 7:
                System.out.println("Funcionalidade: Emitir nota Fiscal - Em desenvolvimento");
                pausar();
                break;
            case 8:
                System.out.println("Funcionalidade: Limpar banco de dados - Em desenvolvimento");
                pausar();
                break;
            case 9:
                System.out.println("Funcionalidade: Relatorio - Maior valor da estadia - Em desenvolvimento");
                pausar();
                break;
            case 10:
                System.out.println("Funcionalidade: Relatorio - Menor valor da estadia - Em desenvolvimento");
                pausar();
                break;
            case 11:
                System.out.println("Funcionalidade: Relatorio - Totalizar estadias - Em desenvolvimento");
                pausar();
                break;
            case 99:
                logout();
                break;
            case 0:
                System.out.println("Saindo do sistema...");
                System.exit(0);
                break;
            default:
                System.out.println("Opcao invalida! Pressione Enter para continuar...");
                scanner.nextLine();
        }
    }

    private static void logout() {
        accessToken = null;
        username = null;
        System.out.println("Logout realizado com sucesso!");
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
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