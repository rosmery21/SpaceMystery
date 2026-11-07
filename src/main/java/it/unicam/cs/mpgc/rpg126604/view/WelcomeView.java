
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

        Label title = new Label("SPACE MYSTERY");
        title.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold;"
        );

        Label subtitle = new Label(
                "Un mistero si nasconde a bordo della stazione spaziale."
        );
        subtitle.setStyle("-fx-font-size: 18px;");

        Label description = new Label(
                "Sei Alex, un astronauta incaricato di indagare "
                        + "su strani eventi a bordo della stazione.\n"
                        + "Esplora le stanze, raccogli indizi "
                        + "e scopri la verità."
        );
        description.setWrapText(true);
        description.setMaxWidth(650);
        description.setAlignment(Pos.CENTER);
        description.setStyle("-fx-font-size: 15px;");

        Label tutorialTitle = new Label("COME SI GIOCA");
        tutorialTitle.getStyleClass().add("section-title");

        Label tutorial = new Label(
                "1. ESPLORA\n"
                        + "Spostati tra le stanze della stazione spaziale.\n\n"
                        + "2. INDAGA\n"
                        + "Raccogli gli indizi che trovi durante l'esplorazione.\n\n"
                        + "3. CONSULTA IL TACCUINO\n"
                        + "Rileggi gli indizi raccolti per ricostruire gli eventi.\n\n"
                        + "4. CREA RAPPORTI\n"
                        + "Pubblica le tue scoperte per aumentare la reputazione."
        );
        tutorial.setWrapText(true);
        tutorial.setMaxWidth(650);
        tutorial.setStyle(
                "-fx-font-size: 14px; -fx-line-spacing: 3px;"
        );

        Button startButton = new Button("INIZIA LA MISSIONE");
        startButton.setPrefWidth(240);
        startButton.setOnAction(event -> {
            GameView gameView = new GameView(controller, stage);
            gameView.show();
        });

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));

        root.getChildren().addAll(
                title,
                subtitle,
                description,
                tutorialTitle,
                tutorial,
                startButton
        );

        Scene scene = new Scene(root, 1024, 768);

        String css = getClass()
                .getResource("/css/style.css")
                .toExternalForm();

        scene.getStylesheets().add(css);

        stage.setTitle("SpaceMystery - Benvenuto");
        stage.setScene(scene);
        stage.show();
    }
}
