package com.project.motoassistant;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerAddProductActivity extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    private TextInputEditText productNameTv;
    @NotEmpty
    private TextInputEditText productMrpTv;
    @NotEmpty
    private TextInputEditText productSellingTv;
    @NotEmpty
    private TextInputEditText productQuantityTv;
    @NotEmpty
    private TextInputEditText productDescriptionTv;
    @NotEmpty
    private TextInputEditText productImageTv;

    private File proImageFile;
    private Validator validator;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;
    private TextView addProductBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);
        initView();

        validator = new Validator(this);
        validator.setValidationListener(this);


        productImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });

        addProductBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });


    }

    private void initView() {
        productNameTv = findViewById(R.id.product_name_tv);
        productMrpTv = findViewById(R.id.product_mrp_tv);
        productSellingTv = findViewById(R.id.product_selling_tv);
        productQuantityTv = findViewById(R.id.product_quantity_tv);
        productDescriptionTv = findViewById(R.id.product_description_tv);
        productImageTv = findViewById(R.id.product_image_tv);
        addProductBt = findViewById(R.id.add_product_bt);
    }

    @Override
    public void onValidationSucceeded() {
        apiCall();
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
        SharedPreferences sh = getApplicationContext().getSharedPreferences("seller_login_data", MODE_PRIVATE);
        String sellerId_ = sh.getString("sellerId", "");

        RequestBody productName = RequestBody.create(MediaType.parse("text/plain"), productNameTv.getText().toString());
        RequestBody mrp = RequestBody.create(MediaType.parse("text/plain"), productMrpTv.getText().toString());
        RequestBody sellingPrice = RequestBody.create(MediaType.parse("text/plain"), productSellingTv.getText().toString());
        RequestBody quantity = RequestBody.create(MediaType.parse("text/plain"), productQuantityTv.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), productDescriptionTv.getText().toString());
        RequestBody sellerId = RequestBody.create(MediaType.parse("text/plain"), sellerId_);

        MultipartBody.Part proImageFilePart = null;

        try {
            proImageFilePart = MultipartBody.Part.createFormData("image", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

        } catch (NullPointerException e) {

        }
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.SELLER_ADD_PRODUCT(productName, mrp, sellingPrice, quantity, description, proImageFilePart, sellerId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SellerAddProductActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(SellerAddProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(SellerAddProductActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getApplicationContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
        }

    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_PRO_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                proImageFile = new File(picturePath);

                final InputStream imageStream = getApplicationContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                // profileImage.setImageBitmap(selectedImageBit);
                productImageTv.setText(picturePath);


            } catch (Exception e) {

            }
        }
    }

}