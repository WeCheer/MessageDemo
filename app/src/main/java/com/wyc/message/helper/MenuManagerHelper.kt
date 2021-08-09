package com.wyc.message.helper

import android.view.Gravity
import android.view.View
import com.wyc.message.App
import com.wyc.message.R
import com.wyc.message.utils.ResUtils
import com.wyc.message.utils.ScreenUtils
import com.wyc.popup.window.PopupManager
import com.wyc.popup.window.PopupManager.createPopMenuList
import java.util.*

/**
 * 长按菜单管理
 */
class MenuManagerHelper private constructor() {
    private val mMenuOptionsNameList: ArrayList<String> = ArrayList()
    private val mMenuOptionsTypeList: ArrayList<MenuOption> = ArrayList()

    private enum class MenuOption {
        Share, Rename, EditLabel, Delete
    }

    fun showPopupWindow(view: View, clickX: Float, clickY: Float, callback: MenuClickCallback) {
        val shareTitle = ResUtils.getString(R.string.share)
        val renameTitle = ResUtils.getString(R.string.rename)
        val editLabelTitle = ResUtils.getString(R.string.edit)
        val deleteTitle = ResUtils.getString(R.string.delete)

        mMenuOptionsNameList.clear()
        mMenuOptionsTypeList.clear()
        // 分享
        mMenuOptionsNameList.add(shareTitle)
        mMenuOptionsTypeList.add(MenuOption.Share)
        //重命名
        mMenuOptionsNameList.add(renameTitle)
        mMenuOptionsTypeList.add(MenuOption.Rename)
        //编辑
        mMenuOptionsNameList.add(editLabelTitle)
        mMenuOptionsTypeList.add(MenuOption.EditLabel)
        //取消
        mMenuOptionsNameList.add(deleteTitle)
        mMenuOptionsTypeList.add(MenuOption.Delete)
        createPopMenu(view, clickX, clickY, callback)
    }

    private fun createPopMenu(view: View, clickX: Float, clickY: Float, callback: MenuClickCallback) {
        showPopWindow(view, clickX, clickY, callback)
    }

    private fun showPopWindow(view: View, clickX: Float, clickY: Float, callback: MenuClickCallback) {
        val dismissListener = object : PopupManager.OnDismissListener {
            override fun onDismiss(view: View, position: Int) {
                popItemClick(position, callback)
            }
        }
        val helper = createPopMenuList(App.getContext(), ScreenUtils.dip2px(126f),
                mMenuOptionsNameList, dismissListener, R.style.activity_pop_window_anim_style)

        val locations = IntArray(2)
        view.getLocationOnScreen(locations)
        val top = locations[1]
        val navBarHeight = 0
        val gravity: Int
        //208 菜单总高度
        if (ScreenUtils.getScreenHeight() - navBarHeight - top < ScreenUtils.dip2px(208f)) {
            //点击View的位置下方空间不足以显示菜单
            gravity = Gravity.START
            if (clickX.toInt() > (ScreenUtils.getScreenWidth() * 2 / 3)) {
                //点击位置已经超过屏幕的2/3，设置从右下方弹出动效
                helper.getPopupWindow()?.animationStyle = R.style.activity_bottom_right_pop_window_anim_style
            } else {
                //设置从下方弹出动效
                helper.getPopupWindow()?.animationStyle = R.style.activity_bottom_pop_window_anim_style
            }
            helper.getPopupWindow()?.update()
            helper.showAtLocation(view, clickX.toInt(), clickY.toInt(), gravity)
        } else {
            //点击View的位置下方空间可以正常显示菜单
            if (clickX.toInt() > (ScreenUtils.getScreenWidth() * 2 / 3)) {
                //点击位置已经超过屏幕的2/3，设置从右方弹出动效
                helper.getPopupWindow()?.animationStyle = R.style.activity_right_pop_window_anim_style
                helper.getPopupWindow()?.update()
            }
            //使用正常动效
            gravity = Gravity.START or Gravity.TOP
            helper.showAtLocation(view, clickX.toInt(), clickY.toInt(), gravity)
        }
    }

    private fun popItemClick(position: Int, callback: MenuClickCallback) {
        when (mMenuOptionsTypeList[position]) {
            MenuOption.Share -> {
                callback.share()
            }
            MenuOption.Rename -> {
                callback.rename()
            }
            MenuOption.EditLabel -> {
                callback.edit()
            }
            MenuOption.Delete -> {
                callback.delete()
            }
        }
    }

    /**
     * 菜单点击回调
     */
    interface MenuClickCallback {
        fun share()
        fun rename()
        fun edit()
        fun delete()
    }

    companion object {

        @JvmStatic
        val instance: MenuManagerHelper = SingletonHolder.INSTANCE

        private object SingletonHolder {
            val INSTANCE = MenuManagerHelper()
        }
    }
}