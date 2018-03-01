package mal;

import java.util.*;

import static mal.Reader.readStr;
import static mal.Types.MalType;

/**
 * @author xck
 */
public class Step2_eval {
    static MalType READ(String exp){
        return readStr(exp);
    }
    static MalType EVAL(MalType exp,Map env){
        MalType malType;
        if(exp instanceof Types.MalList){
            List tempList = ((Types.MalList)exp).malTypeList;
            Types.ILambda f = (Types.ILambda) (env.get(((Types.MalSymbol)tempList.get(0)).value));
            MalType tempList1 = new Types.MalList(tempList.subList(1,tempList.size()));
            malType = evalAst(tempList1,env);
            return f.apply((Types.MalList) malType);
        }else if (exp == null){
            return exp;
        }else {
            return evalAst(exp,env);
        }
    }
    static void PRINT(MalType exp){
         Printer.prStr(exp);
    }
    static void rep(String exp,Map env){
         PRINT(EVAL(READ(exp),env));

    }
    static abstract class MalFunc extends MalType implements Types.ILambda {
        public abstract MalType apply(Types.MalList a);
    }
    static MalFunc add = new MalFunc() {
        @Override
        public MalType apply(Types.MalList a) {
            return new Types.MalInt(((Types.MalInt)a.malTypeList.get(0)).value + ((Types.MalInt)a.malTypeList.get(1)).value);
        }
    };
    static MalFunc mul = new MalFunc() {
        @Override
        public MalType apply(Types.MalList a) {
            return new Types.MalInt(((Types.MalInt)a.malTypeList.get(0)).value * ((Types.MalInt)a.malTypeList.get(1)).value);
        }
    };
    static MalFunc div = new MalFunc() {
        @Override
        public MalType apply(Types.MalList a) {
            return new Types.MalInt(((Types.MalInt)a.malTypeList.get(0)).value / ((Types.MalInt)a.malTypeList.get(1)).value);
        }
    };
    static MalFunc plus = new MalFunc() {
        @Override
        public MalType apply(Types.MalList a) {
            return new Types.MalInt(((Types.MalInt)a.malTypeList.get(0)).value - ((Types.MalInt)a.malTypeList.get(1)).value);
        }
    };

    static MalType evalAst(Types.MalType malType, Map environment){
        if(malType instanceof Types.MalList){
            Types.MalList vals = new Types.MalList();
            Types.MalList exprs = (Types.MalList)malType;
            for (int i = 0; i < exprs.malTypeList.size(); i++) {
                vals.malTypeList.add(EVAL(exprs.malTypeList.get(i),environment));
            }
            return vals;
        }else if(malType instanceof Types.MalSymbol){
            return (MalType) environment.get(malType);
        }else{
            return malType;
        }
    }

    public static void main(String[] args) {
        Map<String,Object> env = new HashMap<>();
        env.put("+",add);
        env.put("*",mul);
        env.put("/",div);
        env.put("-",plus);
        while(true){
            System.out.print("user>");
            Scanner s = new Scanner(System.in);
            String exp = "";
            exp += s.nextLine();
            if("EOF".equals(exp)){
                System.exit(1);
            }else{
                rep(exp,env);
            }
        }
    }
}
