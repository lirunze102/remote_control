package lrz.data;

import lrz.base.BaseOperator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OPN extends BaseOperator {
    @Override
    public  ArrayList<String> exe(String cmdBody) throws Exception {
        ArrayList<String> backList=new ArrayList<String>();
        Desktop desk=Desktop.getDesktop();
        File file=new File(cmdBody);//创建一个java文件系统
        try {
            desk.open(file); //调用open（File f）方法打开文件
            backList.add("成功运行文件："+cmdBody);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            backList.add(ex.toString());
        }
        return backList;
    }
}
