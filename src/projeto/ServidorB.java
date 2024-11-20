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
            e.printStackTrace();
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

            List<String> results = search("data_B.json", query);
            if (results.isEmpty()) {
                out.println("Nenhum resultado encontrado no Servidor B.");
            } else {
                for (String result : results) {
                    out.println(result);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> search(String fileName, String query) {
        List<String> results = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream("/Users/juliacorrea/eclipse-workspace/Sockets/bin/dados/" + fileName)) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);

            if (json.has("title")) {
                JSONObject titles = json.getJSONObject("title");

                for (String key : titles.keySet()) {
                    String title = titles.getString(key);
                    if (title.toLowerCase().contains(query.toLowerCase())) {
                        results.add(title);
                    }
                }
            } else {
                System.out.println("A chave 'title' não foi encontrada no arquivo " + fileName);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return results;
    }
}
