package lrz.tool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileDownLoadSocketThread {
    private ServerSocket serverSocket;
    private long filePos=0;
    private File file;
    public static int port;

    public FileDownLoadSocketThread(File file,long filePos) {
        // TODO Auto-generated constructor stub
        try {
            serverSocket = new ServerSocket(0);//动态分配可用端口
            port=serverSocket.getLocalPort();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.file=file;
        this.filePos=filePos;
    }
    
    public void doCmdTask() throws IOException {
        Socket socket = serverSocket.accept();
        OutputStream outputStream=socket.getOutputStream();

        FileInputStream fis=new FileInputStream(file);
        DataInputStream dis=new DataInputStream(new BufferedInputStream(fis));

        DataOutputStream dataOutputStream=new DataOutputStream(outputStream);

        dataOutputStream.writeLong((Long) file.length());
        dataOutputStream.flush();

        byte[] buffer = new byte[8192];
        while(true) {
            int read = 0;
            if(dis!=null){
                read = fis.read(buffer);
            }
            if(read == -1){
                break;
            }
            dataOutputStream.write(buffer,0,read);
        }

        dataOutputStream.flush();
        dis.close();
        socket.close();
        outputStream.flush();
        System.out.println("线程结束"+port);
    }

    public void work(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doCmdTask();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
