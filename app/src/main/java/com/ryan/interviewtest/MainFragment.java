package com.ryan.interviewtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ryan.interviewtest.adapter.RecyclerViewAdapter;
import com.ryan.interviewtest.base.FieldConfig;
import com.ryan.interviewtest.model.GankModel;
import com.ryan.interviewtest.model.ResultsBean;
import com.ryan.interviewtest.view.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GankModel.CallBack {
    private String mType;
    private Context mContext;
    private int mPage = 1;
    private boolean isVisibleToUser;
    private boolean isViewInit;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerViewAdapter mMyRVAdapter;
    private List<ResultsBean> mBeanList;
    private GestureDetector mGestureDetector;
    private boolean canLoadMore = false;
    private boolean isRefresh = true;
    private GankModel mGankModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mBeanList = new ArrayList<>();
        mMyRVAdapter = new RecyclerViewAdapter(mContext,mBeanList);
        mGankModel = new GankModel(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment,container,false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
        mGestureDetector = new GestureDetector(mContext,new GestureDetector.SimpleOnGestureListener(){ //这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法
                    //单击事件
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = mRecyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null){
                            int position = (int) childView.getTag();
                            if (position<mBeanList.size()){
                                Intent intent = new Intent(mContext,WebViewActivity.class);
                                intent.putExtra(WebViewActivity.URL, mBeanList.get(position).getUrl());
                                mContext.startActivity(intent);
                            }
                            return true;
                        }
                        return false;
                    }
                    //长按事件
                    @Override
                    public void onLongPress(MotionEvent e) {
                        /*View childView = mRecyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(childView != null){
                        }*/
                    }
                });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(mGestureDetector.onTouchEvent(e)){
                    return true;
                }else
                    return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mMyRVAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && canLoadMore){
                    isRefresh = false;
                    requestData(++mPage);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                canLoadMore = lastVisibleItemPosition >= mLayoutManager.getItemCount() - 1;
            }
        });
        isViewInit = true;
        requestData(mPage);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && isViewInit && (mBeanList != null && mBeanList.size() <= 0)) {
            requestData(mPage);
        }
    }

    private void requestData(int page){
        mGankModel.requestData(FieldConfig.BASE_URL, mType, page,this);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        isRefresh = true;
        deleteCache(); //删除缓存
        requestData(mPage);
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    /**
     * 删除缓存
     */
    private void deleteCache() {
        File cacheFile = new File(mContext.getExternalCacheDir().toString(),"cache");
        if (cacheFile.exists() && cacheFile.isDirectory()) {
            for (File item : cacheFile.listFiles()) {
                item.delete();
            }
        }
    }

    @Override
    public void bindData(List<ResultsBean> resultsBeen) {
        if (isRefresh){
            mBeanList.clear();
        }
        if (resultsBeen != null && resultsBeen.size()>0) {
            mGankModel.insertData(resultsBeen);
            mBeanList.addAll(resultsBeen);
            mMyRVAdapter.setCanLoadMore(true);
            mMyRVAdapter.notifyDataSetChanged();
        } else {
            mMyRVAdapter.setCanLoadMore(false);
            mMyRVAdapter.notifyItemChanged(mMyRVAdapter.getItemCount());
        }
        mRefreshLayout.setRefreshing(false);
    }
}
