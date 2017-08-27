package com.sunny.youyun.wifidirect.activity.single_trans.receiver.config;

import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.activity.scan.config.ScanConfig;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public class ReceiverFragmentConfig {
    public static final int  REQUEST_FILE = 0;
    public static final String REQUEST_FILE_RESULT_KEY = FileManagerRequest.KEY_PATH;

    public static final int REQUEST_SCAN = 1;
    public static final String REQUEST_SCAN_RESULT_KEY = ScanConfig.SCAN_RESULT;
}
