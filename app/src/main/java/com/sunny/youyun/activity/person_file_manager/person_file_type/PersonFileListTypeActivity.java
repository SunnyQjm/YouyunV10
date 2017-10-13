package com.sunny.youyun.activity.person_file_manager.person_file_type;

import android.os.Bundle;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;

import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_APPLICATION;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_DOCUMENT;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_HTML;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_MUSIC;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_OTHER;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_PICTURE;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_VIDEO;
import static com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig.TYPE_DIVIDE_ZIP;

@Router(value = {IntentRouter.PersonFileListActivity + "/:type"}, intParams = "type")
public class PersonFileListTypeActivity extends BaseRecyclerViewActivity<PersonFileListTypePresenter>
        implements PersonFileListTypeContract.View, BaseQuickAdapter.OnItemClickListener {

    private int type = TYPE_DIVIDE_APPLICATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, isRefresh);
    }

    private void init() {
        adapter = new FileAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this,
                RecyclerViewDividerItem.VERTICAL));
        type = getIntent().getIntExtra("type", TYPE_DIVIDE_APPLICATION);
        updateTitle(type);
        mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
    }

    private void updateTitle(int type) {
        String title;
        switch (type) {
            case TYPE_DIVIDE_APPLICATION:
                title = getString(R.string.install_package);
                break;
            case TYPE_DIVIDE_ZIP:
                title = getString(R.string.compression_pack);
                break;
            case TYPE_DIVIDE_VIDEO:
                title = getString(R.string.vedio);
                break;
            case TYPE_DIVIDE_MUSIC:
                title = getString(R.string.music);
                break;
            case TYPE_DIVIDE_PICTURE:
                title = getString(R.string.picture);
                break;
            case TYPE_DIVIDE_DOCUMENT:
                title = getString(R.string.document);
                break;
            case TYPE_DIVIDE_HTML:
                title = getString(R.string.web_page);
                break;
            case TYPE_DIVIDE_OTHER:
            default:
                title = getString(R.string.other);
        }
        easyBar.setTitle(title);
    }

    @Override
    protected PersonFileListTypePresenter onCreatePresenter() {
        return new PersonFileListTypePresenter(this);
    }

    @Override
    public void getFileByTypeSuccess() {
        updateAll();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (adapter.getItem(position) == null || !(adapter.getItem(position) instanceof InternetFile)) {
                return;
            }
            InternetFile internetFile = (InternetFile) adapter.getItem(position);
            if (internetFile == null)
                return;
            internetFile = internetFile.copy();
            String uuid = UUIDUtil.getUUID();
            ObjectPool.getInstance()
                    .put(uuid, internetFile);
            RouterUtils.open(this, IntentRouter.FileDetailOnlineActivity, uuid);
    }
}
