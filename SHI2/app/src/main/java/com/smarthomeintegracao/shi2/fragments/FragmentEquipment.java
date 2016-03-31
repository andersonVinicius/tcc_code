
package com.smarthomeintegracao.shi2.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.util.NetworkUtils;

/**
 * Created by root on 18/07/15.
 */
public class FragmentEquipment extends Fragment {
    ListView list;
    String[] macs;
    Boolean[] status;
    int[] imageId = new int[40];
    private static String receiver;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    static View rootView;
    private String ip = "http://192.168.0.6:8085/equips/";
    private ReadJsonAsyncTask conexao;
    // private Thread td;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_equipment, container, false);
        connectIp();

        // Inflate the layout for this fragment
        return rootView;
    }

    public Boolean getStatus(String status) {
        boolean response;
        if (status.toString().equals("0ff")) {
            response = false;

        } else {
            response = true;

        }

        return response;
    }

    public static int GetImage(Context c, String ImageName) {
        Log.i("@@@JSON Equip_img:", ImageName.toString());
        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
    }

    public void connectIp() {
        LinguagemDataSource lsd = new LinguagemDataSource(getActivity());
        ip = lsd.getIp().trim();
        Log.i("connectIp()", ip);
        conexao = new ReadJsonAsyncTask();
        conexao.execute(ip + "/equips/");
    }

    public String[] readJson(String url) {
        InputStream is = null;
        String[] strArray = {""};

        try {
            is = NetworkUtils.OpenHttpConnection(url, getActivity().getApplicationContext());
            //leitura
            if (is.equals(null))
                Log.i("STATUS", "Connect refused!");
            streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            //  Log.i("SSSSSSSSSSSSSSSSS", streamReader.readLine());
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
            JSONObject jObj = new JSONObject(jsonStrBuilder.toString());

            JSONArray jArray = jObj.getJSONArray("data");
            Log.i("@@@ jArray:", jArray.toString());
            if(!jArray.toString().equals("[]")) {
                strArray = new String[jArray.length()];
                macs = new String[jArray.length()];
                status = new Boolean[jArray.length()];

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    //set valores nos vetores
                    strArray[i] = jObject.getString("description");
                    macs[i] = jObject.getString("Mac_node");
                    status[i] = getStatus(jObject.getString("status").toString());
                    imageId[i] = GetImage(getActivity(), jObject.getString("icon"));

                }
            }else{
                strArray[0]="[]";
            }

        } catch (IOException ie) {

//            Log.i("STATUS", "Connect refused!");
//            Log.i("readJson", ie.getLocalizedMessage());
//            ip="http://192.168.0.6:8085/equips/";
//            connectIp();

        } catch (JSONException e) {

            // Log.i("STATUS", "Connect refused!");
            Log.i("readJson", e.getLocalizedMessage());
        }

        return strArray;
    }


    private class ReadJsonAsyncTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog 
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
           // pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String[] doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("#doInBackgroundlink :", params[0].toString());
            return readJson(params[0]);
        }


        protected void onPostExecute(String[] result) {

            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.i("_FROM_FragmentEq res :", result[0].toString());
            try {
                if(!result[0].equals("[]")) {
                    // if (!result[0].equals("")) {
                    Log.i("<-----------O MAC: ", macs.toString());
                    AdapterListEquipment adapter = new
                            AdapterListEquipment(getActivity(), result, imageId, macs, status);

                    list = (ListView) rootView.findViewById(R.id.listView2);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            //Toast.makeText(getActivity(), "You Clicked at " + equipament[+position], Toast.LENGTH_SHORT).show();

                        }
                    });
                    //  }

                    //  checkUser(receiver);
                }

                Log.i("_FROM_FragmentEq res :", result.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void onPause() {
        super.onPause();
        Log.w("OnPause_listEqp", "App Paused");
        // allow=false;
        // conexao.cancel(true);
        //td.interrupt();

    }
}
