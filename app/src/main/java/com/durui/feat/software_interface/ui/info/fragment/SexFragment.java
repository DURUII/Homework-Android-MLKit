package com.durui.feat.software_interface.ui.info.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentSexBinding;
import com.durui.feat.software_interface.ui.info.InfoViewModel;


public class SexFragment extends Fragment {
    FragmentSexBinding binding;
    //FIXME setOnCheckedChangeListener
    Long flag = 1L;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sex, container, false);

        binding.raido.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        flag = 1L;
                        break;
                    case R.id.rb_female:
                        flag = 2L;
                        break;
                }
            }
        });

        binding.nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_sexFragment_to_birthFragment);
                InfoViewModel.currentUser.user_sex_id = flag;
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