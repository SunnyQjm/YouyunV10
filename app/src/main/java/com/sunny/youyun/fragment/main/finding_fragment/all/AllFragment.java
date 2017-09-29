package com.sunny.youyun.fragment.main.finding_fragment.all;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;

public class AllFragment extends BaseRecyclerViewFragment<AllPresenter> implements AllContract.View, BaseQuickAdapter.OnItemClickListener {

    private View view = null;
    private FindingItemAdapter adapter;
    private int page = 1;

    public static AllFragment newInstance() {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AllPresenter onCreatePresenter() {
        return new AllPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("onCreateView");
        if(view == null){
            view = super.onCreateView(inflater, container, savedInstanceState);
            init();
        } else {
            super.onCreateView(inflater, container, savedInstanceState);
        }
        isPrepared = true;
        return view;
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void loadData() {
        if(!isVisible || !isPrepared)
            return;
        if(isFirst){
            page = 1;
            mPresenter.getForumDataALL(page, true);
            isFirst = false;
        }
    }

    private void init() {
        adapter = new FindingItemAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        mPresenter.getForumDataALL(page, true);
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
    }

    @Override
    protected void OnRefreshBeginSync() {
        mPresenter.getForumDataALL(page, true);
    }

    @Override
    protected void OnRefreshFinish() {
        getForumDataSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        page++;
        mPresenter.getForumDataALL(page, false);
    }

    @Override
    protected void onLoadFinish() {
        getForumDataSuccess();
    }

    @Override
    public void getForumDataSuccess() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Logger.i("adapter is null!");
        }
    }

    @Override
    public void allDataLoadFinish() {
        refreshLayout.setLoadAble(false);
        addDataLoadFinishFooter();
    }

    /**
     * 已经没有更多数据可以加载了，在最后添加一个布局
     */
    private void addDataLoadFinishFooter() {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        InternetFile internetFile = (InternetFile) adapter.getItem(position);
        if(internetFile == null)
            return;
        String uuid = UUIDUtil.getUUID();
        ObjectPool.getInstance()
                .put(uuid, internetFile);
        RouterUtils.open(activity, IntentRouter.FileDetailOnlineActivity, uuid);
    }
}
