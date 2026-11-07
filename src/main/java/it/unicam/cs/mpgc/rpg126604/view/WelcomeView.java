
package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeView {

    private final GameController controller;
    private final Stage stage;

    public WelcomeView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void show() {
        Label title = new Label("SpaceMystery");

        Label subtitle = new Label(
                "Un mistero si nasconde a bordo della stazione spaziale."
        );

        Label description = new Label(
                "Sei Alex, un astronauta incaricato di indagare su strani eventi.\n"
                        + "Esplora le stanze, raccogli indizi e scopri la verità."
        );
        description.setWrapText(true);

        Button startButton = new Button("Inizia il gioco");
        startButton.setOnAction(e -> {
            GameView gameView = new GameView(controller, stage);
            gameView.show();
        });

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.getChildren().addAll(
                title,
                subtitle,
                description,
                startButton
        );

        Scene scene = new Scene(root, 1024, 768);

        stage.setTitle("SpaceMystery - Benvenuto");
        stage.setScene(scene);
        stage.show();
    }
}
