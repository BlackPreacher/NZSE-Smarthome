package com.example.bro.smart_home_hda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bro.smart_home_hda.R;

/**
 * Created by bro on 04.05.2016.
 */
public class yourAdapter extends BaseAdapter {

    Context context;
    String[] data;
    Boolean state;
    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, String[] data,Boolean state) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        this.state = state;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.listitem, null);
        TextView text = (TextView) vi.findViewById(R.id.text);
        text.setText(data[position]);

        Toast.makeText(context, "powered by Telekom", Toast.LENGTH_SHORT).show();

        Switch sw = (Switch) vi.findViewById(R.id.switch1);
        sw.setBackgroundColor(0xAFFE);
        sw.setChecked(state);
        return vi;
    }

    public Boolean getState(){
        return state;
    }

}
