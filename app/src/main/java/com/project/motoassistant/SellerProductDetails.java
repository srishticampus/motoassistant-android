package com.project.motoassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProductDetails extends AppCompatActivity {

    private ImageView productIv;
    private TextView productNameTv;
    private TextView productQuantityTv;
    private TextView productDescriptionTv;
    private TextView productPriceTv;
    private TextView productDeleteBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_details);
        initView();

        try {
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("product_image")).into(productIv);
            productNameTv.setText(getIntent().getStringExtra("product_name"));
            productQuantityTv.setText(getIntent().getStringExtra("product_quantity"));
            productDescriptionTv.setText(getIntent().getStringExtra("product_description"));
            productPriceTv.setText(getIntent().getStringExtra("product_price"));

        } catch (Exception e) {

        }

        productDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    apiCall(getIntent().getStringExtra("product_id"));
                } catch (Exception e) {

                }
            }
        });


    }

    public void apiCall(String productId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.SELLER_DELETE_PRODUCT(productId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        // Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));

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

    private void initView() {
        productIv = findViewById(R.id.product_iv);
        productNameTv = findViewById(R.id.product_name_tv);
        productQuantityTv = findViewById(R.id.product_quantity_tv);
        productDescriptionTv = findViewById(R.id.product_description_tv);
        productPriceTv = findViewById(R.id.product_price_tv);
        productDeleteBt = findViewById(R.id.product_delete_bt);
    }
}