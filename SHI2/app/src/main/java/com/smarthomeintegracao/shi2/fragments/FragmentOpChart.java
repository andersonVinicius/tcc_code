
package com.smarthomeintegracao.shi2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.smarthomeintegracao.shi2.chart.HorizontalBarChartActivity;
import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.chart.PieChartActivity;
import com.smarthomeintegracao.shi2.menu.NavigationDrawerCallbacks;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Created by anderson on 29/03/16.
 */
public class FragmentOpChart extends Fragment implements NavigationDrawerCallbacks {
    ListView list;
    //private LinguagemDataSource  lds ;

    private String[] equipament = {
            "Tension in Real Time",
            "Consumer KW Month Home",
            "Consumer KW Month Equipment",
            "--------------"

    };
    private Integer[] imageId = {
            R.drawable.add,
            R.drawable.delete4,
            R.drawable.edit,
            R.drawable.edit


    };
    private String mac = "";
    private String descric = "";
    private String priory = null;
    private String status = null;

    // ListView list;

    ArrayList<String> macs;
    JSONObject jObject;
    //int[] imageId=new int[40] ;
    private static String receiver;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private ArrayList<String> marcados;
    private ArrayList<String> marcados2;

    private String ip;
    private Context context;
    // private LinguagemDataSource lds;
    // static View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_setting_list, container, false);
        AdapterOpChart adapter = new
                AdapterOpChart(getActivity(), equipament, imageId);

        list = (ListView) rootView.findViewById(R.id.listView3);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {
                    // ActionBar actionBar = getSupportActionBar();
                    Fragment fragment = new FragmentChartReal();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                } else if (position == 1) {


                    HorizontalBarChartActivity main = new HorizontalBarChartActivity();
                    Intent it = new Intent(getActivity(),main.getClass());

                    startActivity(it);
                   // LoginActivity.this.finish();
//                    Fragment fragment = new Fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, fragment)
//                            .commit();

                } else if (position == 2) {
                    PieChartActivity main = new PieChartActivity();
                    Intent it = new Intent(getActivity(),main.getClass());
                    startActivity(it);
//                    Fragment fragment = FragmentEdit.newInstance(mac,descric);
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, fragment)
//                            .commit();

                } else if (position == 3) {


                }
                Toast.makeText(getActivity(), "You Clicked at " + equipament[+position], Toast.LENGTH_SHORT).show();

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


}
