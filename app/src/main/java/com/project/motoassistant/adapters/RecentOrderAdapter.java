package com.project.motoassistant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.motoassistant.R;
import com.project.motoassistant.RecentOrderDetails;
import com.project.motoassistant.models.Root;

public class RecentOrderAdapter extends RecyclerView.Adapter<RecentOrderAdapter.MyViewHolder> {

    Context context;
    Root root;


    public RecentOrderAdapter(Context context, Root root) {
        this.context = context;
        this.root = root;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_order_details_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.serviceCenterName.setText(root.orderHistory.get(position).workshop_name);
        holder.serviceType.setText(root.orderHistory.get(position).service_name);
        if (root.orderHistory.get(position).completion_status.equals("Completed")){
            holder.orderStatusTxt.setText(root.orderHistory.get(position).completion_status);
            holder.orderStatusTxt.setTextColor(context.getResources().getColor(R.color.green));
        }else {
            holder.orderStatusTxt.setText(root.orderHistory.get(position).completion_status);
            holder.orderStatusTxt.setTextColor(context.getResources().getColor(R.color.orange));
        }
        holder.mechanicNameTxt.setText(root.orderHistory.get(position).staff_name);
        holder.mechanicMobNumber.setText(root.orderHistory.get(position).phone);
        holder.orderDateTxt.setText(root.orderHistory.get(position).accept_date);
        holder.estimateAmount.setText(root.orderHistory.get(position).estimated_price);
        holder.actualAmount.setText(root.orderHistory.get(position).final_price);
        holder.orderId.setText(root.orderHistory.get(position).request_id);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecentOrderDetails.class);
                intent.putExtra("userId",root.orderHistory.get(position).user_id);
                intent.putExtra("requestId",root.orderHistory.get(position).request_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.orderHistory.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout topLayout;
        private TextView orderId;
        private LinearLayout layoutTwo;
        private TextView serviceCenterName;
        private TextView serviceType;
        private LinearLayout layoutThree;
        private TextView orderStatusTxt;
        private RelativeLayout layoutFour;
        private TextView mechName;
        private TextView mechanicNameTxt;
        private TextView mechanicMobNumber;
        private LinearLayout layoutFive;
        private TextView orderDateTxt;
        private TextView orderSummaryBtn;
        private TextView estimateAmount;
        private TextView actualAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
        private void initView(View itemView) {
            topLayout = itemView.findViewById(R.id.top_layout);
            orderId = itemView.findViewById(R.id.order_id);
            layoutTwo = itemView.findViewById(R.id.layout_two);
            serviceCenterName = itemView.findViewById(R.id.service_center_name);
            serviceType = itemView.findViewById(R.id.service_type);
            layoutThree = itemView.findViewById(R.id.layout_three);
            orderStatusTxt = itemView.findViewById(R.id.order_status_txt);
            layoutFour = itemView.findViewById(R.id.layout_four);
            mechName = itemView.findViewById(R.id.mech_name);
            mechanicNameTxt = itemView.findViewById(R.id.mechanic_name_txt);
            mechanicMobNumber = itemView.findViewById(R.id.mechanic_mob_number);
            layoutFive = itemView.findViewById(R.id.layout_five);
            orderDateTxt = itemView.findViewById(R.id.order_date_txt);
            orderSummaryBtn = itemView.findViewById(R.id.order_summary_btn);
            estimateAmount = itemView.findViewById(R.id.estimate_amount);
            actualAmount = itemView.findViewById(R.id.actual_amount);
        }

    }
}
