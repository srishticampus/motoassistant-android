package com.project.motoassistant;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.project.motoassistant.adapters.WorkShopListAdapter;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String serivceId, serviceName;
    private GpsTracker gpsTracker;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_list);
        // changeStatusBarColor(WorkshopListActivity.this,color);
        initView();


        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //  ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 101);
            } else {
                gpsTracker = new GpsTracker(getApplicationContext());
                if (gpsTracker.canGetLocation()) {

                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                    // apiCall("8.9076","77.0549");
                    //                   apiCall(String.valueOf(latitude),String.valueOf(longitude));
                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // locationTextView.setText(addresses.get(0).getLocality());

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        serivceId = getIntent().getStringExtra("service_id");
        serviceName = getIntent().getStringExtra("service_name");
      /*
      TODO change hardcoded lat and long
        apiCall(serivceId, String.valueOf(latitude), String.valueOf(longitude));
      */
        //Toast.makeText(this, serivceId, Toast.LENGTH_SHORT).show();
        apiCall(serivceId,String.valueOf(latitude) , String.valueOf(longitude));

    }

    public void apiCall(String serviceId, String latitude, String longitude) {

        // Toast.makeText(this, latitude + longitude, Toast.LENGTH_SHORT).show();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.WORKSHOPLISTCALL(latitude, longitude, serviceId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        // Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        WorkShopListAdapter workShopListAdapter = new WorkShopListAdapter(root, getApplicationContext(), serviceId, serviceName);
                        recyclerView.setAdapter(workShopListAdapter);

                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String message = "Server Error";
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.workshops), message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                String message = "Server Error";
                Snackbar snackbar = Snackbar.make(findViewById(R.id.workshops), message, Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();
               // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(WorkshopListActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getApplicationContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }
}