package com.example.stacja_paliw;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Dystrybutor {

    private final Paliwo paliwo;
    private final Stacja stacja;
    Rectangle obrazDys;

    public Dystrybutor(Paliwo paliwo, Stacja stacja, int poziomDys, int xDys, Color color){
        this.paliwo = paliwo;
        this.stacja = stacja;

        obrazDys = new Rectangle();
        obrazDys.setWidth(10);
        obrazDys.setHeight(10);
        obrazDys.setX(xDys);
        obrazDys.setY(poziomDys);
        obrazDys.setFill(color);
    }

    public Paliwo getPaliwo() {
        return paliwo;
    }

    public Rectangle getObrazDys() {
        return obrazDys;
    }
}
