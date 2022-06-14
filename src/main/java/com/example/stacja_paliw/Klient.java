package com.example.stacja_paliw;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Klient{
    private int id;
    private Paliwo paliwo;
    private int wymKli = 10;
    private int poziomJazdy = HelloApplication.poziomUlicy - 20;
    Rectangle obrazKlienta;
    private final Group root;

    private final Lock lock = new ReentrantLock();
    private final Condition animate = lock.newCondition();

    private int currX;
    private int currY;

    public Klient(int id, Group root) {
        this.id = id;
        this.root = root;
        Random rd = new Random();
        obrazKlienta = new Rectangle();
        switch (rd.nextInt(3) + 1){
            case 1:
                this.paliwo = Paliwo.BENZYNA;
                obrazKlienta.setFill(Color.TOMATO);
                break;
            case 2:
                this.paliwo = Paliwo.DISEL;
                obrazKlienta.setFill(Color.ORANGE);
                break;
            case 3:
                this.paliwo = Paliwo.GAZ;
                obrazKlienta.setFill(Color.DARKORCHID);
                break;
        }

        obrazKlienta.setHeight(wymKli);
        obrazKlienta.setWidth(wymKli);
        obrazKlienta.setX(0);
        currX = 0;
        obrazKlienta.setY(poziomJazdy);
        currY = poziomJazdy;

    }

    public Paliwo getPaliwo() {
        return paliwo;
    }

    @Override
    public String toString() {
        return "Klient-" + id;
    }

    public Rectangle getObrazKlienta() {
        return obrazKlienta;
    }

    public Condition getAnimate() {
        return animate;
    }

    public Lock getLock() {
        return lock;
    }

    public void przejazd(){
        Path path = new Path();
        path.getElements().add(new MoveTo(currX, currY+5));
        path.getElements().add(new LineTo(HelloApplication.szerokoscOkna, currY+5));

        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(5));
        animation.setPath(path);
        animation.setNode(obrazKlienta);

        animation.setOnFinished((e)->{
            root.getChildren().remove(obrazKlienta);
        });

        Platform.runLater(animation::play);
    }

    public void wjazdNaStacje(){
        Path path = new Path();
        path.getElements().add(new MoveTo(currX, currY + 5));
        path.getElements().add(new LineTo(currX + 20, currY + 5));
        path.getElements().add(new LineTo(currX + 20,  3 * (HelloApplication.wysokoscOkna/5)));

        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(4));
        animation.setPath(path);
        animation.setNode(obrazKlienta);

        animation.setOnFinished((e)->{
            currX = currX + 20;
            currY = 3 * (HelloApplication.wysokoscOkna/5);
            lock.lock();
            animate.signal();
            lock.unlock();
        });

        Platform.runLater(animation::play);

    }

    public void wjazdStan(Stanowisko stanowisko){
        Path path = new Path();
        path.getElements().add(new MoveTo(currX, currY));
        path.getElements().add(new LineTo(stanowisko.getObrazStan().getX() + stanowisko.getObrazStan().getWidth()/2, stanowisko.obrazStan.getY() + stanowisko.getObrazStan().getHeight()/2));

        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(4));
        animation.setPath(path);
        animation.setNode(obrazKlienta);

        animation.setOnFinished((e)->{
            currX = (int)stanowisko.getObrazStan().getX() + (int)stanowisko.getObrazStan().getWidth()/2;
            currY = (int)stanowisko.getObrazStan().getY() + (int)stanowisko.getObrazStan().getHeight()/2;
            lock.lock();
            animate.signal();
            lock.unlock();
        });

        Platform.runLater(animation::play);
    }

    public void przedKasy(){
        Path path = new Path();
        path.getElements().add(new MoveTo(currX, currY));
        path.getElements().add(new LineTo(3*(HelloApplication.szerokoscOkna/5), 3*(HelloApplication.wysokoscOkna/5)));

        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(4));
        animation.setPath(path);
        animation.setNode(obrazKlienta);

        animation.setOnFinished((e)->{
            currX = 3*(HelloApplication.szerokoscOkna/5);
            currY = 3*(HelloApplication.wysokoscOkna/5);
            lock.lock();
            animate.signal();
            lock.unlock();
        });

        Platform.runLater(animation::play);
    }

    public void doKasy(Kasa kasa){
        Path path = new Path();
        path.getElements().add(new MoveTo(currX, currY));
        path.getElements().add(new LineTo(kasa.getObrazKasy().getCenterX(), kasa.obrazKasy.getCenterY()));

        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(4));
        animation.setPath(path);
        animation.setNode(obrazKlienta);

        animation.setOnFinished((e)->{
            currX = (int)kasa.getObrazKasy().getCenterX();
            currY = (int)kasa.obrazKasy.getCenterY();
            lock.lock();
            animate.signal();
            lock.unlock();
        });

        Platform.runLater(animation::play);
    }

    public void zeStacji(){
        Path path = new Path();
        path.getElements().add(new MoveTo(currX, currY));
        path.getElements().add(new LineTo(HelloApplication.szerokoscOkna - 20, currY));
        path.getElements().add(new LineTo(HelloApplication.szerokoscOkna - 20, poziomJazdy + 5));
        path.getElements().add(new LineTo(HelloApplication.szerokoscOkna, poziomJazdy + 5));

        PathTransition animation = new PathTransition();
        animation.setDuration(Duration.seconds(4));
        animation.setPath(path);
        animation.setNode(obrazKlienta);

        animation.setOnFinished((e)->{
            root.getChildren().remove(obrazKlienta);
        });

        Platform.runLater(animation::play);
    }
}
