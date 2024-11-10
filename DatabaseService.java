import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseService {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public DatabaseService(Properties config) {
        this.dbUrl = config.getProperty("db_url");
        this.dbUser = config.getProperty("db_user");
        this.dbPassword = config.getProperty("db_password");
    }

    public void initializeDatabase() throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            String createTableQuery = """
                CREATE TABLE IF NOT EXISTS translations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    from_lang VARCHAR(10),
                    to_lang VARCHAR(10),
                    original_text TEXT,
                    translated_text TEXT,
                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.executeUpdate(createTableQuery);
        }
    }

    public void storeTranslation(String fromLang, String toLang, String originalText, String translatedText) throws Exception {
        String query = "INSERT INTO translations (from_lang, to_lang, original_text, translated_text) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, fromLang);
            pstmt.setString(2, toLang);
            pstmt.setString(3, originalText);
            pstmt.setString(4, translatedText);
            pstmt.executeUpdate();
        }
    }
}
