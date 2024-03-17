package com.project.motoassistant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.motoassistant.R;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.ui.seller.seller.seller_order.SellerOrderDetailsActivity;

public class SellerOrderHistoryAdapter extends RecyclerView.Adapter<SellerOrderHistoryAdapter.MyViewHolder> {

    Root root;
    Context context;


    public SellerOrderHistoryAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_seller_order_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productNameTv.setText(root.orderDetails.get(position).name);
        holder.productPriceTv.setText(root.orderDetails.get(position).selling_price);
        holder.orderIdTv.setText(root.orderDetails.get(position).order_id);
        holder.orderedDateTv.setText(root.orderDetails.get(position).created_at);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellerOrderDetailsActivity.class);
                intent.putExtra("order_id", root.orderDetails.get(position).order_id);
                intent.putExtra("seller_id", root.orderDetails.get(position).seller_id);
                intent.putExtra("product_name", root.orderDetails.get(position).name);
                intent.putExtra("product_quantity", root.orderDetails.get(position).product_quantity);
                intent.putExtra("product_description", root.orderDetails.get(position).description);
                intent.putExtra("product_price", root.orderDetails.get(position).selling_price);
                intent.putExtra("product_image", root.orderDetails.get(position).image);
                intent.putExtra("product_purchase_date", root.orderDetails.get(position).created_at);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.orderDetails.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTv;
        private TextView productPriceTv;
        private TextView orderIdTv;
        private TextView orderedDateTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            productNameTv = itemView.findViewById(R.id.product_name_tv);
            productPriceTv = itemView.findViewById(R.id.product_price_tv);
            orderIdTv = itemView.findViewById(R.id.order_id_tv);
            orderedDateTv = itemView.findViewById(R.id.ordered_date_tv);
        }
    }
}
