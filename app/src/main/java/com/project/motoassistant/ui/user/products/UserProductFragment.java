package com.project.motoassistant.ui.user.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.motoassistant.GridSpacingItemDecoration;
import com.project.motoassistant.R;
import com.project.motoassistant.adapters.UserProductListAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProductFragment extends Fragment {


    private RecyclerView userProductRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_product, container, false);
        initView(view);
        apiCall();

        return view;
    }


    public void apiCall() {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.VIEW_PRODUCT_USER_API_CALL().enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        // Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                        userProductRv.setLayoutManager(layoutManager);
                        UserProductListAdapter userProductListAdapter = new UserProductListAdapter(root, getActivity());

                        int spanCount = 2; // 3 columns
                        int spacing = 50; // 50px
                        boolean includeEdge = true;
                        userProductRv.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                        userProductRv.setAdapter(userProductListAdapter);

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
        userProductRv = view.findViewById(R.id.user_product_rv);
    }
}