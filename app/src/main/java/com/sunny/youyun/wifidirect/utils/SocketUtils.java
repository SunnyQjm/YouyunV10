package com.sunny.youyun.wifidirect.utils;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.MD5Util;
import com.sunny.youyun.wifidirect.event.FileTransEvent;
import com.sunny.youyun.wifidirect.info.CODE_TABLE;
import com.sunny.youyun.wifidirect.model.SocketRequestBody;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Sunny on 2017/4/20 0020.
 */
public class SocketUtils {
    private final Socket socket;
    private final DataOutputStream out;
    private final DataInputStream input;

    public SocketUtils(Socket socket) throws IOException {
        this.socket = socket;
        out = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
    }


    public void writeUTF(final String info) throws IOException {
        out.writeUTF(info);
    }

    public String readUTF() throws IOException {
        return input.readUTF();
    }

    /**
     * 读文件
     *
     * @return
     */
    public void readFile(@NonNull final TransLocalFile transLocalFile, final FileTransCallback callBack) throws IOException {
        FileOutputStream fileOutputStream = null;
        String savePath = transLocalFile.getPath();
        File file = new File(savePath);
        //如果文件创建不成功
        file.createNewFile();
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException("保存路径无效");
        boolean isSuccess = false;
        try {
            byte[] buffer = new byte[1024 * 24];
            long alreadyTransBits = 0;
            long preTransBits = 0;
            long totalSize = transLocalFile.getSize();
            int pre_process = 0;
            int now_process = 0;

            fileOutputStream = new FileOutputStream(file);
            int length = 0;
            if (callBack != null)
                callBack.onBegin();
            long startTime = System.currentTimeMillis();
            long preTime = startTime;
            while (true) {
                if ((length = input.read(buffer)) == -1) {        //发送端的文件流关闭，
                    break;
                }
                alreadyTransBits += length;
                fileOutputStream.write(buffer, 0, length);

                //获取当前文件的传输进度
                now_process = (int) Math.ceil(alreadyTransBits * 1.0 / totalSize * 100);

                //作一层判断，当下载进度有明显变化时才更新进度
                if (now_process > pre_process) {
                    transLocalFile.setProgress(now_process);      //更新下载进度

                    long span = System.currentTimeMillis() - preTime;
                    if (span < 500)
                        continue;
                    preTime = System.currentTimeMillis();
                    //间隔500ms计算速率
                    transLocalFile.setRate(
                            Tool.convertToRate(alreadyTransBits - preTransBits, span)
                    );
                    preTransBits = alreadyTransBits;

                    pre_process = now_process;
                    if (callBack != null) {
                        callBack.onProgress(new FileTransEvent.Builder()
                                .already(alreadyTransBits)
                                .type(FileTransEvent.Type.DOWNLOAD)
                                .total(totalSize)
                                .done(alreadyTransBits == totalSize)
                                .build()
                        );
                    }

                }
            }
            fileOutputStream.flush();

            transLocalFile.setRate(
                    Tool.convertToRate(totalSize, System.currentTimeMillis() - startTime)
            );
            transLocalFile.setCreateTime(System.currentTimeMillis());
            transLocalFile.setPath(savePath);
            transLocalFile.setDone(true);
            //传输完毕后再发一次
            if (callBack != null)
                callBack.onEnd();
//            TransRxBus.getInstance().post(new FileTransEvent.Builder()
//                    .already(alreadyTransBits)
//                    .total(totalSize)
//                    .done(true)
//                    .position(position)
//                    .type(FileTransEvent.Type.DOWNLOAD)
//                    .build());
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
            if (callBack != null)
                callBack.onError(e);
            Logger.e(e, "readFile failed！");
        } finally {
            //关闭文件输出流即可，Socket不需要手动关闭
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isSuccess) {
                String md5 = MD5Util.getFileMD5(new FileInputStream(file));
                System.out.println("md5: " + md5);
                transLocalFile.setMd5(md5);
                transLocalFile.saveOrUpdate("md5 = ?", md5);
            }
        }
    }

    /**
     * 写文件
     *
     * @return
     */
    public void writeFile(TransLocalFile transLocalFile, FileTransCallback callback) throws IOException {
        File file = new File(transLocalFile.getPath());
        if (!file.exists() || file.isDirectory()) {
            if (callback != null)
                callback.onError(new FileNotFoundException("文件不存在"));
            throw new FileNotFoundException("文件不存在");
        }

        FileInputStream fileInputStream = null;

        byte[] buffer = new byte[1024 * 24];
        int length;
        int pre_process = 0;
        int now_process;
        long totalSize = file.length();
        long startTime = System.currentTimeMillis();
        long preTime = startTime;
        int alreadyTransBits = 0;
        int preTransBits = 0;
        int position;
        boolean isSuccess = false;
        try {
            fileInputStream = new FileInputStream(file);
            SocketRequestBody<TransLocalFile> body =
                    new SocketRequestBody<>(
                            CODE_TABLE.REQUEST_SINGLE_FILE, "传输单个文件", transLocalFile);

            String str = GsonUtil.getInstance().toJson(body);
            str = ProtocolStringBuilder.getInstance()
                    .contract(body.getCode())
                    .contract(str)
                    .toString();
            //发送文件基本信息
            writeUTF(str);
            //开始传输
            if (callback != null)
                callback.onBegin();
            while ((length = fileInputStream.read(buffer)) > 0) { //如果还没发完
                out.write(buffer, 0, length);
                out.flush();
                alreadyTransBits += length;
                now_process = (int) Math.ceil(alreadyTransBits * 1.0 / totalSize * 100);

                //作一层判断，当下载进度有明显变化时才更新进度
                if (now_process > pre_process) {
                    transLocalFile.setProgress(now_process);      //更新下载进度
                    pre_process = now_process;

                    long span = System.currentTimeMillis() - preTime;
                    if (span < 500) {
                        continue;
                    }
                    preTime = System.currentTimeMillis();
                    transLocalFile.setRate(
                            Tool.convertToRate(alreadyTransBits - preTransBits, span)
                    );
                    preTransBits = alreadyTransBits;

                    if (callback != null)
                        callback.onProgress(new FileTransEvent.Builder()
                                .already(alreadyTransBits)
                                .total(totalSize)
                                .done(alreadyTransBits == totalSize)
                                .type(FileTransEvent.Type.UPLOAD)
                                .build());
                }
            }

            transLocalFile.setRate(
                    Tool.convertToRate(totalSize, System.currentTimeMillis() - startTime)
            );
            transLocalFile.setCreateTime(System.currentTimeMillis());
            transLocalFile.setDone(true);
            if (callback != null)
                callback.onEnd();
            isSuccess = true;
        } catch (IOException e) {
            isSuccess = false;
            Logger.e(e, "发送文件失败");
            if (callback != null)
                callback.onError(e);
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (isSuccess) {
                String md5 = MD5Util.getFileMD5(new FileInputStream(file));
                System.out.println("md5: " + md5);
                transLocalFile.setMd5(md5);
                transLocalFile.saveOrUpdate("md5 = ?", md5);
            }
        }
    }

    public void close() throws IOException {
        out.close();
        input.close();
        socket.close();
    }


    public interface FileTransCallback {
        void onBegin();

        void onProgress(FileTransEvent fileTransEvent);

        void onEnd();

        void onError(Exception e);
    }
}
