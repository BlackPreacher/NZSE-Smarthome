package com.example.bro.smart_home_hda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Raume_detail extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raume_detail);

        final Context context = this;

        final ListView listview = (ListView) findViewById(R.id.raum_verbraucher_list);

        final Haus wohnung = (Haus)getApplicationContext();

        Intent intent = getIntent();

        final String name = intent.getStringExtra("name");
        final boolean state = intent.getBooleanExtra("state",false);

        ArrayList<Raum> raume = wohnung.get_alle_raume();
        ArrayList<Verbraucher> diese_verbraucher = new ArrayList<Verbraucher>();

        int gesamt_verbraucher = 0;
        int anzahl_raume = raume.size();

        for(int i = 0 ; i < anzahl_raume; i++){
            int anzahl_verbraucher = raume.get(i).getAnzahlVerbraucher();
            if(raume.get(i).getName().equals( name )){
                for(int j = 0; j < anzahl_verbraucher;j++){
                    raume.get(i).get_Verbraucher(j).setRaum_name(raume.get(i).getName());
                    diese_verbraucher.add(raume.get(i).get_Verbraucher(j));
                }
            }
        }

        myArrayAdapter<Verbraucher> arrayAdapter = new myArrayAdapter(this,R.layout.listitem,diese_verbraucher);

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

        TextView name_view = (TextView)findViewById(R.id.raumname_view);
        final ToggleButton sw_all = (ToggleButton) findViewById(R.id.switch_all);
        Boolean roomstate = false;
        for(int i = 0; i < anzahl_raume; i++) {
            if(raume.get(i).getName().equals(name)){
                roomstate=raume.get(i).getState();
            }
        }
        sw_all.setChecked(roomstate);
        name_view.setText(name);


        sw_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Haus wohnung = (Haus) getApplicationContext();
                ArrayList<Raum> raume = wohnung.get_alle_raume();
                int anzahl_raume = raume.size();
                Button notaus_local = (Button) findViewById(v.getId());
                for(int i = 0; i < anzahl_raume; i++){
                    if(raume.get(i).getName().equals(name)) {
                        raume.get(i).setState(sw_all.isChecked());
                        for (int j = 0; j < raume.get(i).getAnzahlVerbraucher(); j++) {
                            raume.get(i).get_Verbraucher(j).setState(sw_all.isChecked());
                        }
                    }
                }
                reload();
            }
        });

        /*Button add_verbraucher_b = (Button)findViewById(R.id.neuer_verbraucher_button);
        add_verbraucher_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),add_verbraucher.class);
                intent.putExtra("Raum",name);
                startActivity(intent);
            }
        });*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Füge Verbraucher hinzu");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_verbraucher_dialog, null);

                final SeekBar seek = (SeekBar) dialogView.findViewById(R.id.slider_dia);
                final TextView raum_view = (TextView)dialogView.findViewById(R.id.titel_view_value_dia);
                final EditText ed_verbraucher_name = (EditText) dialogView.findViewById(R.id.edit_add_verb_name_dia);
                final EditText ed_verbraucher_verbrauch = (EditText) dialogView.findViewById(R.id.edit_add_verb_verbrauch_dia);
                final ToggleButton tb_state = (ToggleButton)dialogView.findViewById(R.id.verbraucher_zustand_dia);
                final CheckBox chk_slider = (CheckBox)dialogView.findViewById(R.id.slider_check_dia);

                raum_view.setText(name);

                if(!chk_slider.isChecked()){
                    seek.setEnabled(false);
                }

                chk_slider.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                          @Override
                                                          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                              seek.setEnabled(isChecked);
                                                          }
                                                      });

                        dialog.setPositiveButton("Anwenden", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!ed_verbraucher_name.getText().toString().equals("") && !ed_verbraucher_verbrauch.getText().toString().equals("")) {

                                    final String verb_name = ed_verbraucher_name.getText().toString();
                                    final int verbrauch = Integer.parseInt(ed_verbraucher_verbrauch.getText().toString());
                                    final Boolean state = tb_state.isChecked();
                                    final Boolean add_slider = chk_slider.isChecked();
                                    final int value = seek.getProgress();

                                    Haus wohnung = (Haus) getApplicationContext();
                                    ArrayList<Raum> raume = wohnung.get_alle_raume();
                                    int anzahl_raume = raume.size();
                                    for (int i = 0; i < anzahl_raume; i++) {
                                        if (raume.get(i).getName().equals(name)) {
                                            raume.get(i).addVerbraucher(verb_name, verbrauch, state, name, add_slider, value);
                                        }
                                    }

                                    Toast toast = Toast.makeText(getBaseContext(), "Verbraucher wurde erfolgreich angelegt!", Toast.LENGTH_SHORT);
                                    toast.show();
                                    reload();
                                } else {
                                    Toast.makeText(getBaseContext(), "Bitte fülle alle Felder aus", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                dialog.setView(dialogView);
                dialog.show();
            }
        });


    }

    //zurueckbutton ueberschreiben
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ((Integer.parseInt(android.os.Build.VERSION.SDK) > 5)
                && (keyCode == KeyEvent.KEYCODE_BACK)
                && (event.getRepeatCount() == 0)) {
            Log.d("CDA", "onKeyDown Called");
            //Toast toast = Toast.makeText(getBaseContext(),keyCode,Toast.LENGTH_SHORT);
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

    public void reload(){
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }
}
