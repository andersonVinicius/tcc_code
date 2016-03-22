
package com.smarthomeintegracao.shi2.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.util.NetworkUtils;

/**
 * Created by root on 18/07/15.
 */
public class AdapterListSetting extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public AdapterListSetting(Activity context,
                                 String[] web, Integer[] imageId) {
        super(context, R.layout.setting_row, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.setting_row, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView8);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView3);
        //Switch sw = (Switch) rowView.findViewById(R.id.switch2);


        imageView.setImageResource(imageId[position]);
        txtTitle.setText(web[position]);


        return rowView;
    }
}
