package com.sunny.youyun.fragment.main.finding_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.base.MVPBaseFragment;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public class FindingFragment extends MVPBaseFragment<FindingPresenter> implements FindingContract.View {


    public static FindingFragment newInstance() {
        Bundle args = new Bundle();
        FindingFragment fragment = new FindingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finding, container, false);
        return view;
    }

    @Override
    public void showSuccess(String info) {
        super.showSuccess(info);
    }

    @Override
    public void showError(String info) {
        super.showError(info);
    }

    @Override
    public void showTip(String info) {
        super.showTip(info);
    }

    @Override
    protected FindingPresenter onCreatePresenter() {
        return new FindingPresenter(this);
    }
}
