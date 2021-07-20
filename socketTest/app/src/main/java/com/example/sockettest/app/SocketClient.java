package com.example.sockettest.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient {
    public static int SERVER_MSG_OK=0;//用于发送给句柄的消息类型,放在消息的arg2中，表示服务端正常
    public static int SERVER_MSG_ERROR=1;//表示服务端出错
    public static int SERVER_MSG_DLF=2;//表示下载返回
    public static int SERVER_MSG_DIR=3;//文件列表
    public static int SERVER_MSG_OPN=4;//打开文件
    public static int SERVER_MSG_KEY=5;//按键操作
    public static int SERVER_MSG_MOV=6;//鼠标移动
    public static int SERVER_MSG_CLK=7;//鼠标事件
    public static int SERVER_MSG_CMD=8;//服务端打开网站
    public static int SERVER_MSG_CPS=9;//远程输入
    public static int SERVER_MSG_ULF=10;//表示下载返回
    private int msgType;
    private String ip;
    private int port;
    private ArrayList<String> cmd;
    private int connect_timeout=1000;
    private Handler handler;
    private Socket socket;
    public static final String KEY_SERVER_ACK_MSG = "KEY_SERVER_ACK_MSG";
    private OutputStreamWriter writer;
    private BufferedReader bufferedReader;
    public SocketClient(String ip, int port, Handler handler) {
        this.port = port;
        this.ip = ip;
        this.handler = handler;
    }

    private void connect() throws IOException {//连接服务端函数
        InetSocketAddress address = new InetSocketAddress(ip, port);
        socket = new Socket();
        socket.connect(address, connect_timeout);
    }

    private void writeCmd(String cmd) throws IOException {
        BufferedOutputStream os=new BufferedOutputStream(socket.getOutputStream());
        writer=new OutputStreamWriter(os,"UTF-8");
        writer.write("1\n");
        writer.write(cmd+"\n");
        writer.flush();
    }
    private ArrayList<String> readSocketMsg() throws IOException {
        ArrayList<String> msgList=new ArrayList<>();
        InputStreamReader isr=new InputStreamReader(socket.getInputStream(),"UTF-8");
        bufferedReader=new BufferedReader(isr);
        String numStr = bufferedReader.readLine();
        int linNum = Integer.parseInt(numStr);
        msgType=SERVER_MSG_ERROR;
        if(linNum<1){
            msgList.add("Receive empty message");
            return msgList;
        }
        String status = bufferedReader.readLine();
        if(status.equalsIgnoreCase("COM")){
            msgType=SERVER_MSG_OK;
        }else if(status.equalsIgnoreCase("DLF")){
            msgType=SERVER_MSG_DLF;
        }else if(status.equalsIgnoreCase("ULF")){
            msgType=SERVER_MSG_ULF;
        }else if(status.equalsIgnoreCase("DIR")){
            msgType=SERVER_MSG_DIR;
        }else if(status.equalsIgnoreCase("OPN")){
            msgType=SERVER_MSG_OPN;
        }else if(status.equalsIgnoreCase("KEY")){
            msgType=SERVER_MSG_KEY;
        }else if(status.equalsIgnoreCase("MOV")){
            msgType=SERVER_MSG_MOV;
        }else if(status.equalsIgnoreCase("CLK")){
            msgType=SERVER_MSG_CLK;
        }else if(status.equalsIgnoreCase("CMD")){
            msgType=SERVER_MSG_CMD;
        }else if(status.equalsIgnoreCase("CPS")){
            msgType=SERVER_MSG_CPS;
        }else{
            msgList.add(status);//将服务端的错误信息放入消息列表
        }
        for (int i = 1; i <linNum ; i++) {
            String s = bufferedReader.readLine();
            msgList.add(s);
        }
        return msgList;
    }
    private void close() throws IOException {
        bufferedReader.close();
        writer.close();
        socket.close();
    }
    private void doCmdTask(String cmd){
        ArrayList<String> msgList=new ArrayList<>();
        try {
            connect();
            writeCmd(cmd);
            msgList = readSocketMsg();
            close();
        } catch (IOException e) {
            msgType=SERVER_MSG_ERROR;
            msgList.add(e.toString());
            e.printStackTrace();
        }
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_SERVER_ACK_MSG,msgList);
        message.arg2=msgType;
        message.setData(bundle);
        handler.sendMessage(message);

    }
    public void work(final String cmd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCmdTask(cmd);
            }
        }).start();
    }
}