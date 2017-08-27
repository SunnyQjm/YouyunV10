package com.sunny.youyun.fragment.main.message_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.base.MVPBaseFragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MessageFragment extends MVPBaseFragment<MessagePresenter> implements MessageContract.View {

    public static MessageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
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
    protected MessagePresenter onCreatePresenter() {
        return new MessagePresenter(this);
    }
}
