package com.project.motoassistant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.motoassistant.R;
import com.project.motoassistant.WorkshopListActivity;
import com.project.motoassistant.models.Root;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeServiceAdapter extends RecyclerView.Adapter<HomeServiceAdapter.MyViewHolder> {

    Root root;
    Context context;

    public HomeServiceAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.serviceTxt.setText(root.serviceDetails.get(position).service_name);
        Glide.with(context).load(root.serviceDetails.get(position).file).into(holder.serviceImg);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, WorkshopListActivity.class);
                intent.putExtra("service_id",root.serviceDetails.get(position).id);
                intent.putExtra("service_name",root.serviceDetails.get(position).service_name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.serviceDetails.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView serviceImg;
        private TextView serviceTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
        private void initView(View itemView) {
            serviceImg = itemView.findViewById(R.id.service_img);
            serviceTxt = itemView.findViewById(R.id.service_txt);
        }
    }
}
