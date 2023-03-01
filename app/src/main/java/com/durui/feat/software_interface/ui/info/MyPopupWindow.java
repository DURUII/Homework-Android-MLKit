package com.durui.feat.software_interface.ui.info;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.durui.feat.software_interface.ui.R;

/**
 * 自定义组件：继承自PopupWindow
 */
public class MyPopupWindow extends PopupWindow {
    //用于设置背景色，由构造函数传入初始化
    private Activity mActivity;

    /**
     * 构造函数
     *
     * @param context      上下文，由 MainActivity 新建该组件时传入
     * @param itemsOnClick 类型为自定义内部接口，在 MainActivity 传入时匿名类实现 3 个方法
     */
    public MyPopupWindow(Activity context, OnClickListener itemsOnClick) {
        this.mActivity = context;
        mOnClickListener = itemsOnClick;
        //新建并设置布局视图
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window, null);
        this.setContentView(view);
        //设置宽度、高度
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景，这个没什么效果，不添加会报错
        setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        setFocusable(true);
        setOutsideTouchable(true);
        //设置动画，原有的 style 未定义报错
        setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        //设置消失监听
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissPopupWindow();
            }
        });
        //设置PopupWindow的View点击事件，自定义函数
        setOnPopupViewClick(view);
        //设置背景色，自定义函数
        setBackgroundAlpha(0.5f);
    }

    //底部显示
    public void show(View parentView) {
        showAtLocation(parentView, Gravity.BOTTOM, 0, 66);
    }

    //销毁并修改背景色
    private void dismissPopupWindow() {
        dismiss();
        setBackgroundAlpha(1f);
    }

    //设置 mActivity 背景色明暗（传值作用）
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 利用内部接口 mOnClickListener 设置当前控件的点击事件
     *
     * @param view
     */
    private void setOnPopupViewClick(View view) {
        TextView tv_camera, tv_photo, tv_cancel;

        tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(new View.OnClickListener() {//匿名类
            @Override
            public void onClick(View view) {
                dismissPopupWindow();
                mOnClickListener.cameraOnClick();
            }
        });

        tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        tv_photo.setOnClickListener(v -> {//匿名函数
            dismissPopupWindow();
            mOnClickListener.photoOnClick();
        });

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> {
            dismissPopupWindow();
            mOnClickListener.cancel();
        });
    }

    //内部接口 OnClickListener 的 实例，由构造函数传入初始化
    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void cameraOnClick();

        void photoOnClick();

        void cancel();
    }
}
