package com.sunny.youyun.activity.decim;

import android.content.Context;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.utils.LocalDataGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

class DcimModel implements DcimContract.Model {
    private DcimPresenter mPresenter;
    private final List<FileItem> fileItems = new ArrayList<>();
    private final List<String> selectItems = new ArrayList<>();
    private Map<String, List<FileItem>> stringListMap = null;
    DcimModel(DcimPresenter dcimPresenter) {
        mPresenter = dcimPresenter;
    }

    @Override
    public List<FileItem> getFileItems() {
        return fileItems;
    }

    @Override
    public List<String> getSelectItems() {
        return selectItems;
    }

    @Override
    public void getData(Context context) {
        LocalDataGetter.getPictureSync(context, new LocalDataGetter.GetDataCallback<Map<String, List<FileItem>>>() {
            @Override
            public void onSuccess(Map<String, List<FileItem>> stringListMap) {
                fileItems.clear();
                selectItems.clear();
                DcimModel.this.stringListMap = stringListMap;
                if (stringListMap.size() <= 0)
                    return;
                for (Map.Entry<String, List<FileItem>> entry :
                        stringListMap.entrySet()) {
                    selectItems.add(entry.getKey());
                }
                fileItems.addAll(stringListMap.get(selectItems.get(0)));
                mPresenter.getDataSuccess(true);
            }

            @Override
            public void onFail(String msg) {
                mPresenter.showError(context.getString(R.string.get_fail));
            }
        });
    }

    @Override
    public void selected(int position) {
        if(stringListMap == null || stringListMap.size() <= position)
            return;
        String key = selectItems.get(position);
        fileItems.clear();
        fileItems.addAll(stringListMap.get(key));
        mPresenter.getDataSuccess(false);
    }
}
