package org.game.thegreatescape.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.*;

/**
 * The Game class represents the main application class for "The Great Escape" game.
 * Initializes the game's main menu scene.
 */
public class Game extends Application{
    static Logger logger = Logger.getLogger(Game.class.getName());


    /**
     * Starts the application by setting up the main menu scene and displaying the stage.
     *
     * @param stage The primary stage of the application.
     */
    @Override
    public void start(Stage stage) {

        logger.info("Starting the application...");
        stage.setTitle("The Great Escape"); // set title of the stage
        stage.setResizable(false);

        // Create and set the main menu scene
        logger.info("Set MainMenuFactory");
        Scene mainMenuScene = new MainMenuFactory(stage).getMainMenu();

        // Show the stage
        stage.setScene(mainMenuScene);
        stage.show();

        logger.info("Application started successfully.");
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        boolean enableLogging = true;

        for (String arg : args) {
            if (arg.equals("--disable-logging")) {
                enableLogging = false;
                break;
            }
        }

        if (!enableLogging) {
            LogManager.getLogManager().reset();
        }

        launch(args);
    }
}
