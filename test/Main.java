import java.io.IOException;


public class Main implements RealtimeProcessInterface{

    /**
     * @param args
     */
    private RealtimeProcess mRealtimeProcess = null;
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new Main().test();
    }
    public void test(){
        mRealtimeProcess = new RealtimeProcess(this);
        mRealtimeProcess.setDirectory("C:\\Users\\Administrator\\Desktop\\output\\cm");
        mRealtimeProcess.setCommand("C:\\Users\\Administrator\\Desktop\\myProject\\pack\\test\\run.bat");
//        String[] cmdList = new String[]{"cmd.exe /c mvn clean"};
//        mRealtimeProcess.setCommand(cmdList);
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
        //System.out.println(mRealtimeProcess.getAllResult());
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
