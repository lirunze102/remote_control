package lrz.data;

import lrz.base.BaseOperator;
import lrz.tool.VisualKeyMap;

import java.awt.*;
import java.util.ArrayList;

public class KEY extends BaseOperator {
    private Robot robot;
    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {
        ArrayList<String> ackMsg = new ArrayList<String>();
        robot=new Robot();
        int splitIdx = cmdBody.indexOf(",");
        if (splitIdx < 1) {
            int splitIdx2 = cmdBody.indexOf("+");
            if(splitIdx2<1){
                singleKeyPress(cmdBody);
            }else{
                simpleComboKeyPress(cmdBody);//组合键
            }
        }else{
            String keyPressStr=cmdBody.substring(0, splitIdx);
            String keyReleaseStr=cmdBody.substring(splitIdx+1);
            comboKeyPress(keyPressStr,keyReleaseStr);
        }

        ackMsg.add("成功运行热键命令 key:"+cmdBody);
        return ackMsg;
    }
    private void simpleComboKeyPress(String keyPressStr){
        String[] keyPressArray = keyPressStr.split("\\+");
        //split里的字符符合正则表达式规则， "+”是正则表达式的关键词，不能直接用，需要转义，而\本身是转义，所以需要\\来表示
        for(int i=0;i<keyPressArray.length;i++){
            int keycode = VisualKeyMap.getVisualKey(keyPressArray[i]);
            robot.keyPress(keycode);
        }
        for(int i=keyPressArray.length-1;i>=0;i--){//反序释放按键
            int keycode = VisualKeyMap.getVisualKey(keyPressArray[i]);
            robot.keyRelease(keycode);
        }
    }

    private  void comboKeyPress(String keyPressStr, String keyReleaseStr) {
        // TODO Auto-generated method stub
        String[] keyPressArray = keyPressStr.split("\\+");
        String[] keyReleaseArray = keyReleaseStr.split("\\+");
        for(int i=0;i<keyPressArray.length;i++){
            int keycode = VisualKeyMap.getVisualKey(keyPressArray[i]);
            robot.keyPress(keycode);

        }
        for(int i=0;i<keyReleaseArray.length;i++){
            int keycode = VisualKeyMap.getVisualKey(keyReleaseArray[i]);
            robot.keyRelease(keycode);
        }
    }

    private void singleKeyPress(String cmdBody) {
        // TODO Auto-generated method stub
        int keycode = VisualKeyMap.getVisualKey(cmdBody);
        robot.keyPress(keycode);
        robot.keyRelease(keycode);

    }

}