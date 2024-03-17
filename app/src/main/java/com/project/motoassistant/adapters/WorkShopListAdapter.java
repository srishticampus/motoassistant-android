package com.project.motoassistant.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project.motoassistant.R;
import com.project.motoassistant.ServiceBookingActivity;
import com.project.motoassistant.models.Root;

public class WorkShopListAdapter extends RecyclerView.Adapter<WorkShopListAdapter.MyViewHolder> {

    Root root;
    Context context;
    String serviceId,serviceName;


    public WorkShopListAdapter(Root root, Context context, String serviceId, String serviceName) {
        this.root = root;
        this.context = context;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_workshop_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.workshopName.setText(root.workshopDetails.get(position).workshop_name);
        holder.workshopDistanceTv.setText(root.workshopDetails.get(position).distance);
        Glide.with(context).load(root.workshopDetails.get(position).file).into(holder.workshopImg);
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + root.workshopDetails.get(position).phone));//change the number
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
//                if (callIntent.resolveActivity(context.getPackageManager()) != null) {
//                    context.startActivity(callIntent);
//                }

            }
        });
        holder.emailBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("*/*");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, root.workshopDetails.get(position).email);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(emailIntent);
            }
        });
        holder.shopRating.setRating(Float.parseFloat(String.valueOf(root.workshopDetails.get(position).workshop_rating)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ServiceBookingActivity.class);
                intent.putExtra("workshopId",root.workshopDetails.get(position).id);
                intent.putExtra("serviceId",serviceId);
                intent.putExtra("serviceName",serviceName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.workshopDetails.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView workshopImg;
        private TextView workshopName;
        private TextView workshopAddress;
        private TextView workshopDistanceTv;
        private AppCompatRatingBar shopRating;
        private RelativeLayout callBtn;
        private RelativeLayout emailBt;
        TextView serviceRequestBt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            workshopImg = itemView.findViewById(R.id.workshop_img);
            workshopName = itemView.findViewById(R.id.workshop_name);
            workshopAddress = itemView.findViewById(R.id.workshop_address);
            workshopDistanceTv = itemView.findViewById(R.id.workshop_distance_tv);
            shopRating = itemView.findViewById(R.id.shop_rating);
            callBtn = itemView.findViewById(R.id.call_btn);
            emailBt = itemView.findViewById(R.id.email_bt);
            serviceRequestBt = itemView.findViewById(R.id.request_service_bt);
        }
    }
}
