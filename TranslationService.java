import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class TranslationService {
    private final String clientId;
    private final String clientSecret;
    private final String endpoint;

    public TranslationService(Properties config) {
        this.clientId = config.getProperty("client_id");
        this.clientSecret = config.getProperty("client_secret");
        this.endpoint = config.getProperty("api_endpoint");
    }

    public String translate(String fromLang, String toLang, String text) throws Exception {
        // JSON payload
        String jsonPayload = String.format(
            "{\"fromLang\":\"%s\",\"toLang\":\"%s\",\"text\":\"%s\"}", fromLang, toLang, text);

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-WM-CLIENT-ID", clientId);
        conn.setRequestProperty("X-WM-CLIENT-SECRET", clientSecret);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes("UTF-8"));
            os.flush();
        }

        int statusCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            (statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()))) {
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
        }
        conn.disconnect();
        return response.toString();
    }
}
