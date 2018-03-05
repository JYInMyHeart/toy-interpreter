package mal;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import static mal.Core.ns;
import static mal.Reader.readStr;
import static mal.Types.*;

/**
 * @author xck
 */
public class Step5_tco {


    private static MalType READ(String exp) {
        return readStr(exp);
    }

    private static MalType EVAL(MalType exp, Env.Environment env) throws Exception {
        MalType malType = null;
        while (true) {
            if (!(exp instanceof MalList)) {
                return evalAst(exp, env);
            }
            MalList ast = (MalList) exp;
            MalType symbol = ast.nth(0);
            String a0sym = symbol instanceof MalSymbol ? ((MalSymbol) symbol).value
                    : "__<*lambda*>__";
            switch (a0sym) {
                case "do":
                    evalAst(ast.slice(1, ast.size() - 1), env);
                    exp = ast.nth(ast.size() - 1);
                    break;
                case "if":
                    MalType valEnable = ast.nth(1);
                    MalType valTrue = ast.nth(2);
                    MalBoolean enable = (MalBoolean) EVAL(valEnable, env);
                    if (enable.value) {
                        exp = valTrue;
                    } else {
                        MalType valFalse = ast.nth(3);
                        if (valFalse == null) {
                            return new MalString("nil");
                        } else {
                            exp = valFalse;
                        }
                    }
                    break;
                case "lambda":
                    MalList valBinds = (MalList) ast.nth(1);
                    MalType valExprs = ast.nth(2);
                    Env.Environment curEnv = env;
                    return new MalFunc(valExprs, curEnv, valBinds) {
                        @Override
                        public MalType apply(MalList a) throws Exception {
                            return EVAL(valExprs, new Env.Environment(curEnv, valBinds, a));
                        }
                    };
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
                    exp = envTempVal;
                    env = newEnv;
                    break;
                default:
                    MalList malFuncVal = (MalList) evalAst(ast, env);
                    MalFunc f = (MalFunc) malFuncVal.nth(0);
                    MalType fast = f.getAst();
                    if (fast != null) {
                        exp = fast;
                        env = f.getEnv(malFuncVal.slice(1));
                    } else {
                        return f.apply(malFuncVal.slice(1));
                    }

            }

        }
    }

    private static void PRINT(MalType exp) {
        Printer.prStr(exp);
    }

    private static void rep(String exp, Env.Environment env) throws Exception {
        PRINT(EVAL(READ(exp), env));

    }

    private static MalType evalAst(MalType malType, Env.Environment environment) throws Exception {
        if (malType instanceof MalList) {
            MalList vals = ((MalList) malType).list_Q() ? new MalList() : new MalVector();
            MalList expr = (MalList) malType;

            for (int i = 0; i < expr.size(); i++) {
                vals.malTypeList.add(EVAL(expr.nth(i), environment));
            }
            return vals;
        } else if (malType instanceof MalSymbol) {
            return environment.get((MalSymbol) malType);
        } else if (malType instanceof MalHashMap) {
            MalHashMap map = new MalHashMap();
            for (Map.Entry<MalType, MalType> e : ((MalHashMap) malType).entrySet()) {
                map.put(e.getKey(), EVAL(e.getValue(), environment));
            }
            return map;
        } else {
            return malType;
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        command();
    }

    private static void execuateFile() {
        Env.Environment root = new Env.Environment(null, ns);
        String exp = readFile("D://development/sublime/workspace/racket/xck.rkt");
        try {
            rep(exp, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void command() {
        Env.Environment root = new Env.Environment(null, ns);
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
                e.printStackTrace();
                System.err.println(e);
            }

        }
    }

    private static String readFile(String fileName) {
        File file = new File(fileName);
        StringBuffer buffer = new StringBuffer();
        try (InputStream in = new FileInputStream(file)) {
            int c;
            while (((c = in.read()) != -1)) {
                buffer.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


}
