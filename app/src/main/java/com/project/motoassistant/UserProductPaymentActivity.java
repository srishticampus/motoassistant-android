package com.project.motoassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProductPaymentActivity extends AppCompatActivity {


    private TextView payBtn;
    private TextInputEditText addressEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_payment);
        initView();

        String productId = getIntent().getStringExtra("product_id");
        String quantity = getIntent().getStringExtra("quantity");
        String userId = getIntent().getStringExtra("userId");
        String price = getIntent().getStringExtra("sellingPrice");

        try {
            int totalPrice = Integer.parseInt(price) * Integer.parseInt(quantity);
            payBtn.setText("PAY " + totalPrice);
        } catch (Exception e) {

        }


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressEt.getText().toString().isEmpty()) {
                    Toast.makeText(UserProductPaymentActivity.this, "Fill the Details", Toast.LENGTH_SHORT).show();
                } else {
                    buyProductApiCall(productId, quantity, addressEt.getText().toString(), userId);
                }
            }
        });


    }

    private void buyProductApiCall(String productId, String quantity, String shippingAddress, String userId) {

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.BUY_PRODUCT_USER(productId, quantity, shippingAddress, userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(UserProductPaymentActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    } else {
                        Toast.makeText(UserProductPaymentActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserProductPaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(UserProductPaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initView() {
        payBtn = findViewById(R.id.pay_btn);
        addressEt = findViewById(R.id.address_et);
    }
}