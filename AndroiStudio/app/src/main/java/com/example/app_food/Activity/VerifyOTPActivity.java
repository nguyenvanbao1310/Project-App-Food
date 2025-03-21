package com.example.app_food.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_food.R;
import com.example.app_food.Retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity {

    private EditText editOtp1,editOtp2,editOtp3,editOtp4;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);
        editOtp1 = findViewById(R.id.otp1);
        editOtp2 = findViewById(R.id.otp2);
        editOtp3 = findViewById(R.id.otp3);
        editOtp4 = findViewById(R.id.otp4);


        Button verifyButton = findViewById(R.id.btn_confirm);
        email = getIntent().getStringExtra("email");
        verifyButton.setOnClickListener(v -> verifyOTP());
    }
    private void verifyOTP() {
        // Ghép 4 số OTP thành 1 chuỗi
        String otp = editOtp1.getText().toString().trim() +
                editOtp2.getText().toString().trim() +
                editOtp3.getText().toString().trim() +
                editOtp4.getText().toString().trim();

        if (otp.length() < 4) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ mã OTP!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi dữ liệu xác thực OTP
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", otp);
        requestBody.put("email", email);

        RetrofitClient.getApiUserService().verifyCode(requestBody).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().get("message");
                    Log.d("VerifyCode", "Server response: " + response.body());

                    if ("success".equals(message)) {
                        Toast.makeText(VerifyOTPActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, "Xác thực thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(VerifyOTPActivity.this, "Lỗi từ server!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(VerifyOTPActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}