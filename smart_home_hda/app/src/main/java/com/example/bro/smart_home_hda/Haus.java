package com.example.bro.smart_home_hda;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bro on 04.05.2016.
 */
public class Haus extends Application {

    ArrayList<Raum> alle_raume = new ArrayList<>();
    ArrayList<Verbraucher> alle_Verbraucher;

    public Haus() throws IOException, JSONException {

        //JSONArray read = read_file();
        /*
        Verbraucher heizung = new Verbraucher("Heizung",10,true);
        Raum wohnzimmer = new Raum("Wohnzimmer",heizung);
        //wohnzimmer.addVerbraucher("Heizung",10,true);
        wohnzimmer.addVerbraucher("Deckenleuchte",1,false);
        Raum esszimmer = new Raum("Esszimmer");
        esszimmer.addVerbraucher("Heizung",10,false);
        esszimmer.addVerbraucher("Deckenleuchter",1,true);
        Raum kueche = new Raum("Küche");
        kueche.addVerbraucher("Backofen",100,false);
        kueche.addVerbraucher("Herd",1,true);

        alle_raume.add(esszimmer);
        alle_raume.add(kueche);
        alle_raume.add(wohnzimmer);*/

        File file = new File(Environment.getExternalStorageDirectory(),"haus.data");
        if(file.exists()){
            read_file(file);
        }

    }

    public void add_raum(String name,Boolean state){
        Raum tmp_raum = new Raum(name);
        tmp_raum.setState(state);
        alle_raume.add(tmp_raum);
    }

    public void add_raum(String name){
        Raum tmp_raum = new Raum(name);
        alle_raume.add(tmp_raum);
    }
    public void add_raum(Raum raum){
        //Raum tmp_raum = new Raum(name);
        alle_raume.add(raum);
    }


    public void add_verbraucher_to_raum(int index, String name, int verbrauch, boolean state){
        Raum tmp = alle_raume.get(index);
        tmp.addVerbraucher(name,verbrauch,state);
    }

    public void delete_verbraucher(String raum, String name){
        for (int i = 0; i < alle_raume.size(); i++){
            if(alle_raume.get(i).getName().equals(raum)){
                Raum richtiger_raum = alle_raume.get(i);
                for(int j = 0; j < richtiger_raum.getAnzahlVerbraucher(); j++){
                    if(richtiger_raum.get_Verbraucher(j).getName().equals(name)){
                        richtiger_raum.removeVerbraucher(j);
                    }
                }
            }
        }
    }

    public ArrayList<Raum> get_alle_raume() {
        return alle_raume;
    }

    public void write_to_file() throws JSONException, IOException {
        JSONArray job = new JSONArray();

        int anzahl_raume = alle_raume.size();
        //JSONArray jraum = new JSONArray();
        for(int i = 0; i < anzahl_raume; i++){
            //JSONObject tmpjob = new JSONObject();
            //tmpjob.put("name",alle_raume.get(i).getName());
            //jraum.put(tmpjob);
            JSONArray jverb = new JSONArray();
            for(int j = 0 ; j < alle_raume.get(i).getAnzahlVerbraucher(); j++ ){
               JSONObject verb = new JSONObject();
                verb.put("raum",alle_raume.get(i).getName());
                verb.put("name",alle_raume.get(i).get_Verbraucher(j).getName());
                verb.put("verbrauch",alle_raume.get(i).get_Verbraucher(j).getVerbrauch());
                verb.put("state",alle_raume.get(i).get_Verbraucher(j).getState());
                verb.put("values",alle_raume.get(i).get_Verbraucher(j).getValue());
                verb.put("slider",alle_raume.get(i).get_Verbraucher(j).getSlider());
                verb.put("color", getString(R.string.sport));

                jverb.put(verb);
                //job.put("name",alle_raume.get(i).getName());
            }
            job.put(jverb);
        }

        File file = new File(Environment.getExternalStorageDirectory(),"haus.data");
        FileOutputStream outputStream = new FileOutputStream(file);

        outputStream.write(job.toString().getBytes());
        outputStream.close();

    }

    public JSONArray read_file(File file) throws IOException, JSONException {
        FileInputStream inputStream = new FileInputStream(file);
        StringBuffer fileContent = new StringBuffer("");
        int n;
        byte[] buffer = new byte[1024];
        while ((n = inputStream.read(buffer)) != -1) {
            fileContent.append(new String(buffer, 0, n));
        }

        String gelesen = fileContent.toString();

        JSONArray job = new JSONArray(gelesen);

        Log.i("Read JSON", job.toString());

        int anzahl_in_job = job.length();
        boolean found = false;

        for (int i = 0; i < anzahl_in_job; i++) {
            for (int j = 0; j < job.getJSONArray(i).length(); j++) {
                JSONObject tmp = job.getJSONArray(i).getJSONObject(j);
                Log.i("Read", tmp.toString());
                Verbraucher tmp_ver = new Verbraucher((String) tmp.get("name"), (int) tmp.get("verbrauch"), (boolean) tmp.get("state"), (String) tmp.get("raum"), (boolean) tmp.get("slider"), (int) tmp.get("values"));
                for (int k = 0; k < alle_raume.size(); k++) {
                    if (alle_raume.get(k).getName().equals(tmp.get("raum"))) {
                        alle_raume.get(k).addVerbraucher(tmp_ver);
                        found = true;
                    }
                }
                if (found == false) {
                    Raum tmp_raum = new Raum((String) tmp.get("raum"), tmp_ver);
                    add_raum(tmp_raum);
                }
            }
            found = false;
        }

        JSONObject t = job.getJSONArray(0).getJSONObject(0);


        String name = t.getString("raum");
        //Toast.makeText(Haus.this, "powered by sicher inc.", Toast.LENGTH_SHORT).show();
        //Toast toast = Toast.makeText(getBaseContext(),job.getJSONArray(0).toString(),Toast.LENGTH_SHORT);
        //toast.show();

        //Toast toast2 = Toast.makeText(getBaseContext(),"jobs:" + job.getJSONArray(0).getJSONObject(0).toString(),Toast.LENGTH_SHORT);
        //toast2.show();

        return job;


    }

}
