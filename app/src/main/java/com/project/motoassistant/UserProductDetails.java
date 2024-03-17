package com.project.motoassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class UserProductDetails extends AppCompatActivity {

    private ImageView productIv;
    private TextView productNameTv;
    private EditText productQuantityTv;
    private TextView productDescriptionTv;
    private TextView productPriceTv;
    private TextView buyProductBt;
    private TextView quantityLeftTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_details);
        initView();

        SharedPreferences sh = getApplicationContext().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sh.getString("userId", "");
        String sellerId = getIntent().getStringExtra("seller_id");
        String productId = getIntent().getStringExtra("product_id");
        String sellingPrice = getIntent().getStringExtra("product_price");

        try {
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("product_image")).into(productIv);
            productNameTv.setText(getIntent().getStringExtra("product_name"));
            quantityLeftTv.setText("(" + getIntent().getStringExtra("product_quantity") + " left)");
            productDescriptionTv.setText(getIntent().getStringExtra("product_description"));
            productPriceTv.setText(getIntent().getStringExtra("product_price"));

        } catch (Exception e) {

        }

        buyProductBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (productQuantityTv.getText().toString().isEmpty()) {
                    Toast.makeText(UserProductDetails.this, "Select Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(UserProductDetails.this, UserProductPaymentActivity.class);
                    intent.putExtra("product_id", productId);
                    intent.putExtra("quantity", productQuantityTv.getText().toString());
                    intent.putExtra("userId", userId);
                    intent.putExtra("sellingPrice",sellingPrice);
                    startActivity(intent);
                }


            }
        });


    }

    private void initView() {
        productIv = findViewById(R.id.product_iv);
        productNameTv = findViewById(R.id.product_name_tv);
        productQuantityTv = findViewById(R.id.product_quantity_tv);
        productDescriptionTv = findViewById(R.id.product_description_tv);
        productPriceTv = findViewById(R.id.product_price_tv);
        buyProductBt = findViewById(R.id.buy_product_bt);
        quantityLeftTv = findViewById(R.id.quantity_left_tv);
    }
}