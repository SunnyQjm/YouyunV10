package com.sunny.youyun.utils.extension

import com.sunny.youyun.mvp.BaseModel
import org.greenrobot.eventbus.EventBus

/**
 * Created by sunny on 17-11-14.
 */
fun BaseModel.post(o: Any) = EventBus
        .getDefault()
        .post(o)