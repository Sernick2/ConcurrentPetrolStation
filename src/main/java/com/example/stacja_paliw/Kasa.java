package com.example.stacja_paliw;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.concurrent.Semaphore;

public class Kasa {
    private int id;
    Semaphore czyWolna = new Semaphore(1);
    Circle obrazKasy;
    private int promienKasy = 25;
    private int xKasy = 3 * (HelloApplication.szerokoscOkna/4);

    public Kasa(int id, int poziomKasy){
        this.id = id;
        obrazKasy = new Circle();
        obrazKasy.setRadius(promienKasy);
        obrazKasy.setCenterX(xKasy);
        obrazKasy.setCenterY(poziomKasy + promienKasy);
        obrazKasy.setFill(Color.POWDERBLUE);
    }

    @Override
    public String toString() {
        return "Kasa-" + id;
    }

    public int getQueueLength(){
        return 1 - czyWolna.availablePermits() + czyWolna.getQueueLength();
    }

    public Circle getObrazKasy() {
        return obrazKasy;
    }
}
