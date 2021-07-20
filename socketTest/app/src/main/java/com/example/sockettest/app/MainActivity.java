package com.example.sockettest.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sockettest.R;
import com.example.sockettest.data.NetFileData;
import com.example.sockettest.data.NetFileDataAdapter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_SERVER_ACK_MSG = "KEY_SERVER_ACK_MSG";
    public static final String KEY_SERVER_ACK_MSG_DOWNLOAD = "KEY_SERVER_ACK_MSG_DOWNLOAD";
    private static final String KEY_IP="ip";
    private static final String KEY_PORT="port";
    String ip;
    String port;
    String here;
    String cmd_dlf="com";

    private  Handler handler = null;
    private  Handler handler_download = null;
    EditText dir;
    ListView lv;
    ImageView submit,opt,down;
    Button back,phone_dir,phone_down,pc_d,pc_down;
    SocketClient socketClient=null;
    private PopupWindow mPopWindow;

    ArrayList<NetFileData> data;
    ArrayList<String> in_data;
    TextView here_tv,show_msg,cps_show;
    String file_dataname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initdata();

        dir=findViewById(R.id.dir);
        lv=findViewById(R.id.listview);
        submit=findViewById(R.id.submit);
        back=findViewById(R.id.back);
        phone_dir=findViewById(R.id.phone_dir);
        here_tv=findViewById(R.id.here);
        show_msg=findViewById(R.id.show_msg);
        cps_show=findViewById(R.id.cps_show);
        opt=findViewById(R.id.opt);
        phone_down=findViewById(R.id.phone_down);
        pc_d=findViewById(R.id.pc_d);
        pc_down=findViewById(R.id.pc_down);
        down=findViewById(R.id.down);

        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data_bundle = msg.getData();
                in_data=data_bundle.getStringArrayList(KEY_SERVER_ACK_MSG);
                int fp=msg.arg2;
                if(fp==SocketClient.SERVER_MSG_DIR){
                    data=dataMaker();
                    NetFileDataAdapter netFileDataAdapter=new NetFileDataAdapter(MainActivity.this,data);
                    lv.setAdapter(netFileDataAdapter);
                    show_msg.setText("成功执行dir操作");
                }else if(fp==SocketClient.SERVER_MSG_DLF){
                    String[] s=in_data.get(0).split(">");
                    String path = "/sdcard/android_test/"+file_dataname;
                    File file=new File(path);
                    FileDownLoadSocketThread fileDownLoadSocketThread=
                            new FileDownLoadSocketThread(ip,Integer.parseInt(s[0]),handler_download,file,Long.parseLong(s[1]),MainActivity.this);
                    fileDownLoadSocketThread.work_down_log();
                    fileDownLoadSocketThread.work();
                }else if(fp==SocketClient.SERVER_MSG_ULF){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_OPN){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_KEY){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_MOV){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_CLK){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_CMD){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_CPS){
                    show_msg.setText(in_data.get(0).toString());
                }else if(fp==SocketClient.SERVER_MSG_OK){
                    show_msg.setText(in_data.get(0).toString());
                    for (int i=1;i<in_data.size();i++){
                        String[] s=in_data.get(i).split(">");
                        int id=s[2].lastIndexOf("/");
                        String path = "/sdcard/android_test/"+s[2].substring(id+1);
                        File file=new File(path);
                        FileDownLoadSocketThread fileDownLoadSocketThread=
                                new FileDownLoadSocketThread(ip,Integer.parseInt(s[0]),handler_download,file,Long.parseLong(s[1]),MainActivity.this);
                        fileDownLoadSocketThread.work();
                    }
                }else {
                    String s=in_data.get(0);
                    int i=here.lastIndexOf("/");
                    here=here.substring(0,i);
                    show_msg.setText(s);
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
                }
                return false; }
        });

        opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu_down(v);
            }
        });

        handler_download=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle down_result = msg.getData();
                ArrayList<String> s=down_result.getStringArrayList(KEY_SERVER_ACK_MSG_DOWNLOAD);
                int fp=msg.arg2;
                if(fp==SocketClient.SERVER_MSG_OK){
                    Toast.makeText(MainActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                    show_msg.setText("下载成功，文件已保存在/ard/android_test文件夹中");
                }else {
                    Toast.makeText(MainActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                    show_msg.setText("上传成功，文件已保存d://androidtest文件夹至中");
                }

                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dir_test=dir.getText().toString().substring(0,4);
                if(dir_test.equals("dir:")){
                    here=dir.getText().toString();
                    savehere();
                }
                socketClient=newsoc();
                socketClient.work(dir.getText().toString());
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i=here.lastIndexOf("/");
                int j=here.length();
                String cmd=here.substring(0,7);
                if(cmd.equals("/sdcard")){
                    ArrayList<String> phone_dir=new ArrayList<String>();
                    cmd=here+"/"+data.get(position).getFileName();
                    phone_dir=filePhoneDir(cmd);
                    in_data=phone_dir;
                    here=cmd;
                    here_tv.setText("当前位置："+cmd);
                    data=dataMaker();
                    NetFileDataAdapter netFileDataAdapter=new NetFileDataAdapter(MainActivity.this,data);
                    lv.setAdapter(netFileDataAdapter);
                    show_msg.setText("成功访问本机目录："+cmd);
                }else {
                    if (i<j-1){
                        here=here+"/"+data.get(position).getFileName();
                    }else {
                        here=here+data.get(position).getFileName();
                    }
                    socketClient=newsoc();
                    socketClient.work(here);
                    savehere();
                }

            }
        });

        pc_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("dir:d://");
                here="dir:d://";
                savehere();
            }
        });

        pc_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketClient=newsoc();
                socketClient.work("dir:d://androidtest");
                here="dir:d://";
                savehere();
            }
        });

        phone_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> phone_dir=new ArrayList<String>();
                here="/sdcard";
                phone_dir=filePhoneDir(here);
                in_data=phone_dir;
                here_tv.setText("当前位置："+here);
                data=dataMaker();
                NetFileDataAdapter netFileDataAdapter=new NetFileDataAdapter(MainActivity.this,data);
                lv.setAdapter(netFileDataAdapter);
                show_msg.setText("成功访问本机目录");
            }
        });
        phone_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> phone_dir=new ArrayList<String>();
                here="/sdcard/android_test";
                phone_dir=filePhoneDir(here);
                in_data=phone_dir;
                here_tv.setText("当前位置："+here);
                data=dataMaker();
                NetFileDataAdapter netFileDataAdapter=new NetFileDataAdapter(MainActivity.this,data);
                lv.setAdapter(netFileDataAdapter);
                show_msg.setText("成功访问本地下载目录");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aa=here.substring(0,7);
                if (aa.equals("/sdcard")){
                    int i=here.lastIndexOf("/");
                    if(i>6){
                        here=here.substring(0,i);
                        ArrayList<String> phone_dir=new ArrayList<String>();
                        phone_dir=filePhoneDir(here);
                        in_data=phone_dir;
                        here_tv.setText("当前位置："+here);
                        data=dataMaker();
                        NetFileDataAdapter netFileDataAdapter=new NetFileDataAdapter(MainActivity.this,data);
                        lv.setAdapter(netFileDataAdapter);
                        show_msg.setText("成功访问本机目录："+here);
                    }else {
                        show_msg.setText("已在最外层位置");
                        Toast.makeText(MainActivity.this,"已在最外层位置",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    int i=here.lastIndexOf("/");
                    if (here.length()>8){
                        here=here.substring(0,i);
                        if(here.length()<9){
                            here=here+"/";
                        }
                        socketClient=newsoc();
                        socketClient.work(here);
                        savehere();
                        show_msg.setText("成功返回上一层目录");
                    }else {
                        show_msg.setText("已在最外层位置");
                        Toast.makeText(MainActivity.this,"已在最外层位置",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        registerForContextMenu(lv);
    }


    private ArrayList<String> filePhoneDir(String cmd){
        ArrayList<String> phone_dir=new ArrayList<String>();
        File file = new File(cmd);
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
            String phone_data=fileName+">"+fileDate+">"+fileSize+">"+isDir+">";
            phone_dir.add(phone_data);
        }
        return phone_dir;
    }

    private void showPopupMenu_down(final View view){
        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.down_menu, null);
        mPopWindow = new PopupWindow(contentView, 600,800, true);
        mPopWindow.setContentView(contentView);
        //点击事件
        Button bt=contentView.findViewById(R.id.down_begin);
        Button bt2=contentView.findViewById(R.id.down_clean);
        ListView lv=contentView.findViewById(R.id.down_list);
        String popcmd=cmd_dlf;
        popcmd=popcmd.substring(3);
        String[] popcmd_list=popcmd.split("!");
        ArrayList<String> down_dir=new ArrayList<>();
        for (int i = 1; i < popcmd_list.length; i++) {
            String s=popcmd_list[i];
            int a=s.lastIndexOf('?');
            s=s.substring(0,a);
            System.out.println(s);
            s=s.substring(4);
            down_dir.add(s);
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,down_dir);
        lv.setAdapter(adapter);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(cmd_dlf);
                newsoc();
                socketClient.work(cmd_dlf);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmd_dlf="com";
                showPopupMenu_down(v);
            }
        });
        View rootview = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null);
        mPopWindow.showAtLocation(rootview, Gravity.TOP, 180, 200);

    }


    private void showPopupMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this,view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.opt_menu,popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.to_do:
                        ShowDialog_opt showdialog1=new ShowDialog_opt(socketClient,handler,ip,port);
                        showdialog1.showDialog_opt_do(MainActivity.this);
                        break;
                    case R.id.to_show:
                        ShowDialog_opt showDialogOpt2 =new ShowDialog_opt(socketClient,handler,ip,port);
                        showDialogOpt2.show_Dialog_opt_show(MainActivity.this);
                        break;
                    case R.id.to_mov:
                        ShowDialog_opt showDialogOpt3 =new ShowDialog_opt(socketClient,handler,ip,port);
                        showDialogOpt3.showDialog_opt_mov(MainActivity.this);
                        break;
                    case R.id.to_cps:
                        ShowDialog_opt showDialog_opt4 =new ShowDialog_opt(socketClient,handler,ip,port);
                        showDialog_opt4.showDialog_opt_cps(MainActivity.this);
                        break;
                    case R.id.to_cmd:
                        ShowDialog_opt showDialog_opt5=new ShowDialog_opt(socketClient,handler,ip,port);
                        showDialog_opt5.showDialog_opt_cmd(MainActivity.this);
                        break;
                    case R.id.to_pad:
                        showDialog_opt_pad(MainActivity.this);
                        break;
                    case R.id.to_set:
                        showDialog_opt_set(MainActivity.this);
                        break;
                }
                return false;
            }
        });

        //关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }



    //新建socket通讯
    SocketClient newsoc(){
        int p=Integer.parseInt(port);
        SocketClient soc=new SocketClient(ip,p,handler);
        return soc;
    }

    //裁剪字符串得到当前位置
    private void savehere(){
        String s=here.substring(4);
        here_tv.setText("当前位置："+s);
    }

    //ip，port设置
    public void saveset(String c_ip, String c_port){
        ip=c_ip;
        port=c_port;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IP,ip);
        editor.putString(KEY_PORT,port);
        editor.apply();
    }

    //初始化
    private void initdata() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        ip=sharedPreferences.getString(KEY_IP,"192.168.43.12");
        port=sharedPreferences.getString(KEY_PORT,"8019");
    }

    //数据处理
    private ArrayList<NetFileData> dataMaker() {
        ArrayList<NetFileData> dataResult=new ArrayList<>();
        int i=in_data.size();
        for (int j = 0; j <i ; j++) {
            NetFileData netFileData=new NetFileData(in_data.get(j),here);
            dataResult.add(netFileData);
        }
        return  dataResult;
    }

    //长按操作文件
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.list_menu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos=contextMenuInfo.position;
        NetFileData netFileData=(NetFileData) lv.getAdapter().getItem(pos);//其中listView为显示文件列表的视图
        switch(item.getItemId()){
            case R.id.list_show:// 弹出热键对话框
                file_dataname=netFileData.getFileName();
                showDialog_list_main(netFileData,this,here,cmd_dlf);
                break;
            default :break;
        }
        return super.onContextItemSelected(item);
    }

    //鼠标板
    public void showDialog_opt_pad(Context context) {
        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("鼠标板操作");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_pad, null, false);

        TextView mousePad=v.findViewById(R.id.mouse_pad);
        final GestureDetector mGestureDetector=new GestureDetector(this, new MousePadOnGestureListener());
        mousePad.setOnTouchListener(new TextView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = "鼠标板模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();}
    class MousePadOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        //手势处理接口，通过集成SimpleOnGestureListener改写对应手势方法
        @Override
        public void onLongPress(MotionEvent e) {
            String s="clk:right";
            socketClient=newsoc();
            socketClient.work(s);
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击
            // TODO Auto-generated method stub
            String s="clk:left";
            socketClient=newsoc();
            socketClient.work(s);
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {//滚动
            // TODO Auto-generated method stub
//            Log.d("disx:",""+distanceX);
//            Log.d("disy:",""+distanceY);
//
            String s="mov:"+(int)-distanceX+","+(int)-distanceY;//手势方向与鼠标控制方向相反，对值取反
            socketClient=newsoc();
            socketClient.work(s);
            return true;
        }
    }

    //ip,port设置
    public void showDialog_opt_set(Context context){

        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("服务设置");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_opt_set, null, false);
        EditText et1,et2;
        et1=v.findViewById(R.id.opt_set_ip);
        et2=v.findViewById(R.id.opt_set_port);
        et1.setText(ip);
        et2.setText(port);
        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveset(et1.getText().toString(),et2.getText().toString());

                String s = "设置模式已关闭";
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
        bl.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this,requestCode, permissions, grantResults);
    }

    public  void showDialog_list_main(NetFileData netFileData, Context context, String here, String cmd_dlf_list) {
        cmd_dlf=cmd_dlf_list;
        AlertDialog.Builder bl = new AlertDialog.Builder(context);
        bl.setTitle("文件操作");
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_view_list, null, false);
        final Button bt1=v.findViewById(R.id.dialog_bt1);
        final Button bt2=v.findViewById(R.id.dialog_bt2);
        final Button bt3=v.findViewById(R.id.dialog_bt3);
        final Button bt4=v.findViewById(R.id.dialog_bt4);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=here.substring(4);
                socketClient=newsoc();
                socketClient.work("opn:"+s+"/"+netFileData.getFileName());
                String ss = netFileData.getFileName()+"即将在PC端打开";
                Toast.makeText(context,ss,Toast.LENGTH_SHORT).show();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=here.substring(4);
                socketClient=newsoc();
                socketClient.work("dlf:"+s+"/"+netFileData.getFileName()+"?0");
                String ss = netFileData.getFileName()+"开始下载";
                Toast.makeText(context,ss,Toast.LENGTH_SHORT).show();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=here.substring(4);
                File file_up=new File(here+"/"+netFileData.getFileName());
                FileUpLoadSocketThread fileUpLoadSocketThread=new FileUpLoadSocketThread(file_up,0);
                fileUpLoadSocketThread.work();
                socketClient=newsoc();
                socketClient.work("ulf:"+netFileData.getFileName()+","+file_up.length()+","+fileUpLoadSocketThread.port);
                String ss = netFileData.getFileName()+"开始上传";
                Toast.makeText(context,ss,Toast.LENGTH_SHORT).show();

            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=here.substring(4);
                cmd_dlf=cmd_dlf+"!dlf:"+s+"/"+netFileData.getFileName()+"?0";
                String ss = netFileData.getFileName()+"成功加入下载队列";
                Toast.makeText(context,ss,Toast.LENGTH_SHORT).show();

            }
        });


        bl.setView(v);
        bl.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        bl.create().show();
    }
}


