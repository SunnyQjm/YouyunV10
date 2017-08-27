package com.sunny.youyun.wifidirect.event;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public class FileTransEvent {
    private final long already;
    private final long total;
    private final boolean done;
    private final int position;
    private final Type type;

    public enum Type{
        UPLOAD, DOWNLOAD, BEGIN
    }

    private FileTransEvent(Builder builder) {
        already = builder.already;
        total = builder.total;
        done = builder.done;
        position = builder.position;
        type = builder.type;
    }

    @Override
    public String toString() {
        return "FileTransEvent{" +
                "already=" + already +
                ", total=" + total +
                ", done=" + done +
                ", position=" + position +
                '}';
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

    public boolean isDone() {
        return done;
    }

    public int getPosition() {
        return position;
    }

    public static final class Builder {
        private long already;
        private long total;
        private boolean done;
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

        public Builder done(boolean val) {
            done = val;
            return this;
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public Builder type(Type val) {
            type = val;
            return this;
        }

        public FileTransEvent build() {
            return new FileTransEvent(this);
        }
    }
}
