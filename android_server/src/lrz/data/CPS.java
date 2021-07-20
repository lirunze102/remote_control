package lrz.data;

import lrz.base.BaseOperator;
import lrz.tool.VisualKeyMap;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CPS extends BaseOperator {

    private Robot robot;

    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {
        //cps:somestring
        ArrayList<String> ackMsg = new ArrayList<String>();

        robot=new Robot();
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取剪切板
        Transferable tText = new StringSelection(cmdBody);//cmdBody为String字符串，需要拷贝进剪贴板的内容
        clip.setContents(tText, null);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        ackMsg.add("成功远程输入:"+cmdBody);
        return ackMsg;
    }


}