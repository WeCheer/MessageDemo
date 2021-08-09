package com.wyc.message;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wyc.message.adapter.ScrollAdapter;
import com.wyc.message.helper.MenuManagerHelper;
import com.wyc.message.helper.MoreOptionHelper;
import com.wyc.message.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class RecyclerScrollActivity extends AppCompatActivity {
    private static final String TAG = "RecyclerScrollActivity";
    private TextView mTitleView;
    private int mHeight = 900;
    private int mOverallXScroll = 0;
    private float mMaxScale = 0.5f;
    private ScrollAdapter mAdapter;
    private float mClickDownX;
    private float mClickDownY;
    private RecyclerView mRecyclerView;
    private TextView mAnimationTv;
    private MoreOptionHelper moreOptionHelper;
    private AppCompatImageView mIvMore;


    public static void start(Context context) {
        context.startActivity(new Intent(context, RecyclerScrollActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_scroll);
        initView();
        initListener();
    }

    private void initListener() {
        mAdapter.setClickListener(new ScrollAdapter.ItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                Toast.makeText(RecyclerScrollActivity.this, "click position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setLongClickListener(new ScrollAdapter.ItemLongClickListener() {
            @Override
            public boolean itemLongClick(View view, int position) {
                showPopupWindow(view, position);
                //触摸反馈震动
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                view.playSoundEffect(SoundEffectConstants.CLICK);
                return true;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mOverallXScroll = mOverallXScroll + dy;
                if (mOverallXScroll <= 0) {
                    viewScale(1f);
                } else if (mOverallXScroll <= mHeight) {
                    float scale = (float) mOverallXScroll / mHeight;
                    Log.d(TAG, "scale = " + scale);
                    viewScale(Math.max(1 - scale, mMaxScale));
                } else {
                    viewScale(mMaxScale);
                }
            }
        });

        propertyAnimation(mAnimationTv, "backgroundColor", 0xFF000000, 0xFFFF0000, 0xFF00FF00);
        propertyAnimation(mAnimationTv, "textColor", 0xFF00FF00, 0xFFFF0000, 0xFF000000);

        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionHelper.showMoreOption(mIvMore);
            }
        });
    }

    private void initView() {
        mTitleView = findViewById(R.id.title);
        mAnimationTv = findViewById(R.id.animation);
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.suppressLayout(false);

        mAdapter = new ScrollAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
        // 初始化更多操作类
        moreOptionHelper = new MoreOptionHelper(this, mCallback);
        mIvMore = findViewById(R.id.more);
    }

    private MoreOptionHelper.MoreOptionCallback mCallback = new MoreOptionHelper.MoreOptionCallback() {
        @Override
        public void share() {
            Toast.makeText(RecyclerScrollActivity.this, "share", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void edit() {
            Toast.makeText(RecyclerScrollActivity.this, "edit", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void rename() {
            Toast.makeText(RecyclerScrollActivity.this, "rename", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void delete() {
            Toast.makeText(RecyclerScrollActivity.this, "delete", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mClickDownX = event.getRawX();
            mClickDownY = event.getRawY();
        }
        return super.dispatchTouchEvent(event);
    }

    private void showPopupWindow(View view, final int position) {
        //长按菜单管理
        MenuManagerHelper.getInstance().showPopupWindow(view, mClickDownX, mClickDownY, new MenuManagerHelper.MenuClickCallback() {
            @Override
            public void delete() {
                Toast.makeText(RecyclerScrollActivity.this, "delete position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void edit() {
                Toast.makeText(RecyclerScrollActivity.this, "edit position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rename() {
                Toast.makeText(RecyclerScrollActivity.this, "rename position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void share() {
                Toast.makeText(RecyclerScrollActivity.this, "share position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewScale(float scale) {
        int alpha = (int) (255 * scale);

        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24 * scale);
        mTitleView.setTextColor(Color.argb(alpha, 0, 0, 0));

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleView.getLayoutParams();
        params.height = ScreenUtils.dip2px(60 * scale);
        mTitleView.setLayoutParams(params);

        mTitleView.setBackgroundColor(Color.argb(alpha, 41, 193, 246));
    }


    private void propertyAnimation(View view, String propertyName, int... values) {
        //对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
        ValueAnimator animator = ObjectAnimator.ofInt(view, propertyName, values);
        animator.setDuration(5000);
        //如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    private static class ArgbEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            int startInt = (Integer) startValue;
            /*
             * 起始颜色ARGB颜色通道拆分
             */
            float startA = ((startInt >> 24) & 0xff) / 255.0f;
            float startR = ((startInt >> 16) & 0xff) / 255.0f;
            float startG = ((startInt >> 8) & 0xff) / 255.0f;
            float startB = (startInt & 0xff) / 255.0f;
            /*
             * 结束颜色ARGB颜色通道拆分
             */
            int endInt = (Integer) endValue;
            float endA = ((endInt >> 24) & 0xff) / 255.0f;
            float endR = ((endInt >> 16) & 0xff) / 255.0f;
            float endG = ((endInt >> 8) & 0xff) / 255.0f;
            float endB = (endInt & 0xff) / 255.0f;


            // convert from sRGB to linear
            startR = (float) Math.pow(startR, 2.2);
            startG = (float) Math.pow(startG, 2.2);
            startB = (float) Math.pow(startB, 2.2);

            endR = (float) Math.pow(endR, 2.2);
            endG = (float) Math.pow(endG, 2.2);
            endB = (float) Math.pow(endB, 2.2);
            /*
             *根据动画时间因子，计算出中间的过渡颜色
             */
            // compute the interpolated color in linear space
            float a = startA + fraction * (endA - startA);
            float r = startR + fraction * (endR - startR);
            float g = startG + fraction * (endG - startG);
            float b = startB + fraction * (endB - startB);

            // convert back to sRGB in the [0..255] range
            a = a * 255.0f;
            r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
            g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
            b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;
            /*
             *重新将分离的颜色通道组合返回过渡颜色
             */
            return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
        }
    }


    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("开启悬浮球");
        list.add("使用指纹");
        list.add("Wi-Fi");
        list.add("移动网络");
        list.add("开启悬浮球");
        list.add("使用指纹");
        list.add("Wi-Fi");
        list.add("移动网络");
        list.add("开启悬浮球");
        list.add("使用指纹");
        list.add("Wi-Fi");
        list.add("移动网络");
        list.add("开启悬浮球");
        list.add("使用指纹");
        list.add("Wi-Fi");
        list.add("移动网络");
        list.add("开启悬浮球");
        list.add("使用指纹");
        list.add("Wi-Fi");
        list.add("移动网络");
        list.add("开启悬浮球");
        list.add("使用指纹");
        list.add("Wi-Fi");
        list.add("移动网络");
        // 创建数据源
        return list;
    }

}
