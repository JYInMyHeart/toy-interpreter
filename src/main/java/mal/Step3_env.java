package mal;


import java.util.HashMap;

import java.util.Map;
import java.util.Scanner;

import static mal.Core.*;
import static mal.Reader.readStr;
import static mal.Types.*;

/**
 * @author xck
 */
public class Step3_env {


    static MalType READ(String exp) {
        return readStr(exp);
    }

    static MalType EVAL(MalType exp, Env.Environment env) throws Exception {
        MalType malType = null;
        if (exp instanceof MalList) {
            //List tempList = ((MalList) exp).malTypeList;
            MalList ast = (MalList) exp;
            MalType symbol = ast.nth(0);
            String a0sym = symbol instanceof MalSymbol ? ((MalSymbol) symbol).value
                    : "__<*lambda*>__";
            switch (a0sym) {
                case "do":
                    MalType malDo = null;
                    for (int i = 1; i < ast.size(); i++) {
                        malDo = EVAL(ast.nth(i), env);
                    }
                    return malDo;
                case "if":
                    MalType valEnable = ast.nth(1);
                    MalBoolean enable = (MalBoolean) EVAL(valEnable, env);
                    if (enable.value) {
                        MalType valTrue = ast.nth(2);
                        return EVAL(valTrue, env);
                    } else {
                        MalType valFalse = ast.nth(3);
                        if (valFalse == null) {
                            return new MalConstant("nil");
                        } else {
                            return EVAL(valFalse, env);
                        }
                    }
                case "lambda":
                    MalList valBinds = (MalList) ast.nth(1);
                    MalType valExprs = ast.nth(2);
                    return (MalFunc) a -> EVAL(valExprs, new Env.Environment(env, valBinds, a));
                case "def!":
                    MalSymbol variable = (MalSymbol) ast.nth(1);
                    MalType val = ast.nth(2);
                    MalType malVal = EVAL(val, env);
                    env.set(variable, malVal);
                    return malVal;
                case "let*":
                    MalType envVariable = ast.nth(1);
                    MalType envTempVal = ast.nth(2);
                    Env.Environment newEnv = new Env.Environment(env);
                    for (int i = 0; i < ((MalList) envVariable).size(); i = i + 2) {
                        MalSymbol envVariable1 = (MalSymbol) ((MalList) envVariable).nth(i);
                        MalType envTempVal1 = ((MalList) envVariable).nth(i + 1);
                        if (((MalList) envVariable).nth(i + 1) instanceof MalList) {
                            MalList temp = (MalList) envTempVal1;
                            envTempVal1 = EVAL(temp, newEnv);
                        }
                        newEnv.set(envVariable1, envTempVal1);
                    }
                    return EVAL(envTempVal, newEnv);
                default:
                    MalList malFuncVal = (MalList) evalAst(ast, env);
                    ILambda f = (ILambda) malFuncVal.nth(0);
                    malType = f.apply(malFuncVal.slice(1));
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
        if (malType instanceof MalList) {
            MalList vals = ((MalList) malType).list_Q() ? new MalList() : new MalVector();
            MalList exprs = (MalList) malType;

            for (int i = 0; i < exprs.size(); i++) {
                vals.malTypeList.add(EVAL(exprs.nth(i), environment));
            }
            return vals;
        } else if (malType instanceof MalSymbol) {
            return  environment.get((MalSymbol) malType);
        } else {
            return malType;
        }
    }

    public static void main(String[] args) throws Exception {
        Env.Environment root = new Env.Environment(null, Core.ns);
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
                System.out.println(e);
            }

        }
    }


}
