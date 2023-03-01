package com.durui.feat.software_interface.ui.home.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.durui.feat.software_interface.data.crud.OtherDao;
import com.durui.feat.software_interface.data.crud.UserDao;
import com.durui.feat.software_interface.data.db.WorkoutDatabase;
import com.durui.feat.software_interface.data.table.IndexRecord;
import com.durui.feat.software_interface.data.table.LiveRecord;
import com.durui.feat.software_interface.data.table.User;
import com.durui.feat.software_interface.ui.login.LoginViewModel;

import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

public class DataViewModel extends AndroidViewModel {
    WorkoutDatabase db;
    UserDao userDao;
    Long user_phone;
    public static User currentUser;
    OtherDao otherDao;

    public DataViewModel(@NonNull Application application) {
        super(application);
        user_phone = LoginViewModel.phone;
        db = WorkoutDatabase.getInstance(application.getApplicationContext());
        userDao = db.userDao();
        currentUser = db.userDao().get(user_phone).get(0);
        otherDao = db.otherDao();
    }

    public LiveData<List<IndexRecord>> getLiveIndexes() {
        return otherDao.getRecnet8IndexRecord(user_phone);
    }

    public ValueLineSeries prepareSeriesFromList(List<IndexRecord> indexRecords) {
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        if (false) {
            series.addPoint(new ValueLinePoint("Jan", 2.4f));
            series.addPoint(new ValueLinePoint("Feb", 3.4f));
            series.addPoint(new ValueLinePoint("Mar", .4f));
            series.addPoint(new ValueLinePoint("Apr", 1.2f));
            series.addPoint(new ValueLinePoint("Mai", 2.6f));
            series.addPoint(new ValueLinePoint("Jun", 1.0f));
            series.addPoint(new ValueLinePoint("Jul", 3.5f));
            series.addPoint(new ValueLinePoint("Aug", 2.4f));
            series.addPoint(new ValueLinePoint("Sep", 2.4f));
            series.addPoint(new ValueLinePoint("Oct", 3.4f));
            series.addPoint(new ValueLinePoint("Nov", .4f));
            series.addPoint(new ValueLinePoint("Dec", 1.3f));
        }

        if (indexRecords != null) {
            Collections.reverse(indexRecords);
            SimpleDateFormat formatter = new SimpleDateFormat("MM.dd");
            String formerlabel = "";

            for (IndexRecord i : indexRecords) {
                String label = formatter.format(i.record_date);
//                if (!label.equals(formerlabel)) {
                //FIXME durui java.lang.NullPointerException: Attempt to invoke virtual method 'double java.lang.Double.doubleValue()' on a null object reference
                if (i.record_index_weight != null)
                    series.addPoint(new ValueLinePoint(label, BMI(i.record_index_height, i.record_index_weight)));
//                }
                formerlabel = label;
            }
        }

        return series;
    }

    public Float BMI(Double height, Double weight) {
        //体重(kg)除以身高(m)的平方(BMI =公斤/㎡)；
        height /= 100.0;
        String bmi = (weight / height / height + "").substring(0, 5);
        Timber.tag("durui").d(bmi);
        return Float.parseFloat(bmi);
    }

    LiveData<List<LiveRecord>> getRecent50Records(){
        return db.otherDao().getLatest50LiveRecord(currentUser.user_id);
    }

    public LiveData<List<RankTuple>> getRanks() {
        return db.otherDao().getRanking();
    }
}
