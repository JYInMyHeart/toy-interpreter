package mal;
import mal.Types.*;

public class Printer {
    static void prStr(MalType malType){
        /*if(malType instanceof MalList){
            System.out.println(((MalList) malType).malTypeList);
        }else if (malType instanceof Types.MalInt){
            System.out.println(((MalInt) malType).value);
        }else if (malType instanceof Types.MalSymbol){
            System.out.println(((MalSymbol) malType).value);
        }else{
            System.out.println("undefined");
        }*/
        System.out.println(malType);
    }



    public static void println(String msg){
        System.out.println(msg);
    }
}
