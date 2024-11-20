package projeto;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataProcessor {
    private static final String BASE_PATH = "/Users/juliacorrea/eclipse-workspace/Sockets/bin/dados/";

    public static List<String> search(String fileName, String query) {
        List<String> results = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(BASE_PATH + fileName)) {
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
