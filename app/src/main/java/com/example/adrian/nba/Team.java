package com.example.adrian.nba;

/**
 * Created by adrian on 2/2/15.
 */
public class Team {
    private String displayName;
    private String [] playerNames;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }
}
