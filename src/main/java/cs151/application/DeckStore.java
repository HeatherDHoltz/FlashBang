package cs151.application;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import cs151.application.Deck;

public class DeckStore {
    private static final String FILE_NAME = "decks.csv";
    private static Set<String> deckNames = new HashSet<>();

    public static boolean isUnique(String name) {
        loadDecks();
        return !deckNames.contains(name.trim().toLowerCase());
    }

    public static boolean addDeck(Deck deck) {
        loadDecks();
        String name = deck.getName().trim();
        if(deckNames.contains(name))
        {
            return false; //duplicate
        }

        deckNames.add(deck.getName().trim());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String line = escapeCsv(deck.getName().trim()) + "," + escapeCsv(deck.getDescription() == null ? "" : deck.getDescription().trim());
            writer.write(line);
            writer.newLine();

            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void loadDecks() {
        deckNames.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length > 0) {
                    deckNames.add(parts[0].trim().toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return '"' + value + '"';
        }
        return value;
    }
}
