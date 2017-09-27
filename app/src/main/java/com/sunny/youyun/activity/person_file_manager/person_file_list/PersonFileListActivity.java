package com.sunny.youyun.activity.person_file_manager.person_file_list;

import android.os.Bundle;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;

@Router(value = {IntentRouter.PersonFileListActivity + "/:type"},
        intParams = "type", stringParams = "parentId")
public class PersonFileListActivity extends BaseRecyclerViewActivity<PersonFileListPresenter> implements PersonFileListContract.View, BaseQuickAdapter.OnItemClickListener {

    private FileAdapter adapter;
    private int page = 1;
    private int type = DisplayTypeConfig.TYPE_DIVIDE_APPLICATION;
    private String parentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        adapter = new FileAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
        type = getIntent().getIntExtra("type", DisplayTypeConfig.TYPE_DIVIDE_APPLICATION);
        parentId = getIntent().getStringExtra("parentId");
        if(type > 10){
            refreshLayout.setLoadAble(false);
            mPresenter.getFileByPath(parentId);
        } else {
            mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
        }
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
    }

    @Override
    protected void OnRefreshBeginSync() {
        if(type < 10){
            mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
        } else {
            mPresenter.getFileByPath(parentId);
        }
    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadBeginSync() {
        page++;
        mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
    }

    @Override
    protected void onLoadFinish() {

    }

    @Override
    protected PersonFileListPresenter onCreatePresenter() {
        return new PersonFileListPresenter(this);
    }

    @Override
    public void getFileByTypeSuccess() {
        updateUI();
    }


    @Override
    public void getFileByPathSuccess() {
        updateUI();
    }

    private void updateUI(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //全路径查看
        if(type > 10){

        } else {    //分类查看
            if(adapter.getItem(position) == null || !(adapter.getItem(position) instanceof InternetFile)) {
                return;
            }
            InternetFile internetFile = (InternetFile) adapter.getItem(position);
            if(internetFile == null)
                return;
            internetFile = internetFile.copy();
            String uuid = UUIDUtil.getUUID();
            ObjectPool.getInstance()
                    .put(uuid, internetFile);
            RouterUtils.open(this, IntentRouter.FileDetailOnlineActivity, uuid);
        }
    }
}
