package com.adaldosso.spendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        if (Utils.isDebuggable(getActivity())) {
            ((EditText) rootView.findViewById(R.id.email)).setText("alberto");
            ((EditText) rootView.findViewById(R.id.password)).setText("prmnd05");
        }

        return rootView;
    }

}
