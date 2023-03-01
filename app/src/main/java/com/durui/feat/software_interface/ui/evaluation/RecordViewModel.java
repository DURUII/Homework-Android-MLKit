package com.durui.feat.software_interface.ui.evaluation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.durui.feat.software_interface.data.db.WorkoutDatabase;
import com.durui.feat.software_interface.data.table.LiveRecord;
import com.durui.feat.software_interface.ui.login.LoginViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class RecordViewModel extends AndroidViewModel {
    WorkoutDatabase db;
    static LiveRecord currentRecord;
    final ArrayList<Long> CORRECT_BUZZ_PATTERN = new ArrayList<Long>(Arrays.asList(1L, 2L, 3L));

    public RecordViewModel(@NonNull Application application) {
        super(application);
        db = WorkoutDatabase.getInstance(application.getApplicationContext());
        currentRecord = db.otherDao().getLatestLiveRecord(LoginViewModel.phone);
    }

    public void delete() {
        db.liveRecordDao().delete(currentRecord);
    }

    public void update() {
        db.liveRecordDao().update(currentRecord);
    }
}
