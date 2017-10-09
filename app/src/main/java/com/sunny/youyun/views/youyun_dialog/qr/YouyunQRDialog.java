package com.sunny.youyun.views.youyun_dialog.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.QRCodeUtil;

public class YouyunQRDialog extends DialogFragment {
    ImageView imgQr;

    private static final String QR_PATH = FileUtils.getAppPath() + "/qr.jpg";
    private Context context;
    private String content;
    private View view;
    private Bitmap icon = null;


    private void setContext(Context context) {
        this.context = context;
    }

    public YouyunQRDialog() {

    }

    public static YouyunQRDialog newInstance(final Context context, final String content) {

        Bundle args = new Bundle();
        YouyunQRDialog fragment = new YouyunQRDialog();
        fragment.setContext(context);
        fragment.setContent(content);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_youyun_qrdialog, container, false);
            imgQr = (ImageView) view.findViewById(R.id.img_qr);
            setContent(content);
        } else {
            imgQr = (ImageView) view.findViewById(R.id.img_qr);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        return view;
    }

    public void setContent(String content) {
        this.content = content;
        if (imgQr != null) {
            imgQr.post(() -> {
                QRCodeUtil.createQRImage(content, imgQr.getMeasuredWidth(), imgQr.getMeasuredHeight(),
                        icon, QR_PATH);
                Bitmap bitmap1 = BitmapFactory.decodeFile(QR_PATH);
                imgQr.setImageBitmap(bitmap1);
            });
        }
    }

    public void setCenterIcon(Bitmap bitmap) {
        this.icon = bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
