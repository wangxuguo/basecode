package com.basecode.demo.view;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;

import java.util.ArrayList;
/**
 *  * 点击区域扩大
 * User: 王旭国
 * Date: 16/11/2 18:42
 * Email:wangxuguo@jhyx.com.cn
 */

public class TouchDelegateGroup extends TouchDelegate {
    private static final Rect USELESS_HACKY_RECT = new Rect();

    private ArrayList<TouchDelegate> mTouchDelegates;
    private TouchDelegate            mCurrentTouchDelegate;

    public TouchDelegateGroup(View uselessHackyView) {
        // I know this is pretty hacky. Unfortunately there is no other way to
        // create a TouchDelegate containing TouchDelegates since TouchDelegate
        // is not an interface ...
        super(USELESS_HACKY_RECT, uselessHackyView);
    }

    public void addTouchDelegate(TouchDelegate touchDelegate) {
        if (mTouchDelegates == null) {
            mTouchDelegates = new ArrayList<>();
        }
        mTouchDelegates.add(touchDelegate);
    }

    public void removeTouchDelegate(TouchDelegate touchDelegate) {
        if (mTouchDelegates != null) {
            mTouchDelegates.remove(touchDelegate);
            if (mTouchDelegates.isEmpty()) {
                mTouchDelegates = null;
            }
        }
    }

    public void clearTouchDelegates() {
        if (mTouchDelegates != null) {
            mTouchDelegates.clear();
        }
        mCurrentTouchDelegate = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        TouchDelegate delegate = null;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchDelegates != null) {
                    for (TouchDelegate touchDelegate : mTouchDelegates) {
                        if (touchDelegate != null) {
                            if (touchDelegate.onTouchEvent(event)) {
                                mCurrentTouchDelegate = touchDelegate;
                                return true;
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                delegate = mCurrentTouchDelegate;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                delegate = mCurrentTouchDelegate;
                mCurrentTouchDelegate = null;
                break;
        }

        return delegate == null ? false : delegate.onTouchEvent(event);
    }
}
