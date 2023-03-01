package com.durui.feat.software_interface.ui.home.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.durui.feat.software_interface.data.table.User;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentMeBinding;
import com.durui.feat.software_interface.ui.info.InfoActivity;
import com.durui.feat.software_interface.ui.login.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;


public class MeFragment extends Fragment {
    FragmentMeBinding binding;
    MeViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(MeViewModel.class);
        //FIXME
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                //FIXME Attempt to read from field 'java.lang.String com.durui.feat.software_interface.data.table.User.user_avatar' on a null object reference
                if (user != null) {
                    //FIXME java.lang.NullPointerException: Attempt to read from field 'com.durui.feat.software_interface.ui.home.me.ImageRound com.durui.feat.software_interface.ui.databinding.FragmentMeBinding.avatarImage' on a null object reference
                    if (user.user_avatar != null && binding.avatarImage != null && !user.user_avatar.isEmpty())
                        binding.avatarImage.setImageBitmap(BitmapFactory.decodeFile(user.user_avatar));

                    binding.meName.setText(user.user_name);
                    binding.meSign.setText(user.user_sign);

                    binding.meSex.setText("性别: " + viewModel.getUserSexString());

                    Date birth = user.user_birth;

                    //FIXME 区分day和date
                    int yy = birth.getYear();
                    int mm = birth.getMonth();
                    int dd = birth.getDate();

                    Date current = new Date();

                    int yyc = current.getYear();
                    int mmc = current.getMonth();
                    int ddc = current.getDate();

                    int age = yyc - yy;
                    if (mmc < mm) age--;
                    else if (mmc == mm && ddc < dd) age--;

                    binding.meAge.setText("破壳 " + age + " 年");

                    Long millisFromRegister = -user.user_register.getTime() + System.currentTimeMillis();
                    binding.meRegisterTime.setText("已注册： " + (int) Math.floor(millisFromRegister / 1000.0 / 60 / 60) + "天");
                }
            }
        });

        binding.meEditBtn.setOnClickListener(v -> {
            viewModel.requestEditUser();
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            //durui 少有的利用Intent传数据
            intent.putExtra("index_only", false);
            startActivity(intent);
        });

        binding.iceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                //durui 少有的利用Intent传数据
                intent.putExtra("index_only", true);
                startActivity(intent);
            }
        });

        binding.cancelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(getContext())
                        // Add customization options here
                        .setTitle("确定注销账户吗？")
                        .setMessage("您的信息将完全清除，包括用户表、喜好表、运动记录表")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.clearAll();
                                SharedPreferences sp = getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear(); //清除所有数据
                                editor.apply();//提交
                                Log.d("myflag", "数据已删除");
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        binding.feedbackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);//注意DIAL和CALL的区别
                intent.setData(Uri.parse("tel:18120217202"));
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    /**
     * Fragment 的存在时间比其视图长。
     * 请务必在 Fragment 的 onDestroyView() 方法中清除对绑定类实例的所有引用
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}