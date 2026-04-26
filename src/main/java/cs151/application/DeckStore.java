package cs151.application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeckStore {
    private static final String FILE_NAME = "decks.csv";
    private static Set<String> deckNames = new HashSet<>();

    public static boolean isUnique(String name) {
        loadDecks();
        return !deckNames.contains(name.trim());
    }

    public static void addDeck(Deck deck) {
        loadDecks();
        deckNames.add(deck.getName().trim());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(serialize(deck));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDecks() {
        deckNames.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = parseCsvLine(line);
                if (!parts.isEmpty()) {
                    deckNames.add(parts.get(0).trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Deck> getAllDecks() {
        List<Deck> decks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return decks;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = parseCsvLine(line);
                if (parts.size() >= 1) {
                    String name = parts.get(0).trim();
                    // Restore newlines from placeholder
                    String des = (parts.size() > 1) ? parts.get(1).replace("<br>", "\n").trim() : "";
                    decks.add(new Deck(name, des));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decks;
    }

    public static void saveAllDecks(List<Deck> decks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (Deck deck : decks) {
                writer.write(serialize(deck));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadDecks();
    }

    private static String serialize(Deck deck) {
        String description = deck.getDescription() == null ? "" : deck.getDescription();
        // Keep row on one line by replacing \n with <br>
        String safeDescription = description.replace("\n", "<br>").replace("\r", "");
        return escapeCsv(deck.getName().trim()) + "," + escapeCsv(safeDescription);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "\"\"";
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

    public static List<String> getAllDeckNames() {
        List<String> names = new ArrayList<>();
        for (Deck deck : getAllDecks()) {
            names.add(deck.getName());
        }
        return names;
    }

    public static boolean deleteDeck(String deckName) {
        List<Deck> decks = getAllDecks();
        boolean removed = decks.removeIf(d -> d.getName().equalsIgnoreCase(deckName.trim()));
        if (removed) {
            saveAllDecks(decks);
            FlashcardStore.deleteFlashcardsByDeck(deckName);
        }
        return removed;
    }

    public static boolean editDeck(String oldName, String newName, String newDescription) {
        List<Deck> decks = getAllDecks();
        for (Deck deck : decks) {
            if (deck.getName().equalsIgnoreCase(oldName.trim())) {
                if (!oldName.trim().equalsIgnoreCase(newName.trim()) && !isUnique(newName)) {
                    return false;
                }
                // Update associated flashcards if name changed
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
