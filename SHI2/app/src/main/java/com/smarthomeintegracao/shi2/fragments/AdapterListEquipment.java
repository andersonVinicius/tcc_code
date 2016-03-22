
package com.smarthomeintegracao.shi2.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smarthomeintegracao.shi2.R;


/**
 * Created by root on 18/07/15.
 */
public class AdapterListEquipment extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final String[] macs;
    private final int[] imageId;

    public AdapterListEquipment(Activity context,
                                String[] web, int[] imageId,String[]macs) {
        super(context, R.layout.equipment_row, web);
        this.context = context;
        this.web = web;
        this.macs = macs;
        this.imageId = imageId;

    }
    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.equipment_row, null, true);

        final TextView txtTitle = (TextView) rowView.findViewById(R.id.textView4);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);
        final Switch sw = (Switch) rowView.findViewById(R.id.switch2);

        sw.setOnClickListener(new View.OnClickListener() {
            String teste = web[position];
           // Toast.makeText(view.getContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).Show();
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(sw.isChecked()){
                    //String sendMac =macs[position];
                    // String sendStatus="on";
                    //new ReadJsonAsyncTask().execute("http://10.11.81.233:8085/status?mac='"+sendmac+"'&status='"+sendStatus+"');

                    Toast.makeText(v.getContext(), "ON", Toast.LENGTH_SHORT).show();
                }else{
                   // String sendMac =macs[position];
                   // String sendStatus="off";

                    Toast.makeText(v.getContext(), "OFF", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(v.getContext(), "Dipositivo.. " + teste + "\n Mac: " + macs[position].toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(v.getContext(), "Started.. "+teste, Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setImageResource(imageId[position]);
        txtTitle.setText(web[position]);


        return rowView;
    }
}
