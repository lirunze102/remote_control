package lrz.server;
import lrz.tool.Operator;

import javax.swing.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

public class test {
    public static Socket socket;
    int port = 8019;// 自定义一个端口，端口号尽可能挑选一些不被其他服务占用的端口，祥见http://blog.csdn.net/hsj521li/article/details/7678880
    static int connect_count = 0;// 连接次数统计
    ArrayList<String>  msgBackList;


    public test() {
        // TODO Auto-generated constructor stub
    }

    public test(int port) {
        super();
        this.port = port;
    }

    public static void printLocalIp(ServerSocket serverSocket) {// 枚举打印服务端的IP
        try {
            System.out.println("服务端命令端口prot=" + serverSocket.getLocalPort());
            int port=serverSocket.getLocalPort(),i=0;
            Enumeration<NetworkInterface> interfaces = null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    i++;
                    InetAddress nextElement = addresss.nextElement();
                    String hostAddress = nextElement.getHostAddress();
                    System.out.println("本机IP地址为：" + hostAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void work() throws Exception {
        // 注意：由于Socket的工作是阻塞式，Android端Socket的工作必须在新的线程中实现，若在UI主线程中工作会报错
        ServerSocket serverSocket = new ServerSocket(port);
        printLocalIp(serverSocket);

        while (true) {// 无限循环
            System.out.println("=================================");
            System.out.println("| Waiting client to connect.....|");
            System.out.println("=================================");



            socket = serverSocket.accept();// 阻塞式
            System.out.println("连接请求来自: "+ socket.getRemoteSocketAddress().toString());

            try{
                getAndDealCmd(socket);
            } catch (Exception e) {
                cmdFail(e.toString());
            }
            SocketMsg.writeBackMsg(socket,msgBackList);

            System.out.println("..............输出流..............");

            msgBackList.forEach(s -> System.out.println(s));


            socket.close();

            System.out.println("本次Socket服务结束");



        }


    }


    public void getAndDealCmd(Socket socket) throws Exception {

        ArrayList<String> cmdList = SocketMsg.readSocketMsg(socket);
        if(cmdList.size()==0){
            cmdFail("Cmd size is 0. ");//若命令长度0行，则返回错误信息
        }

        System.out.println("..............输入流..............");
        cmdList.forEach(s -> System.out.println(s));
        System.out.println(".................................");

        if(cmdList.size()==1){
            msgBackList=Operator.exeCmd(cmdList.get(0));// Operator类为自定义类，实现命令头部和主体的分离和调用判断
        }
//        else{
//            	msgBackList= MultiOperator.exeCmd(cmdList);//待实现的，支持多条命令串行执行
//
//        }
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

        new test().work();
    }
}



