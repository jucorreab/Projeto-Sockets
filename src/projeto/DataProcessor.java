package projeto;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataProcessor {
    public static List<String> search(String fileName, String query) {
        List<String> results = new ArrayList<>();
        try {
        	InputStream inputStream = new FileInputStream("/Users/juliacorrea/eclipse-workspace/Sockets/bin/dados/" + fileName);
            if (inputStream == null) {
                throw new IllegalArgumentException("Arquivo não encontrado: " + fileName);
            }
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);
            JSONObject titles = json.getJSONObject("title");
            
            for (String key : titles.keySet()) {
                String title = titles.getString(key);
                if (title.toLowerCase().contains(query.toLowerCase())) {
                    results.add(title);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}


