
package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import it.unicam.cs.mpgc.rpg126604.model.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameView {

    private final GameController controller;
    private final Stage stage;

    private Label locationLabel;
    private TextArea descriptionArea;
    private ListView<String> cluesList;
    private Label reputationLabel;
    private Label cluesCountLabel;
    private Label messageLabel;
    private Timeline messageTimeline;

    public GameView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        root.setTop(buildTopPanel());
        root.setCenter(buildMainPanel());
        root.setBottom(buildBottomPanel());

        Scene scene = new Scene(root, 1100, 750);

        String css = getClass()
                .getResource("/css/style.css")
                .toExternalForm();

        scene.getStylesheets().add(css);

        stage.setTitle("SpaceMystery - Gioco di Ruolo");
        stage.setScene(scene);
        stage.show();

        updateView();
    }

    private HBox buildTopPanel() {
        HBox top = new HBox(20);
        top.setAlignment(Pos.CENTER_LEFT);
        top.getStyleClass().add("top-panel");

        Label gameTitle = new Label("SPACE MYSTERY");
        gameTitle.getStyleClass().add("section-title");

        locationLabel = new Label("Posizione: ");
        reputationLabel = new Label("Reputazione: 0");
        cluesCountLabel = new Label("Indizi: 0");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        top.getChildren().addAll(
                gameTitle,
                locationLabel,
                spacer,
                cluesCountLabel,
                reputationLabel
        );

        return top;
    }

    private HBox buildMainPanel() {
        HBox main = new HBox(15);
        main.setPadding(new Insets(15, 0, 15, 0));

        VBox explorationPanel = buildExplorationPanel();
        VBox notebookPanel = buildNotebookPanel();

        HBox.setHgrow(explorationPanel, Priority.ALWAYS);
        HBox.setHgrow(notebookPanel, Priority.ALWAYS);

        explorationPanel.setMinWidth(0);
        notebookPanel.setMinWidth(0);

        explorationPanel.setPrefWidth(520);
        notebookPanel.setPrefWidth(480);

        main.getChildren().addAll(
                explorationPanel,
                notebookPanel
        );

        return main;
    }

    private VBox buildExplorationPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("game-panel");

        Label roomsTitle = new Label("ESPLORAZIONE");
        roomsTitle.getStyleClass().add("section-title");

        Label roomsHint = new Label(
                "Esplora gli ambienti della stazione spaziale."
        );
        roomsHint.setWrapText(true);

        Button mapButton = new Button("MAPPA DELLA STAZIONE");
        mapButton.setMaxWidth(Double.MAX_VALUE);

        mapButton.setOnAction(event -> {
            if (messageTimeline != null) {
                messageTimeline.stop();
            }

            MapView mapView = new MapView(controller, stage);
            mapView.show();
        });

        VBox roomsBox = new VBox(10);

        for (Location location : controller.getAllLocations()) {
            Button button = new Button(location.getName());
            button.setMaxWidth(Double.MAX_VALUE);

            button.setOnAction(event -> {
                controller.moveToLocation(location.getId());
                updateView();

                showMessage(
                        "Alex ha raggiunto: " + location.getName()
                );
            });

            roomsBox.getChildren().add(button);
        }

        Separator separator = new Separator();

        Label descriptionTitle = new Label(
                "DESCRIZIONE DELLA STANZA"
        );
        descriptionTitle.getStyleClass().add("section-title");

        descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(10);

        VBox.setVgrow(descriptionArea, Priority.ALWAYS);

        Button collectButton = new Button("RACCOGLI INDIZI");
        collectButton.setMaxWidth(Double.MAX_VALUE);
        collectButton.setOnAction(event -> collectClues());

        panel.getChildren().addAll(
                roomsTitle,
                roomsHint,
                mapButton,
                roomsBox,
                separator,
                descriptionTitle,
                descriptionArea,
                collectButton
        );

        return panel;
    }

    private VBox buildNotebookPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("game-panel");

        Label notebookTitle = new Label("TACCUINO INVESTIGATIVO");
        notebookTitle.getStyleClass().add("section-title");

        Label notebookHint = new Label(
                "Indizi raccolti durante la missione:"
        );
        notebookHint.setWrapText(true);

        cluesList = new ListView<>();

        cluesList.setCellFactory(listView -> new ListCell<>() {

            private final Label clueLabel = new Label();

            {
                clueLabel.setWrapText(true);
                clueLabel.setMaxWidth(Double.MAX_VALUE);

                clueLabel.prefWidthProperty().bind(
                        listView.widthProperty().subtract(45)
                );
            }

            @Override
            protected void updateItem(String clue, boolean empty) {
                super.updateItem(clue, empty);

                if (empty || clue == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    clueLabel.setText(clue);
                    setText(null);
                    setGraphic(clueLabel);
                }
            }
        });

        VBox.setVgrow(cluesList, Priority.ALWAYS);

        Separator separator = new Separator();

        Label reportTitle = new Label("RAPPORTI");
        reportTitle.getStyleClass().add("section-title");

        Label reportHint = new Label(
                "Pubblica le tue scoperte per aumentare "
                        + "la reputazione."
        );
        reportHint.setWrapText(true);

        Button reportButton = new Button("CREA RAPPORTO");
        reportButton.setMaxWidth(Double.MAX_VALUE);
        reportButton.setOnAction(event -> writeReport());

        panel.getChildren().addAll(
                notebookTitle,
                notebookHint,
                cluesList,
                separator,
                reportTitle,
                reportHint,
                reportButton
        );

        return panel;
    }

    private HBox buildBottomPanel() {
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(12));

        bottom.getStyleClass().add("top-panel");

        messageLabel = new Label(
                "Missione avviata. Esplora la stazione spaziale!"
        );

        bottom.getChildren().add(messageLabel);

        return bottom;
    }

    private void showMessage(String message) {
        if (messageTimeline != null) {
            messageTimeline.stop();
        }

        messageLabel.setText("> " + message);
        messageLabel.setStyle("-fx-text-fill: #7dd3fc;");

        messageTimeline = new Timeline(
                new KeyFrame(Duration.seconds(4), event -> {
                    messageLabel.setText(
                            "> In attesa della prossima azione..."
                    );

                    messageLabel.setStyle(
                            "-fx-text-fill: #e5edf8;"
                    );
                })
        );

        messageTimeline.play();
    }

    private void collectClues() {
        Location current = controller.getCurrentLocation();

        int collected = 0;

        for (Clue clue : current.getClues()) {
            if (!clue.isDiscovered()) {
                controller.collectClue(clue);
                collected++;
            }
        }

        updateView();

        if (collected > 0) {
            showMessage(
                    "Hai raccolto " + collected
                            + " nuovi indizi in "
                            + current.getName() + "!"
            );
        } else {
            showMessage(
                    "Nessun nuovo indizio disponibile in questa stanza."
            );
        }
    }

    private void writeReport() {
        if (controller.getAstronaut().getNotebook().isEmpty()) {
            showAlert(
                    "Devi raccogliere almeno un indizio "
                            + "prima di creare un rapporto."
            );

            showMessage(
                    "Impossibile creare un rapporto: "
                            + "nessun indizio raccolto."
            );

            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo rapporto");
        dialog.setHeaderText("Crea un rapporto investigativo");
        dialog.setContentText("Titolo:");

        dialog.showAndWait().ifPresent(title -> {
            if (!title.isBlank()) {
                Report report = controller.createReport(title);

                for (Clue clue : controller.getAstronaut().getNotebook()) {
                    report.addClue(clue);
                }

                controller.publishReport(report);
                updateView();

                showMessage(
                        "Rapporto pubblicato! Reputazione: "
                                + controller.getAstronaut()
                                .getReputation()
                );

                showAlert(
                        "Rapporto completato! Punteggio: "
                                + controller.getAstronaut()
                                .getReputation()
                );
            }
        });
    }

    private void updateView() {
        Location current = controller.getCurrentLocation();

        locationLabel.setText(
                "Posizione: " + current.getName()
        );

        descriptionArea.setText(current.getDescription());

        reputationLabel.setText(
                "Reputazione: "
                        + controller.getAstronaut().getReputation()
        );

        cluesCountLabel.setText(
                "Indizi: "
                        + controller.getAstronaut()
                        .getNotebook().size()
        );

        cluesList.getItems().clear();

        for (Clue clue : controller.getAstronaut().getNotebook()) {
            cluesList.getItems().add(
                    clue.getDescription()
            );
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setContentText(message);
        alert.showAndWait();
    }
}
