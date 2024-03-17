package com.project.motoassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentOrderDetails extends AppCompatActivity {

    private RelativeLayout layoutOne;
    private LinearLayout layoutTwo;
    private TextView orderId;
    private TextView serviceCenterName;
    private TextView serviceType;
    private RelativeLayout callBtn;
    private RelativeLayout emailBt;
    private LinearLayout layoutThree;
    private TextView orderStatusTxt;
    private RelativeLayout layoutFour;
    private TextView mechName;
    private TextView mechanicNameTxt;
    private TextView mechanicMobNumber;
    private LinearLayout layoutFive;
    private TextView orderDateTxt;
    private TextView userDescriptionTxt;
    private RelativeLayout layoutSix;
    private TextView estimateAmount;
    private TextView actualAmount;
    private TextView payBtn;
    private TextView howTxt;
    private RatingBar ratingBar;
    private TextView ratingSubmitBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText userReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_order_details);
        initView();

        apiCall();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiCall();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    public void apiCall() {

        String userId = getIntent().getStringExtra("userId");
        String requestId = getIntent().getStringExtra("requestId");
       // Toast.makeText(this, requestId, Toast.LENGTH_SHORT).show();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.ORDER_DETAILS(userId, requestId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        orderId.setText(root.orderHistory.get(0).request_id);
                        serviceCenterName.setText(root.orderHistory.get(0).workshop_name);
                        serviceType.setText(root.orderHistory.get(0).service_name);
                        orderStatusTxt.setText(root.orderHistory.get(0).completion_status);
                        mechanicNameTxt.setText(root.orderHistory.get(0).staff_name);
                        mechanicMobNumber.setText(root.orderHistory.get(0).phone);
                        orderDateTxt.setText(root.orderHistory.get(0).accept_date);
                        estimateAmount.setText(root.orderHistory.get(0).estimated_price);
                        actualAmount.setText(root.orderHistory.get(0).final_price);
                        if (root.orderHistory.get(0).completion_status.equals("Completed")) {
                            if (root.orderHistory.get(0).payment_status.equals("paid")) {
                                payBtn.setVisibility(View.GONE);
                                if (root.orderHistory.get(0).rating_status || root.orderHistory.get(0).review_status) {
                                    ratingBar.setVisibility(View.VISIBLE);
                                    ratingBar.setEnabled(false);
                                    ratingBar.setRating(root.orderHistory.get(0).rating_count);
                                    userReview.setVisibility(View.VISIBLE);
                                    userReview.setEnabled(false);
                                    userReview.setText(root.orderHistory.get(0).review);
                                    //TODO add rating and display submitted review
                                } else {
                                    howTxt.setVisibility(View.VISIBLE);
                                    ratingBar.setVisibility(View.VISIBLE);
                                    userReview.setVisibility(View.VISIBLE);
                                    ratingSubmitBtn.setVisibility(View.VISIBLE);
                                    ratingSubmitBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            api.ADD_REVIEW(userId, root.orderHistory.get(0).workshop_id, String.valueOf(ratingBar.getNumStars()), root.orderHistory.get(0).service_id,
                                                    requestId,
                                                    userReview.getText().toString()).enqueue(new Callback<Root>() {
                                                @Override
                                                public void onResponse(Call<Root> call, Response<Root> response) {
                                                    if (response.isSuccessful()) {
                                                        Root root = response.body();
                                                        if (root.status) {
                                                            Toast.makeText(RecentOrderDetails.this, root.message, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(RecentOrderDetails.this, "Server Error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Root> call, Throwable t) {
                                                    Toast.makeText(RecentOrderDetails.this, "Server Error", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    });
                                }
                            } else {
                                payBtn.setVisibility(View.VISIBLE);
                            }
                        } else {
                            payBtn.setVisibility(View.GONE);
                            Toast.makeText(RecentOrderDetails.this, "Refresh To See Current Status", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                    }

                    payBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RecentOrderDetails.this, PaymentActivity.class);
                            intent.putExtra("amount", root.orderHistory.get(0).final_price);
                            intent.putExtra("userId", root.orderHistory.get(0).user_id);
                            intent.putExtra("requestId", root.orderHistory.get(0).request_id);
                            startActivity(intent);
                        }
                    });

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
        layoutOne = findViewById(R.id.layout_one);
        layoutTwo = findViewById(R.id.layout_two);
        orderId = findViewById(R.id.order_id);
        serviceCenterName = findViewById(R.id.service_center_name);
        serviceType = findViewById(R.id.service_type);
        callBtn = findViewById(R.id.call_btn);
        emailBt = findViewById(R.id.email_bt);
        layoutThree = findViewById(R.id.layout_three);
        orderStatusTxt = findViewById(R.id.order_status_txt);
        layoutFour = findViewById(R.id.layout_four);
        mechName = findViewById(R.id.mech_name);
        mechanicNameTxt = findViewById(R.id.mechanic_name_txt);
        mechanicMobNumber = findViewById(R.id.mechanic_mob_number);
        layoutFive = findViewById(R.id.layout_five);
        orderDateTxt = findViewById(R.id.order_date_txt);
        userDescriptionTxt = findViewById(R.id.user_description_txt);
        layoutSix = findViewById(R.id.layout_six);
        estimateAmount = findViewById(R.id.estimate_amount);
        actualAmount = findViewById(R.id.actual_amount);
        payBtn = findViewById(R.id.pay_btn);
        howTxt = findViewById(R.id.how_txt);
        ratingBar = findViewById(R.id.rating_bar);
        ratingSubmitBtn = findViewById(R.id.rating_submit_btn);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        userReview = findViewById(R.id.user_review_et);
    }
}