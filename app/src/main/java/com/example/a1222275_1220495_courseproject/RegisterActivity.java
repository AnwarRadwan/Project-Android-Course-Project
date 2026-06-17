package com.example.a1222275_1220495_courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etFirstName, etLastName, etPassword, etConfirmPassword, etPhone;
    private Spinner spinnerGender, spinnerCategory;
    private Button btnRegister;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DataBaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnRegister = findViewById(R.id.btnRegister);

        // Setup Spinners
        String[] genders = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        spinnerGender.setAdapter(genderAdapter);

        String[] categories = {"Technology", "Science", "Business", "Arts", "Engineering"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if (!validateInputs(email, firstName, lastName, password, confirmPassword, phone)) {
            return;
        }

        // Check if user already exists
        if (dbHelper.getUserByEmail(email) != null) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encrypt Password
        String encryptedPassword = hashPassword(password);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPassword(encryptedPassword);
        newUser.setGender(gender);
        newUser.setCategory(category);
        newUser.setPhone(phone);
        newUser.setImage(""); // Default empty image

        dbHelper.insertUser(newUser);

        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    private boolean validateInputs(String email, String fname, String lname, String pass, String confirmPass, String phone) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            return false;
        }
        if (fname.length() < 3) {
            etFirstName.setError("Minimum 3 characters");
            return false;
        }
        if (lname.length() < 3) {
            etLastName.setError("Minimum 3 characters");
            return false;
        }
        if (!phone.matches("^05\\d{8}$")) {
            etPhone.setError("Phone must be in format 05XXXXXXXX");
            return false;
        }
        if (pass.length() < 6 || !pass.matches(".*[a-zA-Z].*") || !pass.matches(".*\\d.*")) {
            etPassword.setError("Min 6 chars, 1 letter and 1 number");
            return false;
        }
        if (!pass.equals(confirmPass)) {
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
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
            return password; // Fallback (should not happen)
        }
    }
}
