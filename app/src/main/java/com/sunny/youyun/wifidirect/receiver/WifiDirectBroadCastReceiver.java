package com.sunny.youyun.wifidirect.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import com.sunny.youyun.wifidirect.manager.DeviceInfoManager;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.model.DeviceInfo;


/**
 * Wifi-Direct broadcast receiver, this receiver can catch all broadcast signal
 * send by Wifi-Direct
 * Created by qjm3662 on 2016/10/25 0025.
 */

public class WifiDirectBroadCastReceiver extends BroadcastReceiver {
    /**
     * This receiver register in manifest file , so must declare a non-param constructor
     */
    public WifiDirectBroadCastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            switch (action) {
                //当wifi功能打开关闭的时候会广播这个信号
                case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_STATE_CHANGED_ACTION");
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                        //Wifi Direct is enabled
                        WifiDirectManager.getInstance().setWifiDirectEnable(true);
                    } else {
                        //Wifi Direct is not enabled
                        WifiDirectManager.getInstance().setWifiDirectEnable(false);
                    }
                    break;

                //This action received when the peers of available device is change
                //当前可用列表发生改变时执行下面代码块，获取当前可用连接表的列表
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_PEERS_CHANGED_ACTION");
                    WifiDirectManager.getInstance().requestPeers(null);
                    break;

                //This action received when the connection setup or dis-setup
                //当连接建立或者断开的时候会广播该信号
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_CONNECTION_CHANGED_ACTION");
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);

                    //如果群组都还没建立，就直接忽略该信号
                    if (!wifiP2pInfo.groupFormed)
                        break;

                    //申请获得群组的信息，用来更新群组信息
                    WifiDirectManager.getInstance().requestGroupInfo(null);
                    //标记本设备是否是组长
                    DeviceInfoManager.getInstance().setGroupOwner(wifiP2pInfo.isGroupOwner);
                    //设置组长IP
                    DeviceInfoManager.getInstance().setGroupOwnerIp(wifiP2pInfo.groupOwnerAddress.getHostAddress());

                    WifiDirectManager.getInstance().requestConnectionInfo(null);
                    if (wifiP2pInfo.isGroupOwner) {       //群组处理，直接更新自己的设备信息
                        DeviceInfo deviceInfo = DeviceInfoManager.getInstance().getDeviceInfo();
                        //ip
                        deviceInfo.setIp(wifiP2pInfo.groupOwnerAddress.getHostAddress());
                    }
                    break;

                //当前设备的Wifi状态发生改变时会广播该信号(Wifi连接，断开)
                case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                    System.out.println("WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
