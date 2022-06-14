package com.example.stacja_paliw;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Stanowisko {
    List<Dystrybutor> dystrybutory;
    Semaphore czyPusty = new Semaphore(1);
    //Klient klient = null;
    Rectangle obrazStan;
    private int wymiarStan = 50;
    private int xStan = HelloApplication.szerokoscOkna/4;

    public Stanowisko(ArrayList<Dystrybutor> dystrybutory, int poziomStan){
        this.dystrybutory = dystrybutory;
        obrazStan = new Rectangle();
        obrazStan.setWidth(wymiarStan);
        obrazStan.setHeight(wymiarStan);
        obrazStan.setX(xStan);
        obrazStan.setY(poziomStan);
        obrazStan.setFill(Color.VIOLET);
    }

    public int getQueueLength(){
        return 1 - czyPusty.availablePermits() + czyPusty.getQueueLength();
    }

    public Rectangle getObrazStan() {
        return obrazStan;
    }

    public List<Dystrybutor> getDystrybutory() {
        return dystrybutory;
    }
}
