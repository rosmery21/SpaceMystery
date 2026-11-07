
package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeView {

    private final GameController controller;
    private final Stage stage;

    private int currentStep = 0;
    private Label typewriterLabel;
    private Timeline typewriterTimeline;

    private final String[] titles = {
            "SPACE MYSTERY",
            "LA STAZIONE SPAZIALE",
            "LA TUA MISSIONE",
            "COME GIOCARE",
            "CONSIGLI",
            "SEI PRONTO?"
    };

    private final String[] contents = {
            "Benvenuto a bordo.\n\n"
                    + "Sei Alex, un astronauta "
                    + "in missione su una stazione spaziale.\n\n"
                    + "Qualcosa di misterioso sta accadendo...",

            "La stazione nasconde segreti.\n\n"
                    + "La Sala Comandi, il Laboratorio Scientifico "
                    + "e la Sala Motori potrebbero contenere "
                    + "indizi importanti.\n\n"
                    + "Esplora ogni ambiente con attenzione.",

            "Devi indagare sugli strani eventi "
                    + "che si verificano a bordo.\n\n"
                    + "Raccogli gli indizi, "
                    + "ricostruisci gli avvenimenti "
                    + "e prepara i tuoi rapporti investigativi.",

            "1. ESPLORA\n"
                    + "Spostati tra le stanze della stazione.\n\n"
                    + "2. INDAGA\n"
                    + "Raccogli gli indizi disponibili.\n\n"
                    + "3. CONSULTA IL TACCUINO\n"
                    + "Rileggi le informazioni raccolte.\n\n"
                    + "4. CREA RAPPORTI\n"
                    + "Pubblica le scoperte per aumentare "
                    + "la reputazione.",

            "Usa il pulsante MAPPA DELLA STAZIONE "
                    + "per visualizzare gli ambienti.\n\n"
                    + "Nella mappa puoi utilizzare "
                    + "WASD oppure le frecce direzionali.\n\n"
                    + "Premi INVIO per raggiungere "
                    + "la stanza selezionata.\n\n"
                    + "Ogni indizio può essere importante!",

            "La verità è nascosta "
                    + "tra i corridoi della stazione.\n\n"
                    + "Solo un'indagine accurata "
                    + "potrà svelare il mistero.\n\n"
                    + "Buona fortuna, Alex!"
    };

    public WelcomeView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void show() {
        showStep(currentStep);
    }

    private void showStep(int step) {
        currentStep = step;

        // Interrompiamo l'animazione della schermata precedente.
        if (typewriterTimeline != null) {
            typewriterTimeline.stop();
        }

        VBox root = new VBox(24);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(45));

        Label gameTitle = new Label("SPACE MYSTERY");
        gameTitle.setStyle(
                "-fx-font-size: 34px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-text-fill: #7dd3fc;"
        );

        Label stepIndicator = new Label(
                "MISSIONE | " + (step + 1) + " / " + titles.length
        );
        stepIndicator.setStyle(
                "-fx-text-fill: #94a3b8;"
                        + "-fx-font-size: 13px;"
        );

        Label titleLabel = new Label(titles[step]);
        titleLabel.getStyleClass().add("section-title");
        titleLabel.setStyle("-fx-font-size: 23px;");

        Separator topSeparator = new Separator();
        topSeparator.setMaxWidth(700);

        typewriterLabel = new Label();
        typewriterLabel.setWrapText(true);
        typewriterLabel.setAlignment(Pos.CENTER);
        typewriterLabel.setMaxWidth(700);
        typewriterLabel.setMinHeight(240);
        typewriterLabel.setStyle(
                "-fx-font-size: 17px;"
                        + "-fx-text-fill: #e5edf8;"
                        + "-fx-line-spacing: 5px;"
        );

        Separator bottomSeparator = new Separator();
        bottomSeparator.setMaxWidth(700);

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        if (step > 0) {
            Button backButton = new Button("INDIETRO");
            backButton.setPrefWidth(160);
            backButton.setOnAction(event -> showStep(step - 1));

            buttons.getChildren().add(backButton);
        }

        if (step < titles.length - 1) {
            Button nextButton = new Button("AVANTI");
            nextButton.setPrefWidth(160);
            nextButton.setOnAction(event -> showStep(step + 1));

            buttons.getChildren().add(nextButton);
        } else {
            Button startButton = new Button("INIZIA LA MISSIONE");
            startButton.setPrefWidth(240);

            startButton.setOnAction(event -> {
                if (typewriterTimeline != null) {
                    typewriterTimeline.stop();
                }

                GameView gameView = new GameView(controller, stage);
                gameView.show();
            });

            buttons.getChildren().add(startButton);
        }

        root.getChildren().addAll(
                gameTitle,
                stepIndicator,
                titleLabel,
                topSeparator,
                typewriterLabel,
                bottomSeparator,
                buttons
        );

        Scene scene = new Scene(root, 1024, 768);

        String css = getClass()
                .getResource("/css/style.css")
                .toExternalForm();

        scene.getStylesheets().add(css);

        stage.setTitle("SpaceMystery - Benvenuto");
        stage.setScene(scene);
        stage.show();

        // Avviamo l'effetto macchina da scrivere.
        startTypewriterEffect(contents[step]);
    }

    private void startTypewriterEffect(String text) {
        typewriterLabel.setText("");

        final int[] index = {0};

        typewriterTimeline = new Timeline(
                new KeyFrame(Duration.millis(30), event -> {
                    if (index[0] < text.length()) {
                        typewriterLabel.setText(
                                typewriterLabel.getText()
                                        + text.charAt(index[0])
                        );

                        index[0]++;
                    }
                })
        );

        typewriterTimeline.setCycleCount(text.length());
        typewriterTimeline.play();
    }
}
