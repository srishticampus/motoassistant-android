package com.project.motoassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // showLoadingIndicator(true);
        delayFlow();

    }

    private void delayFlow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                if (sP.getBoolean("user_session", false)) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (sP.getBoolean("seller_session", false)) {
                    Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//                //showLoadingIndicator(false);
//                overridePendingTransition(0, 0);
            }
        }, SPLASH_DELAY);
    }
}