package lrz.tool;


import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;



public class FileUpLoadSocketThread {
    private String ip;
    private int port;
    private Socket socket;
    File file;
    long fileSize,downLoadFileSize=0;
    private int connect_timeout=10000;
    public static int SERVER_MSG_OK=0;//用于发送给句柄的消息类型,放在消息的arg2中，表示服务端正常
    public static int SERVER_MSG_ERROR=1;//表示服务端出错
    private int msgType;
    public static final String KEY_SERVER_ACK_MSG_DOWNLOAD = "KEY_SERVER_ACK_MSG_DOWNLOAD";

    public FileUpLoadSocketThread(String ip, int port, File file, long fileSize) {
        this.ip = ip;
        this.port = port;
        this.file = file;
        this.fileSize = fileSize;
    }
    private void connect() throws IOException {//连接服务端函数
        InetSocketAddress address = new InetSocketAddress(ip, port);
        socket = new Socket();
        socket.connect(address, connect_timeout);
    }

    private void doCmdTask(){
        try {
            connect();

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

            System.out.println("文件下载完成");
            dos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
