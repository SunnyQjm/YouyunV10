package com.sunny.youyun.fragment.main.main_fragment.DownloadReccordFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunny.youyun.App;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.MVPBaseFragment;
import com.sunny.youyun.fragment.main.main_fragment.Adapter.FileRecordAdapter;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.download.FileDownloadPosition;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.internet.event.FileDownloadEvent;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.MyPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.sunny.youyun.model.InternetFile.Status.*;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DownloadRecordFragment extends MVPBaseFragment<DownloadRecordPresenter> implements DownloadRecordContract.View, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private FileRecordAdapter adapter = null;
    private List<InternetFile> mList;
    private View view = null;
    private MyPopupWindow popupWindow = null;

    private int position = -1;
    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
        //开始监听
        mPresenter.beginListen();
    }

    public static DownloadRecordFragment newInstance() {
        Bundle args = new Bundle();
        DownloadRecordFragment fragment = new DownloadRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_download_record, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView(container);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView(ViewGroup container) {
        mList = App.mList_DownloadRecord;
        adapter = new FileRecordAdapter(R.layout.item_file_trans_record, mList);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        mPresenter.beginListen();

        //init popupWindow
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_window_bottom_layout, container, false);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> {
            if (popupWindow != null && popupWindow.isShowing())
                popupWindow.dismiss();
        });
        tv_delete.setOnClickListener(v -> {
            delete(position);
            if (popupWindow != null && popupWindow.isShowing())
                popupWindow.dismiss();
        });
        popupWindow = new MyPopupWindow(view, MATCH_PARENT, WRAP_CONTENT);
    }

    private void delete(int position) {
        if(position >= mList.size())
            return;
        InternetFile internetFile = adapter.getItem(position);
        if (internetFile != null)
            internetFile.delete();
        adapter.remove(position);
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
    protected DownloadRecordPresenter onCreatePresenter() {
        return new DownloadRecordPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onDownloadCallback(FileDownloadEvent fileDownloadEvent) {
        if (fileDownloadEvent.getPosition() >= mList.size())
            return;
        switch (fileDownloadEvent.getType()) {
            case START:
                EventBus.getDefault().post(new FileDownloadPosition(-1));
                break;
            case FINISH:
                EventBus.getDefault().post(new FileDownloadPosition(-1));
                break;
            case PAUSE:
                EventBus.getDefault().post(new FileDownloadPosition(-1));
                break;
            case PROGRESS:
                break;
            case ERROR:
                break;
        }
        EventBus.getDefault()
                .post(new FileDownloadPosition(fileDownloadEvent.getPosition()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(FileDownloadPosition fileDownloadPosition) {
        int position = fileDownloadPosition.position;
        if (position < 0) {
            updateAll();
            return;
        }
        if (adapter != null) {
            adapter.notifyItemChanged(mList.size() - 1 - position, mList.get(position));
        } else {
            System.out.println("adapter is null");
        }
    }

    private void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        position = mList.size() - 1 - position;
        InternetFile internetFile = mList.get(position);
        switch (internetFile.getStatus()) {
            case DOWNLOADING:
                pause(internetFile, position);
                break;
            case PAUSE:
                continueDownload(internetFile, position);
                break;
            case ERROR:
                continueDownload(internetFile, position);
                break;
            case FINISH:
                showDetailOffLine(internetFile, position);
                break;
            case CANCEL:
                break;
        }

    }

    private void continueDownload(InternetFile internetFile, int position) {
        FileDownloader.getInstance()
                .continueDownload(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD + internetFile.getIdentifyCode(), position);
    }

    private void pause(InternetFile internetFile, int position) {
        FileDownloader.getInstance()
                .pause(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD + internetFile.getIdentifyCode(), position);
    }

    private void showDetailOffLine(InternetFile internetFile, int position) {
        if (adapter == null)
            return;
        String uuid = UUIDUtil.getUUID();
        ObjectPool.getInstance().put(uuid, internetFile);
        RouterUtils.open(activity, IndexRouter.FileDetailOffLineActivity, uuid, String.valueOf(position));
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        this.position = position;
        return true;
    }
}
