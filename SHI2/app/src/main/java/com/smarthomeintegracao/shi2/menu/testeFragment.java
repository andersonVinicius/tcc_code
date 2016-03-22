package com.smarthomeintegracao.shi2.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarthomeintegracao.shi2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link testeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link testeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class testeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teste, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event



}
