package org.game.thegreatescape.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The GameData class holds various game-related data, such as tile information and starting player position.
 */
public class GameData {
    // Map to store tiles with their corresponding GameItem objects
    public Map<String, GameItem> tiles = new HashMap<>();

    // Starting position and animation phase for the player
    public int START_POSITION_PLAYERX = 256;
    public int START_POSITION_PLAYERY = 256;
    public int START_ANIMATION_PHASE = 0;

    /**
     * Default constructor for the GameData class.
     */
    public GameData() {}
}
