package lrz.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketMsg {


    public static void writeBackMsg(Socket socket, ArrayList<String> msgBackList) throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
        OutputStreamWriter writer=new OutputStreamWriter(os,"UTF-8");
        writer.write(""+msgBackList.size()+"\n");           //未真正写入的输出流，仅仅在内存中
        writer.flush();                                         //写入输出流，真正将数据传输出去
        for(int i=0;i<msgBackList.size();i++){
            writer.write(msgBackList.get(i)+"\n");
            writer.flush();
        }

    }

    public static ArrayList<String> readSocketMsg(Socket socket) throws IOException {
        ArrayList<String> msgList=new ArrayList<String>();
        InputStream inputStream = socket.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader=new BufferedReader(reader);
        String lineNumStr = bufferedReader.readLine();
        int lineNum=Integer.parseInt(lineNumStr);
        for(int i=0;i<lineNum;i++){
            String str = bufferedReader.readLine();
            msgList.add(str);
        }
        //读取结束后，输入流不能关闭，此时关闭，会将socket关闭，从而导致后续对socket写操作无法实现
        return msgList;
    }

}
