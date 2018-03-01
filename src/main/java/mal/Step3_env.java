package mal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static mal.Reader.readStr;
import static mal.Types.MalType;

/**
 * @author xck
 */
public class Step3_env {

    static MalFunc add = a -> new Types.MalInt(((Types.MalInt) a.malTypeList.get(0)).value + ((Types.MalInt) a.malTypeList.get(1)).value);
    static MalFunc mul = a -> new Types.MalInt(((Types.MalInt) a.malTypeList.get(0)).value * ((Types.MalInt) a.malTypeList.get(1)).value);
    static MalFunc div = a -> new Types.MalInt(((Types.MalInt) a.malTypeList.get(0)).value / ((Types.MalInt) a.malTypeList.get(1)).value);
    static MalFunc plus = a -> new Types.MalInt(((Types.MalInt) a.malTypeList.get(0)).value - ((Types.MalInt) a.malTypeList.get(1)).value);


    static MalType READ(String exp) {
        return readStr(exp);
    }

    static MalType EVAL(MalType exp, Env.Environment env) throws Exception {
        MalType malType = null;
        if (exp instanceof Types.MalList) {
            List tempList = ((Types.MalList) exp).malTypeList;
            MalType symbol = (MalType) tempList.get(0);
            String a0sym = symbol instanceof Types.MalSymbol ? ((Types.MalSymbol)symbol).value
                    : "lambda";
            switch (a0sym) {
                case "do":
                    MalType malDo = null;
                    for (int i = 1; i < tempList.size(); i++) {
                        malDo = EVAL((MalType) tempList.get(i), env);
                    }
                    return malDo;
                case "if":
                    MalType valEnable = (MalType) tempList.get(1);
                    Types.MalConstant enable = (Types.MalConstant) EVAL(valEnable, env);
                    if(enable.constant.equals("true")){
                        MalType valTrue = (MalType) tempList.get(2);
                        return EVAL(valTrue, env);
                    }else{
                        MalType valFalse = (MalType) tempList.get(3);
                        if(valFalse == null){
                            return new Types.MalConstant("nil");
                        }else{
                            return EVAL(valFalse, env);
                        }
                    }
                case "lambda":
                    Types.MalList valBinds = (Types.MalList) tempList.get(0);
                    MalType valExprs = (MalType) tempList.get(1);
                    return new MalFunc() {
                        @Override
                        public MalType apply(Types.MalList a) throws Exception {
                            return EVAL(valExprs, new Env.Environment(env, valBinds, a));
                        }
                    };
                case "def!":
                    Types.MalSymbol variable = (Types.MalSymbol) tempList.get(1);
                    MalType val = (MalType) tempList.get(2);
                    MalType malVal = EVAL(val, env);
                    env.set(variable, malVal);
                    return malVal;
                case "let*":
                    MalType envVariable = (MalType) tempList.get(1);
                    MalType envTempVal = (MalType) tempList.get(2);
                    Env.Environment newEnv = new Env.Environment(env);
                    for (int i = 0; i < ((Types.MalList)envVariable).malTypeList.size() ; i = i + 2) {
                        Types.MalSymbol envVariable1 = (Types.MalSymbol) ((Types.MalList)envVariable).malTypeList.get(i);
                        MalType envTempVal1 = ((Types.MalList)envVariable).malTypeList.get(i + 1);
                        if(((Types.MalList)envVariable).malTypeList.get(i + 1) instanceof Types.MalList){
                            Types.MalList temp = (Types.MalList)envTempVal1 ;
                            envTempVal1 = EVAL(temp, newEnv);
                        }
                        newEnv.set(envVariable1, envTempVal1);
                    }
                    return EVAL(envTempVal,newEnv);
                default:
                    Types.ILambda f = (Types.ILambda) (env.get((Types.MalSymbol) symbol));
                    MalType tempList2 = new Types.MalList(tempList.subList(1, tempList.size()));
                    MalType malFuncVal = evalAst(tempList2, env);
                    malType = f.apply((Types.MalList) malFuncVal);
            }
            return malType;
        } else if (exp == null) {
            return exp;
        } else {
            return evalAst(exp, env);
        }
    }

    static void PRINT(MalType exp) {
        Printer.prStr(exp);
    }

    static void rep(String exp, Env.Environment env) throws Exception {
        PRINT(EVAL(READ(exp), env));

    }

    static MalType evalAst(MalType malType, Env.Environment environment) throws Exception {
        if (malType instanceof Types.MalList) {
            Types.MalList vals = new Types.MalList();
            Types.MalList exprs = (Types.MalList) malType;
            for (int i = 0; i < exprs.malTypeList.size(); i++) {
                vals.malTypeList.add(EVAL(exprs.malTypeList.get(i), environment));
            }
            return vals;
        } else if (malType instanceof Types.MalSymbol) {
            return (MalType) environment.get((Types.MalSymbol) malType);
        } else {
            return malType;
        }
    }

    public static void main(String[] args) {
        Map<String, MalType> env = new HashMap<>();
        env.put("+", add);
        env.put("*", mul);
        env.put("/", div);
        env.put("-", plus);
        Env.Environment root = new Env.Environment(null, env);
        while (true) {
            try {
                System.out.print("user>");
                Scanner s = new Scanner(System.in);
                String exp = "";
                exp += s.nextLine();
                if ("EOF".equals(exp)) {
                    System.exit(1);
                } else {
                    rep(exp, root);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FunctionalInterface
    interface MalFunc extends MalType, Types.ILambda {
        MalType apply(Types.MalList a) throws Exception;
    }
}
