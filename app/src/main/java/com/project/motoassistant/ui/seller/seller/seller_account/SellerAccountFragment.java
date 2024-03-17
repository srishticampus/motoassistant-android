package com.project.motoassistant.ui.seller.seller.seller_account;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mobsandgeeks.saripaar.Validator;
import com.project.motoassistant.MainActivity;
import com.project.motoassistant.R;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import java.io.File;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerAccountFragment extends Fragment {

    private LinearLayout linearLayoutEditProfile;
    private CircleImageView profilePicture;
    private LinearLayout imageEditProfile;
    private ImageView imageSelect;
    private EditText fullNameEditProfile;
    private EditText emailIdEditProfile;
    private EditText phoneNumberEditProfile;
    private EditText dateOfBirthEditProfile;
    private EditText addressEditProfile;
    private TextView submitButtonEditProfile, logoutButton;
    private File proImageFile;
    private Validator validator;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int RESULT_LOAD_PRO_IMAGE = 106;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_account, container, false);
        initView(view);

        SharedPreferences sh = getActivity().getSharedPreferences("seller_login_data", MODE_PRIVATE);
        String sellerId = sh.getString("sellerId", "");


        apiCall(sellerId);

        imageEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_PRO_IMAGE);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutApiCall(sellerId);
            }
        });


        return view;
    }

    private void apiCall(String sellerId) {
        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.SELLER_PROFILE(sellerId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {

                        fullNameEditProfile.setText(root.sellerDetails.get(0).name);
                        emailIdEditProfile.setText(root.sellerDetails.get(0).email);
                        phoneNumberEditProfile.setText(root.sellerDetails.get(0).phone);
                        addressEditProfile.setText(root.sellerDetails.get(0).address);
                        Glide.with(getContext()).load(root.sellerDetails.get(0).image).into(profilePicture);

                        submitButtonEditProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateApiCall(sellerId);
                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), root.message, Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void logOutApiCall(String userId) {


        SharedPreferences sP = getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
        SharedPreferences.Editor speditor = sP.edit();
        speditor.putBoolean("seller_session", false);
        speditor.commit();

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
//        api.LOGOUT_API_CALL(userId).enqueue(new Callback<Root>() {
//            @Override
//            public void onResponse(Call<Root> call, Response<Root> response) {
//                if (response.isSuccessful()) {
//                    Root root = response.body();
//                    if (root.status) {
//                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
//                        SharedPreferences sP = getContext().getSharedPreferences("login_pref", MODE_PRIVATE);
//                        SharedPreferences.Editor speditor = sP.edit();
//                        speditor.putBoolean("seller_session", false);
//                        speditor.commit();
//
//                        Intent intent = new Intent(getContext(), MainActivity.class);
//                        startActivity(intent);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Root> call, Throwable t) {
//
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void updateApiCall(String sellerId) {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), fullNameEditProfile.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), phoneNumberEditProfile.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailIdEditProfile.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), addressEditProfile.getText().toString());
        RequestBody rSellerId = RequestBody.create(MediaType.parse("text/plain"), sellerId);
        MultipartBody.Part proImageFilePart = null;

        try {
            proImageFilePart = MultipartBody.Part.createFormData("image", proImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), proImageFile));

        } catch (NullPointerException e) {

        }

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.UPDATE_SELLER_PROFILE(name, email, phone, address, proImageFilePart, rSellerId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(getContext(), root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Server Error!!Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getContext(), "Server Error!!Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getContext(), "Permission Already granted", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
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
                Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                proImageFile = new File(picturePath);

                final InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                final Bitmap selectedImageBit = BitmapFactory.decodeStream(imageStream);
                profilePicture.setImageBitmap(selectedImageBit);

            } catch (Exception e) {

            }
        }
    }

    private void initView(View view) {
        linearLayoutEditProfile = view.findViewById(R.id.linear_layout_edit_profile);
        profilePicture = view.findViewById(R.id.profile_picture);
        imageEditProfile = view.findViewById(R.id.image_edit_profile);
        imageSelect = view.findViewById(R.id.image_select);
        fullNameEditProfile = view.findViewById(R.id.fullName_editProfile);
        emailIdEditProfile = view.findViewById(R.id.emailId_editProfile);
        phoneNumberEditProfile = view.findViewById(R.id.phoneNumber_editProfile);
        dateOfBirthEditProfile = view.findViewById(R.id.dateOfBirth_editProfile);
        addressEditProfile = view.findViewById(R.id.address_editProfile);
        submitButtonEditProfile = view.findViewById(R.id.submit_button_editProfile);
        logoutButton = view.findViewById(R.id.logout_button_editProfile);
    }
}