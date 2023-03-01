package com.durui.feat.software_interface.ui.evaluation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.ActivityRecordBinding;
import com.durui.feat.software_interface.ui.exercise.MyCameraXViewModel;

import java.util.Date;


public class RecordActivity extends AppCompatActivity {
    ActivityRecordBinding binding;
    RecordViewModel viewModel;
    Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record);
        viewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        binding.textView.setText(RecordViewModel.currentRecord.live_sports_num + "");
        currentTime = new Date();
        RecordViewModel.currentRecord.live_time_end = currentTime;

        binding.recordBtn.setOnClickListener(v -> {
            RecordViewModel.currentRecord.live_rate = (int) binding.slider.getValue();
            viewModel.update();
            MyCameraXViewModel.reps = 0;
            finish();
        });

        binding.cancelBtn.setOnClickListener(v -> {
            viewModel.delete();
            MyCameraXViewModel.reps = 0;
            finish();
        });
    }
}