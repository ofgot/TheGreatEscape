package org.game.thegreatescape.model;

public class SavedGameDataPlayer {

    public String savedLevel;
    public int savedPlayerPositionX;
    public int savedPlayerPositionY;

    public void setSavedLevel(String savedLevel) {
        this.savedLevel = savedLevel;
    }

    public void setSavedPlayerPositionX(int playerPositionX) {
        savedPlayerPositionX = playerPositionX;
    }

    public void setSavedPlayerPositionY(int playerPositionY) {
        savedPlayerPositionY = playerPositionY;
    }
}
