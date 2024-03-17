package com.project.motoassistant.ui.user.order_history_product;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.motoassistant.R;
import com.project.motoassistant.adapters.UserProductOrderHistoryAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserOrderHistoryProductsFragments extends Fragment {


    private RecyclerView userRecentProductOrdersRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_order_history_products_fragments, container, false);
        initView(view);

        SharedPreferences sh = getContext().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sh.getString("userId", "");

        apiCall(userId);

        return view;
    }

    public void apiCall(String userId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.USER_PRODUCT_ORDER_HISTORY(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        userRecentProductOrdersRv.setLayoutManager(linearLayoutManager);
                        UserProductOrderHistoryAdapter userProductOrderHistoryAdapter = new UserProductOrderHistoryAdapter(root, getContext());
                        userRecentProductOrdersRv.setAdapter(userProductOrderHistoryAdapter);

                    } else {
                        Toast.makeText(getActivity(), root.message, Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void initView(View view) {
        userRecentProductOrdersRv = view.findViewById(R.id.user_recent_product_orders_rv);
    }
}