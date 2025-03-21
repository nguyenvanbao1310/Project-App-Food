package com.example.app_food.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_food.R;
import com.example.app_food.model.User;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAccountActivity extends AppCompatActivity {


    private EditText emailInput, passwordInput,passwordConfirmInput, usernameInput;
    private TextView otpMessage;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_account);

        // Xử lý toàn màn hình
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy view từ layout
        View signupCard = findViewById(R.id.signup_card);
        View otpCard = findViewById(R.id.otp_card);
        View createAccountButton = findViewById(R.id.btn_create_account);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        passwordConfirmInput=findViewById(R.id.confirm_password_input);
        usernameInput = findViewById(R.id.name_input);
        ImageView passwordToggle = findViewById(R.id.password_toggle);
        ImageView passwordToggleConfirm = findViewById(R.id.confirm_password_toggle);

        // Ẩn OTP Card ban đầu
        otpCard.setVisibility(View.INVISIBLE);

        // Xử lý sự kiện hiển thị/ẩn mật khẩu
        togglePasswordVisibility(passwordInput, passwordToggle);
        togglePasswordVisibility(passwordConfirmInput, passwordToggleConfirm);

        // Sự kiện nhấn nút "Create Account"
        createAccountButton.setOnClickListener(v -> {
            if (validateInput()) {
                String email = emailInput.getText().toString().trim();
                otpMessage.setText("Code sent to " + email + ". This code will expire in 01:30");
                showOTPForm(signupCard, otpCard);
            }
        });
    }
    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String passwordConfirm = passwordConfirmInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();

        // Kiểm tra xác nhận mật khẩu
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(RegisterAccountActivity.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(username, email, password, false);
        Call<Map<String, String>> call = apiService.signUpPostForm(user);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        String message = response.body().get("message");
                        Log.d("API_RESPONSE", "Response: " + message);
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,VerifyCodeActivity.class);
                        intent.putExtra("email", user.getEmail()); // Gửi userId sang MessListActivity
                        startActivity(intent);
                    } else {
                        String errorBody = response.errorBody().string();
                        Log.e("API_RESPONSE", "Error: " + errorBody);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại! " + errorBody, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                Log.e("Register", "Error: " + t.getMessage());
            }
        });
    }
    private boolean validateInput() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String passwordConfirm = passwordConfirmInput.getText().toString().trim();
        boolean isValid = true;

        // Kiểm tra Email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email or already in use!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Kiểm tra Password
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if(!password.equals(passwordConfirm)){
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }


        return isValid;
    }

    private void togglePasswordVisibility(EditText passwordField, ImageView toggleIcon) {
        toggleIcon.setOnClickListener(v -> {
            if (passwordField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleIcon.setImageResource(R.drawable.hidden); // Đổi sang icon hidden
            } else {
                passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleIcon.setImageResource(R.drawable.eye); // Đổi lại icon eye
            }
            passwordField.setSelection(passwordField.getText().length());
        });
    }

    private void showOTPForm(View signupCard, View otpCard) {
        // Animation SignUp trượt sang trái + mờ dần
        ObjectAnimator slideLeft = ObjectAnimator.ofFloat(signupCard, "translationX", 0f, -signupCard.getWidth());
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(signupCard, "alpha", 1f, 0f);

        // Animation OTP Card trượt vào từ phải + hiện dần
        ObjectAnimator slideIn = ObjectAnimator.ofFloat(otpCard, "translationX", otpCard.getWidth(), 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(otpCard, "alpha", 0f, 1f);

        // Gom animation SignUp lại
        AnimatorSet hideSignUp = new AnimatorSet();
        hideSignUp.playTogether(slideLeft, fadeOut);
        hideSignUp.setDuration(500);

        // Gom animation OTP lại
        AnimatorSet showOTP = new AnimatorSet();
        showOTP.playTogether(slideIn, fadeIn);
        showOTP.setDuration(500);

        // Chạy Animation
        hideSignUp.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                signupCard.setVisibility(View.GONE);
                otpCard.setVisibility(View.VISIBLE);
                showOTP.start();
            }
        });

        hideSignUp.start();
    }
}