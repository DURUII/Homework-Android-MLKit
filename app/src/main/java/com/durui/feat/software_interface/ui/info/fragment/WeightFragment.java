package com.durui.feat.software_interface.ui.info.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentWeightBinding;
import com.durui.feat.software_interface.ui.home.HomeActivity;
import com.durui.feat.software_interface.ui.info.InfoViewModel;


public class WeightFragment extends Fragment {
    FragmentWeightBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weight, container, false);
        binding.nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoViewModel.weight = (double) binding.newWeightRuler.currentScale;
                InfoViewModel infoViewModel = new ViewModelProvider(getActivity()).get(InfoViewModel.class);
                infoViewModel.update();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

//        if (InfoViewModel.latestRecord != null)
//            binding.newWeightRuler.computeScrollTo(Float.parseFloat("" + InfoViewModel.latestRecord.record_index_height));

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