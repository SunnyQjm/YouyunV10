package com.sunny.youyun.fragment.main.finding_fragment.item;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class SearchData {
    private final UserItem[] users;
    private final FileItem[] files;

    private SearchData(Builder builder) {
        users = builder.users;
        files = builder.files;
    }


    public UserItem[] getUsers() {
        return users;
    }

    public FileItem[] getFiles() {
        return files;
    }

    public static final class Builder {
        private UserItem[] users;
        private FileItem[] files;

        public Builder() {
        }

        public Builder users(UserItem[] val) {
            users = val;
            return this;
        }

        public Builder files(FileItem[] val) {
            files = val;
            return this;
        }

        public SearchData build() {
            return new SearchData(this);
        }
    }
}
