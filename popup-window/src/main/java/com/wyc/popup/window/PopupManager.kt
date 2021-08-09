package com.wyc.popup.window

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wyc.popup.window.manager.KeyEventManager
import com.wyc.popup.window.manager.PhoneManager

object PopupManager {

    private const val TAG = "PopupManager"

    interface OnDismissListener {
        fun onDismiss(view: View, position: Int)
    }

    @JvmStatic
    fun createPopMenuList(context: Context, width: Int, strArray: Array<String>, listener: OnDismissListener?): PopupWindowHelper {
        return createPopMenuList(context, width, strArray.toMutableList(), listener, -1)
    }

    @JvmStatic
    fun createPopMenuList(context: Context, width: Int, menus: MutableList<String>, listener: OnDismissListener?, animationStyleId: Int): PopupWindowHelper {
        val mContentView = LayoutInflater.from(context).inflate(R.layout.layout_popup_window, null)
        val menuRecycler: RecyclerView? = mContentView.findViewById(R.id.pop_menu_recycler)
        menuRecycler?.setHasFixedSize(true)
        menuRecycler?.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val adapter = PopupMenuAdapter(menus)
        menuRecycler?.adapter = adapter
        val popupWindowHelper = PopupWindowHelper.Builder(context)
                .setContentView(mContentView)
                .setWidth(width)
                .setAnimationStyle(animationStyleId)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .build()

        registerPopupDismissCallback(popupWindowHelper)
        adapter.setItemClickListener { view, position ->
            listener?.onDismiss(view, position)
            popupWindowHelper.dismiss()
        }

        return popupWindowHelper
    }

    private fun registerPopupDismissCallback(popupWindowHelper: PopupWindowHelper) {
        val keyEventManager = KeyEventManager(popupWindowHelper.context, KeyEventManager.KeyEventCallback {
            try {
                popupWindowHelper.dismiss()
            } catch (e: Exception) {
                Log.e(TAG, "popup dismiss ex : ", e)
            }
            false
        }, null)

        // 通话
        val phoneManager = PhoneManager(popupWindowHelper.context, PhoneManager.PhoneStateCallback {
            popupWindowHelper.dismiss()
        })
        popupWindowHelper.setOnDismissListener(PopupWindow.OnDismissListener {
            phoneManager.resetCallback()
            keyEventManager.resetCallback()
        })
    }

    private class PopupMenuAdapter(private val mMenuList: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var mItemClickListener: ((view: View, position: Int) -> Unit)? = null

        fun setItemClickListener(listener: ((view: View, position: Int) -> Unit)?) {
            this.mItemClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_popup_layout, parent, false))
        }

        override fun getItemCount(): Int {
            return mMenuList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ViewHolder) {
                holder.menuTv.text = mMenuList[position]
            }
            holder.itemView.setOnClickListener {
                mItemClickListener?.invoke(holder.itemView, holder.layoutPosition)
            }
        }

        private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val menuTv: TextView = itemView.findViewById(R.id.item_pop_menu_tv)
        }
    }
}