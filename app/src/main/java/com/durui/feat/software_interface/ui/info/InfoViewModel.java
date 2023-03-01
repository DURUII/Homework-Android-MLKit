package com.durui.feat.software_interface.ui.info;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.durui.feat.software_interface.data.crud.IndexRecordDao;
import com.durui.feat.software_interface.data.crud.UserDao;
import com.durui.feat.software_interface.data.db.WorkoutDatabase;
import com.durui.feat.software_interface.data.table.IndexRecord;
import com.durui.feat.software_interface.data.table.User;
import com.durui.feat.software_interface.ui.login.LoginViewModel;

public class InfoViewModel extends AndroidViewModel {
    WorkoutDatabase db;
    UserDao userDao;
    IndexRecordDao indexRecordDao;
    Long user_phone;
    public static User currentUser;
    public static IndexRecord latestRecord = null;
    public static Double height = null;
    public static Double weight = null;

    public InfoViewModel(@NonNull Application application) {
        super(application);
        user_phone = LoginViewModel.phone;
        db = WorkoutDatabase.getInstance(application.getApplicationContext());
        userDao = db.userDao();
        indexRecordDao = db.indexRecordDao();
//        if (db.otherDao().getRecnet8IndexRecord(user_phone) != null)
//            latestRecord = db.otherDao().getRecnet8IndexRecord(user_phone).getValue().get(0);
        currentUser = db.userDao().get(user_phone).get(0);
    }

    public boolean hasFilled() {
        return currentUser.user_state_id != 0;
    }

    public void update() {
        if (currentUser.user_name != null && currentUser.user_avatar != null && currentUser.user_sign != null)
            currentUser.user_state_id = 1L;

        userDao.update(currentUser);

        IndexRecord indexRecord = new IndexRecord();
        if (height != null) indexRecord.record_index_height = height;
        //durui 拒绝CTRL+D
        if (weight != null) indexRecord.record_index_weight = weight;
        indexRecord.record_user_id = user_phone;
        indexRecordDao.insert(indexRecord);
    }
}
