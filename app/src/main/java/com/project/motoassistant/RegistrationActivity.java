package com.project.motoassistant;

import static com.project.motoassistant.Utils.changeStatusBarColor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements Validator.ValidationListener {

    private ScrollView scrollView;
    private CircleImageView profileImage;
    private CircleImageView editProImg;
    private TextView tvOne;
    private LinearLayout etBoxOne;
    private File proImageFile;
    private Validator validator;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;

    String color = "#FFFFFF";


    @NotEmpty
    private TextInputEditText nameEt;

    private LinearLayout etBoxTwo;

   // @Pattern(regex = "^[7-9][0-9]{9}$", message = "Invalid Mobile Number")
   @NotEmpty
   @Length(max = 10, min = 10, message = "Enter a valid mobile number")
    private TextInputEditText phoneNumberEt;

    private LinearLayout etBoxThree;

    @NotEmpty
    @Email
    private TextInputEditText emailEt;
    private LinearLayout etBoxFour;

    @NotEmpty
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private TextInputEditText passwordEt;

    private LinearLayout etBoxFive;

    @NotEmpty
    @ConfirmPassword
    private TextInputEditText confirmPasswordEt;
    private LinearLayout etBoxSix;

    @NotEmpty
    private TextInputEditText addressEt;
    private TextView signUpBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       // changeStatusBarColor(RegistrationActivity.this,color);

        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);

        editProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });


        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();

            }
        });


    }

    private void initView() {
        scrollView = findViewById(R.id.scroll_view);
        profileImage = findViewById(R.id.profile_image);
        editProImg = findViewById(R.id.edit_pro_img);
        tvOne = findViewById(R.id.tv_one);
        etBoxOne = findViewById(R.id.et_box_one);
        nameEt = findViewById(R.id.name_et);
        etBoxTwo = findViewById(R.id.et_box_two);
        phoneNumberEt = findViewById(R.id.phone_number_et);
        etBoxThree = findViewById(R.id.et_box_three);
        emailEt = findViewById(R.id.email_et);
        etBoxFour = findViewById(R.id.et_box_four);
        passwordEt = findViewById(R.id.password_et);
        etBoxFive = findViewById(R.id.et_box_five);
        confirmPasswordEt = findViewById(R.id.confirm_password_et);
        etBoxSix = findViewById(R.id.et_box_six);
        addressEt = findViewById(R.id.address_et);
        signUpBt = findViewById(R.id.sign_up_bt);
    }

    @Override
    public void onValidationSucceeded() {

        // Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
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
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), nameEt.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneNumberEt.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailEt.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), confirmPasswordEt.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), addressEt.getText().toString());
        MultipartBody.Part proImageFilePart = null;

        try {
            proImageFilePart = MultipartBody.Part.createFormData("image", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

        } catch (NullPointerException e) {

        }
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.REGROOTCALL(name, email, phone, password, address, proImageFilePart).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), root.message, Toast.LENGTH_SHORT).show();
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


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{permission}, requestCode);
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
                profileImage.setImageBitmap(selectedImageBit);

            } catch (Exception e) {

            }
        }
    }

}