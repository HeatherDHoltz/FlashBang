package cs151.application;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storing and retrieving flashcards from a CSV file
 */
public class FlashcardStore
{
    // Separate file (DO NOT use DeckStore.FILE_NAME)
    private static final String FILE_NAME = "flashcards.csv";

    /**
     * Saves a flashcard to file
     */
    public static void addFlashcard(Flashcard card)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

            writer.write(card.getDeckName() + "," +
                    card.getFrontText() + "," +
                    card.getBackText() + "," +
                    card.getCreationDate());

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
                String[] parts = line.split(",", 4);

                if (parts.length == 4)
                {
                    String deck = parts[0];
                    String front = parts[1];
                    String back = parts[2];
                    LocalDateTime date = LocalDateTime.parse(parts[3]);


                    Flashcard card = new Flashcard(deck, front, back, date);

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
}