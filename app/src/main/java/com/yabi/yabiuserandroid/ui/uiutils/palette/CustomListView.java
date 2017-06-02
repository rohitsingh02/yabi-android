package com.yabi.yabiuserandroid.ui.uiutils.palette;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by yogeshmadaan on 04/12/15.
 */
public class CustomListView extends ListView {

    public interface ListViewObserver {
        void onScroll(float deltaY);
    }

    private ListViewObserver mObserver;
    private View mTrackedChild;
    private int mTrackedChildPrevPosition;

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int mTrackedChildPrevTop;
        if (mTrackedChild == null) {
            if (getChildCount() > 0) {
                mTrackedChild = getChildInTheMiddle();
                mTrackedChildPrevTop = mTrackedChild.getTop();
                mTrackedChildPrevPosition = getPositionForView(mTrackedChild);
            }
        } else {
            boolean childIsSafeToTrack = mTrackedChild.getParent() == this && getPositionForView(mTrackedChild) == mTrackedChildPrevPosition;
            if (childIsSafeToTrack) {
                int top = mTrackedChild.getTop();
                if (mObserver != null) {
                    mObserver.onScroll(top);
                }
                mTrackedChildPrevTop = top;
            } else {
                mTrackedChild = null;
            }
        }
    }

    private View getChildInTheMiddle() {
        return getChildAt(getChildCount() / 2);
    }

    public void setObserver(ListViewObserver observer) {
        mObserver = observer;
    }

}