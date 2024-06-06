package com.example.loginpermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_CODE = 123123;
    private MaterialButton login_BTN_signIn;
    private MaterialTextView login_LBL_header;
    private MaterialTextView login_LBL_info;
    private int countTries = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        initViews();
    }

    private void initViews() {
        login_BTN_signIn.setOnClickListener(v -> checkRequirements());
    }

    private void checkRequirements() {
        String[] hints = {
                "I can guide you where to go, but only if you let me know.",
                "I hold the names of those you know, give me access so their details can show.",
                "I am the life force of your device, keep me at full, that's my advice."
        };

        if (checkReadContactsPermission() && checkLocationPermission() && isBatteryFull()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (countTries < 2) {
                Toast.makeText(this, "Incorrect solution. You can do better..", Toast.LENGTH_SHORT).show();
            } else {
                int hintIndex = countTries % hints.length;
                String hint = hints[hintIndex];

                // Create an AlertDialog to display the hint
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Hint")
                        .setMessage(hint)
                        .setCancelable(false)
                        .create();

                dialog.show();

                new Handler().postDelayed(dialog::dismiss, 10000);
            }
            countTries++;
            //requestNecessaryPermissions();
        }
    }

    private boolean checkReadContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isBatteryFull() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, filter);
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
        int batteryPct = (int) ((level / (float) scale) * 100);
        return batteryPct == 100;
    }

    private void requestNecessaryPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted && isBatteryFull()) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Incorrect solution. You can do better..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findViews() {
        login_BTN_signIn = findViewById(R.id.login_BTN_signIn);
        login_LBL_header = findViewById(R.id.login_LBL_header);
        login_LBL_info = findViewById(R.id.login_LBL_info);
    }
}