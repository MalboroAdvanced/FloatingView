package com.example.floatingview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnFloating;
    //private static final int DOOAP_ID = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFloating = findViewById(R.id.btnFloating);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        !Settings.canDrawOverlays(this)){

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
           // startActivityForResult(intent,DOOAP_ID);
            startActivity(intent);
        }else {
            floatTheViewOnTheScreen();
        }
    }

    private void floatTheViewOnTheScreen() {
        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this,
                        FloatingViewService.class));
                finish();
            }
        });
    }
}
