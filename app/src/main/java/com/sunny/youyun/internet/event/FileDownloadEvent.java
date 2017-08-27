package com.sunny.youyun.internet.event;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

public class FileDownloadEvent {

    public enum Type{
        PROGRESS, START, FINISH, PAUSE, ERROR
    }
    private Type type;
    private final long already;
    private final long total;
    private final int percent;
    private final boolean done;
    private final String rate;
    private final int position;

    private FileDownloadEvent(Builder builder) {
        type = builder.type;
        already = builder.already;
        total = builder.total;
        percent = builder.percent;
        done = builder.done;
        rate = builder.rate;
        position = builder.position;
    }

    public Type getType() {
        return type;
    }

    public long getAlready() {
        return already;
    }

    public long getTotal() {
        return total;
    }

    public int getPercent() {
        return percent;
    }

    public boolean isDone() {
        return done;
    }

    public String getRate() {
        return rate;
    }

    public int getPosition() {
        return position;
    }

    public static final class Builder {
        private long already;
        private long total;
        private int percent;
        private boolean done;
        private String rate;
        private int position;
        private Type type;

        public Builder() {
        }

        public Builder already(long val) {
            already = val;
            return this;
        }

        public Builder total(long val) {
            total = val;
            return this;
        }

        public Builder percent(int val) {
            percent = val;
            return this;
        }

        public Builder done(boolean val) {
            done = val;
            return this;
        }

        public Builder rate(String val) {
            rate = val;
            return this;
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public FileDownloadEvent build() {
            return new FileDownloadEvent(this);
        }

        public Builder type(Type val) {
            type = val;
            return this;
        }
    }
}
