package com.sunny.youyun.wifidirect.activity.record.send_record;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunny.youyun.App;
import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.views.MyPopupWindow;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;
import com.sunny.youyun.wifidirect.activity.record.Adapter.FileRecordAdapter;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class SendRecordFragment extends MVPBaseFragment<SendRecordPresenter>
        implements SendRecordContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemLongClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private FileRecordAdapter adapter = null;
    private List<TransLocalFile> mList;
    private View view = null;
    private MyPopupWindow popupWindow = null;

    private int position = -1;
    private YouyunTipDialog youyunTipDialog = null;

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
        //开始监听
        mPresenter.beginListen();
    }

    public static SendRecordFragment newInstance() {
        Bundle args = new Bundle();
        SendRecordFragment fragment = new SendRecordFragment();
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
        mList = App.mList_ReceiveRecord;
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
        TransLocalFile transLocalFile = adapter.getItem(position);
        if (transLocalFile != null)
            transLocalFile.delete();
        adapter.remove(position);
    }

    private void showFileNotExistDialog(int position) {
        if (youyunTipDialog == null)
            youyunTipDialog = YouyunTipDialog.newInstance(R.drawable.icon_setting_cancel,
                    "文件不见啦！是否删除记录?", new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            youyunTipDialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            if (ActivityCompat.checkSelfPermission(activity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                RxPermissionUtil.getInstance(activity)
                                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .subscribe(aBoolean -> {
                                            if (aBoolean)
                                                delete(position);
                                        });
                                return;
                            }
                            delete(position);
                        }
                    });
        youyunTipDialog.show(getFragmentManager(), String.valueOf(this.getClass()));
    }

    private void openFile(TransLocalFile transLocalFile) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissionUtil
                    .getInstance(activity)
                    .request(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        System.out.println("获取：" + aBoolean);
                        if (aBoolean)
                            FileUtils.openFile(activity, transLocalFile.getPath());
                    });
        } else {
            FileUtils.openFile(activity, transLocalFile.getPath());
        }
    }

    @Override
    protected SendRecordPresenter onCreatePresenter() {
        return new SendRecordPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        position = mList.size() - 1 - position;
        TransLocalFile transLocalFile = mList.get(position);
        File file  = new File(transLocalFile.getPath());
        if(file.exists()){
            openFile(transLocalFile);
        } else {
            showFileNotExistDialog(position);
        }
    }


    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        this.position = position;
        return true;
    }
}
