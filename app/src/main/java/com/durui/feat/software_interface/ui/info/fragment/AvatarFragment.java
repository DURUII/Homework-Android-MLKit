package com.durui.feat.software_interface.ui.info.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.durui.feat.computer_vision.vision_base.generic.BitmapUtils;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentAvatarBinding;
import com.durui.feat.software_interface.ui.info.InfoViewModel;
import com.durui.feat.software_interface.ui.info.MyPopupWindow;

import java.io.IOException;


public class AvatarFragment extends Fragment {
    FragmentAvatarBinding binding;
    String imagePath = "";

    //1.单击相册->launcher_pht传入MIME文件类型，传出Uri图片类型（注意判空）
    ActivityResultLauncher launcher_pht = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        try {
                            Bitmap result2bitmap = BitmapUtils.getBitmapFromContentUri(getActivity().getContentResolver(), result);
                            imagePath = BitmapUtils.store(result2bitmap, "EXTERNAL_");
                            Bitmap bm = BitmapFactory.decodeFile(imagePath);
                            binding.avatarImage.setImageBitmap(bm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    //2.单击拍照->launcher_tp无需传入，传出Bitmap图片类型（注意判空）
    ActivityResultLauncher launcher_tp = registerForActivityResult(
            new ActivityResultContracts.TakePicturePreview(),
            new ActivityResultCallback<Bitmap>() {
                @Override
                public void onActivityResult(Bitmap result) {
                    if (result != null) {
                        try {
                            imagePath = BitmapUtils.store(result, "");
                            Bitmap bm = BitmapFactory.decodeFile(imagePath);
                            binding.avatarImage.setImageBitmap(bm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_avatar, container, false);

        binding.textInputLayoutName.getEditText().setText(InfoViewModel.currentUser.user_name);
        binding.textInputLayoutSign.getEditText().setText(InfoViewModel.currentUser.user_sign);
        if (InfoViewModel.currentUser.user_avatar != null) {
            imagePath = InfoViewModel.currentUser.user_avatar;
            binding.avatarImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }


        /**
         * 单击头像->显示弹窗控件
         * 第二个参数，匿名类实现接口的功能（实现数据的双向传递）
         * 1.单击相册->launcher_pht传入MIME文件类型，传出Uri图片类型
         * 2.单击拍照->launcher_tp无需传入，传出Bitmao图片类型
         * TODO 当用户点击拍照取消时，图片可以不进行任何改动（空白）
         * 3.单击取消->
         */
        binding.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MyPopupWindow.OnClickListener并不是PopupWindow各个组件的监听事件，而是传入，并在setOnPopupViewClick代码中使用逻辑
                new MyPopupWindow(getActivity(), new MyPopupWindow.OnClickListener() {
                    @Override
                    public void cameraOnClick() {
                        launcher_tp.launch(null);
                    }

                    @Override
                    public void photoOnClick() {
                        //intent.setType("image/*"); Multipurpose Internet Mail Extensions
                        launcher_pht.launch("image/*");
                    }

                    @Override
                    public void cancel() {
                    }
                }).show(view);
            }
        });

        binding.nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imagePath.isEmpty())
                    InfoViewModel.currentUser.user_avatar = imagePath;
                else {
                    Toast.makeText(getContext(), "未指定头像", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!binding.textInputLayoutName.getEditText().getText().toString().isEmpty())
                    InfoViewModel.currentUser.user_name = binding.textInputLayoutName.getEditText().getText().toString();
                else {
                    Toast.makeText(getContext(), "昵称为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!binding.textInputLayoutSign.getEditText().getText().toString().isEmpty())
                    InfoViewModel.currentUser.user_sign = binding.textInputLayoutSign.getEditText().getText().toString();
                else {
                    Toast.makeText(getContext(), "个签为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                Navigation.findNavController(v).navigate(R.id.action_avatarFragment_to_sexFragment);
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