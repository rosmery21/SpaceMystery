package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

// mostra la schermata iniziale di SpaceMystery
public class WelcomeView {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 800;

    private static final String CYAN = "#7dd3fc";
    private static final String BLUE = "#2563eb";
    private static final String DARK = "#050b1b";

    private final GameController controller;
    private final Stage stage;

    private int currentStep = 0;
    private Label typewriterLabel;
    private Timeline typewriterTimeline;

    // Lo sfondo viene disegnato una sola volta e riutilizzato.
    private final Canvas bgCanvas;

    // Ogni schermata del tutorial contiene il proprio titolo e testo.
    private record TutorialStep(String title, String content) {}

    private final TutorialStep[] steps = {
            new TutorialStep(
                    "SPACE MYSTERY",
                    "Benvenuto a bordo.\n\n" +
                            "Sei Alex, un astronauta in missione su una stazione spaziale.\n\n" +
                            "Qualcosa di misterioso sta accadendo..."
            ),
            new TutorialStep(
                    "LA STAZIONE SPAZIALE",
                    "La stazione nasconde segreti.\n\n" +
                            "La Sala Comandi, il Laboratorio Scientifico e " +
                            "la Sala Motori potrebbero contenere indizi importanti.\n\n" +
                            "Esplora ogni ambiente con attenzione."
            ),
            new TutorialStep(
                    "LA TUA MISSIONE",
                    "Devi indagare sugli strani eventi che si verificano a bordo.\n\n" +
                            "Raccogli gli indizi, ricostruisci gli avvenimenti " +
                            "e prepara i tuoi rapporti investigativi."
            ),
            new TutorialStep(
                    "COME GIOCARE",
                    "1. ESPLORA\n" +
                            "Spostati tra le stanze della stazione.\n\n" +
                            "2. INDAGA\n" +
                            "Raccogli gli indizi disponibili.\n\n" +
                            "3. CONSULTA IL TACCUINO\n" +
                            "Rileggi le informazioni raccolte.\n\n" +
                            "4. CREA RAPPORTI\n" +
                            "Pubblica le scoperte per aumentare la reputazione."
            ),
            new TutorialStep(
                    "CONSIGLI",
                    "Usa il pulsante MAPPA DELLA STAZIONE " +
                            "per visualizzare gli ambienti.\n\n" +
                            "Nella mappa puoi utilizzare WASD oppure " +
                            "le frecce direzionali.\n\n" +
                            "Premi INVIO per raggiungere la stanza selezionata.\n\n" +
                            "Ogni indizio può essere importante!"
            ),
            new TutorialStep(
                    "SEI PRONTO?",
                    "La verità è nascosta tra i corridoi della stazione.\n\n" +
                            "Solo un'indagine accurata potrà svelare il mistero.\n\n" +
                            "Buona fortuna, Alex!"
            )
    };

    public WelcomeView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;

