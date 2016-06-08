package com.example.bro.smart_home_hda;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_gesamt_verbrauch();

    }

    public void get_gesamt_verbrauch(){
        Haus wohnung = (Haus)getApplicationContext();

        ArrayList<Raum> raume = wohnung.get_alle_raume();
        int anzahl_raume = raume.size();
        int gesamt_verbraucher = 0;

        for(int i = 0 ; i < anzahl_raume; i++){
            int anzahl_verbraucher = raume.get(i).getAnzahlVerbraucher();
            for(int j = 0; j < anzahl_verbraucher;j++){
                if(raume.get(i).get_Verbraucher(j).getState() == true)
                {
                    gesamt_verbraucher = gesamt_verbraucher + raume.get(i).get_Verbraucher(j).getVerbrauch();
                }
            }
        }

        TextView gesamt_verbrauch_view = (TextView) findViewById(R.id.verbraucht_gesamt_value_view);

        gesamt_verbrauch_view.setText(Integer.toString(gesamt_verbraucher) + " kWh");
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_gesamt_verbrauch();
    }

    public void click_raume(View view){
        Intent intent = new Intent(this,RaumeActivity.class);
        //intent.putExtra("Haus",wohnung);
        startActivity(intent);
    }

    public void click_verbraucher(View view){
        Intent intent = new Intent(this,VerbraucherActivity.class);
        startActivity(intent);
    }

    public void click_save_to_file(View view) throws IOException, JSONException {
        Haus wohnung = (Haus)getApplicationContext();
        wohnung.write_to_file();
        //wohnung.read_file();
    }

    public void save_file() throws IOException, JSONException {
        Haus wohnung = (Haus)getApplicationContext();
        wohnung.write_to_file();
        //wohnung.read_file();
    }

    //zurueckbutton ueberschreiben
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if ((Integer.parseInt(android.os.Build.VERSION.SDK) > 5)
                && (keyCode == KeyEvent.KEYCODE_BACK)
                && (event.getRepeatCount() == 0)) {
            Log.d("CDA", "onKeyDown Called");

            //finish();
            try {
                save_file();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            save_file();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //zurueck zu eltern
    public void return_to_parent(){
        NavUtils.navigateUpFromSameTask(this);
        return;
    }
}
