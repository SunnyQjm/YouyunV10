package com.sunny.youyun.fragment.main.finding_fragment.base

import android.content.Intent
import android.support.v4.util.Pair
import android.view.View

import com.github.mzule.activityrouter.router.Routers
import com.sunny.youyun.App
import com.sunny.youyun.R
import com.sunny.youyun.activity.file_detail_online.FileDetailOnlineActivity
import com.sunny.youyun.base.adapter.BaseQuickAdapter
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment
import com.sunny.youyun.model.InternetFile
import com.sunny.youyun.mvp.BasePresenter
import com.sunny.youyun.mvp.BaseView
import com.sunny.youyun.utils.RouterUtils
import com.sunny.youyun.utils.UUIDUtil
import com.sunny.youyun.utils.bus.ObjectPool
import com.sunny.youyun.utils.extension.startActivityWithAnimation

/**
 * Created by Sunny on 2017/10/12 0012.
 */

abstract class FindingBaseFragment<T : BasePresenter<*, *>> : BaseRecyclerViewFragment<T>(), BaseView {

    open fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val internetFile = adapter.getItem(position) as InternetFile? ?: return
        val uuid = UUIDUtil.getUUID()
        ObjectPool.getInstance()
                .put(uuid, internetFile)
//        val intent = Intent(activity, FileDetailOnlineActivity::class.java)
//        intent.putExtra("uuid", uuid)
        RouterUtils.openToFileDetailOnline(activity, internetFile)
//        activity.startActivityWithAnimation(intent, view.findViewById(R.id.img_icon), getString(R.string.share_finding_item))
//        activity.startActivityWithAnimation(intent,
//                *arrayOf(Pair.create(view, getString(R.string.share_finding_item)))
//                )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && adapter != null) {
            adapter.notifyDataSetChanged()
        }
    }
}
