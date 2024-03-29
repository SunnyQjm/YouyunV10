package com.sunny.youyun.fragment.main.main_fragment.upload_record_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.manager.CheckStateManager;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.main_fragment.adapter.FileRecordAdapter;
import com.sunny.youyun.internet.event.FileUploadEvent;
import com.sunny.youyun.internet.upload.FileUploadPosition;
import com.sunny.youyun.internet.upload.FileUploader;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.event.MultiSelectEvent;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sunny.youyun.model.InternetFile.Status.CANCEL;
import static com.sunny.youyun.model.InternetFile.Status.DOWNLOADING;
import static com.sunny.youyun.model.InternetFile.Status.ERROR;
import static com.sunny.youyun.model.InternetFile.Status.FINISH;
import static com.sunny.youyun.model.InternetFile.Status.PAUSE;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class UploadRecordFragment extends MVPBaseFragment<UploadRecordPresenter>
        implements UploadRecordContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private FileRecordAdapter adapter;
    private List<InternetFile> mList = null;

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
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView() {
        adapter = new FileRecordAdapter(App.mList_UploadRecord);
        mList = adapter.getData();
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemChildClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(activity,
                RecyclerViewDividerItem.VERTICAL, true));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
    }

    /**
     * 删除item
     *
     * @param position
     */
    private void delete(int position) {
        if (position >= mList.size())
            return;
        InternetFile internetFile = adapter.getItem(position);
        if (internetFile != null)
            internetFile.delete();
        adapter.remove(position);
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
        update(position);
    }

    private void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void update(int position) {
        if (adapter != null) {
            adapter.notifyItemChanged(position, mList.get(position));
        } else {
            Logger.i("adapter is null");
        }
    }

    /**
     * 上传回调
     *
     * @param uploadPosition
     */
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


    /**
     * 在文件暂停或者错误的情况下重新上传文件或者继续上传文件
     *
     * @param file
     * @param position
     */
    private void continueOrReUpload(InternetFile file, int position) {
        file.setStatus(DOWNLOADING);
        file.setRate("");
        update(position);
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
        file.setStatus(PAUSE);
        update(position);
        FileUploader.getInstance()
                .pause(file.getPath(), position);
    }


    /**
     * 显示多选器
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MultiSelect(MultiSelectEvent event) {
        switch (event.operator) {
            case SHOW:
                break;
            case HIDE:
                if (adapter != null) {
                    adapter.setMode(FileRecordAdapter.Mode.NORMAL);
                    adapter.notifyDataSetChanged();
                }
                break;
            case SELECT_ALL:
                for (int i = 0; i < mList.size(); i++) {
                    CheckStateManager.getInstance()
                            .put(mList.get(i).getPath_Time(), i,
                                    true);
                }
                adapter.notifyDataSetChanged();
                break;
            case DELETE:
                Integer[] result = CheckStateManager.getInstance().intResult();
                Arrays.sort(result, (o1, o2) -> o2 - o1);
                for (int position : result) {
                    delete(position);
                }
                break;
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        EventBus.getDefault()
                .post(new MultiSelectEvent(MultiSelectEvent.Operator.SHOW));
        InternetFile file = (InternetFile) adapter.getItem(position);
        if (file == null)
            return true;
        if (adapter instanceof FileRecordAdapter) {
            ((FileRecordAdapter) adapter).setMode(FileRecordAdapter.Mode.SELECT);
            CheckStateManager.init();
            CheckStateManager.getInstance().put(file.getPath_Time()
                    , position, true);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == null)
            return;
        String uuid = UUIDUtil.getUUID();
        InternetFile file = (InternetFile) adapter.getItem(position);
        if(file == null)
            return;
        //如果当前处于选择模式
        if (adapter instanceof FileRecordAdapter &&
                ((FileRecordAdapter) adapter).getMode() == FileRecordAdapter.Mode.SELECT) {
            if (CheckStateManager.getInstance().get(position)) {
                CheckStateManager.getInstance().put(file.getPath_Time(), position, false);
            } else {
                CheckStateManager.getInstance().put(file.getPath_Time(), position, true);
            }
            adapter.notifyItemChanged(position);
            return;
        }

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
                RouterUtils.open(activity, IntentRouter.FileDetailOffLineActivity, uuid, String.valueOf(position));
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.checkBox:
                break;
        }
    }
}
