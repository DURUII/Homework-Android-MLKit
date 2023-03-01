package com.durui.feat.software_interface.ui.home.me;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.durui.feat.software_interface.data.crud.OtherDao;
import com.durui.feat.software_interface.data.db.WorkoutDatabase;
import com.durui.feat.software_interface.data.table.IndexRecord;
import com.durui.feat.software_interface.data.table.User;
import com.durui.feat.software_interface.ui.login.LoginViewModel;

public class MeViewModel extends AndroidViewModel {
    Long user_phone;
    WorkoutDatabase db;
    OtherDao otherDao;

    public MeViewModel(@NonNull Application application) {
        super(application);
        user_phone = LoginViewModel.phone;
        db = WorkoutDatabase.getInstance(application.getApplicationContext());
        otherDao = db.otherDao();
    }

    LiveData<User> getUser() {
        return otherDao.getLatestUser(user_phone);
    }

    LiveData<IndexRecord> getIndex() {
        return otherDao.getLatestIndex(user_phone);
    }

    public String getUserSexString() {
        return otherDao.getUserSex(user_phone);
    }

    public void requestEditUser() {
        User user = db.userDao().get(user_phone).get(0);
        user.user_state_id = 0L;
        db.userDao().update(user);
    }

    public void clearAll() {
        db.otherDao().clearLiveRecordTableByUser(user_phone);
        db.otherDao().clearIndexRecordTableByUser(user_phone);
        db.otherDao().clearUserTableByUser(user_phone);
    }
}

