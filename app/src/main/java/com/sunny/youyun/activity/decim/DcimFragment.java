package com.sunny.youyun.activity.decim;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.clip.ClipImageActivity;
import com.sunny.youyun.activity.clip.config.ClipImageConfig;
import com.sunny.youyun.activity.decim.adapter.DcimAdapter;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.views.DividerGridItemDecoration;
import com.sunny.youyun.views.EasyBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DcimFragment extends MVPBaseFragment<DcimPresenter>
        implements DcimContract.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.spinner)
    Spinner spinner;
    Unbinder unbinder;
    private OnFragmentInteractionListener mListener;
    private View view = null;
    private DcimAdapter dcimAdapter = null;
    private ArrayAdapter<String> selectAdapter = null;

    public static DcimFragment newInstance() {
        DcimFragment fragment = new DcimFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_dicm, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.select_photo));

        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                activity.onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        showLoading();
        //解决android6.0权限动态申请问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            RxPermissionUtil.getInstance(activity)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if(aBoolean)
                            mPresenter.getData(activity);
                    });
        }
        dcimAdapter = new DcimAdapter(activity, mPresenter.getFileItems());
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
        dcimAdapter.bindToRecyclerView(recyclerView);
        dcimAdapter.setOnItemClickListener(this);

        // 建立Adapter并且绑定数据源
        selectAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item,
                mPresenter.getSelectItems());
        selectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(selectAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.selected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected DcimPresenter onCreatePresenter() {
        return new DcimPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void getDataSuccess(boolean isFirst) {
        dismissDialog();
        if (dcimAdapter != null)
            dcimAdapter.notifyDataSetChanged();
        if (selectAdapter != null) {
            if (isFirst)
                spinner.setAdapter(selectAdapter);
            selectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getData().get(position) instanceof FileItem) {
            FileItem fileItem = (FileItem) adapter.getData().get(position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Intent intent = new Intent(activity, ClipImageActivity.class);
                intent.putExtra(ClipImageConfig.PATH, fileItem.getPath());
                RouterUtils.openWithAnimation(activity, intent);
            } else {
                RouterUtils.open(activity, IntentRouter.ClipImageActivity, fileItem.getPath());
            }
            activity.onBackPressed();
        }
    }

    @Override
    public void allDataLoadFinish() {

    }
}
