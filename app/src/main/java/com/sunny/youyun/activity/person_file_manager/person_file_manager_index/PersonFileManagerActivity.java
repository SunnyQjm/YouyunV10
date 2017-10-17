package com.sunny.youyun.activity.person_file_manager.person_file_manager_index;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.adapter.ClassificationAdapter;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.adapter.PathAdapter;
import com.sunny.youyun.activity.person_file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.person_file_manager.item.FileItem;
import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivityLazy;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.EasyYouyunAPIManager;
import com.sunny.youyun.model.callback.SimpleListener;
import com.sunny.youyun.model.nodes.ClassificationNode;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.WindowUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.CustomLinerLayoutManager;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;
import com.sunny.youyun.views.popupwindow.FileManagerOptionsPopupwindow;
import com.sunny.youyun.views.popupwindow.TopMenuPopupWindow;
import com.sunny.youyun.views.popupwindow.directory_select.DirectSelectPopupWindow;
import com.sunny.youyun.views.popupwindow.directory_select.DirectorySelectManager;
import com.sunny.youyun.views.youyun_dialog.edit.YouyunEditDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

@Router(IntentRouter.PersonFileManagerActivity)
public class PersonFileManagerActivity extends BaseRecyclerViewActivityLazy<PersonFileManagerPresenter>
        implements PersonFileManagerContract.View {

    @BindView(R.id.classification_recycler_view)
    RecyclerView classificationRecyclerView;

    private RecyclerView pathRecyclerView = null;
    private PathAdapter pathAdapter = null;
    private String currentParentId = null;

    private TopMenuPopupWindow myOptionsPopupWindow = null;

    private ClassificationAdapter classificationAdapter;
    private FileManagerOptionsPopupwindow popupwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_file_manager);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 加载数据回调，由刷新和加载动作回调
     *
     * @param isRefresh
     */
    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getFilesByPath(currentParentId, page, isRefresh);
    }

    private void init() {
        currentParentId = null;

        //getView
        easyBar = (EasyBar) findViewById(R.id.easyBar);
        easyBar.setTitle(getString(R.string.file_manager));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (EasyRefreshLayout) findViewById(R.id.refreshLayout);
        initView();
        easyBar.setRightIconVisible();
        easyBar.setRightIcon(R.drawable.icon_add1);
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
        adapter = new FileAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

        //单击监听
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

        //长点击监听
        adapter.setOnItemLongClickListener((adapter, view, position) -> {
            popupwindow.show(view, position);
            WindowUtil.changeWindowAlpha(this, true);
            return false;
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
        pathAdapter.bindToRecyclerView(pathRecyclerView);
        pathAdapter.setOnItemClickListener((adapter, view, position) -> jumpTo(position));
        load(true);
        showLoading();

        //初始化分类View
        initClassification();

        popupwindow = FileManagerOptionsPopupwindow.bind(this,
                new FileManagerOptionsPopupwindow.OnOptionClickListener() {
                    @Override
                    public void onCancelClick() {
                    }

                    @Override
                    public void onRenameClick(int position) {
                        popupwindow.dismiss(position);
                        rename(position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        popupwindow.dismiss(position);
                        delete(position);
                    }

                    @Override
                    public void onMoveClick(int position) {
                        popupwindow.dismiss(position);
                        move(position);
                    }

                    @Override
                    public void onShareClick(int position) {
                        //TODO share F
                    }

                    @Override
                    public void onDismiss() {
                        WindowUtil.changeWindowAlpha(PersonFileManagerActivity.this, false);
                    }
                });
    }

    /**
     * 显示菜单可选项
     */
    private void showOptions() {
        if (myOptionsPopupWindow == null) {
            myOptionsPopupWindow = new TopMenuPopupWindow(this, new TopMenuPopupWindow.OnSelectListener() {
                @Override
                public void onCreateDirectory() {
                    myOptionsPopupWindow.dismiss();
                    createNewDirectory();
                }

                @Override
                public void onUpload() {
                    RouterUtils.open(PersonFileManagerActivity.this,
                            IntentRouter.FileManagerActivity, currentParentId, pathAdapter.getData()
                                    .get(pathAdapter.getData().size() - 1).getPath());
                    myOptionsPopupWindow.dismiss();
                }
            });
        }
        WindowUtil.changeWindowAlpha(PersonFileManagerActivity.this, true);
        myOptionsPopupWindow.show(easyBar.getRightIcon());
        myOptionsPopupWindow.setOnDismissListener(() -> {
            WindowUtil.changeWindowAlpha(PersonFileManagerActivity.this, false);
        });
    }

    /**
     * 删除文件或文件夹
     *
     * @param position
     */
    private void delete(int position) {
        MultiItemEntity multiItemEntity = (MultiItemEntity) adapter.getItem(position);
        if (multiItemEntity == null) {
            return;
        }
        FileItem fileItem = (FileItem) multiItemEntity;
        mPresenter.delete(fileItem.getSelfId(), position);
    }

    /**
     * 移动文件或文件夹
     *
     * @param position
     */
    private void move(int position) {
        if (position >= adapter.getData().size())
            return;
        FileItem fileItem = (FileItem) adapter.getItem(position);
        if (fileItem == null)
            return;
        DirectorySelectManager.getInstance(this)
                .setOnDismissListener(new DirectSelectPopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onResult(String pathName, String pathId) {
                        EasyYouyunAPIManager.movePath(fileItem.getSelfId(),
                                pathId, fileItem.getPathName(), new SimpleListener() {
                                    @Override
                                    public void onSuccess() {
                                        load(true);
                                    }

                                    @Override
                                    public void onFail() {
                                        showError(getString(R.string.move_fail));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        showError(getString(R.string.move_fail));
                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        mPresenter.addSubscription(d);
                                    }
                                });
                    }
                })
                .show(easyBar);
    }

    /**
     * 修改文件夹的名字
     *
     * @param position
     */
    private void rename(int position) {
        if (position >= adapter.getData().size())
            return;
        FileItem fileItem = (FileItem) adapter.getItem(position);
        if (fileItem == null || fileItem.getItemType() != ItemTypeConfig.TYPE_DIRECT_INFO)
            return;
        YouyunEditDialog.newInstance(getString(R.string.please_input_new_name),
                fileItem.getPathName(), result -> {
                    if (result == null || result.equals(""))
                        showTip(getString(R.string.not_alow_empty));
                    else
                        //是在父路径下修改文件夹的信息
                        EasyYouyunAPIManager.reName(fileItem.getSelfId(), null,
                                result, new SimpleListener() {
                                    @Override
                                    public void onSuccess() {
                                        load(true);
                                    }

                                    @Override
                                    public void onFail() {
                                        showError(getString(R.string.change_fail));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        showError(getString(R.string.change_fail));
                                    }

                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        mPresenter.addSubscription(d);
                                    }
                                });
                }).show(getSupportFragmentManager(), String.valueOf(this.getClass()),
                "");
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
                                load(true);
                            }

                            @Override
                            public void onFail() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onSubscribe(Disposable d) {
                                mPresenter.addSubscription(d);
                            }
                        });
                }).show(getSupportFragmentManager(), String.valueOf(this.getClass()),
                "");
    }

    /**
     * add path
     */
    public void addPath(@NonNull PathItem pathItem) {
        pathAdapter.addData(pathItem);
        currentParentId = pathItem.getParentId();
        load(true);
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
        currentParentId = pathItem.getParentId();
        load(true);
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
        if (size > 0) {
            PathItem pathItem = pathAdapter.getItem(size - 1);
            if (pathItem == null)
                return;
            currentParentId = pathItem.getParentId();
            load(true);
        }
    }

    /**
     * 分类视图初始化
     */
    private void initClassification() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                4, LinearLayoutManager.VERTICAL, false);
        //初始化分类
        classificationRecyclerView.setLayoutManager(gridLayoutManager);
        List<ClassificationNode> classificationNodes = new ArrayList<>();
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_apk)
                .name(getString(R.string.install_package))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_package)
                .name(getString(R.string.compression_pack))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_vedio)
                .name(getString(R.string.vedio))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_music)
                .name(getString(R.string.music))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_picture)
                .name(getString(R.string.picture))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_file)
                .name(getString(R.string.document))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_code)
                .name(getString(R.string.web_page))
                .build());
        classificationNodes.add(new ClassificationNode.Builder()
                .iconRes(R.drawable.icon_file_manage_others)
                .name(getString(R.string.other))
                .build());

        classificationAdapter = new ClassificationAdapter(classificationNodes);
        classificationAdapter.bindToRecyclerView(classificationRecyclerView);
        classificationAdapter.setOnItemClickListener((adapter, view, position) -> {
            RouterUtils.open(this, IntentRouter.PersonFileListActivity, String.valueOf(position));
        });
    }

    @Override
    protected PersonFileManagerPresenter onCreatePresenter() {
        return new PersonFileManagerPresenter(this);
    }

    @Override
    public void getFilesByPathSuccess() {
        updateAll();
    }

    @Override
    public void deleteSuccess(int position) {
        if (adapter != null)
            adapter.remove(position);
    }

    @Override
    public void getPathsSuccess() {
        if (pathAdapter != null)
            pathAdapter.notifyDataSetChanged();
    }


    /**
     * 复写父函数，不显示尾布局
     */
    @Override
    public void allDataLoadFinish() {
        if (adapter != null && adapter.getFooterLayout() != null) {
            adapter.getFooterLayout().setVisibility(View.VISIBLE);
        }

        //设置不可加载更多
        refreshLayout.setLoadAble(false);
        updateAll();
    }
}
