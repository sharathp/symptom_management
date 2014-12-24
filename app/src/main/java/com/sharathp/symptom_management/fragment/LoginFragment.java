package com.sharathp.symptom_management.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.http.LoginAPI;
import com.sharathp.symptom_management.http.RestClient;
import com.sharathp.symptom_management.loader.Callback;
import com.sharathp.symptom_management.loader.RetrofitLoader;
import com.sharathp.symptom_management.loader.RetrofitLoaderUtil;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.AccessTokenResponse;

/**
 * Login Fragment.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int LOGIN_LOADER_ID = 0;

    // UI references
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        // Set up the login form.
        mUserNameView = (EditText) rootView.findViewById(R.id.username);
        mPasswordView = (EditText) rootView.findViewById(R.id.password);

        final Button doctorSignInButton = (Button) rootView.findViewById(R.id.doctor_sign_in_button);
        doctorSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptDoctorLogin();
            }
        });

        final Button patientSignInButton = (Button) rootView.findViewById(R.id.patient_sign_in_button);
        patientSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptPatientLogin();
            }
        });

        mLoginFormView = rootView.findViewById(R.id.login_form);
        mProgressView = rootView.findViewById(R.id.login_progress);

        return rootView;
    }

    private void attemptPatientLogin() {
        attemptLogin(Session.PATIENT);
    }

    private void attemptDoctorLogin() {
        attemptLogin(Session.DOCTOR);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final int userType) {
        final LoginAPI loginAPI = getLoginAPI(userType);

        // Reset errors.
        resetViewErrors();

        // Store values at the time of the login attempt.
        final String username = mUserNameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if(cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            RetrofitLoaderUtil.init(getLoaderManager(), LOGIN_LOADER_ID, new RetrofitLoader<AccessTokenResponse, LoginAPI>(getActivity(), loginAPI) {
                @Override
                public AccessTokenResponse call(final LoginAPI service) {
                    return service.login(username, password, RestClient.GRANT_TYPE_PASSWORD);
                }
            }, new Callback<AccessTokenResponse>() {
                @Override
                public void onSuccess(final AccessTokenResponse result) {
                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Login Successful");
                    if (Session.saveSession(getActivity(), username, result.getAccessToken(), userType)) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    } else {
                        showPasswordError();
                        showProgress(false);
                    }
                }

                @Override
                public void onFailure(final Exception e) {
                    Log.d(TAG, "Login failed: " + e.getMessage());
                    showPasswordError();
                    showProgress(false);
                }

                private void showPasswordError() {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            });
        }
    }

    private void resetViewErrors() {
        mUserNameView.setError(null);
        mPasswordView.setError(null);
    }

    private LoginAPI getLoginAPI(final int userType) {
        LoginAPI loginAPI = null;
        switch(userType) {
            case Session.PATIENT:
                loginAPI = RestClient.patientLoginApi();
                break;
            case Session.DOCTOR:
                loginAPI = RestClient.doctorLoginApi();
                break;
        }
        return loginAPI;
    }

    private boolean isPasswordValid(final String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
