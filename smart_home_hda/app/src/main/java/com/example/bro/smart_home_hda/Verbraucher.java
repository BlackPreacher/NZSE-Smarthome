package com.example.bro.smart_home_hda;

/**
 * Created by bro on 04.05.2016.
 */
public class Verbraucher {

    String name;
    int verbrauch;
    Boolean state;
    String raum_name;
    int value = 0;
    Boolean slider = false;

    public Verbraucher() {
        name = "";
        verbrauch = 0;
    }

    public Verbraucher(String name, int verbrauch, Boolean state){
        this.verbrauch = verbrauch;
        this.name = name;
        this.state = state;
        this.raum_name = "";
        this.value = 0;
        this.slider = false;
    }

    public Verbraucher(String name, int verbrauch, Boolean state, String raum_name){
        this.verbrauch = verbrauch;
        this.name = name;
        this.state = state;
        this.raum_name = raum_name;
        this.value = 0;
        this.slider = false;
    }

    public Verbraucher(String name, int verbrauch, Boolean state, String raum_name, Boolean slider, int value){
        this.verbrauch = verbrauch;
        this.name = name;
        this.state = state;
        this.raum_name = raum_name;
        this.value = value;
        this.slider = slider;
    }

    public void setVerbrauch(int verbrauch){
        this.verbrauch = verbrauch;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(Boolean state){
        this.state = state;
    }

    public boolean getState(){
        return this.state;
    }

    public int getVerbrauch(){
        return verbrauch;
    }

    public String getName(){
        return name;
    }

    public void setRaum_name(String raum_name){
        this.raum_name = raum_name;
    }

    public String getRaum_name(){
        return this.raum_name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Boolean getSlider() {
        return slider;
    }

    public void setSlider(Boolean slider) {
        this.slider = slider;
    }

    public String toString(){
        return "Raumname: " + raum_name +  " Verbrauchername: " + name + " Verbrauch: " + verbrauch + " State: " +  state +  " Value: " + value + " Slider: " + slider;
    }
}
