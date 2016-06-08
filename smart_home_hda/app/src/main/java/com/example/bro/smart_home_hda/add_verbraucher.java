package com.example.bro.smart_home_hda;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class add_verbraucher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_verbraucher);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("Raum");

        TextView raum_name_view = (TextView)findViewById(R.id.titel_view_value);
        raum_name_view.setText(name);

        Button hinzu_b = (Button)findViewById(R.id.add_verbraucher_enter_button);

        hinzu_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_name = (EditText)findViewById(R.id.edit_add_verb_name);
                EditText edit_verbrauch = (EditText)findViewById(R.id.edit_add_verb_verbrauch);
                ToggleButton state_butten = (ToggleButton) findViewById(R.id.verbraucher_zustand);
                CheckBox slider_enable = (CheckBox)findViewById(R.id.slider_check_dia);
                SeekBar slider = (SeekBar)findViewById(R.id.slider);
                TextView view_raum_name = (TextView)findViewById(R.id.titel_view_value_dia);

                String verb_name = edit_name.getText().toString();
                int verbrauch = Integer.parseInt(edit_verbrauch.getText().toString());
                Boolean state = state_butten.isChecked();
                Boolean add_slider = slider_enable.isChecked();
                int value = slider.getProgress();
                view_raum_name.setText(name);

                Haus wohnung = (Haus)getApplicationContext();
                ArrayList<Raum> raume = wohnung.get_alle_raume();
                int anzahl_raume = raume.size();
                for(int i = 0; i < anzahl_raume; i++){
                    if(raume.get(i).getName().equals(name)){
                        raume.get(i).addVerbraucher(verb_name,verbrauch,state,name,add_slider,value);
                    }
                }

                Toast toast = Toast.makeText(getBaseContext(),"Verbraucher wurde erfolgreich angelegt!",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
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
