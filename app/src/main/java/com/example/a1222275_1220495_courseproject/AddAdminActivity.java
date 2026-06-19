package com.example.a1222275_1220495_courseproject;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1222275_1220495_courseproject.database.DataBaseHelper;
import com.example.a1222275_1220495_courseproject.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddAdminActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etFirstName, etLastName, etPassword, etPhone;
    private MaterialButton btnSaveAdmin;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        dbHelper = new DataBaseHelper(this);

        etEmail = findViewById(R.id.etAdminEmail);
        etFirstName = findViewById(R.id.etAdminFirstName);
        etLastName = findViewById(R.id.etAdminLastName);
        etPassword = findViewById(R.id.etAdminPassword);
        etPhone = findViewById(R.id.etAdminPhone);
        btnSaveAdmin = findViewById(R.id.btnSaveAdmin);

        btnSaveAdmin.setOnClickListener(v -> saveAdmin());
    }

    private void saveAdmin() {
        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (!validateInputs(email, firstName, lastName, password, phone)) {
            return;
        }

        if (dbHelper.getUserByEmail(email) != null) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = hashPassword(password);
        User newAdmin = new User(0, email, firstName, lastName, hashedPassword, "", "Admin", phone, "");
        dbHelper.insertUser(newAdmin);

        Toast.makeText(this, "Admin account created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateInputs(String email, String fn, String ln, String pass, String phone) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            return false;
        }
        if (fn.length() < 3) {
            etFirstName.setError("First name must be at least 3 characters");
            return false;
        }
        if (ln.length() < 3) {
            etLastName.setError("Last name must be at least 3 characters");
            return false;
        }
        if (pass.length() < 6 || !pass.matches(".*[a-zA-Z].*") || !pass.matches(".*\\d.*")) {
            etPassword.setError("Min 6 chars, 1 letter and 1 number");
            return false;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Phone required");
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
            return password;
        }
    }
}
