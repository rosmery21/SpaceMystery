
package it.unicam.cs.mpgc.rpg126604;

import it.unicam.cs.mpgc.rpg126604.controller.GameController;
import it.unicam.cs.mpgc.rpg126604.repository.JsonGameRepository;
import it.unicam.cs.mpgc.rpg126604.view.WelcomeView;

import javafx.application.Application;
import javafx.stage.Stage;

// punto di ingresso dell’applicazione JavaFX
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("SpaceMystery");
        stage.setWidth(1024);
        stage.setHeight(768);

        JsonGameRepository repository = new JsonGameRepository();
        GameController controller = new GameController(repository);

        WelcomeView view = new WelcomeView(controller, stage);
        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
