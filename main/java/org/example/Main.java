package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    static Loja loja = new Loja();

    private static String css() {
        return """
                <style>
                        .card {
                            background: #2a2a2a;
                            padding: 20px;
                            margin-bottom: 20px;
                            border-radius: 12px;
                            box-shadow: 0 0 10px rgba(0,0,0,0.4);
                        }
                
                        .card h2 {
                            margin-top: 0;
                            color: #ffffff;
                        }
                
                        .preco {
                            color: #00ff9d;
                            font-size: 22px;
                            font-weight: bold;
                        }
                
                        body {
                            margin: 0;
                            font-family: Arial, sans-serif;
                            background: #121212;
                            color: #f5f5f5;
                        }
                
                        .container {
                            width: 80%;
                            margin: 40px auto;
                            background: #1f1f1f;
                            padding: 30px;
                            border-radius: 12px;
                            box-shadow: 0 0 15px rgba(0,0,0,0.5);
                        }
                
                        h1 {
                            color: #00c3ff;
                        }
                
                        a, button {
                            display: inline-block;
                            background: #00c3ff;
                            color: #000;
                            padding: 10px 18px;
                            margin: 8px 5px 8px 0;
                            border-radius: 8px;
                            text-decoration: none;
                            border: none;
                            font-weight: bold;
                            cursor: pointer;
                        }
                
                        a:hover, button:hover {
                            background: #00ff9d;
                        }
                
                        input, select {
                            width: 100%;
                            padding: 10px;
                            border-radius: 8px;
                            border: none;
                            margin-top: 5px;
                            box-sizing: border-box;
                        }
                
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-top: 20px;
                            background: #2a2a2a;
                        }
                
                        th {
                            background: #00c3ff;
                            color: #000;
                        }
                
                        th, td {
                            padding: 12px;
                            text-align: left;
                            border-bottom: 1px solid #444;
                        }
                    </style>
                """;
    }

    public static void main(String[] args) throws IOException {

        loja.cadastrarProduto("PlayStation 5", "Console", 3999.90, 5);
        loja.cadastrarProduto("Elden Ring", "Jogo", 249.90, 10);

        String portaEnv = System.getenv("PORT");
        int porta = portaEnv == null ? 8080 : Integer.parseInt(portaEnv);

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);

        server.createContext("/", Main::home);
        server.createContext("/cadastro", Main::cadastro);
        server.createContext("/salvar", Main::salvar);
        server.createContext("/produtos", Main::produtos);
        server.createContext("/vender", Main::vender);


        server.start();

        System.out.println("Servidor rodando na porta: " + porta);
    }

    private static void home(HttpExchange exchange) throws IOException {
        String html = """
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Loja de Games</title>
                """ + css() + """
                </head>
                <body>
                    <div class="container">
                        <h1>🎮 Loja de Games</h1>
                        <p>Sistema de cadastro de produtos feito em Java com POO e HTTP Server.</p>
                
                        <a href="/cadastro">Cadastrar Produto</a>
                        <a href="/produtos">Ver Produtos</a>
                    </div>
                </body>
                </html>
                """;

        responder(exchange, html);
    }

    private static void cadastro(HttpExchange exchange) throws IOException {
        String html = """
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Cadastro de Produto</title>
                """ + css() + """
                </head>
                <body>
                    <div class="container">
                        <h1>Cadastro de Produto</h1>
                
                        <form action="/salvar" method="post">
                            Nome:<br>
                            <input name="nome" required><br><br>
                
                           Categoria:<br>
                               <select name="categoria" required>
                                   <option value="">Selecione uma categoria</option>
                                   <option>Jogo</option>
                                   <option>Console</option>
                                   <option>Acessórios</option>
                               </select><br><br>
                
                            Preço:<br>
                            <input name="preco" required><br><br>
                
                            Quantidade:<br>
                            <input name="quantidade" required><br><br>
                
                            <button type="submit">Cadastrar</button>
                        </form>
                
                        <a href="/">Voltar</a>
                    </div>
                </body>
                </html>
                """;

        responder(exchange, html);
    }

    private static void salvar(HttpExchange exchange) throws IOException {
        try {

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> dados = separar(body);

            loja.cadastrarProduto(
                    dados.get("nome"),
                    dados.get("categoria"),
                    Double.parseDouble(dados.get("preco")),
                    Integer.parseInt(dados.get("quantidade"))
            );

            String html = """
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Produto cadastrado</title>
                    """ + css() + """
                    </head>
                    <body>
                        <div class="container">
                    
                            <div class="sucesso">
                                ✅ Produto cadastrado com sucesso!
                            </div>
                    
                            <p>O produto foi adicionado ao catálogo da loja.</p>
                    
                            <a href="/cadastro">Cadastrar outro produto</a>
                            <a href="/produtos">Ver Produtos</a>
                    
                        </div>
                    </body>
                    </html>
                    """;

            responder(exchange, html);

        } catch (Exception e) {

            String htmlErro = """
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Erro no cadastro</title>
                    """ + css() + """
                    </head>
                    <body>
                        <div class="container">
                    
                            <h1>Erro ao cadastrar produto</h1>
                    
                            <p>
                    """ + e.getMessage() + """
                            </p>
                    
                            <a href="/cadastro">Tentar novamente</a>
                            <a href="/">Voltar ao início</a>
                    
                        </div>
                    </body>
                    </html>
                    """;

            responder(exchange, htmlErro);

        }
    }

    private static void produtos(HttpExchange exchange) throws IOException {
        StringBuilder html = new StringBuilder();

        html.append("""
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Produtos</title>
                """).append(css()).append("""
                </head>
                <body>
                    <div class="container">
                        <h1>🎮 Produtos Cadastrados</h1>
                """);

        for (Produto p : loja.listarProdutos()) {
            html.append("""
                    <div class="card">
                    """);

            html.append("<h2>").append(p.getNome()).append("</h2>");
            html.append("<p><strong>Categoria:</strong> ").append(p.getCategoria()).append("</p>");
            html.append("<p class='preco'>R$ ").append(String.format("%.2f", p.getPreco())).append("</p>");
            html.append("<p><strong>Estoque:</strong> ").append(p.getQuantidade()).append(" unidades</p>");
            html.append("<form action='/vender' method='post'>");
            html.append("<input type='hidden' name='id' value='").append(p.getId()).append("'>");
            html.append("<input type='number' name='quantidade' min='1' max='")
                    .append(p.getQuantidade())
                    .append("' placeholder='Quantidade para vender' required>");
            html.append("<button type='submit'>Vender</button>");
            html.append("</form>");
            html.append("""
                    </div>
                    """);
        }

        html.append("""
                        <a href="/cadastro">Cadastrar Produto</a>
                        <a href="/">Voltar</a>
                    </div>
                </body>
                </html>
                """);

        responder(exchange, html.toString());
    }

    private static void responder(HttpExchange exchange, String texto) throws IOException {
        byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/html; charset=UTF-8"
        );
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static Map<String, String> separar(String body) {
        Map<String, String> mapa = new HashMap<>();
        for (String p : body.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2) {
                mapa.put(
                        URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                );
            }
        }
        return mapa;
    }

    private static void vender(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> dados = separar(body);

            int id = Integer.parseInt(dados.get("id"));
            int quantidade = Integer.parseInt(dados.get("quantidade"));

            loja.venderProduto(id, quantidade);

            responder(exchange, """
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Venda realizada</title>
                """ + css() + """
                </head>
                <body>
                    <div class="container">
                        <div class="sucesso">
                            Venda realizada! Estoque atualizado.
                        </div>

                        <a href="/produtos">Voltar aos produtos</a>
                    </div>
                </body>
                </html>
                """);

        } catch (Exception e) {
            String htmlErro = """
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Erro na venda</title>
                """ + css() + """
                </head>
                <body>
                    <div class="container">
                        <h1>Erro ao vender produto</h1>
                        <p>
                """ + e.getMessage() + """
                        </p>

                        <a href="/produtos">Voltar aos produtos</a>
                    </div>
                </body>
                </html>
                """;

            responder(exchange, htmlErro);
        }
    }
}

