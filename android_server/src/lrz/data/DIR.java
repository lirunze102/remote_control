package lrz.data;

import lrz.base.BaseOperator;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DIR extends BaseOperator {
    @Override
    public  ArrayList<String> exe(String cmdBody) throws Exception {
        // TODO Auto-generated method stub
        ArrayList<String> backList=new ArrayList<String>();

        File file = new File(cmdBody);
        File[] listFiles = file.listFiles();
        for(File mfile:listFiles){
            String fileName = mfile.getName();
            long lastModified = mfile.lastModified();//获取文件修改时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//给时间格式，例如：2018-03-16 09:50:23
            String fileDate = dateFormat.format(new Date(lastModified));//取得文件最后修改时间，并按格式转为字符串
            String fileSize="0";
            String isDir="1";
            if(!mfile.isDirectory()){//判断是否为目录
                isDir="0";
                fileSize=""+mfile.length();
            }
            backList.add(fileName+">"+fileDate+">"+fileSize+">"+isDir+">");
        }
        return backList;
    }
}
