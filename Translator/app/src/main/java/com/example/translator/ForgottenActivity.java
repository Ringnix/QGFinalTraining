package com.example.translator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgottenActivity extends AppCompatActivity {
    private EditText newUsernameEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button updatePasswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotten);
        newUsernameEditText = findViewById(R.id.new_username);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        updatePasswordButton = findViewById(R.id.update_pwd);
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newUsernameEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    // Update the password in the database
                    int result = UserDbHelper.getInstance(ForgottenActivity.this).updatePwd(username, newPassword);
                    if (result > 0) {
                        Toast.makeText(ForgottenActivity.this, "密码修改成功，请重新登陆", Toast.LENGTH_SHORT).show();
                        //回传的时候需要startactivityforresult启动
                        setResult(1000);
                        finish();
                        // Redirect to the login screen or finish the activity
                    } else {
                        Toast.makeText(ForgottenActivity.this, "error：密码更新失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgottenActivity.this, "question：两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.toback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}