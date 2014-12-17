package com.sharathp.symptom_management.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sharath on 11/11/14.
 */
public class LoginFragment extends Fragment {
    // TODO - Migrate the UserLoginTask in LoginActivity to here..

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // this fragment solely exists to retain the LoginTask..
        return null;
    }
}
