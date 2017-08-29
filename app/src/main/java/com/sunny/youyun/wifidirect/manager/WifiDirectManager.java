package com.sunny.youyun.wifidirect.manager;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;

import com.sunny.youyun.wifidirect.client.ClientSocketManager;
import com.sunny.youyun.wifidirect.client.ForwardClientStrategy;
import com.sunny.youyun.wifidirect.config.SocketConfig;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.server.ServerSocketStrategy;

import java.io.IOException;
import java.util.List;


/**
 * Created by qjm3662 on 2016/10/30 0030.
 */

public enum WifiDirectManager implements WifiDirectConnectManager, SocketManager {

    INSTANCE;

    public static WifiDirectManager getInstance() {
        return INSTANCE;
    }

    private WifiDirectConnectManagerImpl wifiDirectConnectManager = null;
    private SocketManagerImpl socketManager = null;
    private boolean isWifiDirectEnable = false;
    private Mode mode = Mode.SINGLE;

    public boolean isWifiDirectEnable() {
        return isWifiDirectEnable;
    }

    public void setWifiDirectEnable(boolean wifiDirectEnable) {
        isWifiDirectEnable = wifiDirectEnable;
    }


    public enum Mode {
        SINGLE, GROUP;
    }

    /**
     * 初始化操作，在Application中作初始化
     *
     * @param c
     */
    public static void init(Context c) {
        System.out.println("init");
        bind(c);
    }

    /**
     * 绑定
     *
     * @param c
     */
    public static void bind(@NonNull Context c) {
        WifiP2pManager manager = (WifiP2pManager) c.getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
        if (manager == null)
            return;
        WifiP2pManager.Channel channel = manager.initialize(c.getApplicationContext(), c.getApplicationContext().getMainLooper(), null);

        INSTANCE.wifiDirectConnectManager = new WifiDirectConnectManagerImpl.Builder()
                .manager(manager)
                .channel(channel)
                .build();

        INSTANCE.socketManager = new SocketManagerImpl.Builder()
                .context(c)
                .build();
    }


    //-------------------------------------回调相关操作--------------------------------------//

//    public ReadFileCallback getReadFileCallback() {
//        return readFileCallback;
//    }
//
//    public SendFileCallBack getSendFileCallBack() {
//        return sendFileCallBack;
//    }
//
//    /**
//     * 绑定文件接收回调
//     * @param readFileCallback
//     */
//    public static void bindReadFileCallback(ReadFileCallback readFileCallback){
//        getInstance().readFileCallback = readFileCallback;
//    }
//
//    /**
//     * 接收文件回调
//     * @param sendFileCallBack
//     */
//    public static void bindSendFileCallBack(SendFileCallBack sendFileCallBack){
//        getInstance().sendFileCallBack = sendFileCallBack;
//    }
//
//    /**
//     * 取消绑定接收文件回调
//     */
//    public static void unBindReadFileCallback(){
//        getInstance().readFileCallback = null;
//    }
//
//    public static void unBindSendFileCallback(){ getInstance().sendFileCallBack = null; }

    //--------------------------------------Socket相关操作------------------------------------------//

    /**
     * 开始Socket监听
     */
    @Override
    public void startServer(@NonNull ServerSocketStrategy strategy) {
        socketManager.startServer(strategy);

    }


    @Override
    public void sendFile(String path, String IP, @NonNull final List<TransLocalFile> mList) {
        socketManager.sendFile(path, IP, mList);
    }

    /**
     * 停止Socket监听
     *
     * @throws IOException Socket关闭异常时抛出
     */
    @Override
    public void stopServer() throws IOException {
        socketManager.stopServer();
    }

    @Override
    public void beginDeviceInfoChange(int port, DeviceInfo deviceInfo) throws IOException {
        socketManager.beginDeviceInfoChange(port, deviceInfo);
    }

    @Override
    public void stopDeviceInfoChange() {
        socketManager.stopDeviceInfoChange();
    }

    @Override
    public void beginReceiveDeviceInfo(int port) {
        socketManager.beginReceiveDeviceInfo(port);
    }

    @Override
    public void beginSendDeviceInfo(int port, DeviceInfo deviceInfo, ForwardClientStrategy strategy) throws IOException {
        socketManager.beginSendDeviceInfo(port, deviceInfo, strategy);
    }

    @Override
    public void beginSendDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException {
        socketManager.beginSendDeviceInfo(port, deviceInfo);
    }

