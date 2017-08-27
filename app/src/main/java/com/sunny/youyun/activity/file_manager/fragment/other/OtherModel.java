package com.sunny.youyun.activity.file_manager.fragment.other;

import com.sunny.youyun.activity.file_manager.item.DirectItem;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.model.BaseLocalFileInfo;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class OtherModel implements OtherContract.Model {
    private final OtherPresenter mPresenter;

    private final List<MultiItemEntity> list = new ArrayList<>();
    private String currentPath = MY_ROOT_PATH;
    private final Stack<String> fileBackStack = new Stack<>();
    private static final String MY_ROOT_PATH = "com.sunny.youyun.my_root_path";

    OtherModel(OtherPresenter otherPresenter) {
        mPresenter = otherPresenter;
    }


    @Override
    public void back(){
        if(!fileBackStack.isEmpty()){
            show(fileBackStack.peek());
        }
    }

    @Override
    public boolean isRootPath(){
        return currentPath.equals(MY_ROOT_PATH);
    }

    @Override
    public void show(int position) {
        if(position < 0 || position >= list.size())
            show(MY_ROOT_PATH);
        else
            show(((BaseLocalFileInfo)list.get(position)).getPath());
    }

    public void show(String path) {
        list.clear();
        if(!fileBackStack.isEmpty() && path.equals(fileBackStack.peek()))
            fileBackStack.pop();
        else
            fileBackStack.add(currentPath);
        currentPath = path;
        if (path.equals(MY_ROOT_PATH)) {
            initRootPath();
        } else {
            File file = new File(path);
            List<File> files = Arrays.asList(file.listFiles());
            //根目录
            list.add(new DirectItem(new BaseLocalFileInfo.Builder()
                    .name("根目录")
                    .path(MY_ROOT_PATH)
                    .size(-1)
                    .lastModifiedTime(System.currentTimeMillis())
                    .isDirect(true)));
            //根目录
            list.add(new DirectItem(new BaseLocalFileInfo.Builder()
                    .name("返回上一级")
                    .path(fileBackStack.peek())
                    .size(-1)
                    .lastModifiedTime(file.getParentFile().lastModified())
                    .isDirect(true)));
            Collections.sort(files, (o1, o2) -> {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            });

            //添加所有文件
            for (File f : files) {
                if(f.isDirectory()){
                    list.add(new DirectItem(new BaseLocalFileInfo.Builder()
                            .name(f.getName())
                            .path(f.getPath())
                            .size(f.length())
                            .lastModifiedTime(f.lastModified())
                            .isDirect(true)));
                }else{
                    list.add(new FileItem(new BaseLocalFileInfo.Builder()
                            .name(f.getName())
                            .path(f.getPath())
                            .size(f.length())
                            .lastModifiedTime(f.lastModified())
                            .isDirect(false)));
                }
            }
        }
        mPresenter.updateUI();
    }

    private void initRootPath() {
        File file = null;
        file = new File(FileUtils.getSDPath() + "/Download");
        if(file.exists()){
            //根目录
            list.add(new DirectItem(new BaseLocalFileInfo.Builder()
                    .name(file.getName())
                    .path(file.getPath())
                    .size(file.length())
                    .lastModifiedTime(file.lastModified())
                    .isDirect(true)));
        }
        file = new File(FileUtils.getAppPath());
        if(file.exists()){
            list.add(new DirectItem(new BaseLocalFileInfo.Builder()
                    .name(file.getName())
                    .path(file.getPath())
                    .size(file.length())
                    .lastModifiedTime(file.lastModified())
                    .isDirect(true)));
        }
        String sdPath = FileUtils.getSDPath();
        if(sdPath != null){
            file = new File(sdPath);
            list.add(new DirectItem(new BaseLocalFileInfo.Builder()
                    .name("SDCard")
                    .path(file.getPath())
                    .size(file.length())
                    .lastModifiedTime(file.lastModified())
                    .isDirect(true)));
        }
    }

    @Override
    public List<MultiItemEntity> getData() {
        return list;
    }
}
