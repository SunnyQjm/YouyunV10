package com.sunny.youyun.wifidirect.manager;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * This class is design to manager the connect work of WifiDirect
 * Created by Sunny on 2017/7/31 0031.
 */

class WifiDirectConnectManagerImpl implements WifiDirectConnectManager {

    private final WifiP2pManager manager;
    private final WifiP2pManager.Channel channel;

    private WifiDirectConnectManagerImpl(Builder builder) {
        manager = builder.manager;
        channel = builder.channel;

    }


    //---------------------------------以下是WifiDirect相关操作------------------------------------//

    /**
     * This method create a p2p group with the current device as the group owner.(创建群组)
     *
     * @param createGroupListener for callbacks on success or failure. Can be null.
     */
    @Override
    public void createGroup(WifiP2pManager.ActionListener createGroupListener) {
        manager.createGroup(channel, createGroupListener);
    }

    /**
     * This method remove current group, if the current device is not a group owner, this method
     * will made the current connection disconnect.
     * （该方法用于移除群组，如果调用设备不是群主，则断开当前设备与群主的连接）
     *
     * @param removeGroupListener for callbacks on success or failure. Can be null.
     */
    @Override
    public void disConnect(WifiP2pManager.ActionListener removeGroupListener) {
        manager.removeGroup(channel, removeGroupListener);
    }

    /**
     * This method remove current group, if the current device is not a group owner, this method
     * will made the current connection disconnect.
     * （该方法用于移除群组，如果调用设备不是群主，则断开当前设备与群主的连接）
     * <p>
     * ps: User don't care about the stringResult of this operate
     */
    @Override
    public void disConnect() {
        disConnect(null);
    }

    /**
     * Cancel any ongoing p2p group negotiation(取消正在尝试的连接)
     *
     * @param actionListener for callbacks on success or failure. Can be null.
     */
    @Override
    public void cancelConnect(WifiP2pManager.ActionListener actionListener) {
        manager.cancelConnect(channel, actionListener);
    }


    /**
     * Initiate peer discovery. A discovery process involves scanning for available Wi-Fi peers
     * for the purpose of establishing a connection.
     * (开始搜索附近的设备)
     *
     * @param discoverListener for callbacks on success or failure. Can be null.
     */
    @Override
    public void discover(WifiP2pManager.ActionListener discoverListener) {
        manager.discoverPeers(channel, discoverListener);
    }

    /**
     * Initiate peer discovery. A discovery process involves scanning for available Wi-Fi peers
     * for the purpose of establishing a connection.
     * 开始搜索附近的设备
     */
    @Override
    public void discover() {
        discover(null);
    }


    /**
     * Start a p2p connection to a device with the specified configuration.
     * 连接（用户自定义监听器）
     *
     * @param config   options as described in {@link WifiP2pConfig} class
     * @param listener for callbacks on success or failure. Can be null.
     *                 连接状态监听器（onSuccess只是代表该操作成功， 并不能说明连接成功，需要在receive那边判断）
     */
    @Override
    public void connect(WifiP2pConfig config, WifiP2pManager.ActionListener listener) {
        manager.connect(channel, config, listener);
    }

    /**
     * Start a p2p connection to a device with the specified configuration.
     * 连接（用户自定义监听器）
     * <p>
     * ps: User don't care about the stringResult of this operate
     *
     * @param config options as described in {@link WifiP2pConfig} class
     */
    @Override
    public void connect(WifiP2pConfig config) {
        connect(config, null);
    }

    @Override
    public void requestPeers(WifiP2pManager.PeerListListener peerListListener) {
        manager.requestPeers(channel, peerListListener);
    }

    @Override
    public void requestConnectionInfo(WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        manager.requestConnectionInfo(channel, connectionInfoListener);
    }

    @Override
    public void requestGroupInfo(WifiP2pManager.GroupInfoListener groupInfoListener) {
        manager.requestGroupInfo(channel, groupInfoListener);
    }


    public static final class Builder {
        private WifiP2pManager manager;
        private WifiP2pManager.Channel channel;

        public Builder() {
        }

        public Builder manager(WifiP2pManager val) {
            manager = val;
            return this;
        }

        public Builder channel(WifiP2pManager.Channel val) {
            channel = val;
            return this;
        }

        public WifiDirectConnectManagerImpl build() {
            return new WifiDirectConnectManagerImpl(this);
        }
    }
}
