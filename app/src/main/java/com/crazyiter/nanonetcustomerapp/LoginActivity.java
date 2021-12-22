package com.crazyiter.nanonetcustomerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.crazyiter.nanonetcustomerapp.db.Customer;
import com.crazyiter.nanonetcustomerapp.db.CustomerManager;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameTIL, passwordTIL;
    private CardView rootCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Customer.getShared(this) != null) {
            startApp();
        }

        rootCV = findViewById(R.id.rootCV);
        usernameTIL = findViewById(R.id.usernameTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        Button loginBTN = findViewById(R.id.loginBTN);

        loginBTN.setOnClickListener(v -> {
            String username = usernameTIL.getEditText().getText().toString().trim();
            String password = passwordTIL.getEditText().getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Statics.showSnackBar(rootCV, getString(R.string.enter_username_password_error));
                return;
            }

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            CustomerManager.login(username, password, customer -> {
                loadingDialog.dismiss();
                if (customer == null) {
                    Statics.showSnackBar(rootCV, getString(R.string.try_again));
                } else {
                    customer.setFcm(FirebaseCloudMessagingService.getToken(this));
                    CustomerManager.updateCustomer(customer);
                    customer.saveShared(this);
                    startApp();
                }
            });
        });

    }

    private void startApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}