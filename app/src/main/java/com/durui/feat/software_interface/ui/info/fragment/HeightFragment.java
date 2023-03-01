package com.durui.feat.software_interface.ui.info.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentHeightBinding;
import com.durui.feat.software_interface.ui.info.InfoViewModel;
import com.durui.feat.software_interface.ui.login.LoginViewModel;


public class HeightFragment extends Fragment {
    FragmentHeightBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_height, container, false);

        binding.nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_heightFragment_to_weightFragment);
                InfoViewModel.height = 0.0 + binding.heightRuler.getCurrentIndex();
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