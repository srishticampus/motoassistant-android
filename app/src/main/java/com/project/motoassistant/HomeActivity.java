package com.project.motoassistant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.motoassistant.ui.user.order_history_product.UserOrderHistoryProductsFragments;
import com.project.motoassistant.ui.user.products.UserProductFragment;
import com.project.motoassistant.ui.user.user_account.AccountFragment;
import com.project.motoassistant.ui.user.user_home.HomeFragment;
import com.project.motoassistant.ui.user.user_order.RecentOrderFragment;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    String color = "#FFFFFF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //changeStatusBarColor(HomeActivity.this,color);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.user_home);


    }

    HomeFragment homeFragment = new HomeFragment();
    RecentOrderFragment recentOrderFragment = new RecentOrderFragment();
    AccountFragment accountFragment = new AccountFragment();

    UserOrderHistoryProductsFragments userOrderHistoryProductsFragments = new UserOrderHistoryProductsFragments();

    UserProductFragment userProductFragment = new UserProductFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.user_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            case R.id.user_products:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userProductFragment).commit();
                return true;
            case R.id.user_recent_product_order:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, userOrderHistoryProductsFragments).commit();
                return true;
            case R.id.user_recent_workshop_order:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, recentOrderFragment).commit();
                return true;
            case R.id.user_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Do You Want To Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}