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
import com.durui.feat.software_interface.ui.databinding.FragmentBirthBinding;
import com.durui.feat.software_interface.ui.info.InfoViewModel;

import java.util.Date;

import timber.log.Timber;


public class BirthFragment extends Fragment {
    FragmentBirthBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_birth, container, false);
        //FIXME VIEWMODEL 的生命周期问题 极为重要
        //viewModel = new ViewModelProvider(this).get(InfoViewModel.class);


        //FIXME java.lang.NullPointerException: Attempt to invoke virtual method 'int java.util.Date.getYear()' on a null object referenc
        Date birth = InfoViewModel.currentUser.user_birth;
        if (birth != null) {
            int yy = birth.getYear() + 1900;
            int mm = birth.getMonth() + 1;
            int dd = birth.getDate();
            Timber.tag("durui").d("" + yy + mm + dd);
            binding.datePicker.init(yy, mm, dd, null);
        }

        binding.nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_birthFragment_to_heightFragment);
                int yy = binding.datePicker.getYear();
                int mm = binding.datePicker.getMonth();
                int dd = binding.datePicker.getDayOfMonth();
                Date date = new Date(yy - 1900, mm, dd);
                InfoViewModel.currentUser.user_birth = date;
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