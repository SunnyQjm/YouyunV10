package com.sunny.youyun.model;


import com.sunny.youyun.utils.FileTypeUtil;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Sunny on 2017/5/13 0013.
 */

public class TransLocalFile extends DataSupport {
    @Column(unique = true)
    private String path;
    private String name;
    private long size;
    private long downTime;
    private String type;
    private int icon_id;    //图标的资源ID
    private int process;
    private float rate;   //传输速率
    private String down_ratio;
    private int TYPE_TAG = 0;
    private String rate_unit = "KB/s";

    public static final int SEND_TAG = 0;
    public static final int RECEIVE_TAG = 1;

    private TransLocalFile(Builder builder) {
        setPath(builder.path);
        setName(builder.name);
        setSize(builder.size);
        setDownTime(builder.downTime);
        setType(builder.type);
        setProcess(builder.process);
        setRate(builder.rate);
        setDown_ratio(builder.down_ratio);
        setRate_unit(builder.rate_unit);
        setIcon_id(builder.icon_id);

    }

    @Override
    public String toString() {
        return "TransLocalFile{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", downTime=" + downTime +
                ", type='" + type + '\'' +
                ", process=" + process +
                ", rate=" + rate +
                ", icon_id=" + icon_id +
                ", down_ratio='" + down_ratio + '\'' +
                ", rate_unit ='" + rate_unit + '\'' +
                '}';
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public String getDown_ratio() {
        return down_ratio;
    }

    public void setDown_ratio(String down_ratio) {
        this.down_ratio = down_ratio;
    }

    public int getTYPE_TAG() {
        return TYPE_TAG;
    }

    public void setTYPE_TAG(int TYPE_TAG) {
        this.TYPE_TAG = TYPE_TAG;
    }

    public String getRate_unit() {
        return rate_unit;
    }

    public void setRate_unit(String rate_unit) {
        this.rate_unit = rate_unit;
    }

    /**
     * 保存或更新
     * @param localFile
     */
    public static boolean saveOrUpdate(TransLocalFile localFile) {
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
        private String path;
        private String name;
        private long size;
        private long downTime;
        private String type;
        private int process;
        private float rate;
        private int icon_id;
        private String down_ratio;
        private String rate_unit;

        public Builder() {
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
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

        public Builder icon_id(int val) {
            icon_id = val;
            return this;
        }

        public Builder down_ratio(String val) {
            down_ratio = val;
            return this;
        }

        public Builder rate_unit(String val) {
            rate_unit = val;
            return this;
        }

        public TransLocalFile build() {
            if(icon_id == 0)
                icon_id = FileTypeUtil.getIconIdByFileName(name);
            return new TransLocalFile(this);
        }
    }
}
