package com.example.sockettest.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sockettest.R;

public class ShowDialog_opt {
    private SocketClient socketClient;
    private Handler handler;
    private String ip;

    private String port;

    public ShowDialog_opt(SocketClient socketClient, Handler handler, String ip, String port ) {
        this.socketClient = socketClient;
        this.handler = handler;
        this.port = port;
        this.ip = ip;
    }

    SocketClient newsoc(){
        int p=Integer.parseInt(port);
        SocketClient soc=new SocketClient(ip,p,handler);
        return soc;
    }


    public void showDialog_opt_do(Context context){

        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("控制面板");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_do, null, false);

        final Button bt1=v.findViewById(R.id.vk_alt_tab);
        final Button bt2=v.findViewById(R.id.vk_f5);
        final Button bt3=v.findViewById(R.id.vk_win_d);
        final Button bt4=v.findViewById(R.id.vk_ctrl_f4);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_alt+vk_tab,vk_tab+vk_alt");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_f5");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:win+vk_d,vk_d+win");
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_ctrl+vk_f4,vk_f4+vk_ctrl");
            }
        });
        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = "快捷键模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();
    }

    public void showDialog_opt_cps(Context context){

        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("远程输入");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_cps, null, false);

        EditText et=v.findViewById(R.id.cps_input);
        Button bt1=v.findViewById(R.id.cps_sumbit);
        Button bt2=v.findViewById(R.id.cps_clean);
        TextView tv=v.findViewById(R.id.cps_show);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("cps:"+et.getText().toString());
                tv.setText("成功输入字段："+et.getText().toString());
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = "远程输入模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();
    }




    public void showDialog_opt_cmd(Context context){

        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("网站访问");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_cmd, null, false);

        TextView tv=v.findViewById(R.id.cmd_show);
        EditText et=v.findViewById(R.id.cmd_input);
        Button bt1=v.findViewById(R.id.cmd_sumbit);
        Button bt2=v.findViewById(R.id.cmd_clean);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("cmd:"+et.getText().toString());
                tv.setText("成功访问网站："+et.getText().toString());
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = "浏览器访问模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();
    }



    public void show_Dialog_opt_show(Context context){
        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("幻灯片控制");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_show, null, false);

        final Button bt1=v.findViewById(R.id.vk_f5);
        final Button bt2=v.findViewById(R.id.vk_right);
        final Button bt3=v.findViewById(R.id.vk_left);
        final Button bt4=v.findViewById(R.id.vk_esc);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_f5,vk_f5");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_right,vk_right");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_left,vk_left");
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("key:vk_escape,vk_escape");
            }
        });


        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = "幻灯片控制模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();
    }






    public void showDialog_opt_mov(Context context) {
        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("鼠标增量操作");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_mov, null, false);


        EditText et=v.findViewById(R.id.mov_v);
        String mov_v=et.getText().toString();
        Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9;
        bt1=v.findViewById(R.id.mov_left_up);
        bt2=v.findViewById(R.id.mov_up);
        bt3=v.findViewById(R.id.mov_right_up);
        bt4=v.findViewById(R.id.mov_left);
        bt5=v.findViewById(R.id.mov_center);
        bt6=v.findViewById(R.id.mov_right);
        bt7=v.findViewById(R.id.mov_left_down);
        bt8=v.findViewById(R.id.mov_down);
        bt9=v.findViewById(R.id.mov_right_down);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:-"+mov_v+",-"+mov_v);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:0,-"+mov_v);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:"+mov_v+",-"+mov_v);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:-"+mov_v+",0");
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("clk:left");
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:"+mov_v+",0");
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:-"+mov_v+","+mov_v);
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:0,"+mov_v);
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("mov:"+mov_v+","+mov_v);
            }
        });



        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = "鼠标增量模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();}



}
