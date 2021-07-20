package lrz.tool;
;
import lrz.data.*;
import java.util.ArrayList;

public class MultiOperator {

    public static ArrayList<String> exeCmd(String list) throws Exception {
        ArrayList<String> msgBackList = new ArrayList<String>();

        String[] cmd_list=list.split("!");
        System.out.println(cmd_list);

        for (String cmd:cmd_list) {
            System.out.println(cmd);
            String[] splitCmd=splitCmd(cmd);
            String cmdHead = splitCmd[0];
            String cmdBody = splitCmd[1];
            if (cmdHead.equals("dlf")){
                msgBackList.add(new DLF().exe(cmdBody).get(0));//下载
            }
        }
        if(msgBackList.size()>0){
            msgBackList.add(0,"批量下载成功");
            msgBackList.add(0,"com");//增加正常执行返回的代码"ok"
        }else{
            msgBackList.add(0,"命令执行有误");//msgBackList长度为0，说明命令没有得到解析和执行
        }
        msgBackList.forEach(s -> System.out.println(s));
        return msgBackList;
    }
    public static String[] splitCmd(String cmd) throws Exception {
        String[] cmdout = null;
        int splitIdx = cmd.indexOf(":");
        System.out.println("服务端接受命令: " + cmd);
        if (splitIdx < 1) {
            throw new Exception("非法命令: " + cmd);// 抛出异常
        } else {
            cmdout = new String[2];
            String cmdHead = cmd.substring(0, splitIdx);
            String cmdBody = cmd.substring(splitIdx + 1);
            cmdout[0] = cmdHead.toLowerCase();//按小写处理字符串
            cmdout[1] = cmdBody;
        }
        return cmdout;

    }
}
