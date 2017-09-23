package com.sunny.youyun.model;

/**
 * Created by Sunny on 2017/9/21 0021.
 */

public class FileManageItem {

    /**
     * parentPathName : /
     * selfId : 3e027356-df61-44df-9f8d-fa7a4725314f
     * pathName : ApiDemos.apk
     * userId : 1010
     * fileId : 1005
     * file : {"name":"ApiDemos.apk","size":5279903,"lookNum":0,"downloadCount":0,"identifyCode":"cU2pk9","share":true,"privateOwn":false,"description":"null","score":0,"MIME":"application/vnd.android.package-archive","star":0,"id":1005,"createTime":1505988385947}
     * id : 1000
     * createTime : 1505988386024
     * updateTime : 1505988386024
     */

    private final String parentPathName;
    private final String selfId;
    private final String pathName;
    private final int userId;
    private final int fileId;
    private final InternetFile file;
    private final int id;
    private final long createTime;
    private final long updateTime;

    protected FileManageItem(Builder builder) {
        parentPathName = builder.parentPathName;
        selfId = builder.selfId;
        pathName = builder.pathName;
        userId = builder.userId;
        fileId = builder.fileId;
        file = builder.file;
        id = builder.id;
        createTime = builder.createTime;
        updateTime = builder.updateTime;
    }

    public String getParentPathName() {
        return parentPathName;
    }

    public String getSelfId() {
        return selfId;
    }

    public String getPathName() {
        return pathName;
    }

    public int getUserId() {
        return userId;
    }

    public int getFileId() {
        return fileId;
    }

    public InternetFile getFile() {
        return file;
    }

    public int getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public static final class Builder {
        private String parentPathName;
        private String selfId;
        private String pathName;
        private int userId;
        private int fileId;
        private InternetFile file;
        private int id;
        private long createTime;
        private long updateTime;

        public Builder() {
        }

        public Builder parentPathName(String val) {
            parentPathName = val;
            return this;
        }

        public Builder selfId(String val) {
            selfId = val;
            return this;
        }

        public Builder pathName(String val) {
            pathName = val;
            return this;
        }

        public Builder userId(int val) {
            userId = val;
            return this;
        }

        public Builder fileId(int val) {
            fileId = val;
            return this;
        }

        public Builder file(InternetFile val) {
            file = val;
            return this;
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder updateTime(long val) {
            updateTime = val;
            return this;
        }

        public FileManageItem build() {
            return new FileManageItem(this);
        }
    }
}
