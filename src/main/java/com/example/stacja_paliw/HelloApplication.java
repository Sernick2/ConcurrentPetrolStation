package com.example.stacja_paliw;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final int szerokoscOkna = 1200;
    public static final int wysokoscOkna = 700;
    public static final int poziomUlicy = wysokoscOkna/5;

    private static Stacja stacja;
    private static Traffic traffic;

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Pane root = fxmlLoader.load();
        Scene zmiana = new Scene(root);

        stage.setScene(zmiana);
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.show();
    }

    public static void dzialanie(int maxS, int lS, int lk, int pP){
        Group root = new Group();
        Scene scene = new Scene(root, szerokoscOkna, wysokoscOkna);

        Line line  = new Line();
        line.setStartX(0);
        line.setStartY(poziomUlicy);
        line.setEndX(szerokoscOkna);
        line.setEndY(poziomUlicy);

        stacja = new Stacja(maxS, lS, lk, pP);
        stacja.tworzStanowiska();
        stacja.tworzKasy();

        traffic = new Traffic(stacja, root);
        traffic.start();

        stage.setTitle("Stacja Paliw");
        root.getChildren().add(line);
        for(Stanowisko s : stacja.stanowiska) {
            root.getChildren().add(s.getObrazStan());
            for (Dystrybutor dys : s.getDystrybutory())
                root.getChildren().add(dys.getObrazDys());
        }
        for(Kasa k : stacja.kasy)
            root.getChildren().add(k.getObrazKasy());
        stage.setScene(scene);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}