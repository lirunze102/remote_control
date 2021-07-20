package com.example.sockettest.data;

public class NetFileData {
    private long fileSize = 0;// 文件长度应该long型数据，否则大于2GB的文件大小无法表达
    private String fileName = "$error";// 文件名称，不含目录信息,默认值用于表示文件出错
    private String filePath = ".\\";// 该文件对象所处的目录，默认值为当前相对目录
    private String fileSizeStr = "文件夹";// 文件的大小，用字符串表示，能智能地选择B、KB、MB、GB来表达
    private int fileType = 0;// fileType=0为文件，fileType=1为普通文件夹，fileType=2为盘符
    private String fileModifiedDate = "1970-01-01 00:00:00";// 文件最近修改日期，默认值为1970年基准时间

    public NetFileData(String fileInfo, String filePath2){
        String[] data=fileInfo.split(">");
        fileName=data[0];
        fileModifiedDate=data[1];
        fileSize=Long.parseLong(data[2]);
        fileType=Integer.parseInt(data[3]);
        if (fileSize>0){
            if (fileSize>=1024 ){
                long size_KB=fileSize/1024;
                if (size_KB>=1024){
                    long size_MB=size_KB/1024;
                    if (size_MB>=1024){
                        double a=1.0;
                        double size_GB=size_MB/a/1024;
                        fileSizeStr=size_GB+" GB";
                    }else {
                        fileSizeStr=size_MB+" MB";
                    }
                }else {
                    fileSizeStr=size_KB+" KB";
                }

            }else {
                fileSizeStr="1 KB";
            }
        }
        filePath=filePath2;

    }

//    public long getFileSize() {
//        return fileSize;
//    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public int getFileType() {
        return fileType;
    }

    public String getFileModifiedDate() {
        return fileModifiedDate;
    }
}
