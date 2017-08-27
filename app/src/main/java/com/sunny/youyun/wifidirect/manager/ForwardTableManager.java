package com.sunny.youyun.wifidirect.manager;


import com.sunny.youyun.wifidirect.config.EventConfig;
import com.sunny.youyun.wifidirect.event.BaseEvent;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.utils.EventRxBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/4/28/028.
 */
public enum ForwardTableManager {
    INSTANCE;

    public static ForwardTableManager getInstance() {
        return INSTANCE;
    }

    private final List<DeviceInfo> mList;
    private final Map<String, Integer> map;                         //记录表项的MAC和其在List中位置的映射

    ForwardTableManager() {
        mList = Collections.synchronizedList(new ArrayList<DeviceInfo>());
        map = new ConcurrentHashMap<>();
    }

    //***********************************************以下是对socket送来的表项的操作******************************************************//

    /**
     * 往向量表中添加一项
     *
     * @param deviceInfo 要添加的表项信息
     * @return 返回是否添加成功（如果该表项已经存在，则添加失败，返回false）
     */
    public boolean add(DeviceInfo deviceInfo) {
        if (deviceInfo.getMacAddress() == null) {
            return false;
        }

        if (map.get(deviceInfo.getMacAddress()) != null) {
            return false;
        }
        deviceInfo.setExpire(System.currentTimeMillis() + 1000 * 5);
        map.put(deviceInfo.getMacAddress(), mList.size());
        mList.add(deviceInfo);
        EventRxBus.getInstance().post(new BaseEvent<>(
                EventConfig.FORWARD_SIGNAL_ADD, "添加一条记录", deviceInfo
        ));
        return true;
    }


    public boolean addOrUpdate(DeviceInfo deviceInfo) {
        //原来表项中有则更新
        if (map.get(deviceInfo.getMacAddress()) != null) {
            return update(deviceInfo);
        }
        return add(deviceInfo);
    }

    /**
     * 通过id删除表项
     *
     * @param macAddress
     * @return
     */
    public boolean delete(String macAddress) {
        if (macAddress == null || macAddress.equals("") || map.get(macAddress) == null) {    //如果表项中没有，则删除失败，返回false
            return false;
        }
        int position = map.get(macAddress);
        mList.remove(position);
        map.remove(macAddress);
        for (int i = position; i < mList.size(); i++) {     //mList中remove掉一个项之后，其后的所有表项的位置都往前挪一位
            map.put(mList.get(i).getMacAddress(), i);
        }
        return true;
    }

    /**
     * 更新某个表项的信息
     *
     * @param deviceInfo
     * @return
     */
    public boolean update(DeviceInfo deviceInfo) {
        if (map.get(deviceInfo.getMacAddress()) == null) {       //如果表项中没有该项则跟新失败，返回false
            return false;
        }
        deviceInfo.setExpire(System.currentTimeMillis() + 1000 * 5);
        int position = map.get(deviceInfo.getMacAddress());
        mList.set(position, deviceInfo);
        return true;
    }


    /**
     * 通过表项id来查询表项具体信息
     *
     * @param macAddress
     * @return
     */
    public DeviceInfo query(String macAddress) {
        int position = map.get(macAddress);
        return mList.get(position);
    }

    /**
     * 获得表项的list列表
     *
     * @return
     */
    public List<DeviceInfo> getList() {
        return mList;
    }

    /**
     * 获得表项id和其在List中位置的映射关系
     *
     * @return
     */
    public Map<String, Integer> getMap() {
        return map;
    }
}
