package cs151.application;

import java.io.Serializable;

/**
 *
 * Flashard deck with name and description
 */
public class Deck implements Serializable {
    //Deck name
    private String name;
    //The description of deck content
    private String description;

    /**
     * Creates a new deck object
     * @param name deck name
     * @param description deck description
     */
    public Deck(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * getter for name
     * @return the name of the deck
     */
    public String getName() {
        return name;
    }

    /**
     * setter for name
     * @param name new name for deck
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for description
     * @return the description of the deck
     */
    public String getDescription() {
        return description;
    }

    /**
     *  Setter for description
     * @param description new description of the deck
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

