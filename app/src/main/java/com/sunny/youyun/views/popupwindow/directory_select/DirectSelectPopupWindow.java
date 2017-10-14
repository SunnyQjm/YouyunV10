package com.sunny.youyun.views.popupwindow.directory_select;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.adapter.PathAdapter;
import com.sunny.youyun.activity.person_file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.person_file_manager.item.FileItem;
import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.popupwindow.BaseMVPPopupwindow;
import com.sunny.youyun.model.EasyYouyunAPIManager;
import com.sunny.youyun.model.callback.SimpleListener;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.CustomLinerLayoutManager;
import com.sunny.youyun.views.youyun_dialog.edit.YouyunEditDialog;

/**
 * Created by Sunny on 2017/10/14 0014.
 */

public class DirectSelectPopupWindow extends BaseMVPPopupwindow<DirectSelectPresenter>
        implements DirectSelectContract.View, View.OnClickListener {
    private final View view;
    private EasyBar easyBar = null;
    private RecyclerView pathRecyclerView = null;
    private PathAdapter pathAdapter = null;
    private RecyclerView recyclerView = null;
    private FileAdapter adapter = null;
    private TextView tvCancel;
    private TextView tvSure;
    private OnDismissListener onDismissListener = null;

    private int page = 1;
    private String currentParentId = null;
    private String currentPath = "/";

    private static final int TYPE_SELECT_DIRECTORY = 0;
    public static final int TYPE_MOVE_TO_PATH = 1;
    private int type = TYPE_SELECT_DIRECTORY;

    public DirectSelectPopupWindow(@NonNull final AppCompatActivity context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.activity = context;
        view = LayoutInflater.from(context)
                .inflate(R.layout.select_directory_popup_view, null, false);
        init_();
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 初始化
     */
    private void init_() {
        //对dismiss做一层封装，防止内存泄漏
        this.setOnDismissListener(() -> {
            mPresenter.clearAllDisposable();
            if (onDismissListener != null)
                onDismissListener.onDismiss();
        });

        //获取View
        easyBar = (EasyBar) view.findViewById(R.id.easyBar);
        easyBar.setRightIconVisible();
        easyBar.setRightIcon(R.drawable.icon_new);
        easyBar.setTitle(activity.getString(R.string.select_directory));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                dismiss();
            }

            @Override
            public void onRightIconClick(View view) {
                //TODO expand menu
                createNewDirectory();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new CustomLinerLayoutManager(activity));
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(activity,
                RecyclerViewDividerItem.VERTICAL));
        pathRecyclerView = (RecyclerView) view.findViewById(R.id.pathRecyclerView);
        pathRecyclerView.setLayoutManager(new CustomLinerLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false));

        adapter = new FileAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        pathAdapter = new PathAdapter(mPresenter.getPaths());
        //添加一个根目录
        pathAdapter.addData(new PathItem.Builder()
                .parentId(null)
                .path(activity.getString(R.string.root_path))
                .build());
        pathAdapter.bindToRecyclerView(pathRecyclerView);
        pathAdapter.setOnItemClickListener((adapter, view, position) -> jumpTo(position));
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //TODO file item click
            FileItem fileItem = (FileItem) adapter.getItem(position);
            if (fileItem == null)
                return;
            if (fileItem.getItemType() == ItemTypeConfig.TYPE_FILE_INFO) {            //点击的是文件
//                RouterUtils.openToFileDetailOnline(this, fileItem.getFile());
            } else if (fileItem.getItemType() == ItemTypeConfig.TYPE_DIRECT_INFO) {   //点击的是文件夹
                addPath(new PathItem.Builder()
                        .path(fileItem.getPathName())
                        .parentId(fileItem.getSelfId())
                        .build());
            }
        });
        //右边切入
        this.setAnimationStyle(R.style.RightPopupWindowStyle);
        this.setContentView(view);
        this.setBackgroundDrawable(new ColorDrawable(
                activity.getResources().getColor(R.color.light_gray)));

        loadData(true);

        //初始化底部按钮
        tvSure = (TextView) view.findViewById(R.id.tv_sure);
        tvSure.setText(activity.getString(R.string.select));
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvSure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    /**
     * 新建文件夹
     */
    private void createNewDirectory() {
        YouyunEditDialog.newInstance(activity.getString(R.string.create_new_directory),
                "", result -> {
                    if (result == null || result.equals(""))
                        showTip(activity.getString(R.string.not_alow_empty));
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
                }).show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()),
                "");
    }

    protected void loadData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        mPresenter.getFilesByPath(currentParentId, page, isRefresh);
    }

    /**
     * add path
     */
    private void addPath(@NonNull PathItem pathItem) {
        pathAdapter.addData(pathItem);
        currentParentId = pathItem.getParentId();
        currentPath = pathItem.getPath();
        loadData(true);
    }

    /**
     * back
     */
    private void popPath() {
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
    private void jumpTo(int position) {
        int size = pathAdapter.getData().size();
        for (int i = size - 1; i > position; i--) {
            pathAdapter.remove(i);
        }
        size = pathAdapter.getData().size();
        if (size > 0) {
            PathItem pathItem = pathAdapter.getItem(size - 1);
            if (pathItem == null)
                return;
            currentPath = pathItem.getPath();
            currentParentId = pathItem.getParentId();
            loadData(true);
        }
    }

    @Override
    public void allDataLoadFinish() {
        getFilesByPathSuccess();
    }

    @Override
    protected DirectSelectPresenter onCreatePresenter() {
        return new DirectSelectPresenter(this);
    }

    @Override
    public void getFilesByPathSuccess() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void getPathsSuccess() {
        if(pathAdapter != null)
            pathAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                if(onDismissListener != null){
                    onDismissListener.onResult(currentPath, currentParentId);
                }
                dismiss();
                break;
        }
    }


    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();
        void onResult(String pathName, String pathId);
    }
}
