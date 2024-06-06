package com.example.loginpermissions;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {
    MaterialButton exit_BTN_signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViews();
        exit_BTN_signIn.setOnClickListener(v -> closeApplication());
    }

    private void closeApplication() {
        finishAffinity();
        System.exit(0);
    }

    private void findViews() {
        exit_BTN_signIn = findViewById(R.id.main_BTN_exit);
    }

}