import java.text.SimpleDateFormat;

public class test {
    public static void main(String []args) {
       System.out.println("Hello World test");
      
       System.out.println(test.isValidDate("2017-09-01")) ;
       System.out.println("Hello World test end");
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