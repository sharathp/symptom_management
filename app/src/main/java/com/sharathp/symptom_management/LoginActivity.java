package com.sharathp.symptom_management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sharathp.symptom_management.http.LoginAPI;
import com.sharathp.symptom_management.http.RestClient;
import com.sharathp.symptom_management.login.Session;
import com.sharathp.symptom_management.model.AccessTokenResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUserNameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        final Button doctorSignInButton = (Button) findViewById(R.id.doctor_sign_in_button);
        doctorSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptDoctorLogin();
            }
        });

        final Button patientSignInButton = (Button) findViewById(R.id.patient_sign_in_button);
        patientSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                attemptPatientLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptPatientLogin() {
        attemptLogin(RestClient.patientLoginApi());
    }

    private void attemptDoctorLogin() {
        attemptLogin(RestClient.doctorLoginApi());
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final LoginAPI loginAPI) {

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginAPI.login(username, password, RestClient.GRANT_TYPE_PASSWORD, new Callback<AccessTokenResponse>() {
                @Override
                public void success(final AccessTokenResponse accessTokenResponse, final Response response) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();

//                    setResult(RESULT_OK);
//                    finish();
                    showProgress(false);
                }

                @Override
                public void failure(final RetrofitError error) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    showProgress(false);
                }
            });
        }
    }

    private boolean isPasswordValid(final String password) {
        //TODO: Replace this with your own logic
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