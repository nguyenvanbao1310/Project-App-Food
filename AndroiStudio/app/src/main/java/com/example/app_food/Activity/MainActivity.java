package com.example.app_food.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_food.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        // Nhận username từ Intent
        String username = getIntent().getStringExtra("username");

        // Hiển thị username trên giao diện
        TextView usernameTextView = findViewById(R.id.tvUserGreeting);
        usernameTextView.setText("Hi!, " + username + "!");
    }
}