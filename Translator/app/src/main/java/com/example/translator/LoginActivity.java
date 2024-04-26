package com.example.translator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
public class LoginActivity extends AppCompatActivity {
private EditText et_password;
private EditText et_username;
private boolean is_login;
private SharedPreferences mSharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        //初始化控件
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
        CheckBox checkbox = findViewById(R.id.checkbox);
        mSharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        is_login=mSharedPreferences.getBoolean("is_login",false);
        if(is_login){
            String username=mSharedPreferences.getString("username",null);
            String password=mSharedPreferences.getString("password",null);
            et_username.setText(username);
            et_password.setText(password);
            checkbox.setChecked(true);
        }
        //点击注册
        findViewById(R.id.forget).setOnClickListener(new View.OnClickListener() {
            @Override
            //忘记密码
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgottenActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //跳转注册
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.et_login).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(username)&&TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"请输入用户名和密码",Toast.LENGTH_SHORT).show();
                }else {
                    UserInfo login = UserDbHelper.getInstance(LoginActivity.this).login(username);
                    if (login!=null) {
                        if (username.equals(login.getUsername()) && password.equals(login.getPassword())) {
                            SharedPreferences.Editor edit = mSharedPreferences.edit();
                            edit.putBoolean("is_login", is_login);
                            edit.putString("username",username);
                            edit.putString("password",password);
                            edit.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "该账号暂未注册", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        //点击checkbox
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_login=isChecked;
            }
        });

    }
}