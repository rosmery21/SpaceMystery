package it.unicam.cs.mpgc.rpg126604.view;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import it.unicam.cs.mpgc.rpg126604.model.*;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;

// costruisce e aggiorna l'interfaccia principale del gioco
public class GameView {

    private final GameController controller;
    private final Stage stage;

    // percorso del tema grafico usato anche nelle finestre di dialogo
    private final String styleSheetPath;

    private Label locationLabel;
    private TextArea descriptionArea;
    private ListView<String> cluesList;
    private ListView<String> questsList;
    private Label reputationLabel;
    private Label cluesCountLabel;
    private Label levelLabel;
    private Label experienceLabel;
    private Label daysLabel;
    private Label objectiveLabel;
    private Button collectButton;
    private Label experienceValueLabel;
    private Label reputationValueLabel;
    private Canvas suspicionBar;
    private Canvas experienceBar;
    private Canvas reputationBar;
    private VBox messageLog;
    private boolean gameFinished;

    public GameView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;

        // carica una sola volta il CSS spaziale di SpaceMystery
        URL cssUrl = getClass().getResource("/css/style.css");
        this.styleSheetPath = cssUrl != null ? cssUrl.toExternalForm() : null;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        root.setTop(buildTopPanel());
        root.setCenter(buildMainPanel());
        root.setBottom(buildBottomPanel());

        Scene scene = new Scene(root, 1100, 750);

        // applica il tema alla finestra principale
        if (styleSheetPath != null) {
            scene.getStylesheets().add(styleSheetPath);
        }

        stage.setTitle("SpaceMystery - Gioco di Ruolo");
        stage.setScene(scene);
        stage.show();

        updateView();
        addMessage("Missione SpaceMystery avviata. Hai "
                + controller.getDaysRemaining() + " giorni per completarla.");
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
        levelLabel = new Label("Livello: 1");
        experienceLabel = new Label("XP: 0/100");
        daysLabel = new Label("Giorni: 10");
        suspicionBar = createBar(145, 12);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        top.getChildren().addAll(
                gameTitle,
                locationLabel,
                spacer,
                levelLabel,
                experienceLabel,
                cluesCountLabel,
                reputationLabel,
                daysLabel,
                labeledBar("SOSPETTO", suspicionBar)
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

                addMessage("Alex ha raggiunto: " + location.getName());
                checkGameState();
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

        collectButton = new Button("RACCOGLI INDIZI");
        collectButton.setManaged(false);
        collectButton.setVisible(false);
        collectButton.setMaxWidth(Double.MAX_VALUE);
        collectButton.setOnAction(event -> collectClues());

        Button dialogueButton = new Button("PARLA CON L'EQUIPAGGIO");
        dialogueButton.setMaxWidth(Double.MAX_VALUE);
        dialogueButton.setOnAction(event -> showNPCDialog());

        panel.getChildren().addAll(
                roomsTitle,
                roomsHint,
                mapButton,
                roomsBox,
                separator,
                descriptionTitle,
                descriptionArea,
                collectButton,
                dialogueButton
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

        Label questsTitle = new Label("MISSIONI");
        questsTitle.getStyleClass().add("section-title");

        questsList = new ListView<>();
        questsList.setPrefHeight(175);
        questsList.setMinHeight(110);

        questsList.setCellFactory(listView -> new ListCell<>() {

            private final Label questLabel = new Label();

            {
                questLabel.setWrapText(true);
                questLabel.prefWidthProperty().bind(
                        listView.widthProperty().subtract(45)
                );
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    questLabel.setText(item);
                    setGraphic(questLabel);
                }
            }
        });

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

        // il pulsante si disabilita automaticamente quando il taccuino è vuoto
        reportButton.disableProperty().bind(
                Bindings.isEmpty(cluesList.getItems())
        );

        experienceBar = createBar(220, 12);
        reputationBar = createBar(220, 12);

        experienceValueLabel = new Label("0 / 100 XP");
        reputationValueLabel = new Label("0 / 100");

        Label objectiveTitle = new Label("OBIETTIVO ATTUALE");
        objectiveTitle.getStyleClass().add("section-title");

        objectiveLabel = new Label();
        objectiveLabel.setWrapText(true);
        objectiveLabel.setMaxWidth(Double.MAX_VALUE);
        objectiveLabel.setStyle(
                "-fx-text-fill: #bae6fd;" +
                        "-fx-background-color: #0b1220;" +
                        "-fx-border-color: #38bdf8;" +
                        "-fx-border-width: 2px;" +
                        "-fx-padding: 12px;" +
                        "-fx-font-size: 13px;"
        );

        panel.getChildren().addAll(
                objectiveTitle,
                objectiveLabel,
                labeledBar("ESPERIENZA", experienceBar),
                experienceValueLabel,
                labeledBar("REPUTAZIONE", reputationBar),
                reputationValueLabel,
                notebookTitle,
                notebookHint,
                cluesList,
                questsTitle,
                questsList,
                separator,
                reportTitle,
                reportHint,
                reportButton
        );

        return panel;
    }