    @Override
    public void stopReceiveDeviceInfo() {
        socketManager.stopReceiveDeviceInfo();
    }

    @Override
    public void stopSendDeviceInfo() {
        socketManager.stopSendDeviceInfo();
    }

    @Override
    public void beginDeviceInfoChange(int port, DeviceInfo deviceInfo, ForwardClientStrategy strategy) throws IOException {
        socketManager.beginDeviceInfoChange(port, deviceInfo, strategy);
    }

    @Override
    public void askIp(String IP) throws IOException {
        ClientSocketManager.getInstance().askIP(IP, SocketConfig.FileListenPort);
    }


    @Override
    public void createGroup(WifiP2pManager.ActionListener createGroupListener) {
        wifiDirectConnectManager.createGroup(createGroupListener);
    }

    @Override
    public void disConnect(WifiP2pManager.ActionListener removeGroupListener) {
        wifiDirectConnectManager.disConnect(removeGroupListener);
    }

    @Override
    public void disConnect() {
        wifiDirectConnectManager.disConnect();
    }

    @Override
    public void cancelConnect(WifiP2pManager.ActionListener actionListener) {
        wifiDirectConnectManager.cancelConnect(actionListener);
    }

    @Override
    public void discover(WifiP2pManager.ActionListener discoverListener) {
        wifiDirectConnectManager.discover(discoverListener);
    }

    @Override
    public void discover() {
        wifiDirectConnectManager.discover();
    }

    @Override
    public void connect(WifiP2pConfig config, WifiP2pManager.ActionListener listener) {
        wifiDirectConnectManager.connect(config, listener);
    }

    @Override
    public void connect(WifiP2pConfig config) {
        wifiDirectConnectManager.connect(config);
    }

    @Override
    public void requestPeers(WifiP2pManager.PeerListListener peerListListener) {
        if (peerListListener == null)
            peerListListener = WifiDirectListeners.getInstance().getPeerListListener();
        wifiDirectConnectManager.requestPeers(peerListListener);
    }

    @Override
    public void requestConnectionInfo(WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        if (connectionInfoListener == null)
            connectionInfoListener = WifiDirectListeners.getInstance().getConnectionInfoListener();
        wifiDirectConnectManager.requestConnectionInfo(connectionInfoListener);
    }

    @Override
    public void requestGroupInfo(WifiP2pManager.GroupInfoListener groupInfoListener) {
        if (groupInfoListener == null)
            groupInfoListener = WifiDirectListeners.getInstance().getGroupInfoListener();
        wifiDirectConnectManager.requestGroupInfo(groupInfoListener);
    }

    public enum WifiDirectListeners {
        INSTANCE;

        public static WifiDirectListeners getInstance() {
            return INSTANCE;
        }

        private WifiP2pManager.ConnectionInfoListener connectionInfoListener;
        private WifiP2pManager.PeerListListener peerListListener;

        //在android4.3之前不能在Receiver中获取WifiP2pGroup对象
        //要通过requestGroupInfo的方式，在此处处理
        private final WifiP2pManager.GroupInfoListener groupInfoListener = group -> {

            if(group == null)
                return;
            if (group.getOwner() != null) {
                //设置组长MAC
                DeviceInfoManager.getInstance().setGroupOwnerMacAddr(group.getOwner().deviceAddress);
            }

            if (group.isGroupOwner()) {
                //设置组长name
                DeviceInfoManager.getInstance().setGroupOwnerName(group.getNetworkName());

                DeviceInfo deviceInfo = DeviceInfoManager.getInstance().getDeviceInfo();
                //name
                deviceInfo.setDeviceName(group.getNetworkName());
                //macAddr
                deviceInfo.setMacAddress(group.getOwner().deviceAddress);
            } else {
                //设置组长name
                DeviceInfoManager.getInstance().setGroupOwnerName(group.getOwner().deviceName);
            }
        };

        public WifiP2pManager.GroupInfoListener getGroupInfoListener() {
            return groupInfoListener;
        }

        public WifiP2pManager.ConnectionInfoListener getConnectionInfoListener() {
            return connectionInfoListener;
        }

        public void bindConnectionInfoListener(WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
            this.connectionInfoListener = connectionInfoListener;
        }

        public WifiP2pManager.PeerListListener getPeerListListener() {
            return peerListListener;
        }

        public void bindPeerListListener(WifiP2pManager.PeerListListener peerListListener) {
            if (this.peerListListener == peerListListener)
                return;
            this.peerListListener = peerListListener;
        }
    }
}
