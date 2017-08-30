import java.text.SimpleDateFormat;
import java.util.regex.*;
import java.io.File;
import java.io.IOException;
//import org.apache.commons.io.FileUtils;
// import org.apache.commons.codec.digest.*;
// import org.apache.commons.io.IOUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class test {
    public static void main(String []args) {
       System.out.println("Hello World test");
      
       System.out.println(test.isValidDate("2017-09-01")) ;
       System.out.println("Hello World test end");

        String content = "/branches/cm/requirement/cm/src/main/java/com/zzb/mobile/service/impl/UserCenterServiceImpl.java";

        String pattern = ".*/src/main/java/.*";
        String a[] = content.split("/branches/cm/requirement/cm");
        System.out.println(a[0]);
        System.out.println(a[1]);

        String tmpFile = "C:/Users/Administrator/Desktop/output/cm/src/main/java/com/zzb/cm/Interface/service/impl/CoreInterFaceServiceImpl.java";

        File directory = new File(tmpFile);
        String courseFile = directory.getParent();
        String courseFile1 = courseFile.replace(File.separator,"/");
        String courseFileList[] = courseFile1.split("/src/main/java/");
        System.out.println("courseFile: "+courseFile);
        System.out.println("courseFile11111: "+courseFileList[1]);

        System.out.println("File path : " + courseFile);

        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("is include " + isMatch);
    }

    public static boolean isValidDate(String str) {
      boolean convertSuccess=true;
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       try {
          format.setLenient(false);
          format.parse(str);
       } catch (Exception e) {
          // e.printStackTrace();
           convertSuccess=false;
       } 
       return convertSuccess;
}

}