package com.sunny.youyun.activity.decim;

import android.net.Uri;
import android.os.Bundle;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.YouyunActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.utils.ActivityUtils;

@Router(IndexRouter.DcimActivity)
public class DcimActivity extends YouyunActivity implements MVPBaseFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decim);
        DcimFragment dcimFragment = (DcimFragment)
                getSupportFragmentManager().findFragmentById(R.id.base_contain);
        if(dcimFragment == null){
            dcimFragment = DcimFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), dcimFragment,
                    R.id.base_contain);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
