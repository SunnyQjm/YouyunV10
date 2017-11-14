package com.sunny.youyun.activity.about_youyun

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.github.mzule.activityrouter.annotation.Router
import com.sunny.youyun.IntentRouter
import com.sunny.youyun.R
import com.sunny.youyun.base.activity.MVPBaseActivity
import com.sunny.youyun.views.EasyBar

import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_about_youyun.*
import kotlinx.android.synthetic.main.bar_item.*
import org.jetbrains.anko.toast

@Router(IntentRouter.AboutYouyunActivity)
class AboutYouyunActivity : MVPBaseActivity<AboutYouyunPresenter>(), AboutYouyunContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_youyun)
        ButterKnife.bind(this)
        initView()
    }

    private fun initView() {
        easyBar.setTitle(getString(R.string.about_youyun))
        easyBar.setOnEasyBarClickListener(object : EasyBar.OnEasyBarClickListener {
            override fun onLeftIconClick(view: View) {
                onBackPressed()
            }

            override fun onRightIconClick(view: View) {

            }
        })

        tv_version.setOnClickListener{
            toast("version click")
        }
    }

    override fun onCreatePresenter(): AboutYouyunPresenter = AboutYouyunPresenter(this)


    override fun allDataLoadFinish() {

    }
}
