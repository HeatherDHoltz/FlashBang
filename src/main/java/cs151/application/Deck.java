package cs151.application;

import java.io.Serializable;

public class Deck implements Serializable {
    private String name;
    private String description;

    public Deck(String name, String description) {
        if(name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Deck name cannot be empty");
        }

        this.name = name.trim();
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

