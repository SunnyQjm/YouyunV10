package com.sunny.youyun.fragment.main.main_fragment.UploadRecordFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_detail_off_line.FileDetailOffLineActivity;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.MVPBaseFragment;
import com.sunny.youyun.fragment.main.main_fragment.Adapter.FileRecordAdapter;
import com.sunny.youyun.internet.event.FileUploadEvent;
import com.sunny.youyun.internet.upload.FileUploadPosition;
import com.sunny.youyun.internet.upload.FileUploader;
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
import static com.sunny.youyun.model.InternetFile.Status.CANCEL;
import static com.sunny.youyun.model.InternetFile.Status.DOWNLOADING;
import static com.sunny.youyun.model.InternetFile.Status.ERROR;
import static com.sunny.youyun.model.InternetFile.Status.FINISH;
import static com.sunny.youyun.model.InternetFile.Status.PAUSE;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class UploadRecordFragment extends MVPBaseFragment<UploadRecordPresenter> implements UploadRecordContract.View, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private FileRecordAdapter adapter;
    private List<InternetFile> mList = null;
    private MyPopupWindow popupWindow;
    private volatile int position = 0;

    private View view = null;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault()
                .register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault()
                .unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static UploadRecordFragment newInstance() {

        Bundle args = new Bundle();

        UploadRecordFragment fragment = new UploadRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_upload_record, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView(container);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView(ViewGroup container) {
        adapter = new FileRecordAdapter(R.layout.item_file_trans_record, App.mList_UploadRecord);
        mList = adapter.getData();
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

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
        if (position >= mList.size())
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
    protected UploadRecordPresenter onCreatePresenter() {
        return new UploadRecordPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnUploadFileCallback(FileUploadEvent fileUploadEvent) {
        if (fileUploadEvent.getPosition() >= mList.size()) {
            Logger.e("上传回调错误", new ArrayIndexOutOfBoundsException());
            return;
        }
        switch (fileUploadEvent.getType()) {
            case ERROR:
                EventBus.getDefault()
                        .post(new FileUploadPosition(fileUploadEvent.getPosition(), -2));
                break;
            case PAUSE:
                EventBus.getDefault()
                        .post(new FileUploadPosition(fileUploadEvent.getPosition()));
                break;
            case PROGRESS:
                EventBus.getDefault()
                        .post(new FileUploadPosition(fileUploadEvent.getPosition()));
                break;
            case FINISH:
                EventBus.getDefault()
                        .post(new FileUploadPosition(fileUploadEvent.getPosition(), -1));
                break;
            case START:
                EventBus.getDefault()
                        .post(new FileUploadPosition(fileUploadEvent.getPosition(), -1));
                break;
        }
    }


    private void error(int position) {
    }

    private void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void update(int position) {
        if (adapter != null) {
            adapter.notifyItemChanged(mList.size() - 1 - position, mList.get(position));
        } else {
            Logger.i("adapter is null");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(FileUploadPosition uploadPosition) {
        System.out.println(uploadPosition);
        switch (uploadPosition.judge) {
            case -1:
                updateAll();
                break;
            case -2:
                error(uploadPosition.position);
                break;
            case 0:
            default:
                update(uploadPosition.position);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == null)
            return;
        String uuid = UUIDUtil.getUUID();
        InternetFile file = mList.get(mList.size() - position - 1);
        ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
        switch (file.getStatus()) {
            case CANCEL:
            case PAUSE:
            case ERROR:
                continueOrReUpload(file, position);
                break;
            case DOWNLOADING:
                pause(file, position);
                break;
            case FINISH:        //如果文件下载成功则可进入详情页
                ObjectPool.getInstance().put(uuid, file);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Intent intent = new Intent(activity, FileDetailOffLineActivity.class);
                    intent.putExtra("uuid", uuid);
                    intent.putExtra("position", position);
                    RouterUtils.openWithAnimation(activity, intent, new Pair<>(img_icon, getString(R.string.trans_item_share_icon)));
                } else {
                    RouterUtils.open(activity, IndexRouter.FileDetailOffLineActivity, uuid, String.valueOf(position));
                }
                break;
        }
    }

    /**
     * 在文件暂停或者错误的情况下重新上传文件或者继续上传文件
     *
     * @param file
     * @param position
     */
    private void continueOrReUpload(InternetFile file, int position) {
        FileUploader.getInstance()
                .continueUpload(file.getPath(), position);
    }

    /**
     * 如果文件正在上传，则点击暂停上传
     *
     * @param file
     * @param position
     */
    private void pause(InternetFile file, int position) {
        FileUploader.getInstance()
                .pause(file.getPath(), position);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        this.position = position;
        return true;
    }
}
