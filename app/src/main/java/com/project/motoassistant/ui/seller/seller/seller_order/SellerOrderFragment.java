package com.project.motoassistant.ui.seller.seller.seller_order;

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
import com.project.motoassistant.adapters.SellerOrderHistoryAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellerOrderFragment extends Fragment {


    private RecyclerView sellerRecentOrdersRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order, container, false);
        initView(view);

        SharedPreferences sh = getActivity().getSharedPreferences("seller_login_data", MODE_PRIVATE);
        String sellerId = sh.getString("sellerId", "");

        apiCall(sellerId);


        return view;
    }

    private void initView(View view) {
        sellerRecentOrdersRv = view.findViewById(R.id.seller_recent_orders_rv);
    }

    public void apiCall(String sellerId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.SELLER_ORDER_HISTORY_API(sellerId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        sellerRecentOrdersRv.setLayoutManager(linearLayoutManager);
                        SellerOrderHistoryAdapter sellerOrderHistoryAdapter = new SellerOrderHistoryAdapter(root, getContext());
                        sellerRecentOrdersRv.setAdapter(sellerOrderHistoryAdapter);

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

}