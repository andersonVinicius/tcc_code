
package com.smarthomeintegracao.shi2.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.util.NetworkUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class FragmentChartReal extends Fragment implements
        OnChartValueSelectedListener {

    private LineChart mChart;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private static String receiver = "0.0";
    private Runnable mTimer2;
    private ReadJsonAsyncTask conexao;
    private  Handler mHandler;
    private static View rootView;
    private Thread td;
    private Boolean allow=true;
    private String ip;


    public FragmentChartReal() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       // feedMultiple();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.activity_realtime_linechart, container, false);

        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.BLACK);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        LimitLine ll1 = new LimitLine(127f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTextColor(Color.WHITE);
        ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(110f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTextColor(Color.WHITE);
        ll2.setTypeface(tf);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(150f);
        leftAxis.setAxisMinValue(90f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        feedMultiple();

        return  rootView;



    }


    public void connectIp(){
        LinguagemDataSource lsd = new LinguagemDataSource(getActivity());
        ip=lsd.getIp().trim();
        Log.i("connectIp()", ip);
        conexao=new ReadJsonAsyncTask();
        conexao.execute(ip+"/");
    }


    private void addEntry() {

        LineData data = mChart.getData();
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hor = c.get(Calendar.HOUR_OF_DAY);
//        String State;

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            //conexao
            connectIp();

//
//                Toast errorToast = Toast.makeText(getActivity(),  conexao.execute("http://192.168.0.6:8085/")., Toast.LENGTH_SHORT);
//
//                errorToast.show();




            // add a new x-value first
            data.addXValue("" + hor + ":" + minute + ":" + seconds + "");

            data.addEntry(new Entry(Float.parseFloat(receiver), set.getEntryCount()), 0);


            //==========================================================================


            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getXValCount() - 121);

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {


        LineDataSet set = new LineDataSet(null, "Voltage");
        set.setAxisDependency(AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private void feedMultiple() {

//        mHandler = new Handler();
//
//        mTimer2 = new Runnable() {
//
//            @Override
//            public void run() {
//                //for (int i = 0; i < 500; i++) {
//
//                    addEntry();
//                    Log.i("REC_FeedMult :", receiver.toString());
//                    mHandler.postDelayed(this, 1000);
//
//               // }
//            }
//
//        };
//
//
//        mHandler.postDelayed(mTimer2, 1000);



        td = new Thread(new Mythd());
        td.start();

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    //metodo de leitura de requisicao
    public String[] readJson(String url) {
        InputStream is = null;
        String[] strArray = {""};
        try {
            is = NetworkUtils.OpenHttpConnection(url, getActivity().getApplicationContext());
            //leitura
            streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            //  jsonStrBuilder = new StringBuilder();
            Log.i("1° aqui", "Entrou no metodo readJson");
            Log.i("1°  streamReader", streamReader.toString());
            //Log.i("1° StringBuilder", sb.toString());
            String inputStr;
            //add ao StringBuilder
            while ((inputStr = streamReader.readLine()) != null) {
                sb.append(inputStr);
                Log.i("@@@JSON :", sb.toString());


            }
            //transformado em JSONObject
//            JSONObject jObj = new JSONObject(jsonStrBuilder.toString());
//
//            JSONArray jArray = jObj.getJSONArray("tasks");
//            strArray = new String[jArray.length()];

            int j = sb.length();
            Log.i("@Saiu do WHILE:", "loop1");
            Log.i("@Tamanho SB:", j + "#");
//           for(int i = 0; i < sb.length(); i++){
////                JSONObject jObject = jArray.getJSONObject(i);
//               Log.i("Loop FOR:", "loop"+i+" : "+sb.toString());
//              strArray[i] = sb.toString();
//           }
            strArray[0] = sb.toString();

            Log.i("@@@strArray :", strArray.toString());
        } catch (IOException ie) {
            Log.i("readJson", ie.getLocalizedMessage());
        }

        return strArray;
    }
    @Override
    public void onPause() {
         super.onPause();
         Log.w("OnPause%%%", "App Paused");
         allow=false;
        // conexao.cancel(true);
         td.interrupt();

    }
    @Override
    public void onStop() {
        Log.w("OnStop%%%", "App stopped");

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.w("OnDestroy%", "App destroyed");

        super.onDestroy();
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
            if (result[0].equals("")) {
                receiver = "100";

                // Toast errorToast = Toast.makeText(getApplicationContext(), "No internet connection :-((", Toast.LENGTH_SHORT);

                //errorToast.show();
            } else {
                receiver = result[0];
            }

            Log.i("##RESULT[0]OnPEx :", result[0].toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

    private class Mythd implements Runnable{


        @Override
        public void run() {
           while (allow==true){

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        addEntry();
                    }
                });

                try {
                    td.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
