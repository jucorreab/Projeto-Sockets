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
    
            System.out.println("         Resultados da busca             ");
            System.out.println("=========================================");
            String result;
            while ((result = in.readLine()) != null) {
                if (result.startsWith("Resultados do")) {
                    System.out.println();
                    System.out.println(result); 
                    System.out.println("-----------------------------------------");
                } else {
                    System.out.println("- " + result); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
