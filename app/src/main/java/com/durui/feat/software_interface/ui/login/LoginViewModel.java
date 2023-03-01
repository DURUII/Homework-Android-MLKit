package com.durui.feat.software_interface.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;

import com.durui.feat.computer_vision.classification_counter.PoseClassifierProcessor;
import com.durui.feat.software_interface.data.crud.UserDao;
import com.durui.feat.software_interface.data.db.WorkoutDatabase;
import com.durui.feat.software_interface.data.table.Sex;
import com.durui.feat.software_interface.data.table.Sports;
import com.durui.feat.software_interface.data.table.State;
import com.durui.feat.software_interface.data.table.User;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginViewModel extends AndroidViewModel {
    WorkoutDatabase db;
    UserDao userDao;
    public static Long phone;
    public static String pwd;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        db = WorkoutDatabase.getInstance(application.getApplicationContext());
        userDao = db.userDao();
    }

    public Boolean login(String phone, String pwd) {
        LoginViewModel.phone = Long.parseLong(phone);
        LoginViewModel.pwd = pwd;
        return !userDao.get(LoginViewModel.phone, LoginViewModel.pwd).isEmpty();
    }

    public Boolean register(String phone, String pwd) {
        LoginViewModel.phone = Long.parseLong(phone);
        LoginViewModel.pwd = pwd;
        if (!userDao.get(LoginViewModel.phone).isEmpty()) return false;

        User user = new User();
        user.user_id = LoginViewModel.phone;
        user.user_passport = LoginViewModel.pwd;

        userDao.insert(user);
        return true;
    }

    public void loadDB() {
        ListenableFuture<List<Sex>> sexesFuture = db.sexDao().getAll();
        sexesFuture.addListener(new Runnable() {//数据库 ONCLICK 异步 极端重要！！！！！
            @Override
            public void run() {
                try {
                    if (sexesFuture.get().isEmpty()) {
                        db.sexDao().insert(new Sex(0L, "未知"));
                        db.sexDao().insert(new Sex(1L, "男"));
                        db.sexDao().insert(new Sex(2L, "女"));
                        db.stateDao().insert(new State(0L, "新用户"));
                        db.stateDao().insert(new State(1L, "用户"));
                        db.stateDao().insert(new State(2L, "管理员"));
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getApplication()));

        ListenableFuture<List<Sports>> sportsFuture = db.sportsDao().getAllListenable();
        sportsFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (sportsFuture.get().isEmpty()) {
                        db.sportsDao().insert(new Sports("俯卧撑", "在日常锻炼和体育课上，特别是在军事体能训练中是一项基本训练。俯卧撑主要锻炼上肢、腰部及腹部的肌肉，尤其是胸肌。是很简单易行却十分有效的力量训练手段。", PoseClassifierProcessor.PUSHUP_FILE));
                        db.sportsDao().insert(new Sports("深蹲", "深蹲是一项健身运动，是练大腿肌肉的王牌动作，坚持做还会起到减肥的作用。深蹲被认为是增强腿部和臀部力量和围度，以及发展核心力量必不可少的练习。", PoseClassifierProcessor.SQUAT_FILE));
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(getApplication()));
    }
}
