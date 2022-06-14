package com.example.stacja_paliw;

import javafx.application.Platform;
import javafx.scene.Group;

import java.util.Random;

public class Traffic extends Thread{

    private final Stacja stacja;
    private static int nextId = 1;
    private final Group root;

    public Traffic(Stacja stacja, Group root){
        this.stacja = stacja;
        this.root = root;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            Klient klient = new Klient(Stacja.nextId, root);
            Platform.runLater(()->{
                root.getChildren().add(klient.getObrazKlienta());
            });
            try {
                new Thread(()->{
                    try {
                        stacja.wjazdKlient(klient);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                Random rd = new Random();
                Thread.sleep(rd.nextInt(300) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
