package com.project.motoassistant.adapters;

import android.content.Context;
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
import com.project.motoassistant.models.Root;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    Root root;
    Context context;


    public ReviewAdapter(Root root, Context context) {
        this.root = root;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_review_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.reviewerName.setText(root.reviewDetails.get(holder.getAdapterPosition()).username);
        Glide.with(context).load(root.reviewDetails.get(position).profilePic).into(holder.reviewerImg);
        holder.reviewedDate.setText(root.reviewDetails.get(holder.getAdapterPosition()).date);
        holder.userReview.setText(root.reviewDetails.get(position).review);
        holder.userRating.setRating(Float.parseFloat(root.reviewDetails.get(position).count));

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout layoutOne;
        private ShapeableImageView reviewerImg;
        private TextView reviewerName;
        private RelativeLayout layoutTwo;
        private AppCompatRatingBar userRating;
        private TextView reviewedDate;
        private TextView userReview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }

        private void initView(View itemView) {
            layoutOne = itemView.findViewById(R.id.layout_one);
            reviewerImg = itemView.findViewById(R.id.reviewer_img);
            reviewerName = itemView.findViewById(R.id.reviewer_name);
            layoutTwo = itemView.findViewById(R.id.layout_two);
            userRating = itemView.findViewById(R.id.user_rating);
            reviewedDate = itemView.findViewById(R.id.reviewed_date);
            userReview = itemView.findViewById(R.id.user_review);
        }
    }
}
