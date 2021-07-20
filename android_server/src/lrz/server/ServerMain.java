package lrz.server;
import lrz.data.PrintDataClass;
import lrz.tool.Operator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class ServerMain {
    public static Socket socket;
    int port = 8019;// 自定义一个端口，端口号尽可能挑选一些不被其他服务占用的端口，祥见http://blog.csdn.net/hsj521li/article/details/7678880
    static int connect_count = 0;// 连接次数统计
    ArrayList<String>  msgBackList;
    TextArea tv;
    JTextField server_ip,server_port;
    public ServerMain() {
        // TODO Auto-generated constructor stub
    }

    public ServerMain(int port) {
        super();
        this.port = port;
    }

    public ServerMain(TextArea tv,JTextField server_ip,JTextField server_port) {
        this.tv = tv;
        this.server_ip=server_ip;
        this.server_port=server_port;
    }

    public static void printLocalIp(ServerSocket serverSocket, JTextField server_ip, JTextField server_port, TextArea tv) {// 枚举打印服务端的IP
        try {
            System.out.println("服务端命令端口prot=" + serverSocket.getLocalPort());
            int port=serverSocket.getLocalPort(),i=0;
            server_port.setText(Integer.toString(port));
            Enumeration<NetworkInterface> interfaces = null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    i++;
                    InetAddress nextElement = addresss.nextElement();
                    String hostAddress = nextElement.getHostAddress();
                    tv.append("本机IP地址为：" + hostAddress+"\n");
                    if(i==7){
                        server_ip.setText(hostAddress);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void work() throws Exception {
        // 注意：由于Socket的工作是阻塞式，Android端Socket的工作必须在新的线程中实现，若在UI主线程中工作会报错
        ServerSocket serverSocket = new ServerSocket(port);


        printLocalIp(serverSocket,server_ip,server_port,tv);


        while (true) {// 无限循环

            tv.append("=================================\n");
            tv.append("| Waiting client to connect.....|\n");
            tv.append("=================================\n");

            socket = serverSocket.accept();// 阻塞式
            tv.append("连接请求来自: "+ socket.getRemoteSocketAddress().toString()+"\n");
            try{
                getAndDealCmd(socket);
            } catch (Exception e) {
                cmdFail(e.toString());
            }
            SocketMsg.writeBackMsg(socket,msgBackList);

            tv.append("..............输出流..............\n");
            msgBackList.forEach(s -> tv.append(s+"\n"));
            tv.append(".................................\n");
            socket.close();
            tv.append("本次Socket服务结束\n\n\n");

        }


    }


    public void getAndDealCmd(Socket socket) throws Exception {

        ArrayList<String> cmdList = SocketMsg.readSocketMsg(socket);
        if(cmdList.size()==0){
            cmdFail("Cmd size is 0. ");//若命令长度0行，则返回错误信息
        }

        cmdList.forEach(s -> System.out.println(s));
        tv.append("..............输入流..............");
        tv.append(".................................");

        if(cmdList.size()==1){
            msgBackList=Operator.exeCmd(cmdList.get(0));
        }
    }

    private void cmdFail(String e) {
        msgBackList.clear();//
        String nu="java.lang.NullPointerException";
        if(e.equals(nu)){
            e="目标不存在";
        }
        msgBackList.add(e);//将出错信息放入msgBackList
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        JFrame f = new JFrame("服务端");
        f.setSize(800, 600);
        f.setLocation(500, 200);
        f.setLayout(null);

        JPanel pInput = new JPanel();
        pInput.setBounds(150, 10, 450, 40);
        pInput.setLayout(new FlowLayout());


        JLabel server_Ip = new JLabel("服务器IP:");
        JTextField server_IpText = new JTextField(15);
        JLabel server_Port = new JLabel("主端口:");
        JTextField server_PortText = new JTextField(5);
        JButton b = new JButton("启动服务");

        pInput.add(server_Ip);
        pInput.add(server_IpText);
        pInput.add(server_Port);
        pInput.add(server_PortText);
        pInput.add(b);

        //文本域
        ;//流布局
        TextArea ta = new TextArea();
        ta.setBounds(20, 60, 740, 460);
        ta.setFont(new Font("楷体",Font.PLAIN,20));


        f.add(pInput);
        f.add(ta);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setVisible(true);

        //鼠标监听
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ta.append("------------服务启动中-----------\n");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new ServerMain(ta,server_IpText,server_PortText).work();
                        } catch (Exception ex) {
                            ta.append(ex.toString());
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }


}



