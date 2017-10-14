package com.sunny.youyun.activity.person_file_manager.person_file_path;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.adapter.PathAdapter;
import com.sunny.youyun.activity.person_file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.person_file_manager.item.FileItem;
import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivityLazy;
import com.sunny.youyun.model.EasyYouyunAPIManager;
import com.sunny.youyun.model.callback.SimpleListener;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.CustomLinerLayoutManager;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;
import com.sunny.youyun.views.popupwindow.TopMenuPopupWindow;
import com.sunny.youyun.views.youyun_dialog.edit.YouyunEditDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

@Router(value = {IntentRouter.PersonFileListPathActivity + "/:currentParentId/:path",
        IntentRouter.PersonFileListPathActivity + "/:currentParentId/:path/:type"},
        stringParams = {"currentParentId", "path"})
public class PersonFileListPathActivity extends BaseRecyclerViewActivityLazy<PersonFileListPathPresenter>
        implements PersonFileListPathContract.View {

    private RecyclerView pathRecyclerView = null;
    private PathAdapter pathAdapter = null;
    private String currentParentId = null;
    private String currentPath = "/";

    private TopMenuPopupWindow myOptionsPopupWindow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_file_list_path);
        init();
    }

    private void init() {
        currentParentId = getIntent().getStringExtra("currentParentId");
        currentPath = getIntent().getStringExtra("path");

        easyBar = (EasyBar) findViewById(R.id.easyBar);
        easyBar.setTitle(getString(R.string.file_manager));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (EasyRefreshLayout) findViewById(R.id.refreshLayout);
        initView();
        easyBar.setRightIconVisible();
        easyBar.setRightIcon(R.drawable.icon_add);
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                finish();
            }

            @Override
            public void onRightIconClick(View view) {
                //TODO expand menu
                showOptions();
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this,
                RecyclerViewDividerItem.VERTICAL));
        adapter = new FileAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //TODO file item click
            FileItem fileItem = (FileItem) adapter.getItem(position);
            if (fileItem == null)
                return;
            if (fileItem.getItemType() == ItemTypeConfig.TYPE_FILE_INFO) {            //点击的是文件
                RouterUtils.openToFileDetailOnline(this, fileItem.getFile());
            } else if (fileItem.getItemType() == ItemTypeConfig.TYPE_DIRECT_INFO) {   //点击的是文件夹
                addPath(new PathItem.Builder()
                        .path(fileItem.getPathName())
                        .parentId(fileItem.getSelfId())
                        .build());
            }
        });

        //init path display
        pathRecyclerView = (RecyclerView) findViewById(R.id.pathRecyclerView);
        pathRecyclerView.setLayoutManager(new CustomLinerLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        pathAdapter = new PathAdapter(mPresenter.getPaths());
        //添加一个根目录
        pathAdapter.addData(new PathItem.Builder()
                .parentId(null)
                .path(getString(R.string.root_path))
                .build());
        //添加当前点击的目录
        pathAdapter.addData(new PathItem.Builder()
                .path(currentPath)
                .parentId(currentParentId)
                .build());
        pathAdapter.bindToRecyclerView(pathRecyclerView);
        pathAdapter.setOnItemClickListener((adapter, view, position) -> jumpTo(position));
        loadData(true);
    }

    /**
     * 显示菜单可选项
     */
    private void showOptions() {
        if (myOptionsPopupWindow == null) {
            List<String> options = new ArrayList<>();
            options.add(getString(R.string.create_new_directory));
            options.add(getString(R.string.upload_file_to_here));
            myOptionsPopupWindow = new TopMenuPopupWindow(this, (parent, view, position, id) -> {
                myOptionsPopupWindow.dismiss();
                switch (position){
                    case 0:     //create new directory
                        createNewDirectory();
                        break;
                    case 1:     //uploadFile
                        break;
                }
            }, options);
        }
        myOptionsPopupWindow.show(easyBar.getRightIcon());
    }

    private void createNewDirectory() {
        YouyunEditDialog.newInstance(getString(R.string.create_new_directory),
                "", result -> {
                    if (result == null || result.equals(""))
                        showTip(getString(R.string.not_alow_empty));
                    else
                        EasyYouyunAPIManager.createNewDirectory(currentParentId, result, new SimpleListener() {
                            @Override
                            public void onSuccess() {
                                loadData(true);
                            }

                            @Override
                            public void onFail() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                }).show(getSupportFragmentManager(), String.valueOf(this.getClass()),
                "");
    }

    @Override
    protected PersonFileListPathPresenter onCreatePresenter() {
        return new PersonFileListPathPresenter(this);
    }

    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getFilesByPath(currentParentId, page, isRefresh);
    }


    /**
     * 拦截返回响应
     */
    @Override
    public void onBackPressed() {
        if (pathAdapter.getData().size() <= 1)
            super.onBackPressed();
        popPath();
    }

    @Override
    public void getFilesByPathSuccess() {
        updateAll();
    }

    @Override
    public void getPathsSuccess() {
        if (pathAdapter != null)
            pathAdapter.notifyDataSetChanged();
    }

    /**
     * add path
     */
    public void addPath(@NonNull PathItem pathItem) {
        pathAdapter.addData(pathItem);
        currentParentId = pathItem.getParentId();
        currentPath = pathItem.getPath();
        loadData(true);
    }

    /**
     * back
     */
    public void popPath() {
        int size = pathAdapter.getData().size();
        if (size > 1)
            pathAdapter.remove(size - 1);
        PathItem pathItem = pathAdapter.getItem(pathAdapter.getData().size() - 1);
        if (pathItem == null)
            return;
        currentPath = pathItem.getPath();
        currentParentId = pathItem.getParentId();
        loadData(true);
    }

    /**
     * jump to a path
     *
     * @param position
     */
    public void jumpTo(int position) {
        int size = pathAdapter.getData().size();
        for (int i = size - 1; i > position; i--) {
            pathAdapter.remove(i);
        }
        size = pathAdapter.getData().size();
        if(size > 0){
            PathItem pathItem = pathAdapter.getItem(size - 1);
            if(pathItem == null)
                return;
            currentPath = pathItem.getPath();
            currentParentId = pathItem.getParentId();
            loadData(true);
        }
    }
}
