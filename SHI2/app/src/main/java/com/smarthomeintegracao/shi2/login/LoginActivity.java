
package com.smarthomeintegracao.shi2.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.smarthomeintegracao.shi2.R;

import com.smarthomeintegracao.shi2.menu.MainActivity;
import com.smarthomeintegracao.shi2.menu.MainActivity2;


public class LoginActivity extends Activity {

    private String nomeView;
    private String typeview;
    private EditText editlogin;
    private EditText editpass;
    private Button btnlgn;
    private Spinner spn;
    private static String receiver ;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    private String login="";
    private String pass="";
    private String type="";
    String[] opc ={"User","Utility"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editlogin = (EditText) findViewById(R.id.editText);
        editpass = (EditText) findViewById(R.id.editText2);

        spn = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adp = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, opc);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn.setAdapter(adp);
        btnlgn = (Button) findViewById(R.id.button);

        btnlgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = editlogin.getText().toString();
                pass  = editpass.getText().toString();
                type = spn.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(), "Logado!", Toast.LENGTH_SHORT).show();

                       //receiver = "TI";

                      callMainActivity_user();
//                    Toast.makeText(getApplicationContext(), login + " - " + pass, Toast.LENGTH_SHORT).show();
//                    new ReadJsonAsyncTask().execute("http://10.11.81.233:8085/login?username=" + login + "&password=" + pass);

                           Log.i("RECEIVER :", "Passou e esta rodando à 1(segundo)");

            }
        });


    }
    public void callMainActivity_admin() {
        Intent it = new Intent(this, MainActivity2.class);
        startActivity(it);
        LoginActivity.this.finish();
    }
    public void callMainActivity_user() {
        MainActivity main = new MainActivity(login,type);
        Intent it = new Intent(this,main.getClass());
        startActivity(it);
        LoginActivity.this.finish();
    }
    //metodo de leitura de requisicao
    public String[] readJson(String url) {
        InputStream is = null;
        String[] strArray ={""} ;
        try {
            is = NetworkUtils.OpenHttpConnection(url, this);
            //leitura
            streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            jsonStrBuilder = new StringBuilder();
            Log.i("1° aqui", "ok");
            String inputStr;
            //add ao StringBuilder
            while ((inputStr = streamReader.readLine()) != null) {
                jsonStrBuilder.append(inputStr);
                Log.i("@@@JSON :", jsonStrBuilder.toString());
            }
            //transformado em JSONObject
//            JSONObject jObj = new JSONObject(jsonStrBuilder.toString());
//
//            JSONArray jArray = jObj.getJSONArray("tasks");
//            strArray = new String[jArray.length()];

//            for(int i = 0; i < jArray.length(); i++){
//                JSONObject jObject = jArray.getJSONObject(i);
//                strArray[i] = jObject.getString("title");
//            }
            strArray[0] = jsonStrBuilder.toString();

            Log.i("@@@strArray :", strArray.toString());
        }catch(IOException ie){
            Log.i("readJson", ie.getLocalizedMessage());
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

                checkUser(receiver);


                Log.i("##RESULT[0] :", result[0].toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void checkUser(String receiver){

            if (receiver.equals("T")) {

                //salva bd android login e type
                if (type.equals("Utility")){
                    callMainActivity_admin();
                  }else{
                    callMainActivity_user();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Senha Invalida " + receiver, Toast.LENGTH_SHORT).show();
            }

        }

    }
}
