package com.wjs.pwc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final TextView registreSe = findViewById(R.id.registreSe);

        String strCad = getString(R.string.ainda_nao_tem_cadastro);
        String strReg = getString(R.string.registre_se);
        strCad = strCad + " " + strReg;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strCad);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pumpkin_orange)),
                strCad.length() - strReg.length() - 1, strCad.length(), 18);
        spannableStringBuilder.setSpan(new RelativeSizeSpan(1.0F), strCad.length() - strReg.length() - 1, strCad.length(), 33);
        registreSe.setText(spannableStringBuilder);
        registreSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotToday();
            }
        });
        findViewById(R.id.esqueceuSenha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotToday();
            }
        });

        username.addTextChangedListener(new LoginDataTextWatcher(loginButton,username,password));
        password.addTextChangedListener(new LoginDataTextWatcher(loginButton,username,password));

        loginButton.setOnClickListener(new LoginClickListener(this,username,password));
        password.setOnEditorActionListener(new PasswordActionListener(loginButton));
        //TODO
        //requestPermission();
    }

    void showNotToday(){
        Toast.makeText(LoginActivity.this,R.string.not_today,Toast.LENGTH_SHORT).show();
    }

    //region [perms]
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
    //endregion

    private class PasswordActionListener implements TextView.OnEditorActionListener {
        private WeakReference<Button> login;

        public PasswordActionListener(Button login) {
            this.login = new WeakReference<>(login);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            final Button loginBtn = login.get();
            if (loginBtn.isEnabled()) {
                loginBtn.performClick();
                return true;
            }
            return false;
        }
    }

    private class LoginDataTextWatcher implements TextWatcher{

        private WeakReference<EditText> username;
        private WeakReference<EditText> password;
        private WeakReference<Button> login;

        public LoginDataTextWatcher(final Button login, final EditText username, final EditText password) {
            this.login = new WeakReference<>(login);
            this.username = new WeakReference<>(username);
            this.password = new WeakReference<>(password);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            final EditText usernameEditText = username.get();
            final EditText passwordEditText = password.get();
            final Button loginBtn = login.get();
            if (loginBtn == null || usernameEditText == null || passwordEditText == null)
                return;
            loginBtn.setEnabled(usernameEditText.getText().toString().trim().length() > 0 &&
                    passwordEditText.getText().toString().trim().length() > 0);
        }
    }

    private class LoginClickListener implements View.OnClickListener {

        private WeakReference<Activity> activity;
        private WeakReference<EditText> username;
        private WeakReference<EditText> password;

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
                passwordEditText.setError(getString(R.string.senha_invalido));
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
