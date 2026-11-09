package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import it.unicam.cs.mpgc.rpg126604.model.Location;
import it.unicam.cs.mpgc.rpg126604.model.PlayerStats;
import it.unicam.cs.mpgc.rpg126604.model.Quest;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MapView {

    private static final int COLUMNS = 3;

    private final GameController controller;
    private final Stage stage;
    private final List<Location> locations;

    private GridPane mapGrid;
    private Label positionLabel;
    private Label statsLabel;
    private Label questsLabel;
    private Label selectedRoomDescription;

    private int selectedIndex;

    public MapView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        this.locations = new ArrayList<>(controller.getAllLocations());
        this.selectedIndex = 0;

        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getId().equals(controller.getCurrentLocation().getId())) {
                selectedIndex = i;
                break;
            }
        }
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("MAPPA DELLA STAZIONE");
        title.getStyleClass().add("section-title");

        Label instructions = new Label(
                "Usa WASD o le frecce per scegliere una stanza. "
                        + "Premi INVIO per raggiungerla. ESC per tornare al gioco."
        );
        instructions.setWrapText(true);

        positionLabel = new Label();
        positionLabel.setWrapText(true);
        statsLabel = new Label();
        statsLabel.setWrapText(true);
        questsLabel = new Label();
        questsLabel.setWrapText(true);

        VBox top = new VBox(10, title, instructions, positionLabel, statsLabel, questsLabel);
        top.setAlignment(Pos.CENTER);

        mapGrid = new GridPane();
        mapGrid.setHgap(15);
        mapGrid.setVgap(15);
        mapGrid.setAlignment(Pos.CENTER);

        selectedRoomDescription = new Label();
        selectedRoomDescription.setWrapText(true);
        selectedRoomDescription.setMaxWidth(700);

        Button backButton = new Button("Torna al gioco");
        backButton.setOnAction(event -> openGame());

        VBox bottom = new VBox(15, selectedRoomDescription, backButton);
        bottom.setAlignment(Pos.CENTER);
        BorderPane.setMargin(bottom, new Insets(15, 0, 0, 0));

        root.setTop(top);
        root.setCenter(mapGrid);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 900, 650);
        scene.getStylesheets().add(
                getClass().getResource("/css/style.css").toExternalForm()
        );

        scene.setOnKeyPressed(event -> {
            KeyCode key = event.getCode();
            switch (key) {
                case W, UP -> moveSelection(-COLUMNS);
                case S, DOWN -> moveSelection(COLUMNS);
                case A, LEFT -> moveSelection(-1);
                case D, RIGHT -> moveSelection(1);
                case ENTER -> enterSelectedLocation();
                case ESCAPE -> openGame();
                default -> {
                    return;
                }
            }
            event.consume();
        });

        stage.setTitle("SpaceMystery - Mappa della stazione");
        stage.setScene(scene);
        updateMap();
        stage.show();
    }

    private void moveSelection(int offset) {
        int nextIndex = selectedIndex + offset;
        if (nextIndex >= 0 && nextIndex < locations.size()) {
            selectedIndex = nextIndex;
            updateMap();
        }
    }

    private void enterSelectedLocation() {
        if (locations.isEmpty()) {
            return;
        }
        Location selectedLocation = locations.get(selectedIndex);
        controller.moveToLocation(selectedLocation.getId());
        openGame();
    }

    private void updateMap() {
        mapGrid.getChildren().clear();

        PlayerStats stats = controller.getPlayerStats();
        statsLabel.setText("Alex — Livello " + stats.getLevel()
                + " | XP " + stats.getExperience() + "/" + (stats.getLevel() * 100)
                + " | INT " + stats.getIntelligence()
                + " | CAR " + stats.getCharisma()
                + " | FUR " + stats.getStealth());

        int completed = controller.getCompletedQuests().size();
        int active = controller.getActiveQuests().size();
        questsLabel.setText("Missioni completate: " + completed
                + " | Missioni attive: " + active);

        if (locations.isEmpty()) {
            positionLabel.setText("Nessuna stanza disponibile.");
            selectedRoomDescription.setText("");
            return;
        }

        Location current = controller.getCurrentLocation();
        Location selected = locations.get(selectedIndex);
        positionLabel.setText("Posizione attuale: " + current.getName()
                + " | Stanza selezionata: " + selected.getName());
        selectedRoomDescription.setText("Stanza selezionata: " + selected.getDescription());

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            Button roomButton = new Button(location.getName());
            roomButton.setPrefSize(200, 110);
            roomButton.setWrapText(true);

            if (i == selectedIndex) {
                roomButton.setStyle("-fx-border-color: #7dd3fc;"
                        + "-fx-border-width: 3;"
                        + "-fx-border-radius: 6;");
            }

            if (location.getId().equals(current.getId())) {
                roomButton.setText(location.getName() + "\n(Alex è qui)");
            }

            final int index = i;
            roomButton.setOnAction(event -> {
                selectedIndex = index;
                enterSelectedLocation();
            });

            int row = i / COLUMNS;
            int column = i % COLUMNS;
            mapGrid.add(roomButton, column, row);
        }
    }

    private void openGame() {
        GameView gameView = new GameView(controller, stage);
        gameView.show();
    }
}
