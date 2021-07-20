package lrz.data;

import lrz.base.BaseOperator;
import lrz.tool.VisualKeyMap;

import java.awt.*;
import java.util.ArrayList;

public class MOV extends BaseOperator {
    private Robot robot;
    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {

        ArrayList<String> ackMsg = new ArrayList<String>();
        robot=new Robot();

        Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
        System.out.println("Location:x=" + point.x + ", y=" + point.y);

        //例如 10,-20
        int splitIdx = cmdBody.indexOf(",");
        String m_x=cmdBody.substring(0, splitIdx);
        String m_y=cmdBody.substring(splitIdx+1);

        int p_x,p_y;
        p_x=Integer.parseInt(m_x);
        p_y=Integer.parseInt(m_y);

        int t_x,t_y;
        t_x=point.x+p_x;
        t_y=point.y+p_y;

        robot.mouseMove(t_x,t_y);

        ackMsg.add("从x:"+point.x+" ,y:"+point.y+" 移动至x:"+t_x+" ,y:"+t_y);
        return ackMsg;
    }



}