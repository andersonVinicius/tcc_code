
package com.smarthomeintegracao.shi2.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.smarthomeintegracao.shi2.R;

/**
 * Created by root on 23/07/15.
 */
public class AdapterListRemoveEquipment extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private ArrayList<String> arry;
    private final int[] imageId;

    public AdapterListRemoveEquipment(Activity context,
                                      String[] web, int[] imageId) {
        super(context, R.layout.remove_row, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.remove_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView4);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);
        final CheckBox ch = (CheckBox) rowView.findViewById(R.id.checkBox1);
      //  Button btn = (Button) rowView.findViewById()
        arry = new ArrayList<String>();
        ch.setOnClickListener(new View.OnClickListener() {


            // Toast.makeText(view.getContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).Show();
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(ch.isChecked()){
                    arry.add(web[position]);
                    Toast.makeText(v.getContext(), "Checked", Toast.LENGTH_SHORT).show();
                }else{
                    arry.remove(web[position]);
                    Toast.makeText(v.getContext(), "is not Checked", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(v.getContext(), web[position], Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setImageResource(imageId[position]);
        txtTitle.setText(web[position]);


        return rowView;
    }

    public ArrayList<String> getArray(){
        return  arry;
    }
}