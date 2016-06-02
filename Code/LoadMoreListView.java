package com.jointwisdom.maintenance.util.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jointwisdom.maintenance.R;
import com.jointwisdom.maintenance.util.Logger;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by zgyi on 2016/5/19.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private View mFootView;
    private int mFootHeight;
    private boolean isAddMore = true;
    private TextView mTextView;
    private ProgressWheel mProgressbar;

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.setOnScrollListener(this);
        mFootView = View.inflate(getContext(), R.layout.listview_footer, null);
        mTextView = (TextView) mFootView.findViewById(R.id.pull_to_refresh_loadmore_text);
        mProgressbar = (ProgressWheel) mFootView.findViewById(R.id.pull_to_refresh_load_progress);
        mFootView.measure(0, 0);// 手工测量大小
        mFootHeight = mFootView.getMeasuredHeight();
        this.addFooterView(mFootView);
    }

    /**
     * 隐藏FootView
     */
    public void flushFinish() {
        isAddMore = false;
//         隐藏 footView
//    mFootView.setVisibility(View.VISIBLE);

    }

    /**
     * 重新设置FootView的转态
     */
    public void reSetStatus() {
        mTextView.setText("加载数据...");
        mProgressbar.setVisibility(View.VISIBLE);
    }

    /**
     * 设置没有更多数据的状态
     */
    public void noMoreData() {
        mTextView.setText("没有更多数据了!");
        mProgressbar.setVisibility(View.INVISIBLE);

    }

    /**
     * 设置FootView的错误信息
     *
     * @param errorStr 错误描述
     */
    public void setError(String errorStr) {
        mTextView.setText(errorStr + "");
        mProgressbar.setVisibility(View.INVISIBLE);
    }

    private loadMoreListener loadMoreListener;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition == getCount() - 1 && canLoad && isAddMore != true) {
                isAddMore = true;
                reSetStatus();
                mFootView.setVisibility(View.VISIBLE);
                if (loadMoreListener != null) {
                    loadMoreListener.onLoadMore();
                }

            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface loadMoreListener {

        /**
         * 上拉加载现多的回调方法
         */
        void onLoadMore();
    }

    public void setloadMoreListener(loadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private int mYDown;
    private int mLastY;
    private boolean canLoad = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        canLoad = false;
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //判断手指滑动的距离 防止误触
                if (mYDown - mLastY > 100) {
                    canLoad = true;
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
