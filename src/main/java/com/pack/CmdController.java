package com.pack;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.websocket.Session;

@Controller
@RequestMapping("/cmdController")
public class CmdController implements RealtimeProcessInterface{

    /**
     * @param args
     */
    private RealtimeProcess mRealtimeProcess = null;

    @RequestMapping(value = "/cmdTail",method = RequestMethod.POST, produces = "application/json;text/html;charset=UTF-8")
    @ResponseBody
    public String   cmdTail(HttpServletRequest request, PackBean packBean){
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setDirectory("C:\\Users\\Administrator\\Desktop\\output\\cm");
        mRealtimeProcess.setCommand("C:\\Users\\Administrator\\Desktop\\myProject\\pack\\test\\run.bat");

        try {
            try {
                mRealtimeProcess.start();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(mRealtimeProcess.getAllResult());

        return mRealtimeProcess.getAllResult();
    }
    public void onNewStdoutListener(String newStdout) {
        // TODO Auto-generated method stub
        System.out.println("==>STDOUT  >  " + newStdout);

    }
    public void onNewStderrListener(String newStderr) {
        // TODO Auto-generated method stub
        System.out.println("==>STDERR  >  " + newStderr);
    }
    public void onProcessFinish(int resultCode) {
        // TODO Auto-generated method stub
        System.out.println("==>RESULT_CODE  >  " + resultCode);
    }
}
