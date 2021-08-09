package com.wyc.message.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyc.message.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 作者： wyc
 * <p>
 * 创建时间： 2021/5/26 11:28
 * <p>
 * 文件名字： com.wyc.vivodemo
 * <p>
 * 类的介绍：
 */
public class ScrollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mDatas;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;

    public ScrollAdapter(List<String> datas) {
        this.mDatas = datas;
    }

    public interface ItemClickListener {
        void itemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener listener) {
        this.mClickListener = listener;
    }


    public interface ItemLongClickListener {
        boolean itemLongClick(View view, int position);
    }

    public void setLongClickListener(ScrollAdapter.ItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_scroll, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).textView.setText(mDatas.get(i));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.itemClick(holder.itemView, holder.getLayoutPosition());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickListener != null) {
                        return mLongClickListener.itemLongClick(holder.itemView, holder.getLayoutPosition());
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_home);
        }
    }
}