        // Disegniamo lo sfondo una sola volta.
        this.bgCanvas = new Canvas(WIDTH, HEIGHT);
        drawBackground(bgCanvas.getGraphicsContext2D());
    }

    public void show() {
        if (controller.hasSavedGame()) {
            showContinueOrNewChoice();
        } else {
            showStep(0);
        }
    }

    // Permette di continuare una missione salvata o iniziarne una nuova.
    private void showContinueOrNewChoice() {
        stopTypewriterEffect();

        VBox panel = new VBox(24);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(45));
        panel.setMaxWidth(700);

        panel.setStyle(
                "-fx-background-color: rgba(4, 12, 31, 0.93);" +
                        "-fx-border-color: " + CYAN + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(30,144,255,0.40), 24, 0.3, 0, 0);"
        );

        Label gameTitle = new Label("✦ SPACE MYSTERY ✦");
        gameTitle.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + CYAN + ";"
        );

        Label titleLabel = new Label("BENTORNATO A BORDO");
        titleLabel.setStyle(
                "-fx-font-size: 23px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #e0f2fe;"
        );

        Label divider = new Label("━━━━━━━━━━ ✦ ━━━━━━━━━━");
        divider.setStyle("-fx-text-fill: " + BLUE + ";");

        Label description = new Label(
                "È stata trovata una missione salvata.\n\n" +
                        "Vuoi riprendere l'indagine di Alex oppure " +
                        "iniziare una nuova missione?"
        );

        description.setWrapText(true);
        description.setAlignment(Pos.CENTER);
        description.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER
        );
        description.setMaxWidth(560);
        description.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-text-fill: #e5edf8;" +
                        "-fx-line-spacing: 5px;"
        );

        Button continueButton = createButton("CONTINUA MISSIONE", true);
        continueButton.setPrefWidth(245);

        continueButton.setOnAction(event -> {
            try {
                if (controller.loadGame()) {
                    new GameView(controller, stage).show();
                } else {
                    showLoadError(
                            "Non è stato possibile trovare " +
                                    "una partita salvata valida."
                    );
                }
            } catch (RuntimeException exception) {
                showLoadError(
                        "Non è stato possibile caricare la missione.\n\n" +
                                exception.getMessage()
                );
            }
        });

        Button newGameButton = createButton("NUOVA MISSIONE", false);
        newGameButton.setPrefWidth(245);

        newGameButton.setOnAction(event -> {
            // Il controller contiene già una nuova partita
            // inizializzata nel suo costruttore.
            // Non cancelliamo il salvataggio esistente.
            showStep(0);
        });

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(
                continueButton,
                newGameButton
        );

        Label warning = new Label(
                "Una nuova missione sostituirà il precedente " +
                        "salvataggio al prossimo salvataggio."
        );
        warning.setWrapText(true);
        warning.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER
        );
        warning.setMaxWidth(540);
        warning.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #94a3b8;"
        );

        panel.getChildren().addAll(
                gameTitle,
                titleLabel,
                divider,
                description,
                buttons,
                warning
        );

        showScene(panel);
    }

    // Mostra un errore senza chiudere la schermata iniziale.
    private void showLoadError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle("SpaceMystery");
        alert.setHeaderText("Errore nel caricamento");
        alert.setContentText(message);

        java.net.URL css = getClass().getResource("/css/style.css");

        if (css != null) {
            alert.getDialogPane()
                    .getStylesheets()
                    .add(css.toExternalForm());
        }

        alert.showAndWait();
    }

    private void showStep(int step) {
        currentStep = step;

        stopTypewriterEffect();

        VBox panel = new VBox(19);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(35, 45, 35, 45));
        panel.setMaxWidth(700);
        panel.setMaxHeight(690);

        panel.setStyle(
                "-fx-background-color: rgba(4, 12, 31, 0.93);" +
                        "-fx-border-color: " + CYAN + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(30,144,255,0.40), 24, 0.3, 0, 0);"
        );

        Label gameTitle = new Label("✦ SPACE MYSTERY ✦");
        gameTitle.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + CYAN + ";"
        );

        Label stepIndicator = new Label(
                "MISSIONE | " + (step + 1) + " / " + steps.length
        );
        stepIndicator.setStyle(
                "-fx-font-size: 13px; -fx-text-fill: #94a3b8;"
        );

        Label titleLabel = new Label(steps[step].title());
        titleLabel.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #e0f2fe;"
        );

        Label dividerTop = new Label("━━━━━━━━━━ ✦ ━━━━━━━━━━");
        dividerTop.setStyle("-fx-text-fill: " + BLUE + ";");

        Label dividerBottom = new Label("━━━━━━━━━━ ✦ ━━━━━━━━━━");
        dividerBottom.setStyle("-fx-text-fill: " + BLUE + ";");

        typewriterLabel = new Label("");
        typewriterLabel.setWrapText(true);
        typewriterLabel.setAlignment(Pos.CENTER);
        typewriterLabel.setTextAlignment(
                javafx.scene.text.TextAlignment.CENTER
        );
        typewriterLabel.setMaxWidth(590);
        typewriterLabel.setMinHeight(270);
        typewriterLabel.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-text-fill: #e5edf8;" +
                        "-fx-line-spacing: 5px;"
        );

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);

        if (step > 0) {
            Button back = createButton("INDIETRO", false);
            back.setOnAction(event -> showStep(step - 1));
            buttons.getChildren().add(back);
        }

        if (step < steps.length - 1) {
            Button next = createButton("AVANTI", false);
            next.setOnAction(event -> showStep(step + 1));
            buttons.getChildren().add(next);
        } else {
            Button start = createButton("INIZIA LA MISSIONE", true);

            start.setOnAction(event -> {
                stopTypewriterEffect();
                new GameView(controller, stage).show();
            });

            buttons.getChildren().add(start);
        }

        panel.getChildren().addAll(
                gameTitle,
                stepIndicator,
                titleLabel,
                dividerTop,
                typewriterLabel,
                dividerBottom,
                buttons
        );

        showScene(panel);
        startTypewriterEffect(steps[step].content());
    }

    // Riutilizza lo sfondo spaziale per tutte le schermate iniziali.
    private void showScene(VBox panel) {
        StackPane root = new StackPane(bgCanvas, panel);
        root.setStyle("-fx-background-color: " + DARK + ";");

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        java.net.URL css = getClass().getResource("/css/style.css");

        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("SpaceMystery - Benvenuto");
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text, boolean primary) {
        Button button = new Button(text);
        button.setPrefWidth(primary ? 245 : 160);

        String border = primary ? CYAN : "#64748b";

        String normal =
                "-fx-background-color: #0f172a;" +
                        "-fx-text-fill: #e0f2fe;" +
                        "-fx-border-color: " + border + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 11px 18px;";

        String hover =
                "-fx-background-color: " + CYAN + ";" +
                        "-fx-text-fill: #051020;" +
                        "-fx-border-color: " + CYAN + ";" +
                        "-fx-border-width: 2px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 11px 18px;";

        button.setStyle(normal);
        button.setOnMouseEntered(event -> button.setStyle(hover));
        button.setOnMouseExited(event -> button.setStyle(normal));

        return button;
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.web(DARK));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Stelle riproducibili, senza immagini esterne.
        for (int i = 0; i < 170; i++) {
            int x = (i * 173 + 37) % WIDTH;
            int y = (i * 97 + 23) % HEIGHT;
            double radius = i % 9 == 0 ? 3 : 1.5;

            gc.setFill(Color.web(
                    i % 4 == 0 ? "#7dd3fc" : "#e2e8f0",
                    0.35 + (i % 5) * 0.12
            ));

            gc.fillOval(x, y, radius, radius);
        }

        drawPlanet(gc, 1030, 115);
        drawStation(gc);
        drawAstronaut(gc, 1060, 535);
    }

    private void drawPlanet(GraphicsContext gc, int x, int y) {
        gc.setFill(Color.web("#38bdf8", 0.08));
        gc.fillOval(x - 28, y - 28, 205, 205);

        gc.setFill(Color.web("#1d4ed8"));
        gc.fillOval(x, y, 150, 150);

        gc.setFill(Color.web("#38bdf8", 0.38));
        gc.fillOval(x + 12, y + 15, 110, 115);

        gc.setStroke(Color.web("#93c5fd", 0.75));
        gc.setLineWidth(3);
        gc.strokeOval(x - 25, y + 55, 205, 43);
    }

    private void drawStation(GraphicsContext gc) {
        // Moduli esterni e finestre della stazione, in stile pixel art.
        gc.setFill(Color.web("#172554"));
        gc.fillRect(0, 605, WIDTH, 195);

        gc.setFill(Color.web("#334155"));
        gc.fillRect(0, 600, WIDTH, 20);

        for (int i = 0; i < 9; i++) {
            int x = i * 155 - 35;

            gc.setFill(Color.web("#1e293b"));
            gc.fillRect(x, 430 + (i % 3) * 35, 130, 180);

            gc.setStroke(Color.web("#64748b"));
            gc.setLineWidth(5);
            gc.strokeRect(x + 6, 440 + (i % 3) * 35, 115, 165);

            gc.setFill(Color.web("#0ea5e9", 0.5));
            gc.fillRect(x + 30, 475 + (i % 3) * 35, 65, 52);

            gc.setStroke(Color.web("#7dd3fc"));
            gc.setLineWidth(2);
            gc.strokeRect(x + 30, 475 + (i % 3) * 35, 65, 52);
        }

        gc.setFill(Color.web("#0f172a"));
        gc.fillRect(0, 700, WIDTH, 100);

        gc.setStroke(Color.web("#38bdf8", 0.45));
        gc.setLineWidth(3);
        gc.strokeLine(0, 700, WIDTH, 700);

        for (int x = 30; x < WIDTH; x += 90) {
            gc.setFill(Color.web("#38bdf8", 0.65));
            gc.fillRect(x, 730, 24, 5);
        }
    }

    private void drawAstronaut(GraphicsContext gc, int x, int y) {
        // Alex: casco, visiera, tuta e zaino in pixel art.
        gc.setFill(Color.web("#000000", 0.4));
        gc.fillOval(x - 10, y + 140, 95, 18);

        gc.setFill(Color.web("#cbd5e1"));
        gc.fillRect(x + 6, y + 72, 24, 64);
        gc.fillRect(x + 43, y + 72, 24, 64);

        gc.setFill(Color.web("#475569"));
        gc.fillRect(x + 2, y + 127, 32, 16);
        gc.fillRect(x + 40, y + 127, 32, 16);

        gc.setFill(Color.web("#64748b"));
        gc.fillRect(x + 58, y + 15, 25, 72);

        gc.setFill(Color.web("#e2e8f0"));
        gc.fillRect(x, y + 12, 67, 78);

        gc.setFill(Color.web("#94a3b8"));
        gc.fillRect(x - 15, y + 22, 17, 65);
        gc.fillRect(x + 66, y + 22, 17, 65);

        gc.setFill(Color.web("#38bdf8"));
        gc.fillRect(x + 19, y + 42, 30, 22);

        gc.setFill(Color.web("#e2e8f0"));
        gc.fillOval(x - 1, y - 45, 73, 70);

        gc.setFill(Color.web("#0c4a6e"));
        gc.fillOval(x + 8, y - 35, 55, 47);

        gc.setFill(Color.web("#7dd3fc", 0.55));
        gc.fillOval(x + 15, y - 29, 22, 15);

        gc.setStroke(Color.web("#38bdf8"));
        gc.setLineWidth(3);
        gc.strokeOval(x + 8, y - 35, 55, 47);
    }

    private void startTypewriterEffect(String text) {
        typewriterLabel.setText("");

        final int[] index = {0};

        typewriterTimeline = new Timeline(
                new KeyFrame(Duration.millis(30), event -> {
                    if (index[0] < text.length()) {
                        typewriterLabel.setText(
                                typewriterLabel.getText() +
                                        text.charAt(index[0])
                        );
                        index[0]++;
                    }
                })
        );

        typewriterTimeline.setCycleCount(text.length());
        typewriterTimeline.play();
    }

    private void stopTypewriterEffect() {
        if (typewriterTimeline != null) {
            typewriterTimeline.stop();
            typewriterTimeline = null;
        }
    }
}