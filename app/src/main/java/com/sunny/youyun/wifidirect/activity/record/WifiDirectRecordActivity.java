package com.sunny.youyun.wifidirect.activity.record;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.utils.RecyclerViewUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.wifidirect.activity.record.Adapter.RecordTabsAdapter;
import com.sunny.youyun.wifidirect.activity.record.send_record.SendRecordFragment;
import com.sunny.youyun.wifidirect.activity.record.receive_record.ReceiveRecordFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Router(IntentRouter.WifiDirectRecordActivity)
public class WifiDirectRecordActivity extends MVPBaseActivity<WifiDirectRecordPresenter> implements
        WifiDirectRecordContract.View, MVPBaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private SendRecordFragment downloadRecordFragment;
    private ReceiveRecordFragment uploadRecordFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private RecordTabsAdapter adapter;
    private static final int TAB_MARGIN_LEFT = 40;

    private static final int TAB_MARGIN_RIGHT = 40;
    private static final int DURATION = 300;
    public static final int DOWNLOAD_CODE = 233;
    public static final int PATH_S = 234;
    private static final int UPLOAD_SETTING = 235;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.trans_record));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });
        downloadRecordFragment = SendRecordFragment.newInstance();
        uploadRecordFragment = ReceiveRecordFragment.newInstance();
        fragmentList.add(downloadRecordFragment);
        fragmentList.add(uploadRecordFragment);
        adapter = new RecordTabsAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewpager);
        //使标题居中且宽度充满全屏
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        RecyclerViewUtils.setIndicator(this, tabLayout, TAB_MARGIN_LEFT, TAB_MARGIN_RIGHT);
    }

    @Override
    protected WifiDirectRecordPresenter onCreatePresenter() {
        return new WifiDirectRecordPresenter(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
