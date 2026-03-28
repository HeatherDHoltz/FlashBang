package cs151.application;

import java.time.LocalDateTime;

public class Flashcard
{
    private String deckName;
    private String frontText;
    private String backText;
    private LocalDateTime creationDate;

    // constructor for NEW flashcards
    public Flashcard(String deckName, String frontText, String backText)
    {
        this.deckName = deckName;
        this.frontText = frontText;
        this.backText = backText;
        this.creationDate = LocalDateTime.now();
    }

    // constructor for LOADING from file
    public Flashcard(String deckName, String frontText, String backText, LocalDateTime creationDate)
    {
        this.deckName = deckName;
        this.frontText = frontText;
        this.backText = backText;
        this.creationDate = creationDate;
    }

    public String getDeckName()
    { return deckName; }

    public String getFrontText()
    { return frontText; }

    public String getBackText()
    { return backText; }

    public LocalDateTime getCreationDate()
    { return creationDate; }
}