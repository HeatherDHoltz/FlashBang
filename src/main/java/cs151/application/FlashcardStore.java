package cs151.application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storing and retrieving flashcards from a CSV file.
 */
public class FlashcardStore {
    private static final String FILE_NAME = "flashcards.csv";

    /**
     * Creates the required sample data for the project if no flashcards exist yet.
     */
    public static void ensureSeedData() {
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            return;
        }

        List<Flashcard> seedCards = new ArrayList<>();
        seedCards.add(createSeedCard(
                "CS151 - Design Patterns",
                "What does MVC stand for?",
                "Model View Controller",
                LocalDateTime.now().minusDays(3),
                FlashcardStatus.NEW,
                null));
        seedCards.add(createSeedCard(
                "Spanish Vocabulary",
                "Hola",
                "Hello",
                LocalDateTime.now().minusDays(2),
                FlashcardStatus.LEARNING,
                LocalDateTime.now().minusDays(1)));
        seedCards.add(createSeedCard(
                "Biology Basics",
                "What is the powerhouse of the cell?",
                "Mitochondria",
                LocalDateTime.now().minusDays(1),
                FlashcardStatus.MASTERED,
                LocalDateTime.now().minusHours(12)));

        saveAllFlashcards(seedCards);
    }

    private static Flashcard createSeedCard(String deckName,
                                            String frontText,
                                            String backText,
                                            LocalDateTime creationDate,
                                            FlashcardStatus status,
                                            LocalDateTime lastReviewDate) {
        return new Flashcard(deckName, frontText, backText, creationDate, status, lastReviewDate);
    }

    /**
     * Saves a flashcard to file.
     */
    public static void addFlashcard(Flashcard card) {
        ensureSeedData();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(serialize(card));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all flashcards from file.
     */
    public static List<Flashcard> getAllFlashcards() {
        ensureSeedData();

        List<Flashcard> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = parseCsvLine(line);

                if (parts.size() == 6) {
                    String deck = parts.get(0);
                    String front = parts.get(1).replace("<br>", "\n");
                    String back = parts.get(2).replace("<br>", "\n");
                    LocalDateTime creationDate = LocalDateTime.parse(parts.get(3));
                    FlashcardStatus status = FlashcardStatus.valueOf(parts.get(4));

                    LocalDateTime lastReviewDate = null;
                    if (!parts.get(5).isEmpty()) {
                        lastReviewDate = LocalDateTime.parse(parts.get(5));
                    }

                    list.add(new Flashcard(deck, front, back, creationDate, status, lastReviewDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.sort((a, b) -> b.getCreationDate().compareTo(a.getCreationDate()));
        return list;
    }

    /**
     * Rewrites the flashcard file using the provided list.
     */
    public static void saveAllFlashcards(List<Flashcard> flashcards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Flashcard card : flashcards) {
                writer.write(serialize(card));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String serialize(Flashcard card) {
        String lastReview = "";
        if (card.getLastReviewDate() != null) {
            lastReview = card.getLastReviewDate().toString();
        }

        return escapeCsv(card.getDeckName()) + "," +
                escapeCsv(card.getFrontText().replace("\n", "<br>").replace("\r", "")) + "," +
                escapeCsv(card.getBackText().replace("\n", "<br>").replace("\r", "")) + "," +
                escapeCsv(card.getCreationDate().toString()) + "," +
                escapeCsv(card.getStatus().toString()) + "," +
                escapeCsv(lastReview);
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(current.toString());
        return values;
    }
}