package com.project.motoassistant.ui.seller.seller.seller_order;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.motoassistant.R;

public class SellerOrderDetailsActivity extends AppCompatActivity {

    private ImageView productIv;
    private TextView productNameTv;
    private TextView productQuantityTv;
    private TextView productDescriptionTv;
    private TextView productPriceTv;
    private TextView orderedDateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_details);
        initView();


        try {
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("product_image")).into(productIv);
            productNameTv.setText(getIntent().getStringExtra("product_name"));
            productQuantityTv.setText(getIntent().getStringExtra("product_quantity"));
            productDescriptionTv.setText(getIntent().getStringExtra("product_description"));
            productPriceTv.setText(getIntent().getStringExtra("product_price"));
            orderedDateTv.setText(getIntent().getStringExtra("product_purchase_date"));

        } catch (Exception e) {

        }


    }

    private void initView() {
        productIv = findViewById(R.id.product_iv);
        productNameTv = findViewById(R.id.product_name_tv);
        productQuantityTv = findViewById(R.id.product_quantity_tv);
        productDescriptionTv = findViewById(R.id.product_description_tv);
        productPriceTv = findViewById(R.id.product_price_tv);
        orderedDateTv = findViewById(R.id.ordered_date_tv);
    }
}