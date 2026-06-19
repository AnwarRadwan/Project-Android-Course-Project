package com.example.a1222275_1220495_courseproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.a1222275_1220495_courseproject.R;
import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileFragment extends Fragment {

    private ShapeableImageView imgProfile;
    private FloatingActionButton fabEditImage;
    private TextView tvEmail, tvGender, tvCategory;
    private TextInputEditText etFirstName, etLastName, etPhone, etNewPassword, etConfirmPassword;
    private MaterialButton btnUpdateProfile, btnUpdatePassword;

    private DataBaseHelper dbHelper;
    private long userId;
    private User currentUser;
    private String selectedImageUriStr = "";

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImageUriStr = imageUri.toString();
                        Glide.with(this).load(imageUri).into(imgProfile);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DataBaseHelper(getContext());
        loadUserId();

        initViews(view);
        loadUserData();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        imgProfile = view.findViewById(R.id.imgProfile);
        fabEditImage = view.findViewById(R.id.fabEditImage);
        tvEmail = view.findViewById(R.id.tvProfileEmail);
        tvGender = view.findViewById(R.id.tvProfileGender);
        tvCategory = view.findViewById(R.id.tvProfileCategory);
        etFirstName = view.findViewById(R.id.etProfileFirstName);
        etLastName = view.findViewById(R.id.etProfileLastName);
        etPhone = view.findViewById(R.id.etProfilePhone);
        etNewPassword = view.findViewById(R.id.etProfileNewPassword);
        etConfirmPassword = view.findViewById(R.id.etProfileConfirmNewPassword);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
    }

    private void loadUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);
    }

    private void loadUserData() {
        if (userId != -1) {
            currentUser = dbHelper.getUserById(userId);
            if (currentUser != null) {
                tvEmail.setText(currentUser.getEmail());
                tvGender.setText(currentUser.getGender());
                tvCategory.setText(currentUser.getCategory());
                etFirstName.setText(currentUser.getFirstName());
                etLastName.setText(currentUser.getLastName());
                etPhone.setText(currentUser.getPhone());
                
                selectedImageUriStr = currentUser.getImage();
                if (selectedImageUriStr != null && !selectedImageUriStr.isEmpty()) {
                    Glide.with(this).load(Uri.parse(selectedImageUriStr)).into(imgProfile);
                } else {
                    // Fixed: Use android.R.drawable to reference the system camera icon
                    imgProfile.setImageResource(android.R.drawable.ic_menu_camera);
                }
            }
        }
    }

    private void setupListeners() {
        fabEditImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        btnUpdatePassword.setOnClickListener(v -> updatePassword());
    }

    private void updateProfile() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (firstName.length() < 3) {
            etFirstName.setError("Minimum 3 characters");
            return;
        }
        if (lastName.length() < 3) {
            etLastName.setError("Minimum 3 characters");
            return;
        }
        if (!phone.matches("^05\\d{8}$")) {
            etPhone.setError("Format: 05XXXXXXXX");
            return;
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setPhone(phone);
        currentUser.setImage(selectedImageUriStr);

        dbHelper.updateUser(currentUser);
        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void updatePassword() {
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (newPass.isEmpty()) {
            etNewPassword.setError("Enter new password");
            return;
        }

        if (newPass.length() < 6 || !newPass.matches(".*[a-zA-Z].*") || !newPass.matches(".*\\d.*")) {
            etNewPassword.setError("Min 6 chars, 1 letter and 1 number");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        String hashedPass = hashPassword(newPass);
        dbHelper.updateUserPassword(userId, hashedPass);
        
        etNewPassword.setText("");
        etConfirmPassword.setText("");
        Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}
