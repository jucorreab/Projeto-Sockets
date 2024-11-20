package projeto;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        String query = "Low-Mass X-ray"; 
        try (Socket socket = new Socket("localhost", 5001);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {           
            out.println(query); 
            System.out.println("Resultados da busca:");
            String result;
            while ((result = in.readLine()) != null) {
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