    private VBox buildBottomPanel() {
        VBox bottom = new VBox(8);
        bottom.setPadding(new Insets(12));
        bottom.getStyleClass().add("top-panel");

        Label title = new Label("REGISTRO MISSIONE");
        title.getStyleClass().add("section-title");

        // evita lo sfondo bianco interno di uno ScrollPane
        messageLog = new VBox(5);
        messageLog.setPadding(new Insets(10));
        messageLog.setMinHeight(100);
        messageLog.setPrefHeight(115);
        messageLog.setStyle(
                "-fx-background-color: #0b1220;" +
                        "-fx-border-color: #38bdf8;" +
                        "-fx-border-width: 1px;"
        );

        bottom.getChildren().addAll(title, messageLog);
        return bottom;
    }

    private void addMessage(String message) {
        Label entry = new Label("> " + message);
        entry.setWrapText(true);
        entry.setMaxWidth(Double.MAX_VALUE);
        entry.setStyle(
                "-fx-text-fill: #7dd3fc;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-color: transparent;"
        );

        messageLog.getChildren().add(0, entry);

        while (messageLog.getChildren().size() > 5) {
            messageLog.getChildren().remove(
                    messageLog.getChildren().size() - 1
            );
        }
    }

    private Canvas createBar(double width, double height) {
        return new Canvas(width, height);
    }

    private VBox labeledBar(String title, Canvas bar) {
        Label label = new Label(title);
        return new VBox(4, label, bar);
    }

    private void drawBar(Canvas bar, double value, double max, Color color) {
        GraphicsContext gc = bar.getGraphicsContext2D();

        double width = bar.getWidth();
        double height = bar.getHeight();

        double ratio = max <= 0
                ? 0
                : Math.max(0, Math.min(1, value / max));

        gc.setFill(Color.web("#142035"));
        gc.fillRect(0, 0, width, height);

        gc.setFill(color);
        gc.fillRect(0, 0, width * ratio, height);

        gc.setStroke(Color.web("#7dd3fc"));
        gc.strokeRect(0.5, 0.5, width - 1, height - 1);
    }

    private void checkGameState() {
        if (gameFinished) {
            return;
        }

        if (controller.isGameOver()) {
            gameFinished = true;

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MISSIONE FALLITA");
            alert.setHeaderText("SpaceMystery - Missione fallita");
            alert.setContentText(
                    controller.getGameOverReason()
                            + "\nIndizi raccolti: "
                            + controller.getAstronaut().getNotebook().size()
                            + "\nReputazione: "
                            + controller.getAstronaut().getReputation()
                            + "\nLivello: "
                            + controller.getPlayerStats().getLevel()
            );

            applyStylesheet(alert.getDialogPane());
            alert.showAndWait();

        } else if (controller.isVictory()) {
            gameFinished = true;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MISSIONE COMPIUTA!");
            alert.setHeaderText("SpaceMystery - Missione completata");
            alert.setContentText(
                    "Alex ha risolto il mistero della stazione spaziale!"
                            + "\nReputazione: "
                            + controller.getAstronaut().getReputation()
                            + "\nLivello: "
                            + controller.getPlayerStats().getLevel()
                            + "\nIndizi raccolti: "
                            + controller.getAstronaut().getNotebook().size()
            );

            applyStylesheet(alert.getDialogPane());
            alert.showAndWait();

        } else if (controller.getSuspicionLevel() >= 70) {
            addMessage("ATTENZIONE: livello di sospetto molto alto!");

        } else if (controller.getDaysRemaining() <= 3) {
            addMessage(
                    "URGENTE: restano solo "
                            + controller.getDaysRemaining()
                            + " giorni!"
            );
        }
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

        if (collected > 0) {
            controller.increaseSuspicion(10);
        }

        updateView();

        if (collected > 0) {
            addMessage(
                    "Hai raccolto " + collected
                            + " nuovi indizi in "
                            + current.getName() + "!"
            );
        } else {
            addMessage(
                    "Nessun nuovo indizio disponibile in questa stanza."
            );
        }

        checkGameState();
    }

