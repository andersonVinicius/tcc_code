
package com.smarthomeintegracao.shi2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.util.NetworkUtils;
/**
 * Created by root on 23/07/15.
 */
public class FragmentEdit extends Fragment {

    private String[] opc ={"Refrigerator                                                                  ",
            "Tv","--","Computer","washing machine"};
    private String ip;
    //private ReadJsonAsyncTask conexao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_edit_equipment, container, false);


        Spinner spn = (Spinner) rootView.findViewById(R.id.spinner3);
        // Spinner spn2= (Spinner) rootView.findViewById(R.id.spinner3);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item, opc);

        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_spinner_dropdown_item, opc2);
//        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn.setAdapter(adp);
        // spn2.setAdapter(adp2);
        // Inflate the layout for this fragment
        return rootView;
    }

//    public void connectIp(String param){
//        LinguagemDataSource lsd = new LinguagemDataSource(getActivity());
//        ip=lsd.getIp().trim();
//        Log.i("connectIp()", ip);
//        conexao=new ReadJsonAsyncTask();
//        conexao.execute(ip+"/equips/");
//    }



    // TODO: Rename method, update argument and hook method into UI event



}
