package com.example.a1222275_1220495_courseproject.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1222275_1220495_courseproject.R;
import com.google.android.material.button.MaterialButton;

// Fragment for contact information
public class ContactUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        MaterialButton btnCallUs = view.findViewById(R.id.btnCallUs);
        MaterialButton btnLocateUs = view.findViewById(R.id.btnLocateUs);
        MaterialButton btnEmailUs = view.findViewById(R.id.btnEmailUs);

        // Set click listeners
        btnCallUs.setOnClickListener(v -> callUs());
        btnLocateUs.setOnClickListener(v -> locateUs());
        btnEmailUs.setOnClickListener(v -> emailUs());

        return view;
    }

    // Handle call action
    private void callUs() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+970599123456"));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle maps action
    private void locateUs() {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Nablus, Palestine");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Intent genericMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(genericMapIntent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle email action
    private void emailUs() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:travelplanner@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Travel Planner Inquiry");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
