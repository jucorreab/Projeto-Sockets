package projeto;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class ServidorB {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("Servidor B aguardando conexão...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerBHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor B: " + e.getMessage());
        }
    }
}

class ServerBHandler implements Runnable {
    private final Socket socket;

    public ServerBHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String query = in.readLine();
            if (query == null || query.trim().isEmpty()) {
                out.println("A consulta não pode estar vazia.");
                return;
            }

            List<String> results = search("src/dados/data_B.json", query);
            if (results.isEmpty()) {
                out.println("Nenhum resultado encontrado no Servidor B.");
            } else {
                for (String result : results) {
                    out.println(result);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro de comunicação no servidor B: " + e.getMessage());
        }
    }

    private List<String> search(String filePath, String query) {
        List<String> results = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);
            JSONObject titles = json.optJSONObject("title");
            JSONObject labels = json.optJSONObject("label");

            if (titles != null) {
                for (String key : titles.keySet()) {
                    String title = titles.getString(key);
                    String label = labels != null && labels.has(key) ? labels.getString(key) : "";
                    String combined = title + " " + label;

                    if (combined.toLowerCase().contains(query.toLowerCase())) {
                        results.add("Título: " + title + (label.isEmpty() ? "" : " | Rótulo: " + label));
                    }
                }
            }
        } catch (IOException | JSONException e) {
            System.err.println("Erro ao processar o arquivo " + filePath + ": " + e.getMessage());
        }
        return results;
    }
}
