package com.durui.feat.software_interface.ui.home.workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentWorkoutBinding;
import com.durui.feat.software_interface.ui.exercise.LivePreviewActivity;


public class WorkoutFragment extends Fragment {
    FragmentWorkoutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false);
        binding.anchorWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LivePreviewActivity.class);
            startActivity(intent);
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