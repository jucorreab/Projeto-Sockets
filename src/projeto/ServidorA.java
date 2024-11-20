package projeto;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorA {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5001)) {
            System.out.println("Servidor A aguardando conexão...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServerAHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

            List<String> resultsA = DataProcessor.search("data_A.json", query);
            out.println("Resultados do Servidor A:");
            for (String result : resultsA) {
                out.println(result);
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

            out.println("Resultados do Servidor B:");
            for (String result : resultsB) {
                out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
