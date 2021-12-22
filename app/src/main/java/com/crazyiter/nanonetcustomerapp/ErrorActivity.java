package com.crazyiter.nanonetcustomerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorActivity extends AppCompatActivity {

    public static final int ERROR = 1;
    public static final int NOT_ACTIVE = 2;
    public static final int CHANGED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        int type = getIntent().getIntExtra("type", 1);

        TextView title = findViewById(R.id.titleTV);
        TextView body = findViewById(R.id.bodyTV);

        if (type == ERROR) {
            title.setText(getString(R.string.end_plan));
            body.setText(getString(R.string.end_plan_2));
        } else if (type == NOT_ACTIVE) {
            title.setText(getString(R.string.active_error));
            body.setText(getString(R.string.active_error_2));
        } else if (type == CHANGED) {
            title.setText("تم تغيير اﻹعدادات");
            body.setText("يرجى التواصل مع الوكيل للحصول على اﻹعدادات الجديدة");
        }

        Button backBTN = findViewById(R.id.backBTN);
        backBTN.setOnClickListener(v -> finish());

    }

}