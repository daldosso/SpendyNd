package com.adaldosso.spendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.SignInButton;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        if (Utils.isDebuggable(getActivity())) {
            ((EditText) rootView.findViewById(R.id.email)).setText("demo");
            ((EditText) rootView.findViewById(R.id.password)).setText("demo");
        }

        SignInButton mPlusSignInButton = (SignInButton) rootView.findViewById(R.id.plus_sign_in_button);
        if (Utils.supportsGooglePlayServices(this.getActivity())) {
            mPlusSignInButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LoginActivity login = (LoginActivity) getActivity();
                    login.signIn();
                }

            });
        } else {
            mPlusSignInButton.setVisibility(View.GONE);
        }

        return rootView;
    }

}
