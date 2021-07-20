package com.example.sockettest.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;


public class FileDownLoadSocketThread {
    private ProgressDialog progressDialog;
    private Context context;
    private String ip;
    private int port;
    private Socket socket;
    Handler handler;
    File file;
    long fileSize,downLoadFileSize=0;
    private int connect_timeout=10000;
    public static int SERVER_MSG_OK=0;//用于发送给句柄的消息类型,放在消息的arg2中，表示服务端正常
    public static int SERVER_MSG_ERROR=1;//表示服务端出错
    private int msgType;
    public static final String KEY_SERVER_ACK_MSG_DOWNLOAD = "KEY_SERVER_ACK_MSG_DOWNLOAD";
    int i=0;


    public FileDownLoadSocketThread(String ip, int port, Handler handler, File file, long fileSize,Context context) {
        this.context=context;
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.file = file;
        this.fileSize = fileSize;
    }

    private void connect() throws IOException {//连接服务端函数
        InetSocketAddress address = new InetSocketAddress(ip, port);
        socket = new Socket();
        socket.connect(address, connect_timeout);
    }





    private void doCmdTask(){
        ArrayList<String> msgList=new ArrayList<>();
        try {
            connect();
            msgType=SERVER_MSG_ERROR;
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(in));

            OutputStream song = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(song));


            fileSize = (int) dis.readLong() - 1;
            byte[] buffer = new byte[8192];
            while (true) {
                int read = 0;
                if (dis != null) {
                    read = dis.read(buffer);
                    downLoadFileSize += read;
                }
                if (read == -1) {
                    break;
                }
                dos.write(buffer, 0, read);
            }

            msgList.add("文件下载完成");
            msgType=SERVER_MSG_OK;
            dos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SERVER_ACK_MSG_DOWNLOAD,msgList);
        message.arg2=msgType;
        message.setData(bundle);
        handler.sendMessage(message);

    }


    private Handler dhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progressDialog != null) {
                progressDialog.setProgress(msg.what);
                progressDialog.setSecondaryProgress(msg.what + 20);
            }
        }
    };

    private void iniProgressDialog() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Test AsyncTask");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("");
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        progressDialog.setCancelable(true); // 能够返回
        progressDialog.setCanceledOnTouchOutside(true); // 点击外部返回
        progressDialog.show();
    }

    public void work_down_log(){
        iniProgressDialog();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressDialog!=null){
                    try {

                        Thread.sleep(1000);
                        Double a=downLoadFileSize*1.0/fileSize*100;
                        System.out.println(downLoadFileSize+","+fileSize+","+a);
                        int b=a.intValue();
                        if(b==100){
                            progressDialog.dismiss();
                            break;
                        }
                        dhandler.sendEmptyMessage(b);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public void work(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCmdTask();
            }
        }).start();
    }
}
