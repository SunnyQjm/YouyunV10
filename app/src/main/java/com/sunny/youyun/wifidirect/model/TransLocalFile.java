package com.sunny.youyun.wifidirect.model;


import org.litepal.crud.DataSupport;

/**
 * Created by Sunny on 2017/5/13 0013.
 */

public class TransLocalFile extends DataSupport {
    private String md5;
    private final String name;
    private final long size;
    private String path = "";

    private long createTime;
    private boolean done = false;
    private int progress = 0;
    private String rate;
    private int fileTAG = -1;
    public static final int TAG_SEND = 0;
    public static final int TAG_RECEIVE = 1;

    private TransLocalFile(Builder builder) {
        setMd5(builder.md5);
        name = builder.name;
        size = builder.size;
        setPath(builder.path);
        setCreateTime(builder.createTime);
        setDone(builder.done);
        setProgress(builder.progress);
        setRate(builder.rate);
        fileTAG = builder.fileTAG;
    }


    public String getMd5() {
        return md5;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isDone() {
        return done;
    }

    public String getPath() {
        return path;
    }

    public int getProgress() {
        return progress;
    }

    public String getRate() {
        return rate;
    }

    public int getFileTAG() {
        return fileTAG;
    }

    public void setFileTAG(int fileTAG) {
        this.fileTAG = fileTAG;
    }

    public static final class Builder {
        private String name;
        private long size;
        private long createTime;
        private boolean done;
        private String path;
        private int progress;
        private String rate;
        private int fileTAG;
        private String md5;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder done(boolean val) {
            done = val;
            return this;
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder progress(int val) {
            progress = val;
            return this;
        }

        public Builder rate(String val) {
            rate = val;
            return this;
        }

        public Builder fileTAG(int val) {
            fileTAG = val;
            return this;
        }

        public Builder md5(String val) {
            md5 = val;
            return this;
        }

        public TransLocalFile build() {
            return new TransLocalFile(this);
        }
    }
}
