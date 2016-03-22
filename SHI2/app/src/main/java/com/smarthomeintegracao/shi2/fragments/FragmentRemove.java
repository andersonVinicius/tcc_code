
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.util.NetworkUtils;
/**
 * Created by root on 23/07/15.
 */
public class FragmentRemove extends Fragment {
    ListView list;

    String[] equipament = {
            "TV",
            "Washing Machine",
            " Refrigerator"

    } ;
    ArrayList<String> macs;
    JSONObject jObject;
    int[] imageId=new int[40] ;
    private static String receiver ;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private ArrayList<String> marcados;
    private ArrayList<String> marcados2;
    private ReadJsonAsyncTask conexao;
    private String ip;
    static View rootView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_remove_list, container, false);

        // new ReadJsonAsyncTask().execute("http://10.11.81.233:8085/equips/");
        connectIp("/equips/");
        // Inflate the layout for this fragment
        return rootView;
    }
    public void connectIp(String param){
        LinguagemDataSource lsd = new LinguagemDataSource(getActivity());
        ip=lsd.getIp().trim();
        Log.i("connectIp()", ip);
        conexao=new ReadJsonAsyncTask();
        conexao.execute(ip+param);
    }

    public String[] readJson(String url) {
        InputStream is = null;
        String[] strArray ={""} ;

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

            if(!jsonStrBuilder.toString().equals("Removido com sucesso!")) {
                JSONObject jObj = new JSONObject(jsonStrBuilder.toString());

                JSONArray jArray = jObj.getJSONArray("data");
                strArray = new String[jArray.length()];
                macs = new ArrayList<String>();
                for (int i = 0; i < jArray.length(); i++) {
                    jObject = jArray.getJSONObject(i);
                    strArray[i] = jObject.getString("description");
                    macs.add(jObject.getString("Mac_node"));

                    imageId[i] = GetImage(getActivity(), jObject.getString("icon"));

                }




            }else{
                strArray[0]="removido";
            }

        }catch(IOException ie){
            Log.i("readJson", ie.getLocalizedMessage());
        } catch (JSONException e) {
            Log.i("readJson", e.getLocalizedMessage());
        }

        return strArray;
    }

    public static int GetImage(Context c, String ImageName) {
        Log.i("@@@JSON Equip_img:", ImageName.toString());
        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
    }



    private class ReadJsonAsyncTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected String[] doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("##Acho Que o link :", params[0].toString());
            return readJson(params[0]);
        }
        @Override
        protected void onPreExecute() {

            dialog.setMessage("Please wait..");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }


        protected void onPostExecute(String[] result) {


            Log.i("_FROM_FragmentEq res :", result.toString());
            try {
                if(!result[0].equals("removido")){
                    final AdapterListRemoveEquipment adapter = new
                            AdapterListRemoveEquipment(getActivity(), result, imageId);

                    list=(ListView) rootView.findViewById(R.id.listView4);
                    list.setAdapter(adapter);
                    // adapter.getItem();

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(getActivity(), "You Clicked at " + equipament[position], Toast.LENGTH_SHORT).show();

                        }
                    });

                    Button btn  = (Button) rootView.findViewById(R.id.button3);
                    btn.setOnClickListener(new View.OnClickListener() {
                        // String teste = web[position];
                        // Toast.makeText(view.getContext(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).Show();
                        @Override
                        public void onClick(View v) {

                            marcados = adapter.getArray();
                            //marcados2 = new ArrayList<String>();
                            int pst;
                            //marcados= marcados + 1;

                            Toast.makeText(getActivity(),
                                    marcados + " is Checked!!",
                                    Toast.LENGTH_SHORT).show();
                            //String [] list =new String[3];;
                           String mac="X";
                            for (int i = 0; i <marcados.size(); i++) {
                                pst = adapter.getPosition(marcados.get(i));
                                mac= mac+"-"+macs.get(pst).toString();
                                Log.i("@@@MACBtn:", mac);

                            }
                            //new ReadJsonAsyncTask().execute("http://192.168.0.6:8085/remove?mac='"+mac+"'");
                            connectIp("/remove?mac='"+mac+"'");

                            Fragment fragment = new FragmentRemove();
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .commit();

                        }
                    });

                }else{

                    Fragment fragment = new FragmentSetting();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                }


                //  checkUser(receiver);


                Log.i("_FROM_FragmentEq res :", result.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
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
}