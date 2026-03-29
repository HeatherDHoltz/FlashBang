package cs151.application;

import java.time.LocalDateTime;

/**
 * Represents  asingle flashcard with a front for question and prompty
 * and a back for the answer. Also holds deckname and creation stamp
 */
public class Flashcard
{
    private String deckName;
    private String frontText;
    private String backText;
    private LocalDateTime creationDate;

    /**
     * Constructor for flash card
     * @param deckName name of the deck
     * @param frontText the question or pormpt
     * @param backText the answer
     */
    public Flashcard(String deckName, String frontText, String backText)
    {
        this.deckName = deckName;
        this.frontText = frontText;
        this.backText = backText;
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Constructor for loading existing flash card from storage
     * @param deckName name of deck
     * @param frontText question or pormpt
     * @param backText the answer
     * @param creationDate the original creation date of the flashcard
     */
    public Flashcard(String deckName, String frontText, String backText, LocalDateTime creationDate)
    {
        this.deckName = deckName;
        this.frontText = frontText;
        this.backText = backText;
        this.creationDate = creationDate;
    }

    /**
     * Getter method for deckName
     * @return the deck Name
     */
    public String getDeckName()
    { return deckName; }

    /**
     * getter method for front Text
     * @return the front text
     */
    public String getFrontText()
    { return frontText; }

    /**
     * Getter methdo for backText
     * @return backText
     */
    public String getBackText()
    { return backText; }

    /**
     * Getter method for creationDate
     * @return teh creationDate
     */
    public LocalDateTime getCreationDate()
    { return creationDate; }
}