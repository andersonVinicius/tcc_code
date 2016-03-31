
package com.smarthomeintegracao.shi2.chart;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.fragments.AdapterListEquipment;
import com.smarthomeintegracao.shi2.util.DemoBase;
import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PieChartActivity extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {
    ListView list;
    String[] macs;
    Boolean[] status;
    int[] imageId = new int[40];

    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    String[] equip;
    double tension[];
    double current[];
    int quant[];
    private static String receiver;
    private BufferedReader streamReader;
    private StringBuilder jsonStrBuilder;
    private String ip;
    private ReadJsonAsyncTask conexao;
    String[] strArray = {""};

    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        connectIp();
        setContentView(R.layout.activity_piechart);

        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);

        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        mSeekBarY.setProgress(10);

        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarY.setOnSeekBarChangeListener(this);

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setBackgroundColor(Color.LTGRAY);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);



        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHole: {
                if (mChart.isDrawHoleEnabled())
                    mChart.setDrawHoleEnabled(false);
                else
                    mChart.setDrawHoleEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionDrawCenter: {
                if (mChart.isDrawCenterTextEnabled())
                    mChart.setDrawCenterText(false);
                else
                    mChart.setDrawCenterText(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleXVals: {

                mChart.setDrawSliceText(!mChart.isDrawSliceTextEnabled());
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.actionTogglePercent:
                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                mChart.invalidate();
                break;
            case R.id.animateX: {
                mChart.animateX(1400);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(1400);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(1400, 1400);
                break;
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));

       // setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
    }

    private void setData() {

       // float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < 4; i++) {
            yVals1.add(new Entry( convertKw(current[i],tension[i],quant[i]), i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < 4; i++)
            xVals.add(equip[i]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Month\nCurrent May");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 5, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 5, s.length() - 6, 0);
        //s.setSpan(new ForegroundColorSpan(Color.GRAY), 5, s.length() - 6, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 5, s.length() - 11, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 3, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 3, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public void connectIp() {
        LinguagemDataSource lsd = new LinguagemDataSource(this);
        ip = lsd.getIp().trim();
        Log.i("connectIp()", ip);
        conexao = new ReadJsonAsyncTask();
        conexao.execute(ip +"/macs/");
    }
    //converter para KWH
    public float convertKw(double curr, double tens,int qtd){

        float pot;
              pot = (float) (curr*tens)/qtd;
              pot = (pot/3600)/1000;

        return pot;
    }

    public String[] readJson(String url) {
        InputStream is = null;


        try {
            is = NetworkUtils.OpenHttpConnection(url, this.getApplicationContext());
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
                equip = new String[jArray.length()];
                current = new double[jArray.length()];
                tension = new double[jArray.length()];
                quant =new int[jArray.length()];
                macs = new String[jArray.length()];
                status = new Boolean[jArray.length()];
                Log.i("@@@ jArray Numberrr:",jArray.length()+"");

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject = jArray.getJSONObject(i);
                    //set valores nos vetores
                    equip[i] = jObject.getString("description");
                    //macs[i] = jObject.getString("Mac_node");
                    current[i] = jObject.getDouble("current");
                    tension[i] = jObject.getDouble("tension");
                    quant[i] = (int) jObject.get("quant");

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

        return equip;
    }


    private class ReadJsonAsyncTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(getApplication());
//            pDialog.setMessage("Please wait...");
//            // pDialog.setCancelable(false);
//            pDialog.show();

        }

        @Override
        protected String[] doInBackground(String... params) {
            // TODO Auto-generated method stub
            Log.i("#doInBackgroundlink :", params[0].toString());
            return readJson(params[0]);
        }


        protected void onPostExecute(String[] result) {

//            if (pDialog.isShowing())
//                pDialog.dismiss();

            Log.i("_FROM_FragmentEq res :", result[3].toString());

            setData();

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
