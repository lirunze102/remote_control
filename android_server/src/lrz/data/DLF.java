package lrz.data;

import lrz.base.BaseOperator;
import lrz.tool.FileDownLoadSocketThread;

import java.io.File;
import java.util.ArrayList;

public class DLF extends BaseOperator {
    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {
        //dlf:remoteFile_path_file_name
        ArrayList<String> ackMsg = new ArrayList<String>();

        String[] cmd=cmdBody.split("\\?");
        File file = new File(cmd[0]);


        long filePos=Long.parseLong(cmd[1]);

        FileDownLoadSocketThread fileDownLoadSocketThread=new FileDownLoadSocketThread(file,filePos);

        fileDownLoadSocketThread.work();


        ackMsg.add(fileDownLoadSocketThread.port+">"+file.length()+">"+cmd[0]);

        return ackMsg;
    }
}
