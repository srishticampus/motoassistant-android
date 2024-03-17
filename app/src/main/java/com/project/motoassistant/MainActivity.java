package com.project.motoassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {

    private ImageView loginImg;
    private TextView tvOne;
    private LinearLayout etBoxOne;
    // @Pattern(regex = "^[7-9][0-9]{9}$", message = "Invalid Mobile Number")
    //    @NotEmpty
//    @Min(value = 10)
//    @Or
//    @Max(value = 10)
    @Length(max = 10, min = 10, message = "Enter a valid mobile number")
    private TextInputEditText phoneNumberEt;
    private LinearLayout passwordLayout;
    private TextInputLayout etBoxTwo;
    @NotEmpty
    private TextInputEditText passwordEt;
    private TextView loginBt;
    private TextView regBt;
    private Validator validator;
    private RelativeLayout loginLayout;
    private LinearLayout sellerToggleLayout;
    private SwitchMaterial sellerLoginToggle;
    private LinearLayout regLayout;
    private TextView sellerRegBt;

    Boolean toggleStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // changeStatusBarColor(MainActivity.this,color);

        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);

        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
        sellerRegBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SellerRegistrationActivity.class));
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        sellerLoginToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    toggleStatus = true;
                }else {
                    toggleStatus = false;
                }
            }
        });

    }

    private void initView() {
        loginImg = findViewById(R.id.login_img);
        tvOne = findViewById(R.id.tv_one);
        etBoxOne = findViewById(R.id.et_box_one);
        phoneNumberEt = findViewById(R.id.phone_number_et);
        passwordLayout = findViewById(R.id.password_layout);
        etBoxTwo = findViewById(R.id.et_box_two);
        passwordEt = findViewById(R.id.password_et);
        loginBt = findViewById(R.id.login_bt);
        regBt = findViewById(R.id.reg_bt);
        loginLayout = findViewById(R.id.login_layout);
        sellerToggleLayout = findViewById(R.id.seller_toggle_layout);
        sellerLoginToggle = findViewById(R.id.seller_login_toggle);
        regLayout = findViewById(R.id.reg_layout);
        sellerRegBt = findViewById(R.id.seller_reg_bt);
    }

    @Override
    public void onValidationSucceeded() {

        if (toggleStatus) {
            // Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
            sellerLoginApiCall();
        } else {
            apiCall();
        }


        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }

    }

    public void apiCall() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("token", getApplicationContext().MODE_PRIVATE);
        String deviceToken = sharedPreferences.getString("token", "");
        // Toast.makeText(this, deviceToken, Toast.LENGTH_SHORT).show();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.LOGINROOTCALL(phoneNumberEt.getText().toString(), passwordEt.getText().toString(), deviceToken).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", root.userDetails.get(0).name);
                        editor.putString("phone", root.userDetails.get(0).phone);
                        editor.putString("email", root.userDetails.get(0).email);
                        editor.putString("address", root.userDetails.get(0).address);
                        editor.putString("image", root.userDetails.get(0).image);
                        editor.putString("userId", root.userDetails.get(0).id);
                        editor.commit();

                        SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                        SharedPreferences.Editor speditor = sP.edit();
                        speditor.putBoolean("user_session", true);
                        speditor.commit();

                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String message = "Server Error";
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                    //Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                String message = "Server Error";
                Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();

            }
        });
    }

    public void sellerLoginApiCall() {


        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.SELLER_LOGIN_API(phoneNumberEt.getText().toString(), passwordEt.getText().toString()).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("seller_login_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", root.sellerDetails.get(0).name);
                        editor.putString("phone", root.sellerDetails.get(0).phone);
                        editor.putString("email", root.sellerDetails.get(0).email);
                        editor.putString("address", root.sellerDetails.get(0).address);
                        editor.putString("image", root.sellerDetails.get(0).image);
                        editor.putString("sellerId", root.sellerDetails.get(0).id);
                        editor.commit();

                        SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                        SharedPreferences.Editor speditor = sP.edit();
                        speditor.putBoolean("seller_session", true);
                        speditor.commit();

                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String message = "Server Error";
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                    //Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                String message = "Server Error";
                Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();

            }
        });
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