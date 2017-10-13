package com.sunny.youyun.base.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.ArrowRefreshHeader;
import com.sunny.youyun.views.easy_refresh.CustomLinerLayoutManager;
import com.sunny.youyun.views.easy_refresh.EasyRefreshFooter;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public abstract class BaseRecyclerViewActivityLazy<P extends BasePresenter> extends MVPBaseActivity<P>
        implements EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener {

    @BindView(R.id.easyBar)
    protected EasyBar easyBar;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    protected EasyRefreshLayout refreshLayout;

    protected CustomLinerLayoutManager linerLayoutManager;
    protected View endView = null;

    protected BaseQuickAdapter adapter = null;
    protected int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initView() {
        linerLayoutManager = new CustomLinerLayoutManager(this);
        recyclerView.setLayoutManager(linerLayoutManager);
        refreshLayout.setLoadOnlyDataFullScreen(true);
        refreshLayout.setHeader(new ArrowRefreshHeader(R.layout.easy_refresh_header));
        refreshLayout.setFooter(new EasyRefreshFooter(R.layout.easy_refresh_footer));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });
    }

    protected abstract void loadData(boolean isRefresh);

    protected void onRefreshBegin() {
        if (adapter != null && adapter.getFooterLayout() != null) {
            adapter.getFooterLayout().setVisibility(View.GONE);
        }
        refreshLayout.setLoadAble(true);
    }

    protected void OnRefreshBeginSync() {
        page = 1;
        loadData(true);
    }

    protected void OnRefreshFinish() {

    }

    protected void onLoadBeginSync() {
        page++;
        loadData(false);
    }

    protected void onLoadFinish() {

    }

    @Override
    public void onRefresh() {
        this.onRefreshBegin();
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            linerLayoutManager.setScrollAble(false);
            this.OnRefreshBeginSync();
            e.onNext(0);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    this.OnRefreshFinish();
                    linerLayoutManager.setScrollAble(true);
                    refreshLayout.closeRefresh();
                });

    }

    protected void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoad() {
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            linerLayoutManager.setScrollAble(false);
            this.onLoadBeginSync();
            e.onNext(0);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    linerLayoutManager.setScrollAble(true);
                    refreshLayout.closeLoad();
                    this.onLoadFinish();
                });
    }

    @Override
    public void allDataLoadFinish() {
        if (endView == null) {
            endView = LayoutInflater.from(this)
                    .inflate(R.layout.easy_refresh_end, null, false);
            adapter.addFooterView(endView);
        }
        if (adapter != null && adapter.getFooterLayout() != null) {
            adapter.getFooterLayout().setVisibility(View.VISIBLE);
        }

        //设置不可加载更多
        refreshLayout.setLoadAble(false);
        updateAll();
    }
}
