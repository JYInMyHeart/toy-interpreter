package mal;

import java.util.Scanner;
import static mal.Reader.*;
import static mal.Types.*;
/**
 * @author xck
 */
public class Step1_read_print {
    static MalType READ(String exp){
        return readStr(exp);
    }
    static MalType EVAL(MalType exp){
        return exp;
    }
    static void PRINT(MalType exp){
         Printer.prStr(exp);
    }
    static void rep(String exp){
         PRINT(EVAL(READ(exp)));

    }

    public static void main(String[] args) {
        while(true){
            System.out.print("user>");
            Scanner s = new Scanner(System.in);
            String exp = "";
            exp += s.nextLine();
            if("EOF".equals(exp)){
                System.exit(1);
            }else{
                rep(exp);
            }
        }
    }
}
