
package com.smarthomeintegracao.shi2.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                    alerDialog();
                    Fragment fragment = new FragmentEdit();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();

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

    public void alerDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        EditText input = new EditText(getActivity());
        input.setHint("ID_Equipament");
        //alertDialog.setIcon(R.drawable.ic_title4);
        alertDialog.setPositiveButton("Enter", null);
        alertDialog.setTitle("Please!");
        alertDialog.setMessage("Set the id of the equipment!");


        alertDialog.setView(input);
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
}

