package lrz.data;

import lrz.base.BaseOperator;
import lrz.server.ServerMain;
import lrz.tool.FileUpLoadSocketThread;

import java.io.File;
import java.util.ArrayList;

public class ULF extends BaseOperator {
    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {
        //ulf:file_name
        ArrayList<String> ackMsg = new ArrayList<String>();

        String[] cmd=cmdBody.split(",");
        String dir="d://androidtest/"+cmd[0];

        File file = new File(dir);
        System.out.println(ServerMain.socket.getRemoteSocketAddress().toString()+cmd[0]+cmd[2]+cmd[1]);
        String[] ip_phone=ServerMain.socket.getRemoteSocketAddress().toString().split(":");
        String ip=ip_phone[0].substring(1);
        FileUpLoadSocketThread fileUpLoadSocketThread=
                new FileUpLoadSocketThread(ip,Integer.parseInt(cmd[2]),file,Long.parseLong(cmd[1]));

        fileUpLoadSocketThread.work();
        ackMsg.add(cmd[0]+"  上传成功，已上传至D://androidtest/");

        return ackMsg;
    }
}

