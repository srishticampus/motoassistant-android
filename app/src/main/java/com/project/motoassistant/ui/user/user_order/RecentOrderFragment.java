package com.project.motoassistant.ui.user.user_order;

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
import com.project.motoassistant.adapters.RecentOrderAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecentOrderFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_order, container, false);
        initView(view);

        apiCall();

        return view;
    }

    public void apiCall() {

        SharedPreferences sh = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
        String userId = sh.getString("userId", "");
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.ORDER_HISTORY(userId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        RecentOrderAdapter recentOrderAdapter = new RecentOrderAdapter(getContext(), root);
                        recyclerView.setAdapter(recentOrderAdapter);
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
        recyclerView = view.findViewById(R.id.recycler_view);
    }
}