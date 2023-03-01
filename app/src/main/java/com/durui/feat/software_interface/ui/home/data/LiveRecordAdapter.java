package com.durui.feat.software_interface.ui.home.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.durui.feat.software_interface.ui.R;

import java.util.List;

/**
 * 类似 ListView
 * RecyclerView 需要借助「适配器」的实现类实现数据的传递
 * 建立数据和视图的「绑定关联」
 * <p>
 * 由于每行记录的数据类型并非简单的字符串，需要定义 Commodity 实体类
 * ViewHolder 视图类，作为 RecyclerView 的泛型适配类型
 */
public class LiveRecordAdapter extends RecyclerView.Adapter<LiveRecordAdapter.ViewHolder> {
    //返回选中项位置值
    private int position;
    List<RankTuple> list;
    Context context;

    //静态内部类：视图占位
    static class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder 与 RecyclerView_Item 组件对应
        ImageView avatar_image;
        TextView user_name;
        TextView num;

        /**
         * ViewHolder构造函数：完成组件的绑定
         *
         * @param itemView 传入自定义布局，并以此绑定组件
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_image = (ImageView) itemView.findViewById(R.id.avatar_image);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            num = (TextView) itemView.findViewById(R.id.num);
        }
    }

    @NonNull
    @Override
    //创建并返回ViewHolder实例，并将RecyclerView_item布局传入ViewHolder:界面绑定
    //单击窥见->同Edit()
    //长按修改当前位置
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //注入自定义布局
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_live_record, parent, false);

        //ViewHolder 实例化
        ViewHolder holder = new ViewHolder(view);

//        //设置长按监听
//        holder.title.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                setPosition(holder.getAdapterPosition());
//                return false;
//            }
//        });
//
//        //设置单击监听
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setPosition(holder.getAdapterPosition());
//                mOnClickListener.peep();
//                //Toast.makeText(context, "跳转ing", Toast.LENGTH_SHORT).show();
//            }
//        });

        return holder;
    }

    //适配器构造函数 0
    public LiveRecordAdapter() {
    }

    //适配器构造函数 1
    public LiveRecordAdapter(List<RankTuple> list) {
        this.list = list;
    }

    //适配器构造函数 2
    public LiveRecordAdapter(List<RankTuple> list, Context context) {
        this.list = list;
        this.context = context;
    }

//    //适配器构造函数 3
//    public MyAdapter(List<Diary> list, RecyclableActivity context, OnClickListener mOnClickListener) {
//        this.list = list;
//        this.context = context;
//        this.mOnClickListener = mOnClickListener;
//    }

    /**
     * 用于对子项的数据进行赋值，会在每个子项被滚动到屏幕内时执行
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //用 position 获得当前项需要的实例
        RankTuple rank = list.get(position);

        //完成 数据与 UI 组件值的绑定
        if (rank.user_avatar != null && !rank.user_avatar.isEmpty())
            holder.avatar_image.setImageBitmap(BitmapFactory.decodeFile(rank.user_avatar));
        holder.user_name.setText(rank.user_name + "");
        holder.num.setText(rank.num_max + "");
    }

    //返回 RecyclerView 的子项数目
    @Override
    public int getItemCount() {
        return list.size();
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //launcher
    public interface OnClickListener {
        void peep();

        void edit();

        void delete();
    }

    static OnClickListener mOnClickListener;
}