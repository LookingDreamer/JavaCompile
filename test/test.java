import java.text.SimpleDateFormat;
import java.util.regex.*;

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