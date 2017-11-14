package com.sunny.youyun.utils.extension

import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.support.v4.util.Pair
/**
 * Created by sunny on 17-11-14.
 */
fun AppCompatActivity.startActivityWithAnimation(intent: Intent, shareView: View, shareElementName: String) {
    ActivityCompat.startActivity(this, intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, shareView, shareElementName)
                    .toBundle())
}

fun AppCompatActivity.startActivityWithAnimation(intent: Intent,
                                                 vararg sharedElements: Pair<View, String>){
    ActivityCompat.startActivity(this, intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(this, *sharedElements)
                    .toBundle())
}


