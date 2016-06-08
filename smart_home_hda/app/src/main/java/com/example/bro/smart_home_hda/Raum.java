package com.example.bro.smart_home_hda;

import java.util.ArrayList;

/**
 * Created by bro on 04.05.2016.
 */

public class Raum {

    ArrayList<Verbraucher> alle_verbraucher = new ArrayList<Verbraucher>();
    String name;
    int anzahlVerbraucher;
    Boolean state;

    public Raum() {
        this.name = "";
        anzahlVerbraucher = 0;
        this.state = true;

    }

    public Raum(String name) {
        this.name = name;
        anzahlVerbraucher = 0;
        this.state = true;
    }

    public Raum(String name, Verbraucher verbraucher){
        this(name,verbraucher,true);
    }

    public Raum(String name, Verbraucher verbraucher, Boolean state){
        this.name = name;
        alle_verbraucher.add(verbraucher);
        anzahlVerbraucher = 1;
        this.state = state;
    }

    public String getName(){
        return name;
    }

    public void addVerbraucher(String name, int verbrauch, Boolean state){

        Verbraucher tmp_verbraucher = new Verbraucher();
        tmp_verbraucher.setName(name);
        tmp_verbraucher.setVerbrauch(verbrauch);
        tmp_verbraucher.setState(state);
        alle_verbraucher.add(tmp_verbraucher);
        anzahlVerbraucher++;

    }

    public void addVerbraucher(String name, int verbrauch, Boolean state, Boolean slider, int value){

        Verbraucher tmp_verbraucher = new Verbraucher();
        tmp_verbraucher.setName(name);
        tmp_verbraucher.setVerbrauch(verbrauch);
        tmp_verbraucher.setState(state);
        tmp_verbraucher.setSlider(slider);
        tmp_verbraucher.setValue(value);
        alle_verbraucher.add(tmp_verbraucher);
        anzahlVerbraucher++;

    }

    public void addVerbraucher(String name, int verbrauch, Boolean state, String raum){

        Verbraucher tmp_verbraucher = new Verbraucher();
        tmp_verbraucher.setName(name);
        tmp_verbraucher.setVerbrauch(verbrauch);
        tmp_verbraucher.setState(state);
        tmp_verbraucher.setRaum_name(raum);
        alle_verbraucher.add(tmp_verbraucher);
        anzahlVerbraucher++;

    }

    public void addVerbraucher(String name, int verbrauch, Boolean state,String raum, Boolean slider, int value){

        Verbraucher tmp_verbraucher = new Verbraucher();
        tmp_verbraucher.setName(name);
        tmp_verbraucher.setVerbrauch(verbrauch);
        tmp_verbraucher.setState(state);
        tmp_verbraucher.setSlider(slider);
        tmp_verbraucher.setValue(value);
        tmp_verbraucher.setRaum_name(raum);
        alle_verbraucher.add(tmp_verbraucher);
        anzahlVerbraucher++;

    }

    public void addVerbraucher(Verbraucher tmp_verb){
        alle_verbraucher.add(tmp_verb);
        anzahlVerbraucher++;

    }

    public void removeVerbraucher(int index){
        alle_verbraucher.remove(index);
    }

    public void removeVerbraucher(String name){
        for(int i = 0; i < alle_verbraucher.size(); i++){
            if(alle_verbraucher.get(i).getName().equals(name)){
                alle_verbraucher.remove(i);
            }
        }
    }

    public Verbraucher get_Verbraucher(int index){
        return alle_verbraucher.get(index);
    }

    public ArrayList<Verbraucher> get_alle_Verbraucher(){
        return alle_verbraucher;
    }

    public int getAnzahlVerbraucher() {
        return anzahlVerbraucher;
    }

    public Boolean getState() {
        return this.state;
    }

    public void setState(Boolean state){
        this.state = state;
    }
}
