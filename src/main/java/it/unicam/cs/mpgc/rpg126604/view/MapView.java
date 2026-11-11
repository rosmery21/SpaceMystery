
package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import it.unicam.cs.mpgc.rpg126604.model.Location;
import it.unicam.cs.mpgc.rpg126604.model.PlayerStats;

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

import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

// disegna la mappa della stazione e gestisce i comandi di Alex
public class MapView {

    private static final int COLUMNS = 3;

    private final GameController controller;
    private final Stage stage;
    private final List<Location> locations;

    // associa ogni tasto all'azione da eseguire
    private final Map<KeyCode, MovementCommand> movementCommands =
            new EnumMap<>(KeyCode.class);

    private GridPane mapGrid;
    private Label positionLabel;
    private Label statsLabel;
    private Label questsLabel;
    private Label selectedRoomDescription;

    private int selectedIndex;

    // ogni comando incapsula un'azione sulla mappa
    @FunctionalInterface
    private interface MovementCommand {
        void execute();
    }

    public MapView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        this.locations = new ArrayList<>(controller.getAllLocations());
        this.selectedIndex = 0;

        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getId().equals(
                    controller.getCurrentLocation().getId())) {
                selectedIndex = i;
                break;
            }
        }

        setupMovementCommands();
    }

    // registra i comandi per la navigazione nella stazione spaziale
    private void setupMovementCommands() {
        MovementCommand up = () -> moveSelection(-COLUMNS);
        MovementCommand down = () -> moveSelection(COLUMNS);
        MovementCommand left = () -> moveSelection(-1);
        MovementCommand right = () -> moveSelection(1);

        movementCommands.put(KeyCode.W, up);
        movementCommands.put(KeyCode.UP, up);

        movementCommands.put(KeyCode.S, down);
        movementCommands.put(KeyCode.DOWN, down);

        movementCommands.put(KeyCode.A, left);
        movementCommands.put(KeyCode.LEFT, left);

        movementCommands.put(KeyCode.D, right);
        movementCommands.put(KeyCode.RIGHT, right);

        // conferma la stanza selezionata oppure torna al gioco
        movementCommands.put(KeyCode.ENTER, this::enterSelectedLocation);
        movementCommands.put(KeyCode.ESCAPE, this::openGame);
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

        VBox top = new VBox(
                10, title, instructions, positionLabel, statsLabel, questsLabel
        );
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

        // applica il tema grafico di SpaceMystery
        URL cssUrl = getClass().getResource("/css/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        // esegue il comando associato al tasto premuto
        scene.setOnKeyPressed(event -> {
            MovementCommand command = movementCommands.get(event.getCode());

            if (command != null) {
                command.execute();
                event.consume();
            }
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

        statsLabel.setText(
                "Alex — Livello " + stats.getLevel()
                        + " | XP " + stats.getExperience()
                        + "/" + (stats.getLevel() * 100)
                        + " | INT " + stats.getIntelligence()
                        + " | CAR " + stats.getCharisma()
                        + " | FUR " + stats.getStealth()
        );

        int completed = controller.getCompletedQuests().size();
        int active = controller.getActiveQuests().size();

        questsLabel.setText(
                "Missioni completate: " + completed
                        + " | Missioni attive: " + active
        );

        if (locations.isEmpty()) {
            positionLabel.setText("Nessuna stanza disponibile.");
            selectedRoomDescription.setText("");
            return;
        }

        Location current = controller.getCurrentLocation();
        Location selected = locations.get(selectedIndex);

        positionLabel.setText(
                "Posizione attuale: " + current.getName()
                        + " | Stanza selezionata: " + selected.getName()
        );

        selectedRoomDescription.setText(
                "Stanza selezionata: " + selected.getDescription()
        );

        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);

            Button roomButton = new Button(location.getName());
            roomButton.setPrefSize(200, 110);
            roomButton.setWrapText(true);

            if (i == selectedIndex) {
                roomButton.setStyle(
                        "-fx-border-color: #7dd3fc;"
                                + "-fx-border-width: 3;"
                                + "-fx-border-radius: 6;"
                );
            }

            if (location.getId().equals(current.getId())) {
                roomButton.setText(
                        location.getName() + "\n(Alex è qui)"
                );
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
