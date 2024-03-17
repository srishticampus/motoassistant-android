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
import com.project.motoassistant.SellerProductDetails;
import com.project.motoassistant.UserProductDetails;
import com.project.motoassistant.models.Root;

public class UserProductListAdapter extends RecyclerView.Adapter<UserProductListAdapter.MyViewHolder> {

    Root root;
    Context context;


    public UserProductListAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout_user_product_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productName.setText(root.productDetails.get(position).name);
        Glide.with(context).load(root.productDetails.get(position).image).into(holder.productIv);
        holder.productPrice.setText(root.productDetails.get(position).selling_price);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserProductDetails.class);
                intent.putExtra("product_id", root.productDetails.get(position).id);
                intent.putExtra("seller_id", root.productDetails.get(position).seller_id);
                intent.putExtra("product_name", root.productDetails.get(position).name);
                intent.putExtra("product_quantity", root.productDetails.get(position).quantity);
                intent.putExtra("product_description", root.productDetails.get(position).description);
                intent.putExtra("product_price", root.productDetails.get(position).selling_price);
                intent.putExtra("product_image", root.productDetails.get(position).image);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return root.productDetails.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView productIv;
        private TextView productName;
        private TextView productPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
        private void initView(View itemView) {
            productIv = itemView.findViewById(R.id.product_iv);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
        }

    }
}
