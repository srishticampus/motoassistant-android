package com.project.motoassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    TextView payBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payBt = findViewById(R.id.pay_btn);

        String amount = getIntent().getStringExtra("amount");
        String userId = getIntent().getStringExtra("userId");
        String requestId = getIntent().getStringExtra("requestId");
        payBt.setText("PAY " + amount);
        payBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO check card details filed are empty or not
                apiCall(userId, requestId);
            }
        });

    }

    public void apiCall(String userId, String requestId) {

        // Toast.makeText(this, latitude + longitude, Toast.LENGTH_SHORT).show();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.PAYMENT(userId, requestId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {

                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}