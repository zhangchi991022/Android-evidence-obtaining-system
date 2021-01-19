package com.example.myapplication;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.ContextWrapper;

public class Client extends Socket {

    private static final String SERVER_IP = "154.202.60.121"; // 服务端IP
    private static final int SERVER_PORT = 8899; // 服务端端口

    private Socket client;


    private DataOutputStream dos;

    /**
     * 构造函数<br/>
     * 与服务器建立连接
     * @throws Exception
     */
    public Client() throws Exception {
        super(SERVER_IP, SERVER_PORT);
        this.client = this;
        System.out.println("Client[port:" + client.getLocalPort() + "] 成功连接服务端");
    }

    /**
     * 向服务端传输文件
     * @throws Exception
     */
    public void sendFile(FileInputStream fis,String filename) throws Exception {
        try {


            if(true) {
//                fis = openFileInput("test.txt");
                dos = new DataOutputStream(client.getOutputStream());

                // 文件名和长度
                dos.writeUTF(filename);
                dos.flush();


                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[65536];
                int length = 0;
                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
//                    System.out.print("| " + (100*progress/file.length()) + "% |");
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                fis.close();
            if(dos != null)
                dos.close();
            client.close();
        }
    }

    /**
     * 入口
     * @param args
     */
//    public static void main(String[] args) {
//        boolean flag = true;
//        while (flag) {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//            String time = df.format(new Date());
//            System.out.println(time);
//            if (time.equals("2020-09-09 21:54:00")) {
//                System.out.println(time);// new Date()为获取当前系统时间
//                flag = false;
//            }
//        }
//        try {
//            Client client = new Client(); // 启动客户端连接
//            client.sendFile(); // 传输文件
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}