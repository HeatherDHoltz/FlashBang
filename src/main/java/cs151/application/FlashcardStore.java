package cs151.application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storing and retrieving flashcards from a CSV file
 */
public class FlashcardStore
{
    // Separate file (DO NOT use DeckStore.FILE_NAME)
    private static final String FILE_NAME = "flashcards.txt";
    private static final String SEPARATOR = "||";


    /**
     * Saves a flashcard to file
     */
    public static void addFlashcard(Flashcard card)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

            String lastReview = "";
            if (card.getLastReviewDate() != null)
            {
                lastReview = card.getLastReviewDate().toString();
            }

            writer.write(card.getDeckName() + SEPARATOR +
                    card.getFrontText().replace("\n", "<br>").replace("\r", "") + SEPARATOR +
                    card.getBackText().replace("\n", "<br>").replace("\r", "") + SEPARATOR +
                    card.getCreationDate() + SEPARATOR +
                    card.getStatus() + SEPARATOR +
                    lastReview);

            writer.newLine();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Loads all flashcards from file
     */
    public static List<Flashcard> getAllFlashcards()
    {
        List<Flashcard> list = new ArrayList<>();

        File file = new File(FILE_NAME);

        // If file doesn't exist, return empty list
        if (!file.exists())
        {
            return list;
        }

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("\\|\\|", -1);
                if (parts.length == 6)
                {
                    String deck = parts[0];
                    String front = parts[1].replace("<br>", "\n");
                    String back = parts[2].replace("<br>", "\n");
                    LocalDateTime creationDate = LocalDateTime.parse(parts[3]);
                    FlashcardStatus status = FlashcardStatus.valueOf(parts[4]);

                    LocalDateTime lastReviewDate = null;
                    if (!parts[5].isEmpty())
                    {
                        lastReviewDate = LocalDateTime.parse(parts[5]);
                    }

                    Flashcard card = new Flashcard(deck, front, back, creationDate, status, lastReviewDate);
                    list.add(card);
                }
            }

            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Sort newest first
        list.sort((a, b) -> b.getCreationDate().compareTo(a.getCreationDate()));

        return list;
    }
    public static void saveAllFlashcards(List<Flashcard> flashcards)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

            for (Flashcard card : flashcards)
            {
                String lastReview = "";
                if (card.getLastReviewDate() != null)
                {
                    lastReview = card.getLastReviewDate().toString();
                }

                writer.write(card.getDeckName() + SEPARATOR +
                        card.getFrontText().replace("\n", "<br>").replace("\r", "") + SEPARATOR +
                        card.getBackText().replace("\n", "<br>").replace("\r", "") + SEPARATOR +
                        card.getCreationDate() + SEPARATOR +
                        card.getStatus() + SEPARATOR +
                        lastReview);

                writer.newLine();
            }

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}