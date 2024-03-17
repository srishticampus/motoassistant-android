package com.project.motoassistant.ui.seller.seller.seller_home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.project.motoassistant.GridSpacingItemDecoration;
import com.project.motoassistant.R;
import com.project.motoassistant.SellerAddProductActivity;
import com.project.motoassistant.adapters.SellerProductListAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellerHome extends Fragment {


    private RelativeLayout toolbarLayout;
    private RecyclerView sellerProductRv;
    private ExtendedFloatingActionButton extendedFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);
        initView(view);

        SharedPreferences sh = getActivity().getSharedPreferences("seller_login_data", MODE_PRIVATE);
        String sellerId = sh.getString("sellerId", "");

        apiCall(sellerId);

        extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SellerAddProductActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView(View view) {
        toolbarLayout = view.findViewById(R.id.toolbar_layout);
        sellerProductRv = view.findViewById(R.id.seller_product_rv);
        extendedFab = view.findViewById(R.id.extended_fab);
    }

    public void apiCall(String sellerId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.SELLER_PRODUCT_LIST_API(sellerId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        // Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                        sellerProductRv.setLayoutManager(layoutManager);
                        SellerProductListAdapter sellerProductListAdapter = new SellerProductListAdapter(root, getActivity());

                        int spanCount = 2; // 3 columns
                        int spacing = 50; // 50px
                        boolean includeEdge = true;
                        sellerProductRv.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                        sellerProductRv.setAdapter(sellerProductListAdapter);

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