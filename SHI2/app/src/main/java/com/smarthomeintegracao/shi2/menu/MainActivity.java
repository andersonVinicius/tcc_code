
package com.smarthomeintegracao.shi2.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smarthomeintegracao.shi2.R;
import com.smarthomeintegracao.shi2.chart.RealtimeLineChartActivity;
import com.smarthomeintegracao.shi2.dao.LinguagemDataSource;
import com.smarthomeintegracao.shi2.fragments.FragmentChartReal;
import com.smarthomeintegracao.shi2.fragments.FragmentEquipment;
import com.smarthomeintegracao.shi2.fragments.FragmentOpChart;
import com.smarthomeintegracao.shi2.fragments.FragmentSetting;
;
import com.smarthomeintegracao.shi2.login.LoginActivity;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private static String name;
    private static String type;
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TextView nomeView;
    private TextView typeView;

    public MainActivity() {

    }

    public MainActivity(String name, String type) {
        Log.i("Nome", name);
        Log.i("tipo", type);
        this.name = name;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setTextView(name, type);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        Fragment fragment = null;

        fragment = new FragmentEquipment();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void alerDialog2() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText door = new EditText(this);
        door.setHint("Door");
        layout.addView(door);

        final EditText ip = new EditText(this);
        ip.setHint("IP");
        layout.addView(ip);


        alertDialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("_PORTA:", door.getText().toString().trim());
                Log.i("_IP:", ip.getText().toString().trim());
                LinguagemDataSource lds = new LinguagemDataSource(getApplication());
                lds.addConexao(door.getText().toString(), ip.getText().toString());

            }
        });
        alertDialog.setTitle("Please!");
        alertDialog.setMessage("Set the Door and IP of the equipment!");

        alertDialog.setView(layout);



        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_search: {
                alerDialog2();
                break;
            }
            case R.id.actionClear: {
              //  mChart.clearValues();
                Toast.makeText(this, "Chart cleared!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.actionFeedMultiple: {
              //  feedMultiple();
                break;
            }
        }
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        ActionBar actionBar = getSupportActionBar();
        Fragment fragment = null;
        Intent i;
        switch (position) {

            case 0:


                if (actionBar != null) {
                    actionBar.setTitle("   Home");
                    actionBar.setIcon(getResources().getDrawable(R.drawable.home));
                    //fragment = new tv_fragment();
                    fragment = new FragmentEquipment();
                }





                break;
            case 1:

                actionBar.setTitle("   Statistic");
                actionBar.setIcon(getResources().getDrawable(R.drawable.statistic2));

                fragment = new FragmentOpChart();

                break;

            case 2:
                actionBar.setTitle("   Settings");
                actionBar.setIcon(getResources().getDrawable(R.drawable.setting));

                //actionBar.setLogo(getResources().getDrawable(R.drawable.icon_geladeira));
                fragment = new FragmentSetting();

                break;
            case 3:
                //Intent i;
                actionBar.setTitle("   Logout");
                actionBar.setIcon(getResources().getDrawable(R.drawable.logout));
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                MainActivity.this.finish();

                break;
        }


        if (fragment != null) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }
}
