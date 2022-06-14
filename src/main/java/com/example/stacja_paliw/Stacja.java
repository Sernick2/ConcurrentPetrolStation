package com.example.stacja_paliw;

import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Stacja extends Thread{

    public static int nextId = 1;

    private final Lock zamek = new ReentrantLock();
    private int dopLiczbKlientow = 30;
    private int aktLiczbKlientow = 0;
    private int liczbaStan = 7;
    private int poczStPaliwa = 30;
    private int liczbaKas = 6;
    private boolean czyOtwarte = true;
    ArrayList<Stanowisko> stanowiska = new ArrayList<>();
    ArrayList<Kasa> kasy = new ArrayList<>();

    Map<Paliwo, Integer> aktStanPaliwa = new ConcurrentHashMap<>();

    public Stacja(int maxS, int lS, int lk, int pP){
        aktStanPaliwa.put(Paliwo.DISEL, poczStPaliwa);
        aktStanPaliwa.put(Paliwo.BENZYNA, poczStPaliwa);
        aktStanPaliwa.put(Paliwo.GAZ, poczStPaliwa);
        this.dopLiczbKlientow = maxS;
        this.liczbaStan = lS;
        this.liczbaKas = lk;
        this.poczStPaliwa = pP;
    }


    public void wjazdKlient(Klient klient) throws InterruptedException{

        zamek.lock();
            if (aktLiczbKlientow == dopLiczbKlientow || !czyOtwarte) {
                //pelny.await();
                klient.przejazd();
                zamek.unlock();
                return;
            }

            nextId++;
            klient.wjazdNaStacje();
            System.out.println(klient + " wjechal na stacje");
            aktLiczbKlientow++;
        zamek.unlock();
            Random rd = new Random();
            Thread.sleep(rd.nextInt(300) + 500);
            klient.getLock().lock();
            klient.getAnimate().await();
            klient.getLock().unlock();
            stanowiskoKlient(klient);
            //wyjazdKlient(klient);

            //
    }

    private void wyjazdKlient(Klient klient) throws InterruptedException{
        System.out.println(klient + " wyjechal ze stacji");
        aktLiczbKlientow--;

        Random rd2 = new Random();

        if(aktLiczbKlientow == 0){
            System.out.println("Uzupelnianie paliwa na stacji...");
            aktStanPaliwa.put(Paliwo.DISEL, poczStPaliwa);
            aktStanPaliwa.put(Paliwo.BENZYNA, poczStPaliwa);
            aktStanPaliwa.put(Paliwo.GAZ, poczStPaliwa);
            Thread.sleep(rd2.nextInt(1000) + 1600);
            System.out.println("Uzupelniono paliwo na stacji");
            czyOtwarte = true;
        }
    }

    public void tworzStanowiska() {
        for(int i = 0; i < liczbaStan; i++){
            Random rd = new Random();
            ArrayList<Dystrybutor> dystrybutors = new ArrayList<>();
            for(int j = 0; j < rd.nextInt(3) + 1; j++){
                int poziomDys = HelloApplication.poziomUlicy+10+((HelloApplication.wysokoscOkna-10-HelloApplication.poziomUlicy+10)/liczbaStan)*i;
                int xDys = HelloApplication.szerokoscOkna/4 + 15 * j;
                Dystrybutor dystr  = new Dystrybutor(null, this, poziomDys, xDys, Color.BLACK);
                switch (rd.nextInt(3) + 1){
                    case 1:
                        dystr = new Dystrybutor(Paliwo.BENZYNA, this, poziomDys, xDys, Color.TOMATO);
                        break;
                    case 2:
                        dystr = new Dystrybutor(Paliwo.DISEL, this, poziomDys, xDys, Color.ORANGE);
                        break;
                    case 3:
                        dystr = new Dystrybutor(Paliwo.GAZ, this, poziomDys, xDys, Color.DARKORCHID);
                        break;
                }
                dystrybutors.add(dystr);
            }
            stanowiska.add(new Stanowisko(dystrybutors, HelloApplication.poziomUlicy+10+((HelloApplication.wysokoscOkna-10-HelloApplication.poziomUlicy+10)/liczbaStan)*i));
        }
    }

    public void tankowanie(Paliwo paliwo) throws InterruptedException{
        aktStanPaliwa.put(paliwo, aktStanPaliwa.get(paliwo)-1);
        if(aktStanPaliwa.get(paliwo) <= aktLiczbKlientow){
               czyOtwarte = false;
        }
        Random rd1 = new Random();
        Thread.sleep(rd1.nextInt(300) + 2000);
    }

    public void stanowiskoKlient(Klient klient) throws InterruptedException{
        Stanowisko found = znajdzStanowisko(klient);
        if(found == null){
            klient.zeStacji();
            wyjazdKlient(klient);
        }else {

            found.czyPusty.acquire();
            klient.wjazdStan(found);
            System.out.println(klient + " podjechal do stanowiska");
            tankowanie(klient.getPaliwo());
            klient.getLock().lock();
            klient.getAnimate().await();
            klient.getLock().unlock();
            klient.przedKasy();
            System.out.println(klient + " odjechal ze stanowiska");
            found.czyPusty.release();
            klient.getLock().lock();
            klient.getAnimate().await();
            klient.getLock().unlock();
            klientKasy(klient);
        }
    }

    private Stanowisko znajdzStanowisko(Klient klient){
        return stanowiska.stream()
                .filter(stan -> stan.getDystrybutory().stream().anyMatch(dyst -> dyst.getPaliwo().equals(klient.getPaliwo())))
                .sorted(Comparator.comparingInt(Stanowisko::getQueueLength))
                .findFirst().orElse(null);
    }

    public void tworzKasy(){
        for(int i = 0; i < liczbaKas; i++){
            kasy.add(new Kasa(i + 1, HelloApplication.poziomUlicy+10+((HelloApplication.wysokoscOkna-10-HelloApplication.poziomUlicy+10)/liczbaKas)*i));
        }
    }

    public void klientKasy(Klient klient) throws InterruptedException{
        Kasa found = znajdzKase(klient);
        //System.out.println("Znaleziono " + found);
        Random rd3 = new Random();
        found.czyWolna.acquire();
        klient.doKasy(found);
        System.out.println(klient + " podjechal do kasy " + found);
        Thread.sleep(rd3.nextInt(1000) + 1000);
        System.out.println(klient + " wyjechal z kasy " + found);
        klient.getLock().lock();
        klient.getAnimate().await();
        klient.getLock().unlock();
        klient.zeStacji();
        found.czyWolna.release();
        wyjazdKlient(klient);

    }

    private Kasa znajdzKase(Klient klient){
        return kasy.stream()
                .sorted(Comparator.comparingInt(Kasa::getQueueLength))
                .findFirst().orElse(null);
    }

}