    /**
     * Permette di scegliere un membro dell'equipaggio con cui parlare.
     */
    private void showNPCDialog() {
        java.util.List<NPC> crew =
                new java.util.ArrayList<>(controller.getAllNPCs());

        if (crew.isEmpty()) {
            addMessage("Nessun membro dell'equipaggio disponibile.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                crew.get(0).getName(),
                crew.stream().map(NPC::getName).toList()
        );

        dialog.setTitle("Equipaggio della stazione");
        dialog.setHeaderText("Con chi vuoi parlare?");
        dialog.setContentText("Membro dell'equipaggio:");

        // applica il CSS anche alla scelta dell'equipaggio
        applyStylesheet(dialog.getDialogPane());

        dialog.showAndWait().ifPresent(selectedName -> {
            for (NPC npc : crew) {
                if (npc.getName().equals(selectedName)) {
                    showDialogueChoices(npc);
                    return;
                }
            }
        });
    }

    /**
     * Visualizza le risposte disponibili e il requisito di carisma.
     */
    private void showDialogueChoices(NPC npc) {
        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Dialogo con " + npc.getName());
        dialog.setHeaderText(npc.getName() + " — " + npc.getRole());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        VBox content = new VBox(12);
        content.setPadding(new Insets(10));

        Label hint = new Label(
                "Carisma di Alex: "
                        + controller.getPlayerStats().getCharisma()
        );
        content.getChildren().add(hint);

        Label responseLabel = new Label(
                "Seleziona una domanda per leggere la risposta."
        );
        responseLabel.setWrapText(true);
        responseLabel.setMaxWidth(Double.MAX_VALUE);
        responseLabel.setMinHeight(65);

        java.util.List<DialogueChoice> choices =
                controller.getDialogueChoices(npc.getId());

        if (choices.isEmpty()) {
            Label noChoices = new Label(
                    "Nessuna scelta di dialogo disponibile."
            );
            noChoices.setWrapText(true);
            content.getChildren().add(noChoices);
        }

        for (DialogueChoice choice : choices) {
            boolean allowed =
                    controller.canSelectDialogueChoice(choice);

            String label = choice.getText()
                    + " (CAR " + choice.getRequiredCharisma() + ")"
                    + (allowed
                    ? ""
                    : " — Carisma insufficiente");

            Button option = new Button(label);
            option.setMaxWidth(Double.MAX_VALUE);
            option.setWrapText(true);
            option.setDisable(!allowed);

            option.setOnAction(event -> {
                try {
                    String response =
                            controller.selectDialogueChoice(
                                    npc.getId(),
                                    choice
                            );

                    responseLabel.setText(
                            npc.getName() + ":\n"
                                    + (response == null || response.isBlank()
                                    ? "Nessuna risposta disponibile."
                                    : response)
                    );

                    controller.decreaseSuspicion(5);
                    updateView();
                    checkGameState();

                    hint.setText(
                            "Carisma di Alex: "
                                    + controller.getPlayerStats().getCharisma()
                    );

                    addMessage(
                            "Dialogo completato con "
                                    + npc.getName() + "."
                    );

                } catch (IllegalArgumentException
                         | IllegalStateException exception) {
                    responseLabel.setText(exception.getMessage());
                }
            });

            content.getChildren().add(option);
        }

        Separator separator = new Separator();
        content.getChildren().addAll(separator, responseLabel);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportWidth(480);
        scroll.setPrefViewportHeight(360);

        dialog.getDialogPane().setContent(scroll);

        // applica il tema spaziale anche ai dialoghi dell'equipaggio
        applyStylesheet(dialog.getDialogPane());

        dialog.showAndWait();
    }

    private void writeReport() {
        if (controller.getAstronaut().getNotebook().isEmpty()) {
            showAlert(
                    "Devi raccogliere almeno un indizio "
                            + "prima di creare un rapporto."
            );

            addMessage(
                    "Impossibile creare un rapporto: "
                            + "nessun indizio raccolto."
            );

            return;
        }

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Nuovo rapporto");
        dialog.setHeaderText("Crea un rapporto investigativo");
        dialog.setContentText("Titolo:");

        // applica il CSS alla finestra di creazione del rapporto
        applyStylesheet(dialog.getDialogPane());

        dialog.showAndWait().ifPresent(title -> {
            if (!title.isBlank()) {
                Report report = controller.createReport(title);

                for (Clue clue :
                        controller.getAstronaut().getNotebook()) {
                    report.addClue(clue);
                }

                controller.publishReport(report);

                updateView();
                checkGameState();

                addMessage(
                        "Rapporto pubblicato! Reputazione: "
                                + controller.getAstronaut().getReputation()
                );

                showAlert(
                        "Rapporto completato! Punteggio: "
                                + controller.getAstronaut().getReputation()
                );
            }
        });
    }

    private void updateView() {
        Location current = controller.getCurrentLocation();

        PlayerStats stats = controller.getPlayerStats();

        levelLabel.setText("Livello: " + stats.getLevel());

        experienceLabel.setText(
                "XP: " + stats.getExperience()
                        + "/" + (stats.getLevel() * 100)
        );

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
                        + controller.getAstronaut().getNotebook().size()
        );

        daysLabel.setText(
                "Giorni: " + controller.getDaysRemaining()
        );

        daysLabel.setStyle(
                controller.getDaysRemaining() <= 3
                        ? "-fx-text-fill: #ff5555;"
                        : controller.getDaysRemaining() <= 5
                        ? "-fx-text-fill: #ffbb55;"
                        : ""
        );

        drawBar(
                suspicionBar,
                controller.getSuspicionLevel(),
                controller.getMaxSuspicion(),
                Color.web("#ef4444")
        );

        drawBar(
                experienceBar,
                stats.getExperience(),
                stats.getLevel() * 100,
                Color.web("#60a5fa")
        );

        drawBar(
                reputationBar,
                controller.getAstronaut().getReputation(),
                100,
                Color.web("#fbbf24")
        );

        experienceValueLabel.setText(
                stats.getExperience()
                        + " / "
                        + (stats.getLevel() * 100)
                        + " XP"
        );

        reputationValueLabel.setText(
                controller.getAstronaut().getReputation()
                        + " / 100"
        );

        objectiveLabel.setText(controller.getCurrentObjective());

        boolean hasClues = current.getClues().stream()
                .anyMatch(clue -> !clue.isDiscovered());

        collectButton.setVisible(hasClues);
        collectButton.setManaged(hasClues);

        if (hasClues) {
            collectButton.setText(
                    "RACCOGLI INDIZI - "
                            + current.getName().toUpperCase()
            );
        }

        updateQuests();

        cluesList.getItems().clear();

        for (Clue clue :
                controller.getAstronaut().getNotebook()) {
            cluesList.getItems().add(
                    clue.getDescription()
            );
        }
    }

