package com.durui.feat.software_interface.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;

import timber.log.Timber;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d(this.getClass().getSimpleName());

        //setStatusBarTranslucent();
        hideBottomUIMenu();


        if (!this.allRuntimePermissionsGranted()) {
            this.getRuntimePermissions();
        }
    }

    private static final String[] REQUIRED_RUNTIME_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CALL_PHONE"};

    private boolean allRuntimePermissionsGranted() {
        for (String permission : REQUIRED_RUNTIME_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != 0) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_RUNTIME_PERMISSIONS, 1);
    }

    //隐藏导航栏 https://blog.csdn.net/qq_33382900/article/details/89713874
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT >= 19) { //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; /*|View.SYSTEM_UI_FLAG_FULLSCREEN*/
            decorView.setSystemUiVisibility(uiOptions);
        } else {// lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
    }

    //将StatusBar设置为透明 https://blog.csdn.net/weixin_45146479/article/details/125622891
    public void setStatusBarTranslucent() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this,
                0, null);
        StatusBarUtil.setLightMode(this);
    }
}
