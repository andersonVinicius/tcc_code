
package com.smarthomeintegracao.shi2.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.menu.NavigationDrawerCallbacks;
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
 * Created by root on 18/07/15.
 */
public class FragmentSetting extends Fragment implements NavigationDrawerCallbacks {
    ListView list;
    //private LinguagemDataSource  lds ;

    private String[] equipament = {
            "ADD Equipment",
            "Remove Equipment",
            "Edit Equipment",
            "IP Configure"

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
    private ReadJsonAsyncTask conexao;
    private String ip;
    private Context context;
    // private LinguagemDataSource lds;
    // static View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_setting_list, container, false);
        AdapterListSetting adapter = new
                AdapterListSetting(getActivity(), equipament, imageId);

        list = (ListView) rootView.findViewById(R.id.listView3);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (position == 0) {
                    // ActionBar actionBar = getSupportActionBar();
                    Fragment fragment = new FragmentAdd();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                } else if (position == 1) {
                    Fragment fragment = new FragmentRemove();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();

                } else if (position == 2) {
                    context = getActivity();
                    alerDialog(getActivity());
//                    Fragment fragment = FragmentEdit.newInstance(mac,descric);
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.container, fragment)
//                            .commit();

                } else if (position == 3) {
                    alerDialog2();

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


    public void alerDialog(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        final EditText mac2 = new EditText(getActivity());
        mac2.setHint("ID_Equipament");
        //alertDialog.setIcon(R.drawable.ic_title4);
        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                LinguagemDataSource lds = new LinguagemDataSource(context);
                ip = lds.getIp().trim();
                Log.i("connectIp()", ip);
                conexao = new ReadJsonAsyncTask();
                conexao.execute(ip + "/consult?mac=" + mac2.getText().toString());

            }
        });
        alertDialog.setTitle("Please!");
        alertDialog.setMessage("Set the id Mac of the equipment!");
        // alertDialog.setCancelable(false);


        alertDialog.setView(mac2);
        alertDialog.show();
    }

    public void alerDialog2() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText door = new EditText(getActivity());
        door.setHint("Door");
        layout.addView(door);

        final EditText ip = new EditText(getActivity());
        ip.setHint("IP");
        layout.addView(ip);


        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("_PORTA:", door.getText().toString().trim());
                Log.i("_IP:", ip.getText().toString().trim());
                LinguagemDataSource lds = new LinguagemDataSource(getActivity());
                lds.addConexao(door.getText().toString(), ip.getText().toString());

            }
        });
        alertDialog.setTitle("Please!");
        alertDialog.setMessage("Set the Door and IP of the equipment!");

        alertDialog.setView(layout);


        alertDialog.show();
    }

//    public void connectIp(String param){
//        lds = new LinguagemDataSource(getActivity());
//        ip=lds.getIp().trim();
//        Log.i("connectIp()", ip);
//        conexao=new ReadJsonAsyncTask();
//        conexao.execute(ip+param);
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

            // if(!jsonStrBuilder.toString().equals("Removido com sucesso!")) {
            JSONObject jObj = new JSONObject(jsonStrBuilder.toString());

            JSONArray jArray = jObj.getJSONArray("data");
            strArray = new String[jArray.length()];
            macs = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                jObject = jArray.getJSONObject(i);

                descric = jObject.getString("description");
                mac = jObject.getString("Mac_node");

                // imageId[i] = GetImage(getActivity(), jObject.getString("icon"));

            }


//
//            }else{
//                strArray[0]="removido";
//            }

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


        protected void onPostExecute(String[] result) {


            try {


                receiver = result[0];
//
//
                Toast errorToast = Toast.makeText(context, mac + "-" + descric, Toast.LENGTH_SHORT);
                errorToast.show();
//
                Fragment fragment = FragmentEdit.newInstance(mac, descric);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();

                Log.i("##RESULT[0]OnPEx :", result[0].toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

