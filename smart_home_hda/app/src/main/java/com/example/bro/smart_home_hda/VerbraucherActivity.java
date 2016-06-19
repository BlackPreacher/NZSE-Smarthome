package com.example.bro.smart_home_hda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class VerbraucherActivity extends AppCompatActivity implements myArrayAdapter.ElementChangedCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbraucher);
        final Context context = this;
        final ListView listview = (ListView) findViewById(R.id.listView);

        TextView gesamt_verbrauch_view = (TextView)findViewById(R.id.textVerGesamtValue);

        Haus wohnung = (Haus)getApplicationContext();

        //Haus wohnung = new Haus();
        ArrayList<Raum> raume = wohnung.get_alle_raume();
        final ArrayList<Verbraucher> diese_verbraucher = new ArrayList<Verbraucher>();

        int gesamt_verbraucher = 0;
        int anzahl_raume = raume.size();

        for(int i = 0 ; i < anzahl_raume; i++){
            int anzahl_verbraucher = raume.get(i).getAnzahlVerbraucher();
            for(int j = 0; j < anzahl_verbraucher;j++){
                if(raume.get(i).get_Verbraucher(j).getState() == true)
                {
                    gesamt_verbraucher = gesamt_verbraucher + raume.get(i).get_Verbraucher(j).getVerbrauch();
                }
            }
        }

        gesamt_verbrauch_view.setText(Integer.toString(gesamt_verbraucher) + " kWh");


        for(int i = 0 ; i < anzahl_raume; i++){
            int anzahl_verbraucher = raume.get(i).getAnzahlVerbraucher();
            for(int j = 0; j < anzahl_verbraucher;j++){
                raume.get(i).get_Verbraucher(j).setRaum_name(raume.get(i).getName());
                diese_verbraucher.add(raume.get(i).get_Verbraucher(j));
            }
        }

        myArrayAdapter<Verbraucher> arrayAdapter = new myArrayAdapter(this,R.layout.listitem,diese_verbraucher);

        arrayAdapter.notifyDataSetChanged();
        //Callback auf die aufrufende Activity setzen
        arrayAdapter.setCallback(this);

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listview.getItemAtPosition(position);
                final Verbraucher tmpv = (Verbraucher)o;


                if(tmpv.getSlider()) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Setze dynamischen Wert");
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_layout, null);

                    final SeekBar seek = (SeekBar) dialogView.findViewById(R.id.dialog_seekBar);
                    TextView raum_view = (TextView)dialogView.findViewById(R.id.dialog_raum_view);
                    TextView verbraucher_view = (TextView) dialogView.findViewById(R.id.dialog_verbraucher_view);
                    final TextView value_view = (TextView)dialogView.findViewById(R.id.dialog_value_view);

                    raum_view.setText(tmpv.getRaum_name());
                    verbraucher_view.setText(tmpv.getName());
                    value_view.setText(tmpv.getValue()+" °C");

                    seek.setProgress(tmpv.getValue()-14);

                    if(!tmpv.getState()){
                        seek.setEnabled(false);
                    }

                    seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            int temperatur = progress + 14;
                            value_view.setText(temperatur + " °C");
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    if(tmpv.getState()) {
                        dialog.setPositiveButton("Anwenden", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                tmpv.setValue(seek.getProgress() + 14);
                                int newvalues = seek.getProgress() + 14;

                                Toast toast = Toast.makeText(getApplicationContext(), "Die Temperatur wurde auf " + newvalues + " °C gesetzt", Toast.LENGTH_SHORT);
                                toast.show();
                                reload();
                            }
                        });
                    }

                    dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    dialog.setView(dialogView);
                    dialog.show();

                }
            }
        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listview.getItemAtPosition(position);
                Verbraucher wahl = (Verbraucher)o;
                final String raum = wahl.getRaum_name();
                final String name = wahl.getName();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Löschen");
                alertDialogBuilder.setMessage("Möchtest du diesen eintrag wirklich löschen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    Haus wohnung = (Haus)getApplicationContext();
                                    wohnung.delete_verbraucher(raum,name);
                                }
                            })
                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                //AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.show();
                ///button_pressed();

                reload();

                return true;
            }
        });


    }

    public void reload(){
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    //Callback-Aufruf ueberschreiben um darauf zu reagieren
    @Override
    public void button_pressed() {
        reload();
    }

    //Zurueckbutton ueberschreiben
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ((Integer.parseInt(android.os.Build.VERSION.SDK) > 5)
                && (keyCode == KeyEvent.KEYCODE_BACK)
                && (event.getRepeatCount() == 0)) {
            Log.d("CDA", "onKeyDown Called");
            //Toast toast = Toast.makeText(getBaseContext(),"Zurueck gedrueck",Toast.LENGTH_SHORT);
            //toast.show();
            //finish();
            return_to_parent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //zurueck zu eltern
    public void return_to_parent(){
        NavUtils.navigateUpFromSameTask(this);
        return;
    }
}
