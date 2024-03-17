package com.project.motoassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.motoassistant.adapters.ReviewAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceBookingActivity extends AppCompatActivity {

    private TextView tvOne;
    private ShapeableImageView ivDriverProPic;
    private TextView tvWorkshopName;
    private LinearLayout layoutOne;
    private TextView tvWorkshopOverallRating;
    private TextView tvNoOfRatingsWorkshop;
    private LinearLayout layoutTwo;
    private RelativeLayout callBtn;
    private RelativeLayout emailBt;
    private LinearLayout layoutThree;
    private TextView tvWorkshopAddress;
    private TextView tvWorkshopDistrict;
    private TextView tvWorkshopState;
    private LinearLayout layoutFour;
    private RecyclerView rvWorkshopReview;
    private RelativeLayout btBookWorkshop;
    private EditText userMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_booking);
        initView();

        String workshopId = getIntent().getStringExtra("workshopId");
        apiCall(workshopId);

        btBookWorkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userMsg.getText().toString().isEmpty()) {
                    userMsg.setError("Enter Your Problem Here");
                } else {
                    serviceBookApiCall();
                }
            }
        });
    }

    public void apiCall(String workshopId) {

        // Toast.makeText(this, workshopId, Toast.LENGTH_SHORT).show();
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.WORKSHOP_DETAILS(workshopId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Glide.with(getApplicationContext()).load(root.workshopDetails.get(0).file).into(ivDriverProPic);
                        tvWorkshopName.setText(root.workshopDetails.get(0).workshop_name);
                        tvWorkshopOverallRating.setText(String.valueOf(root.workshopDetails.get(0).workshop_rating));
                        tvNoOfRatingsWorkshop.setText(String.valueOf(root.workshopDetails.get(0).no_of_rating));
                        tvWorkshopAddress.setText(root.workshopDetails.get(0).address);
                        reviewApiCall(workshopId);
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

    public void reviewApiCall(String workshopId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.VIEW_ALL_REVIEW(workshopId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        rvWorkshopReview.setLayoutManager(linearLayoutManager);
                        ReviewAdapter reviewAdapter = new ReviewAdapter(root, getApplicationContext());
                        rvWorkshopReview.setAdapter(reviewAdapter);
                    } else {
                        rvWorkshopReview.setVisibility(View.GONE);
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

    private void serviceBookApiCall() {

        SharedPreferences sh = getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sh.getString("userId", "");
        String workshopId = getIntent().getStringExtra("workshopId");
        String serviceId = getIntent().getStringExtra("serviceId");
        String serviceName = getIntent().getStringExtra("serviceName");
        String message = userMsg.getText().toString();
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.REQUEST_SERVICE(workshopId, userId, serviceName, serviceId, message).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(ServiceBookingActivity.this, "Service Booked", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ServiceBookingActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ServiceBookingActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ServiceBookingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                Toast.makeText(ServiceBookingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        tvOne = findViewById(R.id.tv_one);
        ivDriverProPic = findViewById(R.id.iv_driver_pro_pic);
        tvWorkshopName = findViewById(R.id.tv_workshop_name);
        layoutOne = findViewById(R.id.layout_one);
        tvWorkshopOverallRating = findViewById(R.id.tv_workshop_overall_rating);
        tvNoOfRatingsWorkshop = findViewById(R.id.tv_no_of_ratings_workshop);
        layoutTwo = findViewById(R.id.layout_two);
        callBtn = findViewById(R.id.call_btn);
        emailBt = findViewById(R.id.email_bt);
        layoutThree = findViewById(R.id.layout_three);
        tvWorkshopAddress = findViewById(R.id.tv_workshop_address);
        tvWorkshopDistrict = findViewById(R.id.tv_workshop_district);
        tvWorkshopState = findViewById(R.id.tv_workshop_state);
        layoutFour = findViewById(R.id.layout_four);
        rvWorkshopReview = findViewById(R.id.rv_workshop_review);
        btBookWorkshop = findViewById(R.id.bt_book_workshop);
        userMsg = findViewById(R.id.user_msg);
    }
}