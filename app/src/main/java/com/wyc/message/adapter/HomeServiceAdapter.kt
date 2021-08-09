package com.wyc.message.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wyc.message.R

/**
 *作者： wyc
 * <p>
 * 创建时间： 2019/11/20 19:03
 * <p>
 * 文件名字： com.wyc.vivodemo
 * <p>
 * 类的介绍：
 */
class HomeServiceAdapter(private var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mPages = mutableListOf<String>()

    private var mHomeServiceListener: ((view: View, position: Int) -> Unit)? = null

    fun setPages(pages: MutableList<String>) {
        mPages.clear()
        mPages = pages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomeActivityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
    }

    override fun getItemCount(): Int = mPages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeActivityViewHolder) {
            Glide.with(mContext)
                    .load(mPages[position])
                    .into(holder.imageView)
            holder.itemView.setOnClickListener {
                mHomeServiceListener?.invoke(it, position)
            }
        }
    }

    fun setOnHomeServiceItemClickListener(listener: (view: View, position: Int) -> Unit) {
        mHomeServiceListener = listener
    }

    inner class HomeActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}