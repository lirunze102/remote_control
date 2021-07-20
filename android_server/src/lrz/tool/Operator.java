package lrz.tool;

import lrz.data.*;

import java.util.ArrayList;

public class Operator {
    public static ArrayList<String> exeCmd(String cmd) throws Exception {
        //所有的命令操作在此静态函数中判断及调用返回
        //后续新增的命令判断操作在此函数中添加
        ArrayList<String> msgBackList = new ArrayList<String>();
        if(cmd.substring(0,4).equals("com!")){
            msgBackList=MultiOperator.exeCmd(cmd.substring(4));
            return msgBackList;
        }

        String[] splitCmd = splitCmd(cmd);//按":"分割命令
        // 分割时，会把命令前缀转为小写，以保证判断不分大小写
        String cmdHead = splitCmd[0];
        String cmdBody = splitCmd[1];



        if (cmdHead.equals("dir")) {
            msgBackList = new DIR().exe(cmdBody);
            msgBackList.add(0,"dir");//文件列表操作命令
            return msgBackList;
        }
        if (cmdHead.equals("opn")) {
            msgBackList = new OPN().exe(cmdBody); // 打开文件操作命令
            msgBackList.add(0,"opn");
            return msgBackList;
        }
        if (cmdHead.equals("key")) {
            msgBackList = new KEY().exe(cmdBody);//组合按键操作命令
            msgBackList.add(0,"key");
            return msgBackList;
        }
        if (cmdHead.equals("mov")) {
            msgBackList = new MOV().exe(cmdBody);//鼠标移动命令
            msgBackList.add(0,"mov");
            return msgBackList;
        }
        if (cmdHead.equals("clk")) {
            msgBackList = new CLK().exe(cmdBody);//鼠标事件命令
            msgBackList.add(0,"clk");
            return msgBackList;
        }
        if (cmdHead.equals("cmd")){
            msgBackList = new CMD().exe(cmdBody);//调用默认浏览器打开指定网址命令
            msgBackList.add(0,"cmd");
            return msgBackList;
        }
        if (cmdHead.equals("cps")){
            msgBackList = new CPS().exe(cmdBody);//远程输入命令
            msgBackList.add(0,"cps");
            return msgBackList;
        }
        if (cmdHead.equals("dlf")){
            msgBackList = new DLF().exe(cmdBody);//下载
            msgBackList.add(0,"dlf");
            return msgBackList;
        }
        if (cmdHead.equals("ulf")){
            msgBackList = new ULF().exe(cmdBody);//下载
            msgBackList.add(0,"ulf");
            return msgBackList;
        }
        //... 继续判断其他命令

        throw new Exception("无效命令!");//若代码正确执行了，就return了，不会执行到这里，执行到这说明出错了
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