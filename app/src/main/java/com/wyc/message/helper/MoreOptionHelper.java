package com.wyc.message.helper;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;

import com.wyc.message.App;
import com.wyc.message.R;
import com.wyc.message.utils.ScreenUtils;
import com.wyc.popup.window.PopupManager;
import com.wyc.popup.window.PopupWindowHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MoreOptionHelper {

    private static final String TAG = "MoreOptionHelper";

    private Context mContext;


    private MoreOptionCallback mCallback;

    private ArrayList<String> mMoreOptionsNameList;

    private ArrayList<MoreOption> mMoreOptionsTypeList;

    private enum MoreOption {
        Edit, Rename, Delete, Share
    }

    public interface MoreOptionCallback {
        void share();

        void edit();

        void rename();

        void delete();
    }

    public MoreOptionHelper(Context mContext, MoreOptionCallback callback) {
        this.mContext = mContext;
        this.mCallback = callback;
    }


    /**
     * 显示更多操作弹窗
     */
    public void showMoreOption(View view) {
        Resources res = mContext.getResources();
        if (mMoreOptionsTypeList == null || mMoreOptionsNameList == null) {
            mMoreOptionsTypeList = new ArrayList<>();
            mMoreOptionsNameList = new ArrayList<>();
            mMoreOptionsNameList.add(res.getString(R.string.share));
            mMoreOptionsTypeList.add(MoreOption.Share);
            mMoreOptionsNameList.add(res.getString(R.string.edit));
            mMoreOptionsTypeList.add(MoreOption.Edit);
            mMoreOptionsNameList.add(res.getString(R.string.rename));
            mMoreOptionsTypeList.add(MoreOption.Rename);
            mMoreOptionsNameList.add(res.getString(R.string.delete));
            mMoreOptionsTypeList.add(MoreOption.Delete);
        }
        createMenu(view);
    }

    private void createMenu(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        PopupWindowHelper helper = PopupManager.createPopMenuList(App.getContext(), ScreenUtils.dip2px(126),
                mMoreOptionsNameList, new PopupManager.OnDismissListener() {
                    @Override
                    public void onDismiss(@NotNull View view, int position) {
                        popWindowItemClick(position);
                    }
                }, R.style.pop_window_anim_style);
        helper.showAsDropDown(view, 0, ScreenUtils.dip2px(18), Gravity.END);
    }


    private void popWindowItemClick(int position) {
        switch (mMoreOptionsTypeList.get(position)) {
            case Share:
                if (mCallback != null) {
                    mCallback.share();
                }
                break;
            case Edit:
                if (mCallback != null) {
                    mCallback.edit();
                }
                break;
            case Rename:
                if (mCallback != null) {
                    mCallback.rename();
                }
                break;
            case Delete:
                if (mCallback != null) {
                    mCallback.delete();
                }
                break;
        }
    }
}
