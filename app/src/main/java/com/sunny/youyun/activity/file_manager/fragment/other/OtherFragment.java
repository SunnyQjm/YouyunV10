package com.sunny.youyun.activity.file_manager.fragment.other;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.adpater.FileAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.base.entity.MyDecoration;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OtherFragment extends MVPBaseFragment<OtherPresenter> implements OtherContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private FileAdapter adapter = null;
    private View view = null;
    private final Stack<Integer> clickPath = new Stack<>();

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public static OtherFragment newInstance() {
        Bundle args = new Bundle();
        OtherFragment fragment = new OtherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_other, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        return view;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FileAdapter(mPresenter.getData(), mListener);
        recyclerView.addItemDecoration(new MyDecoration(activity, MyDecoration.VERTICAL_LIST));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

        //显示根目录
        mPresenter.show(-1);

        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            if (clickPath.isEmpty() || position > 1) {
                clickPath.add(layoutManager.findLastVisibleItemPosition());
                mPresenter.show(position);
                return;
            }
            if (position == 1)
                onBackPressed();
            else {
                clickPath.clear();
                mPresenter.show(position);
            }

        });
    }

    @Override
    public boolean onBackPressed() {
        if (mPresenter.isRootPath()) {
            return false;
        } else {
            mPresenter.back();
            if (!clickPath.isEmpty())
                recyclerView.scrollToPosition(clickPath.pop());
            return true;
        }
    }

    @Override
    protected OtherPresenter onCreatePresenter() {
        return new OtherPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void updateUI() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSuccess(String info) {

    }

    @Override
    public void showError(String info) {

    }

    @Override
    public void allDataLoadFinish() {

    }
}
