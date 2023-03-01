package com.durui.feat.software_interface.ui.home.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.durui.feat.software_interface.data.table.IndexRecord;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.FragmentDataBinding;

import java.util.List;


public class DataFragment extends Fragment {
    DataViewModel viewModel;
    FragmentDataBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data, container, false);
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);

        //FIXME 不可以写getActivity on a null object reference
        viewModel.getLiveIndexes().observe(getViewLifecycleOwner(), new Observer<List<IndexRecord>>() {
            @Override
            public void onChanged(List<IndexRecord> indexRecords) {
                if (indexRecords != null && !indexRecords.isEmpty()) {
                    //FIXME 注意不能调换位置
                    IndexRecord indexRecord = indexRecords.get(0);
                    Float bmi = viewModel.BMI(indexRecord.record_index_height, indexRecord.record_index_weight);
                    binding.bmiIndexText.setText("当前您的BMI指数为：" + bmi);
                    if (bmi <= 24 && bmi > 18.5)
                        binding.bmiIndexText.setBackgroundColor(getResources().getColor(R.color.colorAccent, null));
                    else
                        binding.bmiIndexText.setBackgroundColor(getResources().getColor(R.color.colorFail, null));
                    binding.weightBarchart.addSeries(viewModel.prepareSeriesFromList(indexRecords));
                    binding.weightBarchart.startAnimation();
                }
            }
        });

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.liveRecordRecycler.setLayoutManager(layoutManager);
        viewModel.getRanks().observe(getViewLifecycleOwner(), new Observer<List<RankTuple>>() {
            @Override
            public void onChanged(List<RankTuple> rankTuples) {
                LiveRecordAdapter adapter = new LiveRecordAdapter(rankTuples, getContext());
                binding.liveRecordRecycler.setAdapter(adapter);
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