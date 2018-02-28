import java.util.Scanner;

/**
 * @author xck
 */
public class Step1_read_print {
    static String READ(String exp){
        return exp;
    }
    static String EVAL(String exp){
        return exp;
    }
    static String PRINT(String exp){
        return exp;
    }
    static void rep(String exp){
        String rep = PRINT(EVAL(READ(exp)));
        System.out.println(rep);
    }

    public static void main(String[] args) {
        while(true){
            System.out.print("user>");
            Scanner s = new Scanner(System.in);
            String exp = s.next();
            if("EOF".equals(exp)){
                System.exit(1);
            }else{
                rep(exp);
            }
        }
    }
}
