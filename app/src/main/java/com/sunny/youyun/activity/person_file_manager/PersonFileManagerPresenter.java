package com.sunny.youyun.activity.person_file_manager;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class PersonFileManagerPresenter extends PersonFileManagerContract.Presenter{
    PersonFileManagerPresenter(PersonFileManagerActivity personFileManagerActivity) {
        mView = personFileManagerActivity;
        mModel = new PersonFileManagerModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
