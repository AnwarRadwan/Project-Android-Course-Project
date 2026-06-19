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

public class ContactUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        MaterialButton btnCallUs = view.findViewById(R.id.btnCallUs);
        MaterialButton btnLocateUs = view.findViewById(R.id.btnLocateUs);
        MaterialButton btnEmailUs = view.findViewById(R.id.btnEmailUs);

        // Implementation of Call Us functionality
        btnCallUs.setOnClickListener(v -> callUs());

        // Implementation of Locate Us functionality
        btnLocateUs.setOnClickListener(v -> locateUs());

        // Implementation of Email Us functionality
        btnEmailUs.setOnClickListener(v -> emailUs());

        return view;
    }

    private void callUs() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+970599123456"));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No dialer application found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void locateUs() {
        try {
            // Using geo URI for Nablus, Palestine
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=Nablus, Palestine");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Attempt to open specifically with Google Maps if available
            mapIntent.setPackage("com.google.android.apps.maps");
            
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // If Google Maps is not installed, try a general VIEW intent
                Intent genericMapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(genericMapIntent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No maps application found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void emailUs() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:travelplanner@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Travel Planner Inquiry");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No email application found.", Toast.LENGTH_SHORT).show();
        }
    }
}
