package com.project.motoassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.project.motoassistant.R;

import java.util.Objects;

public class HomeViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    int[] images;

    public HomeViewPagerAdapter(Context mContext, int[] images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
