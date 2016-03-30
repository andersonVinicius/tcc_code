
package com.smarthomeintegracao.shi2.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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


/**
 * Created by root on 18/07/15.
 */
public class AdapterListEquipment extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final String[] macs;
    private final int[] imageId;
    private final Boolean[] status;
    private  ReadJsonAsyncTask conexao;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private static View rootView;
    private String ip;
    private LinguagemDataSource lst;

    public AdapterListEquipment(Activity context,
                                String[] web, int[] imageId,String[]macs, Boolean[] status) {
        super(context, R.layout.equipment_row, web);
        this.context = context;
        this.web = web;
        this.macs = macs;
        this.imageId = imageId;
        this.status=status;

    }
    @Override
    public View getView(final int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView= inflater.inflate(R.layout.equipment_row, null, true);

        final TextView txtTitle = (TextView) rowView.findViewById(R.id.textView4);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView2);
        final Switch sw = (Switch) rowView.findViewById(R.id.switch2);
        lst=new LinguagemDataSource(context);



        sw.setOnClickListener(new View.OnClickListener() {
            String teste = web[position];


            // Toast.makeText(view.getContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).Show();
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ;
                if (sw.isChecked()) {
                    String sendMac =macs[position];
                    String sendStatus="on";
                    new ReadJsonAsyncTask().execute(lst.getIp()+"/status?mac='"+sendMac+"'&status='"+sendStatus+"'");

                    Toast.makeText(v.getContext(), "ON", Toast.LENGTH_SHORT).show();
                } else {
                    String sendMac =macs[position];
                    String sendStatus="0ff";
                    new ReadJsonAsyncTask().execute(lst.getIp()+"/status?mac='"+sendMac+"'&status='"+sendStatus+"'");

                    Toast.makeText(v.getContext(), "0FF", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(v.getContext(), "Dipositivo.. " + teste + "\n Mac: " + macs[position].toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(v.getContext(), "Started.. "+teste, Toast.LENGTH_SHORT).show();
            }
        });

       // Toast.makeText(v.getContext(), status.toString(), Toast.LENGTH_SHORT).show();

        imageView.setImageResource(imageId[position]);
        txtTitle.setText(web[position]);
        sw.setChecked(status[position]);
        Log.i("$$$$$$$$$$$Status:", status[position].toString());
        Log.i("$$$$$$$$$$$Equip:", web[position].toString());
       // Log.i("$$$$$$$$$$$Status:", status[position].toString());
//        for (int i=0;i<status.length;i++){
//            sw.setChecked(status[i]);
//        }



        return rowView;
    }

//    public void connectIp() {
//        LinguagemDataSource lsd = new LinguagemDataSource(getActivity());
//        ip = lsd.getIp().trim();
//        Log.i("connectIp()", ip);
//        conexao = new ReadJsonAsyncTask();
//        conexao.execute(ip + "/equips/");
//    }

    public String[] readJson(String url) {
        InputStream is = null;
        String[] strArray = {""};

        try {
            is = NetworkUtils.OpenHttpConnection(url, context);
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


    private class ReadJsonAsyncTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("##Acho Que o link :", params[0].toString());
            return readJson(params[0]);
        }
        @Override
        protected void onPostExecute(String[] result) {

            Log.i("_FROM_FragmentEq res :", result.toString());




            }


            //  checkUser(receiver);


//            Log.i("_FROM_FragmentEq res :", result.toString());

        }


}







