package com.sunny.youyun.fragment.main.finding_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.RecordTabsAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.SearchAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.all.AllFragment;
import com.sunny.youyun.fragment.main.finding_fragment.concern.ConcernFragment;
import com.sunny.youyun.fragment.main.finding_fragment.hot.HotFragment;
import com.sunny.youyun.utils.RecyclerViewUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.MyPopupWindow;
import com.sunny.youyun.views.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public class FindingFragment extends MVPBaseFragment<FindingPresenter> implements FindingContract.View {


    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    Unbinder unbinder;

    private View view = null;
    private AllFragment allFragment;
    private ConcernFragment concernFragment;
    private HotFragment hotFragment;
    private RecordTabsAdapter recordTabsAdapter;
    private static final int TAB_MARGIN_LEFT = 40;
    private static final int TAB_MARGIN_RIGHT = 40;

    //搜索window控件
    private MyPopupWindow searchPopupWindow = null;
    private ImageView imgSearch = null;
    private EditText etSearch = null;
    private RecyclerView recyclerView = null;
    private SearchAdapter searchAdapter = null;

    public static FindingFragment newInstance() {
        Bundle args = new Bundle();
        FindingFragment fragment = new FindingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_finding, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }

        return view;
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.finding_square));
        easyBar.setDisplayMode(EasyBar.Mode.ICON);
        easyBar.setLeftIconInVisible();
        easyBar.setRightIconVisible();
        easyBar.setRightIcon(R.drawable.icon_search);
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {

            }

            @Override
            public void onRightIconClick(View view) {
                //TODO Open search window
                showSearchWindow(view);
            }
        });
        allFragment = AllFragment.newInstance();
        concernFragment = ConcernFragment.newInstance();
        hotFragment = HotFragment.newInstance();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(allFragment);
        fragments.add(concernFragment);
        fragments.add(hotFragment);
        recordTabsAdapter = new RecordTabsAdapter(getChildFragmentManager(), fragments,
                new String[]{getString(R.string.all), getString(R.string.concern),
                        getString(R.string.hot)});
        viewpager.setAdapter(recordTabsAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setScroll(true);

        tabLayout.setupWithViewPager(viewpager);
        //使标题居中且宽度充满全屏
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        RecyclerViewUtils.setIndicator(activity, tabLayout, TAB_MARGIN_LEFT, TAB_MARGIN_RIGHT);

        //创建搜索window
        createSearchWindow();
    }

    /**
     * 显示搜索窗口
     *
     * @param view
     */
    private void showSearchWindow(View view) {
        if (!searchPopupWindow.isShowing()) {
            searchPopupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
        }
    }

    /**
     * 创建搜索视图
     */
    private void createSearchWindow() {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.finding_search_view_window, null, false);
        searchPopupWindow = new MyPopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        searchPopupWindow.setAnimationStyle(R.style.RightPopupWindowStyle);
        imgSearch = (ImageView) view.findViewById(R.id.img_search);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        imgSearch.setOnClickListener(v -> {
            String str = etSearch.getText().toString();
            mPresenter.search(str);
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(
                new RecyclerViewDividerItem(activity, RecyclerViewDividerItem.VERTICAL));
        searchAdapter = new SearchAdapter(mPresenter.getData());
        searchAdapter.bindToRecyclerView(recyclerView);
        searchAdapter.setEmptyView(R.layout.recycler_empty_view);
        searchPopupWindow.setOnDismissListener(() -> {
            if(searchAdapter != null){
                searchAdapter.getData().clear();
                searchAdapter.notifyDataSetChanged();
                etSearch.setText("");
            }
        });
    }

    @Override
    protected FindingPresenter onCreatePresenter() {
        return new FindingPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void searchSuccess() {
        if (searchAdapter != null)
            searchAdapter.notifyDataSetChanged();
    }
}
