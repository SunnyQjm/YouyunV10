package com.sunny.youyun.wifidirect.activity.single_trans.main.main_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.base.MVPBaseFragment;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends MVPBaseFragment<MainPresenter> implements MainContract.View {
    @BindView(R.id.btn_i_want_receive_file)
    RichText btnIWantReceiveFile;
    @BindView(R.id.btn_i_want_send_file)
    RichText btnIWantSendFile;
    Unbinder unbinder;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        WifiDirectManager.getInstance().setWifiDirectEnable(true);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main2, container, false);
        unbinder = ButterKnife.bind(this, view);
        WifiDirectManager.getInstance().cancelConnect(null);
        WifiDirectManager.getInstance().disConnect();
        return view;
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_i_want_receive_file, R.id.btn_i_want_send_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_i_want_receive_file:
                paramListener.integerParam(1);
                break;
            case R.id.btn_i_want_send_file:
                paramListener.integerParam(2);
                break;
        }
    }
}
