package lrz.data;

import lrz.base.BaseOperator;
import lrz.tool.VisualKeyMap;

import java.awt.*;
import java.util.ArrayList;

public class CLK extends BaseOperator {
    private Robot robot;


    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {
        ArrayList<String> ackMsg = new ArrayList<String>();
        robot=new Robot();

        singleKeyPress(cmdBody);


        ackMsg.add("成功运行操作 clk:"+cmdBody);
        return ackMsg;
    }

    private void singleKeyPress(String cmdBody) {
        // TODO Auto-generated method stub
        int keycode = VisualKeyMap.getVisualKey(cmdBody);
        System.out.println(keycode);
        robot.mousePress(keycode);
        robot.mouseRelease(keycode);

    }

}