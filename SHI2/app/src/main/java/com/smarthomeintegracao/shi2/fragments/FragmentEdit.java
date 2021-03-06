
package com.smarthomeintegracao.shi2.fragments;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by root on 23/07/15.
 */
public class FragmentEdit extends Fragment {
    private String[] opc2 = {"--                                                                        ",
            "Low", "Medium", "High"};
    private String ip;
    //private ReadJsonAsyncTask conexao;
    private String param1;
    private String param2;
    private static View rootView;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private ReadJsonAsyncTask conexao;

    String mac;
    String priority;
    Spinner spn2;
    EditText text;


    //recebe parametros da fragment que o invoca--construtor
    public static  final FragmentEdit newInstance(String param1,String param2) {
        FragmentEdit ltf = new FragmentEdit();


        Bundle bundle = new Bundle(2);
        bundle.putString("param1", param1);
        bundle.putString("param2", param2);


        ltf.setArguments(bundle);

        return ltf;

    }


    private String[] opc ={",Refrigerator                                                                  ",
            "Tv","--","Computer","washing machine"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        param1 = getArguments().getString("param1");
        param2 = getArguments().getString("param2");


        rootView = inflater.inflate(R.layout.fragment_add_node, container, false);
        //conexao;
        connectIp("/appliances/");

        spn2 = (Spinner) rootView.findViewById(R.id.spinner4);


        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item, opc2);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spn2.setAdapter(adp2);
        // Inflate the layout for this fragment
        text = (EditText) rootView.findViewById(R.id.editText3);
        text.setHint(param1);
        text.setEnabled(false);
        return rootView;
    }

    public void connectIp(String param) {
        LinguagemDataSource lsd = new LinguagemDataSource(getActivity());
        ip = lsd.getIp().trim();
        Log.i("connectIp()", ip);
        conexao = new ReadJsonAsyncTask();
        conexao.execute(ip + param);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public String[] readJson(String url) {
        InputStream is = null;
        String[] strArray = {""};

        try {
            is = NetworkUtils.OpenHttpConnection(url, getActivity().getApplicationContext());
            //leitura
            streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            // StringBuilder sb = new StringBuilder();
            jsonStrBuilder = new StringBuilder();
            Log.i("1° aqui", "ok");
            String inputStr;
            // final Drawable d = getDrawable("tv",null);
            //add ao StringBuilder
            while ((inputStr = streamReader.readLine()) != null) {
                jsonStrBuilder.append(inputStr);


            }
            Log.i("@@@JSON Equip:", jsonStrBuilder.toString());
            //transformado em JSONObject
            if (!jsonStrBuilder.toString().equals("cadastrado com sucesso!")) {
                JSONObject jObj = new JSONObject(jsonStrBuilder.toString());

                JSONArray jArray = jObj.getJSONArray("data");
                strArray = new String[jArray.length()];

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    strArray[i] = jObject.getString("description");

                    //imageId[i] = GetImage(getActivity(), jObject.getString("icon"));
                }
            } else {
                strArray[0] = "cadastrado";
            }

        } catch (IOException ie) {
            Log.i("readJson", ie.getLocalizedMessage());
        } catch (JSONException e) {
            Log.i("readJson", e.getLocalizedMessage());
        }

        return strArray;
    }

//    public static int GetImage(Context c, String ImageName) {
//        Log.i("@@@JSON Equip_img:", ImageName.toString());
//        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
//    }


    private class ReadJsonAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("##Acho Que o link :", params[0].toString());
            return readJson(params[0]);
        }


        protected void onPostExecute(String[] result) {

            Log.i("_FROM_FragmentEq res :", result.toString());
            final Spinner spn = (Spinner) rootView.findViewById(R.id.spinner2);
            if (!result[0].equals("cadastrado")) {
                ArrayAdapter<String> adp = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, result);


                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn.setAdapter(adp);



                Button btn = (Button) rootView.findViewById(R.id.button2);
                btn.setOnClickListener(new View.OnClickListener() {
                    // String teste = web[position];
                    // Toast.makeText(view.getContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).Show();
                    @Override
                    public void onClick(View v) {

                        int marcado = spn.getSelectedItemPosition();

                        //mac = text.getText().toString();
                        priority = spn2.getSelectedItem().toString();

                        if (!param1.equals("")) {
                            Toast.makeText(getActivity(), marcado + " is Checked!!", Toast.LENGTH_SHORT).show();
                            marcado = marcado + 1;
                            //new ReadJsonAsyncTask().execute("http://10.11.81.233:8085/insert?mac='"+mac+"'&priority='"+priority+"'&id_appliance="+marcado);
                            connectIp("/edit?mac='"+param1+"'&priority='"+ priority +"'&id_appliance=" + marcado);


                            Fragment fragment = new FragmentSetting();
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();

                        } else {
                            Toast.makeText(getActivity(), "Insert Address Mac! ", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }


            //  checkUser(receiver);


            Log.i("_FROM_FragmentEq res :", result.toString());

        }

//        public void checkUser(String receiver){
//
//            if (receiver.equals("T")) {
//
//
//                if (type.equals("Utility")){
//                   // callMainActivity_admin();
//                }else{
//                    callMainActivity_user();
//                }
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Senha Invalida " + receiver, Toast.LENGTH_SHORT).show();
//            }
//
//        }

    }

}