package cs151.application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles storing and retrieving of deck objects from a csv file
 */
public class DeckStore {
    // File name used to store deck data
    private static final String FILE_NAME = "decks.csv";

    // Stores existing deck names for uniqueness
    private static Set<String> deckNames = new HashSet<>();

    /**
     * Check if deck name is unique
     * @param name the deck name to check
     * @return true if name is not used, false otherwise
     */
    public static boolean isUnique(String name) {
        loadDecks();
        return !deckNames.contains(name.trim());
    }

    /**
     * Adds a new deck to the storage file
     * @param deck the Deck object to be stored
     */
    public static void addDeck(Deck deck) {
        deckNames.add(deck.getName().trim());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String line = escapeCsv(deck.getName().trim()) + "," +
                    escapeCsv(deck.getDescription() == null ? "" : deck.getDescription().trim());
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the deck names from file into memory
     * Also used to check duplicate names
     */
    public static void loadDecks() {
        deckNames.clear();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);

                if (parts.length > 0) {
                    deckNames.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escapes special characters for CSV formatting
     * @param value string to be formatted
     * @return a properly escaped CSV string
     */
    private static String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return '"' + value + '"';
        }
        return value;
    }

    /**
     * Retrieves all the stored decks from csv file
     * @return a list of Deck objects
     */
    public static List<Deck> getAllDecks() {
        List<Deck> decks = new ArrayList<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return decks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);

                String name = parts[0].trim();
                String des;

                if (parts.length > 1) {
                    des = parts[1].trim();
                } else {
                    des = "";
                }

                decks.add(new Deck(name, des));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decks;
    }
    public static List<String> getAllDeckNames()
    {
        List<String> names = new ArrayList<>();

        for (Deck deck : getAllDecks())
        {
            names.add(deck.getName());
        }

        return names;
    }

    /**
     * Saves all decks back into the csv file
     * @param decks list of decks to save
     */
    public static void saveAllDecks(List<Deck> decks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Deck deck : decks) {
                String line = escapeCsv(deck.getName().trim()) + "," +
                        escapeCsv(deck.getDescription() == null ? "" : deck.getDescription().trim());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadDecks();
    }

    /**
     * Deletes a deck by name
     * @param deckName name of the deck to delete
     * @return true if deleted, false if not found
     */
    public static boolean deleteDeck(String deckName) {
        List<Deck> decks = getAllDecks();
        boolean removed = false;

        for (int i = 0; i < decks.size(); i++) {
            if (decks.get(i).getName().equalsIgnoreCase(deckName.trim())) {
                decks.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            saveAllDecks(decks);
            FlashcardStore.deleteFlashcardsByDeck(deckName);
        }

        return removed;
    }

    /**
     * Edits a deck's name and description
     * @param oldName current deck name
     * @param newName updated deck name
     * @param newDescription updated deck description
     * @return true if updated, false if deck not found or new name already exists
     */
    public static boolean editDeck(String oldName, String newName, String newDescription) {
        List<Deck> decks = getAllDecks();
    
        for (Deck deck : decks) {
            if (deck.getName().equalsIgnoreCase(oldName.trim())) {
    
                if (!oldName.trim().equalsIgnoreCase(newName.trim()) && !isUnique(newName)) {
                    return false;
                }
    
                // Fix 1: Update associated flashcards if the name changed
                if (!oldName.trim().equalsIgnoreCase(newName.trim())) {
                    List<Flashcard> allCards = FlashcardStore.getAllFlashcards();
                    for (Flashcard card : allCards) {
                        if (card.getDeckName().equalsIgnoreCase(oldName.trim())) {
                            card.setDeckName(newName.trim());
                        }
                    }
                    FlashcardStore.saveAllFlashcards(allCards);
                }
    
                deck.setName(newName.trim());
                deck.setDescription(newDescription == null ? "" : newDescription.trim());
                saveAllDecks(decks);
                return true;
            }
        }
        return false;
    }
}
