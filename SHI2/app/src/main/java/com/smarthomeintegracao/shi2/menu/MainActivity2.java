
package com.smarthomeintegracao.shi2.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.smarthomeintegracao.shi2.R;



public class MainActivity2 extends AppCompatActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment2 mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer2);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        mNavigationDrawerFragment = (NavigationDrawerFragment2) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
        ActionBar actionBar = getSupportActionBar();
        Fragment fragment = null;
        switch (position) {

            case 0:
//                ActionBar actionBar1 = getSupportActionBar();
                if(actionBar != null){
                    actionBar.setTitle("   Tv");
//                    actionBar.setIcon(getResources().getDrawable(R.drawable.tv));
//                    fragment = new tv_fragment();
                }


                //actionBar.setLogo(getResources().getDrawable(R.drawable.icon_geladeira));


//			//fragment = new AreaEstratoFragment();
//			//fragment = new ISSNFragment();
//			fragment = new HistoricoFragment();
                break;
            case 1:
                Intent i;
                actionBar.setTitle("   Refrigerator");
                actionBar.setIcon(getResources().getDrawable(R.drawable.icon_geladeira));
                //actionBar.setLogo(getResources().getDrawable(R.drawable.icon_geladeira));
//                i = new Intent(this, ListViewBarChartFragment.class);
//                startActivity(i);


//			//fragment = new AreaEstratoFragment();
//			//fragment = new ISSNFragment();
//			fragment = new HistoricoFragment();
                break;

            case 2:
                actionBar.setTitle("   Washing machine");
                actionBar.setIcon( getResources().getDrawable(R.drawable.lavadora));

                //actionBar.setLogo(getResources().getDrawable(R.drawable.icon_geladeira));
                fragment = new testeFragment();

//			//fragment = new AreaEstratoFragment();
//			//fragment = new ISSNFragment();
//			fragment = new HistoricoFragment();
                break;
            case 3:
                actionBar.setTitle("   Computer");
                actionBar.setIcon( getResources().getDrawable(R.drawable.computer));

                //actionBar.setLogo(getResources().getDrawable(R.drawable.icon_geladeira));
                //fragment = new testeFragment();

//			//fragment = new AreaEstratoFragment();
//			//fragment = new ISSNFragment();
//			fragment = new HistoricoFragment();
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