    private void updateQuests() {
        questsList.getItems().clear();

        for (Quest quest : controller.getActiveQuests()) {
            StringBuilder details = new StringBuilder(
                    quest.getTitle()
                            + " ("
                            + quest.getCompletedObjectivesCount()
                            + "/"
                            + quest.getTotalObjectivesCount()
                            + ")"
            );

            for (int i = 0; i < quest.getObjectives().size(); i++) {
                details.append("\n")
                        .append(
                                quest.isObjectiveCompleted(i)
                                        ? "✓ "
                                        : "○ "
                        )
                        .append(quest.getObjectives().get(i));
            }

            int total = quest.getTotalObjectivesCount();
            int completed = quest.getCompletedObjectivesCount();

            int filled = total == 0
                    ? 0
                    : (int) Math.round(
                    10.0 * completed / total
            );

            details.append("\n")
                    .append("█".repeat(filled))
                    .append("░".repeat(10 - filled));

            questsList.getItems().add(details.toString());
        }

        for (Quest quest : controller.getCompletedQuests()) {
            questsList.getItems().add(
                    "✓ " + quest.getTitle() + " — COMPLETATA"
            );
        }
    }

    // applica il tema spaziale ai dialoghi JavaFX
    private void applyStylesheet(DialogPane pane) {
        if (styleSheetPath != null) {
            pane.getStylesheets().add(styleSheetPath);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setContentText(message);

        // applica il CSS anche ai messaggi informativi
        applyStylesheet(alert.getDialogPane());

        alert.showAndWait();
    }
}
