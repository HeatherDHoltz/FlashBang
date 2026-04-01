package cs151.application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storing and retrieving flashcards from a text file.
 */
public class FlashcardStore {
    private static final String FILE_NAME = "flashcards.txt";
    private static final String SEPARATOR = "||";

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
                String[] parts = line.split("\\|\\|", -1);
                if (parts.length == 6) {
                    String deck = parts[0];
                    String front = parts[1].replace("<br>", "\n");
                    String back = parts[2].replace("<br>", "\n");
                    LocalDateTime creationDate = LocalDateTime.parse(parts[3]);
                    FlashcardStatus status = FlashcardStatus.valueOf(parts[4]);

                    LocalDateTime lastReviewDate = null;
                    if (!parts[5].isEmpty()) {
                        lastReviewDate = LocalDateTime.parse(parts[5]);
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

        return card.getDeckName() + SEPARATOR +
                card.getFrontText().replace("\n", "<br>").replace("\r", "") + SEPARATOR +
                card.getBackText().replace("\n", "<br>").replace("\r", "") + SEPARATOR +
                card.getCreationDate() + SEPARATOR +
                card.getStatus() + SEPARATOR +
                lastReview;
    }
}