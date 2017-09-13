package com.sunny.youyun.activity.person_file_manager;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class PersonFileManagerModel implements PersonFileManagerContract.Model {
    private final PersonFileManagerPresenter mPresenter;

    PersonFileManagerModel(PersonFileManagerPresenter personFileManagerPresenter) {
        mPresenter = personFileManagerPresenter;
    }
}
