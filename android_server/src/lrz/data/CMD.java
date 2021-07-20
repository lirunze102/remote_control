package lrz.data;

import lrz.base.BaseOperator;
import java.util.ArrayList;

public class CMD extends BaseOperator {

    @Override
    public ArrayList<String> exe(String cmdBody) throws Exception {
        //cmd:www.wzu.edu.cn
        ArrayList<String> ackMsg = new ArrayList<String>();

        String cmd="cmd /c start "+cmdBody;

        Runtime.getRuntime().exec(cmd);

        ackMsg.add("成功运行:"+cmd);
        return ackMsg;
    }
}