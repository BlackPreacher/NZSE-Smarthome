package com.example.bro.smart_home_hda;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class RaumeActivity extends AppCompatActivity  implements myArrayAdapter.ElementChangedCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raume);
        final Context context = this;

        final ListView listview = (ListView) findViewById(R.id.raume_list);

        Button sw_notaus = (Button) findViewById(R.id.not_aus_button);

        Haus wohnung = (Haus) getApplicationContext();

        ArrayList<Raum> raume = wohnung.get_alle_raume();

        myArrayAdapter<Raum> arrayAdapter = new myArrayAdapter(this, R.layout.listitem, raume);

        arrayAdapter.setCallback(this);

        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listview.getItemAtPosition(position);
                Raum rap = (Raum) o;

                int raumverbrauch = 0;
                for(int i = 0; i < rap.getAnzahlVerbraucher(); i++){
                    raumverbrauch =  raumverbrauch + rap.get_Verbraucher(i).getVerbrauch();
                }

                Intent intent = new Intent(getApplicationContext(), Raume_detail.class);
                intent.putExtra("name", rap.getName());
                intent.putExtra("state", rap.getState());
                intent.putExtra("verbrauch", raumverbrauch);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Object o = listview.getItemAtPosition(position);
                Raum rap = (Raum) o;
                final String name = rap.getName();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Löschen");
                alertDialogBuilder.setMessage("Möchtest du diesen eintrag wirklich löschen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Haus wohnung = (Haus)getApplicationContext();
                                wohnung.delete_raum(name);
                            }
                        })
                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alertDialogBuilder.show();

                return true;
            }
        });

        sw_notaus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Haus wohnung = (Haus) getApplicationContext();
                ArrayList<Raum> raume = wohnung.get_alle_raume();
                int anzahl_raume = raume.size();
                Button notaus_local = (Button) findViewById(v.getId());
                for(int i = 0; i < anzahl_raume; i++){
                    raume.get(i).setState(false);
                    for(int j = 0 ; j < raume.get(i).getAnzahlVerbraucher(); j++){
                        raume.get(i).get_Verbraucher(j).setState(false);
                    }
                }
                reload();
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_raum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Füge Raum hinzu");
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_raum_dialog, null);

                final EditText ed_raum_name = (EditText) dialogView.findViewById(R.id.ed_add_raum_name_diag);
                //final ToggleButton tb_state = (ToggleButton)dialogView.findViewById(R.id.tgb_add_raum_state);



                dialog.setPositiveButton("Anwenden", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!ed_raum_name.getText().toString().equals("")) {

                            final String raum_name = ed_raum_name.getText().toString();
                            final Boolean state = true;

                            Haus wohnung = (Haus) getApplicationContext();
                            ArrayList<Raum> raume = wohnung.get_alle_raume();
                            wohnung.add_raum(raum_name, state);

                            Toast toast = Toast.makeText(getBaseContext(), "Verbraucher wurde erfolgreich angelegt!", Toast.LENGTH_SHORT);
                            toast.show();
                            reload();
                        } else {
                            Toast.makeText(getBaseContext(), "Sorry, du hast keinen Namen vergeben...", Toast.LENGTH_SHORT).show();

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

    public void reload(){
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }


    @Override
    public void button_pressed() {
        reload();
    }

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

    public void return_to_parent(){
         NavUtils.navigateUpFromSameTask(this);
         return;
    }

}
