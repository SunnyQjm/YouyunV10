package com.sunny.youyun.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
import com.sunny.youyun.model.event.RefreshEvent;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.utils.idling.EspressoIdlingResource;
import com.sunny.youyun.views.easy_refresh.ArrowRefreshHeader;
import com.sunny.youyun.views.easy_refresh.CustomLinerLayoutManager;
import com.sunny.youyun.views.easy_refresh.EasyRefreshFooter;
import com.sunny.youyun.views.easy_refresh.EasyRefreshFooterHandler;
import com.sunny.youyun.views.easy_refresh.EasyRefreshHeaderHandler;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 自定义的一个Fragment
 * 包含一个title和RecyclerView
 * 并且支持下拉刷新和上拉加载更多
 * 同时支持懒加载
 * Created by Sunny on 2017/8/29 0029.
 */

public abstract class BaseRecyclerViewFragment<P extends BasePresenter> extends MVPBaseFragment<P>
        implements EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    protected EasyRefreshLayout refreshLayout;
    protected CustomLinerLayoutManager linerLayoutManager;

    //当前Fragment是否可见
    protected boolean isVisible = false;
    //标志位，标识Fragment是否已经完成初始化
    protected boolean isPrepared = false;
    //是否是第一次
    protected boolean isFirst = true;
    protected View endView = null;

    Unbinder unbinder;

    protected View view = null;
    protected BaseQuickAdapter adapter = null;
    protected int page = 1;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault()
                .register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshFinish(null);
        EventBus.getDefault()
                .unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.base_recycler_view_layout, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
            init();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        isPrepared = true;
        return view;
    }

    protected void loadData() {
        if (!isVisible || !isPrepared)
            return;
        if (isFirst) {
            EspressoIdlingResource.getInstance()
                    .increment();
            loadData(true);
            isFirst = false;
        }
    }


    protected void onRefreshBegin() {
        if (adapter != null && adapter.getFooterLayout() != null) {
            adapter.getFooterLayout().setVisibility(View.GONE);
        }
        refreshLayout.setLoadAble(true);
    }

    protected void OnRefreshBeginSync() {
        EspressoIdlingResource.getInstance()
                .increment();
        loadData(true);
    }

    protected void onLoadBeginSync() {
        EspressoIdlingResource.getInstance()
                .increment();
        loadData(false);
    }

    protected void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void allDataLoadFinish() {
        if (endView == null) {
            endView = LayoutInflater.from(activity)
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

    protected abstract void loadData(boolean isRefresh);

    protected abstract void init();


    /**
     * 下面的函数由系统调用
     * 在Fragment可见时加载数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {
    }

    protected void onVisible() {
        //加载数据
        loadData();
    }


    private void initView() {
        linerLayoutManager = new CustomLinerLayoutManager(activity);
        recyclerView.setLayoutManager(linerLayoutManager);
        //设置数据充满布局才允许上拉加载更多
        refreshLayout.setLoadOnlyDataFullScreen(true);
        //设置头布局
        EasyRefreshHeaderHandler easyRefreshHeaderHandler = new ArrowRefreshHeader(R.layout.easy_refresh_header);
        refreshLayout.setHeader(easyRefreshHeaderHandler);
        //设置尾布局
        EasyRefreshFooterHandler easyRefreshFooterHandler = new EasyRefreshFooter(R.layout.easy_refresh_footer);
        refreshLayout.setFooter(easyRefreshFooterHandler);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        this.onRefreshBegin();
        linerLayoutManager.setScrollAble(false);
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            this.OnRefreshBeginSync();
            Thread.sleep(500);
            e.onNext(0);
        })
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(o -> {
                    this.OnRefreshFinish();
                });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshFinish(RefreshEvent refreshEvent) {
        if(progressBar.getVisibility() == View.VISIBLE && !isFirst){
            progressBar.setVisibility(View.INVISIBLE);
            adapter.setEmptyView(R.layout.recycler_empty_view);
        }
        linerLayoutManager.setScrollAble(true);
        refreshLayout.closeRefresh();
        refreshLayout.closeLoad();
    }


    @Override
    public void onLoad() {
        linerLayoutManager.setScrollAble(false);
        Observable.create((ObservableOnSubscribe<Integer>) e -> {
            this.onLoadBeginSync();
            Thread.sleep(500);
            e.onNext(0);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    this.onLoadFinish();
                });
    }

    protected void OnRefreshFinish() {
    }

    protected void onLoadFinish() {
    }
}
