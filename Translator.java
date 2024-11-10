import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

public class Translator {
    public static void main(String[] args) throws Exception {
        // Load configuration
        Properties config = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            config.load(input);
        }

        // Initialize services
        TranslationService translationService = new TranslationService(config);
        DatabaseService databaseService = new DatabaseService(config);
        databaseService.initializeDatabase();

        // Define the source and target languages
        String fromLang = "en"; // default source language is English
        String toLang = "es";   // default target language is Spanish

        // Prompt user for the text to translate
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the text you want to translate from English to Spanish: ");
        String text = scanner.nextLine();

        // Translate the text
        String translatedText = translationService.translate(fromLang, toLang, text);
        System.out.println("Translated Text: " + translatedText);
        System.out.println("text saved on DataBase.");

        // Store the translation in the database
        databaseService.storeTranslation(fromLang, toLang, text, translatedText);

        // Close the scanner
        scanner.close();
    }
}
