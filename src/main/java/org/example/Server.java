package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000; // Set your desired port number
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        server.createContext("/receive-json", new JSONHandler());
        server.setExecutor(null); // Use default executor

        server.start();
        System.out.println("Server is listening on port " + serverPort);
    }

    static class JSONHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStream requestBody = exchange.getRequestBody();
                String json = convertStreamToString(requestBody);

                System.out.println("Received JSON:");
                System.out.println(json);

                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                String email = jsonObject.get("email").getAsString();
                System.out.println("Email: " + email);

                // Parse JSON using Gson
                //Gson gson = new Gson();
                //JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

                // Here you can process the received JSON data

                // Send a response (optional)
                String response = "JSON received successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    private static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
