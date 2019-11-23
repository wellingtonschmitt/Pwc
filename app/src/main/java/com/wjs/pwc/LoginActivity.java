package com.wjs.pwc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 101;
    private final String user = "abacomm";
    private final String pass = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new LoginClickListener(this,
                (EditText) findViewById(R.id.username),
                (EditText) findViewById(R.id.password)));

        requestPermission();
    }

    private void requestPermission() {
        List<String> permissionList = new ArrayList<>(3);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.INTERNET);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        // Here, thisActivity is the current activity
        if (!permissionList.isEmpty()) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[permissionList.size()]),
                    MY_PERMISSIONS_REQUEST);

        } else {
            // Permission has already been granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    requestPermission();
                }
                return;
            }
        }
    }

    private class LoginClickListener implements View.OnClickListener {

        WeakReference<Activity> activity;
        WeakReference<EditText> username;
        WeakReference<EditText> password;

        public LoginClickListener(final Activity activity, final EditText username, final EditText password) {
            this.activity = new WeakReference<>(activity);
            this.username = new WeakReference<>(username);
            this.password = new WeakReference<>(password);
        }

        @Override
        public void onClick(View v) {
            final Activity activity = this.activity.get();
            final EditText usernameEditText = username.get();
            final EditText passwordEditText = password.get();
            if (activity == null || usernameEditText == null || passwordEditText == null)
                return;
            boolean hasError = false;
            if (!user.equals(usernameEditText.getText().toString().trim())) {
                usernameEditText.setError(getString(R.string.login_invalido));
                hasError = true;
            }
            if (!pass.equals(passwordEditText.getText().toString().trim())) {
                hasError = true;
                usernameEditText.setError(getString(R.string.senha_invalido));
            }
            if (!hasError) {
                setResult(Activity.RESULT_OK);
                //Complete and destroy login activity once successful
                finish();
                startActivity(new Intent(activity, EnqueteActivity.class));
            }
        }
    }


}
