package com.durui.feat.software_interface.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


//https://blog.csdn.net/qq_35605213/article/details/93731169
public class TopWindowUtils {
    private static PopupWindow popupWindow;

    public static void show(final Activity activity, final CharSequence text, boolean success) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow();
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout = new LinearLayout(activity);
            View viewContent = inflater.inflate(R.layout.view_popup, linearLayout);
            popupWindow.setContentView(viewContent);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.PopupTopAnim);
        }
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0, 0);

        ImageView iv = ((ImageView) popupWindow.getContentView().findViewById(R.id.pop_up_image));
        if (!success) {
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fail));
        } else {
            iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_success));
        }

        final TextView tv = ((TextView) popupWindow.getContentView().findViewById(R.id.pop_up_text));
        tv.setText(text);

        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    public static void dismiss() {
        popupWindow.dismiss();
    }

    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            popupWindow.dismiss();
        }
    };

    /**
     * 从dp单位转换为px
     *
     * @param dp dp值
     * @return 返回转换后的px值
     */
    private static int dp2px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}


