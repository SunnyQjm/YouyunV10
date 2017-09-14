package com.sunny.youyun.activity.person_file_manager;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.adapter.ClassificationAdapter;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.config.ClassificationNumber;
import com.sunny.youyun.activity.person_file_manager.item.DirectItem;
import com.sunny.youyun.activity.person_file_manager.item.FileItem;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.nodes.ClassificationNode;
import com.sunny.youyun.utils.WindowUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.popupwindow.FileManagerOptionsPopupwindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Router(IntentRouter.PersonFileManagerActivity)
public class PersonFileManagerActivity extends MVPBaseActivity<PersonFileManagerPresenter> implements PersonFileManagerContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.classification_recycler_view)
    RecyclerView classificationRecyclerView;
    @BindView(R.id.filerecycler_view)
    RecyclerView filerecyclerView;

    private ClassificationAdapter classificationAdapter;
    private List<MultiItemEntity> files;
    private FileAdapter fileAdapter;

    private FileManagerOptionsPopupwindow popupwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_file_manager);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.file_manager));
        easyBar.setRightIconVisible();
        easyBar.setRightIcon(R.drawable.icon_new);
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {
                //TODO create new directory
            }
        });

        //初始化分类View
        initClassification();

        //初始化文件列表
        initFileList();

        popupwindow = FileManagerOptionsPopupwindow.bind(this, new FileManagerOptionsPopupwindow.OnOptionClickListener() {
            @Override
            public void onCancelClick() {

            }

            @Override
            public void onRenameClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }

            @Override
            public void onMoveClick(int position) {

            }

            @Override
            public void onShareClick(int position) {

            }

            @Override
            public void onDismiss() {
                WindowUtil.changeWindowAlpha(PersonFileManagerActivity.this, 1.0f);            }
        });
    }

    private void initFileList() {
        files = new ArrayList<>();
        files.add(new DirectItem(new InternetFile.Builder()
                .createTime(System.currentTimeMillis())
                .size(1024000000)
                .description("来自优云的分享")
                .name("电子书")
                .isDiretory(true)
        ));
        files.add(new DirectItem(new InternetFile.Builder()
                .createTime(System.currentTimeMillis())
                .size(1024000000)
                .description("来自优云的分享")
                .name("电影")
                .isDiretory(true)
        ));

        files.add(new FileItem(new InternetFile.Builder()
                .createTime(System.currentTimeMillis())
                .size(1024000000)
                .description("来自优云的分享")
                .name("青春正好.jpg")
                .isDiretory(false)
        ));

        filerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filerecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        fileAdapter = new FileAdapter(files);
        fileAdapter.bindToRecyclerView(filerecyclerView);
        fileAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
        fileAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            popupwindow.show(view, position);
            WindowUtil.changeWindowAlpha(PersonFileManagerActivity.this, 0.7f);
            return false;
        });
    }

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
            switch (position) {
                case ClassificationNumber.apk:
                    break;
                case ClassificationNumber.zip:
                    break;
                case ClassificationNumber.vedio:
                    break;
                case ClassificationNumber.music:
                    break;
                case ClassificationNumber.picture:
                    break;
                case ClassificationNumber.document:
                    break;
                case ClassificationNumber.webPage:
                    break;
                case ClassificationNumber.other:
                    break;
            }
        });
    }

    @Override
    protected PersonFileManagerPresenter onCreatePresenter() {
        return new PersonFileManagerPresenter(this);
    }
}
