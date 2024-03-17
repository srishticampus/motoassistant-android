package com.project.motoassistant.ui.user.user_home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.motoassistant.GridSpacingItemDecoration;
import com.project.motoassistant.R;
import com.project.motoassistant.adapters.HomeServiceAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private RelativeLayout toolbar;
    private TextView welcomeTxt;
    private TextView userName;
    private CircleImageView homeProPic;
    private LinearLayout txtLayout;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);


        SharedPreferences sh=getActivity().getSharedPreferences("login_data",MODE_PRIVATE);
        String name=sh.getString("name","user");
        String image=sh.getString("image","");

        userName.setText(name);
        Glide.with(getContext()).load(image).into(homeProPic);

        apiCall();

        return view;
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        welcomeTxt = view.findViewById(R.id.welcome_txt);
        userName = view.findViewById(R.id.user_name);
        homeProPic = view.findViewById(R.id.home_pro_pic);
        txtLayout = view.findViewById(R.id.txt_layout);
        txt1 = view.findViewById(R.id.txt_1);
        txt2 = view.findViewById(R.id.txt_2);
        txt3 = view.findViewById(R.id.txt_3);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    public void apiCall() {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);

        api.SERVICELISTCALL().enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                       // Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                        recyclerView.setLayoutManager(layoutManager);
                        HomeServiceAdapter homeServiceAdapter = new HomeServiceAdapter(root, getActivity());

                        int spanCount = 2; // 3 columns
                        int spacing = 50; // 50px
                        boolean includeEdge = true;
                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                        recyclerView.setAdapter(homeServiceAdapter);

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