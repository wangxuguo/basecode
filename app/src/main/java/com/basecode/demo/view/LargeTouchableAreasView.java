package com.basecode.demo.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basecode.R;

import static android.graphics.Color.*;

/**
 * 点击区域扩大
 * User: 王旭国
 * Date: 16/11/2 18:37
 * Email:wangxuguo@jhyx.com.cn
 */

public class LargeTouchableAreasView extends LinearLayout {
    private static final int TOUCH_ADDITION = 20;
    private static final int COLOR_SELECT_AREA = argb(50, 255, 0, 0);
    private static final int COLOR_STAR_AREA = argb(50, 0, 0, 255);

    public interface OnLargeTouchableAreasListener {
        void onSelected(LargeTouchableAreasView view, boolean selected);

        void onStarred(LargeTouchableAreasView view, boolean starred);
    }

    private static class TouchDelegateRecord {
        public Rect rect;
        public int  color;

        public TouchDelegateRecord(Rect _rect, int _color) {
            rect = _rect;
            color = _color;
        }
    }

    private final ArrayList<TouchDelegateRecord> mTouchDelegateRecords = new ArrayList<>();
    private final Paint                          mPaint                = new Paint();

    private ImageButton mSelectButton;
    private ImageButton mStarButton;
    private TextView    mTextView;

    private TouchDelegateGroup mTouchDelegateGroup;
    private OnLargeTouchableAreasListener mOnLargeTouchableAreasListener;

    private int mTouchAddition;

    private boolean mIsStarred;
    private boolean mIsSelected;

    private int mPreviousWidth = -1;
    private int mPreviousHeight = -1;

    public LargeTouchableAreasView(Context context) {
        super(context);
        init(context);
    }

    public LargeTouchableAreasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        setOrientation(LinearLayout.HORIZONTAL);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mTouchDelegateGroup = new TouchDelegateGroup(this);
        mPaint.setStyle(Paint.Style.FILL);

        final float density = context.getResources().getDisplayMetrics().density;
        mTouchAddition = (int) (density * TOUCH_ADDITION + 0.5f);

        LayoutInflater.from(context).inflate(R.layout.item_list_expand_clickable_area, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mSelectButton = (ImageButton) findViewById(R.id.btn_select);
        mSelectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemViewSelected(!mIsSelected);
                if (mOnLargeTouchableAreasListener != null) {
                    mOnLargeTouchableAreasListener.onSelected(LargeTouchableAreasView.this, mIsSelected);
                }
            }
        });

        mStarButton = (ImageButton) findViewById(R.id.btn_star);
        mStarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemViewStarred(!mIsStarred);
                if (mOnLargeTouchableAreasListener != null) {
                    mOnLargeTouchableAreasListener.onStarred(LargeTouchableAreasView.this, mIsStarred);
                }
            }
        });

        mTextView = (TextView) findViewById(R.id.content);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        final int width = r - l;
        final int height = b - t;

        if (width != mPreviousWidth || height != mPreviousHeight) {

            mPreviousWidth = width;
            mPreviousHeight = height;

            mTouchDelegateGroup.clearTouchDelegates();

            //@formatter:off
            addTouchDelegate(
                    new Rect(0, 0, mSelectButton.getWidth() + mTouchAddition, height),
                    COLOR_SELECT_AREA,
                    mSelectButton);

            addTouchDelegate(
                    new Rect(width - mStarButton.getWidth() - mTouchAddition, 0, width, height),
                    COLOR_STAR_AREA,
                    mStarButton);
            //@formatter:on

            setTouchDelegate(mTouchDelegateGroup);
        }
    }

    private void addTouchDelegate(Rect rect, int color, View delegateView) {
        mTouchDelegateGroup.addTouchDelegate(new TouchDelegate(rect, delegateView));
        mTouchDelegateRecords.add(new TouchDelegateRecord(rect, color));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        for (TouchDelegateRecord record : mTouchDelegateRecords) {
            mPaint.setColor(record.color);
            canvas.drawRect(record.rect, mPaint);
        }
        super.dispatchDraw(canvas);
    }

    public void setOnLargeTouchableAreasListener(OnLargeTouchableAreasListener listener) {
        mOnLargeTouchableAreasListener = listener;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setItemViewSelected(boolean selected) {
        if (mIsSelected != selected) {
            mIsSelected = selected;
            mSelectButton.setImageResource(mIsSelected ? R.mipmap.btn_check_on_normal : R.mipmap.btn_check_on_normal);
        }
    }

    public void setItemViewStarred(boolean starred) {
        if (mIsStarred != starred) {
            mIsStarred = starred;
            mStarButton.setImageResource(mIsStarred ? R.mipmap.btn_star_on_normal : R.mipmap.btn_star_on_normal);
        }
    }
}
