package com.sunny.youyun.views.popupwindow.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.base.popupwindow.BaseMVPPopupwindow;
import com.sunny.youyun.views.popupwindow.search.adapter.SearchAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.config.SearchItemType;
import com.sunny.youyun.views.popupwindow.search.item.FileItem;
import com.sunny.youyun.fragment.main.finding_fragment.item.UserItem;
import com.sunny.youyun.utils.InputMethodUtil;
import com.sunny.youyun.utils.RouterUtils;

/**
 * Created by Sunny on 2017/10/19 0019.
 */

public class SearchWindow extends BaseMVPPopupwindow<SearchPresenter>
        implements BaseQuickAdapter.OnItemClickListener, SearchContract.View {
    private TextView imgSearch = null;
    private EditText etSearch = null;
    private RecyclerView recyclerView = null;
    private SearchAdapter searchAdapter = null;

    public SearchWindow(@NonNull AppCompatActivity context) {
        super(context, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.finding_search_view_window, null, false);
        this.setContentView(view);
        this.setAnimationStyle(R.style.RightPopupWindowStyle);
        imgSearch = (TextView) view.findViewById(R.id.img_search);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        imgSearch.setOnClickListener(v -> this.dismiss());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(
                new RecyclerViewDividerItem(context, RecyclerViewDividerItem.VERTICAL));
        searchAdapter = new SearchAdapter(mPresenter.getData());
        searchAdapter.bindToRecyclerView(recyclerView);
        searchAdapter.setEmptyView(R.layout.recycler_empty_view);
        searchAdapter.setOnItemClickListener(this);
        this.setOnDismissListener(() -> {
            if (searchAdapter != null) {
                searchAdapter.getData().clear();
                searchAdapter.notifyDataSetChanged();
                etSearch.setText("");
            }
        });
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                String str = etSearch.getText().toString();
                mPresenter.search(str);
                InputMethodUtil.hide(context, v);
            }
            return false;
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //TODO On search result click
        MultiItemEntity multiItemEntity = (MultiItemEntity) adapter.getItem(position);
        if (multiItemEntity == null)
            return;
        switch (multiItemEntity.getItemType()) {
            case SearchItemType.TYPE_FILE:
                if(multiItemEntity instanceof FileItem){
                    FileItem fileItem = (FileItem) multiItemEntity;
                    RouterUtils.openToFileDetailOnline(activity, fileItem);
                }
                break;
            case SearchItemType.TYPE_USER:
                UserItem userItem = (UserItem) multiItemEntity;
                RouterUtils.openToUser(activity, userItem.getId());
                break;
        }
    }

    @Override
    public void allDataLoadFinish() {
        searchSuccess();
    }

    @Override
    protected SearchPresenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void searchSuccess() {
        if(searchAdapter != null)
            searchAdapter.notifyDataSetChanged();
    }
}
