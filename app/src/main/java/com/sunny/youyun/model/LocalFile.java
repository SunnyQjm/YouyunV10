package com.sunny.youyun.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qjm3662 on 2016/9/26 0026.
 */

public class LocalFile extends DataSupport implements Serializable {

    private String name;
    @Column(unique = true)
    private String path;
    private long size;
    private long downTime;
    private String type;
    private int process;
    public static final String PHOTO = "photo";
    public static final String MUSIC = "music";
    public static final String VIDEO = "video";
    public static final String DOC = "doc";
    public static final String HTML = "html";
    public static final String PPT_PDF = "ppt pdf";
    public static final String ZIP = "zip";

    protected LocalFile(Builder builder) {
        name = builder.name;
        path = builder.path;
        size = builder.size;
        downTime = builder.downTime;
        type = builder.type;
        process = builder.process;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDownTime() {
        return downTime;
    }

    public void setDownTime(long downTime) {
        this.downTime = downTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    /**
     * 保存或更新
     * @param localFile
     */
    public static boolean saveOrUpdate(LocalFile localFile) {
        List<LocalFile> mList = DataSupport.where("path = ?", localFile.path).find(LocalFile.class);
        if (mList == null || mList.size() == 0) {
            localFile.save();
            return true;
        } else {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).delete();
            }
            localFile.save();
            return false;
        }
    }


    public static final class Builder {
        private String name;
        private String path;
        private long size;
        private long downTime;
        private String type;
        private int process;
        private float rate;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }

        public Builder downTime(long val) {
            downTime = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder process(int val) {
            process = val;
            return this;
        }

        public Builder rate(float val) {
            rate = val;
            return this;
        }

        public LocalFile build() {
            return new LocalFile(this);
        }
    }
}
