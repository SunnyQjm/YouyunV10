package com.sunny.youyun.activity.person_file_manager;

import android.os.Bundle;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;

@Router(IntentRouter.PersonFileManagerActivity)
public class PersonFileManagerActivity extends MVPBaseActivity<PersonFileManagerPresenter> implements PersonFileManagerContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_file_manager);
    }

    @Override
    protected PersonFileManagerPresenter onCreatePresenter() {
        return new PersonFileManagerPresenter(this);
    }
}
