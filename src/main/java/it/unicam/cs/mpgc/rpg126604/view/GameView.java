
package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import it.unicam.cs.mpgc.rpg126604.model.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameView {

    private final GameController controller;
    private final Stage stage;

    private Label locationLabel;
    private TextArea descriptionArea;
    private ListView<String> cluesList;
    private Label reputationLabel;

    public GameView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        root.setTop(buildTopPanel());
        root.setCenter(buildMainPanel());

        Scene scene = new Scene(root, 1100, 750);

        // Colleghiamo il foglio di stile di SpaceMystery.
        String css = getClass().getResource("/css/style.css")
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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        top.getChildren().addAll(
                gameTitle,
                locationLabel,
                spacer,
                reputationLabel
        );

        return top;
    }

    private HBox buildMainPanel() {
        HBox main = new HBox(15);
        main.setPadding(new Insets(15, 0, 0, 0));

        VBox explorationPanel = buildExplorationPanel();
        VBox notebookPanel = buildNotebookPanel();

        HBox.setHgrow(explorationPanel, Priority.ALWAYS);
        HBox.setHgrow(notebookPanel, Priority.ALWAYS);

        explorationPanel.setMinWidth(0);
        notebookPanel.setMinWidth(0);

        explorationPanel.setPrefWidth(520);
        notebookPanel.setPrefWidth(480);

        main.getChildren().addAll(explorationPanel, notebookPanel);

        return main;
    }

    private VBox buildExplorationPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("game-panel");

        Label roomsTitle = new Label("Esplorazione");
        roomsTitle.getStyleClass().add("section-title");

        // Nuovo pulsante per aprire la mappa della stazione.
        Button mapButton = new Button("Mappa della stazione");
        mapButton.setMaxWidth(Double.MAX_VALUE);
        mapButton.setOnAction(event -> {
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
            });

            roomsBox.getChildren().add(button);
        }

        Label descriptionTitle = new Label("Descrizione della stanza");
        descriptionTitle.getStyleClass().add("section-title");

        descriptionArea = new TextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapText(true);
        descriptionArea.setPrefRowCount(10);

        VBox.setVgrow(descriptionArea, Priority.ALWAYS);

        Button collectButton = new Button("Raccogli indizi");
        collectButton.setMaxWidth(Double.MAX_VALUE);
        collectButton.setOnAction(event -> collectClues());

        panel.getChildren().addAll(
                roomsTitle,
                mapButton,
                roomsBox,
                descriptionTitle,
                descriptionArea,
                collectButton
        );

        return panel;
    }

    private VBox buildNotebookPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("game-panel");

        Label notebookTitle = new Label("Taccuino investigativo");
        notebookTitle.getStyleClass().add("section-title");

        cluesList = new ListView<>();

        // Ogni indizio viene mostrato su più righe, se necessario.
        cluesList.setCellFactory(listView -> new ListCell<>() {

            private final Label clueLabel = new Label();

            {
                clueLabel.setWrapText(true);
                clueLabel.setMaxWidth(Double.MAX_VALUE);

                // La larghezza del testo segue quella del taccuino.
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

        Button reportButton = new Button("Crea rapporto");
        reportButton.setMaxWidth(Double.MAX_VALUE);
        reportButton.setOnAction(event -> writeReport());

        panel.getChildren().addAll(
                notebookTitle,
                cluesList,
                reportButton
        );

        return panel;
    }

    private void collectClues() {
        Location current = controller.getCurrentLocation();

        for (Clue clue : current.getClues()) {
            if (!clue.isDiscovered()) {
                controller.collectClue(clue);
            }
        }

        updateView();
    }

    private void writeReport() {
        if (controller.getAstronaut().getNotebook().isEmpty()) {
            showAlert(
                    "Devi raccogliere almeno un indizio prima di creare un rapporto."
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

                showAlert(
                        "Rapporto completato! Punteggio: "
                                + controller.getAstronaut().getReputation()
                );
            }
        });
    }

    private void updateView() {
        Location current = controller.getCurrentLocation();

        locationLabel.setText("Posizione: " + current.getName());
        descriptionArea.setText(current.getDescription());

        reputationLabel.setText(
                "Reputazione: "
                        + controller.getAstronaut().getReputation()
        );

        cluesList.getItems().clear();

        for (Clue clue : controller.getAstronaut().getNotebook()) {
            cluesList.getItems().add(clue.getDescription());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
