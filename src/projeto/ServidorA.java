package projeto;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

public class ServidorA {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5001)) {
            System.out.println("Servidor A aguardando conexão...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerAHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor A: " + e.getMessage());
        }
    }
}

class ServerAHandler implements Runnable {
    private final Socket clientSocket;

    public ServerAHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String query = in.readLine();
            if (query == null || query.trim().isEmpty()) {
                out.println("A consulta não pode estar vazia.");
                return;
            }

            List<String> resultsA = search("src/dados/data_A.json", query);
            if (resultsA.isEmpty()) {
                out.println("Nenhum resultado encontrado no Servidor A.");
            } else {
                out.println("Resultados do Servidor A:");
                for (String result : resultsA) {
                    out.println(result);
                }
            }

            List<String> resultsB = new ArrayList<>();
            try (Socket socketB = new Socket("localhost", 6000);
                 PrintWriter outB = new PrintWriter(socketB.getOutputStream(), true);
                 BufferedReader inB = new BufferedReader(new InputStreamReader(socketB.getInputStream()))) {

                outB.println(query);
                String line;
                while ((line = inB.readLine()) != null) {
                    resultsB.add(line);
                }
            }

            if (resultsB.isEmpty()) {
                out.println("Nenhum resultado encontrado no Servidor B.");
            } else {
                out.println("Resultados do Servidor B:");
                for (String result : resultsB) {
                    out.println(result);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro de comunicação no servidor A: " + e.getMessage());
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
