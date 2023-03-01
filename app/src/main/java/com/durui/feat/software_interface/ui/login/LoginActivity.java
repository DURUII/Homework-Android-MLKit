package com.durui.feat.software_interface.ui.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.durui.feat.software_interface.ui.BaseActivity;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.TopWindowUtils;
import com.durui.feat.software_interface.ui.databinding.ActivityLoginBinding;
import com.durui.feat.software_interface.ui.info.InfoActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        readSP();

        viewModel.loadDB();

        binding.loginBtn.setOnClickListener(v -> {
            if (binding.checkboxAgree.isChecked()) {
                String phone = binding.inputUser.getEditText().getText().toString();
                String pwd = binding.inputPwd.getEditText().getText().toString();
                if (viewModel.login(phone, pwd)) {
                    if (binding.checkboxRemember.isChecked()) rememberme(); //如果选中，将把数据保存到xml文件
                    else unrememberme(); //如果取消选中，则清除xml文件数据
                    Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                    intent.putExtra("index_only", false);
                    startActivity(intent);
                } else {
                    TopWindowUtils.show(this, "用户名或密码错误", false);
                }
            } else {
                checkValidation();
            }
        });

        binding.registerBtn.setOnClickListener(v -> {
            if (binding.checkboxAgree.isChecked()) {
                String phone = binding.inputUser.getEditText().getText().toString();
                String pwd = binding.inputPwd.getEditText().getText().toString();
                if (viewModel.register(phone, pwd)) {
                    binding.loginBtn.performClick();
                } else {
                    TopWindowUtils.show(this, "用户已存在", false);
                }
            } else {
                checkValidation();
            }
        });
    }

    //保存数据(记住我)
    public void rememberme() {
        String phone = binding.editUser.getText().toString();
        String password = binding.editPwd.getText().toString();

        SharedPreferences sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("rememberme", true);
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.apply();//记得要提交

        Log.d("myflag", "数据保存成功");
    }

    //清除数据(取消记住我)
    public void unrememberme() {
        SharedPreferences sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear(); //清除所有数据
        editor.apply();//提交

        Log.d("myflag", "数据已删除");
    }

    //读取保存数据
    public void readSP() {
        SharedPreferences sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        Boolean remember = sp.getBoolean("rememberme", false);
        //如果没有找到rememberme，缺省值为false
        if (remember) {
            //如果true (表明"记住我"的数据已经存储) ，则读取数据
            binding.checkboxRemember.setChecked(true);
            ///设置"记住我"为选中状态
            String phone = sp.getString("phone", "");
            String password = sp.getString("password", "");
            binding.editUser.setText(phone);
            binding.editPwd.setText(password);
            Log.d("myflag", "数据读取成功");
        }
    }

    void checkValidation() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("隐私保护政策")
                .setMessage("您需要同意进入《Feat用户协议》后我们才能为您提供服务。若您点击「不同意」按钮，则您将退出使用。")
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        binding.checkboxAgree.setChecked(true);
                        Toast.makeText(LoginActivity.this, "已为您自动勾选", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}