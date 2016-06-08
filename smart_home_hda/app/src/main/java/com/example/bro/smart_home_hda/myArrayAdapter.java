package com.example.bro.smart_home_hda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by bro on 04.05.2016.
 */


public class myArrayAdapter<t> extends ArrayAdapter{

    Context context;
    int layoutResourceId;
    private ArrayList<t> liste;

    public ElementChangedCallback callback;

    public myArrayAdapter(Context context, int layoutResourceId, ArrayList<t> liste) {
        super(context, layoutResourceId, liste);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.liste = liste;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        View row = convertView;

        //System.out.println("Position: " + position);


        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        t i = liste.get(position);

        Object v;
        String name;
        String verbrauch;
        final Boolean state;
        String raum_name= "";
        String temperatur = "";
        Boolean slider = false;
        String typ ;

        final Verbraucher v_tmp;

        if(i instanceof Verbraucher)
        {
            v_tmp = (Verbraucher) i;
            System.out.println(v_tmp.toString());
            name = v_tmp.getName();
            verbrauch = Integer.toString(v_tmp.getVerbrauch());
            state = v_tmp.getState();
            slider = v_tmp.getSlider();
            if(v_tmp.getSlider()){
                temperatur = Integer.toString(v_tmp.getValue());
            }
            if(v_tmp.getRaum_name() != "")
            {
                raum_name = v_tmp.getRaum_name();
            } else {
                raum_name = "nicht gesetzt";
            }
        } else {
            Raum tmp = (Raum) i;
            raum_name = tmp.getName();
            name = "Alle Verbraucher";
            int gesamt_verbrauch = 0;
            for(int j = 0; j < tmp.getAnzahlVerbraucher(); j++){
                if(tmp.get_Verbraucher(j).getState() == true) {
                    gesamt_verbrauch = gesamt_verbrauch + tmp.get_Verbraucher(j).getVerbrauch();
                }
            }
            verbrauch = Integer.toString(gesamt_verbrauch);
            state = tmp.getState();
        }

        Verbraucher tmp_ver = new Verbraucher();

        Class j = i.getClass();
        Method k[] = j.getMethods();

        final String test = k[0].getName();

        if(i != null) {

            final TextView text = (TextView) row.findViewById(R.id.text);
            text.setText(name);

            final TextView temperatur_view = (TextView)row.findViewById(R.id.listitem_temperatur_view);
            if(slider){
                Log.i("Arrayadapter","silder: " + slider);
                Log.i("Arrayadapter","Name: " + name);
                temperatur_view.setText(temperatur + " °C");
            } else {
                temperatur_view.setText("");
            }

            TextView verbrauch_tv = (TextView)row.findViewById(R.id.verbrauch_value_view);
            verbrauch_tv.setText(verbrauch + " kWh");

            final TextView raum_name_tv = (TextView)row.findViewById(R.id.raum_name_view);
            raum_name_tv.setText(raum_name);

            Switch sw = (Switch) row.findViewById(R.id.switch1);
            sw.setChecked(state);

            ToggleButton tooglb = (ToggleButton)row.findViewById(R.id.toggle);
            tooglb.setChecked(state);

            tooglb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Switch s = (Switch)v.findViewById(v.getId());
                    ToggleButton s = (ToggleButton)v.findViewById(v.getId());
                    if(s.isChecked() != state){

                        Haus lwohung = (Haus) getContext().getApplicationContext();

                        String raum_name = raum_name_tv.getText().toString();
                        String verbraucher_name = text.getText().toString();

                        ArrayList<Raum> raume = lwohung.get_alle_raume();
                        int anzahl_raume = raume.size();
                        //Raum
                        for(int i = 0 ; i < anzahl_raume; i ++){
                            if(verbraucher_name.equals("Alle Verbraucher")) {
                                if (raume.get(i).getName() == raum_name) {
                                    raume.get(i).setState(s.isChecked());
                                    for(int j = 0; j < raume.get(i).getAnzahlVerbraucher(); j++){
                                        raume.get(i).get_Verbraucher(j).setState(s.isChecked());
                                    }
                                }
                            } else {
                                //Verbraucher
                                if (raume.get(i).getName() == raum_name) {
                                    boolean min_one_on = false;
                                    for(int j = 0; j < raume.get(i).getAnzahlVerbraucher(); j++){
                                        if(raume.get(i).get_Verbraucher(j).getName().equals(verbraucher_name)){
                                            raume.get(i).get_Verbraucher(j).setState(s.isChecked());
                                        }
                                        if(raume.get(i).getState() == false && s.isChecked() == true){
                                            raume.get(i).setState(true);
                                        }
                                        if(raume.get(i).get_Verbraucher(j).getState() == true){
                                            min_one_on = true;
                                        }
                                    }
                                    if(min_one_on == false){
                                        raume.get(i).setState(false);
                                    }

                                }

                            }

                        }


                        Toast.makeText(getContext(),"Changed to " + s.isChecked() + " at " + raum_name,Toast.LENGTH_SHORT).show();
                    }

                    notifyDataSetChanged();

                    //Wenn Callback gesetzt, rufe das ueberschriebene button_pressed auf
                    if(callback != null) {
                        callback.button_pressed();
                    }


                }
            });
        }

        return row;
    }

    //Setzt den Callback auf die Aufrufende Activity

    public void setCallback(ElementChangedCallback callback) {
        this.callback = callback;
    }

    //Muss durch aufrufende Activity ueberschreiben werden

    public interface ElementChangedCallback {
        void button_pressed();
    }

}


