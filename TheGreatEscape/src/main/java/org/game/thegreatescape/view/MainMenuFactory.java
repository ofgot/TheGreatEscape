package org.game.thegreatescape.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.game.thegreatescape.model.Model;

import java.util.logging.Logger;

/**
 * Factory class generates the main menu of the game.
 * This class constructs the main menu interface containing buttons for starting a new game, loading a saved game,
 * and exiting the application.
 */
public class MainMenuFactory {
    Logger logger = Logger.getLogger(Game.class.getName());

    // Constants
    private final int WIDTH = 550;
    private final int HEIGHT = 550;
    private final int NEW_GAME_BUTTON_HEIGHT = 200;
    private final int LOAD_GAME_BUTTON_HEIGHT = 150;
    private final int EXIT_BUTTON_HEIGHT = 100;
    private final int BUTTON_WIDTH = 50;
    private final int SPACING = 10;

    // Style
    private final String vboxStyle = "-fx-font-size:20;" +
            "-fx-font-family: 'Comic Sans MS'";

    // Stage
    private final Stage stage;


    /**
     * Constructor for the MainMenuFactory class.
     *
     * @param stage The primary stage of the application.
     */
    public MainMenuFactory(Stage stage) {
        this.stage = stage;
    }

    /**
     * Generates the main menu scene.
     * Contains 3 buttons, set image on background, set actions on buttons
     *
     * @return The main menu scene.
     */
    public Scene getMainMenu() {
        //images
        Image img = new Image("MainGameCover.png");
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(WIDTH);
        imgView.setFitHeight(HEIGHT);

        //buttons
        Button NewGameButton = new Button("New Game");
        Button LoadGameButton = new Button("Load Game");
        Button ExitButton = new Button("Exit");

        //exit button events
        ExitButton.setOnAction((ActionEvent event) -> {
            logger.info("Exit button clicked.");
            Platform.exit();
        });

        //new game button events
        NewGameButton.setOnAction((ActionEvent event) -> {
            logger.info("New Game button clicked.");
            stage.setScene(new GameSceneFactory(stage).getGameScene(new Model(1)));
        });

        //load game button events
        LoadGameButton.setOnAction((ActionEvent event) ->{
            logger.info("Load Game button clicked.");
            stage.setScene(new GameSceneFactory(stage).getGameScene(new Model()));
        });

        stage.setOnCloseRequest(windowEvent -> {
            logger.info("Stage closed.");
            stage.close();
        });

        //Vbox
        VBox vbox = new VBox(NewGameButton, LoadGameButton, ExitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(SPACING);
        vbox.setStyle(vboxStyle);

        // Vbox buttons size
        NewGameButton.setMinSize(NEW_GAME_BUTTON_HEIGHT, BUTTON_WIDTH);
        LoadGameButton.setMinSize(LOAD_GAME_BUTTON_HEIGHT, BUTTON_WIDTH);
        ExitButton.setMinSize(EXIT_BUTTON_HEIGHT, BUTTON_WIDTH);

        //Vbox buttons style
        NewGameButton.getStyleClass().add("button");
        LoadGameButton.getStyleClass().add("button");
        ExitButton.getStyleClass().add("button");

        //root and scene
        StackPane root = new StackPane(imgView, vbox);

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.getStylesheets().add("styles.css");

        return scene;
    }
}
